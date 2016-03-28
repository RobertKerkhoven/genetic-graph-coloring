package com.nibado.example.geneticgraphcolor;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.DelaunayTriangulationBuilder;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphPanel extends JPanel {
    private static final Random RANDOM = new Random();
    private List<Coordinate> coordinateList;
    private List<LineString> edges;
    private List<Polygon> polygons;
    private List<Color> colors;

    public GraphPanel() {
        setSize(800, 600);
        setPreferredSize(getSize());

        coordinateList = new ArrayList<>();
        for(int i = 0; i < 250;i++) {
            coordinateList.add(new Coordinate(RANDOM.nextInt(800), RANDOM.nextInt(600)));
        }
        edges = buildGraph();
        polygons = buildVorenoi();
        colors = new ArrayList<>(polygons.size());
        for(int i = 0;i < polygons.size();i++) {
            colors.add(Color.getHSBColor((float)i / (float)polygons.size(), 1.0f, 1.0f));
        }
        //Collections.shuffle(colors);
    }
    @Override
    public void paintComponent(Graphics g) {
        fillPolygons(g, polygons, colors);
        g.setColor(Color.black);
        for(Coordinate c : coordinateList) {
            g.fillOval((int)c.x - 4, (int)c.y - 4, 8, 8);
        }
        for(LineString edge : edges) {
            drawLine(g, edge);
        }
        g.setColor(Color.BLUE);
        for(Polygon p : polygons) {
            drawPolygon(g, p);
        }
    }

    private List<LineString> buildGraph() {
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(coordinateList);

        return edgesFrom(builder);
    }

    private List<Polygon> buildVorenoi() {
        VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setSites(coordinateList);

        return polygonsFrom(builder);
    }

    private static void drawLine(Graphics g, LineString line) {
        Coordinate c0 = line.getCoordinateN(0);
        Coordinate c1 = line.getCoordinateN(1);

        g.drawLine((int)c0.x, (int)c0.y, (int)c1.x, (int)c1.y);
    }

    private static void drawPolygon(Graphics g, Polygon p) {
        Coordinate[] coords = p.getCoordinates();

        for(int i = 0;i < coords.length - 1;i++) {
            g.drawLine((int)coords[i].x, (int)coords[i].y, (int)coords[i + 1].x, (int)coords[i + 1].y);
        }
    }

    private static void fillPolygons(Graphics g, List<Polygon> polygons, List<Color> colors) {
        for(int i = 0;i < polygons.size();i++) {
            g.setColor(colors.get(i));
            g.fillPolygon(toAwtPolygon(polygons.get(i)));
        }
    }

    private static java.awt.Polygon toAwtPolygon(Polygon p) {
        java.awt.Polygon awtPolygon = new java.awt.Polygon();
        for(Coordinate c : p.getCoordinates()) {
            awtPolygon.addPoint((int)c.x, (int)c.y);
        }

        return awtPolygon;
    }

    private static List<LineString> edgesFrom(DelaunayTriangulationBuilder builder) {
        GeometryCollection geom = (GeometryCollection) builder.getEdges(new GeometryFactory());
        List<LineString> lines = new ArrayList<>(geom.getNumGeometries());

        for(int i = 0;i < geom.getNumGeometries();i++) {
            lines.add((LineString) geom.getGeometryN(i));
        }

        return lines;
    }

    private static List<Polygon> polygonsFrom(VoronoiDiagramBuilder builder) {
        GeometryCollection geom = (GeometryCollection) builder.getDiagram(new GeometryFactory());
        List<Polygon> polygons = new ArrayList<>(geom.getNumGeometries());

        for(int i = 0;i < geom.getNumGeometries();i++) {
            polygons.add((Polygon) geom.getGeometryN(i));
        }

        return polygons;
    }
}
