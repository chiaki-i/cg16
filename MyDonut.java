
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.gl2.GLUT;
import java.lang.Math;

public class MyDonut{

	float sunflower[]  = { 1.0f, 0.945098039f, 0.058823529f, 1.0f };
	float darkgreen[]  = { 0.156862745f, 0.639215686f, 0.043137255f, 1.0f };
	float olive[]      = { 0.388235294f, 0.48627451f, 0.207843137f, 1.0f };
	float silvergray[] = { 0.647058824f, 0.647058824f, 0.647058824f, 1.0f };
	float black[]      = { 0.149019608f, 0.149019608f, 0.149019608f, 1.0f };

	public void draw(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();
		float size = 1.0f;
		float temp_length = 0.0f;
		float pipe_width = 0.15f;

		//原点に黒丸を作っておこう…
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		//ただの球：glutSolidSphere(半径，経線の分割数，緯線の分割数)
		glut.glutSolidTeapot(0.5f);

		/*
		// ただの目印的な何か。
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, sunflower, 1);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, sunflower, 0);
		gl.glPushMatrix();
			gl.glTranslatef(0,0,0);
			drawer.cylinder(0.2f,3.0f,4,drawable);
		gl.glPopMatrix();
		*/

		// パイプ１本目 z軸周りに回転 = xy平面上での原点中心回転
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, darkgreen, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, darkgreen, 0);
		gl.glPushMatrix();
			float pipe1_length = size*3.0f;
			float pipe1_xtrans = pipe1_length*(float)Math.cos(10.0f/180*Math.PI);
			float pipe1_ytrans = pipe1_length*(float)Math.sin(10.0f/180*Math.PI);
			gl.glTranslatef(pipe1_xtrans, pipe1_ytrans, 0);
			gl.glRotatef(-80.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe1_length,4,drawable);
		gl.glPopMatrix();
		
		// パイプ２本目
		gl.glPushMatrix();
			float pipe2_length = size*2.0f;
			float pipe2_xtrans = pipe2_length*(float)Math.sin(10.0f/180*Math.PI);
			float pipe2_ytrans = -pipe2_length*(float)Math.cos(10.0f/180*Math.PI);
			gl.glTranslatef(pipe2_xtrans, pipe2_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe2_length,4,drawable);
		gl.glPopMatrix();

		// パイプ３本目
		// パイプ1と2の中点を移動座標とする。素直にベクトルの足し算的なイメージでよい。
		gl.glPushMatrix();
			float pipe3_length = size*(float)Math.sqrt(13);
			float pipe3_xtrans = pipe1_xtrans + pipe2_xtrans;
			float pipe3_ytrans = pipe2_xtrans + pipe2_ytrans;
			gl.glTranslatef( pipe3_xtrans, 0.1f + pipe3_ytrans, 0 );
			gl.glRotatef( -(float)Math.atan(0.66666666f)/(float)Math.PI*180 - 12.0f, 0,0,1);
			//本当は10とか綺麗な数字におちつくはずなのだが、多分atanで誤差が出てるので、見た目綺麗な数字12-13で手を打つ。
			//0.1fも本来はいらないはずです。
			drawer.cylinder(pipe_width,pipe3_length,4,drawable);
		gl.glPopMatrix();

