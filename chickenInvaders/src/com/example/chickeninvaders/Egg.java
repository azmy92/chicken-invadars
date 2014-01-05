package com.example.chickeninvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Egg {
	Egg next;
	int x;
	int y;
	// azmy//////
	Rect src = new Rect();
	Rect dst = new Rect();
	long timestamp;
	boolean active = false;
	// /azmy///////////
	static Chicken[][] chiks; // chickens array
	static Egg[] eggs;
	static long interval; // interval to next drop
	static int level; // level no
	static long lastDropTime;// time of last drop
	static EggList eggList;
	static int maxInterval;
	static int minInterval;
	boolean broken = false;
	int frame = 0;
	int numFrames = 5;

	public void update(int eggVelocity, int eggHeight, int eggBrokenWidth,
			int eggBrokenHeight, int screenHeight) {
		if (this.y <screenHeight-eggHeight  && this.active) {
			this.y += eggVelocity;
			this.src.top = this.y;
			this.src.bottom = this.y + eggHeight;
		} else {
			long currTime = System.currentTimeMillis();
			if (!this.broken) {
				this.broken = true;
				this.timestamp = currTime;
			}
			long timeElapsed = currTime - this.timestamp;
			int period = 150;
			if (this.frame == numFrames)
				period = 1000;
			if (this.frame == 0)
				period = 0;
			if (timeElapsed >= period) {
				this.timestamp = System.currentTimeMillis();
				++this.frame;
				if (this.frame > numFrames) {
					this.active = false;
				}
			}
			this.dst.set((this.frame - 1) * eggBrokenWidth, 0, this.frame
					* eggBrokenWidth, eggBrokenHeight);
		}
	}

	public static Egg getEgg() {
		Egg egg = new Egg();
		return egg;

	}

	public void drawBroken(Canvas canvas, Bitmap eggBroken, Paint p) {
		canvas.drawBitmap(eggBroken, dst, src, p);

	}

	public void draw(Canvas canvas, Bitmap egg, Paint p) {
		canvas.drawBitmap(egg, x, y, p);

	}
}
