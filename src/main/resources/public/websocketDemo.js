// small helper function for selecting element by id
let id = id => document.getElementById(id);

//Establish the WebSocket connection and set up event handlers

let ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
console.log("connected");
ws.onmessage = msg => updateChat(msg);
ws.onclose = () => alert("WebSocket connection closed");

// Add event listeners to button and input field
id("send_button").addEventListener("click", () => sendAndClear(id("message").value));
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { // Send message if enter is pressed in input field
        sendAndClear(e.target.value);
    }
});
id("register_button").addEventListener("click", () => {
  if(id("userName").value.trim() == ""){
    alert("Name field can't be empty");
  }
  if (id("userName").value.trim() != "" && id("agent").checked) {
    console.log("$%$$ register " + id("userName").value.trim() + " 1");
    ws.send("$%$$ register " + id("userName").value.trim() + " 1");
    id("form_back").remove();
    return;
  }
  if (id("userName").value.trim() != "" && id("client").checked) {
    console.log("$%$$ register " + id("userName").value.trim() + " 0");
    ws.send("$%$$ register " + id("userName").value.trim() + " 0");
    id("form_back").remove();
    return;
  }
});
id("leave_button").addEventListener("click", () => {
  console.log("$%$$ leave");
  ws.send("$%$$ leave");
});
id("exit_button").addEventListener("click", () => {
  console.log("$%$$ exit");
  ws.send("$%$$ exit");
});



function sendAndClear(message) {
    if (message !== "") {
        ws.send(message);
        id("message").value = "";
    }
}



function updateChat(msg) { // Update chat-panel and list of connected users
    let data = JSON.parse(msg.data);
    id("chat").insertAdjacentHTML("afterbegin", data.userMessage);
    id("userlist").innerHTML = data.userlist.map(user => "<li>" + user + "</li>").join("");
}
