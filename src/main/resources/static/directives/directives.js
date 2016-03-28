var appModule = angular.module('myApp');

appModule.directive('graph', ['mainService', function(mainService) {
    return {
         restrict: 'E',
         templateUrl: 'directives/graph.html',

         scope: {
            data:"="
         },
         controller: function($scope) {
             function drawGraph(graph) {
                 var canvas = document.getElementById('canvas');
                 var ctx = canvas.getContext('2d');

                 angular.forEach(graph.polygons, function(p) {
                    drawPolygon(ctx, p);
                 });

                ctx.fillStyle = "black";
                 angular.forEach(graph.coordinates, function(c) {
                    ctx.beginPath();
                    ctx.arc(c.x,c.y, 4, 0, Math.PI*2, true); // Outer circle
                    ctx.fill();
                 });
             }

             function drawPolygon(ctx, polygon) {
                ctx.fillStyle = "orange";
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

             $scope.$on('new-data', function(scope, argument) {
                 drawGraph(argument);
             }, true);
         }

    };
}]);