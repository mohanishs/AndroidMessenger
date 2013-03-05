/*
 * Splash.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */
package com.androidmessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * The Splash class is used to initialize the splash screen when the application
 * starts
 * 
 * 
 */

public class Splash extends Activity {
	// String buffer for outgoing messages

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread logotimer = new Thread() {
			public void run() {

				try {

					int logotimer = 0;
					while (logotimer < 5000) {
						sleep(100);
						logotimer = logotimer + 100;
					}
					startActivity(new Intent(Splash.this,
							AndroidMessenger.class));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					finish();

				}

			}
		};
		logotimer.start();
	}

}