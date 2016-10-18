/**
 * 車を定義するクラス
 */

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.gl2.GLUT;


public class MyCar {

	// 車の色
	float color[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	float silver[] = { 0.5f, 0.5f, 0.5f, 1.0f };
	
	// 車の頂点の座標値
	double vertex1[][] = {
		  { -0.5, 0.0,  0.0 },
		  {  0.5, 0.0,  0.0 },
		  {  0.5, 0.25, 0.0 },
		  { -0.5, 0.25, 0.0 },
		  { -0.5, 0.0,  0.5 },
		  {  0.5, 0.0,  0.5 },
		  {  0.5, 0.25, 0.5 },
		  { -0.5, 0.25, 0.5 }
	};

	//　車の頂点の座標値
	double vertex2[][] = {
		  { -0.3, 0.25, 0.0 },
		  {  0.3, 0.25, 0.0 },
		  {  0.2, 0.5,  0.0 },
		  { -0.2, 0.5,  0.0 },
		  { -0.3, 0.25, 0.5 },
		  {  0.3, 0.25, 0.5 },
		  {  0.2, 0.5,  0.5 },
		  { -0.2, 0.5,  0.5 }
	};
	
	// 車のボディを表現する直方体の各面を構成する頂点の番号
	int face[][] = {
		  { 0, 3, 2, 1 },
		  { 1, 2, 6, 5 },
		  { 5, 6, 7, 4 },
		  { 4, 7, 3, 0 },
		  { 4, 0, 1, 5 },
		  { 3, 7, 6, 2 }
	};

	// 車のボディを表現する直方体の各面の法線ベクトル
	double normal[][] = {
		  { 0.0, 0.0, 1.0 },
		  {-1.0, 0.0, 0.0 },
		  { 0.0, 0.0,-1.0 },
		  { 1.0, 0.0, 0.0 },
		  { 0.0, 1.0, 0.0 },
		  { 0.0,-1.0, 0.0 }
	};

	// 車の回転運動しているときの回転角
	static int r = 0;
	
	// 車の角速度（に相当する値）
	static int velocity = 5;
	
	// 車の回転中心からの変位
	static double transform = 1.0;
	
		
	/**
	 * 車のボディの色を設定する
	 */
	public void setColor(double r, double g, double b) {
		color[0] = (float)r;
		color[1] = (float)g;
		color[2] = (float)b;
		color[3] = 1.0f;
	}
	
	/**
	 * 車の速度を設定する
	 */
	public void setVelocity(int v) {
		velocity = v;
	}
	
	/**
	 * 車の回転中心からの変位を設定する
	 */
	public void setTransform(double t) {
		transform = t;
	}
	
	/**
	 * 車の回転核を計算する
	 */  
	static void calculateMovement() {  
		r += velocity;
	    if (r >= 3600) {
	    	r = 0;
	    }
	}

	
	/**
	 * 車の回転をリセットする
	 */
	static void resetMovement() {
		r = 0;
	}
	
	
	
	/**
	 * 車を描画する
	 */
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		
		// 車の変位を求める
		gl.glRotated(((double)r * 0.1), 0.0, 1.0, 0.0);
		gl.glTranslated(0.0, 0.0, transform);
		 
		// ボディの拡散反射係数・鏡面反射係数を設定する
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silver, 0);

		// ボディの各面について
		for (int j = 0; j < 6; ++j) {
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(normal[j], 0);
			for (int i = 0; i < 4; ++i) {
				gl.glVertex3dv(vertex1[face[j][i]], 0);
			}
			gl.glEnd();
		}

		// ボディの各面について
		for (int j = 0; j < 6; ++j) {
			gl.glBegin(GL2.GL_POLYGON);
			gl.glNormal3dv(normal[j], 0);
			for (int i = 0; i < 4; ++i) {
				gl.glVertex3dv(vertex2[face[j][i]], 0);
			}
			gl.glEnd();
		}

		// タイヤの拡散反射係数・鏡面反射係数を設定する
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, silver, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silver, 0);

		// タイヤを4個の球で近似表現する
		gl.glTranslated(0.2, 0.0, 0.05);
		glut.glutSolidSphere(0.1, 30, 20);

		gl.glTranslated(-0.4, 0.0, 0.0);
		glut.glutSolidSphere(0.1, 30, 20);

		gl.glTranslated(0.0, 0.0, 0.4);
		glut.glutSolidSphere(0.1, 30, 20);

		gl.glTranslated(0.4, 0.0, 0.0);
		glut.glutSolidSphere(0.1, 30, 20);
		
		// 回転角を算出する
		calculateMovement();
	}
	
	

}
