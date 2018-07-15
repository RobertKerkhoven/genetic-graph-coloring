package com.nibado.example.geneticgraphcolor;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

public class ColorFactory {
    private static final List<Color> BASE_COLORS = Arrays.asList(
            new Color(230, 25, 75),
            new Color(60, 180, 75),
            new Color(255, 225, 25),
            new Color(0, 130, 200),
            new Color(245, 130, 48),
            new Color(145, 30, 180),
            new Color(70, 240, 240),
            new Color(240, 50, 230),
            new Color(210, 245, 60),
            new Color(250, 190, 190),
            new Color(0, 128, 128),
            new Color(230, 190, 255),
            new Color(170, 110, 40),
            new Color(255, 250, 200),
            new Color(128, 0, 0),
            new Color(170, 255, 195),
            new Color(128, 128, 0),
            new Color(255, 215, 180),
            new Color(0, 0, 128),
            new Color(128, 128, 128),
            new Color(255, 255, 255),
            new Color(0, 0, 0)
    );

    private static final Random RANDOM = new Random();

    public List<Color> create(int number) {
        List<Color> colors = new ArrayList<>(number);
        Set<Color> colorSet = new HashSet<>();
        if (number <= BASE_COLORS.size()) {
            return BASE_COLORS.subList(0, number);
        }

        colors.addAll(BASE_COLORS);
        colorSet.addAll(BASE_COLORS);

        while (colors.size() < number) {
            Color c = new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
            if (!colorSet.contains(c)) {
                colors.add(c);
                colorSet.add(c);
            }
        }

        return colors;
    }

    public List<String> createStrings(int number) {
        return create(number).stream().map(ColorFactory::mapToString).collect(Collectors.toList());
    }

    public static String mapToString(Color c) {
        return String.format(Locale.ROOT, "rgb(%s,%s,%s)", c.getRed(), c.getGreen(), c.getBlue());
    }
}
