package com.example.chickeninvaders;


import com.example.finalchicken.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Finish extends Activity implements OnClickListener {

	private Context c;
	private boolean type;
	private int sc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// to set it full screen and turn of title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// for full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finish);

		Button v = (Button) findViewById(R.id.playAgain);
		v.setOnClickListener(this);
		type = getIntent().getExtras().getBoolean("type");
		//sc = getIntent().getExtras().getInt("sc");
		if (type) 
			((TextView) findViewById(R.id.statusTxt))
					.setText("Mission Complete");
			((TextView) findViewById(R.id.textView2)).setText("Score : "+Variables.spaceShip.score);

		
		c = this;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.playAgain:
			finish();

			startActivity(new Intent(c, Menu.class));
			break;

		}

	}
}
