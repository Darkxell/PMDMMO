
# CLIENT / SERVER communication protocol
Last updated may 17th (2018)

# 1 - General information

Communication between the game server and the clients is based around the Websocket protocol.
The websocket RFC is available here:
https://tools.ietf.org/html/rfc6455

It is the client's job to connect to the endpoint to send and be able to recieve the appropriate information.
The endpoint is available at the following adress:

\[server ip or linked dns]:8080/PMDMMOServer/game

Note:
As of 30 Aug 2018, support for the chat endpoint has been fully removed.
Any mùention of a chat endpoint is legacy and needs to be updated.

# 2.1 - Required payload structure

The data sent to the server MUST be in a valid JSON structure, and encoded as text in the websocket container.
The server will ignore all binary payloads recieved (This is subject to change, refer to the latest documentation folder on github).

The server will not send a direct response to a client upon recieving a payload, but can be expected to send a receipt back for most informations.

The Json containing any payload must contain a String field called "action", wich is used to describe the content of the payload.
The other informations are payload dependant.

# 2.2 - Encryption

Any payload sent by a logged client to the server can be encrypted using AES256.
The encryption key used must be the key sent by the client to the server in the SETENCRYPTIONKEY payload
To use encrypted payloads, the client can just encrypt the JSON string as an encrypted string and send it in a clear text payload using the following structure:
{encrypted:1,value:"YOUR ENCRYPTED VALUE HERE"}

The server will encrypt any payload it sends to the client using the exact same fashion.

The only exeptions to this is is the SETENCRYPTIONKEY payload.
Refer to the specific documentation of this payload for further information.

For more informations about the login challenge, check "hashing norms.md".

# 3 - List of client -> server payloads

■ CHATMESSAGE

{"action":"chatmessage",

"tag":"DEV", // (Optionnal) The tag of the user sending the message 

"message":"Message content", // the content of the message

"tagcolor":"#333333", // (Optionnal) The color of the sender's tag, in valid hexadecimal string

"messagecolor":"#333333", // (Optionnal) The color of the message, in valid hexadecimal string

"sendercolor":"#333333" // (Optionnal) The color of the sender, in valid hexadecimal string

}

This is the payload you should send to the server when you want to add a message to the chat.
Note: this payload used to be adressed at the legacy chat endpoint. Chat endpoint is no longer supported for various reasons including security ones.

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

■ PUBLICKEYREQUEST

{"action":"publickeyrequest"}

This payload can be sent by the client to the server anytime. This will notify the server that the client would like to know the server's public async encryption key to setup a synchronous encryption communication. 

■ SETENCRYPTIONKEY

{"action":"setencryptionkey",

"value" : 
        #---------encrypted as hex string of bytes obtained by encrypting with the server public key--------
		{"key":"-------------", // Hexa string representation of the synchronous key the client wants to use from now on
		}
        #---------------------------------------------------------------------------------------------------
}

This payload should be sent by any client who wants to use encryption.
The server will now return payloads encrypted with the parsed key, and will be able to decrypt any message sync encrypted with it from the client who sent it.
This payload "value" field has to be the needed json payload above as string encrypted asynchronously with the server public key. For more information, refer to "doc/hashing norms.md"

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

■ ITEMACTION

{"action":"itemaction",

"value":"trash" OR "give" OR "take", // The action to perform on the item

"item":3546512, // The ID of the item to perform the action on.

"pokemon":31869846 // The ID of the affected Pokémon if action is "give" or "take".

}

This payload is sent when the player executes a movement action on an item in freezones.

■ BANKACTION

{"action":"bankaction",

"money":3546512, // The amount of money **deposited** in the bank. If it's negative, then it's the amount **withdrawn** from the bank.

}

This payload is sent when the player interacts with the bank to deposit or withdraw money.

■ STORAGEACTION

{"action":"storageaction",

"value":"withdraw|deposit",

"items":[45,897,12], // The items to deposit or withdraw.

"quantities":[45,1,1] // The quantities to move for each item.

}

This payload is sent when the player interacts with the storage to deposit or withdraw a stack of a single item.

■ DUNGEONSTART

{"action":"dungeonstart",

"dungeon":16 // The ID of the dungeon to enter

}

This payload is sent when the player starts a dungeon exploration.

■ DUNGEONEND

{"action":"dungeonend",

"outcome":{OUTCOME_JSON_VALUE},

"player":{PLAYER_JSON_VALUE},

"inventory":{INV_JSON_VALUE},

"team":[{POK1}],

"moves":[{MOVE1},{MOVE2},...],

"items":[{ITEM1},{ITEM2},...],

"completedmissions":["mission1id","mission2id",...]

}

