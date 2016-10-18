/**
 * 旗を定義するクラス
 */

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class MyFlag {

	// 旗の柱の頂点の座標値
	double vertex3[][] = {
		  { -0.02, 0.0, -0.02 },
		  {  0.02, 0.0, -0.02 },
		  {  0.02, 1.0, -0.02 },
		  { -0.02, 1.0, -0.02 },
		  { -0.02, 0.0,  0.02 },
		  {  0.02, 0.0,  0.02 },
		  {  0.02, 1.0,  0.02 },
		  { -0.02, 1.0,  0.02 }
	};

	// 旗の布の頂点の座標値
	double vertex4[][] = {
			{ 0.5, 0.8, -0.5},
			{ 0.0, 1.0, 0.0},
			{ 0.0, 0.6, 0.0}
	};

	// 旗の柱を構成する各面の頂点番号
	int face[][] = {
		  { 0, 3, 2, 1 },
		  { 1, 2, 6, 5 },
		  { 5, 6, 7, 4 },
		  { 4, 7, 3, 0 },
		  { 4, 0, 1, 5 },
		  { 3, 7, 6, 2 }
	};

	// 旗の柱を構成する各面の法線ベクトル
	double normal[][] = {
		  { 0.0, 0.0, 1.0 },
		  {-1.0, 0.0, 0.0 },
		  { 0.0, 0.0,-1.0 },
		  { 1.0, 0.0, 0.0 },
		  { 0.0, 1.0, 0.0 },
		  { 0.0,-1.0, 0.0 }
	};


	// 色の定義
	float red[] = { 0.8f, 0.0f, 0.0f, 1.0f };
	float blue[] = { 0.2f, 0.2f, 0.8f, 1.0f };
	float silver[] = { 0.5f, 0.5f, 0.5f, 1.0f };

	/**
	 * 旗を描画する
	 */
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		// 柱の拡散反射係数・鏡面反射係数を設定する
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, blue, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silver, 0);

		// 柱の各面について
		for (int j = 0; j < 6; ++j) {
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(normal[j], 0);
			for (int i = 0; i < 4; ++i) {
				gl.glVertex3dv(vertex3[face[j][i]], 0);
			}
			gl.glEnd();
		}

		// 布の拡散反射係数・鏡面反射係数を設定する
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, red, 0);

		// 布の面について
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < 3; ++i) {
			gl.glVertex3dv(vertex4[i], 0);
		}
		gl.glEnd();
	}
	
}
