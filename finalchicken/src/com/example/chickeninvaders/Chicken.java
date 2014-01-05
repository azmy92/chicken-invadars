package com.example.chickeninvaders;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Chicken {

	public int x;
	public int y;
	public Rect srcRect;
	public Rect dstRect;
	public Rect srcRectSmoke;
	public Rect dstRectSmoke;
	public int NumOfHits;
	public boolean dead;
	public int startX, startY;
	public int endX, endY;
	public boolean moving;
	public int dx, dy;
	public boolean GDistance;
	static final int speed = 5;
	public int directionX;
	public int directionY;
	public static final int up = -1, down = 1, left = -1, right = 1;
	Point curr;
	public Bonus weaponUpgrade;
	public Bonus ScoreBonus;
	boolean isExploded;
	int timerExplosion = 4;
	int explosionWidth;
	int framNum = 0;
	private int setFrameDirection;
	long timeStamp = 0;
	int period = 100;

	public Chicken(int x, int y, int startX, int startY, int endX, int endY,
			int numOfHits) {
		NumOfHits = numOfHits;
		this.x = x;
		this.y = y;
		dead = false;
		srcRect = new Rect(0, 0, Variables.chickenWidth, Variables.chickenHight);
		dstRect = new Rect(x, y, x + Variables.chickenWidth, y
				+ Variables.chickenHight);
		srcRectSmoke = new Rect();
		dstRectSmoke = new Rect(x, y, x + Variables.chickenWidth, y
				+ Variables.chickenHight);
		;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		dx = endX - startX;
		dy = endY - startY;
		GDistance = (Math.abs(dx) >= Math.abs(dy));
		directionX = right;
		directionY = up;
		setFrameDirection = 1;
		curr = new Point(startX, startY);
		explosionWidth = Variables.explosion.getWidth() / 6;
		isExploded = false;
	}

	/**
	 * for blue chicken
	 */
	public void setFrame1() {
		if (this.isExploded) {
			long currentTime = System.currentTimeMillis();
			long elapsed = (currentTime - timeStamp);
			if (elapsed >= period) {
				framNum++;
				timeStamp = System.currentTimeMillis();
			}
			srcRectSmoke
					.set((framNum - 1) * explosionWidth, 0, (explosionWidth)
							* framNum, Variables.explosion.getHeight());
			// this.dst.set((this.frame - 1) * eggBrokenWidth, 0, this.frame
			// * eggBrokenWidth, eggBrokenHeight);

			if (framNum > 6) {
				this.dead = true;
				this.isExploded = false;
			}
		} else {
			srcRect.left = (srcRect.left + (Variables.chickenWidth * setFrameDirection));
			if (srcRect.left == (Variables.chickenWidth * 6))
				setFrameDirection = -1;
			else if (srcRect.left == 0)
				setFrameDirection = 1;
			srcRect.right = srcRect.left + Variables.chickenWidth;
		}
	}

	/**
	 * for red chicken
	 */
	public void setFrame3() {
		if (this.isExploded) {
			long currentTime = System.currentTimeMillis();
			long elapsed = (currentTime - timeStamp);
			if (elapsed >= period) {
				framNum++;
				timeStamp = System.currentTimeMillis();
			}
			srcRectSmoke
					.set((framNum - 1) * explosionWidth, 0, (explosionWidth)
							* framNum, Variables.explosion.getHeight());
			// this.dst.set((this.frame - 1) * eggBrokenWidth, 0, this.frame
			// * eggBrokenWidth, eggBrokenHeight);

			if (framNum > 6) {
				this.dead = true;
				this.isExploded = false;
			}
		} else {
			srcRect.left = (srcRect.left + Variables.chickenWidth)
					% (Variables.chickenWidth * 3);
			srcRect.right += Variables.chickenWidth;
			if (srcRect.left == 0) {
				srcRect.right = Variables.chickenWidth;
				srcRect.top += Variables.chickenHight;
				if (srcRect.top == (Variables.chickenHight * 3)) {
					srcRect.top -= Variables.chickenHight;
					// srcRect.bottom = Variables.chickenHight;
				} else
					srcRect.bottom += Variables.chickenHight;
			}
		}
	}

	// public void introMoveLevel1() {
	// x += Variables.Direction;
	// if (x < 0) {
	// Variables.Direction *= -1;
	// x += Variables.Direction;
	//
	// } else if (x > (Variables.scWidth - Variables.chickenWidth)) {
	// Variables.Direction *= -1;
	// x += Variables.Direction;
	// }
	// dstRect.left = x;
	// dstRect.right = x + Variables.chickenWidth;
	//
	// }
	//
	// public void introMoveLevel2() {
	//
	// if (moving) {
	// if (GDistance) {
	// x += speed * directionX;
	// y = (int) ((startY) + (dy) * (x - (startX)) / (dx));
	// } else {
	// if (directionX == 2)
	// y += speed * 1;
	// else
	// y += speed * -1;
	//
	// x = (int) ((startX) + (dx) * (y - (startY)) / (dy));
	// }
	// if (directionX == 1) {
	// if (x >= endX && y <= endY) {
	// x = endX;
	// y = endY;
	// moving = false;
	// }
	// } else if (directionX == -1) {
	// if (x <= endX && y <= endY) {
	// x = endX;
	// y = endY;
	// moving = false;
	// }
	// } else {
	// if (y >= endY) {
	// x = endX;
	// y = endY;
	// moving = false;
	// }
	// }
	//
	// dstRect.left = x;
	// dstRect.right = x + Variables.chickenWidth;
	// dstRect.top = y;
	// dstRect.bottom = y + Variables.chickenHight;
	// }
	//
	// }
	//
	// public void introMoveLevel3() {
	//
	// if (moving) {
	// if (GDistance) {
	// x += speed * directionX;
	// y = (int) ((startY) + (dy) * (x - (startX)) / (dx));
	// } else {
	// y += speed * directionY;
	// x = (int) ((startX) + (dx) * (y - (startY)) / (dy));
	// }
	// if (directionY == up && y <= endY) {// in case of up direction
	// // check curr point
	// if (curr.equals(Levels.p1)) {
	// // let point2 be the start and point3 be the end and set
	// // directions
	// startX = Levels.p2.x;
	// startY = Levels.p2.y;
	// x = startX;
	// y = startY;
	// endX = Levels.p3.x;
	// endY = Levels.p3.y;
	// directionX = right;
	// directionY = down;
	// } else if (curr.equals(Levels.p3)) {
	// startX = Levels.p4.x;
	// startY = Levels.p4.y;
	// x = startX;
	// y = startY;
	// endX = Levels.p1.x;
	// endY = Levels.p1.y;
	// directionX = left;
	// directionY = down;
	// }
	// curr.x = startX;
	// curr.y = startY;
	// dx = endX - startX;
	// dy = endY - startY;
	// GDistance = (Math.abs(dx) >= Math.abs(dy));
	// } else if (directionY == down && y >= endY) {
	// // check curr point
	// if (curr.equals(Levels.p2)) {
	// startX = Levels.p3.x;
	// startY = Levels.p3.y;
	// x = startX;
	// y = startY;
	// endX = Levels.p4.x;
	// endY = Levels.p4.y;
	// directionX = right;
	// directionY = up;
	//
	// } else if (curr.equals(Levels.p4)) {
	// startX = Levels.p1.x;
	// startY = Levels.p1.y;
	// x = startX;
	// y = startY;
	// endX = Levels.p2.x;
	// endY = Levels.p2.y;
	// directionX = right;
	// directionY = up;
	// }
	// curr.x = startX;
	// curr.y = startY;
	// dx = endX - startX;
	// dy = endY - startY;
	// GDistance = (Math.abs(dx) >= Math.abs(dy));
	// }
	//
	// dstRect.left = x;
	// dstRect.right = x + Variables.chickenWidth;
	// dstRect.top = y;
	// dstRect.bottom = y + Variables.chickenHight;
	// }
	// }

}
