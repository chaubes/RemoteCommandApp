(function() {
    var app = angular.module("remoteCommand");

    var RegisterController = function($scope, $routeParams, $location, websocketService) {
        $scope.usertype = $routeParams.usertype;
        $scope.message = "Please register as a "+$routeParams.usertype;

        var ws = websocketService.getws();
        ws.onMessage(function(event) {
            console.log("message: ", event.data);
            var response;
            try {
                response = angular.fromJson(event.data);
            } catch (e) {
                console.log('error: ', e);
                response = {
                    'error': e
                };
            }
            if (response.message) {
                $scope.message = response.message;
            }
            if (response.status == "SUCCESS") {
                if ($scope.usertype == "SLAVE") {
                    $location.path("/slave/"+$scope.email);
                } else if ($scope.usertype == "MASTER") {
                    $location.path("/master/"+$scope.email);
                } else if ($scope.usertype == "ADMIN") {
                    $location.path("/admin/"+$scope.email);
                }
            }
        });

        $scope.register = function() {
            var message = {
                email: $scope.email,
                password: $scope.password,
                usertype: $scope.usertype,
                messagetype: "REGISTER"
            };
            websocketService.send(message);
        };

    };

    app.controller("RegisterController", RegisterController);

}());