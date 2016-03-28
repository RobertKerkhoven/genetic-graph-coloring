package com.nibado.example.geneticgraphcolor;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.DelaunayTriangulationBuilder;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GraphFactory {
    private static final Random RANDOM = new Random();
    public Graph randomGraph(int points, int width, int height) {
        List<Coordinate> coordinates = new ArrayList<>(points);

        for(int i = 0;i < points;i++) {
            coordinates.add(new Coordinate(RANDOM.nextInt(width), RANDOM.nextInt(height)));
        }

        return createGraph(coordinates);
    }

    public Graph createGraph(List<Coordinate> coordinates) {
        Graph graph = new Graph();
        graph.setCoordinates(coordinates);

        List<Graph.Edge> edges = buildGraph(coordinates)
                .stream()
                .map(Graph.Edge::fromLineString)
                .collect(Collectors.toList());

        List<Graph.Polygon> vorenoi = buildVorenoi(coordinates)
                .stream()
                .map(Graph.Polygon::fromPolygon)
                .collect(Collectors.toList());

        graph.setEdges(edges);
        graph.setPolygons(vorenoi);

        return graph;
    }

    private static List<LineString> buildGraph(List<Coordinate> coordinates) {
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(coordinates);

        return edgesFrom(builder);
    }

    private static List<Polygon> buildVorenoi(List<Coordinate> coordinates) {
        VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setSites(coordinates);

        return polygonsFrom(builder);
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
        List<com.vividsolutions.jts.geom.Polygon> polygons = new ArrayList<>(geom.getNumGeometries());

        for(int i = 0;i < geom.getNumGeometries();i++) {
            polygons.add((com.vividsolutions.jts.geom.Polygon) geom.getGeometryN(i));
        }

        return polygons;
    }
}
