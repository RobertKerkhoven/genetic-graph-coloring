package com.nibado.example.geneticgraphcolor;

import com.nibado.example.geneticgraphcolor.genalg.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class GraphSolver implements Runnable {
    private final static Random RANDOM = new Random();
    private final static Logger LOG = LoggerFactory.getLogger(GraphSolver.class);

    @Autowired
    private SimpMessagingTemplate broker;

    private Graph graph;
    private SolverState state = SolverState.READY;
    private Solver solver;
    private Thread thread;
    private int[] colorIndices;
    private int unique;

    public void start(Graph graph) {
        LOG.debug("Start(graph size: {})", graph.getCoordinates().size());
        this.graph = graph;
        this.colorIndices = new int[graph.getCoordinates().size()];
        unique = colorIndices.length;

        for(int i = 0;i < colorIndices.length;i++) {
            colorIndices[i] = i;
        }

        thread = new Thread(this, "GraphSolver");
        thread.start();
    }

    public SolverState getState() {
        return state;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public void run() {
        solver = new Solver(graph);
        solver.setUpdateConsumer((gen, c) -> update(gen, c.getColors(), c.getScore(), graph.getCoordinates().size()));
        state = SolverState.RUNNING;
        updateStatus();
        solver.solve(10000);
        state = SolverState.FINISHED;
        updateStatus();
    }

    private void shuffleIndices() {
        for(int i = 0;i < colorIndices.length;i++) {
            int other = i;
            while(other == i) {
                other = RANDOM.nextInt(colorIndices.length);
            }

            int temp = colorIndices[i];
            colorIndices[i] = colorIndices[other];
            colorIndices[other] = temp;
        }
    }

    public enum SolverState {
        READY,
        RUNNING,
        FINISHED
    }

    public void update(int generation, int[] colors, int uniqueColors, int startingColors) {
        LOG.debug("Update(unique: {})", uniqueColors);
        shiftColors(colors);
        broker.convertAndSend("/topic/update", new UpdateMessage(generation, colors, uniqueColors, startingColors));
    }

    private static void shiftColors(int[] colors) {
        int index = 0;
        Map<Integer, Integer> colorMap = new HashMap<>();
        for(int i = 0;i < colors.length;i++) {
            if(!colorMap.containsKey(colors[i])) {
                colorMap.put(colors[i], index);
                colors[i] = index;
                index++;
            }
            else {
                colors[i] = colorMap.get(colors[i]);
            }
        }
    }

    public void updateStatus() {
        LOG.debug("updateStatus(): " + state);
        broker.convertAndSend("/topic/status", new WebSocketController.Status(state));
    }

    public static class UpdateMessage {
        public final int[] colors;
        public final int uniqueColors;
        public final int startingColors;
        public final int generation;
        public final long time;

        public UpdateMessage(int generation, int[] colors, int uniqueColors, int startingColors) {
            this.generation = generation;
            this.colors = colors;
            this.uniqueColors = uniqueColors;
            this.startingColors = startingColors;
            this.time = System.currentTimeMillis();
        }
    }
}
