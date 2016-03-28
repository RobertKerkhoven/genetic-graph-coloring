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

                 angular.forEach(graph.coordinates, function(c) {
                    ctx.beginPath();
                    ctx.arc(c.x,c.y, 8, 0, Math.PI*2, true); // Outer circle
                    ctx.stroke();
                 });
             }

             $scope.$on('new-data', function(scope, argument) {
                 drawGraph(argument);
             }, true);
         }

    };
}]);