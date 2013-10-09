package com.glasstunes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.woodblockwithoutco.remotemetadataprovider.media.RemoteMetadataProvider;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.PlayState;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnPlaybackStateChangeListener;

public class GlassTunesApp extends Application implements
		OnPlaybackStateChangeListener {

	private RemoteMetadataProvider mMetadataProvider;

	List<WeakReference<OnPlaybackStateChangeListener>> mPlaybackStateChangeListeners = new ArrayList<WeakReference<OnPlaybackStateChangeListener>>();

	private PlayState mCurrentState = PlayState.STOPPED;

	@Override
	public void onCreate() {
		super.onCreate();
		mMetadataProvider = RemoteMetadataProvider.getInstance(this);
		mMetadataProvider.setOnPlaybackStateChangeListener(this);
		mMetadataProvider.acquireRemoteControls();
	}

	public void addOnPlaybackStateChangedListener(
			OnPlaybackStateChangeListener listener) {
		mPlaybackStateChangeListeners
				.add(new WeakReference<OnPlaybackStateChangeListener>(listener));
		listener.onPlaybackStateChanged(mCurrentState);
	}

	@Override
	public void onPlaybackStateChanged(PlayState playbackState) {
		for (WeakReference<OnPlaybackStateChangeListener> ref : mPlaybackStateChangeListeners) {
			OnPlaybackStateChangeListener listener = ref.get();
			if (listener != null) {
				listener.onPlaybackStateChanged(playbackState);
			} else {
				mPlaybackStateChangeListeners.remove(ref);
			}
		}

		mCurrentState = playbackState;
	}

	public void removeOnPlaybackStateChangedListener(
			OnPlaybackStateChangeListener listener) {
		for (int i = mPlaybackStateChangeListeners.size() - 1; i >= 0; i--) {
			WeakReference<OnPlaybackStateChangeListener> ref = mPlaybackStateChangeListeners
					.get(i);
			OnPlaybackStateChangeListener item = ref.get();
			if (item == null || item.equals(listener)) {
				mPlaybackStateChangeListeners.remove(i);
			}
		}
	}
}
