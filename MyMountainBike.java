
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.gl2.GLUT;
import java.lang.Math;

public class MyMountainBike{

	float sunflower[]  = { 1.0f, 0.945098039f, 0.058823529f, 1.0f };
	float darkgreen[]  = { 0.156862745f, 0.639215686f, 0.043137255f, 1.0f };
	float olive[]      = { 0.388235294f, 0.48627451f, 0.207843137f, 1.0f };
	float silvergray[] = { 0.647058824f, 0.647058824f, 0.647058824f, 1.0f };
	float black[]      = { 0.149019608f, 0.149019608f, 0.149019608f, 1.0f };

	//基本計算
	float degree10 = 10.0f/180*(float)Math.PI;
	float size = 1.0f;
	float temp_length = 0.0f;
	float pipe_width = 0.15f;

	//パイプ
	float pipe1_length = size*3.0f;
	float pipe1_xtrans = pipe1_length*(float)Math.cos(degree10);
	float pipe1_ytrans = pipe1_length*(float)Math.sin(degree10);
	float pipe2_length = size*2.0f;
	float pipe2_xtrans = pipe2_length*(float)Math.sin(degree10);
	float pipe2_ytrans = -pipe2_length*(float)Math.cos(degree10);
	float pipe3_length = size*(float)Math.sqrt(13);
	float pipe3_xtrans = pipe1_xtrans + pipe2_xtrans;
	float pipe3_ytrans = pipe2_xtrans + pipe2_ytrans;
	float pipe4_length = size*(1.0f/(float)Math.sin(degree10))/2.0f;
	float pipe4_xtrans = 2*pipe2_xtrans - pipe4_length;
	float pipe4_ytrans = 2*pipe2_ytrans;
	//単純に正弦定理で辺の長さを求めているだけ。遅くなるようだったら適当に数字を入れとけばいいのでは。
	float pipe5_length = size*(float)Math.sqrt( (float)Math.pow(pipe2_length,2) + (float)Math.pow(pipe4_length,2) - 2.0f*pipe2_length*pipe4_length*(float)Math.cos(80.0f/180*Math.PI) );
	float pipe5_xtrans = (pipe4_xtrans - pipe5_length/2.0f)/2.0f - 0.55f;
	float pipe5_ytrans = pipe4_ytrans/2.0f;
	float pipe8_length = pipe1_length*0.2f;
	float pipe8_xtrans = pipe1_xtrans*2.0f;
	float pipe8_ytrans = pipe1_ytrans*2.0f;
	float pipe9_length = ( pipe1_length*(float)Math.sin(degree10)+Math.abs(pipe4_ytrans) ) / (float)Math.cos(degree10)* 0.5f;
	float pipe9_xtrans = pipe8_xtrans + pipe2_xtrans*1.1f;
	float pipe9_ytrans = pipe4_ytrans/2.0f + 0.3f;
	float pipejoint_length = pipe4_length*(float)Math.sin(degree10)*1.2f;//実質、piperear_length/2.0f*1.2fということ。
	float pipejoint_xtrans = pipe1_length*(float)Math.cos(degree10)*2 + 0.1f;
	float pipesaddle_length = pipe8_length;
	float pipesaddle_xtrans = -pipesaddle_length*(float)Math.sin(degree10);
	float pipesaddle_ytrans = pipesaddle_length*(float)Math.cos(degree10);
	float piperear_length = pipe4_length*(float)Math.sin(degree10)*1.25f;// Z軸方向の差を計算。
	float piperear_xtrans = 2*pipe2_xtrans - 2*pipe4_length;
	float pipefront_xtrans = pipe1_length*(float)Math.cos(degree10)*2 + pipe9_length*(float)Math.sin(degree10)*2;

	//タイヤとスポーク
	float tire_rad = pipe4_length*1.2f;
	float front_xcenter = pipefront_xtrans;
	float front_ycenter = pipe4_ytrans;
	float rear_xcenter = piperear_xtrans;
	float rear_ycenter = front_ycenter;
	float axle_rad = pipe_width*2.0f;
	float wheel_rad = tire_rad; // 後で内側にホイールをつけるかもしれないが。
	float spoke_div = 9.0f; // 見た目的に、deg10なら18本のスポークがベストだった。
	float degree_plus = (360.0f/spoke_div)/180*(float)Math.PI;


