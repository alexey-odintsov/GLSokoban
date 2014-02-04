package com.alekso.glsokoban;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.util.Log;

public class GLText {
 
 	/**
 	 * TODO: move presets out to separate text configuration file 
 	 */
    float textAttributes2[][] = { //
            { 6.0f, 6.0f, 33.0f, 87.0f }, { 45.0f, 6.0f, 13.0f, 87.0f }, { 64.0f, 6.0f, 25.0f, 87.0f },
            { 95.0f, 6.0f, 30.0f, 87.0f }, { 140.0f, 6.0f, 34.0f, 87.0f }, { 180.0f, 6.0f, 40.0f, 87.0f },
            { 226.0f, 6.0f, 39.0f, 87.0f }, { 271.0f, 6.0f, 11.0f, 87.0f }, { 288.0f, 6.0f, 23.0f, 87.0f },
            { 317.0f, 6.0f, 22.0f, 87.0f }, { 345.0f, 6.0f, 31.0f, 87.0f }, { 382.0f, 6.0f, 36.0f, 87.0f },
            { 424.0f, 6.0f, 20.0f, 87.0f }, { 450.0f, 6.0f, 23.0f, 87.0f }, { 479.0f, 6.0f, 15.0f, 87.0f },
            { 500.0f, 6.0f, 32.0f, 87.0f },

            { 6.0f, 99.0f, 35.0f, 87.0f }, { 47.0f, 99.0f, 33.0f, 87.0f }, { 86.0f, 99.0f, 32.0f, 87.0f },
            { 124.0f, 99.0f, 32.0f, 87.0f }, { 162.0f, 99.0f, 39.0f, 87.0f }, { 207.0f, 99.0f, 30.0f, 87.0f },
            { 243.0f, 99.0f, 34.0f, 87.0f }, { 283.0f, 99.0f, 33.0f, 87.0f }, { 322.0f, 99.0f, 33.0f, 87.0f },
            { 361.0f, 99.0f, 34.0f, 87.0f }, { 401.0f, 99.0f, 14.0f, 87.0f }, { 421.0f, 99.0f, 20.0f, 87.0f },
            { 447.0f, 99.0f, 30.0f, 87.0f }, { 483.0f, 99.0f, 33.0f, 87.0f }, { 522.0f, 99.0f, 30.0f, 87.0f },
            { 558.0f, 99.0f, 25.0f, 87.0f },

            { 6.0f, 192.0f, 41.0f, 87.0f }, { 53.0f, 192.0f, 41.0f, 87.0f }, { 100.0f, 192.0f, 33.0f, 87.0f },
            { 139.0f, 192.0f, 34.0f, 87.0f }, { 179.0f, 192.0f, 36.0f, 87.0f }, { 221.0f, 192.0f, 29.0f, 87.0f },
            { 256.0f, 192.0f, 28.0f, 87.0f }, { 290.0f, 192.0f, 36.0f, 87.0f }, { 332.0f, 192.0f, 35.0f, 87.0f },
            { 373.0f, 192.0f, 31.0f, 87.0f }, { 410.0f, 192.0f, 27.0f, 87.0f }, { 443.0f, 192.0f, 34.0f, 87.0f },
            { 483.0f, 192.0f, 29.0f, 87.0f }, { 518.0f, 192.0f, 39.0f, 87.0f }, { 563.0f, 192.0f, 33.0f, 87.0f },
            { 602.0f, 192.0f, 38.0f, 87.0f },

            { 6.0f, 285.0f, 33.0f, 87.0f }, { 45.0f, 285.0f, 39.0f, 87.0f }, { 90.0f, 285.0f, 34.0f, 87.0f },
            { 130.0f, 285.0f, 34.0f, 87.0f }, { 170.0f, 285.0f, 35.0f, 87.0f }, { 211.0f, 285.0f, 35.0f, 87.0f },
            { 252.0f, 285.0f, 41.0f, 87.0f }, { 299.0f, 285.0f, 39.0f, 87.0f }, { 344.0f, 285.0f, 40.0f, 87.0f },
            { 390.0f, 285.0f, 41.0f, 87.0f }, { 437.0f, 285.0f, 35.0f, 87.0f }, { 478.0f, 285.0f, 21.0f, 87.0f },
            { 505.0f, 285.0f, 33.0f, 87.0f }, { 544.0f, 285.0f, 21.0f, 87.0f }, { 571.0f, 285.0f, 34.0f, 87.0f },
            { 611.0f, 285.0f, 41.0f, 87.0f },

            { 6.0f, 378.0f, 20.0f, 87.0f }, { 32.0f, 378.0f, 32.0f, 87.0f }, { 70.0f, 378.0f, 33.0f, 87.0f },
            { 109.0f, 378.0f, 30.0f, 87.0f }, { 145.0f, 378.0f, 33.0f, 87.0f }, { 184.0f, 378.0f, 33.0f, 87.0f },
            { 223.0f, 378.0f, 37.0f, 87.0f }, { 266.0f, 378.0f, 36.0f, 87.0f }, { 308.0f, 378.0f, 31.0f, 87.0f },
            { 345.0f, 378.0f, 31.0f, 87.0f }, { 382.0f, 378.0f, 29.0f, 87.0f }, { 417.0f, 378.0f, 33.0f, 87.0f },
            { 456.0f, 378.0f, 31.0f, 87.0f }, { 493.0f, 378.0f, 35.0f, 87.0f }, { 534.0f, 378.0f, 31.0f, 87.0f },
            { 571.0f, 378.0f, 35.0f, 87.0f },

            { 6.0f, 472.0f, 33.0f, 87.0f }, { 45.0f, 472.0f, 33.0f, 87.0f }, { 84.0f, 472.0f, 32.0f, 87.0f },
            { 122.0f, 472.0f, 30.0f, 87.0f }, { 158.0f, 472.0f, 33.0f, 87.0f }, { 198.0f, 472.0f, 31.0f, 87.0f },
            { 235.0f, 472.0f, 37.0f, 87.0f }, { 278.0f, 472.0f, 39.0f, 87.0f }, { 323.0f, 472.0f, 37.0f, 87.0f },
            { 366.0f, 472.0f, 37.0f, 87.0f }, { 409.0f, 472.0f, 31.0f, 87.0f }, { 446.0f, 472.0f, 29.0f, 87.0f },
            { 481.0f, 472.0f, 9.0f, 87.0f }, { 496.0f, 472.0f, 29.0f, 87.0f }, };

