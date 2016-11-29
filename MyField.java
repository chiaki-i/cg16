import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.GL;
import javax.media.opengl.GLEventListener;

import java.lang.Math;

public class MyField{

	float field_max_x = 30.0f;
	float field_max_z = 30.0f;

	float black[]      = { 0.149019608f, 0.149019608f, 0.149019608f };
	float color[]       = { 0.2f, 0.2f, 0.2f };

	float vertex[][] = {
		{  field_max_x, 0.0f,  field_max_z },
		{  field_max_x, 0.0f, -field_max_z },
		{ -field_max_x, 0.0f,  field_max_z },
		{ -field_max_x, 0.0f, -field_max_z }
	};

	int face[][] = {
		{ 0, 3, 2, 1 }
	};

	public void draw(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		CgDrawer drawer = new CgDrawer();
		
		// 地面の拡散反射係数・鏡面反射係数を設定する
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, color, 0);

		gl.glColor3f(color[0], color[1], color[2]);
		gl.glNormal3d(0.0, 1.0, 0.0);//法線ベクトル。Y軸正方向が上となる。
		gl.glBegin(GL2.GL_POLYGON);
				gl.glVertex3d( -field_max_x, 0.0f,  field_max_z );
				gl.glVertex3d(  field_max_x, 0.0f,  field_max_z );
				gl.glVertex3d(  field_max_x, 0.0f, -field_max_z );
				gl.glVertex3d( -field_max_x, 0.0f, -field_max_z );
		gl.glEnd();

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, black, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, black, 0);
		gl.glPushMatrix();	
			gl.glTranslatef(0,0.01f,0);
			//drawer.oval(20.0f,1,0.5f,drawable);
			drawer.lissajous(20.0f,1,1,3,4,drawable);
		gl.glPopMatrix();

		
	}
}