	public void draw(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();

		/*
		//原点に黒丸を作っておこう…
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		//ただの球：glutSolidSphere(半径，経線の分割数，緯線の分割数)
		glut.glutSolidTeapot(0.5f);
		*/
		
		// パイプ１本目 z軸周りに回転 = xy平面上での原点中心回転
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, darkgreen, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, darkgreen, 0);
		gl.glPushMatrix();
			gl.glTranslatef(pipe1_xtrans, pipe1_ytrans, 0);
			gl.glRotatef(-80.0f,0,0,1);
			drawer.cylinder(pipe_width*1.2f,pipe1_length,8,drawable);
		gl.glPopMatrix();
		
		// パイプ２本目
		gl.glPushMatrix();
		gl.glTranslatef(pipe2_xtrans, pipe2_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width*1.2f,pipe2_length,8,drawable);
		gl.glPopMatrix();

		// パイプ３本目
		// パイプ1と2の中点を移動座標とする。素直にベクトルの足し算的なイメージでよい。
		gl.glPushMatrix();
			gl.glTranslatef( pipe3_xtrans, 0.16f + pipe3_ytrans, 0 );
			gl.glRotatef( -(float)Math.atan(0.66666666f)/(float)Math.PI*180 - 12.0f, 0,0,1);
			//本当は10とか綺麗な数字におちつくはずなのだが、多分atanで誤差が出てるので、見た目綺麗な数字12-13で手を打つ。
			//0.1fも本来はいらないはずです。
			drawer.cylinder(pipe_width*1.1f,pipe3_length,8,drawable);
		gl.glPopMatrix();

