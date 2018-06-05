package com.darkxell.common.dungeon;

import com.darkxell.common.dungeon.TempIDRegistry.ItemsTempIDRegistry;
import com.darkxell.common.dungeon.TempIDRegistry.MovesTempIDRegistry;
import com.darkxell.common.dungeon.TempIDRegistry.PokemonTempIDRegistry;
import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.player.ItemContainer;
import com.darkxell.common.player.ItemContainer.ItemContainerType;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.util.Communicable.JsonReadingException;

public class CommunicationUtils
{
	public final ItemsTempIDRegistry itemIDs = new ItemsTempIDRegistry();
	public final MovesTempIDRegistry moveIDs = new MovesTempIDRegistry();
	public final PokemonTempIDRegistry pokemonIDs = new PokemonTempIDRegistry();

	public ItemContainer identifyContainer(Floor floor, ItemContainerType type, long id) throws JsonReadingException
	{
		try
		{
			switch (type)
			{
				case TILE:
					int y = (int) (id / floor.getWidth());
					int x = (int) (id - y * floor.getWidth());
					return floor.tileAt(x, y);

				case INVENTORY:
					for (Player p : floor.dungeon.exploringPlayers())
						if (p.inventory().containerID() == id) return p.inventory();
					throw new JsonReadingException("Inventory with id " + id + " doesn't match any known players inventory.");

				case DUNGEON_POKEMON:
				case POKEMON:
					Pokemon p = this.pokemonIDs.get(id);
					if (p == null) throw new JsonReadingException("No Pokémon with id " + id);
					return p;

				default:
					throw new JsonReadingException("ItemContainerType " + type + " CANNOT EXIST!!! WTF??");
			}
		} catch (JsonReadingException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new JsonReadingException("Couldn't find inventory with type " + type + " and id " + id);
		}
	}

}
