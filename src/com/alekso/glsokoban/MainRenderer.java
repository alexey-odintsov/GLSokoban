package com.alekso.glsokoban;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.alekso.math.Vector3f;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class MainRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MainRenderer";

	private final Context activityContext;
	
	private float pressX;
	private float pressY;
	private float accX;
	private float accY;

	private int programHandle;
	private int textProgramHandle;
	private int programHandleAnim;
	private AssetsManager assetsManager;

	GLText gltext;

	// meshes and textures handlers
	int heroMesh;
	int boxMesh;
	int skyboxMesh;
	int planeMesh;
	int wallTexture;
	int floorTexture;
	int targetTexture;
	int levelTexture;
	int levelTexture2;
	int spaceTexture;
	int boxTexture;
	int fontTexture;
	int heroTexture;

	int moves = 0;
	int pushes = 0;
	int time = 0;

	// delta time and fps related fields
	private long startTime = System.currentTimeMillis();
	private long endTime;
	private long dt;

	private float[] modelMatrix = new float[16];
	private float[] mvMatrix = new float[16];
	private float[] mvpMatrix = new float[16];

	float green[] = { 0.0f, 1.0f, 0.0f, 1.0f };
	float white[] = { 1.0f, 1.0f, 1.0f, 1.0f };

	public Camera camGameLoop;
	public Camera camPause;
	public Camera camMainMenu;
	float angle, angle2 = 0.0f;
	float ticks;

	Vector3f camMMVelocity = new Vector3f();

	ArrayList<Level> levels;
	int curLevel = 1;
	int mainMenuIndex = 1;

	public char[][][] curLevelData; // store current level data

	public int heroX, heroZ;

	enum State {
		MENU, GAME_LOOP, PAUSE, LEVEL_COMPLETE
	}

	public DbAdapter mDbHelper;
	State state;

	public MainRenderer(final Context activityContext) {
		Log.d(TAG, "Constructor");
		this.activityContext = activityContext;
		
		state = State.MENU;

		levels = new ArrayList<Level>();

		mDbHelper = new DbAdapter(this.activityContext);
		mDbHelper.open();

		levels = mDbHelper.getLevels();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		camGameLoop.update(width, height);
		camPause.update(width, height);
		camMainMenu.update(width, height);

	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		camGameLoop = new Camera();
		camGameLoop.pos.y = 7.0f;
		camGameLoop.up.set(0.0f, 0.0f, -1.0f);

		camPause = new Camera();
		camPause.pos.y = 7.0f;
		camPause.up.set(0.0f, 1.0f, 0.0f);

		camMainMenu = new Camera();
		camMainMenu.pos.y = 1.5f;
		camMainMenu.dir.z = 0.0f;
		camMainMenu.up.set(0.0f, 0.0f, -1.0f);

		// init data
		programHandle = ShaderHelper.createShader(activityContext,
				R.raw.vertex_shader_tex_and_light,
				R.raw.fragment_shader_tex_and_light, new String[] {
						"a_Position", "a_Normal", "a_TexCoordinate" });

		programHandleAnim = ShaderHelper.createShader(activityContext,
				R.raw.vs_dynamic, R.raw.fs_dynamic, new String[] {
						"a_Position", "a_Normal", "a_TexCoordinate" });

		textProgramHandle = ShaderHelper.createShader(activityContext,
				R.raw.text_vertex_shader, R.raw.text_fragment_shader,
				new String[] { "a_Position", "a_Color", "a_TexCoordinate" });

		// load resources
		assetsManager = new AssetsManager(this.activityContext);

		wallTexture = assetsManager.loadTexture(R.drawable.wall_l);
		floorTexture = assetsManager.loadTexture(R.drawable.floor_l);
		targetTexture = assetsManager.loadTexture(R.drawable.target);
		levelTexture = assetsManager.loadTexture(R.drawable.target);
		levelTexture2 = assetsManager.loadTexture(R.drawable.target2);
		spaceTexture = assetsManager.loadTexture(R.drawable.space);
		boxTexture = assetsManager.loadTexture(R.drawable.crate_l);
		fontTexture = assetsManager.loadTexture(R.drawable.consolas);
		heroTexture = assetsManager.loadTexture(R.drawable.orange);

		heroMesh = assetsManager.loadMesh("sphere.obj", 0.5f);
		boxMesh = assetsManager.loadMesh("box.obj", 0.5f);
		skyboxMesh = assetsManager.loadMesh("skybox.obj", 30.0f);
		planeMesh = assetsManager.loadMesh("plane.obj", 0.5f);

		gltext = new GLText(fontTexture, 1024.0f);
	}

	/**
	 * 
	 * @param dt
	 */
	private void update(float dt) {
		angle2 += 0.01f;
		if (angle2 >= 3.140000f) {
			angle2 = 0.0f;
		}

		switch (state) {
		case MENU:
			float dx = camMMVelocity.x * dt / 10;

			camMainMenu.pos.x += dx;
			camMainMenu.dir.x += dx;
			camMainMenu.update();
			camMMVelocity.x -= dx;
			break;
		case GAME_LOOP:
			break;
		case PAUSE:
			angle += 10.0f / dt;
			// camPause.pos.x = (float) ((camPause.pos.x * Math.cos(angle)) -
			// (camPause.pos.y * Math.sin(angle)));
			// camPause.pos.y = (float) ((camPause.pos.y * Math.cos(angle)) +
			// (camPause.pos.x * Math.sin(angle)));

			// Log.d("ALEKSO", "angle: " + angle);
			camPause.dir.x = 5;
			camPause.dir.z = 5;
			// camPause.pos.x = (float) (((camPause.pos.x - heroX) *
			// Math.cos(angle)) - ((heroZ - camPause.pos.z) * Math.sin(angle)) +
			// heroX) ;
			// camPause.pos.z = (float) (((heroZ - camPause.pos.z) *
			// Math.cos(angle)) - ((camPause.pos.x - heroX) * Math.sin(angle)) +
			// heroZ);
			camPause.update();

			// Matrix.rotateM(camPause.viewMatrix, 0, 3.0f/dt, 0.0f, 1.0f,
			// 0.0f);

			break;
		default:
			break;
		}
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		endTime = System.currentTimeMillis();
		dt = endTime - startTime;

		update(dt);

		// smooth fps to 30
		if (dt < 33) {
			try {
				Thread.sleep(33 - dt);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ticks = dt;

		startTime = System.currentTimeMillis();

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		switch (state) {
		case MENU:

			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			for (int i = 0; i < levels.size(); i++) {
				if (levels.get(i).isCompleted == 1) {
					renderMesh(programHandle, planeMesh, levelTexture,
							i * 1.2f, 0.0f, 0.0f, camMainMenu, 0);
				} else {
					renderMesh(programHandle, planeMesh, levelTexture2,
							i * 1.2f, 0.0f, 0.0f, camMainMenu, 0);
				}
			}

			renderMesh(programHandle, skyboxMesh, spaceTexture, 0.0f, 0.0f,
					0.0f, camMainMenu, 0);

			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			print(0.7f, 0.12f, 0.0f, "Level: "
					+ levels.get(mainMenuIndex - 1).id);
			print(0.4f, 0.8f, 0.0f, "Tap to start", green, 2.0f);

			break;

		case PAUSE:
			renderGame(camPause);
			print(0.6f, 0.4f, 0.0f, "PAUSE", green, 3.0f);

			break;

		case LEVEL_COMPLETE:
			renderGame(camGameLoop);
			print(0.3f, 0.4f, 0.0f, "LEVEL COMPLETE!", white, 2.0f);

			break;

		case GAME_LOOP:
			time += dt;

			renderGame(camGameLoop);

			break;

		default:
			break;
		}

	}

	void renderGame(Camera cam) {
		// 3D stuff
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		for (int y = 0; y < curLevelData[1].length; y++) {
			for (int x = 0; x < curLevelData[1][y].length; x++) {
				switch (curLevelData[1][y][x]) {
				case '#':
					renderMesh(programHandle, boxMesh, wallTexture, x, 0, y,
							cam, 0);
					break;
				case '@':
					renderMesh(programHandle, boxMesh, boxTexture, x, 0, y,
							cam, 0);
					break;
				default:
					break;
				}
				if (curLevelData[0][y][x] == '+') {
					renderMesh(programHandleAnim, planeMesh, targetTexture, x,
							-0.5f, y, cam, 1);
				} else {
					renderMesh(programHandle, planeMesh, floorTexture, x,
							-0.5f, y, cam, 0);
				}
			}
		}

		// render entities
		renderMesh(programHandle, heroMesh, heroTexture, heroX, -0.5f, heroZ,
				cam, 0);
		renderMesh(programHandle, skyboxMesh, spaceTexture, 0.0f, 0.0f, 0.0f,
				cam, 0);

		// 2D stuff
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);

		print(0.01f, 0.9f, 0.0f, "Moves: " + moves, green);
		print(0.01f, 0.85f, 0.0f, "Pushes: " + pushes, green);
		print(1.00f, 0.9f, 0.0f, "Time: " + time / 1000);

		// print(1.0f, 0.85f, 0.0f, "dt:  " + dt);
		// print(1.0f, 0.8f, 0.0f, "fps: " + (1000 / dt));
	}

	/**
	 * 
	 * @param meshId
	 * @param textureId
	 * @param x
	 * @param y
	 * @param z
	 */
	public void renderMesh(int program, int meshId, int textureId, float x,
			float y, float z, Camera cam, int type) {

		Mesh mesh = assetsManager.meshes.get(meshId);

		GLES20.glUseProgram(program);

		int mMVPMatrixHandle = GLES20.glGetUniformLocation(program,
				"u_MVPMatrix");
		int mMVMatrixHandle = GLES20
				.glGetUniformLocation(program, "u_MVMatrix");
		int mLightPosHandle = GLES20
				.glGetUniformLocation(program, "u_LightPos");
		int mDtHandle = GLES20.glGetUniformLocation(program, "u_dt");
		int mTextureUniformHandle = GLES20.glGetUniformLocation(program,
				"u_Texture");
		int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
		int mNormalHandle = GLES20.glGetAttribLocation(program, "a_Normal");
		int mTextureCoordinateHandle = GLES20.glGetAttribLocation(program,
				"a_TexCoordinate");

		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, z);
		if (meshId == heroMesh) {
			Matrix.translateM(modelMatrix, 0, 0.0f, 0.5f, 0.0f);
			Matrix.rotateM(modelMatrix, 0, -90, 0.0f, 1.0f, 0.0f);
		}

		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
				assetsManager.textures.get(textureId));

		// Tell the texture uniform sampler to use this texture in the shader by
		// binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		mesh.mPositions.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, mesh.mPositions);
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the normal information
		mesh.mNormals.position(0);
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false,
				0, mesh.mNormals);
		GLES20.glEnableVertexAttribArray(mNormalHandle);

		// Pass in the texture coordinate information
		mesh.mTextureCoordinates.position(0);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2,
				GLES20.GL_FLOAT, false, 0, mesh.mTextureCoordinates);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

		// MV = V * M
		Matrix.multiplyMM(mvMatrix, 0, cam.viewMatrix, 0, modelMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvMatrix, 0);

		// MVP = P * MV
		Matrix.multiplyMM(mvpMatrix, 0, cam.projMatrix, 0, mvMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		// Pass in the light position in eye space.
		GLES20.glUniform3f(mLightPosHandle, -1.5f, 0.5f, 3.0f);

		if (type == 1) {
			GLES20.glUniform3f(mDtHandle, angle2, 0.0f, 0.0f);
		}

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mesh.vertexCount);
	}

	/**
	 * Print text
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param text
	 *            Text to print
	 */
	public void print(float x, float y, float z, String text) {
		print(x, y, z, text, white, 1.0f);
	}

	public void print(float x, float y, float z, String text, float color[]) {
		print(x, y, z, text, color, 1.0f);
	}

	/**
	 * Print text with specified colors components
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param text
	 *            Text to print
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void print(float x, float y, float z, String text, float color[],
			float scale) {
		GLES20.glUseProgram(textProgramHandle);

		int mMVPMatrixHandle = GLES20.glGetUniformLocation(textProgramHandle,
				"u_mvpMatrix");
		int mTextureUniformHandle = GLES20.glGetUniformLocation(
				textProgramHandle, "s_texture");
		int mPositionHandle = GLES20.glGetAttribLocation(textProgramHandle,
				"a_position");
		int mTextureCoordinateHandle = GLES20.glGetAttribLocation(
				textProgramHandle, "a_texCoord");
		int mColorHandle = GLES20.glGetUniformLocation(textProgramHandle,
				"a_Color");

		Matrix.setIdentityM(modelMatrix, 0);
		Matrix.translateM(modelMatrix, 0, x, y, z);
		Matrix.scaleM(modelMatrix, 0, scale, scale, scale);

		// Set the active texture unit to texture unit 0.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
				assetsManager.textures.get(fontTexture));

		// Tell the texture uniform sampler to use this texture in the shader by
		// binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		gltext.mPositions.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, 0, gltext.mPositions);
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Pass in the texture coordinate information
		gltext.mTextureCoordinates.position(0);
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2,
				GLES20.GL_FLOAT, false, 0, gltext.mTextureCoordinates);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

		GLES20.glUniform4f(mColorHandle, color[0], color[1], color[2], color[3]);

		Matrix.multiplyMM(mvMatrix, 0, camGameLoop.orthoMatrix, 0, modelMatrix,
				0);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		float span = 0.0f;
		for (int i = 0; i < text.length(); i++) {
			int chr = (int) text.charAt(i);
			Matrix.translateM(mvMatrix, 0, span, 0.0f, 0.0f);

			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvMatrix, 0);

			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,
					gltext.textMapping[chr] * 4, 4);
			span = gltext.textAttributes2[gltext.textMapping[chr]][2] + 0.01f;
		}

		GLES20.glDisable(GLES20.GL_BLEND);
	}

	/**
	 * 
	 * @param dx
	 * @param dz
	 */
	public void moveHero(int dx, int dz) {
		if (dx != 0) {
			// heroX += dx > 0 ? 1 : -1;
			if (dx > 0) {
				if (curLevelData[1][heroZ][heroX + 1] == '@') {
					if (curLevelData[1][heroZ][heroX + 2] == ' '
							|| curLevelData[1][heroZ][heroX + 2] == '+') {
						curLevelData[1][heroZ][heroX + 1] = ' ';
						curLevelData[1][heroZ][heroX + 2] = '@';
						heroX += dx;
						moves++;
						pushes++;
					}
				} else if (curLevelData[1][heroZ][heroX + 1] != '#') {
					heroX += dx;
					moves++;
				}
			} else {
				if (curLevelData[1][heroZ][heroX - 1] == '@') {
					if (curLevelData[1][heroZ][heroX - 2] == ' '
							|| curLevelData[1][heroZ][heroX - 2] == '+') {
						curLevelData[1][heroZ][heroX - 1] = ' ';
						curLevelData[1][heroZ][heroX - 2] = '@';
						heroX += dx;
						moves++;
						pushes++;
					}
				} else if (curLevelData[1][heroZ][heroX - 1] != '#') {
					heroX += dx;
					moves++;
				}
			}

		} else if (dz != 0) {

			// heroZ += dz > 0 ? 1 : -1;
			if (dz > 0) {
				if (curLevelData[1][heroZ + 1][heroX] == '@') {
					if (curLevelData[1][heroZ + 2][heroX] == ' '
							|| curLevelData[1][heroZ + 2][heroX] == '+') {
						curLevelData[1][heroZ + 1][heroX] = ' ';
						curLevelData[1][heroZ + 2][heroX] = '@';
						heroZ += dz;
						moves++;
						pushes++;
					}
				} else if (curLevelData[1][heroZ + 1][heroX] != '#') {
					heroZ += dz;
					moves++;
				}
			} else {
				if (curLevelData[1][heroZ - 1][heroX] == '@') {
					if (curLevelData[1][heroZ - 2][heroX] == ' '
							|| curLevelData[1][heroZ - 2][heroX] == '+') {
						curLevelData[1][heroZ - 1][heroX] = ' ';
						curLevelData[1][heroZ - 2][heroX] = '@';
						heroZ += dz;
						pushes++;
						moves++;
					}
				} else if (curLevelData[1][heroZ - 1][heroX] != '#') {
					heroZ += dz;
					moves++;
				}
			}
		}

		// check whether all boxes are in the right places
		int left = 0;

		for (int y = 0; y < curLevelData[1].length; y++) {
			for (int x = 0; x < curLevelData[1][y].length; x++) {
				if (curLevelData[0][y][x] == '+'
						&& curLevelData[1][y][x] != '@') {
					left++;
				}
			}
		}

		if (left < 1) {
			// goto next level
			Log.d("ALEKSO", "LEVEL DONE!!");
			levelDone();
		}

		camGameLoop.dir.x = heroX;
		camGameLoop.pos.x = heroX;
		camGameLoop.dir.z = heroZ;
		camGameLoop.pos.z = heroZ + 1.0f;

		camGameLoop.update();

		// camPause.dir.x = heroX;
		// camPause.dir.z = heroZ;
		// camPause.update();
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pressX = x;
			pressY = y;
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			accX = x - pressX;
			accY = y - pressY;
		}

		switch (state) {
		case MENU:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (Math.abs(accX) > 40 && Math.abs(accX) > Math.abs(accY)) {
					// do not respond when menu is moving
					if (Math.abs(camMMVelocity.x) <= 0.01f) {
						if (accX < 0.0001f && mainMenuIndex < levels.size()) {
							camMMVelocity.x = 1.2f;
							mainMenuIndex++;
							Log.d("ALEKSO", "Move cam: " + camMMVelocity);
						} else if (accX > 0.00011f && mainMenuIndex > 1) {
							camMMVelocity.x = -1.2f;
							mainMenuIndex--;
							Log.d("ALEKSO", "Move cam: " + camMMVelocity);
						}
					}
				} else {
					curLevel = mainMenuIndex - 1;
					state = State.GAME_LOOP;

					curLevelData = levels.get(curLevel).getLevelCopy();
					moves = 0;
					pushes = 0;
					time = 0;

					for (int i = 0; i < curLevelData[0].length; i++) {
						for (int j = 0; j < curLevelData[0][i].length; j++) {
							if (curLevelData[0][i][j] == '*') {
								// levels.get(curLevel).data[0][i][j] = ' ';
								heroX = j;
								heroZ = i;
								Log.d("ALEKSO", "move hero to {" + j + "; " + i
										+ "}");
								moveHero(0, 0);
							}
						}
					}
				}

				accX = 0;
				accY = 0;
			}

			break;
		case GAME_LOOP:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (Math.abs(accX) > 20 || Math.abs(accY) > 20) {
					if (Math.abs(accX) > Math.abs(accY)) {
						moveHero(accX > 0 ? 1 : -1, 0);
					} else {
						moveHero(0, accY > 0 ? 1 : -1);
					}
				}

				accX = 0;
				accY = 0;
			}

			break;
		case PAUSE:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				state = State.GAME_LOOP;
			}
			break;
		case LEVEL_COMPLETE:
			if (event.getAction() == MotionEvent.ACTION_UP) {
				state = State.MENU;
			}
			break;
		default:
			break;
		}

		return true;

	}

	/**
	 * 
	 */
	void levelDone() {
		// Save best scores
		Level level = levels.get(curLevel);
		level.isCompleted = 1;

		if (time < level.bestTime) {
			level.bestTime = time;
		}

		level.score = 3; // TODO: calculate score by moves, pushes and time
							// values

		mDbHelper.updateLevel(level);

		state = State.LEVEL_COMPLETE;
	}

	/**
	 * 
	 * @return
	 */
	public boolean onBackPressed() {
		switch (state) {
		case GAME_LOOP:
			state = State.PAUSE;
			return false;
		case PAUSE:
		case LEVEL_COMPLETE:
			state = State.MENU;
			return false;
		case MENU:
			mDbHelper.close();
			return true;
		default:
			return false;
		}
	}

	public void onResume() {
		
	}

	public void onPause() {
		
	}


}
