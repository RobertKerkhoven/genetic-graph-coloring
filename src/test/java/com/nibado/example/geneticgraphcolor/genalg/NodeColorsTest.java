package com.nibado.example.geneticgraphcolor.genalg;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeColorsTest {
    private static List<int[]> ADJECENCY_LIST = Solver.buildAdjacencyList(SolverTest.testGraphSmall());
    @Test
    public void testGetColors() throws Exception {
        NodeColors colors = new NodeColors(new int[] {1, 2, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.getColors()).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void testIsValid() throws Exception {
        NodeColors colors = new NodeColors(new int[] {1, 2, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.isValid()).isTrue();

        colors = new NodeColors(new int[] {1, 1, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.isValid()).isFalse();

        colors = new NodeColors(new int[] {1, 2, 2, 4}, ADJECENCY_LIST);
        assertThat(colors.isValid()).isFalse();
    }

    @Test
    public void testGetColorSet() throws Exception {
        NodeColors colors = new NodeColors(new int[] {1, 2, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.getColorSet()).containsExactly(1, 2, 3, 4);

        colors = new NodeColors(new int[] {1, 1, 2, 2, 3, 3, 4, 4, 4, 4}, ADJECENCY_LIST);
        assertThat(colors.getColorSet()).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void testScore() {
        NodeColors colors = new NodeColors(new int[] {1, 2, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.getScore()).isEqualTo(4);

        colors = new NodeColors(new int[] {1, 1, 3, 4}, ADJECENCY_LIST);
        assertThat(colors.getScore()).isEqualTo(Integer.MAX_VALUE);
    }
}