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

import com.darkxell.client.launchable.messagehandlers.BankActionConfirmHandler;
import com.darkxell.client.launchable.messagehandlers.DungeonStartConfirmHandler;
import com.darkxell.client.launchable.messagehandlers.FreezonePositionHandler;
import com.darkxell.client.launchable.messagehandlers.InventoryRequestHandler;
import com.darkxell.client.launchable.messagehandlers.ItemActionHandler;
import com.darkxell.client.launchable.messagehandlers.LoginPlayerHandler;
import com.darkxell.client.launchable.messagehandlers.LogininfoHandler;
import com.darkxell.client.launchable.messagehandlers.MonsterRequestHandler;
import com.darkxell.client.launchable.messagehandlers.ObjectRequestHandler;
import com.darkxell.client.launchable.messagehandlers.SaltResetHandler;
import com.darkxell.client.launchable.messagehandlers.TestResultConfirmHandler;
import com.darkxell.common.util.Communicable;
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
			Logger.e("Could not create a valid URI to: ws://" + ClientSettings.getSetting(ClientSettings.SERVER_ADDRESS)
					+ "game");
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
			this.connectionStatus = FAILED;
			Logger.e("Could not connect to the server for game communication : " + e.toString());
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
		this.userSession = userSession;
		Logger.i("Game socket connected to the server sucessfully.");
		this.connectionStatus = CONNECTED;
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
			String actionstring = obj.asObject().getString("action", "");
			switch (actionstring) {
			case "freezoneposition":
				new FreezonePositionHandler().handleMessage(obj.asObject());
				break;
			case "saltreset":
				new SaltResetHandler().handleMessage(obj.asObject());
				break;
			case "login":
				new LoginPlayerHandler().handleMessage(obj.asObject());
				break;
			case "objectrequest":
				new ObjectRequestHandler().handleMessage(obj.asObject());
				break;
			case "requestinventory":
				new InventoryRequestHandler().handleMessage(obj.asObject());
				break;
			case "requestmonster":
				new MonsterRequestHandler().handleMessage(obj.asObject());
				break;
			case "testresultrecieved":
				new TestResultConfirmHandler().handleMessage(obj.asObject());
				break;
			case "logininfo":
				new LogininfoHandler().handleMessage(obj.asObject());
				break;
			case "itemactionresult":
				new ItemActionHandler().handleMessage(obj.asObject());
				break;
			case "bankactionconfirm":
				new BankActionConfirmHandler().handleMessage(obj.asObject());
				break;
				
			// DUNGEON COMMUNICATION

			case "dungeonstartconfirm":
				new DungeonStartConfirmHandler().handleMessage(obj.asObject());
				break;
			case "dungeonendconfirm":
				new DungeonEndConfirmHandler().handleMessage(obj.asObject());
				break;
			default:
				Logger.w("Unrecognized message from the server : " + message);
				break;
			}
		} catch (Exception e) {
			Logger.w("Could not read the recieved message from the server : " + message);
			e.printStackTrace();
		}
	}

	/**
	 * Shortcut to send a message to the server with only an action value and a
	 * communicable.
	 */
	public void sendMessage(String action, String name, Communicable value) {
		this.sendMessage(action, name, value.toJson());
	}

	/**
	 * Shortcut to send a message to the server containing only an action value
	 * and a Json object.
	 */
	public void sendMessage(String action, String name, JsonObject value) {
		JsonObject message = Json.object();
		message.add("action", action);
		message.add(name, value);
		this.sendMessage(message.toString());
	}

	/**
	 * Send a message to the server. This message is in string form and will not
	 * be wrapped in a JSON container.
	 *
	 * @param message
	 */
	public void sendMessage(String message) {
		try {
			this.userSession.getAsyncRemote().sendText(message);
		} catch (Exception e) {
			Logger.e("Could not send message to server socket.");
		}

	}

	/**
	 * Shortcut to send a message to the server requesting an inventory value.
	 */
	public void requestInventory(long id)
	{
		JsonObject message = Json.object();
		message.add("action", "requestinventory");
		message.add("id", id);
		this.sendMessage(message.toString());
	}

	/**
	 * Shortcut to send a message to the server requesting a Pokemon value.
	 */
	public void requestMonster(long id)
	{
		JsonObject message = Json.object();
		message.add("action", "requestmonster");
		message.add("id", id);
		this.sendMessage(message.toString());
	}

	/**
	 * Shortcut to send a message to the server requesting an object value.
	 */
	public void requestObject(String objectType, long id)
	{
		JsonObject message = Json.object();
		message.add("action", "objectrequest");
		message.add("id", id);
		message.add("type", objectType);
		this.sendMessage(message.toString());
	}

}
