var appModule = angular.module('myApp', []);

appModule.controller('MainCtrl', ['socketService', 'graphService', '$scope', '$interval', function(socketService, graphService, $scope, $interval) {
    $scope.title = 'Welcome to the Genetic Graph Coloring Demo!';

    $scope.cells = 1000;
    $scope.connected = false;

    socketService.addListener(function(status) {
        $scope.status = status;
        $scope.connected = status.status != 'error';

        $scope.$apply();
        if($scope.status != 'READY' && !graphService.hasGraph()) {
            socketService.getGraph();
        }
    });

    socketService.connect(function(message) {
        $scope.message = message;
        graphService.update(message);
        $scope.$apply();
        //$scope.$root.$broadcast('new-data');
        console.log(message);
    });

    $scope.start = function() {
        socketService.start(100, 800, 600);
    }
}]);

appModule.service('graphService', function() {
    var graphInfo = null;
    var indices = null;
    var listeners = [];
    var updates = [];

    return {
        setGraphInfo: function(info) {
            graphInfo = info;
            updates.length = 0;
            this.notifyListeners();
        },

        update: function(message) {
            indices = message.colors;
            updates.push(message);
            this.notifyListeners();
        },

        getGraph: function() {
            return graphInfo.graph;
        },

        getUpdates: function() {
            return updates;
        },

        hasGraph: function() {
            return graphInfo && graphInfo.graph;
        },

        getColor: function(index) {
            var i = index;
            if(indices) {
                i = indices[index];
            }

            return graphInfo.colors[i];
        },

        addListener: function(callback) {
            listeners.push(callback);
        },

        notifyListeners: function() {
            angular.forEach(listeners, function(cb) {
                cb();
            });
        }
    }
});

appModule.service('socketService', ['$rootScope', 'graphService', function($rootScope, graphService) {
    var listeners = [];
    var stompClient;
    return {
        connect: function() {
            var self = this;
            var socket = new SockJS('/socket');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;
            stompClient.connect({}, function(frame) {
                self.notifyListeners({'status':'connected'});
                stompClient.subscribe('/topic/graph', function(message) {
                    graphService.setGraphInfo(JSON.parse(message.body));
                });
                stompClient.subscribe('/topic/status', function(message) {
                    self.notifyListeners(JSON.parse(message.body));
                });
                stompClient.subscribe('/topic/update', function(message) {
                    graphService.update(JSON.parse(message.body));
                });

                stompClient.send("/app/status", {}, JSON.stringify({}));
            },
            function(error) {
                self.notifyListeners({'status':'error'});
            });
        },

        start: function(points, width, height) {
            stompClient.send("/app/start", {}, JSON.stringify({points: points, width: width, height: height}));
        },

        getGraph: function() {
            stompClient.send("/app/graph", {}, JSON.stringify({}));
        },

        addListener: function(callback) {
            listeners.push(callback);
        },

        notifyListeners: function(status) {
            angular.forEach(listeners, function(cb) {
                cb(status);
            });
        },

        disconnect: function() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        }
    };
}]);