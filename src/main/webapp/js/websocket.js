(function(){
    var websocketService = function($websocket){
        // Open a WebSocket connection
        var ws = $websocket("ws://" + document.location.host +
            "/RemoteCommandApp/command");
        ws.onError(function (event) {
            console.log("connection Error", event);
        });
        ws.onClose(function (event) {
            console.log("connection closed", event);
        });
        ws.onOpen(function () {
            console.log("connection open");
            //ws.send("Connection Open....");
        });
        ws.onMessage(function (event) {
            console.log("message: ", event.data);
        });

        return {
            getws: function () {
                return ws;
            },
            status: function () {
                return ws.readyState;
            },
            send: function (message) {
                if (angular.isString(message)) {
                    ws.send(message);
                }
                else if (angular.isObject(message)) {
                    ws.send(JSON.stringify(message));
                }
            }
        };
    };

    var app = angular.module("remoteCommand");
    app.factory("websocketService", websocketService);

}());