(function(){
    var app = angular.module("remoteCommand");

    var AdminController = function($scope, $routeParams, websocketService){
        $scope.message = "Welcome "+$routeParams.email+". You are registered as a Admin User."

        $scope.submitCommand = function(commandstring) {
            var message = {
                email: $routeParams.email,
                commandstring: commandstring,
                usertype: "ADMIN",
                messagetype: "COMMAND",
                password: "adminpassword"
            };
            websocketService.send(message);
        };
    };

    app.controller("AdminController", AdminController);
}());