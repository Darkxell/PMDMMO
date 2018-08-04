package com.darkxell.common.util;

import java.util.Random;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;

public class Util
{

	public static Player createDefaultPlayer()
	{
		Player player = new Player("Offline debug account name", PokemonRegistry.find(4).generate(new Random(), 10));
		player.setStoryPosition(1);
		player.setMoneyInBag(100);
		player.setMoneyInBank(456789);
		player.addAlly(PokemonRegistry.find(1).generate(new Random(), 80, 1));
		// player.addAlly(PokemonRegistry.find(255).generate(new Random(), 80));
		player.getTeamLeader().setItem(new ItemStack(208));
		player.getTeamLeader().setMove(2, new LearnedMove(45));
		player.getTeamLeader().setMove(3, new LearnedMove(806));

		for (int i = 1; i < 5; ++i)
			player.inventory().addItem(new ItemStack(i));
		player.inventory().addItem(new ItemStack(86));
		for (int i = 21; i < 28; ++i)
			player.inventory().addItem(new ItemStack(i, i - 20));
		player.inventory().addItem(new ItemStack(250));

		for (int id = 201; id <= 386; id += 3)
		{
			Pokemon p = PokemonRegistry.find(id).generate(new Random(), 1);
			p.getData().id = id;
			player.addPokemonInZone(p);
		}

		return player;
	}

}
