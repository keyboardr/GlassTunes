package com.glasstunes.cards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.glasstunes.GlassTunesApp;
import com.glasstunes.R;
import com.glasstunes.controller.MusicController;
import com.glasstunes.screenslide.BasicCardFragment;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.PlayState;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnPlaybackStateChangeListener;

public class PausePlayCard extends BasicCardFragment implements
		OnPlaybackStateChangeListener {
	public static PausePlayCard newInstance() {
		PausePlayCard frag = new PausePlayCard();
		Bundle args = BasicCardFragment.generateArgs(
				R.drawable.ic_musicplayer_play, R.string.av_play);
		frag.setArguments(args);
		return frag;
	}

	private boolean isPlaying = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

		((GlassTunesApp) getActivity().getApplication())
				.addOnPlaybackStateChangedListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		((GlassTunesApp) getActivity().getApplication())
				.removeOnPlaybackStateChangedListener(this);
	}

	@Override
	public void onSelect() {
		super.onSelect();
		if (isPlaying) {
			MusicController.getInstance().pause(getActivity());
		} else {
			MusicController.getInstance().play(getActivity());
		}
		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
	}

	@Override
	public void onPlaybackStateChanged(PlayState playbackState) {
		switch (playbackState) {
		case BUFFERING:
			isPlaying = true;
			break;
		case ERROR:
			isPlaying = false;
			break;
		case FAST_FORWARDING:
			break;
		case PAUSED:
			isPlaying = false;
			break;
		case PLAYING:
			isPlaying = true;
			break;
		case REWINDING:
			break;
		case SKIPPING_BACKWARDS:
			break;
		case SKIPPING_FORWARDS:
			break;
		case STOPPED:
			isPlaying = false;
			break;
		}
		updateState(getView());
	}

	private void updateState(View view) {
		setIconRes(isPlaying ? R.drawable.ic_musicplayer_pause
				: R.drawable.ic_musicplayer_play);
		setLabelRes(isPlaying ? R.string.av_pause : R.string.av_play);
	}
}
