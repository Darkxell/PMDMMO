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

import com.darkxell.common.util.Logger;

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
			// this.holder.messages.add(new ChatMessage("", "Connection to the
			// server failed.", Color.RED, Color.RED, "ERROR", Color.RED));
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
		Logger.i("Game socket connected to the server sucessfully.");
		this.connectionStatus = CONNECTED;
		this.userSession = userSession;
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
		/*
		 * try { JsonValue obj = Json.parse(message); holder.messages.add(new
		 * ChatMessage(obj)); } catch (Exception e) {
		 * Logger.w("Could not add the recieved message to messages list : " +
		 * message); e.printStackTrace(); }
		 */

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