		// 4本目 後輪パイプ ＋Z側 下の1本目
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(degree10) + 0.05f);//タイヤとの重なりを防ぐため
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,6,drawable);
		gl.glPopMatrix();

		// 5本目 後輪パイプ +Z側 上のパイプ
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(degree10));
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,6,drawable);
		gl.glPopMatrix();

		//6本目 後輪パイプ -Z側 下 基本は4本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(degree10) - 0.05f);//タイヤとの重なりをふせぐため。
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,6,drawable);
		gl.glPopMatrix();

		// 7本目 後輪パイプ -Z側 上 基本は5本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(degree10));
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,6,drawable);
		gl.glPopMatrix();

		// 8本目 前輪 カラーパイプ
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans, pipe8_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width*1.5f,pipe8_length,8,drawable);
		gl.glPopMatrix();		

		// 9本目 前輪  +Z軸側 
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, pipe4_length*(float)Math.sin(degree10));
			gl.glTranslatef(pipe9_xtrans, pipe9_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe9_length,6,drawable);
		gl.glPopMatrix();

		// 10本目 前輪  -Z軸側 
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*(float)Math.sin(degree10));
			gl.glTranslatef(pipe9_xtrans, pipe9_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe9_length,6,drawable);
		gl.glPopMatrix();

		// 前輪 カラーパイプ連結部
		gl.glPushMatrix();
			gl.glTranslatef(pipejoint_xtrans, pipe1_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);// ZY平面上で倒したいので、X軸回転ということになる。
			drawer.cylinder(pipe_width*2.0f,pipejoint_length,8,drawable);
		gl.glPopMatrix();


		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, silvergray, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silvergray, 0);

		// サドル下
		gl.glPushMatrix();
			gl.glTranslatef(pipesaddle_xtrans, pipesaddle_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipesaddle_length*0.8f,6,drawable);
		gl.glPopMatrix();

		// 後輪の車軸
		gl.glPushMatrix();
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);// ZY平面上で倒したいので、X軸回転ということになる。
			drawer.cylinder(pipe_width*2.0f,piperear_length*1.2f,8,drawable);
		gl.glPopMatrix();

		// 前輪の車軸
		gl.glPushMatrix();
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length,8,drawable);
		gl.glPopMatrix();

		// ペダルの車軸
		gl.glPushMatrix();
			gl.glTranslatef(pipe2_xtrans*2.0f, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width,pipejoint_length,8,drawable);
		gl.glPopMatrix();

		// ハンドル 中身
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*(float)Math.sin(degree10), pipe8_ytrans + pipe8_length*(float)Math.cos(degree10), 0);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width,piperear_length*5.0f,8,drawable);
		gl.glPopMatrix();

		// 後輪のホイール
		gl.glPushMatrix();
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.1f,tire_rad*0.9f,16,64);
		gl.glPopMatrix();

		// 前輪のホイール
		gl.glPushMatrix();
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.1f,tire_rad*0.9f,16,64);
		gl.glPopMatrix();	



		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);

		// ハンドル +Z側 ラバー
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*(float)Math.sin(degree10), pipe8_ytrans + pipe8_length*(float)Math.cos(degree10), piperear_length*4.0f);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length*2.0f,8,drawable);
		gl.glPopMatrix();

		// ハンドル -Z側 ラバー
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*(float)Math.sin(degree10), pipe8_ytrans + pipe8_length*(float)Math.cos(degree10), -piperear_length*4.0f);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length*2.0f,8,drawable);
		gl.glPopMatrix();		// 後輪
		//glutSolidTorus(内側の半径,外側の半径,断面の分割数,リングの分割数)
		gl.glPushMatrix();
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.30f,tire_rad,16,64);
		gl.glPopMatrix();

		// 前輪
		gl.glPushMatrix();
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			glut.glutSolidTorus(0.30f,tire_rad,16,64);
		gl.glPopMatrix();	

		//サドル
		//int uiStacks, int uiSlices, float fA, float fB, float fC,GLAutoDrawable drawable
		gl.glPushMatrix();
			gl.glTranslatef(0, pipesaddle_ytrans*2.5f,0);
			drawer.elipsoid(10,10,1.2f,0.4f,1,drawable);
		gl.glPopMatrix();

		// スポーク
		gl.glBegin(GL2.GL_LINES);
			gl.glLineWidth(1.0f);
			for(int i = 0; i < spoke_div; i++){ // 前輪右回り
				gl.glVertex3d(front_xcenter - axle_rad*(float)Math.sin(degree10+degree_plus*i), front_ycenter + axle_rad*(float)Math.cos(degree10+degree_plus*i), 0.0f);
				gl.glVertex3d(front_xcenter + wheel_rad*(float)Math.sin(degree10-degree_plus*i), front_ycenter + wheel_rad*(float)Math.cos(degree10-degree_plus*i), 0.0f);
			}
			for(int i = 0; i < spoke_div; i++){ // 前輪左回り
				gl.glVertex3d(front_xcenter - axle_rad*(float)Math.sin(-degree10+degree_plus*i), front_ycenter + axle_rad*(float)Math.cos(-degree10+degree_plus*i), 0.0f);
				gl.glVertex3d(front_xcenter + wheel_rad*(float)Math.sin(-degree10-degree_plus*i), front_ycenter + wheel_rad*(float)Math.cos(-degree10-degree_plus*i), 0.0f);
			}
			for(int i = 0; i < spoke_div; i++){ // 後ろ右回り
				gl.glVertex3d(rear_xcenter - axle_rad*(float)Math.sin(degree10+degree_plus*i), rear_ycenter + axle_rad*(float)Math.cos(degree10+degree_plus*i), 0.0f);
				gl.glVertex3d(rear_xcenter + wheel_rad*(float)Math.sin(degree10-degree_plus*i), rear_ycenter + wheel_rad*(float)Math.cos(degree10-degree_plus*i), 0.0f);
			}
			for(int i = 0; i < spoke_div; i++){ // 後ろ左回り
				gl.glVertex3d(rear_xcenter - axle_rad*(float)Math.sin(-degree10+degree_plus*i), rear_ycenter + axle_rad*(float)Math.cos(-degree10+degree_plus*i), 0.0f);
				gl.glVertex3d(rear_xcenter + wheel_rad*(float)Math.sin(-degree10-degree_plus*i), rear_ycenter + wheel_rad*(float)Math.cos(-degree10-degree_plus*i), 0.0f);
			}
			gl.glEnd();

	}
}