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

             /*
             $scope.$on('new-data', function(scope, argument) {
                 $scope.drawGraph();
             }, true);
             */

             graphService.addListener($scope.drawGraph);
         }

    };
}]);