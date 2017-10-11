
# CLIENT / SERVER communication protocol
Last updated october 11th (2017)

#1 - General informations

Communication between the game server and the clients is based around the Websocket protocol.
The websocket RFC is available here:
https://tools.ietf.org/html/rfc6455

The server is divided in two parts, each having their own TCP socket endpoint: a chat and a game information endpoint.
It is the client's job to connect to both to send and be able to recieve the appropriate information.
The endpoints are available at he following adresses:
[server ip or linked dns]:8080/PMDMMOServer/chat
[server ip or linked dns]:8080/PMDMMOServer/game

#2 - Required payload structure

The data sent to the server MUST be in a valid JSON structure, and encoded as text in the websocket container.
The server will ignore all binary payloads recieved (This is subject to change, refer to the latest documentation folder on github).

The server will not send a direct response to a client upon recieving a payload, but can be expected to send a receipt back for most informations.

The Json contained any payload must contain a String field called "action", wich is used to describe the content of the payload.
The other informations are payload dependant.

#3 - List of client -> server payloads

■ MESSAGE (This is a payload destined to the chat endpoint)
{"action":"message",
"tag":"DEV", // (Optionnal) The tag of the user sending the message
"sender":"Darkxell", // The name of the sender
"message":"Message content", // the content of the message
"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string
"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string
"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string
}

#4 - List of server -> client payloads

■ MESSAGE (This is a payload from to the chat endpoint)
{"action":"message",
"tag":"DEV", // (Optionnal) The tag of the user sending the message
"sender":"Darkxell", // The name of the sender
"message":"Message content", // the content of the message
"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string
"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string
"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string
}
