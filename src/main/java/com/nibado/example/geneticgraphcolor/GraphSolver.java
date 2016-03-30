package com.nibado.example.geneticgraphcolor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GraphSolver implements Runnable {
    private final static Random RANDOM = new Random();
    private final static Logger LOG = LoggerFactory.getLogger(GraphSolver.class);

    @Autowired
    private SimpMessagingTemplate broker;

    private Graph graph;
    private SolverState state = SolverState.READY;
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
        state = SolverState.RUNNING;
        updateStatus();
        while(true) {
            int prevUnique = unique;
            shuffleIndices();
            update(colorIndices, unique, graph.getCoordinates().size());
            unique = Math.max(unique - unique / 10, 1);
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e){}

            if(prevUnique == unique) {
                break;
            }
        }
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

    public void update(int[] colors, int uniqueColors, int startingColors) {
        LOG.debug("Update(unique: {})", uniqueColors);
        broker.convertAndSend("/topic/update", new UpdateMessage(colors, uniqueColors, startingColors));
    }

    public void updateStatus() {
        LOG.debug("updateStatus(): " + state);
        broker.convertAndSend("/topic/status", new WebSocketController.Status(state));
    }

    public static class UpdateMessage {
        public int[] colors;
        public int uniqueColors;
        public int startingColors;
        public long time;

        public UpdateMessage(int[] colors, int uniqueColors, int startingColors) {
            this.colors = colors;
            this.uniqueColors = uniqueColors;
            this.startingColors = startingColors;
            this.time = System.currentTimeMillis();
        }
    }
}
