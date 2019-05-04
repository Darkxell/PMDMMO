# Client & Server communication protocol

Last updated 17 May 2018

## Table of Contents

- [Client & Server communication protocol](#client--server-communication-protocol)
  - [Table of Contents](#table-of-contents)
  - [1. General information](#1-general-information)
  - [2. Communications](#2-communications)
    - [2.1. Required payload structure](#21-required-payload-structure)
    - [2.2. Encryption](#22-encryption)
  - [3. List of client &rarr; server payloads](#3-list-of-client-rarr-server-payloads)
  - [4. List of server &rarr; client payloads](#4-list-of-server-rarr-client-payloads)

## 1. General information

Communication between the game server and the clients is based around the
[Websocket] protocol.

It is the client's job to connect to the endpoint to send and be able to recieve
the appropriate information. The endpoint is available at the following adress:

```url
[server ip or linked dns]:8080/PMDMMOServer/game
```

Note: As of 30 Aug 2018, support for the chat endpoint has been fully removed.
Any mention of a chat endpoint is legacy and needs to be updated.

[Websocket]: https://tools.ietf.org/html/rfc6455

## 2. Communications

### 2.1. Required payload structure

The data sent to the server MUST be in a valid JSON structure, and encoded as
text in the websocket container. The server will ignore all binary payloads
recieved (This is subject to change, refer to the latest documentation folder on
github).

The server will not send a direct response to a client upon recieving a payload,
but can be expected to send a receipt back for most informations.

The JSON containing any payload must contain a String field called "action",
which is used to describe the content of the payload. The other informations are
payload dependent.

### 2.2. Encryption

Any payload sent by a logged client to the server can be encrypted using AES256.
The encryption key used must be the key sent by the client to the server in the
`SETENCRYPTIONKEY` payload.

To use encrypted payloads, the client can just encrypt the JSON string as an
encrypted string and send it in a clear text payload:

| field       | type or value | about            |
| ----------- | ------------- | ---------------- |
| `encrypted` | `1`           |                  |
| `value`     | string        | Encrypted value. |

example:

```json
{
    "encrypted": 1,
    "value": "YOUR ENCRYPTED VALUE HERE"
}
```

The server will encrypt any payload it sends to the client using the exact same
fashion.

The only exeptions to this is is the `SETENCRYPTIONKEY` payload. Refer to the
specific documentation of this payload for further information.

[Login challenge documentation](./hashing%20norms.md)

## 3. List of client &rarr; server payloads

[Go to documentation](./ws-api/client.md)

## 4. List of server &rarr; client payloads

[Go to documentation](./ws-api/server.md)
