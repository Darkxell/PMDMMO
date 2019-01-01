package com.darkxell.common.pokemon;

import java.awt.Color;

import com.darkxell.common.util.language.Message;

/** Pokemon types. */
public enum PokemonType {

	Normal(0, new Color(168, 168, 120)),
	Fighting(1, new Color(192, 48, 40)),
	Flying(2, new Color(168, 144, 240)),
	Poison(3, new Color(160, 64, 160)),
	Ground(4, new Color(224, 192, 104)),
	Rock(5, new Color(184, 160, 56)),
	Bug(6, new Color(168, 184, 32)),
	Ghost(7, new Color(112, 88, 152)),
	Steel(8, new Color(184, 184, 208)),
	Fire(9, new Color(240, 128, 48)),
	Water(10, new Color(104, 144, 240)),
	Grass(11, new Color(120, 200, 80)),
	Electric(12, new Color(248, 208, 48)),
	Psychic(13, new Color(248, 88, 136)),
	Ice(14, new Color(152, 216, 216)),
	Dragon(15, new Color(112, 56, 248)),
	Dark(16, new Color(112, 88, 72)),
	Fairy(17, new Color(238, 153, 172)),
	Unknown(19, new Color(104, 160, 144));

	public static final double SUPER_EFFECTIVE = 1.4, NORMALLY_EFFECTIVE = 1., NOT_VERY_EFFECTIVE = .7, NO_EFFECT = 0.;

	static {
		Normal.setEffectivenessOn(Ghost, NO_EFFECT);
		Normal.setEffectivenessOn(Rock, NOT_VERY_EFFECTIVE);
		Normal.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);

		Fighting.setEffectivenessOn(Ghost, NO_EFFECT);
		Fighting.setEffectivenessOn(Flying, NOT_VERY_EFFECTIVE);
		Fighting.setEffectivenessOn(Poison, NOT_VERY_EFFECTIVE);
		Fighting.setEffectivenessOn(Bug, NOT_VERY_EFFECTIVE);
		Fighting.setEffectivenessOn(Psychic, NOT_VERY_EFFECTIVE);
		Fighting.setEffectivenessOn(Fairy, NOT_VERY_EFFECTIVE);
		Fighting.setEffectivenessOn(Normal, SUPER_EFFECTIVE);
		Fighting.setEffectivenessOn(Rock, SUPER_EFFECTIVE);
		Fighting.setEffectivenessOn(Steel, SUPER_EFFECTIVE);
		Fighting.setEffectivenessOn(Ice, SUPER_EFFECTIVE);
		Fighting.setEffectivenessOn(Dark, SUPER_EFFECTIVE);

		Flying.setEffectivenessOn(Rock, NOT_VERY_EFFECTIVE);
		Flying.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Flying.setEffectivenessOn(Electric, NOT_VERY_EFFECTIVE);
		Flying.setEffectivenessOn(Fighting, SUPER_EFFECTIVE);
		Flying.setEffectivenessOn(Bug, SUPER_EFFECTIVE);
		Flying.setEffectivenessOn(Grass, SUPER_EFFECTIVE);

		Poison.setEffectivenessOn(Steel, NO_EFFECT);
		Poison.setEffectivenessOn(Poison, NOT_VERY_EFFECTIVE);
		Poison.setEffectivenessOn(Ground, NOT_VERY_EFFECTIVE);
		Poison.setEffectivenessOn(Rock, NOT_VERY_EFFECTIVE);
		Poison.setEffectivenessOn(Ghost, NOT_VERY_EFFECTIVE);
		Poison.setEffectivenessOn(Grass, SUPER_EFFECTIVE);
		Poison.setEffectivenessOn(Fairy, SUPER_EFFECTIVE);