		// 4本目 後輪パイプ ＋Z側 下の1本目
		gl.glPushMatrix();
			float pipe4_length = size*(1.0f/(float)Math.sin(10.0f/180*Math.PI))/2.0f;
			float pipe4_xtrans = 2*pipe2_xtrans - pipe4_length;
			float pipe4_ytrans = 2*pipe2_ytrans;
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,4,drawable);
		gl.glPopMatrix();

		// 5本目 後輪パイプ +Z側 上のパイプ
		gl.glPushMatrix();
			//単純に正弦定理で辺の長さを求めているだけ。遅くなるようだったら適当に数字を入れとけばいいのでは。
			float pipe5_length = size*(float)Math.sqrt( (float)Math.pow(pipe2_length,2) + (float)Math.pow(pipe4_length,2) - 2.0f*pipe2_length*pipe4_length*(float)Math.cos(80.0f/180*Math.PI) );
			float pipe5_xtrans = (pipe4_xtrans - pipe5_length/2.0f)/2.0f - 0.6f;
			float pipe5_ytrans = pipe4_ytrans/2.0f;
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,4,drawable);
		gl.glPopMatrix();

		//6本目 後輪パイプ -Z側 下 基本は4本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,4,drawable);
		gl.glPopMatrix();

		// 7本目 後輪パイプ -Z側 上 基本は5本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,4,drawable);
		gl.glPopMatrix();

		// 8本目 前輪 カラーパイプ
		gl.glPushMatrix();
			float pipe8_length = pipe1_length*0.15f;
			float pipe8_xtrans = pipe1_xtrans*2.0f;
			float pipe8_ytrans = pipe1_ytrans*2.0f;
			gl.glTranslatef(pipe8_xtrans, pipe8_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width*1.5f,pipe8_length,4,drawable);
		gl.glPopMatrix();		

		// 9本目 前輪  +Z軸側 
		gl.glPushMatrix();
			float pipe9_length = ( pipe1_length*(float)Math.sin(10.0f/180*Math.PI)+Math.abs(pipe4_ytrans) ) / (float)Math.cos(10.0f/180*Math.PI)* 0.5f;
			float pipe9_xtrans = pipe8_xtrans + pipe2_xtrans*1.1f;
			float pipe9_ytrans = pipe4_ytrans/2.0f + 0.3f;
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glTranslatef(pipe9_xtrans, pipe9_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe9_length,4,drawable);
		gl.glPopMatrix();

		// 10本目 前輪  -Z軸側 
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(10.0f/180*Math.PI));
			gl.glTranslatef(pipe9_xtrans, pipe9_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe9_length,4,drawable);
		gl.glPopMatrix();


		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);//便宜上黒
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);

		// 後輪の車軸
		gl.glPushMatrix();
			float piperear_length = pipe4_length*(float)Math.sin(10.0f/180*Math.PI)*2;// Z軸方向の差を計算。
			float piperear_xtrans = 2*pipe2_xtrans - 2*pipe4_length;
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);// ZY平面上で倒したいので、X軸回転ということになる。
			drawer.cylinder(pipe_width*2.0f,piperear_length,8,drawable);
		gl.glPopMatrix();

		// 前輪の車軸
		gl.glPushMatrix();
			float pipefront_xtrans = pipe1_length*(float)Math.cos(10.0f/180*Math.PI)*2 + pipe9_length*(float)Math.sin(10.0f/180*Math.PI)*2;
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);// ZY平面上で倒したいので、X軸回転ということになる。
			drawer.cylinder(pipe_width*2.0f,piperear_length,8,drawable);
		gl.glPopMatrix();

		// 前輪 カラーパイプ連結部
		gl.glPushMatrix();
			float pipejoint_length = piperear_length/2.0f;
			float pipejoint_xtrans = pipe1_length*(float)Math.cos(10.0f/180*Math.PI)*2 + 0.1f;
			gl.glTranslatef(pipejoint_xtrans, pipe1_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);// ZY平面上で倒したいので、X軸回転ということになる。
			drawer.cylinder(pipe_width*2.0f,pipejoint_length,8,drawable);
		gl.glPopMatrix();

		/****************
		 　　タイヤ
		****************/

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		// 後輪
		//glutSolidTorus(内側の半径,外側の半径,断面の分割数,リングの分割数)
		gl.glPushMatrix();
			float tire_rad = pipe4_length*1.2f;
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.30f,tire_rad,16,64);
		gl.glPopMatrix();

		// 前輪
		gl.glPushMatrix();
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.30f,tire_rad,16,64);
		gl.glPopMatrix();		

	}
}