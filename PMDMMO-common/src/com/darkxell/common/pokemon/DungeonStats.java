package com.darkxell.common.pokemon;

import java.util.ArrayList;

import com.darkxell.common.dungeon.floor.Floor;
import com.darkxell.common.event.DungeonEvent;
import com.darkxell.common.event.stats.SpeedChangedEvent;
import com.darkxell.common.event.turns.GameTurn;
import com.darkxell.common.pokemon.BaseStats.Stat;

public class DungeonStats
{
	public static final int[] accuracyTable = new int[] { 84, 89, 94, 102, 110, 115, 140, 153, 179, 204, 256, 332, 409, 422, 435, 448, 460, 473, 486, 512 };
	public static final int[] attackTable = new int[] { 64, 69, 74, 79, 84, 89, 102, 115, 128, 179, 256, 332, 384, 406, 422, 435, 448, 460, 473, 486, 512 };
	public static final double DEFAULT_ACCURACY = 1, DEFAULT_EVASIVENESS = 1;
	public static final int[] defenseTable = new int[] { 64, 69, 74, 79, 84, 89, 102, 140, 179, 222, 256, 332, 384, 409, 422, 435, 448, 460, 473, 486, 512 };
	public static final int[] evasivenessTable = new int[] { 512, 486, 473, 460, 448, 435, 422, 409, 384, 345, 256, 204, 179, 153, 128, 102, 89, 76, 64, 51,
			38 };
	public static final float[] speedTable = { .5f, 1f, 2f, 3f, 4f };

	/** Accuracy. */
	private double accuracy;
	/** Attack. */
	private int attack;
	/** The base stats. */
	public final BaseStats baseStats;
	/** Defense. */
	private int defense;
	/** Evasiveness. */
	private double evasiveness;
	/** Health Points. */
	private int health;
	/** Owner of these stats. */
	public final DungeonPokemon pokemon;
	/** Special Attack. */
	private int specialAttack;
	/** Special Defense. */
	private int specialDefense;
	private int[] speedBuffs = new int[GameTurn.SUB_TURNS - 1], speedDebuffs = new int[GameTurn.SUB_TURNS - 1];
	private int[] stages = new int[] { 10, 10, 10, 10, 10, 10, 10 };

	public DungeonStats(DungeonPokemon pokemon)
	{
		this.pokemon = pokemon;
		this.baseStats = this.pokemon.getBaseStats();
		this.health = this.baseStats.health;
		this.onStatChange();
		this.resetStages();
	}

	public void addStage(Stat stat, int stage)
	{
		this.setStage(stat, this.getStage(stat) + stage);
	}

	@Override
	public DungeonStats clone()
	{
		DungeonStats s = new DungeonStats(this.pokemon);
		s.accuracy = this.accuracy;
		s.attack = this.attack;
		s.defense = this.defense;
		s.evasiveness = this.evasiveness;
		s.health = this.health;
		s.specialAttack = this.specialAttack;
		s.specialDefense = this.specialDefense;
		s.speedBuffs = this.speedBuffs.clone();
		s.speedDebuffs = this.speedDebuffs.clone();
		s.stages = this.stages.clone();
		return s;
	}

	public int effectiveChange(Stat stat, int stage)
	{
		switch (stat)
		{
			case Speed:
				if (stage < 0 && this.speedDebuffs() == this.speedDebuffs.length) return 0;
				if (stage > 0 && this.speedBuffs() == this.speedBuffs.length) return 0;
				return stage;

			default:
				if (this.stages[stat.id] + stage < 0) return -this.stages[stat.id];
				if (this.stages[stat.id] + stage >= 20) return 20 - this.stages[stat.id];
				return stage;
		}
	}

	public double getAccuracy()
	{
		return this.accuracy * accuracyTable[this.stages[Stat.Accuracy.id]] / 256;
	}

	public int getAttack()
	{
		return this.attack * attackTable[this.stages[Stat.Attack.id]] / 256;
	}

	public int getDefense()
	{
		return this.defense * defenseTable[this.stages[Stat.Defense.id]] / 256;
	}

	public double getEvasiveness()
	{
		return this.evasiveness * evasivenessTable[this.stages[Stat.Evasiveness.id]] / 256;
	}

	public int getHealth()
	{
		return this.health;
	}

