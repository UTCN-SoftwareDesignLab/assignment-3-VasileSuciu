
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/message/appointment', function (greeting) {
            console.log("We received a message!")
            if (greeting.body!=null) {
                showGreeting(JSON.parse(greeting.body).content);
            }
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function showGreeting(message) {
    $("#newMessages").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect();
    });
    $( "#disconnect" ).click(function() { disconnect(); });

    $( "#patientBtn" ).click(function() {
        var fields = $("#patientID").val().split(" ");
        var value= fields[0];
        window.location.href="/viewPatient?patientID="+value;
    });
    $( "#addObservationBtn" ).click(function() {
        var fields = $("#consultationID").val().split(" ");
        var value= fields[0];
        window.location.href="/addObservation?consultationID="+value+"&observation="+$("#observation").val();
    });
    $( "#logoutBtn" ).click(function() {
        window.location.href="logout"
    });
    $( "#secretaryBtn" ).click(function() {
        window.location.href="/secretaryChange2"
    });
    $( "#consultationBtn" ).click(function() {
        window.location.href="/consultationChange2"
    });
    $( "#adminBtn" ).click(function() {
        window.location.href="/administratorChange2"
    });
});

