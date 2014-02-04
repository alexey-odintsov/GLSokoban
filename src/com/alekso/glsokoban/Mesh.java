package com.alekso.glsokoban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.util.Log;

public class Mesh {

	private static final String TAG = "Mesh";
	
	private Context context;

	private final int mBytesPerFloat = 4;

	public int vertexCount;
	FloatBuffer mPositions;
	FloatBuffer mNormals;
	FloatBuffer mTextureCoordinates;

	/**
	 * 
	 * @param context
	 * @param path
	 */
	public Mesh(Context context, String path, float scale) {
		this.context = context;

		InputStream is = null;
		BufferedReader br = null;

		int vCount = 0; // vertices count
		int vnCount = 0; // normals count
		int vtCount = 0; // texture coordinates count
		int fCount = 0; // faces count

		try {
			is = context.getAssets().open(path);
			br = new BufferedReader(new InputStreamReader(is));
			String l;

			/**
			 * First pass: collect information
			 */
			Log.d(TAG, "Collecting model info..");
			while ((l = br.readLine()) != null) {
				l.trim();
				if (l.startsWith("v "))
					vCount++;
				if (l.startsWith("vn "))
					vnCount++;
				if (l.startsWith("vt "))
					vtCount++;
				if (l.startsWith("f "))
					fCount++;
			}

			Log.d(TAG, "vertices: " + vCount);
			Log.d(TAG, "vertex normals: " + vnCount);
			Log.d(TAG, "texture coords: " + vtCount);
			Log.d(TAG, "faces: " + fCount);

			br.close();
			is.close();

			/**
			 * Second pass: retrieve data
			 */

			// initialize arrays for data
			float vertices[] = new float[vCount * 3];
			float normals[] = new float[vnCount * 3];
			float texcoords[] = new float[vtCount * 2];

			int facesVertices[] = new int[fCount * 3];
			int facesNormals[] = new int[fCount * 3];
			int facesTexCoords[] = new int[fCount * 3];

			int vertexIdx = 0;
			int normalIdx = 0;
			int texcoordIdx = 0;
			int faceIdx = 0;

			// read file again
			is = context.getAssets().open(path);
			br = new BufferedReader(new InputStreamReader(is));
			while ((l = br.readLine()) != null) {
				l.trim();
				if (l.startsWith("v ")) {
					String arr[] = l.substring(2).split(" ");
					vertices[vertexIdx++] = Float.parseFloat(arr[0]);
					vertices[vertexIdx++] = Float.parseFloat(arr[1]);
					vertices[vertexIdx++] = Float.parseFloat(arr[2]);
					continue;
				}

				if (l.startsWith("vn ")) {
					String arr[] = l.substring(3).split(" ");
					normals[normalIdx++] = Float.parseFloat(arr[0]);
					normals[normalIdx++] = Float.parseFloat(arr[1]);
					normals[normalIdx++] = Float.parseFloat(arr[2]);
					continue;
				}

				if (l.startsWith("vt ")) {
					String arr[] = l.substring(3).split(" ");
					texcoords[texcoordIdx++] = Float.parseFloat(arr[0]);
					texcoords[texcoordIdx++] = Float.parseFloat(arr[1]);
					continue;
				}

				if (l.startsWith("f ")) {
					String arr[] = l.substring(2).trim().split(" ");

					String parts[] = arr[0].split("/");
					facesVertices[faceIdx] = Integer.parseInt(parts[0]) - 1;
					if (parts.length > 1) {
						facesTexCoords[faceIdx] = Integer.parseInt(parts[1]) - 1;
					}
					if (parts.length > 2) {
						facesNormals[faceIdx] = Integer.parseInt(parts[2]) - 1;
					}
					faceIdx++;

					parts = arr[1].split("/");
					facesVertices[faceIdx] = Integer.parseInt(parts[0]) - 1;
					if (parts.length > 1) {
						facesTexCoords[faceIdx] = Integer.parseInt(parts[1]) - 1;
					}
					if (parts.length > 2) {
						facesNormals[faceIdx] = Integer.parseInt(parts[2]) - 1;
					}
					faceIdx++;

					parts = arr[2].split("/");
					facesVertices[faceIdx] = Integer.parseInt(parts[0]) - 1;
					if (parts.length > 1) {
						facesTexCoords[faceIdx] = Integer.parseInt(parts[1]) - 1;
					}
					if (parts.length > 2) {
						facesNormals[faceIdx] = Integer.parseInt(parts[2]) - 1;
					}
					faceIdx++;
					continue;
				}
			}

			float meshVertices[] = new float[facesVertices.length * 3];
			float meshNormals[] = new float[facesNormals.length * 3];
			float meshTexCoords[] = new float[facesTexCoords.length * 2];

			int nid = 0, tid = 0, vid = 0;
			for (int i = 0; i < fCount * 3; i++) {
				if (vnCount > 0) {
					int nIdx = facesNormals[i] * 3;
					meshNormals[nid++] = normals[nIdx] * scale;
					meshNormals[nid++] = normals[nIdx + 1] * scale;
					meshNormals[nid++] = normals[nIdx + 2] * scale;
				}
				if (vtCount > 0) {
					int uvIdx = facesTexCoords[i] * 2;
					meshTexCoords[tid++] = texcoords[uvIdx];
					meshTexCoords[tid++] = texcoords[uvIdx + 1];
				}

				int vIdx = facesVertices[i] * 3;
				meshVertices[vid++] = vertices[vIdx] * scale;
				meshVertices[vid++] = vertices[vIdx + 1] * scale;
				meshVertices[vid++] = vertices[vIdx + 2] * scale;
			}

			vertexCount = fCount * 3;

			// Initialize the buffers.
			mPositions = ByteBuffer
					.allocateDirect(meshVertices.length * mBytesPerFloat)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mPositions.put(meshVertices).position(0);

			mNormals = ByteBuffer
					.allocateDirect(meshNormals.length * mBytesPerFloat)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mNormals.put(meshNormals).position(0);

			mTextureCoordinates = ByteBuffer
					.allocateDirect(meshTexCoords.length * mBytesPerFloat)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			mTextureCoordinates.put(meshTexCoords).position(0);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
