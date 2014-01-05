package com.example.chickeninvaders;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.animation.BounceInterpolator;

public class Levels {

	public Chicken[][] chickens;
	public static int levelNum=1;
	public static Point p1, p2, p3, p4;
	public int MAX_WEAPON_UPGRADES = 2;
	public Bonus[] BonusPool;
	public int bonusPointer = 0;
	// ////osama/////
	public Chicken[] level3_rest;

	public Levels(int rows, int columns, int levelNum) {
		if (levelNum != 3)
			chickens = new Chicken[rows][columns - 1];
		Levels.levelNum = levelNum;
		bonusPointer = 0;
		BonusPool = new Bonus[300];
		// initialize bonusPool
		for (int i = 0; i < BonusPool.length; ++i) {
			BonusPool[i] = new Bonus();
		}

		ini();
	}

	public void ini() {
		int padX = Variables.paddingX;
		int padY = 20;
		int x = 0;
		int y = 0;

		switch (levelNum) {
		case 1:
			for (int i = 0; i < chickens.length; i++) {
				for (int j = 0; j < chickens[0].length; j++) {
					chickens[i][j] = new Chicken(padX + x, padY + y, 0, 0, 0, 0,2);
					chickens[i][j].moving = false;
					x += Variables.chickenWidth;
				}
				y += Variables.chickenHight;
				x = 0;
			}
			break;
		case 2:
			int starty = Variables.scHight - Variables.chickenHight;
			for (int i = 0; i < chickens.length; i++) {
				for (int j = 0; j < chickens[0].length; j++) {
					int endx = padX + x;
					int endy = padY + y;
					chickens[i][j] = new Chicken(0, starty, 0, starty, endx,
							endy,3);
					chickens[i][j].moving = true;
					// chickens[i][j].srcRect = new Rect(Variables.rotatWidth,
					// 0,
					// Variables.rotatWidth* 2, Variables.rotatHight);
					chickens[i][j].dstRect = new Rect(0, starty,
							Variables.chickenWidth, starty
									+ Variables.chickenHight);
					x += Variables.chickenWidth;
				}
				y += Variables.chickenHight;
				x = 0;
			}
			int ini_x = 0;
			int ini_y = starty;
			int ini_opx = Variables.scWidth - Variables.chickenWidth;
			int ini_opy = Variables.scHight - Variables.chickenHight;
			boolean flag = true;
			for (int j = chickens[0].length - 1; j >= 0; j--) {
				if (j < chickens[0].length / 2 && flag) {
					ini_x = ini_opx;
					ini_y = ini_opy;
					flag = false;
				}
				for (int i = 0; i < chickens.length; i++) {
					Chicken c = chickens[i][j];

					c.x = ini_x;
					c.y = ini_y;
					c.startX = c.x;
					c.startY = c.y;
					if (!flag) {
						c.directionX = -1;
						c.dx = c.endX - ini_opx;
						c.dy = c.endY - ini_opy;
						c.GDistance = (Math.abs(c.dx) >= Math.abs(c.dy));
					}
					ini_x -= 10 * Variables.chickenWidth * c.directionX;
					ini_y += 10 * Variables.chickenHight;
				}
			}

			// for mid coumn
			if (chickens[0].length % 2 != 0) {// odd
				int jj = chickens[0].length / 2;
				int indexY = 0 - 5 * Variables.chickenHight;

				for (int i = 0; i < chickens.length; i++) {
					Chicken c = chickens[i][jj];
					c.directionX = 2;
					c.y = indexY;
					c.x = c.endX;
					c.startX = c.x;
					c.startY = c.y;
					indexY += Variables.chickenHight;
					c.dx = c.endX - c.startX;
					c.dy = c.endY - c.startY;
					c.GDistance = (Math.abs(c.dx) >= Math.abs(c.dy));
				}
			}
			break;
		case 3:
			// inf shape
			// initiallize 4 points coordinates
			int center = Variables.scHight / 2;
			int p1y = center + 10;
			int p1x = 0;
			p1 = new Point(p1x, p1y);
			p2 = new Point(p1.x, 20);
			p3 = new Point(Variables.scWidth - Variables.chickenWidth, p1.y);
			p4 = new Point(p3.x, p2.y);

			// get Equilidian dist for corner line
			int d1 = (int) Math.sqrt(Math.pow(p1.x - p4.x, 2)
					+ Math.pow(p1.y - p4.y, 2));
			int num1 = (int) (d1 / (Math.sqrt(Math.pow(Variables.chickenWidth,
					2) + Math.pow(Variables.chickenHight, 2))));
			// num1 -= 2;

			int num2 = (p1.y - p2.y) / Variables.chickenHight;
			int totalsqr = (num1 * 2) + (num2 * 2) +3;
			int total = (int) Math.sqrt(totalsqr);
			// //osama/////////////////
			int rest = totalsqr - (total * total);
			// level3_rest = new Chicken[rest];
			// int inc = 0;
			// if (total * total != totalsqr) {
			// inc = totalsqr - (total * total) - 3;
			// while (total * (inc + total) > totalsqr)
			// inc--;
			// }
			// if (inc < 0)
			// inc = 0;
			// chickens = new Chicken[total][total + inc];
			chickens = new Chicken[1][totalsqr];
			x = 0 - Variables.chickenWidth;
			// 3dlha b tol el mrkb
			y = p1y;
			for (int i = 0; i < chickens.length; i++) {
				for (int j = 0; j < chickens[0].length; j++) {
					// H7OT AY KIMA FL ENDX WL ENDY TILL IT WORKS
					chickens[i][j] = new Chicken(x, y, x, y, p1.x, p2.y,5);
					chickens[i][j].moving = true;
					chickens[i][j].dstRect = new Rect(p1.x, p1.y,
							Variables.chickenWidth, p1.y
									+ Variables.chickenHight);
					chickens[i][j].dx = p2.x - p1.x;
					chickens[i][j].dy = p2.y - p1.y;
					chickens[i][j].GDistance = (Math.abs(chickens[i][j].dx) >= Math
							.abs(chickens[i][j].dy));
					x -= 9 * Variables.chickenWidth;
				}
			}
			// fill rest array
			// for (int i = 0; i < level3_rest.length; i++) {
			// level3_rest[i] = new Chicken(x, y, x, y, p1.x, p2.y);
			// level3_rest[i].moving = true;
			// level3_rest[i].dstRect = new Rect(p1.x, p1.y,
			// Variables.chickenWidth, p1.y + Variables.chickenHight);
			// level3_rest[i].dx = p2.x - p1.x;
			// level3_rest[i].dy = p2.y - p1.y;
			// level3_rest[i].GDistance = (Math.abs(level3_rest[i].dx) >= Math
			// .abs(level3_rest[i].dy));
			// x -= 9 * Variables.chickenWidth;
			//
			// }

			break;
		}

		// initialize gifts associated with chicks
		initGifts();

	}

