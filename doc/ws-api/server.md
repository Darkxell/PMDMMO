# List of client &rarr; server payloads

## Table of Contents

- [List of client &rarr; server payloads](#list-of-client-rarr-server-payloads)
  - [Table of Contents](#table-of-contents)
  - [Endpoints](#endpoints)
    - [`CHATMESSAGE`](#chatmessage)
    - [`FREEZONEPOSITION`](#freezoneposition)
    - [`LOGIN`](#login)
    - [`PUBLICKEYREQUEST`](#publickeyrequest)
    - [`SETENCRYPTIONKEY`](#setencryptionkey)
    - [`SALTRESET`](#saltreset)
    - [`LOGININFO`](#logininfo)
    - [`TESTRESULTRECEIVED`](#testresultreceived)
    - [`OBJECTREQUEST`](#objectrequest)
    - [`REQUESTINVENTORY`](#requestinventory)
    - [`REQUESTMONSTER`](#requestmonster)
    - [`ITEMACTIONRESULT`](#itemactionresult)
    - [`BANKACTIONCONFIRM`](#bankactionconfirm)
    - [`STORAGEACTIONCONFIRM`](#storageactionconfirm)
    - [`DUNGEONSTARTCONFIRM`](#dungeonstartconfirm)
    - [`DUNGEONENDCONFIRM`](#dungeonendconfirm)
    - [`GETMISSIONS`](#getmissions)
    - [`ACCEPTMISSION`](#acceptmission)

## Endpoints

### `CHATMESSAGE`

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

This payload is sent by the server to notify the client of a new chat message.

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

This payload is sent by the server to any client who sent a valid
[`FREEZONEPOSITION`](./client.md#freezoneposition) payload to the server. Note
that this payload is sent each time you notify the server of your movement for
every other player.

### `LOGIN`

| field    | type or value | about         |
| -------- | ------------- | ------------- |
| `action` | `login`       |               |
| `player` | object        | player object |

example:

```json
{
    "action": "login",
    "player": {}
}
```

This payload is sent by the server to the client when he sucessfully logs in.
This payload contains the information about the player the clients needs to
sync in with the server upon logging in.

### `PUBLICKEYREQUEST`

| field      | type or value | about                        |
| ---------- | ------------- | ---------------------------- |
| `action`   | `login`       |                              |
| `keybytes` | hex string    | public key of current server |
| `version`  | String        | Current server version       |

example:

```json
{
    "action": "publickeyrequest",
    "keybytes": "3082012...2300D06",
	"version": "1.0"
}
```

This is sent by the server to a client who asked to receive it. This key can be
turned into a byte array.

### `SETENCRYPTIONKEY`

| field    | type or value | about |
| -------- | ------------- | ----- |
| `action` | `login`       |       |
| `ack`    | `ok`          |       |

example:

```json
{
    "action": "setencryptionkey",
    "ack": "ok"
}
```

This simple payload is sent by the server in response to a
[`SETENCRYPTIONKEY`](./client.md#setencryptionkey) from the client to
acknowledge the receipt of the key. This will usually be the first encrypted
payload of the client/server communication.

### `SALTRESET`

| field    | type or value  | about |
| -------- | -------------- | ----- |
| `action` | `saltreset`    |       |
| `value`  | Base64 payload |       |

example:

```json
{
    "action": "saltreset",
    "value": "mynameAUbr5af7ef51gre"
}
```

This payload is sent after a client request and contains the needed salt to
authenticate and prevent MIM attacks (in case of an SSL breach, wich is unlikely
to happen in the first place). The client must authenticate using the salt value
in this payload using an authenticate client payload.

### `LOGININFO`

| field    | type or value | about |
| -------- | ------------- | ----- |
| `action` | `saltreset`   |       |
| `value`  | string        |       |

example:

```json
{
    "action": "logininfo",
    "value": "info.message.id"
}
```

This payload is sent by the server to the client to notify it of an action. This
payload should be used for UID ends. It might be sent for example if the player
tries to create an account but the name is already taken, or if a server error
occurs when logging in.

### `TESTRESULTRECEIVED`

| field    | type or value        | about |
| -------- | -------------------- | ----- |
| `action` | `testresultrecieved` |       |

example:

```json
{
    "action": "testresultrecieved"
}
```

This payload is sent by the server to the client after the server receives the
information about the passed personnality quiz.

### `OBJECTREQUEST`

| field    | type or value   | about |
| -------- | --------------- | ----- |
| `action` | `objectrequest` |       |
| `type`   | string          |       |
| `object` | JSON            |       |

example:

```json
{
    "action": "objectrequest",
    "type": "DBPlayer",
    "object": {}
}
```

This payload is sent after a client request and contains the value of the
requested object.

### `REQUESTINVENTORY`

| field    | type or value      | about                  |
| -------- | ------------------ | ---------------------- |
| `action` | `requestinventory` |                        |
| `object` | JSON               | inventory              |
| `items`  | JSON[]             | all items in inventory |

example:

```json
{
    "action": "requestinventory",
    "object": {},
    "items": [{}, {}]
}
```

This payload is sent after a client request and contains the value of the
requested inventory, and each item it contains.

### `REQUESTMONSTER`

| field    | type or value | required | about        |
| -------- | ------------- | :------: | ------------ |
| `action` | `chatmessage` | &check;  |              |
| `object` | JSON          | &check;  | pokémon data |
| `item`   | JSON          |          | held item    |
| `moves`  | JSON[]        | &check;  | known moves  |

example:

```json
{
    "action": "monsterrequest",
    "object": {},
    "item": {},
    "moves": [{}, {}]
}
```

This payload is sent after a client request and contains the value of the
requested Pokémon, its held item, and each move it knows.

### `ITEMACTIONRESULT`

| field     | type or value      | about                  |
| --------- | ------------------ | ---------------------- |
| `action`  | `itemactionresult` |                        |
| `value`   | enum*              | pokémon data           |
| `item`    | number             | item id                |
| `pokemon` | number             | pokemon id if relevant |

\* `trashsuccess`, `givesuccess`, `takesuccess`, `pokemonhasitem`,
`pokemonhasnoitem`, `inventoryfull`, `cantbetrashed`

example:

```json
{
    "action": "itemactionresult",
    "value": "trashsuccess",
    "item": 3546512,
    "pokemon": 31869846
}
```

This payload is sent after a client requests an action on an item to describe
how that action went.

### `BANKACTIONCONFIRM`

| field         | type or value       | about |
| ------------- | ------------------- | ----- |
| `action`      | `bankactionconfirm` |       |
| `moneyinbag`  | number              |       |
| `moneyinbank` | number              |       |

example:

```json
{
    "action": "bankactionconfirm",
    "moneyinbag": 3546512,
    "moneyinbank": 3546512,
}
```

This payload is sent when the player interacts with the bank, after database
update, sending confirmation and new values to the client.

### `STORAGEACTIONCONFIRM`

| field    | type or value          | about |
| -------- | ---------------------- | ----- |
| `action` | `storageactionconfirm` |       |
| `result` | enum*                  |       |

\* `ok`, `inventoryfull`, `storagefull`

example:

```json
{
    "action":"storageactionconfirm",
    "result":"ok"
}
```

This payload is sent after a client requests an action in the storage to
describe how that action went.

### `DUNGEONSTARTCONFIRM`

| field    | type or value         | about                   |
| -------- | --------------------- | ----------------------- |
| `action` | `dungeonstartconfirm` |                         |
| `seed`   | number                | dungeon generation seed |

example:

```json
{
    "action": "dungeonstartconfirm",
    "seed": 1458746519864512
}
```

This payload is sent after a client sends a dungeonstart request to give the
client a seed for the exploraiton, generated by the server.

### `DUNGEONENDCONFIRM`

| field    | type or value       | about                   |
| -------- | ------------------- | ----------------------- |
| `action` | `dungeonendconfirm` |                         |
| `seed`   | JSON                | dungeon generation seed |

example:

```json
{
    "action": "dungeonendconfirm",
    "outcome": {}
}
```

This payload is sent after a client sends a dungeonend request to notify the
client that its Player has been updated.

### `GETMISSIONS`

| field    | type or value | about         |
| -------- | ------------- | ------------- |
| `action` | `getmissions` |               |
| `seed`   | string[]      | mission codes |

example:

```json
{
    "action": "getmissions",
    "missions": ["code1", "code2", "codeN"]
}
```

This payload is sent by the server upon client request. The session doesn't need
to be logged in for the server to send this payload. The codes returned are the
deterministic mission ids.

### `ACCEPTMISSION`

| field      | type or value   | about        |
| ---------- | --------------- | ------------ |
| `action`   | `acceptmission` |              |
| `mission`  | string[]        | mission code |
| `accepted` | 0, 1            |              |

example:

```json
{
    "action":"acceptmission",
    "mission":"missioncode",
    "accepted":1
}
```

This payload is sent by the server to the client to tell him if the player was
allowed to acept the mission or not. 1 means accepted, any other value means
it has been refused.
