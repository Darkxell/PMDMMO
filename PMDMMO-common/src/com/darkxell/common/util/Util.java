package com.darkxell.common.util;

import java.util.Random;

import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.player.Player;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.Pokemon;
import com.darkxell.common.pokemon.PokemonRegistry;

public class Util
{

	public static Player createDefaultPlayer()
	{
		final int starter = 4;
		Player player = new Player("Offline debug account name", PokemonRegistry.find(starter).generate(new Random(), 10, 1));
		player.setStoryPosition(500);
		player.setMoneyInBag(100);
		player.setMoneyInBank(456789);
		player.addAlly(PokemonRegistry.find(starter).generate(new Random(), 80));
		// player.addAlly(PokemonRegistry.find(255).generate(new Random(), 80));
		player.getTeamLeader().setItem(new ItemStack(208));
		player.getTeamLeader().setMove(0, new LearnedMove(352));
		player.getTeamLeader().setMove(1, new LearnedMove(1));
		player.getTeamLeader().setMove(2, new LearnedMove(404));
		player.getTeamLeader().setMove(3, new LearnedMove(809));
		// player.getTeamLeader().getData().abilityid = 42;

		for (int i = 1; i < 13; ++i)
			player.inventory().addItem(new ItemStack(i));
		player.inventory().addItem(new ItemStack(86));
		for (int i = 21; i < 22; ++i)
			player.inventory().addItem(new ItemStack(i, 6));
		player.inventory().addItem(new ItemStack(31, 3));
		player.inventory().addItem(new ItemStack(42, 3));
		player.inventory().addItem(new ItemStack(223));

		for (int id = 201; id <= 386; id += 3)
		{
			Pokemon p = PokemonRegistry.find(id).generate(new Random(), 1);
			p.getData().id = id;
			player.addPokemonInZone(p);
		}

		for (int id = 10001; id <= 10033; id += 2)
		{
			Pokemon p = PokemonRegistry.find(id).generate(new Random(), 1);
			p.getData().id = id;
			player.addPokemonInZone(p);
		}

		try
		{
			player.getData().missionsids
					.add(new Mission("E", 1, 2, 3, 6, 1, new MissionReward(55, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_RESCUEHIM).toString());
			player.getData().missionsids
					.add(new Mission("A", 12, 14, 15, 71, 2, new MissionReward(70, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_DEFEAT).toString());
			player.getData().missionsids
					.add(new Mission("A", 12, 15, 15, 71, 2, new MissionReward(70, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_DEFEAT).toString());
			player.getData().missionsids
					.add(new Mission("C", 12, 14, 15, 71, 2, new MissionReward(70, new int[] { 1 }, new int[] { 1 }, 5, null), Mission.TYPE_ESCORT).toString());
		} catch (InvalidParammetersException e)
		{
			e.printStackTrace();
		}

		return player;
	}

}
