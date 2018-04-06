package com.darkxell.client.state.quiz;

public enum Nature
{

	Brave(0),
	Calm(1),
	Docile(2),
	Hardy(3),
	Hasty(4),
	Impish(5),
	Jolly(6),
	Lonely(7),
	Naive(8),
	Quirky(9),
	Relaxed(10),
	Sassy(11),
	Timid(12);

	public static Nature get(int id)
	{
		for (Nature n : values())
			if (n.id == id) return n;
		return Nature.Brave;
	}

	public final int id;

	private Nature(int id)
	{
		this.id = id;
	}

}
