package com.alekso.math;

public class Vector3f {
	public float x;
	public float y;
	public float z;

	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public void set(Vector3f v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void add(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void sub(Vector3f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public void sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void scale(float f) {
		x *= f;
		y *= f;
		z *= f;
	}

	public Vector3f vadd(Vector3f v) {
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}

	public Vector3f vadd(float x, float y, float z) {
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}

	public Vector3f vsub(Vector3f v) {
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}

	public Vector3f vsub(float x, float y, float z) {
		return new Vector3f(this.x - x, this.y - y, this.z - z);
	}

	public Vector3f vscale(float f) {
		return new Vector3f(x * f, y * f, z * f);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public void normalize() {
		float len = length();
		x /= len;
		y /= len;
		z /= len;
	}

	public Vector3f vnormalize() {
		float len = length();
		return new Vector3f(x / len, y / len, z / len);
	}

	public String toString() {
		return "{ " + x + "; " + y + "; " + z + " }";
	}
}
