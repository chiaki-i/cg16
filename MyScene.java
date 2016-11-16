/**
 * 描画するシーンを定義するクラス
 */

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;


public class MyScene {

	// 旗と車を1個ずつ定義する
	
	static MyFlag flag1 = null;
	static MyCar car1 = null;
	
	static MyMountainBike bike1 = null;

	
	/**
	 * シーンの初期化
	 */
	public static void init() {
		
		 // 旗の初期化
		 flag1 = new MyFlag();
		 
		 
		 // 車の初期化および色・速度・変位の設定
		 car1 = new MyCar();
		 car1.setColor(1.0, 0.0, 0.0);
		 car1.setVelocity(5);
		 car1.setTransform(1.5);
		

		 //とりあえず自転車を表示させておく
		 bike1 = new MyMountainBike();

	}
	
	/**
	 * シーンを描画する
	 */
	public static void draw(GLAutoDrawable drawable) {
		if(drawable == null) return;
		
		GL2 gl = drawable.getGL().getGL2();

		 // 物体が裏面を向いていたとしても光を当てる
		 gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); 
		/*
	    // 旗を描画する
	    gl.glPushMatrix();
	    if(flag1 != null)
	    	flag1.draw(drawable);
	    gl.glPopMatrix();
	 
	    // 車を描画する
	    
		gl.glPushMatrix();
	    if(car1 != null)
	    	car1.draw(drawable);
	    gl.glPopMatrix();
	  
	  */
	    // ドーナツを描画する
		gl.glPushMatrix();
	    if(car1 != null) bike1.draw(drawable);
	   gl.glPopMatrix();	  
	}
	
	/**
	 * 動きをリセットする
	 */
	public static void resetMovement() {

		// 車の回転を初期状態に戻す
		car1.resetMovement();
	}
	
}
