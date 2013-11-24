$(function () {

    var registered = false;

    var text = $("#text");
    var button = $("#button");
    var user = $("#user");
    var error = $(".simple_overlay .error");
    var status = $(".status");

    var messages = $(".messages");

    var overlay = $(".simple_overlay").overlay(
        {
            load: false,
            closeOnClick: false
        }
    );
    var name = $("#name");
    var ok = $("#ok");

    var request = {
        url: websocketUrl,
        contentType : "application/json",
        logLevel : 'debug',
        transport : 'websocket' ,
        fallbackTransport: 'long-polling',

        onMessage: function(response) {
            var msg = JSON.parse(response.responseBody);
            if(msg.type == "register") {
                if(msg.data != "FAIL") {
                    user.val(msg.data);
                    button.text("Send");
                    enable(text);
                    registered = true;
                    overlay.remove();
                } else {
                    error.text("Username already in use");
                }
            } else if(msg.type == "adduser") {
                pringMessage(msg.data + " has joined");
                $(".users").append("<div class='user'>" + msg.data + "</div>");
            } else if(msg.type == "removeuser") {
                pringMessage(msg.data + " has left");
                $('.user').filter(function(){return (msg.data == $(this).text().trim())}).remove();
            } else if(msg.type == "message") {
                var message = JSON.parse(msg.data);
                var messageText = message.date + " " + message.author + ": " + message.text;
                pringMessage(messageText);
            } else if(msg.type == "error") {
                status.text(msg.data);
            }

            console.log("Atmosphere onMessage: " + msg);
        },
        onOpen: function(response) {
            status.text("Connected");
        },
        onReconnect: function (request, response) {
            console.log("Atmosphere onReconnect: Reconnecting");
            status.text("Reconnecting...");
        },
        onClose: function(response) {
            console.log('Atmosphere onClose executed');
            status.text("Disconnected");
        },

        onError: function(response) {
            console.log('Atmosphere onError: Sorry, but there is some problem with your '
                + 'socket or the server is down');
            status.text("Problems with socket or server is down");
        }
    };

    var socket = $.atmosphere;
    var subSocket = socket.subscribe(request);

    disable(text);

    button.click(function() {
        if(!registered) {
            overlay.overlay().load();
        } else {
            message();
        }
    });

    text.bind("keypress", function(e) {
        if(isKey(e, 13)) {
            message();
        }
    });

    name.bind("keypress", function(e) {
        if(isKey(e, 13)) {
            register();
        }
    });

    ok.click(function() {
        register();
    });

    function register() {
        var nameVal = name.val();
        if(!nameVal.trim()) {
            return;
        }
        var msg = {
            type: "register",
            data: nameVal
        };
        subSocket.push(JSON.stringify(msg));
    }

    function message() {
        if(!text.val().trim()) {
            return;
        }
        var msg = {
            type: "message",
            data: text.val()
        }
        subSocket.push(JSON.stringify(msg));
        text.val("");
    }

    function isKey(e, code) {
        var kcode = e.keyCode || e.which;
        return (code == kcode);
    }

    function enable(o) {
        o.removeAttr("disabled");
    }

    function disable(o) {
        o.attr("disabled", "disabled");
    }

    function scrollDown(o) {
        o.scrollTop(o[0].scrollHeight);
    }

    function pringMessage(msg) {
        messages.append("<div class='message'>" + msg + "</div>");
        scrollDown(messages);
    }
});