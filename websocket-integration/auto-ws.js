const WebSocket = require('ws');

function create_ws(index) {
    var sock = new WebSocket('ws://localhost:8080/ws-integration/time');
        sock.onopen = function () {
            console.log(index + "-onopen");
        };
        sock.onmessage = function (e) {
            console.log(index + "-message received");
            console.log(index + "-data:"+e.data);
        };
        sock.onclose = function () {
            console.log(index + "-onclose");
        };
}

 var connections = [];
 var numberOfconnections=500;

 for(var i = 0; i < numberOfconnections; i++) {
    connections.push(create_ws(i));
 }