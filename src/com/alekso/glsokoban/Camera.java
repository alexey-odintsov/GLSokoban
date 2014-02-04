package com.alekso.glsokoban;

import android.opengl.Matrix;

import com.alekso.math.Vector3f;

public class Camera {
	public Vector3f pos;
	public Vector3f dir;
	public Vector3f up;

	public float w, h;

	public float near;
	public float far;
	public float fov;
	public float ratio;

	public float[] viewMatrix = new float[16];
	public float[] projMatrix = new float[16];
	public float[] orthoMatrix = new float[16];

	/**
     * 
     */
	public Camera() {
		near = 0.1f;
		far = 100.0f;
		fov = 60.0f;

		pos = new Vector3f();
		dir = new Vector3f(0.0f, 0.0f, -1.0f);
		up = new Vector3f(0.0f, 1.0f, 0.0f);
	}

	/**
	 * 
	 * @param width
	 * @param height
	 */
	public void update(int width, int height) {
		this.ratio = (float) width / height;

		this.w = width;
		this.h = height;

		// update matrices ratio
		float top = (float) Math.tan(fov * 3.14f / 360.0f) * near;
		float bottom = -top;
		float left = ratio * bottom;
		float right = ratio * top;

		Matrix.frustumM(projMatrix, 0, left, right, bottom, top, near, far);
		Matrix.orthoM(orthoMatrix, 0, 0, ratio, 0, 1, -0.001f, 1000.0f);

		Matrix.setLookAtM(viewMatrix, 0, pos.x, pos.y, pos.z, dir.x, dir.y,
				dir.z, up.x, up.y, up.z);

	}

	public void update() {
		Matrix.setLookAtM(viewMatrix, 0, pos.x, pos.y, pos.z, dir.x, dir.y,
				dir.z, up.x, up.y, up.z);
	}

}
