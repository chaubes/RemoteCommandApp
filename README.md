# RemoteCommandApp
Remote Command Websocket Application

Application Information:

The Remote Command Broadcast application showcases the power of websocket API which enables the servers to communicate with 
the client browser sessions and vice versa. This experimental web application helps you to send commands remotely from one 
of your machine's browser session (registered as MASTER) to several other machines sessions(registered as SLAVE). 
The user interface in action is your web browser on your machine which supports websockets. 

Play with this application by following the below steps: 

1) Register your web browser session as a SLAVE using a valid email id and a password. By registering as a SLAVE 
The commands as invoked by the MASTER web browser session would be executed in the SLAVE sessions. You can register 
as many SLAVE sessions as you want on different machines which you want to control remotely. 

2) Register one of your browser session as a MASTER using the same valid email id and password which was used while 
registering the SLAVE sessions. This will allow to link up the MASTER session with all the SLAVE sessions registered 
with the same email id. Please note that there can only be one MASTER session for every registered email Id.


Technologies used:

This application is built over AngularJS on the client end and the makes use of the websocket API support offered in Java EE7 
on the server side. Also, this application makes use of the angular-websocket.js script (can be found at repo:https://github.com/gdi2290/angular-websocket.git). It is hosted over OpenShift tomcat7 Cartridge.
