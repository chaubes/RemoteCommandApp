(function(){
    var app = angular.module("remoteCommand",["ngRoute","ngWebSocket"]);

    app.config(function($routeProvider){
        $routeProvider
            .when("/main",{
                templateUrl: "main.html"
            })
            .when("/slave/:email",{
                templateUrl: "slave.html",
                controller: "SlaveController"
            })
            .when("/master/:email",{
                templateUrl: "master.html",
                controller: "MasterController"
            })
            .when("/admin/:email",{
                templateUrl: "admin.html",
                controller: "AdminController"
            })
            .when("/register/:usertype",{
                templateUrl: "register.html",
                controller: "RegisterController"
            })
            .otherwise({redirectTo:"/main"});
    });

}());