	public float getMoveSpeed()
	{
		return speedTable[this.getStage(Stat.Speed)];
	}

	public int getSpecialAttack()
	{
		return this.specialAttack * attackTable[this.stages[Stat.SpecialAttack.id]] / 256;
	}

	public int getSpecialDefense()
	{
		return this.specialDefense * defenseTable[this.stages[Stat.SpecialDefense.id]] / 256;
	}

	public int getStage(Stat stat)
	{
		if (stat == Stat.Speed) return Math.max(0, this.speedBuffs() - this.speedDebuffs() + 1);
		return this.stages[stat.id];
	}

	public double getStat(Stat stat)
	{
		switch (stat)
		{
			case Accuracy:
				return this.getAccuracy();

			case Attack:
				return this.getAttack();

			case Defense:
				return this.getDefense();

			case Evasiveness:
				return this.getEvasiveness();

			case Health:
				return this.getHealth();

			case SpecialAttack:
				return this.getSpecialAttack();

			case SpecialDefense:
				return this.getSpecialDefense();

			case Speed:
				return this.getMoveSpeed();

			default:
				return 0;
		}
	}

	public boolean hasAStatDown()
	{
		for (Stat s : Stat.values())
			if (s != Stat.Speed && this.getStage(s) < 10) return true;
			else if (s == Stat.Speed && this.getStage(s) < 1) return true;
		return false;
	}

	public void onFloorStart(Floor floor, ArrayList<DungeonEvent> events)
	{
		int speed = this.getStage(Stat.Speed);

		for (int i = 0; i < this.speedBuffs.length; ++i)
			this.speedBuffs[i] = 0;
		for (int i = 0; i < this.speedDebuffs.length; ++i)
			this.speedDebuffs[i] = 0;

		int newSpeed = this.getStage(Stat.Speed);

		if (speed != newSpeed)
		{
			SpeedChangedEvent e = new SpeedChangedEvent(floor, this.pokemon);
			e.displayMessages = false;
			events.add(e);
		}
	}

	/** Called when the base stats change. */
	public void onStatChange()
	{
		int hpchange = this.baseStats.health - this.health;
		this.attack = this.baseStats.attack;
		this.defense = this.baseStats.defense;
		this.health = this.baseStats.health;
		this.specialAttack = this.baseStats.specialAttack;
		this.specialDefense = this.baseStats.specialDefense;
		this.evasiveness = DEFAULT_EVASIVENESS;
		this.accuracy = DEFAULT_ACCURACY;
		this.pokemon.setHP(this.pokemon.getHp() + hpchange);
	}

	public void onTurnStart(Floor floor, ArrayList<DungeonEvent> events)
	{
		int speed = this.getStage(Stat.Speed);

		for (int i = 0; i < this.speedBuffs.length; ++i)
			if (this.speedBuffs[i] > 0) --this.speedBuffs[i];
		for (int i = 0; i < this.speedDebuffs.length; ++i)
			if (this.speedDebuffs[i] > 0) --this.speedDebuffs[i];

		int newSpeed = this.getStage(Stat.Speed);

		if (speed != newSpeed) events.add(new SpeedChangedEvent(floor, this.pokemon));
	}

	/** Called when the Pokemon steps on a Wonder Tile or changes Floor. */
	public void resetStages()
	{
		for (int i = 0; i < this.stages.length; i++)
			this.stages[i] = 10;
	}

	public void setStage(Stat stat, int stage)
	{
		if (stat == Stat.Speed)
		{
			if (stage > 0)
			{
				for (int i = 0; i < this.speedBuffs.length; ++i)
					if (this.speedBuffs[i] == 0)
					{
						this.speedBuffs[i] = 15;
						break;
					}
			} else for (int i = 0; i < this.speedDebuffs.length; ++i)
				if (this.speedDebuffs[i] == 0)
				{
					this.speedDebuffs[i] = 15;
					break;
				}
		} else
		{
			if (stage < 0) stage = 0;
			else if (stage > 20) stage = 20;

			this.stages[stat.id] = stage;
		}
	}

	public int speedBuffs()
	{
		int b = 0;
		for (int buff : this.speedBuffs)
			if (buff != 0) ++b;
		return b;
	}

	public int speedDebuffs()
	{
		int b = 0;
		for (int debuff : this.speedDebuffs)
			if (debuff != 0) ++b;
		return b;
	}

}
