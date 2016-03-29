package com.nibado.example.geneticgraphcolor;

import org.junit.Test;

import java.awt.Color;

import static com.nibado.example.geneticgraphcolor.ColorFactory.mapToString;
import static org.assertj.core.api.Assertions.assertThat;

public class ColorFactoryTest {

    @Test
    public void testCreate() throws Exception {
        ColorFactory cf = new ColorFactory();
        assertThat(cf.create(1)).hasSize(1);
        assertThat(cf.create(8)).hasSize(8);
        assertThat(cf.create(16)).hasSize(16);
        assertThat(cf.create(1000)).hasSize(1000);
    }

    @Test
    public void testMapToString() throws Exception {
        assertThat(mapToString(new Color(0, 0, 0))).isEqualTo("rgb(0,0,0)");
        assertThat(mapToString(new Color(127, 0, 0))).isEqualTo("rgb(127,0,0)");
        assertThat(mapToString(new Color(0, 127, 0))).isEqualTo("rgb(0,127,0)");
        assertThat(mapToString(new Color(0, 0, 127))).isEqualTo("rgb(0,0,127)");
        assertThat(mapToString(new Color(127, 127, 127))).isEqualTo("rgb(127,127,127)");
        assertThat(mapToString(new Color(255, 0, 0))).isEqualTo("rgb(255,0,0)");
        assertThat(mapToString(new Color(0, 255, 0))).isEqualTo("rgb(0,255,0)");
        assertThat(mapToString(new Color(0, 0, 255))).isEqualTo("rgb(0,0,255)");
        assertThat(mapToString(new Color(255, 255, 255))).isEqualTo("rgb(255,255,255)");
    }
}