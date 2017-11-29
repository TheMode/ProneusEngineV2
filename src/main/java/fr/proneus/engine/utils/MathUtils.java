package fr.proneus.engine.utils;

import java.util.Random;

public class MathUtils {
	
	public static double getAngle(double x, double y, double x2, double y2) {
		return Math.toDegrees(Math.atan2(y2 - y, x2 - x)) + 180;
	}

	public static double getDistance(double x, double y, double x2, double y2) {
		return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}

	public static int randomInt(Random random, int min, int max) {
		return random.nextInt(max + 1 - min) + min;
	}

	public static int floor(double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int ceil(final double num) {
        final int floor = (int) num;
        return floor == num ? floor : floor + (int) (~Double.doubleToRawLongBits(num) >>> 63);
    }

    public static int round(double num) {
        return floor(num + 0.5d);
    }

    public static double square(double num) {
        return num * num;
    }
	
	public static AngleValue getMoveValue(double angle, float x, float y, float xSpeed, float ySpeed) {
		x = (float) Math.cos(Math.toRadians(angle)) * xSpeed;
		y = (float) Math.sin(Math.toRadians(angle)) * ySpeed;
		return new AngleValue(x, y);
	}

	public static class AngleValue {

		public float x, y;

		public AngleValue(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

}
