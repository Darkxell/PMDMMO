package com.darkxell.client.mechanics.chat;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.darkxell.common.util.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

@ClientEndpoint
public class ChatClientEndpoint {
	
	public static final byte CONNECTING = 0, CONNECTED = 1, FAILED = 2;
	
	Session userSession = null;
	private ChatBox holder;
	private final URI endpointURI;
	private byte connectionStatus = CONNECTING;

	public ChatClientEndpoint(URI endpointURI, ChatBox parent) {
		this.holder = parent;
		this.endpointURI = endpointURI;
	}
	
	public void connect() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, this.endpointURI);
		} catch (Exception e) {
			e.printStackTrace();
			this.connectionStatus = FAILED;
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
		Logger.i("Chat connected to the server sucessfully.");
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
		Logger.i("Chat socket connection closed.");
		this.connectionStatus = FAILED;
		this.userSession = null;
	}

	/**
	 * Callback hook for Message Events. This method will be invoked when a
	 * client send a message.
	 *
	 * @param message
	 *            The text message
	 */
	@OnMessage
	public void onMessage(String message) {
		try {
			JsonValue obj = Json.parse(message);
			holder.messages.add(new ChatMessage(obj));
		} catch (Exception e) {
			Logger.w("Could not add the recieved message to messages list : " + message);
			e.printStackTrace();
		}

	}

	/**
	 * Send a message.
	 *
	 * @param message
	 */
	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}
}
