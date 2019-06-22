var div = document.createElement('div');
div.className = 'chat-main-container';
div.id = 'chat_main';
document.body.appendChild(div);
div.innerHTML =
    '<div id="form_back">\n' +
    '    <div id="register_form">\n' +
    '        <input type="text" placeholder="Type your Name" id="userName">\n' +
    '        <span>\n' +
    '            <input type="radio" id="agent" name="userType">\n' +
    '            <label for="agent">Agent</label>\n' +
    '        </span>\n' +
    '        <span>\n' +
    '            <input type="radio" id="client" name="userType">\n' +
    '            <label for="client">Client</label>\n' +
    '        </span>\n' +
    '        <div class="common-button" id="register_button">Register</div>\n' +
    '    </div>\n' +
    '</div>\n' +
    '<div id="body_container" class="chat-body-container">\n' +
    '    <div class="top-controls">' +
    '        <span id="userlist" class="userlist"></span>' +
    '        <span class="top-hide"></span>' +
    '    </div>' +
    '    <div id="chat" class="chat">\n' +
    '       <article class="server-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet animi asperiores cupiditate, delectus earum explicabo illo ipsum libero magni mollitia odio odit optio porro quo reprehenderit sed sit sunt tempore.</p><p class="sender-datetime"><span class="sender">Server, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="sender-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. At debitis, dicta ducimus eligendi error laboriosam minima modi nemo neque odit, optio quos vero voluptatibus? Doloremque est inventore molestias. Dolorem, sequi.</p><p class="sender-datetime"><span class="sender">Server, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="server-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid asperiores aspernatur blanditiis deleniti doloremque earum expedita impedit neque, nobis officiis quae, quaerat quasi qui quos rem sapiente unde velit veniam!</p><p class="sender-datetime"><span class="sender">Server, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="sender-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Architecto cum cupiditate dignissimos ex exercitationem illo, modi molestias numquam porro quae qui quis quod repellendus vitae voluptatum! Facere natus odio sequi!</p><p class="sender-datetime"><span class="sender">You, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="server-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Cupiditate dolorem odit quaerat, sint sunt ullam ut! Assumenda consectetur, earum id iure labore, laboriosam nemo nostrum provident sit, ut veritatis voluptas.</p><p class="sender-datetime"><span class="sender">You, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="sender-article"><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at, debitis ex perferendis rerum totam voluptates. Asperiores consectetur consequatur culpa dolores, explicabo id illo, itaque iure nam sapiente velit voluptates.</p><p class="sender-datetime"><span class="sender">You, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="server-article"><p>lorem</p><p class="sender-datetime"><span class="sender">Server, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '       <article class="sender-article"><p>lorem</p><p class="sender-datetime"><span class="sender">You, </span><span class="datetime">22/06/2019 18:21:11</span></p></article>' +
    '        <!-- Built by JS -->\n' +
    '    </div>\n' +
    '    <div class="msgType-area">\n' +
    '        <div class="chatControls">\n' +
    '          <input id="message" class="def_input" placeholder="Type your message">\n' +
    '          <div id="message_buttons_container">\n' +
    '              <div class="bottom-button common-button" id="send_button">Send</div>\n' +
    '              <div class="bottom-button common-button" id="leave_button">Leave</div>\n' +
    '              <div class="bottom-button common-button" id="exit_button">Exit</div>\n' +
    '          </div>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '</div>';
