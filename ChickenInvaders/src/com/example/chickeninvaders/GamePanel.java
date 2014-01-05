package com.example.chickeninvaders;

import com.example.finalchicken.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback,
		SensorEventListener {

	private Paint scorePaint;
	public static final int chickensound = R.raw.chdie;
	public static final int bulletsound = R.raw.fire;
	public static final int shipExplosion = R.raw.pexp;
	// osama//
	private boolean levelFinished;
	private Bitmap background, loading;
	// //////
	// /azmy..///////////

	Paint paint = new Paint();

	// /////////////////////

	// /////////////shlby/////////////////////////////////

	public int GAIN = 2;
	public int UP_GAIN = 1;
	public int DOWN_GAIN = 3;
	public float cx, cy, radius;
	public float[] linearAcc;
	public float[] Aout;
	public BulletList blist;
	public Bullet[] barray;
	int currentBullet = 0;
	boolean fire = false;
	int bulletConter = 0;
	MediaPlayer p;

	// //////////////////////////////////////////////////

	private GameLoop loop;
	private Bitmap chicken;
	private Levels data;
	private byte changeFrame;
	private boolean level2;

	// Azmy///////////////
	private Bitmap leg, egg, eggBroken, gift;
	private Bitmap[] legRotated = new Bitmap[18];
	Egg[] eggs = new Egg[50];
	long interval; // interval to next drop
	long lastDropTime;// time of last drop
	EggList eggList = new EggList();
	long maxInterval = 4000;
	int minInterval;
	int chickenLeBounceXRadius = 30;
	int chickenLeBounceYRadius = 60;
	int eggVelocity = 2;
	int chickenLegVelocity = 2;
	int giftVelocity = 2;
	BonusList bonusList = new BonusList();
	BonusList giftList = new BonusList();
	// //////////////////

	private int count = -1;
	private Paint TextPaint;
	private long currTime;

	public GamePanel(Context context) {
		// adding call back to intercept events
		super(context);
		getHolder().addCallback(this);
		setFocusable(true);// to handle events
		loop = new GameLoop(getHolder(), this);

		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.game_bg);
		loading = BitmapFactory.decodeResource(getResources(),
				R.drawable.galaxy);
		Variables.bullet_Bit_Double = BitmapFactory.decodeResource(
				getResources(), R.drawable.doublebullet);

		// ////AZMY/////////////////////////////////////
		for (int i = 0; i < eggs.length; ++i) {
			eggs[i] = new Egg();
		}
		// /////////////////////////////////////////////
		scorePaint = new Paint();
		scorePaint.setColor(Color.RED);

		TextPaint = new Paint();
		TextPaint.setColor(Color.RED);

		// bulletSound = MediaPlayer.create(getContext(), R.raw.fire);
		playerSound = MediaPlayer.create(getContext(), R.raw.pexp);
		// chickenDie = MediaPlayer.create(getContext(),R.raw.chdie);

		chicken = BitmapFactory.decodeResource(getResources(), R.drawable.ch);
		chicken = getResizedBitmap(chicken, 46, 287);
		Variables.chickenWidth = chicken.getWidth() / 7;
		Variables.chickenHight = chicken.getHeight();
		changeFrame = 1;
		Variables.Direction = -1;
		level2 = false;
		levelFinished = false;
		linearAcc = new float[3];
		Aout = new float[3];

		Variables.bullet_Bit = BitmapFactory.decodeResource(getResources(),
				R.drawable.bullet);
		Variables.BulletWidth = Variables.bullet_Bit.getWidth();
		Variables.BulletHeight = Variables.bullet_Bit.getHeight();
		currTime = 0;

		// azmy/// ini bitmaps
		Variables.explosion = BitmapFactory.decodeResource(getResources(),
				R.drawable.smokered);
		egg = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
		leg = BitmapFactory.decodeResource(getResources(),
				R.drawable.chickenleg);
		eggBroken = BitmapFactory.decodeResource(getResources(),
				R.drawable.eggbroken2);
		gift = BitmapFactory.decodeResource(getResources(), R.drawable.giftred);

		Variables.GiftWidth = gift.getWidth() / 5;
		Variables.GiftHeight = gift.getHeight();
		Variables.eggHeight = egg.getHeight();
		Variables.eggWidth = egg.getWidth();
		Variables.eggBrokenHeight = eggBroken.getHeight();
		Variables.eggBrokenWidth = eggBroken.getWidth() / 5;
		Variables.ChicenLegWidth = leg.getWidth();
		Variables.ChicenLegHeight = leg.getHeight();

		// init. chicken rotated legs
		for (int i = 0; i < legRotated.length; ++i) {
			Matrix matrix = new Matrix();

			// create matrix for the manipulation
			// resize the bit map
			matrix.postScale(1, 1);
			int rotatAngle = 360 / legRotated.length;
			// rotate the Bitmap
			matrix.postRotate(rotatAngle * i);

			// recreate the new Bitmap
			legRotated[i] = Bitmap.createBitmap(leg, 0, 0,
					Variables.ChicenLegWidth, Variables.ChicenLegHeight,
					matrix, true);

		}

		// /////////shlby///////////////
		Variables.bullet_Bit_Green = BitmapFactory.decodeResource(
				getResources(), R.drawable.green);
		// /////////////////

		// /////////////////////////////////////////////////

		initiateBullets();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		Variables.scWidth = getWidth();
		Variables.scHight = getHeight();
		Variables.spaceShip = new ship(this);
		// resize------------------------------------
		background = getResizedBitmap(background, Variables.scHight,
				Variables.scWidth);
		loading = getResizedBitmap(loading, Variables.scHight,
				Variables.scWidth);
		int Columns = (getWidth() / Variables.chickenWidth) - 1;
		Variables.paddingX = (int) Math.ceil(getWidth()
				- (Columns * Variables.chickenWidth));
		int rows= (Variables.scHight/2)/Variables.chickenHight;
		data = new Levels(rows-1, Columns, 1);
		loop.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// stopping thread when surface closed
		boolean flag = true;
		loop.loop = false;
		while (flag) {
			try {
				loop.join();
				flag = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// loop on trying shutdown thread
			}
		}
	}

	/*
	 * Method that update positions for shooter ball and check for collisions
	 * params and return not decided yet
	 */
	public void update() {

	}

	boolean first = true;
	private MediaPlayer playerSound;

	@Override
	protected void onDraw(Canvas canvas) {

		// clear screen
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		if (!levelFinished) {
			// set background screen
			canvas.drawBitmap(background, 0, 0, null);
			switch (Levels.levelNum) {
			case 1:
				level_1_view(canvas);
				break;
			case 2:
				level_2_view(canvas);
				break;
			case 3:
				level_3_view(canvas);
				break;
			}
			changeFrame++;

			// //////////////azmy test chicken legs////////////////
			// ////////////////////////////////////////////////////

			eggEngine();

			// azmyyyyyyyyyyyyyyyyyyy///////////////////////////////////////////////////////
			// draw eggs here
			Egg current = eggList.root;
			// Paint p = new Paint();
			paint.setColor(Color.WHITE);
			for (int i = 0; i < eggList.length; ++i) {
				current.update(eggVelocity, Variables.eggHeight,
						Variables.eggBrokenWidth, Variables.eggBrokenHeight,
						getHeight());
				if (current.broken) {
					current.drawBroken(canvas, eggBroken, paint);
				} else {
					current.draw(canvas, egg, paint);
					checkCollEggShip(current);
				}
				current = current.next;
			}
			// loop to remove in active and out of screen eggs from the eggs
			// list
			current = eggList.root;
			for (int i = 0; i < eggList.length; ++i) {
				if (current.y > getHeight() || current.active == false) {
					eggList.removeBall(current);
				}
				current = current.next;
			}
			// draw bonus here //////////////////////////////////////
			Bonus currentBonus = bonusList.root;
			paint.setColor(Color.YELLOW);
			int y;
			for (int i = 0; i < bonusList.length; ++i) {
				if (currentBonus.type == Bonus.TYPE_CHICKENLEG
						&& currentBonus.active) {
					currentBonus.updateLeg(getHeight(),
							Variables.ChicenLegHeight,
							Variables.ChicenLegWidth, chickenLegVelocity,
							Variables.chickenLegScatterRadius,
							chickenLeBounceXRadius, chickenLeBounceYRadius);
					currentBonus.drawLeg(canvas, paint, legRotated);
					checkCollShipBonus(currentBonus);

				}
				currentBonus = currentBonus.next;

			}

			// loop on gift list
			Bonus currGift = giftList.root;
			for (int i = 0; i < giftList.length; ++i) {
				if (currGift.y > getHeight() || currGift.active == false) {
					// remove gift from view
					giftList.removeBonus(currGift);
				} else {
					if (currGift.type == Bonus.TYPE_GIFT) {
						currGift.updateGift(giftVelocity, Variables.GiftHeight,
								Variables.GiftWidth);
						currGift.drawGift(canvas, paint, gift);
						checkCollShipBonus(currGift);
					}
				}
				currGift = currGift.next;
			}

			// ///////////////////////////////////.//////////////////////////////////

			// ///shlby/////////////////////////
			if (Variables.spaceShip.destroying) {
				// hna arsm el swar el enfgar
				playerSound.start();

				boolean draw = Variables.spaceShip.explosionSetFrame();
				if (draw)
					canvas.drawBitmap(Variables.spaceShip.explosion,
							Variables.spaceShip.src, Variables.spaceShip.dst,
							null);
				else
					onDestroyShip(Variables.spaceShip);
			} else
				Variables.spaceShip.onDraw(canvas);
			if (blist.length > 0) {
				Bullet currentBullet = blist.root;
				for (int i = 0; i < blist.length; i++) {
					currentBullet.onDraw(canvas);
					currentBullet = currentBullet.next;
				}
				Bullet b = blist.root;
				for (int i = 0; i < blist.length; i++) {
					if ((b.y <= 0)) {
						blist.removeBullet(b);
					}
					b = b.next;
				}
			}
			// ///////////////////////////////////

			canvas.drawText("Score : " + Variables.spaceShip.score, 10, 15,
					scorePaint);
			canvas.drawText("Life :" + Variables.spaceShip.life,
					getWidth() - 50, 15, scorePaint);
		} else {
			if (currTime == 0) {// it is first time to switch level
				// level finished
				// to the next level or to finish layout if it is the last level
				if (Levels.levelNum != 3) {// to the next level
					canvas.drawBitmap(loading, 0, 0, null);
					// draw string
					Levels.levelNum++;
					canvas.drawText("Level " + Levels.levelNum,
							(getWidth() / 2) - 20, getHeight() / 2, TextPaint);
					// mfrod hna a prepare el next level
					toNextLevel();
					currTime = System.currentTimeMillis();
				} else {// game completed
					endGame(true);
				}
			} else {
				// remain while with level number on screen
				if (System.currentTimeMillis() - currTime >= 5000) {
					// begin next level
					levelFinished = false;
					currTime = 0;
				} else {// keep the screen on
					canvas.drawBitmap(loading, 0, 0, null);
					canvas.drawText("Level up :)", (getWidth() / 3),
							(getHeight() / 2), TextPaint);
					canvas.drawText("Score : " + Variables.spaceShip.score, 15,
							getHeight() - 100, TextPaint);
					canvas.drawText("Lives : " + Variables.spaceShip.life,
							(getWidth() / 2) - 20, getHeight() - 100, TextPaint);
					canvas.drawText("Level : " + Levels.levelNum,
							getWidth() - 60, getHeight() - 100, TextPaint);

				}
			}
		}

	}

	private void checkCollShipBonus(Bonus currentBonus) {
		if (Variables.spaceShip.dst.intersect(currentBonus.src)) {
			currentBonus.active = false;
			currentBonus.taken = false;
			bonusList.removeBonus(currentBonus);
			if (currentBonus.type == Bonus.TYPE_CHICKENLEG) {
				// TODO increase score
				Variables.spaceShip.score++;

				// sound
				AudioManager audioManager = (AudioManager) MainActivity.o;

				float actualVolume = (float) audioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				float maxVolume = (float) audioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volume = actualVolume / maxVolume;
				// Is the sound loaded already?
				if (MainActivity.loaded3) {
					MainActivity.soundPool3.play(MainActivity.eatid, volume,
							volume, 1, 0, 1f);
				}
				// end sound

			} else if (currentBonus.type == Bonus.TYPE_GIFT) {
				// TODO upgrade wepon
				// shlby//////////
				if (Bullet.upgrade == 1) {
					Bullet.upgrade = 2;
					Variables.BulletWidth = Variables.bullet_Bit_Double
							.getWidth();
					Variables.BulletHeight = Variables.bullet_Bit_Double
							.getHeight();
				} else {
					if (Bullet.upgrade == 2) {
						Bullet.upgrade = 3;
						Variables.BulletWidth = Variables.bullet_Bit_Green
								.getWidth();
						Variables.BulletHeight = Variables.bullet_Bit_Green
								.getHeight();

					}
				}
			}
		}

	}

	private void checkCollEggShip(Egg current) {
		if (Variables.spaceShip.dst.intersect(current.src)) {
			current.active = false;
			// call on ship destroyed->>pass ship 1
			Variables.spaceShip.destroying = true;
		}

	}

	/*
	 * Method that scale the image
	 */
	private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/**
	 * this method is called when a chicken is dying, it should pop up bonuse
	 * (gift and fried chicken legs :D)
	 */
	public void onChickenDestroyed(Chicken chicken, Bullet bullet) {
		chicken.isExploded = true; // /shlbyy dead--> is exploded
		chicken.moving = false;
		chicken.dead = true;
		// make probability of no legs = 2 probability of leg
		int rand = (int) (Math.random() * 135245);
		int alpha = rand % 2;
		alpha = 2;
		if (alpha > 1) {
			Bonus leg = data.getBonus();
			leg.type = Bonus.TYPE_CHICKENLEG;
			leg.taken = true;
			leg.active = true;
			leg.centerX = chicken.x + Variables.chickenWidth / 2; // mbd2yn
			leg.centerY = chicken.y + Variables.chickenHight;// mbd2yn
			leg.x = leg.centerX;
			leg.y = chicken.y;
			leg.src.top = leg.y;
			leg.src.left = leg.x;
			leg.src.right = leg.x + Variables.ChicenLegWidth;
			leg.src.bottom = leg.y + Variables.ChicenLegHeight;
			bonusList.addBonus(leg);
			// from where the leg will be projected
			// three choices left right straight
			int chickenMiddle = chicken.x + Variables.chickenWidth / 2;

			if (bullet.x <= chickenMiddle + Variables.chickenWidth / 8
					&& bullet.x >= chickenMiddle - Variables.chickenWidth / 8) {
				// bullet falling straight
				leg.fallingState = Bonus.FALLING_STRAIGHT;
			} else if (bullet.x < chickenMiddle - Variables.chickenWidth / 8) {
				// bullet falling left
				leg.fallingState2 = Bonus.FALLING_LEFT;
				leg.fallingState = Bonus.FALLING_CURVE;
			} else {
				// bullet falling right
				leg.fallingState2 = Bonus.FALLING_RIGHT;
				leg.fallingState = Bonus.FALLING_CURVE;
			}
		}
		// ///////azmy///////////////
		// drop weapon upgrades
		if (chicken.weaponUpgrade != null) {

			chicken.weaponUpgrade.active = true;
			chicken.weaponUpgrade.x = chicken.x + Variables.chickenWidth / 2;
			chicken.weaponUpgrade.y = chicken.y + Variables.chickenHight;
			chicken.weaponUpgrade.src.top = chicken.weaponUpgrade.y;
			chicken.weaponUpgrade.src.left = chicken.weaponUpgrade.x;
			chicken.weaponUpgrade.src.bottom = chicken.weaponUpgrade.y
					+ Variables.GiftHeight;
			chicken.weaponUpgrade.src.right = chicken.weaponUpgrade.x
					+ Variables.GiftHeight;
			chicken.weaponUpgrade.type = Bonus.TYPE_GIFT;
			chicken.weaponUpgrade.color = Bonus.COLOR_RED;
			giftList.addBonus(chicken.weaponUpgrade);
		}
		// ///////////////////////////////

	}

	/**
	 * number of eggs falling in the same time is dependent on level number
	 * numEggs=Alpha * level, Alpha is chosen randomly from 0 to 2
	 * 
	 */
	String outmsg = "";

	public void eggEngine() {
		int rand;
		rand = (int) (Math.random() * 1352);
		int alpha = rand % 2;
		if (alpha == 0)
			alpha = 1;
		long currtime = System.currentTimeMillis();
		if (interval == 0)
			interval = 1000;
		if (lastDropTime == 0)
			lastDropTime = currtime;
		long elapsed = currtime - lastDropTime;
		if (elapsed >= interval) {
			lastDropTime = System.currentTimeMillis();
			// drop some eggs
			// set new interval max = maxInterval
			rand = (int) (Math.random() * 135245);
			interval = rand % maxInterval;
			if (interval < 1000)
				interval += 1000;
			// set number of falling eggs depending on the interval
			int fallingEggs;
			fallingEggs = alpha;
			// chose firing chickens randomly
			// this can be improved by considering the current player position
			int col, row;
			for (int i = 0; i < fallingEggs; ++i) {
				rand = (int) (Math.random() * 1352);
				col = rand % data.chickens[0].length;
				rand = (int) (Math.random() * 2489);
				row = rand % data.chickens.length;
				Chicken choosen = data.chickens[row][col];
				if (choosen.dead) {
					--i;
					continue;
				}
				Egg egg = getEgg();
				egg.active = true;
				egg.x = choosen.x + Variables.chickenWidth / 2;
				egg.y = choosen.y + Variables.chickenHight;
				egg.src.top = egg.y;
				egg.src.left = egg.x;
				egg.src.bottom = egg.y + Variables.eggHeight;
				egg.src.right = egg.x + Variables.eggWidth;
				eggList.addBullet(egg);

			}
		}

	}

	public Egg getEgg() {
		++count;
		if (count == eggs.length)
			count = 0;
		eggs[count].broken = false;
		eggs[count].frame = 0;
		return eggs[count];
	}

	// method to draw view of level1
	public void level_1_view(Canvas canvas) {
		levelFinished = true;
		for (int i = 0; i < data.chickens.length; i++) {
			for (int j = 0; j < data.chickens[0].length; j++) {
				Chicken c = data.chickens[i][j];
				// /////////////shlby/////////////////////////////////
				if (c.isExploded) {
					canvas.drawBitmap(Variables.explosion, c.srcRectSmoke,
							c.dstRect, null);
					if (changeFrame % 2 == 0)
						c.setFrame1();
				} else {
					// ////////////////////shlby///////////////////
					if (!c.dead) {
						levelFinished = false;
						collideBulletChicken(c, blist);
						// one life lost
						if (Variables.spaceShip.dst.intersect(c.dstRect)) {
							Variables.spaceShip.destroying = true;
						}

						canvas.drawBitmap(chicken, c.srcRect, c.dstRect, null);

						data.introMoveLevel1(c);
						// make delay of changing chicken frame
						if (changeFrame % 3 == 0)
							c.setFrame1();
					}
				}
			}
		}

	}

	// method to draw view of level2
	public void level_2_view(Canvas canvas) {
		levelFinished = true;
		if (!level2) {
			boolean flag = false;
			for (int j = data.chickens[0].length - 1; j >= 0; j--) {
				if (j == data.chickens[0].length / 2) {
					continue;
				}
				for (int i = 0; i < data.chickens.length; i++) {
					Chicken c = data.chickens[i][j];
					if (c.isExploded) {
						canvas.drawBitmap(Variables.explosion, c.srcRectSmoke,
								c.dstRect, null);
						if (changeFrame % 2 == 0)
							c.setFrame1();
					} else {
						if (!c.dead) {
							levelFinished = false;
							// update positions
							if (c.directionX == 1
									&& c.x < 0
									&& c.y > (Variables.scHight - Variables.chickenHight)) {
								c.x += Variables.chickenWidth;
								c.y -= Variables.chickenHight;
								c.startX = c.x;
								c.startY = c.y;
							} else if (c.directionX == -1
									&& c.x > (Variables.scWidth - Variables.chickenWidth)
									&& c.y > (Variables.scHight - Variables.chickenHight)) {
								c.x -= Variables.chickenWidth;
								c.y -= Variables.chickenHight;
								c.startX = c.x;
								c.startY = c.y;
							} else if (c.directionX == 2 && c.y < 0) {
								c.y += Variables.chickenHight;
								c.startY = c.y;
							} else {
								collideBulletChicken(c, blist);
								if (Variables.spaceShip.dst
										.intersect(c.dstRect)) {
									Variables.spaceShip.destroying = true;
								}
								canvas.drawBitmap(chicken, c.srcRect,
										c.dstRect, null);
								if (c.moving) {
									data.introMoveLevel2(c);
									flag = true;
								}
							}
							if (changeFrame % 3 == 0)
								c.setFrame1();
						}
					}
				}
			}

			if (!flag) {
				// draw mid chicken
				int j = data.chickens[0].length / 2;
				for (int i = data.chickens.length - 1; i >= 0; i--) {
					Chicken c = data.chickens[i][j];
					if (!c.dead) {
						levelFinished = false;
						collideBulletChicken(c, blist);
						if (Variables.spaceShip.dst.intersect(c.dstRect)) {
							Variables.spaceShip.destroying = true;
						}

						canvas.drawBitmap(chicken, c.srcRect, c.dstRect, null);
						if (c.moving)
							data.introMoveLevel2(c);
						if (changeFrame % 3 == 0)
							c.setFrame1();
					}
				}
				if (data.chickens[0][data.chickens[0].length / 2].moving == false
						&& data.chickens[data.chickens.length - 1][data.chickens[0].length / 2].moving == false)
					level2 = true;
			}
		} else
			level_1_view(canvas);
	}

	// method to draw view of level3
	public void level_3_view(Canvas canvas) {
		levelFinished = true;
		for (int i = 0; i < data.chickens.length; i++) {
			for (int j = 0; j < data.chickens[0].length; j++) {
				Chicken c = data.chickens[i][j];
				if (c.isExploded) {
					canvas.drawBitmap(Variables.explosion, c.srcRectSmoke,
							c.dstRect, null);
					if (changeFrame % 2 == 0)
						c.setFrame1();
				} else {
					if (!c.dead) {
						levelFinished = false;
						if (c.x < 0) {
							c.x += Variables.chickenWidth;
							c.startX = c.x;
						} else {
							collideBulletChicken(c, blist);
							if (Variables.spaceShip.dst.intersect(c.dstRect)) {
								Variables.spaceShip.destroying = true;
							}
							canvas.drawBitmap(chicken, c.srcRect, c.dstRect,
									null);
							data.introMoveLevel3(c);
							// make delay of changing chicken frame
							if (changeFrame % 3 == 0)
								c.setFrame3();
						}
					}
				}
			}
		}
	}

	/**
	 * method to return y-coordinate of half circle if y = -1 the half circle is
	 * complete it is supposed that y is always +ve
	 * 
	 * @param x
	 * @param radius
	 * @param cenX
	 * @param cenY
	 * @return
	 */
	public static int getYHalfCircle(int x, int radius, int cenX, int cenY) {
		int CradiusSqr = (int) Math.pow(radius, 2);
		double dif1 = CradiusSqr - Math.pow(x - cenX, 2);
		if (dif1 < 0)
			return -1;
		return (int) (-Math.sqrt(dif1) + cenY);
	}

	/**
	 * method to return y-coordinate of half ellipse if y = -1 the half circle
	 * is complete it is supposed that y is always +ve
	 * 
	 * @param x
	 * @param radius
	 * @param cenX
	 * @param cenY
	 * @return
	 */
	public static int getYHalfEllipse(int x, int radiusX, int radiusY,
			int cenX, int cenY) {
		int rXsq = (int) Math.pow(radiusX, 2);
		int rYsq = (int) Math.pow(radiusY, 2);
		int diff = (int) (rYsq - (rYsq / rXsq) * Math.pow((x - cenX), 2));
		if (diff < 0)
			return -1;
		return (int) (-Math.sqrt(diff) + cenY);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void sensorChange(ship Spaceship, float[] values) {
		noFilter(values);
		Spaceship.xSpeed -= GAIN * Aout[0];
		if (Spaceship.xSpeed > getWidth() - Spaceship.width) {
			Spaceship.xSpeed += GAIN * Aout[0];
		}
		if (Spaceship.xSpeed < 0) {
			Spaceship.xSpeed += GAIN * Aout[0];
		}
		if (Aout[0] < 0) {
			Spaceship.direction = 2;
		} else if (Aout[0] > 0) {
			Spaceship.direction = 0;
		} else {
			Spaceship.direction = 1;
		}
		if (Aout[1] > -0.5) {
			Spaceship.ySpeed += DOWN_GAIN * Aout[1];
			if (Spaceship.ySpeed > getHeight() - Spaceship.height) {
				Spaceship.ySpeed -= DOWN_GAIN * Aout[1];
			}
		} else {
			Spaceship.ySpeed += UP_GAIN * Aout[1];
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		noFilter(event.values);
		Variables.spaceShip.xSpeed -= GAIN * Aout[0];
		if (Variables.spaceShip.xSpeed > getWidth() - Variables.spaceShip.width) {
			Variables.spaceShip.xSpeed += GAIN * Aout[0];
		}
		if (Variables.spaceShip.xSpeed < 0) {
			Variables.spaceShip.xSpeed += GAIN * Aout[0];
		}
		if (Aout[0] < 0) {
			Variables.spaceShip.direction = 2;
		} else if (Aout[0] > 0) {
			Variables.spaceShip.direction = 0;
		} else {
			Variables.spaceShip.direction = 1;
		}
		if (Aout[1] > -0.5) {
			Variables.spaceShip.ySpeed += DOWN_GAIN * Aout[1];
			if (Variables.spaceShip.ySpeed > getHeight()
					- Variables.spaceShip.height) {
				Variables.spaceShip.ySpeed -= DOWN_GAIN * Aout[1];
			}
		} else {
			Variables.spaceShip.ySpeed += UP_GAIN * Aout[1];
		}
	}

	private void noFilter(float[] values) {
		// TODO Auto-generated method stub
		Aout[0] = values[0];
		Aout[1] = values[1];
		Aout[2] = values[2];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		barray[bulletConter].x = (int) Variables.spaceShip.xSpeed + 10;
		barray[bulletConter].y = (int) Variables.spaceShip.ySpeed;
		barray[bulletConter].dem.set((int) Variables.spaceShip.xSpeed + 10,
				(int) Variables.spaceShip.ySpeed,
				(int) Variables.spaceShip.xSpeed + 10 + Variables.BulletWidth,
				(int) Variables.spaceShip.ySpeed + Variables.BulletHeight);
		blist.addBullet(barray[bulletConter]);
		bulletConter++;

		if (bulletConter >= barray.length) {
			bulletConter = 0;

		}
		AudioManager audioManager = (AudioManager) MainActivity.o;

		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		// Is the sound loaded already?
		if (MainActivity.loaded) {
			MainActivity.soundPool.play(MainActivity.soundID, volume, volume,
					1, 0, 1f);
		}
		return super.onTouchEvent(event);
		// fire=true;
	}

	public void collideBulletChicken(Chicken c, BulletList l) {
		Bullet current = l.root;
		for (int i = 0; i < l.length; i++) {
			if (current.dem.intersect(c.dstRect)) {
				// sound
				AudioManager audioManager = (AudioManager) MainActivity.o;

				float actualVolume = (float) audioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				float maxVolume = (float) audioManager
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volume = actualVolume / maxVolume;
				// Is the sound loaded already?
				if (MainActivity.loaded2) {
					MainActivity.soundPool2.play(MainActivity.soundID2, volume,
							volume, 1, 0, 1f);
				}
				// end sound
				if (current.upgrade == 1)
					c.NumOfHits--;
				else if (current.upgrade == 2)
					c.NumOfHits -= 2;
				else if (current.upgrade == 3)
					c.NumOfHits -= 3;
				Variables.spaceShip.score += 2;
				if (c.NumOfHits <= 0) {
					onChickenDestroyed(c, current);
					Variables.spaceShip.score += 3;
				}
				blist.removeBullet(current);
				break;
			}
			current = current.next;
		}
	}

	private void onDestroyShip(ship spaShip) {
		Variables.spaceShip.dead = true;
		Variables.spaceShip.life--;
		spaShip.reset();
		if (Variables.spaceShip.life == 0) {
			// end game
			// loop.loop=false;
			endGame(false);
		}
	}

	private void endGame(boolean type) {
		// surfaceDestroyed(getHolder());
		loop.loop = false;
		Intent t = new Intent(this.getContext(), Finish.class);
		t.putExtra("type", type);// false means game over,true means next
		t.putExtra("sc",Variables.spaceShip.score);
		// level
		MainActivity.view.finish();
		MainActivity.view.startActivity(t);

	}

	/**
	 * Method that is called when level is completed and moving to the next
	 * level all it does that it resets the whole data the differs feom level to
	 * another.
	 */
	private void toNextLevel() {
		// reset eggs
		// reset bullets
		// reset any list (hdaia,eggs,legs,bulle list)
		// reset spaceship
		// choose the next level
		// at2kd mn el reset 3shan lw zwdo hma 7aga mfrod a3mlha reset hia kman

		// m3rfsh b2a 3dd el bed hizid y2l 3la asaso hzbt el loop eli t7t di
		// ////AZMY/////////////////////////////////////
		count = -1;
		// for (int i = 0; i < eggs.length; ++i) {
		// eggs[i] = new Egg();
		// }
		// /////////////////////////////////////////////

		changeFrame = 1;
		Variables.Direction = -1;
		level2 = false;
		lastDropTime = 0;
		minInterval = 0;

		Variables.spaceShip.reset();// just reset position of the spaceship
		bulletConter = 0;
		currentBullet = 0;

		// reset lists
		blist.length = 0;
		bonusList.length = 0;
		eggList.length = 0;
		giftList.length = 0;
		if (Levels.levelNum == 2)
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.bk2);
		else {
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.bk3);
			chicken = BitmapFactory.decodeResource(getResources(),
					R.drawable.chicken);
			Variables.chickenWidth = chicken.getWidth() / 3;
			Variables.chickenHight = chicken.getHeight() / 3;
		}
		background = getResizedBitmap(background, getHeight(), getWidth());
		// reset bouns pool and bouns counter
		data.nextLevel();
	}

	public void initiateBullets() {
		// TODO Auto-generated method stub
		blist = new BulletList();
		barray = new Bullet[30];
		for (int i = 0; i < barray.length; i++) {
			Bullet b = new Bullet();
			barray[i] = b;
		}
	}
}
