package com.nibado.example.geneticgraphcolor;

import com.vividsolutions.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final static Logger LOG = LoggerFactory.getLogger(RestController.class);

    @Autowired
    private GraphFactory graphFactory;

    @Autowired
    private ColorFactory colorFactory;

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

    @RequestMapping(value = "/colors/{number}", method = RequestMethod.GET)
    public List<String> createColors(@PathVariable int number) {
        return colorFactory.createStrings(number);
    }

    private static class RandomRequest {
        public int points;
        public int height;
        public int width;
    }
}
