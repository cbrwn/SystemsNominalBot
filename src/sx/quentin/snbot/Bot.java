package sx.quentin.snbot;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Bot implements Runnable {

	private Robot robot;
	static final int[][] keyboard = { { KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P },
			{ KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L },
			{ KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M } };
	static final int kbdStartX = 288, kbdStartY = 460;
	private Point topLeft;

	public Bot() {
		Scanner _scan = new Scanner(System.in);
		System.out.print("Put mouse in top-left corner and hit enter");
		_scan.nextLine();
		topLeft = MouseInfo.getPointerInfo().getLocation();
		_scan.close();
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doPress(int kPress) {
		robot.keyPress(kPress);
		sleep(50);
		robot.keyRelease(kPress);
	}

	@Override
	public void run() {
		if (robot == null)
			return;
		System.out.println("Will start in 5 seconds, make sure you're in game!");
		sleep(5000);
		doPress(KeyEvent.VK_SPACE); // Restart
		while (true) // Game is endless until you stop it
			for (int y = 0; y < 3; y++) {
				int kStartX = kbdStartX + (y * 40), kStartY = kbdStartY + (y * 85);
				for (int x = 0; x < keyboard[y].length; x++) {
					Color c = robot.getPixelColor(topLeft.x + kStartX + (x * 90), topLeft.y + kStartY);
					if (c.getRed() > 220 && c.getGreen() < 200 && c.getBlue() < 50) {  // bad colour approximation but it works
						doPress(keyboard[y][x]);
						System.out.println(String.format("Found %s", KeyEvent.getKeyText(keyboard[y][x])));
					}
				}
			}
	}

	private void sleep(long millis) { // Better than exceptions being everywhere
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	public static void main(String[] args) {
		Bot bot = new Bot();
		Thread t = new Thread(bot);
		t.start();
	}

}
