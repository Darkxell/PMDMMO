package com.darkxell.client.launchable.crypto;

import java.security.PublicKey;

/**
 * Shitty makeshift safe for the client to store encryption keys and stuff.
 * Everything's here, so a real safe implementation could just redo this
 * class.<br/>
 * As there is no key the client should not have, the informations contained
 * here are only sensible if someone else than the user gets to them,
 * compromising the security of the connection between the client and the
 * server.
 */
public class Safe {

	/** The server's public key for the SETENCRYPTIONKEY payload. */
	public static PublicKey serverPublicKey = null;

}