		Ground.setEffectivenessOn(Flying, NO_EFFECT);
		Ground.setEffectivenessOn(Bug, NOT_VERY_EFFECTIVE);
		Ground.setEffectivenessOn(Grass, NOT_VERY_EFFECTIVE);
		Ground.setEffectivenessOn(Poison, SUPER_EFFECTIVE);
		Ground.setEffectivenessOn(Rock, SUPER_EFFECTIVE);
		Ground.setEffectivenessOn(Steel, SUPER_EFFECTIVE);
		Ground.setEffectivenessOn(Fire, SUPER_EFFECTIVE);
		Ground.setEffectivenessOn(Electric, SUPER_EFFECTIVE);

		Rock.setEffectivenessOn(Fighting, NOT_VERY_EFFECTIVE);
		Rock.setEffectivenessOn(Ground, NOT_VERY_EFFECTIVE);
		Rock.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Rock.setEffectivenessOn(Flying, SUPER_EFFECTIVE);
		Rock.setEffectivenessOn(Bug, SUPER_EFFECTIVE);
		Rock.setEffectivenessOn(Fire, SUPER_EFFECTIVE);
		Rock.setEffectivenessOn(Ice, SUPER_EFFECTIVE);

		Bug.setEffectivenessOn(Fighting, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Flying, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Poison, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Ghost, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Electric, NOT_VERY_EFFECTIVE);
		Bug.setEffectivenessOn(Grass, SUPER_EFFECTIVE);
		Bug.setEffectivenessOn(Psychic, SUPER_EFFECTIVE);
		Bug.setEffectivenessOn(Dark, SUPER_EFFECTIVE);

		Ghost.setEffectivenessOn(Normal, NO_EFFECT);
		Ghost.setEffectivenessOn(Dark, NOT_VERY_EFFECTIVE);
		Ghost.setEffectivenessOn(Ghost, SUPER_EFFECTIVE);
		Ghost.setEffectivenessOn(Psychic, SUPER_EFFECTIVE);

		Steel.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Steel.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Steel.setEffectivenessOn(Water, NOT_VERY_EFFECTIVE);
		Steel.setEffectivenessOn(Electric, NOT_VERY_EFFECTIVE);
		Steel.setEffectivenessOn(Rock, SUPER_EFFECTIVE);
		Steel.setEffectivenessOn(Ice, SUPER_EFFECTIVE);
		Steel.setEffectivenessOn(Fairy, SUPER_EFFECTIVE);

		Fire.setEffectivenessOn(Rock, NOT_VERY_EFFECTIVE);
		Fire.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Fire.setEffectivenessOn(Water, NOT_VERY_EFFECTIVE);
		Fire.setEffectivenessOn(Dragon, NOT_VERY_EFFECTIVE);
		Fire.setEffectivenessOn(Bug, SUPER_EFFECTIVE);
		Fire.setEffectivenessOn(Steel, SUPER_EFFECTIVE);
		Fire.setEffectivenessOn(Grass, SUPER_EFFECTIVE);
		Fire.setEffectivenessOn(Ice, SUPER_EFFECTIVE);

		Water.setEffectivenessOn(Water, NOT_VERY_EFFECTIVE);
		Water.setEffectivenessOn(Grass, NOT_VERY_EFFECTIVE);
		Water.setEffectivenessOn(Dragon, NOT_VERY_EFFECTIVE);
		Water.setEffectivenessOn(Ground, SUPER_EFFECTIVE);
		Water.setEffectivenessOn(Rock, SUPER_EFFECTIVE);
		Water.setEffectivenessOn(Fire, SUPER_EFFECTIVE);

		Grass.setEffectivenessOn(Flying, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Poison, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Bug, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Grass, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Dragon, NOT_VERY_EFFECTIVE);
		Grass.setEffectivenessOn(Ground, SUPER_EFFECTIVE);
		Grass.setEffectivenessOn(Rock, SUPER_EFFECTIVE);
		Grass.setEffectivenessOn(Water, SUPER_EFFECTIVE);

