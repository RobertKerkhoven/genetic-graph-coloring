package com.nibado.example.geneticgraphcolor.genalg;

import com.nibado.example.geneticgraphcolor.Graph;
import com.nibado.example.geneticgraphcolor.GraphFactory;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {
    public static Graph testGraphSmall() {
        Graph g = new Graph();
        List<Coordinate> coords = Arrays.asList(new Coordinate(10, 10), new Coordinate(20, 20), new Coordinate(20, 10), new Coordinate(10, 20));
        g.setCoordinates(coords);
        g.setEdges(Arrays.asList(
                new Graph.Edge(coords.get(0), coords.get(1)),
                new Graph.Edge(coords.get(1), coords.get(0)),
                new Graph.Edge(coords.get(1), coords.get(2)),
                new Graph.Edge(coords.get(2), coords.get(1)),
                new Graph.Edge(coords.get(1), coords.get(3)),
                new Graph.Edge(coords.get(3), coords.get(1)),
                new Graph.Edge(coords.get(3), coords.get(2)),
                new Graph.Edge(coords.get(2), coords.get(3))
        ));

        return g;
    }

    public static Graph testGraphBig(int points) {
        return new GraphFactory().randomGraph(points, 1000, 1000);
    }

    @Test
    public void testBuildAdjacencyList() throws Exception {
        Graph g = testGraphSmall();

        List<int[]> list = Solver.buildAdjacencyList(g);

        assertThat(list).hasSize(g.getCoordinates().size());
        assertThat(list.get(0)).containsExactly(1);
        assertThat(list.get(1)).containsExactly(2, 3);
        assertThat(list.get(2)).isEmpty();
        assertThat(list.get(3)).containsExactly(2);
    }

    @Test
    public void testSolve() {
        Solver solver = new Solver(testGraphBig(100));

        solver.solve(10000);
    }
}