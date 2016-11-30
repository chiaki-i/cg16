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

		// "1"を押した時
		case KeyEvent.VK_1:
			if(MyMountainBike.flag == MyMountainBike.STRAIGHT){}//そのままだと何も起きない
			else{
				MyScene.resetMovement();
				MyMountainBike.setFlag(MyMountainBike.STRAIGHT);
				MyMountainBike.setDist_bike(0);
				MyMountainBike.setVelocity_bike(9);
				MyMountainBike.setVelocity_wheel(30);
				MyMountainBike.setVelocity_front(0);
				canvas.display();
			}
			break;

		// "2"を押した時
		case KeyEvent.VK_2:
			if(MyMountainBike.flag == MyMountainBike.CIRCLE){}//何も起きない
			else{
				MyScene.resetMovement();
				MyMountainBike.setFlag(MyMountainBike.CIRCLE);
				canvas.display();
			}
			break;

		// "3"を押した時
		case KeyEvent.VK_3:
			if(MyMountainBike.flag == MyMountainBike.LISSAJOUS){}//何も起きない
			else{
				MyScene.resetMovement();
				MyMountainBike.setFlag(MyMountainBike.LISSAJOUS);
				canvas.display();
			}
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
