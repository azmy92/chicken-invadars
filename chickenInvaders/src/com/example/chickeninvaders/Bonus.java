package com.example.chickeninvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bonus {
	// //azmy///
	Rect src = new Rect();
	Rect dst = new Rect();
	// ////////

	Bonus next;
	boolean active;
	boolean taken;
	int index;
	int x, y;
	int type;
	static int TYPE_GIFT = 0;
	static int TYPE_BURGER = 1;
	static int TYPE_CHICKENLEG = 2;
	static int TYPE_COIN = 3;
	static int TYPE_INI = -1;

	int color;
	static int COLOR_BLUE = 0;
	static int COLOR_RED = 1;
	static int COLOR_YELLOW = 2;
	static int COLOR_INI = -1;

	int fallingState2;
	int fallingState;
	static int FALLING_CURVE = -1;
	static int FALLING_STRAIGHT = 0;
	static int FALLING_LEFT = 1;
	static int FALLING_RIGHT = 2;
	static int FALLING_BOUNCING = 3;
	static int FALLING_BOUNCING_LEFT = 4;
	static int FALLING_BOUNCING_RIGHT = 5;

	int centerX;
	int centerY;

	int giftframe = 1;
	int legFrame = 0;
	int giftnumFrames = 5;
	int legnumFrames = 18;
	long timeStamp = 0;

	public Bonus() {
		x = 0;
		y = 0;
		type = TYPE_INI;
		color = COLOR_INI;
		next = null;
		active = false;
		taken = false;
	}

	public void updateGift(int giftVelocity, int GiftHeight, int GiftWidth) {
		y += giftVelocity;
		src.top = y;
		src.bottom = y + GiftHeight;
		src.left = x;
		src.right = x + GiftWidth;

		long currentTime = System.currentTimeMillis();
		long elapsed = currentTime - timeStamp;
		if (elapsed >= 200) {
			giftframe = (giftframe + 1) % (giftnumFrames + 1);
			if (giftframe == 0) {
				giftframe = 1;
			}
			timeStamp = System.currentTimeMillis();
		}

		this.dst.set((this.giftframe - 1) * GiftWidth, 0, this.giftframe
				* GiftWidth, GiftHeight);
	}

	public void updateLeg(int screenHeight, int ChicenLegHeight,
			int ChicenLegWidth, int chickenLegVelocity,
			int chickenLegScatterRadius, int chickenLeBounceXRadius,
			int chickenLeBounceYRadius) {
		int ytemp;
		if (this.fallingState == Bonus.FALLING_CURVE) {
			ytemp = Graphs.getYHalfCircle(this.x, chickenLegScatterRadius,
					this.centerX, this.centerY);
			if (ytemp == -1) {
				// arc completed
				this.y += chickenLegVelocity;
				this.src.top = this.y;
				this.src.bottom = this.y + ChicenLegHeight;
				this.src.left = this.x;
				this.src.right = this.x + ChicenLegWidth;
			} else {
				this.y = ytemp;
				if (this.fallingState2 == Bonus.FALLING_LEFT)
					this.x -= chickenLegVelocity;
				else
					this.x += chickenLegVelocity;
				this.src.top = this.y;
				this.src.bottom = this.y + ChicenLegHeight;
				this.src.left = this.x;
				this.src.right = this.x + ChicenLegWidth;
			}
			if (this.y >= screenHeight-ChicenLegHeight) {
				// bounce
				this.fallingState = Bonus.FALLING_BOUNCING;
				this.centerY = this.y;// mbd2yan
				if (this.fallingState2 == Bonus.FALLING_LEFT) {
					this.fallingState2 = Bonus.FALLING_BOUNCING_LEFT;
					this.centerX = this.x - chickenLeBounceXRadius; // mbd2yan
				} else {
					this.fallingState2 = Bonus.FALLING_BOUNCING_RIGHT;
					this.centerX = this.x + chickenLeBounceXRadius; // mbd2yan
				}
			}
		} else if (this.fallingState == Bonus.FALLING_STRAIGHT) {
			this.y += chickenLegVelocity;
			this.src.top = this.y;
			this.src.bottom = this.y + ChicenLegHeight;
			if (y >= screenHeight-ChicenLegHeight) {
				this.fallingState = Bonus.FALLING_BOUNCING;
				this.fallingState2 = Bonus.FALLING_STRAIGHT;
			}

		} else if (this.fallingState == Bonus.FALLING_BOUNCING) {

			ytemp = Graphs.getYHalfEllipse(this.x, chickenLeBounceXRadius,
					chickenLeBounceYRadius, this.centerX, this.centerY);
			if (ytemp == -1) {
				// arc completed
				// TODO remove Bonus Object from Bonus list
				// return it to pool
				this.active = false;
				this.taken = false;

			} else {
				this.y = ytemp;
				if (this.fallingState2 == Bonus.FALLING_BOUNCING_RIGHT)
					this.x += chickenLegVelocity;
				else
					this.x -= chickenLegVelocity;
				this.src.top = this.y;
				this.src.bottom = this.y + ChicenLegHeight;
				this.src.left = this.x;
				this.src.right = this.x + ChicenLegWidth;
			}

		}

		// here update the frame number
		long currTime = System.currentTimeMillis();
		long elapsed = currTime - timeStamp;
		if (elapsed >= 50) {
			legFrame = (legFrame + 1) % legnumFrames;
			timeStamp = currTime;
		}
	}

	public void drawLeg(Canvas canvas, Paint p, Bitmap[] frames) {

		// canvas.drawCircle(x, y, 5, p);
		canvas.drawBitmap(frames[legFrame], this.x, this.y, p);

	}

	public void drawGift(Canvas canvas, Paint p, Bitmap gift) {

		canvas.drawBitmap(gift, dst, src, p);

	}

	// when level finished and jump to the next level reset bouns data
	public void resetBouns() {

		centerX = 0;
		centerY = 0;

		giftframe = 1;
		legFrame = 0;
		giftnumFrames = 5;
		legnumFrames = 18;
		timeStamp = 0;

		fallingState2 = 0;
		fallingState = 0;

		x = 0;
		y = 0;
		type = TYPE_INI;
		color = COLOR_INI;
		next = null;
		active = false;
		taken = false;
	}
}
