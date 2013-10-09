package com.glasstunes.cards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glasstunes.ControlsActivity;
import com.glasstunes.GlassTunesApp;
import com.glasstunes.R;
import com.glasstunes.screenslide.CardFragment;
import com.woodblockwithoutco.remotemetadataprovider.media.RemoteMetadataProvider;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.PlayState;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnArtworkChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnMetadataChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnPlaybackStateChangeListener;

public class NowPlayingCard extends CardFragment implements
		OnArtworkChangeListener, OnMetadataChangeListener,
		OnPlaybackStateChangeListener {

	public static final NowPlayingCard newInstance() {
		return new NowPlayingCard();
	}

	private RemoteMetadataProvider mMetadataProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMetadataProvider = RemoteMetadataProvider.getInstance(getActivity());
		mMetadataProvider.setOnArtworkChangeListener(this);
		mMetadataProvider.setOnMetadataChangeListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_content_card, container, false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		mMetadataProvider.acquireRemoteControls();
		((GlassTunesApp) getActivity().getApplication())
				.addOnPlaybackStateChangedListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		mMetadataProvider.dropRemoteControls(false);
		((GlassTunesApp) getActivity().getApplication())
				.removeOnPlaybackStateChangedListener(this);
	}

	@Override
	public void onSelect() {
		startActivity(new Intent(getActivity(), ControlsActivity.class));
	}

	@Override
	public void onArtworkChanged(Bitmap arg0) {
		((ImageView) getView().findViewById(R.id.image)).setImageBitmap(arg0);
	}

	@Override
	public void onMetadataChanged(String artist, String title, String album,
			String albumArtist, long duration) {
		((TextView) getView().findViewById(R.id.name)).setText(title);
		((TextView) getView().findViewById(R.id.description)).setText(artist);
	}

	@Override
	public void onPlaybackStateChanged(PlayState playbackState) {
		getView().findViewById(R.id.progress)
				.setVisibility(
						playbackState == PlayState.BUFFERING ? View.VISIBLE
								: View.GONE);
	}

}
