package org.spacehq.openclassic.client.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Frustum {

	public static float[][] frustum = new float[16][16];
	public static float[] projectionMatrix = new float[16];
	public static float[] modelviewMatrix = new float[16];
	public static float[] clippingMatrix = new float[16];

	private static FloatBuffer projectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer modelviewMatrixBuffer = BufferUtils.createFloatBuffer(16);

	public static void update() {
		projectionMatrixBuffer.clear();
		modelviewMatrixBuffer.clear();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrixBuffer);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelviewMatrixBuffer);
		projectionMatrixBuffer.flip().limit(16);
		projectionMatrixBuffer.get(projectionMatrix);
		modelviewMatrixBuffer.flip().limit(16);
		modelviewMatrixBuffer.get(modelviewMatrix);
		clippingMatrix[0] = modelviewMatrix[0] * projectionMatrix[0] + modelviewMatrix[1] * projectionMatrix[4] + modelviewMatrix[2] * projectionMatrix[8] + modelviewMatrix[3] * projectionMatrix[12];
		clippingMatrix[1] = modelviewMatrix[0] * projectionMatrix[1] + modelviewMatrix[1] * projectionMatrix[5] + modelviewMatrix[2] * projectionMatrix[9] + modelviewMatrix[3] * projectionMatrix[13];
		clippingMatrix[2] = modelviewMatrix[0] * projectionMatrix[2] + modelviewMatrix[1] * projectionMatrix[6] + modelviewMatrix[2] * projectionMatrix[10] + modelviewMatrix[3] * projectionMatrix[14];
		clippingMatrix[3] = modelviewMatrix[0] * projectionMatrix[3] + modelviewMatrix[1] * projectionMatrix[7] + modelviewMatrix[2] * projectionMatrix[11] + modelviewMatrix[3] * projectionMatrix[15];
		clippingMatrix[4] = modelviewMatrix[4] * projectionMatrix[0] + modelviewMatrix[5] * projectionMatrix[4] + modelviewMatrix[6] * projectionMatrix[8] + modelviewMatrix[7] * projectionMatrix[12];
		clippingMatrix[5] = modelviewMatrix[4] * projectionMatrix[1] + modelviewMatrix[5] * projectionMatrix[5] + modelviewMatrix[6] * projectionMatrix[9] + modelviewMatrix[7] * projectionMatrix[13];
		clippingMatrix[6] = modelviewMatrix[4] * projectionMatrix[2] + modelviewMatrix[5] * projectionMatrix[6] + modelviewMatrix[6] * projectionMatrix[10] + modelviewMatrix[7] * projectionMatrix[14];
		clippingMatrix[7] = modelviewMatrix[4] * projectionMatrix[3] + modelviewMatrix[5] * projectionMatrix[7] + modelviewMatrix[6] * projectionMatrix[11] + modelviewMatrix[7] * projectionMatrix[15];
		clippingMatrix[8] = modelviewMatrix[8] * projectionMatrix[0] + modelviewMatrix[9] * projectionMatrix[4] + modelviewMatrix[10] * projectionMatrix[8] + modelviewMatrix[11] * projectionMatrix[12];
		clippingMatrix[9] = modelviewMatrix[8] * projectionMatrix[1] + modelviewMatrix[9] * projectionMatrix[5] + modelviewMatrix[10] * projectionMatrix[9] + modelviewMatrix[11] * projectionMatrix[13];
		clippingMatrix[10] = modelviewMatrix[8] * projectionMatrix[2] + modelviewMatrix[9] * projectionMatrix[6] + modelviewMatrix[10] * projectionMatrix[10] + modelviewMatrix[11] * projectionMatrix[14];
		clippingMatrix[11] = modelviewMatrix[8] * projectionMatrix[3] + modelviewMatrix[9] * projectionMatrix[7] + modelviewMatrix[10] * projectionMatrix[11] + modelviewMatrix[11] * projectionMatrix[15];
		clippingMatrix[12] = modelviewMatrix[12] * projectionMatrix[0] + modelviewMatrix[13] * projectionMatrix[4] + modelviewMatrix[14] * projectionMatrix[8] + modelviewMatrix[15] * projectionMatrix[12];
		clippingMatrix[13] = modelviewMatrix[12] * projectionMatrix[1] + modelviewMatrix[13] * projectionMatrix[5] + modelviewMatrix[14] * projectionMatrix[9] + modelviewMatrix[15] * projectionMatrix[13];
		clippingMatrix[14] = modelviewMatrix[12] * projectionMatrix[2] + modelviewMatrix[13] * projectionMatrix[6] + modelviewMatrix[14] * projectionMatrix[10] + modelviewMatrix[15] * projectionMatrix[14];
		clippingMatrix[15] = modelviewMatrix[12] * projectionMatrix[3] + modelviewMatrix[13] * projectionMatrix[7] + modelviewMatrix[14] * projectionMatrix[11] + modelviewMatrix[15] * projectionMatrix[15];
		frustum[0][0] = clippingMatrix[3] - clippingMatrix[0];
		frustum[0][1] = clippingMatrix[7] - clippingMatrix[4];
		frustum[0][2] = clippingMatrix[11] - clippingMatrix[8];
		frustum[0][3] = clippingMatrix[15] - clippingMatrix[12];
		normalize(frustum, 0);
		frustum[1][0] = clippingMatrix[3] + clippingMatrix[0];
		frustum[1][1] = clippingMatrix[7] + clippingMatrix[4];
		frustum[1][2] = clippingMatrix[11] + clippingMatrix[8];
		frustum[1][3] = clippingMatrix[15] + clippingMatrix[12];
		normalize(frustum, 1);
		frustum[2][0] = clippingMatrix[3] + clippingMatrix[1];
		frustum[2][1] = clippingMatrix[7] + clippingMatrix[5];
		frustum[2][2] = clippingMatrix[11] + clippingMatrix[9];
		frustum[2][3] = clippingMatrix[15] + clippingMatrix[13];
		normalize(frustum, 2);
		frustum[3][0] = clippingMatrix[3] - clippingMatrix[1];
		frustum[3][1] = clippingMatrix[7] - clippingMatrix[5];
		frustum[3][2] = clippingMatrix[11] - clippingMatrix[9];
		frustum[3][3] = clippingMatrix[15] - clippingMatrix[13];
		normalize(frustum, 3);
		frustum[4][0] = clippingMatrix[3] - clippingMatrix[2];
		frustum[4][1] = clippingMatrix[7] - clippingMatrix[6];
		frustum[4][2] = clippingMatrix[11] - clippingMatrix[10];
		frustum[4][3] = clippingMatrix[15] - clippingMatrix[14];
		normalize(frustum, 4);
		frustum[5][0] = clippingMatrix[3] + clippingMatrix[2];
		frustum[5][1] = clippingMatrix[7] + clippingMatrix[6];
		frustum[5][2] = clippingMatrix[11] + clippingMatrix[10];
		frustum[5][3] = clippingMatrix[15] + clippingMatrix[14];
		normalize(frustum, 5);
	}

	private static void normalize(float[][] frustum, int plane) {
		float len = (float) Math.sqrt(frustum[plane][0] * frustum[plane][0] + frustum[plane][1] * frustum[plane][1] + frustum[plane][2] * frustum[plane][2]);
		frustum[plane][0] /= len;
		frustum[plane][1] /= len;
		frustum[plane][2] /= len;
		frustum[plane][3] /= len;
	}

	public static boolean isBoxInFrustum(float x1, float y1, float z1, float x2, float y2, float z2) {
		for(int plane = 0; plane < 6; plane++) {
			if(frustum[plane][0] * x1 + frustum[plane][1] * y1 + frustum[plane][2] * z1 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x2 + frustum[plane][1] * y1 + frustum[plane][2] * z1 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x1 + frustum[plane][1] * y2 + frustum[plane][2] * z1 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x2 + frustum[plane][1] * y2 + frustum[plane][2] * z1 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x1 + frustum[plane][1] * y1 + frustum[plane][2] * z2 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x2 + frustum[plane][1] * y1 + frustum[plane][2] * z2 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x1 + frustum[plane][1] * y2 + frustum[plane][2] * z2 + frustum[plane][3] <= 0.0F && frustum[plane][0] * x2 + frustum[plane][1] * y2 + frustum[plane][2] * z2 + frustum[plane][3] <= 0.0F) {
				return false;
			}
		}

		return true;
	}

}
