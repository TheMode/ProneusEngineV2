package fr.proneus.engine.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Vector implements Cloneable {

	protected double x;
	protected double y;

	public Vector() {
		this.x = 0;
		this.y = 0;
	}

	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

	/**
	 * Gets the X component.
	 *
	 * @return The X component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the Y component.
	 *
	 * @return The Y component.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the X component.
	 *
	 * @param x
	 *            The new X component.
	 * @return This vector.
	 */
	public Vector setX(int x) {
		this.x = x;
		return this;
	}

	/**
	 * Set the X component.
	 *
	 * @param x
	 *            The new X component.
	 * @return This vector.
	 */
	public Vector setX(double x) {
		this.x = x;
		return this;
	}

	/**
	 * Set the X component.
	 *
	 * @param x
	 *            The new X component.
	 * @return This vector.
	 */
	public Vector setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Set the Y component.
	 *
	 * @param y
	 *            The new Y component.
	 * @return This vector.
	 */
	public Vector setY(int y) {
		this.y = y;
		return this;
	}

	/**
	 * Set the Y component.
	 *
	 * @param y
	 *            The new Y component.
	 * @return This vector.
	 */
	public Vector setY(double y) {
		this.y = y;
		return this;
	}

	/**
	 * Set the Y component.
	 *
	 * @param y
	 *            The new Y component.
	 * @return This vector.
	 */
	public Vector setY(float y) {
		this.y = y;
		return this;
	}

	/**
	 * Returns a hash code for this vector
	 *
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		int hash = 7;

		hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 79 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		return hash;
	}

	/**
	 * Get a new vector.
	 *
	 * @return vector
	 */
	@Override
	public Vector clone() {
		try {
			return (Vector) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/**
	 * Returns this vector's components as x,y,z.
	 */
	@Override
	public String toString() {
		return x + "," + y;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("x", getX());
		result.put("y", getY());

		return result;
	}

	public static Vector deserialize(Map<String, Object> args) {
		double x = 0;
		double y = 0;

		if (args.containsKey("x")) {
			x = (Double) args.get("x");
		}
		if (args.containsKey("y")) {
			y = (Double) args.get("y");
		}

		return new Vector(x, y);
	}
}