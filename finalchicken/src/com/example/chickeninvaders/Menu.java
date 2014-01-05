package com.example.chickeninvaders;

import com.example.finalchicken.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class Menu extends Activity implements OnClickListener {
	public static Activity view;
	public static final String networkName = "chicken@osa";
	public static final String networkPass = "a1b2c3d45";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = this;
		// to set it full screen and turn of title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// for full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		findViewById(R.id.start).setOnClickListener(this);

		findViewById(R.id.create).setOnClickListener(this);

		findViewById(R.id.join).setOnClickListener(this);

		findViewById(R.id.exit).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.start:
			finish();
			Intent t = new Intent(view, MainActivity.class);
			t.putExtra("case", "start");
			startActivity(t);
			break;
		case R.id.create:
			finish();
			Intent rt = new Intent(view, MainActivity.class);
			rt.putExtra("case", "server");
			startActivity(rt);
			break;
		case R.id.join:
			finish();
			Intent tt = new Intent(view, MainActivity.class);
			tt.putExtra("case", "join");
			startActivity(tt);

			break;
		case R.id.exit:
			finish();
			onDestroy();
			break;

		}
	}
}
