package com.example.chickeninvaders;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Bullet {

	public String type;
	public short state;
	public Bullet next;
	public int x;
	public int y;
	int yBulletSpeed = 10;
	public static int upgrade;
	Rect dem;
	
	public Bullet() {
		dem = new Rect();
		this.upgrade = 1;
	}

	public void onDraw(Canvas c) {

		y -= yBulletSpeed;
		;
		dem.set(x, y, x + Variables.BulletWidth, y + Variables.BulletHeight);
		// if(Variables.numOfweapon==0)

		if (upgrade == 1) {
			c.drawBitmap(Variables.bullet_Bit, x, y, null);
		} else if (upgrade == 3) {
			c.drawBitmap(Variables.bullet_Bit_Green, x, y, null);
		} else if (upgrade == 2) {
			c.drawBitmap(Variables.bullet_Bit_Double, x, y, null);
		}
	}
}
