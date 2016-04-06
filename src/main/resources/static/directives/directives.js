var appModule = angular.module('myApp');

appModule.directive('graph', ['graphService', function(graphService) {
    return {
         restrict: 'E',
         templateUrl: 'directives/graph.html',

         scope: {
            data:"="
         },
         controller: function($scope) {
             $scope.drawGraph = function() {
                 if(!graphService.hasGraph()) {
                    return;
                 }
                 var graph = graphService.getGraph();

                 var canvas = document.getElementById('canvas');
                 var ctx = canvas.getContext('2d');

                 angular.forEach(graph.polygons, function(p, i) {
                    var color = graphService.getColor(i);
                    $scope.drawPolygon(ctx, p, color);
                 });

                 ctx.fillStyle = 'black';
                 angular.forEach(graph.coordinates, function(c) {
                    ctx.beginPath();
                    ctx.arc(c.x,c.y, 4, 0, Math.PI * 2, true); // Outer circle
                    ctx.fill();
                 });
             }

             $scope.drawPolygon = function(ctx, polygon, color) {
                ctx.fillStyle = color;
                ctx.beginPath();

                angular.forEach(polygon.coordinates, function(c) {
                    ctx.lineTo(c.x, c.y);
                });

                ctx.fill();


                ctx.beginPath();

                angular.forEach(polygon.coordinates, function(c) {
                    ctx.lineTo(c.x, c.y);
                });

                ctx.stroke();

             }

             graphService.addListener($scope.drawGraph);
         }

    };
}]);

appModule.directive('histogram', ['graphService', function(graphService) {
    return {
         restrict: 'E',
         templateUrl: 'directives/histogram.html',

         scope: {
         },
         controller: function($scope) {
             $scope.drawGraph = function() {
                 var dimensions = {width: 800, height: 200};
                 if(!graphService.hasGraph()) {
                    return;
                 }
                 var graph = graphService.getGraph();

                 var canvas = document.getElementById('histogram');
                 var ctx = canvas.getContext('2d');
                 ctx.clearRect(0, 0, 800, 200);

                 var points = [];
                 var maxValues = {x: 10000, y: 0};

                 angular.forEach(graphService.getUpdates(), function(u) {
                    points.push({x: u.generation, y: u.uniqueColors});
                    //maxValues.x = Math.max(maxValues.x, u.generation);
                    maxValues.y = Math.max(maxValues.y, u.uniqueColors);
                 });

                 points = points.map(function(p) {
                    return $scope.scale(p, dimensions, maxValues);
                 });

                 $scope.drawLines(ctx, points);
             }

             $scope.drawLines = function(ctx, points) {
                ctx.beginPath();

                angular.forEach(points, function(c) {
                    ctx.lineTo(c.x, c.y);
                });

                ctx.stroke();
             }

             $scope.scale = function(point, dimensions, maxValues) {
                point.x = point.x / maxValues.x * dimensions.width;
                point.y = dimensions.height - (point.y / maxValues.y * dimensions.height);

                return point;
             }

             graphService.addListener($scope.drawGraph);
         }
    };
}]);