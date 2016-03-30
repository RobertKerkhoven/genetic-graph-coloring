package com.nibado.example.geneticgraphcolor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {
    private final static Logger LOG = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private GraphFactory graphFactory;

    @Autowired
    private ColorFactory colorFactory;

    @Autowired
    private GraphSolver graphSolver;

    @Autowired
    private SimpMessagingTemplate broker;

    private List<String> colors;

    @MessageMapping("/start")
    @SendTo("/topic/status")
    public Status command(StartCommand command) throws Exception {
        LOG.debug("Start(points={}, width={}, height={})", command.points, command.width, command.height);
        if(graphSolver.getState() == GraphSolver.SolverState.RUNNING) {
            LOG.info("Start: Already running");
            return new Status(graphSolver.getState());
        }
        Graph graph = graphFactory.randomGraph(command.points, command.width, command.height);

        colors = colorFactory.createStrings(command.points);

        broker.convertAndSend("/topic/graph", new GraphInfo(graph, colors));

        graphSolver.start(graph);

        return new Status(graphSolver.getState());
    }

    @MessageMapping("/graph")
    @SendTo("/topic/graph")
    public GraphInfo getGraph() {
        LOG.debug("getGraph()");
        return new GraphInfo(graphSolver.getGraph(), colors);
    }

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public Status getStatus() {
        LOG.debug("getStatus(): {}", graphSolver.getState());
        return new Status(graphSolver.getState());
    }

    public static class StartCommand {
        public int points;
        public int width;
        public int height;
    }

    public static class Status {
        public final String status;

        public Status(GraphSolver.SolverState state) {
            this(state.toString());
        }

        public Status(String status) {
            this.status = status;
        }
    }

    public static class GraphInfo {
        public final Graph graph;
        public final List<String> colors;

        public GraphInfo(Graph graph, List<String> colors) {
            this.colors = colors;
            this.graph = graph;
        }
    }
}
