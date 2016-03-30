package com.nibado.example.geneticgraphcolor.genalg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NodeColors {
    private int[] colors;
    private Set<Integer> colorSet;
    private boolean isValid;
    private boolean checked = false;
    private int score = -1;
    private List<int[]> adjacency;

    public NodeColors(int[] colors, List<int[]> adjacency) {
        this.colors = colors;
        this.adjacency = adjacency;
    }

    public int[] getColors() {
        return colors;
    }

    public boolean isValid() {
        if(!checked) {
            for(int from = 0;from < adjacency.size();from++) {
                for(int to : adjacency.get(from)) {
                    if(colors[from] == colors[to]) {
                        checked = true;
                        isValid = false;
                        return isValid;
                    }
                }
            }
            checked = true;
            isValid = true;
        }

        return isValid;
    }

    public Set<Integer> getColorSet() {
        if(colorSet == null) {
            colorSet = new HashSet<>();
            for(int c : colors) {
                colorSet.add(c);
            }
        }

        return colorSet;
    }

    public int getScore() {
        if(score == -1) {
            if(!isValid()) {
                score = Integer.MAX_VALUE;
            }
            else {
                score = getColorSet().size();
            }
        }

        return score;
    }
}
