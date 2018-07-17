package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.zones.FriendAreaFreezone;
import com.darkxell.common.zones.FreezoneInfo;

public class JungleFreezone extends FriendAreaFreezone
{

	public JungleFreezone()
	{
		super(28, 38, FreezoneInfo.FRIEND_JUNGLE);
		this.freezonebgm = "friendarea-wilds.mp3";
	}

}
