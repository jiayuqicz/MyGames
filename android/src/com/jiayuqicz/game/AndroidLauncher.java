package com.jiayuqicz.game;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        fullScreen();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
        config.useAccelerometer = false;
		initialize(new DropGame(), config);
	}

	//设置全屏
	public void fullScreen() {
        if(Build.VERSION.SDK_INT >14 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        }
        else if(Build.VERSION.SDK_INT >=19) {
            View view = getWindow().getDecorView();
            int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            view.setSystemUiVisibility(uiOption);
        }
    }
}
