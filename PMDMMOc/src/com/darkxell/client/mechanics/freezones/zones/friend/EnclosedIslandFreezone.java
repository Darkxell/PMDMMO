package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.zones.FriendAreaFreezone;
import com.darkxell.common.zones.FreezoneInfo;

public class EnclosedIslandFreezone extends FriendAreaFreezone
{

	public EnclosedIslandFreezone()
	{
		super(23, 36, FreezoneInfo.FRIEND_ENCLOSEDISLAND);
		this.freezonebgm = "friendarea-enclosedisland.mp3";
	}

}
