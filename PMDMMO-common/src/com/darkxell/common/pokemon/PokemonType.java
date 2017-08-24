package com.darkxell.common.pokemon;

import java.awt.Color;

/** Pokémon types. */
public enum PokemonType
{

	NORMAL(0, "normal", new Color(168, 168, 120)),
	FIGHT(1, "fighting", new Color(192, 48, 40)),
	FLYING(2, "flying", new Color(168, 144, 240)),
	POISON(3, "poison", new Color(160, 64, 160)),
	GROUND(4, "ground", new Color(224, 192, 104)),
	ROCK(5, "rock", new Color(184, 160, 56)),
	BUG(6, "bug", new Color(168, 184, 32)),
	GHOST(7, "ghost", new Color(112, 88, 152)),
	STEEL(8, "steel", new Color(184, 184, 208)),
	FIRE(9, "fire", new Color(240, 128, 48)),
	WATER(10, "water", new Color(104, 144, 240)),
	GRASS(11, "grass", new Color(120, 200, 80)),
	ELECTR(12, "electric", new Color(248, 208, 48)),
	PSYCHC(13, "psychic", new Color(248, 88, 136)),
	ICE(14, "ice", new Color(152, 216, 216)),
	DRAGON(15, "dragon", new Color(112, 56, 248)),
	DARK(16, "dark", new Color(112, 88, 72)),
	FAIRY(17, "fairy", new Color(238, 153, 172));

	public static final float SUPER_EFFECTIVE = 2, NORMALLY_EFFECTIVE = 1, NOT_VERY_EFFECTIVE = 0.5f, NO_EFFECT = 0;

	static
	{
		NORMAL.setEffectivenessOn(GHOST, NO_EFFECT);
		NORMAL.setEffectivenessOn(ROCK, NOT_VERY_EFFECTIVE);
		NORMAL.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);

		FIGHT.setEffectivenessOn(GHOST, NO_EFFECT);
		FIGHT.setEffectivenessOn(FLYING, NOT_VERY_EFFECTIVE);
		FIGHT.setEffectivenessOn(POISON, NOT_VERY_EFFECTIVE);
		FIGHT.setEffectivenessOn(BUG, NOT_VERY_EFFECTIVE);
		FIGHT.setEffectivenessOn(PSYCHC, NOT_VERY_EFFECTIVE);
		FIGHT.setEffectivenessOn(FAIRY, NOT_VERY_EFFECTIVE);
		FIGHT.setEffectivenessOn(NORMAL, SUPER_EFFECTIVE);
		FIGHT.setEffectivenessOn(ROCK, SUPER_EFFECTIVE);
		FIGHT.setEffectivenessOn(STEEL, SUPER_EFFECTIVE);
		FIGHT.setEffectivenessOn(ICE, SUPER_EFFECTIVE);
		FIGHT.setEffectivenessOn(DARK, SUPER_EFFECTIVE);

		FLYING.setEffectivenessOn(ROCK, NOT_VERY_EFFECTIVE);
		FLYING.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		FLYING.setEffectivenessOn(ELECTR, NOT_VERY_EFFECTIVE);
		FLYING.setEffectivenessOn(FIGHT, SUPER_EFFECTIVE);
		FLYING.setEffectivenessOn(BUG, SUPER_EFFECTIVE);
		FLYING.setEffectivenessOn(GRASS, SUPER_EFFECTIVE);

		POISON.setEffectivenessOn(STEEL, NO_EFFECT);
		POISON.setEffectivenessOn(POISON, NOT_VERY_EFFECTIVE);
		POISON.setEffectivenessOn(GROUND, NOT_VERY_EFFECTIVE);
		POISON.setEffectivenessOn(ROCK, NOT_VERY_EFFECTIVE);
		POISON.setEffectivenessOn(GHOST, NOT_VERY_EFFECTIVE);
		POISON.setEffectivenessOn(GRASS, SUPER_EFFECTIVE);
		POISON.setEffectivenessOn(FAIRY, SUPER_EFFECTIVE);

