package com.alekso.glsokoban;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MainRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MainRenderer";

	private final Context activityContext;

	public MainRenderer(final Context activityContext) {
		Log.d(TAG, "Constructor");
		this.activityContext = activityContext;
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// init shaders
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		// update
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// render
	}

	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	public void onResume() {
		
	}

	public void onPause() {
		
	}

	public boolean onBackPressed() {
		return true;
	}

}
