package fr.proneus.engine.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Vector implements Cloneable {

	private static Random random = new Random();

	/**
	 * Threshold for fuzzy equals().
	 */
	private static final double epsilon = 0.000001;

	protected double x;
	protected double y;

	/**
	 * Construct the vector with all components as 0.
	 */
	public Vector() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Construct the vector with provided integer components.
	 *
	 * @param x
	 *            X component
	 * @param y
	 *            Y component
	 * @param z
	 *            Z component
	 */
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Construct the vector with provided double components.
	 *
	 * @param x
	 *            X component
	 * @param y
	 *            Y component
	 * @param z
	 *            Z component
	 */
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Construct the vector with provided float components.
	 *
	 * @param x
	 *            X component
	 * @param y
	 *            Y component
	 * @param z
	 *            Z component
	 */
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds a vector to this one
	 *
	 * @param vec
	 *            The other vector
	 * @return the same vector
	 */
	public Vector add(Vector vec) {
		x += vec.x;
		y += vec.y;
		return this;
	}

	/**
	 * Subtracts a vector from this one.
	 *
	 * @param vec
	 *            The other vector
	 * @return the same vector
	 */
	public Vector subtract(Vector vec) {
		x -= vec.x;
		y -= vec.y;
		return this;
	}

	/**
	 * Multiplies the vector by another.
	 *
	 * @param vec
	 *            The other vector
	 * @return the same vector
	 */
	public Vector multiply(Vector vec) {
		x *= vec.x;
		y *= vec.y;
		return this;
	}

	/**
	 * Divides the vector by another.
	 *
	 * @param vec
	 *            The other vector
	 * @return the same vector
	 */
	public Vector divide(Vector vec) {
		x /= vec.x;
		y /= vec.y;
		return this;
	}

	/**
	 * Copies another vector
	 *
	 * @param vec
	 *            The other vector
	 * @return the same vector
	 */
	public Vector copy(Vector vec) {
		x = vec.x;
		y = vec.y;
		return this;
	}

	/**
	 * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2). The value
	 * of this method is not cached and uses a costly square-root function, so
	 * do not repeatedly call this method to get the vector's magnitude. NaN
	 * will be returned if the inner result of the sqrt() function overflows,
	 * which will be caused if the length is too long.
	 *
	 * @return the magnitude
	 */
	public double length() {
		return Math.sqrt(MathUtils.square(x) + MathUtils.square(y));
	}

	/**
	 * Gets the magnitude of the vector squared.
	 *
	 * @return the magnitude
	 */
	public double lengthSquared() {
		return MathUtils.square(x) + MathUtils.square(y);
	}

	/**
	 * Get the distance between this vector and another. The value of this
	 * method is not cached and uses a costly square-root function, so do not
	 * repeatedly call this method to get the vector's magnitude. NaN will be
	 * returned if the inner result of the sqrt() function overflows, which will
	 * be caused if the distance is too long.
	 *
	 * @param o
	 *            The other vector
	 * @return the distance
	 */
	public double distance(Vector o) {
		return Math.sqrt(MathUtils.square(x - o.x) + MathUtils.square(y - o.y));
	}

	/**
	 * Get the squared distance between this vector and another.
	 *
	 * @param o
	 *            The other vector
	 * @return the distance
	 */
	public double distanceSquared(Vector o) {
		return MathUtils.square(x - o.x) + MathUtils.square(y - o.y);
	}

	/**
	 * Gets the angle between this vector and another in radians.
	 *
	 * @param other
	 *            The other vector
	 * @return angle in radians
	 */
	public float angle(Vector other) {
		double dot = dot(other) / (length() * other.length());

		return (float) Math.acos(dot);
	}

	/**
	 * Sets this vector to the midpoint between this vector and another.
	 *
	 * @param other
	 *            The other vector
	 * @return this same vector (now a midpoint)
	 */
	public Vector midpoint(Vector other) {
		x = (x + other.x) / 2;
		y = (y + other.y) / 2;
		return this;
	}

	/**
	 * Gets a new midpoint vector between this vector and another.
	 *
	 * @param other
	 *            The other vector
	 * @return a new midpoint vector
	 */
	public Vector getMidpoint(Vector other) {
		double x = (this.x + other.x) / 2;
		double y = (this.y + other.y) / 2;
		return new Vector(x, y);
	}

	/**
	 * Performs scalar multiplication, multiplying all components with a scalar.
	 *
	 * @param m
	 *            The factor
	 * @return the same vector
	 */
	public Vector multiply(int m) {
		x *= m;
		y *= m;
		return this;
	}

	/**
	 * Performs scalar multiplication, multiplying all components with a scalar.
	 *
	 * @param m
	 *            The factor
	 * @return the same vector
	 */
	public Vector multiply(double m) {
		x *= m;
		y *= m;
		return this;
	}

	/**
	 * Performs scalar multiplication, multiplying all components with a scalar.
	 *
	 * @param m
	 *            The factor
	 * @return the same vector
	 */
	public Vector multiply(float m) {
		x *= m;
		y *= m;
		return this;
	}

	/**
	 * Calculates the dot product of this vector with another. The dot product
	 * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
	 *
	 * @param other
	 *            The other vector
	 * @return dot product
	 */
	public double dot(Vector other) {
		return x * other.x + y * other.y;
	}

	/**
	 * Converts this vector to a unit vector (a vector with length of 1).
	 *
	 * @return the same vector
	 */
	public Vector normalize() {
		double length = length();

		x /= length;
		y /= length;

		return this;
	}

	/**
	 * Zero this vector's components.
	 *
	 * @return the same vector
	 */
	public Vector zero() {
		x = 0;
		y = 0;
		return this;
	}

	/**
	 * Returns whether this vector is in an axis-aligned bounding box.
	 * <p>
	 * The minimum and maximum vectors given must be truly the minimum and
	 * maximum X, Y and Z components.
	 *
	 * @param min
	 *            Minimum vector
	 * @param max
	 *            Maximum vector
	 * @return whether this vector is in the AABB
	 */
	public boolean isInAABB(Vector min, Vector max) {
		return x >= min.x && x <= max.x && y >= min.y && y <= max.y;
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
	 * Checks to see if two objects are equal.
	 * <p>
	 * Only two Vectors can ever return true. This method uses a fuzzy match to
	 * account for floating point errors. The epsilon can be retrieved with
	 * epsilon.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vector)) {
			return false;
		}

		Vector other = (Vector) obj;

		return Math.abs(x - other.x) < epsilon && Math.abs(y - other.y) < epsilon
				&& (this.getClass().equals(obj.getClass()));
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

	/**
	 * Get the threshold used for equals().
	 *
	 * @return The epsilon.
	 */
	public static double getEpsilon() {
		return epsilon;
	}

	/**
	 * Gets the minimum components of two vectors.
	 *
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return minimum
	 */
	public static Vector getMinimum(Vector v1, Vector v2) {
		return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
	}

	/**
	 * Gets the maximum components of two vectors.
	 *
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return maximum
	 */
	public static Vector getMaximum(Vector v1, Vector v2) {
		return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
	}

	/**
	 * Gets a random vector with components having a random value between 0 and
	 * 1.
	 *
	 * @return A random vector.
	 */
	public static Vector getRandom() {
		return new Vector(random.nextDouble(), random.nextDouble());
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