		GROUND.setEffectivenessOn(FLYING, NO_EFFECT);
		GROUND.setEffectivenessOn(BUG, NOT_VERY_EFFECTIVE);
		GROUND.setEffectivenessOn(GRASS, NOT_VERY_EFFECTIVE);
		GROUND.setEffectivenessOn(POISON, SUPER_EFFECTIVE);
		GROUND.setEffectivenessOn(ROCK, SUPER_EFFECTIVE);
		GROUND.setEffectivenessOn(STEEL, SUPER_EFFECTIVE);
		GROUND.setEffectivenessOn(FIRE, SUPER_EFFECTIVE);
		GROUND.setEffectivenessOn(ELECTR, SUPER_EFFECTIVE);

		ROCK.setEffectivenessOn(FIGHT, NOT_VERY_EFFECTIVE);
		ROCK.setEffectivenessOn(GROUND, NOT_VERY_EFFECTIVE);
		ROCK.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		ROCK.setEffectivenessOn(FLYING, SUPER_EFFECTIVE);
		ROCK.setEffectivenessOn(BUG, SUPER_EFFECTIVE);
		ROCK.setEffectivenessOn(FIRE, SUPER_EFFECTIVE);
		ROCK.setEffectivenessOn(ICE, SUPER_EFFECTIVE);

