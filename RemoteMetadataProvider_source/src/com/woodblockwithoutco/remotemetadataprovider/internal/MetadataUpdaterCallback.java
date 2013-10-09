/*	
 * Copyright (C) 2013 by Alexander Leontev
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.woodblockwithoutco.remotemetadataprovider.internal;

import java.util.ArrayList;
import java.util.List;

import com.woodblockwithoutco.remotemetadataprovider.media.RemoteMetadataProvider;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.PlayState;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.RemoteControlFeature;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Implementation of Handler.Callback interface to transfer necessary data to
 * RemoteMetadataProvider. Shouldn't be used explicitly by user.
 */
public class MetadataUpdaterCallback implements Handler.Callback {

	/*
	 * Information about current client.
	 */
	private int mGenerationId;
	private Bitmap mPrevBitmap = null;
	private List<RemoteControlFeature> mFeatureList = null;
	private RemoteMetadataProvider mMetadataProvider;

	public MetadataUpdaterCallback(RemoteMetadataProvider metadataProvider) {
		mMetadataProvider = metadataProvider;
		mFeatureList = new ArrayList<RemoteControlFeature>();
	}

	/**
	 * @param bundle
	 * @param key
	 * @return Will return null if we request duration.
	 */
	private String getMetadataString(Bundle bundle, int key) {
		if (key != MediaMetadataRetriever.METADATA_KEY_DURATION) {
			return bundle.getString(String.valueOf(key));
		} else {
			return null;
		}
	}

	private long getDuration(Bundle bundle) {
		return bundle.getLong(String.valueOf(MediaMetadataRetriever.METADATA_KEY_DURATION));
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case RemoteControlDisplay.MSG_SET_GENERATION_ID:
			mGenerationId = msg.arg1;
			mMetadataProvider.setCurrentClientPendingIntent((PendingIntent) msg.obj);
			return true;
		case RemoteControlDisplay.MSG_SET_METADATA:
			if (mGenerationId == msg.arg1) {
				if (mMetadataProvider.getOnMetadataChangeListener() != null) {
					Bundle metadata = (Bundle) msg.obj;
					mMetadataProvider.getOnMetadataChangeListener().onMetadataChanged(getMetadataString(metadata, MediaMetadataRetriever.METADATA_KEY_ARTIST), getMetadataString(metadata, MediaMetadataRetriever.METADATA_KEY_TITLE), getMetadataString(metadata, MediaMetadataRetriever.METADATA_KEY_ALBUM), getMetadataString(metadata, MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST), getDuration(metadata));
				}
			}
			return true;
		case RemoteControlDisplay.MSG_SET_TRANSPORT_CONTROLS:
			if (mGenerationId == msg.arg1) {
				if (mMetadataProvider.getOnRemoteControlFlagsChangeListener() != null) {
					int flags = msg.arg2;
					mFeatureList.clear();
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_FAST_FORWARD) == flags) mFeatureList.add(RemoteControlFeature.USES_FAST_FORWARD);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_NEXT) == flags) mFeatureList.add(RemoteControlFeature.USES_NEXT);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE) == flags) mFeatureList.add(RemoteControlFeature.USES_PAUSE);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_PLAY) == flags) mFeatureList.add(RemoteControlFeature.USES_PLAY);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE) == flags) mFeatureList.add(RemoteControlFeature.USES_PLAY_PAUSE);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS) == flags) mFeatureList.add(RemoteControlFeature.USES_PREVIOUS);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_REWIND) == flags) mFeatureList.add(RemoteControlFeature.USES_REWIND);
					if ((flags | RemoteControlClient.FLAG_KEY_MEDIA_STOP) == flags) mFeatureList.add(RemoteControlFeature.USES_STOP);
					mMetadataProvider.getOnRemoteControlFlagsChangeListener().onFeaturesChanged(mFeatureList);
				}
			}
			return true;
		case RemoteControlDisplay.MSG_SET_ARTWORK:
			if (mGenerationId == msg.arg1) {
				if (mMetadataProvider.getOnArtworkChangeListener() != null) {
					if (mPrevBitmap != null) {
						mPrevBitmap.recycle();
					}
					mPrevBitmap = (Bitmap) msg.obj;
					mMetadataProvider.getOnArtworkChangeListener().onArtworkChanged(mPrevBitmap);
				}
			}
			return true;
		case RemoteControlDisplay.MSG_UPDATE_STATE:
			if (mGenerationId == msg.arg1) {
				if (mMetadataProvider.getOnPlaybackStateChangeListener() != null) {
					switch (msg.arg2) {
					case RemoteControlClient.PLAYSTATE_BUFFERING:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.BUFFERING);
						break;
					case RemoteControlClient.PLAYSTATE_ERROR:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.ERROR);
						break;
					case RemoteControlClient.PLAYSTATE_FAST_FORWARDING:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.FAST_FORWARDING);
						break;
					case RemoteControlClient.PLAYSTATE_PAUSED:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.PAUSED);
						break;
					case RemoteControlClient.PLAYSTATE_PLAYING:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.PLAYING);
						break;
					case RemoteControlClient.PLAYSTATE_REWINDING:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.REWINDING);
						break;
					case RemoteControlClient.PLAYSTATE_SKIPPING_BACKWARDS:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.SKIPPING_BACKWARDS);
						break;
					case RemoteControlClient.PLAYSTATE_SKIPPING_FORWARDS:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.SKIPPING_FORWARDS);
						break;
					case RemoteControlClient.PLAYSTATE_STOPPED:
						mMetadataProvider.getOnPlaybackStateChangeListener().onPlaybackStateChanged(PlayState.STOPPED);
						break;
					}
				}
			}
			return true;
		}
		return false;
	}
}
