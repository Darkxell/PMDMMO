# Progression

This document describes how the player progression is stored.
This document does not include inventory/team storage informations.

# Serverside storage

The player's progression is stored in a 2Bytes integer serverside defaulted at 0.
This value is given to the client when logging in and cannot be modified by the client direclty.
This value can be restrictive on specified payloads, especially dungeon completion ones.
As a rule of thumb, a higher value means further story progression. This is not safe to assume however, as diamond save points situations may happen.

## VALUES

| Value         | Description                                                           | Obtained |
|-|-|-|
|0|Default sever value. This value means the player must have no pokemons or items in his inventory. A 0 value means the player can send the personality test results| Account creation|
|1|Value when the player has sent a personality test result. This value puts the player in the butterfree cutscene|Upon recieving the personality test results|
|2|Value when the player has somehow failed the tiny woods|Upon failing the tiny woods if value is 1.|
|10|Value when you beat the tiny woods.|When you beat the tiny woods when value is below 10.|