package com.nibado.example.geneticgraphcolor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ColorFactory {
    private static final int BASE_COLORS = 16;
    private static final Random RANDOM = new Random();
    private List<Color> baseColors;

    public ColorFactory() {
        baseColors = new ArrayList<>(16);

        for(int i = 0;i < BASE_COLORS;i++) {
            float h = (float)i / (float)BASE_COLORS;
            baseColors.add(Color.getHSBColor(h, 1.0f, 1.0f));
        }
    }

    public List<Color> create(int number) {
        List<Color> colors = new ArrayList<>(number);
        Set<Color> colorSet = new HashSet<>();
        if(number <= BASE_COLORS) {
            return baseColors.subList(0, number);
        }

        colors.addAll(baseColors);
        colorSet.addAll(baseColors);

        while(colors.size() < number) {
            Color c = new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
            if(!colorSet.contains(c)) {
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
