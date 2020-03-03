package com.darkxell.client.launchable.crypto;

import java.security.PublicKey;

import javax.crypto.SecretKey;

/**
 * Shitty makeshift safe for the client to store encryption keys and stuff. Everything's here, so a real safe
 * implementation could just redo this class.<br/>
 * As there is no key the client should not have, the informations contained here are only sensible if someone else than
 * the user gets to them, compromising the security of the connection between the client and the server.
 */
public class Safe {

    /** The server's public key for the SETENCRYPTIONKEY payload. */
    public static PublicKey serverPublicKey = null;

    /**
     * The reason the Safe class should actually be a safe. This is the secret key used to encrypt payloads symetricly
     * between the client and the server.
     */
    public static SecretKey symmetricKey = null;

    /**
     * Boolean flag set to true if the server acknowledged the symmetric key above.
     */
    public static boolean serverhaskey = false;

}
