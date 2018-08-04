package com.darkxell.common.dungeon;

import com.darkxell.common.dungeon.data.Dungeon;
import com.darkxell.common.dungeon.data.DungeonRegistry;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveRegistry;
import com.darkxell.common.util.Communicable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/** Object sent by server to client to describe the outcome of a dungeon exploration. */
public class DungeonOutcome implements Communicable
{

	public static enum Outcome
	{
		/** Team was sent back to base because they couldn't protect a client. */
		CLIENT_KO,
		/** Team has reached and completed the final floor of the Dungeon. */
		DUNGEON_CLEARED,
		/** Team has used an item to escape the Dungeon. */
		ESCAPED,
		/** Team was KO'ed. */
		KO,
		/** Team has chosen to leave the Dungeon after completing a mission. */
		MISSON_COMPLETE;
	}

	/** ID of the Dungeon the exploration ended in. */
	private int dungeonID;
	/** If outcome is KO, the ID of the move that killed the last remaining team member. -1 else. */
	private int moveID;
	/** What outcome the exploration had. */
	private Outcome outcome;

	public DungeonOutcome()
	{}

	public DungeonOutcome(Outcome outcome, int dungeonID)
	{
		this(outcome, dungeonID, -1);
	}

	public DungeonOutcome(Outcome outcome, int dungeonID, int moveID)
	{
		this.outcome = outcome;
		this.dungeonID = dungeonID;
		this.moveID = moveID;
	}

	public Dungeon dungeon()
	{
		return DungeonRegistry.find(this.dungeonID);
	}

	public int getDungeonID()
	{
		return this.dungeonID;
	}

	public int getMoveID()
	{
		return this.moveID;
	}

	public Outcome getOutcome()
	{
		return this.outcome;
	}

	public Move koMove()
	{
		return MoveRegistry.find(this.moveID);
	}

	@Override
	public void read(JsonObject value) throws JsonReadingException
	{
		try
		{
			this.outcome = Outcome.valueOf(value.getString("outcome", "null"));
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for outcome: " + value.get("outcome"));
		}

		try
		{
			this.dungeonID = value.getInt("dungeon", -1);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for dungeon ID: " + value.get("dungeon"));
		}

		if (this.outcome == Outcome.KO) try
		{
			this.moveID = value.getInt("move", -1);
		} catch (Exception e)
		{
			throw new JsonReadingException("Wrong value for move ID: " + value.get("move"));
		}
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject root = Json.object();
		root.add("outcome", this.outcome.name());
		root.add("dungeon", this.dungeonID);
		if (this.outcome == Outcome.KO) root.add("move", this.moveID);
		return root;
	}

}
