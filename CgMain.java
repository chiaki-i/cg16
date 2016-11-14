/**
 * 本演習のメインメソッドを含むクラス
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.Animator;

public class CgMain extends JApplet {
	static int width = 800;
	static int height = 650;
	static Animator animator;

	/**
	 * ウィンドウ起動時に一度だけ呼び出されるメソッド
	 */
	public void init() {
		// ウィンドウのサイズを設定する
		setSize(new Dimension(width, height));
		
		// 描画領域を設定する
		CgCanvas canvas = new CgCanvas(width, height);
		canvas.requestFocus();
		GLCanvas glc = canvas.getGLCanvas();
		animator = new Animator(glc);

		// ウィンドウのレイアウトを決定する
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(glc, BorderLayout.CENTER);
		
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(panel, BorderLayout.CENTER);
		
		// キー操作への反応を設定する
		CgKeyListener mkl = new CgKeyListener(canvas);
		canvas.addKeyListener(mkl);
		glc.addKeyListener(mkl);
		
		// マウス操作への反応を設定する
		CgMouseListener mml = new CgMouseListener(canvas, animator);
		canvas.addMouseListener(mml);
		glc.addMouseListener(mml);
		
		// 描画するためのシーンを初期化する
		MyScene.init();
	}

	/**
	 * ウィンドウ操作の開始時に呼び出される
	 */
	public void start() {
	}

	/**
	 * ウィンドウ操作の停止時に呼び出される
	 */
	public void stop() {
	}


	/**
	 * メインメソッド
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("um...");
		frame.setSize(width, height);
		CgMain cgmain = new CgMain(); 

		cgmain.init();
		frame.getContentPane().add(cgmain);
		frame.setVisible(true);

		cgmain.start();
		
	    // ウィンドウを閉じたときの処理を定義する
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                    	animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
	}

}
