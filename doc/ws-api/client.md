# List of client &rarr; server payloads

## Table of Contents

- [List of client &rarr; server payloads](#list-of-client-rarr-server-payloads)
  - [Table of Contents](#table-of-contents)
  - [Endpoints](#endpoints)
    - [`CHATMESSAGE` **(DEPRECATED)**](#chatmessage-deprecated)
    - [`SALTRESET`](#saltreset)
    - [`CREATEACCOUNT`](#createaccount)
    - [`PUBLICKEYREQUEST`](#publickeyrequest)
    - [`SETENCRYPTIONKEY`](#setencryptionkey)
    - [`LOGIN`](#login)
    - [`FREEZONEPOSITION`](#freezoneposition)
    - [`TESTRESULTS`](#testresults)
    - [`OBJECTREQUEST`](#objectrequest)
    - [`REQUESTINVENTORY`](#requestinventory)
    - [`REQUESTMONSTER`](#requestmonster)
    - [`ITEMACTION`](#itemaction)
    - [`BANKACTION`](#bankaction)
    - [`STORAGEACTION`](#storageaction)
    - [`DUNGEONSTART`](#dungeonstart)
    - [`DUNGEONEND`](#dungeonend)
    - [`GETMISSIONS`](#getmissions)
    - [`ACCEPTMISSION`](#acceptmission)
    - [`DELETEMISSION`](#deletemission)
    - [`NICKNAME`](#nickname)
    - [`STORYADVANCE`](#storyadvance)

## Endpoints

### `CHATMESSAGE` **(DEPRECATED)**

| field          | type or value | required | about                           |
| -------------- | ------------- | :------: | ------------------------------- |
| `action`       | `chatmessage` | &check;  |                                 |
| `message`      | string        | &check;  | message content                 |
| `tag`          | string        |          | tag of the user sending message |
| `tagcolor`     | hex color     |          | color of sender's tag           |
| `messagecolor` | hex color     |          | color of message                |
| `sendercolor`  | hex color     |          | color of sender                 |

example:

```json
{
    "action": "chatmessage",
    "message":"Message content",
    "tag": "DEV",
    "tagcolor": "#333333",
    "messagecolor": "#333333",
    "sendercolor": "#333333"
}
```

This is the payload you should send to the server when you want to add a message
to the chat.

Note: this payload used to be adressed at the legacy chat endpoint. Chat
endpoint is no longer supported for various reasons including security ones.

### `SALTRESET`

| field    | type or value | about |
| -------- | ------------- | ----- |
| `action` | `saltreset`   |       |

example:

```json
{
    "action": "saltreset"
}
```

This payload must be sent by a client to get a salt in order to log in. The
server will generate a new random salt and send it back to the client. The
client must authentificate by salting his hashed password using the set salt.
Trying to authentificate without reseting the salt will not be taken into
account by the server.

### `CREATEACCOUNT`

| field       | type or value   | about                            |
| ----------- | --------------- | -------------------------------- |
| `action`    | `createaccount` |                                  |
| `name`      | string          | username                         |
| `passhash`  | string          | hashed password                  |
| `deploykey` | string          | deploy key if required by server |

example:

```json
{
    "action": "createaccount",
    "name": "Darkxell",
    "passhash": "h4guz8eph5u",
    "deploykey": "value"
}
```

This payload must by a client to create a new account. This will create a new
account entry in the database. The account will not be created if the server is
in deploykey only mode and a valid key isn't provided. Note that if the server
doesn't need a key for account creation at the moment he recieves this payload,
the key won't be consumed, even if it is valid.

### `PUBLICKEYREQUEST`

| field    | type or value      | about |
| -------- | ------------------ | ----- |
| `action` | `publickeyrequest` |       |

example:

```json
{
    "action": "publickeyrequest"
}
```

This payload can be sent by the client to the server anytime. This will notify
the server that the client would like to know the server's public async
encryption key to setup a synchronous encryption communication.

### `SETENCRYPTIONKEY`

| field       | type or value      | about                                      |
| ----------- | ------------------ | ------------------------------------------ |
| `action`    | `publickeyrequest` |                                            |
| `value`     | object             | hex string of sync key client wants to use |
| `value.key` | string             |                                            |

example:

```json
{
    "action": "setencryptionkey",
    "value": {
        "key":"-------------",
    }
}
```

This payload should be sent by any client who wants to use encryption. The
server will now return payloads encrypted with the parsed key, and will be able
to decrypt any message sync encrypted with it from the client who sent it.

This payload "value" field has to be the needed json payload above as string
encrypted asynchronously with the server public key. For more information, refer
to the [login documentation](./hashing%20norms.md)

### `LOGIN`

| field      | type or value | about           |
| ---------- | ------------- | --------------- |
| `action`   | `login`       |                 |
| `name`     | string        | username        |
| `passhash` | string        | hashed password |

example:

```json
{
    "action": "login",
    "name": "Darkxell",
    "passhash":"h4guz8eph5u"
}
```

This payload must by a client to login. This must be sent with the appropriate
salted password, see hashing norms.

### `FREEZONEPOSITION`

| field        | type or value      | about            |
| ------------ | ------------------ | ---------------- |
| `action`     | `freezoneposition` |                  |
| `freezoneid` | string             | current freezone |
| `posfx`      | string (Double)    | x coordinates    |
| `posfy`      | string (Double)    | y coordinates    |
| `posfy`      | string (int)       | pokémon ID       |

example:

```json
{
    "action": "freezoneposition",
    "freezoneid": "base",
    "posfx": "42.1337",
    "posfy": "42.1337",
    "currentpokemon": "1",
}
```

This payload should be sent by a client to the server to notify it about the
player's position in freezones. The server will send to the client the position
of nearby other players in response.

### `TESTRESULTS`

| field        | type or value | about                  |
| ------------ | ------------- | ---------------------- |
| `action`     | `testresults` |                        |
| `mainid`     | number        | main pokémon id        |
| `maingender` | number        | main pokémon gender    |
| `offid`      | number        | partner pokémon id     |
| `offgender`  | number        | partner pokémon gender |

example:

```json
{
    "action": "testresults",
    "mainid": 1,
    "maingender": 1,
    "offid": 2,
    "offgender": 2
}
```

This payload is sent by a client wanting to submit test results to the server.

### `OBJECTREQUEST`

| field    | type or value   | about                 |
| -------- | --------------- | --------------------- |
| `action` | `objectrequest` |                       |
| `id`     | number          | object id             |
| `object` | string          | object type requested |

example:

```json
{
    "action": "objectrequest",
    "id": 1,
    "object": "DBPlayer"
}
```

This payload notifies the server that the client would need information about a
specific object knowing it's id. The server will typically respond with a
[`OBJECTREQUEST`](./server.md#objectrequest) payload containing the item too.

### `REQUESTINVENTORY`

| field    | type or value      | about     |
| -------- | ------------------ | --------- |
| `action` | `requestinventory` |           |
| `id`     | number             | object id |

example:

```json
{
    "action": "requestinventory",
    "id": 1,
}
```

This payload notifies the server that the client would need the value of the
inventory with the input id. The server will typically respond with a
[`REQUESTINVENTORY`](./server.md#requestinventory) payload containing the
inventory's value.

### `REQUESTMONSTER`

| field    | type or value    | about     |
| -------- | ---------------- | --------- |
| `action` | `requestmonster` |           |
| `id`     | number           | object id |

example:

```json
{
    "action": "requestmonster",
    "id": 1,
}
```

This payload notifies the server that the client would need the value of the
Pokémon with the input id. The server will typically respond with a
[`REQUESTMONSTER`](./server.md#requestmonster) payload containing the Pokémon's
value.

### `ITEMACTION`

| field     | type or value           | about                  |
| --------- | ----------------------- | ---------------------- |
| `action`  | `itemaction`            |                        |
| `value`   | `give`, `trash`, `take` | action to perform      |
| `item`    | number                  | item id                |
| `pokemon` | number                  | pokémon id if relevant |

example:

```json
{
    "action": "itemaction",
    "value": "trash",
    "item": 3546512,
    "pokemon": 31869846
}
```

This payload is sent when the player executes a movement action on an item in
freezones.

### `BANKACTION`

| field    | type or value | about                                 |
| -------- | ------------- | ------------------------------------- |
| `action` | `bankaction`  |                                       |
| `money`  | number        | amount deposited (+) or withdrawn (-) |

example:

```json
{
    "action": "bankaction",
    "money":3546512
}
```

This payload is sent when the player interacts with the bank to deposit or
withdraw money.

### `STORAGEACTION`

| field        | type or value         | about                        |
| ------------ | --------------------- | ---------------------------- |
| `action`     | `storageaction`       |                              |
| `value`      | `withdraw`, `deposit` |                              |
| `items`      | number[]              | items to deposit or withdraw |
| `quantities` | number[]              | quantities of each item      |

example:

```json
{
    "action": "storageaction",
    "value": "withdraw",
    "items": [45, 897, 12],
    "quantities": [45, 1, 1]
}
```

This payload is sent when the player interacts with the storage to deposits or
withdraws a stack of a single item.

### `DUNGEONSTART`

| field     | type or value  | about      |
| --------- | -------------- | ---------- |
| `action`  | `dungeonstart` |            |
| `dungeon` | number         | dungeon id |

example:

```json
{
    "action":"dungeonstart",
    "dungeon": 16
}
```

This payload is sent when the player starts a dungeon exploration.

### `DUNGEONEND`

| field               | type or value  | about                  |
| ------------------- | -------------- | ---------------------- |
| `action`            | `dungeonstart` |                        |
| `outcome`           | object         | outcome details        |
| `success`           | boolean        | quick outcome          |
| `player`            | object         | player object          |
| `inventory`         | object         | inventory when leaving |
| `team`              | object[]       |                        |
| `moves`             | object[]       |                        |
| `items`             | items[]        |                        |
| `completedmissions` | mission codes  |                        |

example:

```json
{
    "action": "dungeonend",
    "outcome": {},
    "success": true,
    "player": {},
    "inventory": {},
    "team": [{}],
    "moves": [{}, {}],
    "items": [{}, {}],
    "completedmissions": ["mission1id", "mission2id"]
}
```

This payload is sent when the player finishes a dungeon exploration. Allows the
server to modify the database Player in consequence.

### `GETMISSIONS`

| field    | type or value | about |
| -------- | ------------- | ----- |
| `action` | `getmissions` |       |

example:

```json
{
    "action": "getmissions"
}
```

This payload is sent when the client wants to be notified shortly about all the
missions available at the moment.

### `ACCEPTMISSION`

| field     | type or value   | about |
| --------- | --------------- | ----- |
| `action`  | `acceptmission` |       |
| `mission` | mission code    |       |

example:

```json
{
    "action": "acceptmission",
    "mission": "missioncode"
}
```

This payload is sent to the server to accept a mission from the daily mission
board. Note that the server might ignore this payload if the player has already
accepted this mission today.

### `DELETEMISSION`

| field     | type or value   | about |
| --------- | --------------- | ----- |
| `action`  | `deletemission` |       |
| `mission` | mission code    |       |

example:

```json
{
    "action": "deletemission",
    "mission": "missioncode"
}
```

This payload is sent to the server to delete a mission from the player mission
inventory.

### `NICKNAME`

| field       | type or value | about |
| ----------- | ------------- | ----- |
| `action`    | `nickname`    |       |
| `pokemonid` | number        |       |
| `nickname`  | string        |       |

example:

```json
{
    "action": "nickname",
    "pokemonid": 1,
    "nickname": "newnickname"
}
```

This payload can be sent by any connected client and will change the nickname of
the parsed Pokémon to the parsed name. Note that this will only occur if the
connected player owns the Pokémon.

### `STORYADVANCE`

| field    | type or value | about             |
| -------- | ------------- | ----------------- |
| `action` | `nickname`    |                   |
| `target` | number        | story position id |

example:

```json
{
    "action": "storyadvance",
    "target": 1
}
```

This payload can be sent by a client to change it's serverside storyposition to
the desired value. Note that this only works for a very limited ammount of
values, and only if the account already is in a specific storyposition.
[`StorypositionAdvanceHandler.java`] contains the list of positions reachable.

[`StorypositionAdvanceHandler.java`]: ../../PMDMMOServer/src/java/com/darkxell/gameserver/messagehandlers/StorypositionAdvanceHandler.java
