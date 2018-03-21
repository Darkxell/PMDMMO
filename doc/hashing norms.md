# Hashing norms

This document lists the different places where passwords informations are stored and how they are hashed.

#List of storage locations

<b>Textfield:</b>
<br/>Uptime: few seconds
<br/>Encryption: clear text
<br/>Notes: As this value is stored clientside for a short ammount of time, no encryption is needed. This opens vulnerabilities 
to clientside attacks. This policy is often used in web technologies and is not an issue on well secured devices.

<b>Internal clientside storage:</b>
<br/>Uptime: virtually forever
<br/>Encryption: sha256 hashing
<br/>Hashing algorithm: sha256(clear password+username+HASHSALTTYPE_CLIENT = "client");
<br/>Notes: This value is enough to authentificate if possessed. It is a salted hash of the actual username passowrd to prevent
user based multi application attacks, based of the fact that many users use the same password on all their applications.

<b>Internal server storage:</b>
<br/>Uptime: virtually forever
<br/>Encryption: sha256 hashing
<br/>Hashing algorithm: sha256(clientside storage hash+username+HASHSALTTYPE_SERVER = "server");
<br/>Notes: This value is the shared secret and is enough to authentificate. It must be communicated to the server using a
symetrical encryption algorithm, such as SSL. This value should only be communicated once to the server.
<br/>Also note that this hash is stored on the server, but also calculated and stored in ram by the client when connecting.


<b>Login payload:</b>
<br/>Uptime: few seconds
<br/>Encryption: sha256 hashing
<br/>Hashing algorithm: sha256(serverside storage hash+server generated salt+HASHSALTTYPE_LOGIN = "login");
<br/>Notes: This password hash is generated to login and acts as a unique use login token. The server salt used is generated
by the server upon depand of a socket connection. The client must hash his version of the serverside password hash (that he
should not store in durable storage) and send it to the server to initiate a logged session to the corresponding account.
The server must destroy the salt upon veryfying the answer.

<br/>All payloads should be secured by ssl.
