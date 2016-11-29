
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

	//基本計算:同じ計算はなるべくさせない
	float degree10 = 10.0f/180*(float)Math.PI;
	float sin10		 = (float)Math.sin(degree10);
	float cos10		 = (float)Math.cos(degree10);
	float size     = 1.0f;
	float temp_length = 0.0f;
	float pipe_width = 0.15f;

	//パイプ
	float pipe1_length = size*3.0f;
	float pipe1_xtrans = pipe1_length*cos10;
	float pipe1_ytrans = pipe1_length*sin10;
	float pipe2_length = size*2.0f;
	float pipe2_xtrans = pipe2_length*sin10;
	float pipe2_ytrans = -pipe2_length*cos10;
	float pipe3_length = size*(float)Math.sqrt(13);
	float pipe3_xtrans = pipe1_xtrans + pipe2_xtrans;
	float pipe3_ytrans = pipe2_xtrans + pipe2_ytrans;
	float pipe4_length = size*(1.0f/sin10)/2.0f;
	float pipe4_xtrans = 2*pipe2_xtrans - pipe4_length;
	float pipe4_ytrans = 2*pipe2_ytrans;
	//単純に正弦定理で辺の長さを求めているだけ。遅くなるようだったら適当に数字を入れとけばいいのでは。
	float pipe5_length = size*(float)Math.sqrt( (float)Math.pow(pipe2_length,2) + (float)Math.pow(pipe4_length,2) - 2.0f*pipe2_length*pipe4_length*(float)Math.cos(80.0f/180*Math.PI) );
	float pipe5_xtrans = (pipe4_xtrans - pipe5_length/2.0f)/2.0f-0.55f;
	float pipe5_ytrans = pipe4_ytrans/2.0f;
	float pipe8_length = pipe1_length*0.2f;
	float pipe8_xtrans = pipe1_xtrans*2.0f;
	float pipe8_ytrans = pipe1_ytrans*2.0f;
	float pipe9_length = ( pipe1_length*sin10+Math.abs(pipe4_ytrans) ) / cos10* 0.5f;
	float pipe9_xtrans = pipe8_xtrans + pipe2_xtrans*1.1f;
	float pipe9_ytrans = pipe4_ytrans/2.0f + 0.3f;
	float pipejoint_length  = pipe4_length*sin10*1.2f;//実質、piperear_length/2.0f*1.2fということ。
	float pipejoint_xtrans  = pipe1_length*cos10*2 + 0.1f;
	float pipesaddle_length = pipe8_length;
	float pipesaddle_xtrans = -pipesaddle_length*sin10;
	float pipesaddle_ytrans = pipesaddle_length*cos10;
	float piperear_length   = pipe4_length*sin10*1.25f;// Z軸方向の差を計算。
	float piperear_xtrans   = 2*pipe2_xtrans - 2*pipe4_length;
	float pipefront_xtrans  = pipe1_length*cos10*2 + pipe9_length*sin10*2;

	//タイヤとスポーク
	float tire_rad      = pipe4_length*1.2f;
	float front_xcenter = pipefront_xtrans;
	float front_ycenter = pipe4_ytrans;
	float rear_xcenter  = piperear_xtrans;
	float rear_ycenter  = front_ycenter;
	float axle_rad      = pipe_width*2.0f;
	float wheel_rad     = tire_rad; // 後で内側にホイールをつけるかもしれないが。
	float spoke_div     = 9.0f; // 見た目的に、deg10なら18本のスポークがベストだった。
	float degree_plus   = (360.0f/spoke_div)/180*(float)Math.PI;

	//アニメーション
	static float x_crt, x_bike;
	static float z_crt, z_bike;
	static int rad = 20, lissx = 1, lissz = 1, alpha = 3, beta = 4;
	static int r1 = 0;
	static int r2;
	static float r1_rad, r2_rad;
	static float x_prv = rad*(float)Math.cos(0*alpha)*lissx;
	static float z_prv = rad*(float)Math.sin(0*beta)*lissz;

	static int r_bike = 0;// 車の回転運動しているときの回転角*10(整数値で取り扱う方が計算が楽)
	static int t_bike = 0;
	static int r_wheel = 3600;
	static int r_front = 0;
	static int velocity_bike_rotate;// 車の角速度（に相当する値）
	static float velocity_bike_straight;// 直線での速度	
	static int velocity_wheel;// 車輪の角速度(相当値)
	static int velocity_front;
	static float distance_bike;// 自転車の、原点(0,0,0)からの変位

	static int flag;//アニメーションのモード
	static int STRAIGHT  = 1;
	static int CIRCLE    = 2;
	static int LISSAJOUS = 3;

	static void setDist_bike(float t) {//自転車の回転中心からの変位を設定
		distance_bike = t;
	}

	static void setVelocity_bike(int v) {//自転車全体の速度は基本的にはタイヤの回転角に依存する。
		velocity_bike_rotate = v;
		velocity_bike_straight = v;
	}

	static void setVelocity_wheel(int v){
		velocity_wheel = v;
	}
	
	static void setVelocity_front(int v){
		velocity_front = v;
	}
	
	static void setFlag(int f){
		flag = f;
	}

	static void calMovement_bike(int flag, int dist) {
		while(flag == STRAIGHT){
			r_bike += velocity_bike_rotate;
			if(r_bike >= 3600){
				r_bike = 0;
			}
			else if(r_bike >= 1800 && r_bike < 3600){
				t_bike -= velocity_bike_straight;
				if(t_bike <= -velocity_bike_straight*(r_bike/velocity_bike_rotate/2)) {
					t_bike = (int)Math.ceil(-velocity_bike_straight*(r_bike/velocity_bike_rotate/2));
				}
			}
			else if(r_bike > 0 && r_bike < 1800){
				t_bike += velocity_bike_straight;
				if(t_bike >= velocity_bike_straight*(r_bike/velocity_bike_rotate/2)) {
					t_bike = (int)Math.floor(velocity_bike_straight*(r_bike/velocity_bike_rotate/2));
				}
			}
			flag = 0;
		}
		while(flag == CIRCLE){
			r_bike += velocity_bike_rotate;
			if(r_bike >= 3600) {
				r_bike = 0;
			}
			flag = 0;
		}
		while(flag == LISSAJOUS) {
			r_bike += velocity_bike_rotate;
			r2_rad = (float)Math.toRadians(r_bike/10);
			x_crt = dist*0.015f*(float)Math.cos(r2_rad*alpha)*lissx;
			z_crt = dist*0.015f*(float)Math.sin(r2_rad*beta)*lissz;
			if(r_bike >= 3600) {
				r_bike = 0;
			}
			flag = 0;
		}
	}

	static void calMovement_wheel(){
		r_wheel -= velocity_bike_rotate;
	  if (r_wheel <= 0) {
	  	r_wheel = 3600;
	  }
	}

	static void calMovement_front(int flag){
		if (flag == CIRCLE){
			r_front += velocity_bike_rotate;
			if (r_front >= 450) {
				r_front = 450;
			}
		}
	}

	//自転車の、全ての動きをリセットする
	static void resetMovement() {
		r_bike = 0;
		r_wheel = 3600;
		r_front = 0;
		t_bike = 0;
	}
	

	public void draw_tire(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();

		gl.glPushMatrix();
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
			gl.glColor3f(black[0], black[1], black[2]);
			glut.glutSolidTorus(0.30f,tire_rad,16,64);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, silvergray, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silvergray, 0);
			gl.glColor3f(silvergray[0], silvergray[1], silvergray[2]);
			glut.glutSolidTorus(0.1f,tire_rad*0.9f,16,64);
		gl.glPopMatrix();	
		gl.glPushMatrix();
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length,8,drawable);
		gl.glPopMatrix();

		// スポーク
		gl.glBegin(GL2.GL_LINES);
			gl.glLineWidth(1.0f);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
			gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
			gl.glColor3f(black[0], black[1], black[2]);
			for(int i = 0; i < spoke_div; i++){ // 前輪右回り
				gl.glVertex3d( - axle_rad*(float)Math.sin(degree10+degree_plus*i),  + axle_rad*(float)Math.cos(degree10+degree_plus*i), 0.0f);
				gl.glVertex3d( + wheel_rad*(float)Math.sin(degree10-degree_plus*i),  + wheel_rad*(float)Math.cos(degree10-degree_plus*i), 0.0f);
			}
			for(int i = 0; i < spoke_div; i++){ // 前輪左回り
				gl.glVertex3d( - axle_rad*(float)Math.sin(-degree10+degree_plus*i),  + axle_rad*(float)Math.cos(-degree10+degree_plus*i), 0.0f);
				gl.glVertex3d( + wheel_rad*(float)Math.sin(-degree10-degree_plus*i),  + wheel_rad*(float)Math.cos(-degree10-degree_plus*i), 0.0f);
			}
		gl.glEnd();
	}

	public void draw_frontbody(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, darkgreen, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, darkgreen, 0);
		gl.glColor3f(darkgreen[0], darkgreen[1], darkgreen[2]);

		gl.glPushMatrix();
			gl.glTranslatef(pipefront_xtrans, pipe4_ytrans, 0);
			gl.glRotatef((r_wheel * 0.1f), 0, 0, 1);
			draw_tire(drawable);//前輪	
			calMovement_wheel();
		gl.glPopMatrix();

		// 8本目 前輪 カラーパイプ
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans, pipe8_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width*1.5f,pipe8_length,8,drawable);
		gl.glPopMatrix();		

		// 9本目 前輪  +Z軸側 
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, pipe4_length*sin10);
			gl.glTranslatef(pipe9_xtrans, pipe9_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipe9_length,6,drawable);
		gl.glPopMatrix();

		// 10本目 前輪  -Z軸側 
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*sin10);
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
		gl.glColor3f(silvergray[0], silvergray[1], silvergray[2]);

		// ハンドル 中身
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*sin10, pipe8_ytrans + pipe8_length*cos10, 0);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width,piperear_length*5.0f,8,drawable);
		gl.glPopMatrix();

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		gl.glColor3f(black[0], black[1], black[2]);
		// ハンドル +Z側 ラバー
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*sin10, pipe8_ytrans + pipe8_length*cos10, piperear_length*4.0f);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length*2.0f,8,drawable);
		gl.glPopMatrix();

		// ハンドル -Z側 ラバー
		gl.glPushMatrix();
			gl.glTranslatef(pipe8_xtrans - pipe8_length*sin10, pipe8_ytrans + pipe8_length*cos10, -piperear_length*4.0f);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width*2.0f,piperear_length*2.0f,8,drawable);
		gl.glPopMatrix();			
	} 

	public void draw_pedal(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, silvergray, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silvergray, 0);
		gl.glColor3f(silvergray[0], silvergray[1], silvergray[2]);

		// ペダルの車軸
		gl.glPushMatrix();
			gl.glTranslatef(pipe2_xtrans*2.0f, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 1,0,0);
			drawer.cylinder(pipe_width,pipejoint_length,8,drawable);
		gl.glPopMatrix();

		//ペダルのクランク +Z側
		float pipecrank_length = pipe2_length*0.2f;
		float degree_r_wheel = r_wheel*(float)Math.PI;
		float pipecrank_xtrans = pipe2_xtrans*2.0f + pipecrank_length*(float)Math.sin(degree_r_wheel);
		float pipecrank_ytrans = pipe4_ytrans + pipecrank_length*(float)Math.cos(degree_r_wheel);
		gl.glPushMatrix();
			gl.glTranslatef(pipecrank_xtrans, pipecrank_ytrans, pipejoint_length*0.6f);
			gl.glRotatef((r_wheel * 0.1f), 0, 0, 1);
			drawer.cylinder(pipe_width*0.8f,pipecrank_length,6,drawable);
		gl.glPopMatrix();
	}

	public void draw_body(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();

		gl.glPushMatrix();
			gl.glTranslatef(pipejoint_xtrans, pipe1_ytrans, 0);		
			gl.glRotatef(10.0f,0,0,1);
			gl.glRotatef((r_front * 0.1f), 0, 1, 0);
			gl.glRotatef(-10.0f,0,0,1);
			gl.glTranslatef(-pipejoint_xtrans, -pipe1_ytrans, 0);
			draw_frontbody(drawable);//前半分も描画。
			calMovement_front(flag);
		gl.glPopMatrix();

		//draw_pedal(drawable);

		gl.glPushMatrix();
			gl.glTranslatef(piperear_xtrans, pipe4_ytrans, 0);
			gl.glRotatef((r_wheel * 0.1f), 0, 0, 1);
			draw_tire(drawable);//後輪。
			calMovement_wheel();
		gl.glPopMatrix();

		// パイプ１本目 z軸周りに回転 = xy平面上での原点中心回転
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, darkgreen, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, darkgreen, 0);
		gl.glColor3f(darkgreen[0], darkgreen[1], darkgreen[2]);
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
			gl.glTranslatef(0, 0, pipe4_length*sin10 + 0.05f);//タイヤとの重なりを防ぐため
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,6,drawable);
		gl.glPopMatrix();

		// 5本目 後輪パイプ +Z側 上のパイプ
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, pipe4_length*sin10 - 0.15f);
			gl.glRotatef(10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,6,drawable);
		gl.glPopMatrix();

		//6本目 後輪パイプ -Z側 下 基本は4本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*sin10 - 0.05f);//タイヤとの重なりをふせぐため。
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe4_xtrans, pipe4_ytrans, 0);
			gl.glRotatef(90.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe4_length,6,drawable);
		gl.glPopMatrix();

		// 7本目 後輪パイプ -Z側 上 基本は5本目と同じ。
		gl.glPushMatrix();
			gl.glTranslatef(0, 0, -pipe4_length*sin10 + 0.15f);
			gl.glRotatef(-10.0f, 1,1,0);
			gl.glTranslatef(pipe5_xtrans, pipe5_ytrans, 0);
			gl.glRotatef(-53.0f, 0,0,1);
			drawer.cylinder(pipe_width,pipe5_length,6,drawable);
		gl.glPopMatrix();

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, silvergray, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, silvergray, 0);
		gl.glColor3f(silvergray[0], silvergray[1], silvergray[2]);

		// サドル下
		gl.glPushMatrix();
			gl.glTranslatef(pipesaddle_xtrans, pipesaddle_ytrans, 0);
			gl.glRotatef(10.0f,0,0,1);
			drawer.cylinder(pipe_width,pipesaddle_length*0.8f,6,drawable);
		gl.glPopMatrix();

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		gl.glColor3f(black[0], black[1], black[2]);

		//サドル
		//int uiStacks, int uiSlices, float fA, float fB, float fC,GLAutoDrawable drawable
		gl.glPushMatrix();
			gl.glTranslatef(0, pipesaddle_ytrans*2.5f,0);
			drawer.elipsoid(10,10,1.2f,0.4f,1,drawable);
		gl.glPopMatrix();
	}

	public void draw(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();

			if(flag == STRAIGHT){
				gl.glTranslatef((t_bike*0.015f),0,0);
			}
			else if(flag == CIRCLE){
				gl.glRotatef((r_bike * 0.1f), 0, 1, 0);
				gl.glTranslatef(0,0,distance_bike);
			}
			else if (flag == LISSAJOUS){
				//gl.glRotatef((r_bike * 0.1f), 0, 1, 0);
				//gl.glTranslatef(t_bike*0.01f, 0, 0);
				//gl.glTranslatef( distance_bike*(float)Math.cos(r_bike/100*(float)Math.PI*alpha)*lissx, 0, 0 );
				gl.glTranslatef( x_crt, 0, z_crt );
			}
			calMovement_bike(flag,1000);

		gl.glPushMatrix();
			gl.glTranslatef(0, (-front_ycenter)+tire_rad+0.15f, 0);
			draw_body(drawable);	
		gl.glPopMatrix();

	}
}