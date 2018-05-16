
# CLIENT / SERVER communication protocol
Last updated may 17th (2018)

# 1 - General information

Communication between the game server and the clients is based around the Websocket protocol.
The websocket RFC is available here:
https://tools.ietf.org/html/rfc6455

The server is divided in two parts, each having their own TCP socket endpoint: a chat and a game information endpoint.
It is the client's job to connect to both to send and be able to recieve the appropriate information.
The endpoints are available at the following adresses:

\[server ip or linked dns]:8080/PMDMMOServer/chat

\[server ip or linked dns]:8080/PMDMMOServer/game

# 2 - Required payload structure

The data sent to the server MUST be in a valid JSON structure, and encoded as text in the websocket container.
The server will ignore all binary payloads recieved (This is subject to change, refer to the latest documentation folder on github).

The server will not send a direct response to a client upon recieving a payload, but can be expected to send a receipt back for most informations.

The Json contained any payload must contain a String field called "action", wich is used to describe the content of the payload.
The other informations are payload dependant.

# 3 - List of client -> server payloads

■ MESSAGE (This is a payload destined to the chat endpoint)

{"action":"message",

"tag":"DEV", // (Optionnal) The tag of the user sending the message 

"sender":"Darkxell", // The name of the sender

"message":"Message content", // the content of the message

"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string

"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string

"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string

}

This is the payload you should send to the server when you want to add a message to the chat.


■ SALTRESET

{"action":"saltreset"}

This payload must be sent by a client to get a salt in order to log in. The server will generate a new random salt and send it back to the client. The client must authentificate by salting his hashed password using the set salt.
Trying to authentificate without reseting the salt will not be taken into account by the server.

■ CREATEACCOUNT

{"action":"createaccount",

"name":"Darkxell", // The name of the player

"passhash":"h4guz8eph5u", // The hashed password

}

This payload must by a client to create a new account. This will create a new account entry in the database.

■ LOGIN

{"action":"login",

"name":"Darkxell", // The name of the player

"passhash":"h4guz8eph5u", // The hashed password

}

This payload must by a client to login. This must be sent with the appropriate salted password, see hashing norms.

■ FREEZONEPOSITION

{"action":"freezoneposition",

"freezoneid":"base", // The string id of the player's current freezone

"posfx":"42.1337", // The x position of the player as a Double

"posfy":"42.1337", // The y position of the player as a Double

"currentpokemon":"1", // The id of the pokemon as a String

}

This payload should be sent by a client to the server to notify it about the player's position in freezones. The server will send to the client the position of nearby other players in response.

■ TESTRESULTS

{"action":"testresults",

"mainid":1, // The id of the main pokemon

"maingender":1, // The gender of the main pokemon

"offid":2, // The id of the teammate

"offgender":2 // The gender of the teammate

}

This payload is sent by a client wanting to submit test results to the server.

■ OBJECTREQUEST

{"action":"objectrequest",

"id":1, // The requested object ID

"object":"DBPlayer" // The object type requested

}

This payload notifies the server that the client would need information about a specific object knowing it's id.
The server will typically respond with an OBJECTREQUEST payload containing the item too.

■ REQUESTINVENTORY

{"action":"requestinventory",

"id":1, // The requested object ID

}

This payload notifies the server that the client would need the value of the inventory with the input id.
The server will typically respond with an REQUESTINVENTORY payload containing the inventory's value.

■ REQUESTMONSTER

{"action":"requestmonster",

"id":1, // The requested object ID

}

This payload notifies the server that the client would need the value of the Pokémon with the input id.
The server will typically respond with an REQUESTMONSTER payload containing the Pokémon's value.

# 4 - List of server -> client payloads

■ MESSAGE (This is a payload from to the chat endpoint)

{"action":"message",

"tag":"DEV", // (Optionnal) The tag of the user sending the message

"sender":"Darkxell", // The name of the sender

"message":"Message content", // the content of the message

"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string

"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string

"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string

}

This payload is sent by the server to notify the client of a new chat message.

■ FREEZONEPOSITION

{"action":"freezoneposition",

"name":"Darkxell", // The name of the player

"posfx":"42.1337", // The x position of the player as a Double

"posfy":"42.1337", // The y position of the player as a Double

"currentpokemon":"1", // The id of the pokemon as a String

}

This payload is sent by the server to any client who sent a valid freezoneposition payload to the server.
Note that this payload is sent each time you notify the server of your movement for each other player.

■ LOGIN

{"action":"login",

"player":{JSONVALUE} // The player object

}

This payload is sent by the server to the client when he sucessfully logs in.
This payload contains the information about the player the clients needs to sync in with the server upon logging in.

■ SALTRESET

{"action":"saltreset",

"value":"mynameAUbr5af7ef51gre"

}

This payload is sent after a client request and contains the needed salt to authentificate and prevent MIM attacks (in case of an SSL breach, wich is unlikely to happen in the first place).
The client must authentificate using the salt value in this payload using an authentificate client payload.

■ TESTRESULTRECEIVED

{"action":"testresultrecieved"}

This payload is sent by the server to the client after the server receives the information about the passed personnality quiz.

■ OBJECTREQUEST

{"action":"objectrequest",

"type":"DBPlayer", // The object type requested

"object":[JSONVALUE] // The object type requested

}

This payload is sent after a client request and contains the value of the requested object.

■ REQUESTINVENTORY

{"action":"requestinventory",

"object":[JSONVALUE], // The inventory value

"items":[{JSONVALUE},{JSONVALUE},...] // The values of all items in the inventory

}

This payload is sent after a client request and contains the value of the requested inventory, and each item it contains.

■ REQUESTMONSTER

{"action":"monsterrequest",

"object":[JSONVALUE], // The pokemon value

"item":[JSONVALUE], (Optionnal) The held item value of the Pokémon if it has one.

"moves":[{JSONVALUE},{JSONVALUE},...] // The values of all moves the Pokémon knows.

}

This payload is sent after a client request and contains the value of the requested Pokémon, its held item, and each move it knows.
