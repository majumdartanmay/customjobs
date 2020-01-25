var stompClient = null;

function setConnected(connected) {
    $("#connect").text(connected ? "connected" : "disconnected")
}

function connect() {
    var socket = new SockJS('/script-repo-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/scriptStatus', function (greeting) {
            console.log(greeting);
            $("#message").text(greeting.body);
        });
    });
}

connect();