This payload is sent when the player finishes a dungeon exploration. Allows the server to modify the database Player in consequence.

■ GETMISSIONS

{"action":"getmissions"}

This payload is sent when the client wants to be notified shortly about all the missions available at the moment.

■ ACCEPTMISSION

{"action":"acceptmission",

"mission":"missioncode"

}

This payload is sent to the server to accept a mission from the daily mission board.
Note that the server might ignore this payload if the player has already accepted this mission today

■ DELETEMISSION

{"action":"deletemission",

"mission":"missioncode"

}

This payload is sent to the server to delete a mission from the player mission inventory.

■ NICKNAME

{"action":"nickname",

"pokemonid":id,

"nickname":"newnickname"

}

This payload can be sent by any connected client and will change the nickname of the parsed pokemon to the parsed name.
Note that this will only occur if the connected player owns the pokemon.

■ STORYADVANCE

{"action":"storyadvance",

"target":id

}

This payload can be sent by a client to change it's serverside storyposition to the desired value.
Note that this only works for a very limited ammount of values, and only if the account already is in a specific storyposition.
StorypositionAdvanceHandler.java in the server project contains the list of positions reachable.

# 4 - List of server -> client payloads

■ CHATMESSAGE

{"action":"chatmessage",

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

■ PUBLICKEYREQUEST

{"action":"publickeyrequest",

"keybytes":"3082012...2300D06" // Hex representation of the public key of the current server instance 

}

This is sent by the server to a client who asked to recieve it. This key can be turned into a byte array

■ SETENCRYPTIONKEY

{"action":"setencryptionkey",
"ack":"ok"
}

This simple payload is sent by the server in response to a setencryptionkey from the client to acknowledge the receipt of the key.
THis will usually be the first encrypted payload of the client/server communication.

■ SALTRESET

{"action":"saltreset",

"value":"mynameAUbr5af7ef51gre"

}

This payload is sent after a client request and contains the needed salt to authentificate and prevent MIM attacks (in case of an SSL breach, wich is unlikely to happen in the first place).
The client must authentificate using the salt value in this payload using an authentificate client payload.

■ LOGININFO

{"action":"logininfo",

"value":"info.message.id"

}

This payload is sent by the server to the client to nitify it of an action. This payload should be used for UID ends.
It might be sent for exemple if the player tries to create an account but his name is already taken, or if a server error occurs when logging in.

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

■ ITEMACTIONRESULT

{"action":"itemactionresult",

"value":"trashsuccess|givesuccess|takesuccess|pokemonhasitem|pokemonhasnoitem|inventoryfull|cantbetrashed", // The value of the result

"item":3546512, // The ID of the item the action was performed on.

"pokemon":31869846 // The ID of the affected Pokémon if action is "give" or "take".

}

This payload is sent after a client requests an action on an item to describe how that action went.

■ BANKACTIONCONFIRM

{"action":"bankactionconfirm",

"moneyinbag":3546512, // The resulting amount of money in the player's bag.

"moneyinbank":3546512, // The resulting amount of money in the player's bank.

}

This payload is sent when the player interacts with the bank, after database update, sending confirmation and new values to the client.

■ STORAGEACTIONCONFIRM

{"action":"storageactionconfirm",

"result":"ok|inventoryfull|storagefull" // The result of the storage interaction.

}

This payload is sent after a client requests an action in the storage to describe how that action went.

■ DUNGEONSTARTCONFIRM

{"action":"dungeonstartconfirm",

"seed":1458746519864512 // The seed to use for the Dungeon exploration.

}

This payload is sent after a client sends a dungeonstart request to give the client a seed for the exploraiton, generated by the server.

■ DUNGEONENDCONFIRM

{"action":"dungeonendconfirm",

"outcome":{OUTCOME_JSON_VALUE}

}

This payload is sent after a client sends a dungeonend request to notify the client that its Player has been updated.

■ GETMISSIONS

{"action":"getmissions"

"missions":["code1","code2", ... ,"codeN"]

}

This payload is sent by the server upon client request. The session doesn't need to be loged in for the server to send this payload.
The code returned are the deterministic mission ids.

■ ACCEPTMISSION

{"action":"acceptmission"

"mission":"missioncode"

"accepted":1
}

This payload is sent by the server to the client to tell him if the player was allowed to acept the mission or not.
1 means accepted, any other value means it has been refused.
