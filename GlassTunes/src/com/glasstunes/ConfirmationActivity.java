package com.glasstunes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConfirmationActivity extends Activity {
	public static final String EXTRA_TEXT = "EXTRA_TEXT";
	public static final String EXTRA_FOLLOW_ON_INTENT = "EXTRA_FOLLOW_ON_INTENT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		((TextView) findViewById(R.id.text)).setText(getIntent()
				.getStringExtra(EXTRA_TEXT));
		ObjectAnimator animator = ObjectAnimator.ofInt(
				((ProgressBar) findViewById(R.id.progress)), "progress", 100)
				.setDuration(1000);
		animator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				try {
					if (getIntent().hasExtra(EXTRA_FOLLOW_ON_INTENT)) {
						((PendingIntent) getIntent().getParcelableExtra(
								EXTRA_FOLLOW_ON_INTENT)).send();
					}
				} catch (CanceledException e) {
					e.printStackTrace();
				}
				finish();

			}

		});
		animator.start();

	}
}
