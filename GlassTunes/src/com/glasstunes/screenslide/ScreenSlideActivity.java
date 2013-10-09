package com.glasstunes.screenslide;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.glasstunes.R;
import com.viewpagerindicator.PageIndicator;

public abstract class ScreenSlideActivity extends FragmentActivity {

	protected ViewPager mPager;

	protected ScreenSlidePagerAdapter mPagerAdapter;

	private PageIndicator mPageIndicator;

	private int mInitialItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_screen_slide);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				select();
			}
		});
		mPagerAdapter = onCreatePagerAdapter();
		mPager.setAdapter(mPagerAdapter);

		mPageIndicator = (PageIndicator) findViewById(R.id.vpi);
		if (mInitialItem > 0) {
			mPageIndicator.setViewPager(mPager, mInitialItem);
		} else {
			mPageIndicator.setViewPager(mPager);
		}

		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onSingleTapConfirmed(MotionEvent e) {
						mPager.performClick();
						return true;
					};
				});
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		event.setLocation(getResources().getDisplayMetrics().widthPixels
				- event.getX(), event.getY());
		return mGestureDetector.onTouchEvent(event)
				|| mPager.onTouchEvent(event);
	}

	protected abstract ScreenSlidePagerAdapter onCreatePagerAdapter();

	public void next() {
		mPageIndicator.setCurrentItem(mPager.getCurrentItem() + 1);
	}

	public void prev() {
		mPageIndicator.setCurrentItem(mPager.getCurrentItem() - 1);
	}

	public void select() {
		mPagerAdapter.onSelect(mPager.getCurrentItem());
	}

	public void setCurrentItem(int position) {
		if (mPageIndicator != null) {
			mPageIndicator.setCurrentItem(position);
		} else {
			mInitialItem = position;
		}
	}

	private GestureDetector mGestureDetector;
}