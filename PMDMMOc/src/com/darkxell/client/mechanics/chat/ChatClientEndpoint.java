package com.darkxell.client.mechanics.chat;

import java.awt.Color;
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

@ClientEndpoint
public class ChatClientEndpoint {

	Session userSession = null;
	private ChatBox holder;

	public ChatClientEndpoint(URI endpointURI, ChatBox parent) {
		this.holder = parent;
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		holder.messages.add(new ChatMessage("User", message, Color.WHITE, Color.WHITE, "DEV", Color.RED));
		// TODO: change the message attributes to match the recived JSON string.
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
