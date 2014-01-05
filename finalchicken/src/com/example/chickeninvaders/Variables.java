package com.example.chickeninvaders;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

public class Variables {

	public static Bitmap bullet_Bit;
	private Paint scorePaint;
	public static int BulletWidth;
	public static int BulletHeight;

	// osama//
	private boolean levelFinished;
	private Bitmap background, loading;
	public static Bitmap bullet_Bit_Double;
	// //////
	// /azmy..///////////

	static int eggWidth = 10;
	static int eggHeight = 10;
	static int eggBrokenWidth;
	static int eggBrokenHeight;

	static int ChicenLegWidth = 10;
	static int ChicenLegHeight = 10;

	static int GiftWidth = 10;
	static int GiftHeight = 10;
	Paint paint = new Paint();
	// /////////////////////

	// /////////////shlby/////////////////////////////////
	public static Bitmap bullet_Bit_Green;

	public static ship spaceShip;
	// //////////////////////////////////////////////////

	public static int chickenWidth, chickenHight, rotatWidth, rotatHight;
	public static int paddingX;
	public static short Direction;
	public static int scWidth, scHight;

	// Azmy///////////////

	static Bitmap explosion;
	static int level = 2; // level no
	static int chickenLegScatterRadius = 50;

}
