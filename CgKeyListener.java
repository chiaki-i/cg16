/**
 * キーボード操作に関する反応を扱うクラス
 */

import java.awt.event.*;

public class CgKeyListener implements KeyListener {
	CgCanvas canvas;
	
	
	public CgKeyListener(CgCanvas c) {
		canvas = c;
	}
	
	
   	/**
	 * キーを押したときに呼び出されるメソッド
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		
		// "Q"を押したとき
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		
		// "R"を押したとき
		case KeyEvent.VK_R:
			MyScene.resetMovement();
			canvas.display();
			break;
		}
		
	}

	/**
	 * キーから手を離したときに呼び出されるメソッド
	 */
	public void keyReleased(KeyEvent e) {

	}

	/**
	 * キーをタイプしたときに呼び出されるメソッド
	 */
	public void keyTyped(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		
		// "Q"を押したとき
		case KeyEvent.VK_Q:
			System.exit(0);
		}
	}
}
