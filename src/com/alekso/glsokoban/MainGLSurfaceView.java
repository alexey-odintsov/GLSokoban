package com.alekso.glsokoban;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainGLSurfaceView extends GLSurfaceView {

	private MainRenderer mRenderer;

	private float mDensity;

	public MainGLSurfaceView(Context context) {
		super(context);
	}

	public MainGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event != null) {

			return mRenderer.onTouchEvent(event);
		} else {
			return super.onTouchEvent(event);
		}
	}

	public void setRenderer(MainRenderer renderer, float density) {
		mRenderer = renderer;
		mDensity = density;
		super.setRenderer(renderer);
	}

}
