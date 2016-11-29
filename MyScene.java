/**
 * 描画するシーンを定義するクラス
 */

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;


public class MyScene {
	
	static MyField field1 = null;
	static MyMountainBike bike1 = null;

	/**
	 * シーンの初期化
	 */
	public static void init() {
		
		//地面
		field1 = new MyField();

		//とりあえず自転車を表示させておく
		bike1 = new MyMountainBike();
		bike1.setDist_bike(20);
		bike1.setVelocity_bike(5);
		bike1.setVelocity_wheel(20);
		bike1.setVelocity_front(3);
		bike1.setFlag(bike1.LISSAJOUS);

	}
	
	/**
	 * シーンを描画する
	 */
	public static void draw(GLAutoDrawable drawable) {
		if(drawable == null) return;
		
		GL2 gl = drawable.getGL().getGL2();

		 // 物体が裏面を向いていたとしても光を当てる
		 gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); 
		
	   // 自転車を描画する
		gl.glPushMatrix();
	    if(field1 != null) field1.draw(drawable);
	   gl.glPopMatrix();

	    // 自転車を描画する
		gl.glPushMatrix();
	    if(bike1 != null) bike1.draw(drawable);
	   gl.glPopMatrix();	
	}
	
	/**
	 * 動きをリセットする
	 */
	public static void resetMovement() {

		// 車の回転を初期状態に戻す
		bike1.resetMovement();
	}
	
}
