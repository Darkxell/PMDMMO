package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.zones.FriendAreaFreezone;
import com.darkxell.common.zones.FreezoneInfo;

public class SafariFreezone extends FriendAreaFreezone
{

	public SafariFreezone()
	{
		super(28, 38, FreezoneInfo.FRIEND_SAFARI);
		this.freezonebgm = "friendarea-steppe.mp3";
	}

}
