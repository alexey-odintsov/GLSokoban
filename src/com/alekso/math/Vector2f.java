package com.alekso.math;

public class Vector2f {
	public float x;
	public float y;

	public Vector2f() {
		x = 0.0f;
		y = 0.0f;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v) {
		x = v.x;
		y = v.y;
	}

	public void set(Vector2f v) {
		x = v.x;
		y = v.y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2f v) {
		x += v.x;
		y += v.y;
	}

	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void sub(Vector2f v) {
		x -= v.x;
		y -= v.y;
	}

	public void sub(float x, float y) {
		this.x -= x;
		this.y -= y;
	}

	public void scale(float f) {
		x *= f;
		y *= f;
	}

	public Vector2f vadd(Vector2f v) {
		return new Vector2f(x + v.x, y + v.y);
	}

	public Vector2f vadd(float x, float y) {
		return new Vector2f(this.x + x, this.y + y);
	}

	public Vector2f vsub(Vector2f v) {
		return new Vector2f(x - v.x, y - v.y);
	}

	public Vector2f vsub(float x, float y, float z) {
		return new Vector2f(this.x - x, this.y - y);
	}

	public Vector2f vscale(float f) {
		return new Vector2f(x * f, y * f);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public void normalize() {
		float len = length();
		x /= len;
		y /= len;
	}

	public Vector2f vnormalize() {
		float len = length();
		return new Vector2f(x / len, y / len);
	}

	public String toString() {
		return "{ " + x + "; " + y + " }";
	}
}
