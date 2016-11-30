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
	static float light0pos[] = { 0.0f, 0.0f, -100.0f, 1.0f };//画面右下から当てる光
	static float light1pos[] = { -10.0f, 10.0f, 10.0f, 1.0f };//画面左上から当てる光


	
	/**
	 * 描画処理のための初期化
	 */
	public void init(GLAutoDrawable drawable) {
		float silver[] = {0.2f, 0.2f, 0.2f, 1.0f};
		float skyblue[] = { 0.69140625f, 0.890625f, 0.890625f};
    float ambient [] = { 0.0f, 0.0f, 0.0f, 1.0f};
    float diffuse [] = { 0.7f, 0.7f, 0.7f, 1.0f};
    float specular[] = { 0.8f, 0.8f, 0.8f, 1.0f};

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
		
		//光源を有効にする
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_LIGHT1);

		//光源0の設定
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light1pos,0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient,0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse,0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular,0);
    gl.glLightModelf(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);

		//光源1の設定
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, light1pos,0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient,0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse,0);
    gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular,0);
    gl.glLightModelf(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);

 		//材質の設定
    float material_ambient  [] = { 0.1f, 0.1f, 0.1f, 1.0f};
    float material_diffuse  [] = { 1.0f, 1.0f, 1.0f, 1.0f};
    float material_specular [] = { 1.0f, 1.0f, 1.0f, 1.0f};
    float material_shininess[] = { 0.1f};
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, material_ambient,0);
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, material_diffuse,0);
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, material_specular,0);
    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, material_shininess,0);


    // 背景色設定
		gl.glClearColor(skyblue[0], skyblue[1], skyblue[2], 1.0f);

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
        //glFrustum(GLdouble left, GLdouble right, GLdouble bottom, GLdouble top, GLdouble near, GLdouble far);
        //http://www.wakayama-u.ac.jp/~wuhy/GSS/04.htmlここみて
        //gl.glFrustum(200.0, 200.0, 100.0, 120.0, 50.0, 1000.0);
  	glu.gluPerspective(60.0, h, 1.0, 100.0);

        // ここから物体の幾何変換に関する設定
  	gl.glMatrixMode(GL2.GL_MODELVIEW);
  	gl.glLoadIdentity();


  }

  //物体を描画するときに呼び出されるメソッド
  public void display(GLAutoDrawable drawable) {

        // 物体を描画する
  	draw(drawable);

  }


  //ディスプレイ装置自体を別の物に差し替えたときに呼び出されるメソッド
  //(本講義では想定外なので空っぽのメソッドとしておく）
  public void displayChanged(
  	GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
  	;
  }

  //物体を描画するときに呼び出されるメソッド
  public void draw(GLAutoDrawable drawable) {
  	GL2 gl = drawable.getGL().getGL2();
  	GLUgl2 glu = new GLUgl2();

		// ウィンドウをクリアする
  	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// 幾何変換のための行列を初期化する
  	gl.glLoadIdentity();

		// 視点を設定する
  	glu.gluLookAt(0.0f, 20.0f, 50.0f,  /* カメラの座標 */
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

		//下面 //逆向き！
		gl.glNormal3d(0.0, -1.0, 0.0);//法線ベクトル。Y軸負方向が上となる。
		gl.glBegin(GL2.GL_POLYGON);
		for(double i = 0; i < sides; i++) {
			double t = pi*2/sides*(double)i;
			gl.glVertex3d( x + radius*Math.cos(t), y - height, z + radius*Math.sin(t) );
		}
		gl.glEnd();
	}
	
	public void elipsoid(int stacks, int slices, float fA, float fB, float fC,GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUgl2 glu = new GLUgl2();

		float tStep = (float)Math.PI / (float)slices;
		float sStep = (float)Math.PI / (float)stacks;

		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		for(float t = -(float)Math.PI/2; t <= ((float)Math.PI/2)+.0001; t += tStep){
			gl.glBegin(GL.GL_TRIANGLE_STRIP);
				for(float s = -(float)Math.PI; s <= (float)Math.PI+.0001; s += sStep){
					gl.glVertex3f(fA * (float)Math.cos(t) * (float)Math.cos(s), fB * (float)Math.cos(t) * (float)Math.sin(s), fC * (float)Math.sin(t));
					gl.glVertex3f(fA * (float)Math.cos(t+tStep) * (float)Math.cos(s), fB * (float)Math.cos(t+tStep) * (float)Math.sin(s), fC * (float)Math.sin(t+tStep));
				}
			gl.glEnd();
		}
	}
/*
	public void mytube(int slices, ){
		GL2 gl = drawable.getGL().getGL2();
		GLUgl2 glu = new GLUgl2();

		//関数引数はとりあえず保留にして、関数をここで定義することにする
		for (int i = 0; i < slices; i++) {
						
		}

	}
	*/
	public void oval(float rad,float ovalx,float ovalz,GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();

		float x_prv = 0;
		float z_prv = rad*ovalz;

		gl.glLineWidth(1.0f); 
		for (int r1 = 0; r1 < 36; r1++){  
			float r2 = r1 + 10;
			float r1_rad = r1 / 18 * (float)Math.PI; 
			float r2_rad = r2 / 18 * (float)Math.PI;

			float x_crt = rad*(float)Math.cos(r2_rad)*ovalx;
			float z_crt = rad*(float)Math.sin(r2_rad)*ovalz;
			gl.glBegin(GL2.GL_LINE_STRIP); 
				gl.glVertex3d( x_prv, 0.0f, z_prv );     
				gl.glVertex3d( x_crt, 0.0f, z_crt );
			gl.glEnd();

			x_prv = x_crt;
			z_prv = z_crt;
		}
	}
		public void lissajous(float rad,float lissx,float lissz,float alpha,float beta,GLAutoDrawable drawable){//リサージュ図形でのprvからcrtまでの何かを返す？
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();

		float x_prv = rad*(float)Math.cos(0*alpha)*lissx;
		float z_prv = rad*(float)Math.sin(0*beta)*lissz;

		gl.glLineWidth(1.0f); 
		for (int r1 = 0; r1 < 360; r1++){  
			float r2 = r1 + 1;
			float r1_rad = r1 / 180 * (float)Math.PI; 
			float r2_rad = r2 / 180 * (float)Math.PI;

			float x_crt = rad*(float)Math.cos(r2_rad*alpha)*lissx;
			float z_crt = rad*(float)Math.sin(r2_rad*beta)*lissz;
			gl.glBegin(GL2.GL_LINE_STRIP); 
				gl.glVertex3d( x_prv, 0.0f, z_prv );     
				gl.glVertex3d( x_crt, 0.0f, z_crt );
			gl.glEnd();

			x_prv = x_crt;
			z_prv = z_crt;
		}
	}
	
	public GLAutoDrawable getGLAutoDrawable() {
		return glAD;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
