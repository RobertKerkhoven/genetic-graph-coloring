package com.nibado.example.geneticgraphcolor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatusController {
    private final static Logger LOG = LoggerFactory.getLogger(StatusController.class);

    @Autowired
    private GraphFactory graphFactory;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Graph createGraph(@RequestBody List<Coordinate> coordinates) {
        LOG.debug("Create(size={})", coordinates.size());

        return graphFactory.createGraph(coordinates);
    }

    @RequestMapping(value = "/random", method = RequestMethod.POST)
    public Graph random(@RequestBody RandomRequest request) {
        LOG.debug("Random(points={}, w={}, h={})", request.points, request.width, request.height);

        return graphFactory.randomGraph(request.points, request.width, request.height);
    }

    private static class RandomRequest {
        public int points;
        public int height;
        public int width;
    }
}
