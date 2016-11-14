/**
 * 描画処理のためのクラス
 */


import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.media.opengl.GLEventListener;

import java.lang.Math;


public class CgDrawer implements GLEventListener {
	GLAutoDrawable glAD;
	
	// 光源の位置を設定
	static float light0pos[] = { 0.0f, -6.0f, -10.0f, 1.0f };//画面右下から当てる光
	static float light1pos[] = { -10.0f, 6.0f, 0.0f, 1.0f };//画面左上から当てる光


	
	/**
	 * 描画処理のための初期化
	 */
    public void init(GLAutoDrawable drawable) {
        float silver[] = {0.5f, 0.5f, 0.5f, 1.0f};
    	
        this.glAD = drawable;
      
        GL2 gl= drawable.getGL().getGL2();
	    
        // Zバッファ法を有効にする
		gl.glEnable(GL.GL_RGBA);
		gl.glEnable(GL2.GL_DEPTH);
        gl.glEnable(GL2.GL_DOUBLE);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glCullFace(GL.GL_BACK);
        
        // 2個の光源を有効にする
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, silver, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, silver, 0);
        
        // 背景色を白にする
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
	}
    
    /**
     * ウィンドウの大きさを変更したときに自動的に呼び出されるメソッド
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        GLUgl2 glu = new GLUgl2();

        // ウィンドウの横縦比を求める
        if (height <= 0) 
            height = 1;
        float h = (float) width / (float) height;

        // ビューポートを設定する
        gl.glViewport(0, 0, width, height);

        // ここから投影変換に関する設定
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(30.0, h, 1.0, 100.0);
               
        // ここから物体の幾何変換に関する設定
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
		 
    }
    

    /**
     * 物体を描画するときに呼び出されるメソッド
     */
    public void display(GLAutoDrawable drawable) {
    	
        // 物体を描画する
        draw(drawable);
      
    }
    

    /**
     *　ディスプレイ装置自体を別の物に差し替えたときに呼び出されるメソッド
     *　（本講義では想定外なので空っぽのメソッドとしておく）
     */
    public void displayChanged(
    	GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    	;
    }

    

	/**
	 * 物体を描画するときに呼び出されるメソッド
	 */
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLUgl2 glu = new GLUgl2();

		// ウィンドウをクリアする
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// 幾何変換のための行列を初期化する
		gl.glLoadIdentity();

		// 視点を設定する
		glu.gluLookAt(20.0f, 20.0f, 50.0f,  /* カメラの座標 */
									0.0f, 0.0f, 0.0f, 	/* 注視点の座標 */
									0.0f, 1.0f, 0.0f);  /* 画面の上方向を指すベクトル */
		

		// 光源の位置を設定する
		//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0pos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light1pos, 0);
		
		// シーンを描画する
		MyScene.draw(drawable);

	}

	public void cylinder(float radius, float height, int sides, GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUgl2 glu = new GLUgl2();
		double pi = 3.1415;
		float x = 0.0f, y = 0.0f, z = 0.0f;
		
		//上面
		gl.glNormal3d(0.0, 1.0, 0.0);//法線ベクトル。Y軸正方向が上となる。
		gl.glBegin(GL2.GL_POLYGON);
		for(double i = sides; i >= 0; --i) {
			double t = pi*2/sides * (double)i;
			gl.glVertex3d(x + radius*Math.cos(t), y + height, z + radius*Math.sin(t));
		}
		gl.glEnd();

		//側面
		gl.glBegin(GL2.GL_QUAD_STRIP);
		for(double i = 0; i <= sides; i = i+1){
			double t = i*2*pi/sides;
			gl.glNormal3f( (float)Math.cos(t), 0.0f, (float)Math.sin(t) );//法線ベクトルの設定。面の外向き方向へのベクトル。
			gl.glVertex3f( x + radius*(float)Math.cos(t), y - height, z + radius*(float)Math.sin(t) );
			gl.glVertex3f( x + radius*(float)Math.cos(t), y + height, z + radius*(float)Math.sin(t) );
		}
		gl.glEnd();

		//下面
		gl.glNormal3d(0.0, -1.0, 0.0);//法線ベクトル。Y軸負方向が上となる。
		gl.glBegin(GL2.GL_POLYGON);
		for(double i = 0; i < sides; i++) {
			double t = pi*2/sides*(double)i;
			gl.glVertex3d( x + radius*Math.cos(t), y - height, z + radius*Math.sin(t) );
		}
		gl.glEnd();
	}
	
	
	public GLAutoDrawable getGLAutoDrawable() {
		return glAD;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
