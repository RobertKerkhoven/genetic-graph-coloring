var appModule = angular.module('myApp', []);

appModule.controller('MainCtrl', ['mainService','$scope', '$interval', function(mainService, $scope, $interval) {
    $scope.title = 'Welcome to the Genetic Graph Coloring Demo!';

    mainService.random(1000, 800, 600).then(function(graph) {
        $scope.graph = graph;

        $scope.$root.$broadcast('new-data', graph);
    });
}]);

appModule.service('mainService', function($http) {
    return {
         create : function(coordinates) {
            return $http.post('/create', coordinates).then(function(response) {
                return response.data;
            });
        },

        random : function(points, width, height) {
            return $http.post('/random', {points:points, width: width, height: height}).then(function(response) {
                return response.data;
            });
        }
    };
});