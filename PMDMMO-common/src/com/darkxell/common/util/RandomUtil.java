package com.darkxell.common.util;

import java.util.ArrayList;
import java.util.Random;

/** Contains utility methods for randomness. */
public final class RandomUtil
{

	/** @param mean - The Mean of the distribution.
	 * @param deviation - The Deviation of the distribution.
	 * @param random - A Random Number Generator.
	 * @return A Random double from the input Gaussian distribution. */
	public static double nextGaussian(double mean, double deviation, Random random)
	{
		return (int) (random.nextGaussian() * deviation + mean);
	}

	/** @param mean - The Mean of the distribution.
	 * @param deviation - The Deviation of the distribution.
	 * @param random - A Random Number Generator.
	 * @return A Random integer from the input Gaussian distribution. */
	public static int nextGaussian(int mean, double deviation, Random random)
	{
		return (int) (random.nextGaussian() * deviation) + mean;
	}

	/** @param min - A minimum value.
	 * @param max - A maximum value.
	 * @param random - A Random Number Generator.
	 * @return A Random integer between a minimum and a maximum value (max excluded). */
	public static int nextIntInBounds(int min, int max, Random random)
	{
		return min + random.nextInt(max - min);
	}

	/** @param objects - A List of Objects to choose from.
	 * @param random - A Random Number Generator.
	 * @return A random Object from the input List. All Objects are equally probable to be chosen. */
	public static <T> T random(ArrayList<T> candidates, Random random)
	{
		if (candidates.isEmpty())
		{
			new Exception("random(): no objects to randomize!").printStackTrace();
			return null;
		}
		return candidates.get(random.nextInt(candidates.size()));
	}

	/** @return A Random number close to the input number, according to a Gaussian distribution. */
	public static int randomize(int number, Random random)
	{
		if (number < 3) return nextGaussian(number - 1, 1.5, random);
		return nextGaussian(number * 3 / 4, number / 3, random);
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

	private RandomUtil()
	{}

}
