
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
    console.log("It is working!");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/message/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
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

function sendName() {
    var fields = $("#patientID").val().split(" ");
    var value= fields[0];
    fields = $("#doctorID").val().split(" ");
    var value2=fields[0];
    stompClient.send("/consultationApp/appointment", {}, JSON.stringify({'doctorID': value2, 'patientID': value,'appointmentDate': $("#date").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function sendToJava(){
    var form = document.createElement("form");
    form.method = 'post';
    form.action = '/consultation/makeAppointment';
    var doctorID = document.createElement('input');
    doctorID.type = "number";
    doctorID.name = "doctorID";
    doctorID.value = $("#doctorID").val();
    form.appendChild(doctorID);
    var patientID = document.createElement('input');
    patientID.type = "number";
    patientID.name = "patientID";
    patientID.value = $("#patientID").val();
    form.appendChild(patientID);
    var date = document.createElement('input');
    date.type = "text";
    date.name = "date";
    date.value = $("#date").val();
    form.appendChild(date);
    var ID = document.createElement('input');
    ID.type = "number";
    ID.name = "date";
    ID.value = $("#ID").val();
    form.appendChild(ID);
    var button = document.createElement('button');
    button.name = "createBtn";
    form.appendChild();
    form.submit();
    console.log("Message send")
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect();
    });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#createBtn" ).click(function() { sendName();
        //console.log("entered method")
        var fields = $("#patientID").val().split(" ");
        var value= fields[0];
        fields = $("#doctorID").val().split(" ");
        var value2=fields[0];
        //console.log(value);
        //console.log(value2);
        window.location.href="/makeAppointment?doctorID="+value2+"&patientID="+value+"&date="+$("#date").val();
        //sendToJava()
        });
    $( "#updateBtn" ).click(function() {
        window.location.href="/updateAppointment?consultationID="+$("#consultationID").val()+"&date="+$("#date").val();
    });
    $( "#deleteBtn" ).click(function() {
        window.location.href="/deleteAppointment?consultationID="+$("#consultationID").val();
    });
    $( "#logoutBtn" ).click(function() {
        window.location.href="logout";
    });
    $( "#secretaryBtn" ).click(function() {
        window.location.href="/secretaryChange";
    });
    $( "#doctorBtn" ).click(function() {
        window.location.href="/doctorChange";
    });
    $( "#adminBtn" ).click(function() {
        window.location.href="/administratorChange";
    });
});

