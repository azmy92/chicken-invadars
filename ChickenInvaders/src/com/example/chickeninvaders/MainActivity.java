package com.example.chickeninvaders;

import java.io.IOException;

import com.example.finalchicken.R;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private SensorManager sensorManager;
	private GamePanel game;
	private serverPanel sp;
	private clientPanel cp;
	public static Server sv;
	public static Client cl;
	public static Activity view;
	private short w;

	public static SoundPool soundPool;
	public static SoundPool soundPool2;
	public static SoundPool soundPool3;
	public static int soundID, eatid;
	public static boolean loaded = false;
	public static Object o;
	public static int soundID2;
	public static boolean loaded2 = false, loaded3 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		o = getSystemService(AUDIO_SERVICE);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool2 = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool3 = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});
		soundID = soundPool.load(this, R.raw.fire, 1);
		soundPool2.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded2 = true;
			}
		});
		soundID2 = soundPool2.load(this, R.raw.chdie, 1);

		soundPool3.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded3 = true;
			}
		});
		eatid = soundPool3.load(this, R.raw.eat, 1);

		view = this;
		sv = null;
		cl = null;
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// to set it full screen and turn of title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// for full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String cases = getIntent().getExtras().getString("case");
		if (cases.equals("start")) {
			w = 0;
			game = new GamePanel(this);
			setContentView(game);
		} else if (cases.equals("server")) {
			w = 1;
			sv = new Server(this);
			if (sv != null)
				sp = new serverPanel(this);
			setContentView(sp);
		} else if (cases.equals("join")) {
			w = 2;
			cl = new Client(this);
			if (cl.socket != null) {
				cp = new clientPanel(this);
				setContentView(cp);
			}
		}

		// setContentView(game);
		// findViewById(R.id.start_button).setOnClickListener(this);
		//
		// findViewById(R.id.continue_button).setOnClickListener(this);
		//
		// findViewById(R.id.create_button).setOnClickListener(this);
		//
		// findViewById(R.id.join_button).setOnClickListener(this);
		//
		// findViewById(R.id.exit_button).setOnClickListener(this);

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		// close socket here
		onDestroy();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		switch (w) {
		case 1:
			// am the server
			try {
				if (sv != null && sv.socket != null) {
					if (sv.ss != null)
						sv.ss.close();
					if (sp != null && sp.loop.out != null)
						sp.loop.out.close();
					if (sp != null && sp.loop.in != null)
						sp.loop.in.close();
					sv.socket.close();
				}
			} catch (IOException e) {
				Log.d("main", "drb fl on destroy");
			}
			break;
		case 2:
			// am the client
			try {
				if (cl != null && cl.socket != null) {
					if (cp != null && cp.loop.in != null)
						cp.loop.in.close();
					if (cp != null && cp.loop.out != null)
						cp.loop.out.close();
					cl.socket.close();
				}
			} catch (IOException e) {
				Log.d("main", "drb fl on destroy");
			}
			break;
		default:
			break;
		}
	}

	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and
		// accelerometer sensors
		switch (w) {
		case 0:
			sensorManager.registerListener(game,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_GAME);
			break;
		case 1:
			sensorManager.registerListener(sp,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_GAME);
			break;
		case 2:
			sensorManager.registerListener(cp,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_GAME);
			break;

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		// unregister listener
		switch (w) {
		case 0:
			sensorManager.unregisterListener(game);
			break;
		case 1:
			// am the server
			sensorManager.unregisterListener(sp);
			// try {
			// if (sp != null) {
			// sp.loop.server.socket.close();
			// sp.loop.in.close();
			// sp.loop.out.close();
			// }
			// } catch (IOException e) {
			// Log.d("main", "drb fl on pause");
			// }
			break;
		case 2:
			// am the client
			sensorManager.unregisterListener(cp);
			// try {
			// if (cp != null) {
			// cp.loop.client.socket.close();
			// cp.loop.in.close();
			// cp.loop.out.close();
			// }
			// } catch (IOException e) {
			// Log.d("main", "drb fl on pause");
			// }
			break;
		default:
			break;
		}
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.start_button:
	// // game = new GamePanel(c);
	// // setContentView(game);
	// break;
	// case R.id.continue_button:
	// break;
	// case R.id.create_button:
	// //
	// break;
	// case R.id.join_button:
	//
	// break;
	// case R.id.exit_button:
	// finish();
	// onDestroy();
	// break;
	//
	// }
	// }

}
