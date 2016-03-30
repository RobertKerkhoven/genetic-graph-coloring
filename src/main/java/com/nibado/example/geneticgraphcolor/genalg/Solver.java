package com.nibado.example.geneticgraphcolor.genalg;

import com.nibado.example.geneticgraphcolor.Graph;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Solver {
    private static final int GENERATION_SIZE = 500;
    private static final Random RANDOM = new Random();
    private Graph graph;
    private List<int[]> adjacency;

    private List<NodeColors> generation = new ArrayList<>(GENERATION_SIZE * 2);

    public Solver(Graph graph) {
        this.graph = graph;
    }

    public void solve(int maxGenerations) {
        adjacency = buildAdjacencyList(graph);
        int prevScore = -1;
        for(int i = 0;i < maxGenerations;i++) {
            fillToSize();
            cross(100);
            mutate(100);
            sortList();
            generation = new ArrayList<>(generation.subList(0, GENERATION_SIZE / 2));
            int score = generation.get(0).getScore();
            if(prevScore != score) {
                System.out.printf("%s:\t%s\n", i, generation.get(0).getScore());
                prevScore = score;
            }
        }
    }

    public void fillToSize() {
        while(generation.size() < GENERATION_SIZE) {
            generation.add(createRandom(graph.getCoordinates().size(), adjacency));
        }
    }

    public void cross(int x)  {
        x = Math.min(x, GENERATION_SIZE);
        for(int i = 0;i < x;i++) {
            int[] pair = randomPair();
            NodeColors c = cross(generation.get(pair[0]), generation.get(pair[1]));
            if(c.isValid()) {
                generation.add(c);
            }
        }
    }

    public void mutate(int x) {
        for(int i = 0;i < x;i++) {
            NodeColors c = mutate(generation.get(RANDOM.nextInt(generation.size())));
            if(c.isValid()) {
                generation.add(c);
            }
        }
    }

    public NodeColors cross(NodeColors a, NodeColors b) {
        int[] colorsA = a.getColors();
        int[] colorsB = b.getColors();
        int[] colors = new int[colorsA.length];

        int crossPoint = RANDOM.nextInt(colors.length);
        for(int i = 0;i < colors.length;i++) {
            colors[i] = i <= crossPoint ? colorsA[i] : colorsB[i];
        }

        return new NodeColors(colors, adjacency);
    }

    public NodeColors mutate(NodeColors c) {
        int[] colors = Arrays.copyOf(c.getColors(), c.getColors().length);

        int a = RANDOM.nextInt(colors.length);
        int b = a;
        while(a == b) {
            b = RANDOM.nextInt(colors.length);
        }

        int t = colors[a];
        colors[a] = colors[b];
        colors[b] = t;

        return new NodeColors(colors, adjacency);
    }

    public static List<int[]> buildAdjacencyList(Graph graph) {
        Map<Coordinate, Integer> nodeMap = new HashMap<>();
        List<List<Integer>> list = new ArrayList<>(graph.getCoordinates().size());

        for(int i = 0;i < graph.getCoordinates().size();i++) {
            nodeMap.put(graph.getCoordinates().get(i), i);
            list.add(new ArrayList<>());
        }

        for(Graph.Edge edge : graph.getEdges()) {
            int fromIndex = nodeMap.get(edge.from);
            int toIndex = nodeMap.get(edge.to);

            if(!list.get(toIndex).contains(fromIndex)) {
                list.get(fromIndex).add(toIndex);
            }
        }

        return list.stream().map(Solver::toIntArray).collect(Collectors.toList());
    }

    public static NodeColors createRandom(int num, List<int[]> adjecency) {
        int[] colors = new int[num];
        for(int i = 0;i < num;i++) {
            colors[i] = i;
        }

        shuffle(colors);

        return new NodeColors(colors, adjecency);
    }

    private int[] randomPair() {
        int[] result = {
                RANDOM.nextInt(generation.size()),
                RANDOM.nextInt(generation.size())};

        while(result[0] == result[1]) {
            result[1] = RANDOM.nextInt(generation.size());
        }

        return result;
    }

    private static void shuffle(int[] colorIndices) {
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

    private void sortList() {
        Collections.sort(generation, (a, b) -> Integer.compare(a.getScore(), b.getScore()));
    }

    private static int[] toIntArray(List<Integer> list) {
        return list.stream().mapToInt(i -> i).toArray();
    }

}
