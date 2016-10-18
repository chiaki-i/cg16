/**
 * 描画領域確保のためのクラス
 */

import javax.swing.*;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;


 
public class CgCanvas extends JPanel {
	static GLAutoDrawable drawable = null;
	GLCanvas glc; 
	CgDrawer drawer;
	int width, height;


	
	public CgCanvas(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		setSize(width, height);
		glc = new GLCanvas();
		drawer = new CgDrawer();
		glc.addGLEventListener(drawer);
	}
	
	
	public GLCanvas getGLCanvas() {
		return glc;
	}
	
	public void display() {
		if (drawer == null) return;
		GLAutoDrawable  glAD = drawer.getGLAutoDrawable();
		if (glAD == null) return;
		glAD.display();
	}
	
}
