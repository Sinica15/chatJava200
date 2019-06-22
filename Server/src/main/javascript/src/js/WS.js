// small helper function for selecting element by id
let id = id => document.getElementById(id);

//Establish the WebSocket connection and set up event handlers

let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
console.log("connected");

ws.onopen = () => {
    ws.send(
    JSON.stringify({
        msgType : "service",
        action : "setConnectionType",
        message : "web"
    }));
}
ws.onmessage = msg => updateChat(msg);
ws.onclose = () => alert("WebSocket connection closed");

// Add event listeners to button and input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) {
        // Send message if enter is pressed in input field
        sendAndClear(e.target.value);
    }
});
id("send_button").addEventListener("click", () => sendAndClear(id("message").value));
id("register_button").addEventListener("click", () => {
    if(id("userName").value.trim() == ""){
        alert("Name field can't be empty");
    }
    if (id("userName").value.trim() != "" && id("agent").checked) {
        console.log("!register " + id("userName").value.trim() + " 1");
        sendAndClear("1 " + id("userName").value.trim(), "register");
        id("form_back").remove();
        return;
    }
    if (id("userName").value.trim() != "" && id("client").checked) {
        console.log("!register " + id("userName").value.trim() + " 0");
        sendAndClear("0 " + id("userName").value.trim(), "register");
        id("form_back").remove();
        return;
    }
});
id("leave_button").addEventListener("click", () => {
    console.log("!leave");
    sendAndClear("", "leave");
});
id("exit_button").addEventListener("click", () => {
    console.log("!exit");
    sendAndClear("", "exit");
});

function sendAndClear(message, mode) {
    mode = mode || "message";
    if (message != "" || mode != "message") {
        console.log("sending");

        let msgType;
        if (mode == "message"){
            msgType = "message";
        }else{
            msgType = "service";
        }
        ws.send(JSON.stringify({
            msgType : msgType,
            action : mode,
            message : message
        }));
        if (mode == "message") {
            id("message").value = "";
        }
    }
}

function updateChat(msg) { // Update chat-panel and list of connected users
    let data = JSON.parse(msg.data);
    console.log(data);
    if (data.userMessage != undefined){
        id("chat").insertAdjacentHTML("afterbegin", data.userMessage);
        id("userlist").innerHTML = data.userlist.map(user => "<li>" + user + "</li>").join("");
    }

}
