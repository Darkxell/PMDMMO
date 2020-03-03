package com.darkxell.client.mechanics.freezones.zones.friend;

import com.darkxell.client.mechanics.freezones.zones.FriendAreaFreezone;
import com.darkxell.common.zones.FreezoneInfo;

public class MtDisciplineFreezone extends FriendAreaFreezone
{

	public MtDisciplineFreezone()
	{
		super(30, 36, FreezoneInfo.FRIEND_MTDISCIPLINE);
		this.freezonebgm = "friendarea-wilds.mp3";
	}

}
