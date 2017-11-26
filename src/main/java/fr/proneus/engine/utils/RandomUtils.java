package fr.proneus.engine.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

	public static int getRandomInteger(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public static float getRandomFloat(float min, float max) {
		return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
	}

	public static boolean chance(int min, int max, int value) {
		return value >= getRandomInteger(min, max);
	}

	public static boolean chance(float min, float max, float value) {
		return value >= getRandomFloat(min, max);
	}

}