	// move type for level 1
	public void introMoveLevel1(Chicken c) {
		c.x += Variables.Direction;
		if (c.x < 0) {
			Variables.Direction *= -1;
			c.x += Variables.Direction;

		} else if (c.x > (Variables.scWidth - Variables.chickenWidth)) {
			Variables.Direction *= -1;
			c.x += Variables.Direction;
		}
		c.dstRect.left = c.x;
		c.dstRect.right = c.x + Variables.chickenWidth;
	}

	// move type for level 2
	public void introMoveLevel2(Chicken c) {

		if (c.moving) {
			if (c.GDistance) {
				c.x += c.speed * c.directionX;
				c.y = (int) ((c.startY) + (c.dy) * (c.x - (c.startX)) / (c.dx));
			} else {
				if (c.directionX == 2)
					c.y += c.speed * 1;
				else
					c.y += c.speed * -1;

				c.x = (int) ((c.startX) + (c.dx) * (c.y - (c.startY)) / (c.dy));
			}
			if (c.directionX == 1) {
				if (c.x >= c.endX && c.y <= c.endY) {
					c.x = c.endX;
					c.y = c.endY;
					c.moving = false;
				}
			} else if (c.directionX == -1) {
				if (c.x <= c.endX && c.y <= c.endY) {
					c.x = c.endX;
					c.y = c.endY;
					c.moving = false;
				}
			} else {
				if (c.y >= c.endY) {
					c.x = c.endX;
					c.y = c.endY;
					c.moving = false;
				}
			}

			c.dstRect.left = c.x;
			c.dstRect.right = c.x + Variables.chickenWidth;
			c.dstRect.top = c.y;
			c.dstRect.bottom = c.y + Variables.chickenHight;
		}

	}

