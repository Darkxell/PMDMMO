package com.darkxell.client.launchable;

import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.darkxell.client.mechanics.chat.ChatMessage;
import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

@ClientEndpoint
public class GameSocketEndpoint {

	public static final byte CONNECTING = 0, CONNECTED = 1, FAILED = 2;

	Session userSession = null;
	private URI endpointURI;
	private byte connectionStatus = CONNECTING;
	private Thread thr;

	public GameSocketEndpoint() {
		try {
			this.endpointURI = new URI("ws://" + ClientSettings.getSetting(ClientSettings.SERVER_ADDRESS) + "game");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		thr = new Thread(new Runnable() {

			@Override
			public void run() {
				connect();
			}
		});
		thr.start();
	}

	public void connect() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, this.endpointURI);
		} catch (Exception e) {
			e.printStackTrace();
			this.connectionStatus = FAILED;
			Logger.e("Could not connect to the server for game communication");
		}
	}

	public byte connectionStatus() {
		return this.connectionStatus;
	}

	/**
	 * Callback hook for Connection open events.
	 *
	 * @param userSession
	 *            the userSession which is opened.
	 */
	@OnOpen
	public void onOpen(Session userSession) {
		try {
			this.userSession = userSession;
			JsonObject mess = new JsonObject().add("action", "sessioninfo").add("name",
					ClientSettings.getSetting(ClientSettings.LOGIN));
			this.sendMessage(mess.toString());
			Logger.i("Game socket connected to the server sucessfully.");
			this.connectionStatus = CONNECTED;
		} catch (Exception e) {
			Logger.e("Could not aggree with the server for a valid session");
			e.printStackTrace();
		}

	}

	/**
	 * Callback hook for Connection close events.
	 * 
	 * @param userSession
	 *            the userSession which is getting closed.
	 * @param reason
	 *            the reason for connection close
	 */
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		Logger.i("Game socket connection closed.");
		this.connectionStatus = FAILED;
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when the
	 * server sends informations to the client.
	 *
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		try {
			JsonValue obj = Json.parse(message);
			Logger.d("Message from server : " + message);
		} catch (Exception e) {
			Logger.w("Could not read the recieved message from the server : " + message);
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to the server.
	 *
	 * @param message
	 */
	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}

}
