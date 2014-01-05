package com.example.chickeninvaders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;

public class ClientLoop extends Thread {

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
	int fps = 50;
	// ////////////////////////////////////////////////

	public boolean loop;
	private SurfaceHolder holder;
	private clientPanel panel;
//	public Client client;
	DataInputStream in;
	DataOutputStream out;
	public static String data;

	public ClientLoop(SurfaceHolder holder, clientPanel panel) {
		//client = new Client(Menu.view.getApplicationContext());
		data = "";
		InputStream sin;
		try {
			sin = MainActivity.cl.socket.getInputStream();
			OutputStream sout = MainActivity.cl.socket.getOutputStream();
			in = new DataInputStream(sin);
			out = new DataOutputStream(sout);
		} catch (IOException e) {
		}
		loop = true;
		this.holder = holder;
		this.panel = panel;
		this.framePeriod = 1000 / fps;
		lastTime = System.currentTimeMillis();
		this.maxFrameSkips = 1;
		this.numberOfFramesSkipped = 0;
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas c = null;
		// initialization must be her
		// game loop
		String outS="";
		while (loop == true) {
			if (holder.getSurface().isValid()) {

				beginTime = System.currentTimeMillis();

				try {
					//recive data
					data = in.readUTF();
					Log.w("C-din",data);
					Log.w("C-dout",data);
					out.writeUTF(panel.msgout);
					panel.msgout="";
					out.flush();
				
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					MainActivity.view.finish();
					//connection lost
					
				}
				c = holder.lockCanvas();
				panel.update(data);
				
				
				
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
					// some frames are skipped don nothing
				}

			}

		}
	}

}
