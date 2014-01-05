package com.example.chickeninvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import com.example.finalchicken.R;

public class ship {
	SurfaceView game;
	float xSpeed, ySpeed;
	int space_colums = 3;
	int space_rows = 1;
	int width;
	int height;
	Bitmap spaceShip;
	int counter = 0;
	int direction = 1;
	Rect dst;
	Rect src;
	public int life;
	public boolean dead;
	public boolean destroying;
	public int score;
	protected Bitmap explosion;
	private byte helpSetFrame;
	private short counterExplosion;

	public ship(SurfaceView game) {
		this.game = game;
		spaceShip = BitmapFactory.decodeResource(game.getResources(),
				R.drawable.ship);
		explosion = BitmapFactory.decodeResource(game.getResources(),
				R.drawable.chicken_blast);

		width = spaceShip.getWidth() / space_colums;
		height = spaceShip.getHeight() / space_rows;
		xSpeed = game.getWidth() / 2;
		ySpeed = game.getHeight() - spaceShip.getHeight();
		src = new Rect(0, 0, 0, 0);
		dst = new Rect((int) xSpeed, (int) ySpeed, (int) xSpeed + width,
				(int) ySpeed + height);
		dead = false;
		destroying = false;
		life = 3;
		score = 0;
		helpSetFrame = 0;
		counterExplosion = 0;
	}

	protected void onDraw(Canvas canvas) {
		int srcX = direction * width;
		int srcY = 1;
		// Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		// dst = new Rect((int) xSpeed, (int) ySpeed, (int) xSpeed + width,
		// (int) ySpeed+ height);
		src.set(srcX, srcY, srcX + width, srcY + height);
		dst.set((int) xSpeed, (int) ySpeed, (int) xSpeed + width, (int) ySpeed
				+ height);
		canvas.drawBitmap(spaceShip, src, dst, null);
	}

	protected boolean explosionSetFrame() {
		// so that each frame hold a while on the screen
		dst.right = dst.left + (explosion.getWidth() / 4);
		dst.bottom = dst.top + (explosion.getHeight());
		if (counterExplosion % 4 == 0) {
			if (helpSetFrame == 0) {
				src.left = 0;
				src.set(0, 0, explosion.getWidth() / 4, explosion.getHeight());
				helpSetFrame = 1;
			} else {
				src.left = src.right;
				if (src.left >= explosion.getWidth()) {
					helpSetFrame = 0;
					counterExplosion++;
					return false;
				}
				src.right += explosion.getWidth() / 4;
			}
		}
		counterExplosion++;
		return true;
	}

	// when life gone resetart ship speed and positions
	public void reset() {
	//	if (game instanceof GamePanel) {
			xSpeed = game.getWidth() / 2;
			ySpeed = game.getHeight() - spaceShip.getHeight();
//		} else {
//			
//		}
		src.set(0, 0, 0, 0);
		dst.set((int) xSpeed, (int) ySpeed, (int) xSpeed + width, (int) ySpeed
				+ height);
		dead = false;
		destroying = false;
		counterExplosion = 0;
		helpSetFrame = 0;
	}


}