		Electric.setEffectivenessOn(Ground, NO_EFFECT);
		Electric.setEffectivenessOn(Grass, NOT_VERY_EFFECTIVE);
		Electric.setEffectivenessOn(Electric, NOT_VERY_EFFECTIVE);
		Electric.setEffectivenessOn(Dragon, NOT_VERY_EFFECTIVE);
		Electric.setEffectivenessOn(Flying, SUPER_EFFECTIVE);
		Electric.setEffectivenessOn(Water, SUPER_EFFECTIVE);

		Psychic.setEffectivenessOn(Dark, NO_EFFECT);
		Psychic.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Psychic.setEffectivenessOn(Psychic, NOT_VERY_EFFECTIVE);
		Psychic.setEffectivenessOn(Fighting, SUPER_EFFECTIVE);
		Psychic.setEffectivenessOn(Poison, SUPER_EFFECTIVE);

		Ice.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Ice.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Ice.setEffectivenessOn(Water, NOT_VERY_EFFECTIVE);
		Ice.setEffectivenessOn(Ice, NOT_VERY_EFFECTIVE);
		Ice.setEffectivenessOn(Flying, SUPER_EFFECTIVE);
		Ice.setEffectivenessOn(Ground, SUPER_EFFECTIVE);
		Ice.setEffectivenessOn(Grass, SUPER_EFFECTIVE);
		Ice.setEffectivenessOn(Dragon, SUPER_EFFECTIVE);

		Dragon.setEffectivenessOn(Fairy, NO_EFFECT);
		Dragon.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Dragon.setEffectivenessOn(Dragon, NOT_VERY_EFFECTIVE);

		Dark.setEffectivenessOn(Fighting, NOT_VERY_EFFECTIVE);
		Dark.setEffectivenessOn(Dark, NOT_VERY_EFFECTIVE);
		Dark.setEffectivenessOn(Fairy, NOT_VERY_EFFECTIVE);
		Dark.setEffectivenessOn(Ghost, SUPER_EFFECTIVE);
		Dark.setEffectivenessOn(Psychic, SUPER_EFFECTIVE);

		Fairy.setEffectivenessOn(Poison, NOT_VERY_EFFECTIVE);
		Fairy.setEffectivenessOn(Steel, NOT_VERY_EFFECTIVE);
		Fairy.setEffectivenessOn(Fire, NOT_VERY_EFFECTIVE);
		Fairy.setEffectivenessOn(Fighting, SUPER_EFFECTIVE);
		Fairy.setEffectivenessOn(Dragon, SUPER_EFFECTIVE);
		Fairy.setEffectivenessOn(Dark, SUPER_EFFECTIVE);
	}

	/** @return The Pokemon type with the input ID. null if ID doesn't match a type. */
	public static PokemonType find(int typeID) {
		for (PokemonType type : values())
			if (type.id == typeID) return type;
		return null;
	}

	public final Color color;
	private final double[] effectiveness;
	public final int id;

	private PokemonType(int id, Color color) {
		this.id = id;
		this.color = color;
		this.effectiveness = new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, };
	}

	public double effectivenessOn(PokemonSpecies species) {
		if (species.type2 == null) return this.effectivenessOn(species.type1);
		return this.effectivenessOn(species.type1, species.type2);
	}

	/** @return The effectiveness of this type on a single-type. */
	public double effectivenessOn(PokemonType type) {
		return this.effectiveness[type.id];
	}

	/** @return The effectiveness of this type on a dual-type. */
	public double effectivenessOn(PokemonType type1, PokemonType type2) {
		return this.effectiveness[type1.id] * this.effectiveness[type2.id];
	}

	public Message getName() {
		return new Message("type." + this.id).addPrefix(this.symbol() + " ");
	}

	/** Sets the effectiveness of this type on the input type. */
	private void setEffectivenessOn(PokemonType type, double effectiveness) {
		this.effectiveness[type.id] = effectiveness;
	}

	public String symbol() {
		return "<type-" + this.id + ">";
	}

}
