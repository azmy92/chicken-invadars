package com.example.chickeninvaders;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {

	// //////////////////////////////////////////////////
	int sleepTime;
	int numberOfFramesSkipped;
	int maxFrameSkips;
	long beginTime;
	long endTime;
	long lastTime;
	int differenceTime;
	int framePeriod;
	Canvas frameBuffer;
	int frameCount;
	int fps=50;
	// ////////////////////////////////////////////////

	public boolean loop;
	private SurfaceHolder holder;
	private GamePanel panel;

	public GameLoop(SurfaceHolder holder, GamePanel panel) {
		loop = true;
		this.holder = holder;
		this.panel = panel;
		this.framePeriod = 1000 / fps;
		lastTime = System.currentTimeMillis();
		this.maxFrameSkips=1;
		this.numberOfFramesSkipped=0;
	}

	@SuppressWarnings("WrongCall")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas c = null;
		// initialization must be her
		// game loop
		while (loop == true) {
			if (holder.getSurface().isValid()) {

				beginTime = System.currentTimeMillis();

				// call your update method here
				// render here
				c = holder.lockCanvas();
				panel.update();
				panel.onDraw(c);
				holder.unlockCanvasAndPost(c);

				// Frame Per Second Count
				frameCount++;

				if (lastTime + 1000 < System.currentTimeMillis()) {
					lastTime = System.currentTimeMillis();
					frameCount = 0;
				}

				endTime = System.currentTimeMillis();
				
				differenceTime = (int) (endTime - beginTime);
				sleepTime = (int) (framePeriod - differenceTime);

				if (sleepTime > 0) {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException exception) {
						exception.printStackTrace();
					}
				} else {
					//some frames are skipped don nothing
				}

			}

		}
	}

}
