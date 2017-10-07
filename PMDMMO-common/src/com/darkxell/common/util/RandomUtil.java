package com.darkxell.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/** Contains utility methods for randomness. */
public final class RandomUtil
{

	/** @param objects - A List of Objects to choose from.
	 * @param random - A Random Number Generator.
	 * @return A random Object from the input List. All Objects are equally probable to be chosen. */
	public static <T> T random(ArrayList<T> candidates, Random random)
	{
		if (candidates.isEmpty())
		{
			Logger.e("random(): no objects to randomize!");
			return null;
		}
		return candidates.get(random.nextInt(candidates.size()));
	}

	/** @param objects - A List of Objects to choose from.
	 * @param weights - A List of Weights associated with each Object.
	 * @param random - A Random Number Generator.
	 * @return A random Object from the input List. The greater an Object's Weight is, the more likely it is to be chosen. */
	public static <T> T weightedRandom(ArrayList<T> candidates, ArrayList<Integer> weights, Random random)
	{
		if (candidates.isEmpty())
		{
			Logger.e("weightedRandom(): no objects to randomize!");
			return null;
		}
		if (candidates.size() != weights.size())
		{
			Logger.e("weightedRandom(): objects and weights have different sizes!");
			return candidates.get(0);
		}

		int sum = 0;
		for (Integer weight : weights)
			sum += weight;

		int r = random.nextInt(sum);
		sum = 0;
		for (int index = 0; index < candidates.size(); ++index)
		{
			sum += weights.get(index);
			if (r <= sum) return candidates.get(index);
		}

		return candidates.get(0);
	}

	/** @param weightedObjects - A List of Objects to choose from, with their respective Weights.
	 * @param random - A Random Number Generator.
	 * @return A random Object from the input List. The greater an Object's Weight is, the more likely it is to be chosen. */
	public static <T> T weightedRandom(HashMap<T, Integer> weightedObjects, Random random)
	{
		ArrayList<T> objects = new ArrayList<T>();
		ArrayList<Integer> weights = new ArrayList<Integer>();

		for (T object : weightedObjects.keySet())
		{
			objects.add(object);
			weights.add(weightedObjects.get(object));
		}

		return weightedRandom(objects, weights, random);
	}

	private RandomUtil()
	{}

}