	// move type for level 3
	public void introMoveLevel3(Chicken c) {

		if (c.moving) {
			if (c.GDistance) {
				c.x += c.speed * c.directionX;
				c.y = (int) ((c.startY) + (c.dy) * (c.x - (c.startX)) / (c.dx));
			} else {
				c.y += c.speed * c.directionY;
				c.x = (int) ((c.startX) + (c.dx) * (c.y - (c.startY)) / (c.dy));
			}
			if (c.directionY == c.up && c.y <= c.endY) {// in case of up
														// direction
				// check curr point
				if (c.curr.equals(p1)) {
					// let point2 be the start and point3 be the end and set
					// directions
					c.startX = p2.x;
					c.startY = p2.y;
					c.x = c.startX;
					c.y = c.startY;
					c.endX = p3.x;
					c.endY = p3.y;
					c.directionX = c.right;
					c.directionY = c.down;
				} else if (c.curr.equals(p3)) {
					c.startX = p4.x;
					c.startY = p4.y;
					c.x = c.startX;
					c.y = c.startY;
					c.endX = p1.x;
					c.endY = p1.y;
					c.directionX = c.left;
					c.directionY = c.down;
				}
				c.curr.x = c.startX;
				c.curr.y = c.startY;
				c.dx = c.endX - c.startX;
				c.dy = c.endY - c.startY;
				c.GDistance = (Math.abs(c.dx) >= Math.abs(c.dy));
			} else if (c.directionY == c.down && c.y >= c.endY) {
				// check curr point
				if (c.curr.equals(p2)) {
					c.startX = p3.x;
					c.startY = p3.y;
					c.x = c.startX;
					c.y = c.startY;
					c.endX = p4.x;
					c.endY = p4.y;
					c.directionX = c.right;
					c.directionY = c.up;

				} else if (c.curr.equals(p4)) {
					c.startX = p1.x;
					c.startY = p1.y;
					c.x = c.startX;
					c.y = c.startY;
					c.endX = p2.x;
					c.endY = p2.y;
					c.directionX = c.right;
					c.directionY = c.up;
				}
				c.curr.x = c.startX;
				c.curr.y = c.startY;
				c.dx = c.endX - c.startX;
				c.dy = c.endY - c.startY;
				c.GDistance = (Math.abs(c.dx) >= Math.abs(c.dy));
			}

			c.dstRect.left = c.x;
			c.dstRect.right = c.x + Variables.chickenWidth;
			c.dstRect.top = c.y;
			c.dstRect.bottom = c.y + Variables.chickenHight;
		}
	}

	/**
	 * Method for initializing gifts number is choosen randomly from
	 * max_weapon_upgrade
	 */
	private void initGifts() {
		int rand = (int) (Math.random() * 1352);
		int numbWeaponUpgrades = rand % MAX_WEAPON_UPGRADES;
		if (numbWeaponUpgrades == 0)
			numbWeaponUpgrades = 1;
		// chose random chickens
		int col, row;
		for (int i = 0; i < numbWeaponUpgrades; ++i) {
			rand = (int) (Math.random() * 1352);
			col = rand % chickens[0].length;
			rand = (int) (Math.random() * 1352);
			row = rand % chickens.length;
			Chicken choosen = chickens[row][col];
			if (choosen.weaponUpgrade != null) {
				--i;
				continue;
			}
			Bonus upGrade = getBonus();
			choosen.weaponUpgrade = upGrade;

		}

	}

	/**
	 * can be improved to be O(1) by replacing Bonus pool by a List having tail
	 * and before tail pointer
	 * 
	 * @return
	 */
	public Bonus getBonus() {
//		Bonus el3alehael3eanwelneny = BonusPool[bonusPointer];
//		bonusPointer = (bonusPointer + 1) % BonusPool.length;
//		if (el3alehael3eanwelneny.active)
//			// ///////om new hna :( :(
			return new Bonus();
//		return el3alehael3eanwelneny;
	}

	public void nextLevel() {
		bonusPointer = 0;
		// loop on all bouns and reset them
		for (int i = 0; i < BonusPool.length; ++i) {
			BonusPool[i].resetBouns();
		}
		ini();
		// hal el variables eli fo2 leha 7aga tt3ml
		// ask 3zmi
	}

}
