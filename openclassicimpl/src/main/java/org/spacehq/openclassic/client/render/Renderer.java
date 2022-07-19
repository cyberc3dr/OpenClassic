package org.spacehq.openclassic.client.render;

import java.nio.*;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

	private static final Renderer instance = new Renderer(0x200000);

	public static Renderer get() {
		return instance;
	}

	private ByteBuffer dataByteBuffer;
	private IntBuffer dataIntBuffer;
	private FloatBuffer dataFloatBuffer;
	private int vertexArray[];
	private int vertices;
	private double texU;
	private double texV;
	private int color;
	private boolean colors;
	private boolean textures;
	private boolean normals;
	private int vertexIndex;
	private int points;
	private boolean noColors;
	private int mode;
	private double mx;
	private double my;
	private double mz;
	private int normal;
	private boolean rendering;
	private int vertexDataSize;

	private Renderer(int vertexDataSize) {
		this.vertices = 0;
		this.colors = false;
		this.textures = false;
		this.normals = false;
		this.vertexIndex = 0;
		this.points = 0;
		this.noColors = false;
		this.rendering = false;
		this.vertexDataSize = vertexDataSize;
		this.dataByteBuffer = BufferUtils.createByteBuffer(vertexDataSize * 4);
		this.dataIntBuffer = this.dataByteBuffer.asIntBuffer();
		this.dataFloatBuffer = this.dataByteBuffer.asFloatBuffer();
		this.vertexArray = new int[vertexDataSize];
	}

	public void end() {
		if(!this.rendering) throw new IllegalStateException("Not rendering!");
		this.rendering = false;
		if(this.vertices > 0) {
			this.dataIntBuffer.clear();
			this.dataIntBuffer.put(this.vertexArray, 0, this.vertexIndex);
			this.dataByteBuffer.position(0);
			this.dataByteBuffer.limit(this.vertexIndex * 4);
			if(this.textures) {
				this.dataFloatBuffer.position(3);
				glTexCoordPointer(2, 32, this.dataFloatBuffer);
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			}

			if(this.colors) {
				this.dataByteBuffer.position(20);
				glColorPointer(4, true, 32, this.dataByteBuffer);
				glEnableClientState(GL_COLOR_ARRAY);
			}

			if(this.normals) {
				this.dataByteBuffer.position(24);
				glNormalPointer(32, this.dataByteBuffer);
				glEnableClientState(GL_NORMAL_ARRAY);
			}

			this.dataFloatBuffer.position(0);
			glVertexPointer(3, 32, this.dataFloatBuffer);
			glEnableClientState(GL_VERTEX_ARRAY);
			if(this.mode == GL_QUADS) {
				glDrawArrays(GL_TRIANGLES, 0, this.vertices);
			} else {
				glDrawArrays(this.mode, 0, this.vertices);
			}

			glDisableClientState(GL_VERTEX_ARRAY);
			if(this.textures) glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			if(this.colors) glDisableClientState(GL_COLOR_ARRAY);
			if(this.normals) glDisableClientState(GL_NORMAL_ARRAY);
		}

		this.clear();
	}

	private void clear() {
		this.vertices = 0;
		this.dataByteBuffer.clear();
		this.vertexIndex = 0;
		this.points = 0;
	}

	public void begin() {
		this.begin(GL_QUADS);
	}

	public void begin(int mode) {
		if(this.rendering) {
			throw new IllegalStateException("Already rendering!");
		} else {
			this.rendering = true;
			this.clear();
			this.mode = mode;
			this.normals = false;
			this.colors = false;
			this.textures = false;
			this.noColors = false;
		}
	}

	public void texture(double u, double v) {
		this.textures = true;
		this.texU = u;
		this.texV = v;
	}

	public void color(float r, float g, float b) {
		this.color((int) (r * 255F), (int) (g * 255F), (int) (b * 255F));
	}

	public void color(float r, float g, float b, float a) {
		this.color((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), (int) (a * 255F));
	}

	public void color(int r, int g, int b) {
		this.color(r, g, b, 255);
	}

	public void color(int r, int g, int b, int a) {
		if(this.noColors) return;
		if(r > 255) r = 255;
		if(g > 255) g = 255;
		if(b > 255) b = 255;
		if(a > 255) a = 255;
		if(r < 0) r = 0;
		if(g < 0) g = 0;
		if(b < 0) b = 0;
		if(a < 0) a = 0;
		this.colors = true;
		this.color = a << 24 | b << 16 | g << 8 | r;
	}

	public void vertexuv(double x, double y, double z, double u, double v) {
		this.texture(u, v);
		this.vertex(x, y, z);
	}

	public void vertex(double x, double y, double z) {
		this.points++;
		if(this.mode == GL_QUADS && this.points % 4 == 0) {
			for(int count = 0; count < 2; count++) {
				int mod = 8 * (3 - count);
				if(this.textures) {
					this.vertexArray[this.vertexIndex + 3] = this.vertexArray[(this.vertexIndex - mod) + 3];
					this.vertexArray[this.vertexIndex + 4] = this.vertexArray[(this.vertexIndex - mod) + 4];
				}

				if(this.colors) {
					this.vertexArray[this.vertexIndex + 5] = this.vertexArray[(this.vertexIndex - mod) + 5];
				}
				
				if(this.normals) {
					this.vertexArray[this.vertexIndex + 6] = this.vertexArray[(this.vertexIndex - mod) + 6];
				}

				this.vertexArray[this.vertexIndex + 0] = this.vertexArray[this.vertexIndex - mod];
				this.vertexArray[this.vertexIndex + 1] = this.vertexArray[(this.vertexIndex - mod) + 1];
				this.vertexArray[this.vertexIndex + 2] = this.vertexArray[(this.vertexIndex - mod) + 2];
				this.vertices++;
				this.vertexIndex += 8;
			}
		}

		if(this.textures) {
			this.vertexArray[this.vertexIndex + 3] = Float.floatToRawIntBits((float) this.texU);
			this.vertexArray[this.vertexIndex + 4] = Float.floatToRawIntBits((float) this.texV);
		}

		if(this.colors) {
			this.vertexArray[this.vertexIndex + 5] = this.color;
		}

		if(this.normals) {
			this.vertexArray[this.vertexIndex + 6] = this.normal;
		}

		this.vertexArray[this.vertexIndex] = Float.floatToRawIntBits((float) (x + this.mx));
		this.vertexArray[this.vertexIndex + 1] = Float.floatToRawIntBits((float) (y + this.my));
		this.vertexArray[this.vertexIndex + 2] = Float.floatToRawIntBits((float) (z + this.mz));
		this.vertexIndex += 8;
		this.vertices++;
		if(this.vertices % 4 == 0 && this.vertexIndex >= this.vertexDataSize - 32) {
			this.end();
			this.rendering = true;
		}
	}

	public void color(int rgb) {
		this.color(rgb, false);
	}

	public void color(int rgb, boolean alpha) {
		int r = rgb >> 16 & 0xff;
		int g = rgb >> 8 & 0xff;
		int b = rgb & 0xff;
		if(alpha) {
			int a = rgb >> 24 & 0xff;
			this.color(r, g, b, a);
		} else {
			this.color(r, g, b);
		}
	}

	public void disableColors() {
		this.noColors = true;
	}

	public void normal(float x, float y, float z) {
		this.normals = true;
		byte xx = (byte) (int) (x * 128F);
		byte yy = (byte) (int) (y * 127F);
		byte zz = (byte) (int) (z * 127F);
		this.normal = xx | yy << 8 | zz << 16;
	}

	public void setBasePosition(double x, double y, double z) {
		this.mx = x;
		this.my = y;
		this.mz = z;
	}

	public void translate(float x, float y, float z) {
		this.mx += x;
		this.my += y;
		this.mz += z;
	}

}
