package com.scp.slidingmenu;

import com.scp.slidingmenu.view.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
	private SlidingMenu leftMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main);
		initView();
	}
	
	private void initView() {
		leftMenu = (SlidingMenu) findViewById(R.id.main_left_menu);
	}

	public void toggleMenu(View view){
		leftMenu.toggle();
	}
}