    float vertices[];

    float uvs[];

    int textMapping[];

    FloatBuffer mPositions;

    FloatBuffer mTextureCoordinates;
    
    public static final String TAG = "GLText";

    public GLText(int textureId, float textureSize) {

        textMapping = new int[255];

        Log.d(TAG, "letters count = " + textAttributes2.length);

        // scale letters components for texturing
        for (int i = 0; i < textAttributes2.length; i++) {
            textAttributes2[i][0] /= textureSize;
            textAttributes2[i][1] /= textureSize; // = (textureSize - textAttributes2[i][1]) / textureSize;
            textAttributes2[i][2] /= textureSize;
            textAttributes2[i][3] /= textureSize;
        }

        vertices = new float[textAttributes2.length * 3 * 4];
        uvs = new float[textAttributes2.length * 2 * 4];

        // generate vertices
        for (int i = 0; i < textAttributes2.length; i++) {
            // 0
            vertices[i * 3 * 4 + 0] = 0.0f;
            vertices[i * 3 * 4 + 1] = textAttributes2[i][3];
            vertices[i * 3 * 4 + 2] = 0.0f;
            uvs[i * 2 * 4 + 0] = textAttributes2[i][0];
            uvs[i * 2 * 4 + 1] = textAttributes2[i][1];

            // 1
            vertices[i * 3 * 4 + 3] = 0.0f;
            vertices[i * 3 * 4 + 4] = 0.0f;
            vertices[i * 3 * 4 + 5] = 0.0f;
            uvs[i * 2 * 4 + 2] = textAttributes2[i][0];
            uvs[i * 2 * 4 + 3] = textAttributes2[i][1] + textAttributes2[i][3];

            // 2
            vertices[i * 3 * 4 + 6] = textAttributes2[i][2];
            vertices[i * 3 * 4 + 7] = textAttributes2[i][3];
            vertices[i * 3 * 4 + 8] = 0.0f;
            uvs[i * 2 * 4 + 4] = textAttributes2[i][0] + textAttributes2[i][2];
            uvs[i * 2 * 4 + 5] = textAttributes2[i][1];

            // 3
            vertices[i * 3 * 4 + 9] = textAttributes2[i][2];
            vertices[i * 3 * 4 + 10] = 0.0f;
            vertices[i * 3 * 4 + 11] = 0.0f;
            uvs[i * 2 * 4 + 6] = textAttributes2[i][0] + textAttributes2[i][2];
            uvs[i * 2 * 4 + 7] = textAttributes2[i][1] + textAttributes2[i][3];

        }

        // make mapping between characters and bitmaps
        for (int i = 0; i < textAttributes2.length; i++) {
            textMapping[32 + i] = i; // ?
        }

        // Initialize the buffers.
        mPositions = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPositions.put(vertices).position(0);

        mTextureCoordinates = ByteBuffer.allocateDirect(uvs.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureCoordinates.put(uvs).position(0);
    }

}