		BUG.setEffectivenessOn(FIGHT, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(FLYING, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(POISON, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(GHOST, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(ELECTR, NOT_VERY_EFFECTIVE);
		BUG.setEffectivenessOn(GRASS, SUPER_EFFECTIVE);
		BUG.setEffectivenessOn(PSYCHC, SUPER_EFFECTIVE);
		BUG.setEffectivenessOn(DARK, SUPER_EFFECTIVE);

		GHOST.setEffectivenessOn(NORMAL, NO_EFFECT);
		GHOST.setEffectivenessOn(DARK, NOT_VERY_EFFECTIVE);
		GHOST.setEffectivenessOn(GHOST, SUPER_EFFECTIVE);
		GHOST.setEffectivenessOn(PSYCHC, SUPER_EFFECTIVE);

		STEEL.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		STEEL.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		STEEL.setEffectivenessOn(WATER, NOT_VERY_EFFECTIVE);
		STEEL.setEffectivenessOn(ELECTR, NOT_VERY_EFFECTIVE);
		STEEL.setEffectivenessOn(ROCK, SUPER_EFFECTIVE);
		STEEL.setEffectivenessOn(ICE, SUPER_EFFECTIVE);
		STEEL.setEffectivenessOn(FAIRY, SUPER_EFFECTIVE);

		FIRE.setEffectivenessOn(ROCK, NOT_VERY_EFFECTIVE);
		FIRE.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		FIRE.setEffectivenessOn(WATER, NOT_VERY_EFFECTIVE);
		FIRE.setEffectivenessOn(DRAGON, NOT_VERY_EFFECTIVE);
		FIRE.setEffectivenessOn(BUG, SUPER_EFFECTIVE);
		FIRE.setEffectivenessOn(STEEL, SUPER_EFFECTIVE);
		FIRE.setEffectivenessOn(GRASS, SUPER_EFFECTIVE);
		FIRE.setEffectivenessOn(ICE, SUPER_EFFECTIVE);

		WATER.setEffectivenessOn(WATER, NOT_VERY_EFFECTIVE);
		WATER.setEffectivenessOn(GRASS, NOT_VERY_EFFECTIVE);
		WATER.setEffectivenessOn(DRAGON, NOT_VERY_EFFECTIVE);
		WATER.setEffectivenessOn(GROUND, SUPER_EFFECTIVE);
		WATER.setEffectivenessOn(ROCK, SUPER_EFFECTIVE);
		WATER.setEffectivenessOn(FIRE, SUPER_EFFECTIVE);

		GRASS.setEffectivenessOn(FLYING, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(POISON, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(BUG, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(GRASS, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(DRAGON, NOT_VERY_EFFECTIVE);
		GRASS.setEffectivenessOn(GROUND, SUPER_EFFECTIVE);
		GRASS.setEffectivenessOn(ROCK, SUPER_EFFECTIVE);
		GRASS.setEffectivenessOn(WATER, SUPER_EFFECTIVE);

		ELECTR.setEffectivenessOn(GROUND, NO_EFFECT);
		ELECTR.setEffectivenessOn(GRASS, NOT_VERY_EFFECTIVE);
		ELECTR.setEffectivenessOn(ELECTR, NOT_VERY_EFFECTIVE);
		ELECTR.setEffectivenessOn(DRAGON, NOT_VERY_EFFECTIVE);
		ELECTR.setEffectivenessOn(FLYING, SUPER_EFFECTIVE);
		ELECTR.setEffectivenessOn(WATER, SUPER_EFFECTIVE);

		PSYCHC.setEffectivenessOn(DARK, NO_EFFECT);
		PSYCHC.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		PSYCHC.setEffectivenessOn(PSYCHC, NOT_VERY_EFFECTIVE);
		PSYCHC.setEffectivenessOn(FIGHT, SUPER_EFFECTIVE);
		PSYCHC.setEffectivenessOn(POISON, SUPER_EFFECTIVE);

		ICE.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		ICE.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		ICE.setEffectivenessOn(WATER, NOT_VERY_EFFECTIVE);
		ICE.setEffectivenessOn(ICE, NOT_VERY_EFFECTIVE);
		ICE.setEffectivenessOn(FLYING, SUPER_EFFECTIVE);
		ICE.setEffectivenessOn(GROUND, SUPER_EFFECTIVE);
		ICE.setEffectivenessOn(GRASS, SUPER_EFFECTIVE);
		ICE.setEffectivenessOn(DRAGON, SUPER_EFFECTIVE);

		DRAGON.setEffectivenessOn(FAIRY, NO_EFFECT);
		DRAGON.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		DRAGON.setEffectivenessOn(DRAGON, NOT_VERY_EFFECTIVE);

		DARK.setEffectivenessOn(FIGHT, NOT_VERY_EFFECTIVE);
		DARK.setEffectivenessOn(DARK, NOT_VERY_EFFECTIVE);
		DARK.setEffectivenessOn(FAIRY, NOT_VERY_EFFECTIVE);
		DARK.setEffectivenessOn(GHOST, SUPER_EFFECTIVE);
		DARK.setEffectivenessOn(PSYCHC, SUPER_EFFECTIVE);

		FAIRY.setEffectivenessOn(POISON, NOT_VERY_EFFECTIVE);
		FAIRY.setEffectivenessOn(STEEL, NOT_VERY_EFFECTIVE);
		FAIRY.setEffectivenessOn(FIRE, NOT_VERY_EFFECTIVE);
		FAIRY.setEffectivenessOn(FIGHT, SUPER_EFFECTIVE);
		FAIRY.setEffectivenessOn(DRAGON, SUPER_EFFECTIVE);
		FAIRY.setEffectivenessOn(DARK, SUPER_EFFECTIVE);
	}

	public final Color color;
	private final float[] effectiveness;
	public final int id;
	public final String name;

	private PokemonType(int id, String name, Color color)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.effectiveness = new float[]
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	}

	/** @return The effectiveness of this type on a single-type. */
	public float effectivenessOn(PokemonType type)
	{
		return this.effectiveness[type.id];
	}

	/** @return The effectiveness of this type on a dual-type. */
	public float effectivenessOn(PokemonType type1, PokemonType type2)
	{
		return this.effectiveness[type1.id] * this.effectiveness[type2.id];
	}

	/** Sets the effectiveness of this type on the input type. */
	private void setEffectivenessOn(PokemonType type, float effectiveness)
	{
		this.effectiveness[type.id] = effectiveness;
	}

}
