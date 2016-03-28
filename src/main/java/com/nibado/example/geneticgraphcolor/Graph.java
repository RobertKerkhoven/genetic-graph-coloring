package com.nibado.example.geneticgraphcolor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

import java.util.Arrays;
import java.util.List;

public class Graph {
    private List<Coordinate> coordinates;
    private List<Edge> edges;
    private List<Polygon> polygons;

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public static class Edge {
        public final Coordinate from;
        public final Coordinate to;

        public Edge(Coordinate from, Coordinate to) {
            this.from = from;
            this.to = to;
        }

        public static Edge fromLineString(LineString lineString) {
            return new Edge(lineString.getCoordinateN(0), lineString.getCoordinateN(1));
        }
    }

    public static class Polygon {
        public final List<Coordinate> coordinates;

        public Polygon(List<Coordinate> coordinates) {
            this.coordinates = coordinates;
        }

        public static Polygon fromPolygon(com.vividsolutions.jts.geom.Polygon p) {
            return new Polygon(Arrays.asList(p.getCoordinates()));
        }
    }
}
