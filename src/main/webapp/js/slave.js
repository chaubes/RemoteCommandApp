(function(){
    var app = angular.module("remoteCommand");

    var SlaveController = function($scope, $routeParams, websocketService){
        $scope.message = "Welcome "+$routeParams.email+". You are registered as a Slave machine."

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

            if (response.type == "COMMAND") {
                if (response.message) {
                    $scope.message = response.message;
                }
                if(response.data){
                    $scope.data = response.data;
                    alert("The received command is..."+$scope.data);
                }
            }
        });
    };

    app.controller("SlaveController", SlaveController);
}());