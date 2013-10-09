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
package com.woodblockwithoutco.remotemetadataprovider.media;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;

import com.woodblockwithoutco.remotemetadataprovider.internal.MetadataUpdaterCallback;
import com.woodblockwithoutco.remotemetadataprovider.internal.RemoteControlDisplay;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.MediaCommand;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnArtworkChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnMetadataChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnPlaybackStateChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnRemoteControlFeaturesChangeListener;

public final class RemoteMetadataProvider {

	private static RemoteMetadataProvider INSTANCE;
	private static final String TAG = "RemoteMetadataProvider";
	private OnArtworkChangeListener mArtworkListener;
	private AudioManager mAudioManager;
	private PendingIntent mClientIntent;
	private Context mContext;
	private OnRemoteControlFeaturesChangeListener mFeaturesListener;
	private Handler mHandler;
	private boolean mIsLooperUsed = false;
	private Looper mLooper;
	private OnMetadataChangeListener mMetadataListener;
	private MetadataUpdaterCallback mMetadataUpdaterCallback;
	private OnPlaybackStateChangeListener mPlaystateListener;
	private RemoteControlDisplay mRemoteControlDisplay;
	private boolean mShouldUpdateHandler;

	/*
	 * Constructor should be private as we don't want multiple instances.
	 */
	private RemoteMetadataProvider(Context context) {
		mContext = context;
		if (mContext != null) {
			mAudioManager = (AudioManager) mContext
					.getSystemService(Context.AUDIO_SERVICE);
		}
	}

	/**
	 * Returns instance of RemoteMetadataProvider
	 * 
	 * @param context
	 *            Current application context. This is required to get instance
	 *            of AudioManager.
	 * @return Active instance of RemoteMetadataProvider.
	 */
	public static synchronized RemoteMetadataProvider getInstance(
			Context context) {
		if (INSTANCE == null) {
			INSTANCE = new RemoteMetadataProvider(context);
		}
		return INSTANCE;
	}

	/**
	 * Acquires remote media controls. This method MUST be called whenever your
	 * View displaying metadata is shown or else you will not receive metadata
	 * updates and probably you won't be able to send media commands.
	 */
	public void acquireRemoteControls() {
		if (mAudioManager != null) {
			// if we don't have any RemoteControlDisplay or we have to update
			// our Handler
			if (mRemoteControlDisplay == null || mShouldUpdateHandler) {
				if (mMetadataUpdaterCallback == null) {
					mMetadataUpdaterCallback = new MetadataUpdaterCallback(
							INSTANCE);
				}
				// if we don't have any Handler or we should update it.
				if (mHandler == null || mShouldUpdateHandler) {
					if (mIsLooperUsed) {
						mHandler = new Handler(mLooper,
								mMetadataUpdaterCallback);
					} else {
						mHandler = new Handler(mMetadataUpdaterCallback);
						mLooper = null;
					}
				}
				mRemoteControlDisplay = new RemoteControlDisplay(mHandler);
				mShouldUpdateHandler = false;
			}
			// registering our RemoteControlDisplay
			mAudioManager.registerRemoteControlDisplay(mRemoteControlDisplay);
		} else {
			Log.w(TAG,
					"Failed to get instance of AudioManager while acquiring remote media controls");
		}
	}

	/**
	 * Drops remote media controls. This method MUST be called whenever your
	 * View displaying metadata is hidden or else you may block other
	 * applications working with remote media controls.
	 * 
	 * @param destroyRemoteControls
	 *            Set this to true if you have problems with displaying artwork.
	 *            Otherwise use false.
	 */
	public void dropRemoteControls(boolean destroyRemoteControls) {
		if (mAudioManager != null) {
			mAudioManager.unregisterRemoteControlDisplay(mRemoteControlDisplay);
			if (destroyRemoteControls) {
				mRemoteControlDisplay = null;
			}
			// remove all the messages from Handler
			mHandler.removeMessages(RemoteControlDisplay.MSG_SET_ARTWORK);
			mHandler.removeMessages(RemoteControlDisplay.MSG_SET_GENERATION_ID);
			mHandler.removeMessages(RemoteControlDisplay.MSG_SET_METADATA);
			mHandler.removeMessages(RemoteControlDisplay.MSG_SET_TRANSPORT_CONTROLS);
			mHandler.removeMessages(RemoteControlDisplay.MSG_UPDATE_STATE);
		} else {
			Log.w(TAG,
					"Failed to get instance of AudioManager while adropping remote media controls");
		}
	}

	/**
	 * @return Intent for launching current player or null if there is no
	 *         current client.
	 * @throws NameNotFoundException
	 *             This exception will be thrown in case the launch intent for
	 *             current player can't be found.
	 */
	public Intent getCurrentClientIntent() throws NameNotFoundException {
		if (mClientIntent == null) {
			return null;
		}
		return mContext.getPackageManager().getLaunchIntentForPackage(
				mClientIntent.getTargetPackage());
	}

	/**
	 * Returns current client media events receiver in form of PendingIntent.
	 * 
	 * @return Current client media events receiver or null if there is no
	 *         client.
	 */
	public PendingIntent getCurrentClientPendingIntent() {
		return mClientIntent;
	}

	/**
	 * Returns user-defined Looper.
	 * 
	 * @return user-defined Looper or null if default is used.
	 */
	public Looper getLooper() {
		return mLooper;
	}

	/**
	 * Returns the registered callback for artwork change event.
	 * 
	 * @return The callback or null if there is nothing registered.
	 */
	public OnArtworkChangeListener getOnArtworkChangeListener() {
		return mArtworkListener;
	}

	/**
	 * Returns the registered callback for metadata change event.
	 * 
	 * @return The callback or null if there is nothing registered.
	 */
	public OnMetadataChangeListener getOnMetadataChangeListener() {
		return mMetadataListener;
	}

	/**
	 * Returns the registered callback for playback state change event.
	 * 
	 * @return The callback or null if there is nothing registered.
	 */
	public OnPlaybackStateChangeListener getOnPlaybackStateChangeListener() {
		return mPlaystateListener;
	}

	/**
	 * Returns the registered callback for remote control features change event.
	 * 
	 * @return The callback or null if there is nothing registered.
	 */
	public OnRemoteControlFeaturesChangeListener getOnRemoteControlFlagsChangeListener() {
		return mFeaturesListener;
	}

	/**
	 * Check if remote media client is active(e.g. can receive media events and
	 * provide metadata).
	 * 
	 * @return true if there is remote media client to send events to and false
	 *         otherwise.
	 */
	public boolean isClientActive() {
		return !(mClientIntent == null);
	}

	/**
	 * Tells the RemoteMetadataProvider to stop using the looper. Note that
	 * there will be no effect until you call
	 * {@link RemoteMetadataProvider#acquireRemoteControls()}.
	 */
	public void removeLooper() {
		mIsLooperUsed = false;
		mShouldUpdateHandler = true;
	}

	private void sendBroadcastButton(int keyCode) {
		if (mContext != null) {
			long eventtime = SystemClock.uptimeMillis();
			Intent keyIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
			KeyEvent keyEvent = new KeyEvent(eventtime, eventtime,
					KeyEvent.ACTION_DOWN, keyCode, 0);
			keyIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
			mContext.sendOrderedBroadcast(keyIntent, null);
			keyEvent = KeyEvent.changeAction(keyEvent, KeyEvent.ACTION_UP);
			keyIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
			mContext.sendOrderedBroadcast(keyIntent, null);
		}
	}

	/**
	 * Sends input event with the specified keycode system-wide in form of
	 * Broadcast. Use this method only in case there is no active clients and
	 * you want to try to initialize one. If there is active client, use
	 * {@link RemoteMetadataProvider#sendMediaCommand(int)} instead. Please note
	 * that not all players support this.
	 * 
	 * @param keyCode
	 *            Button keycode to send.
	 */
	public void sendBroadcastMediaCommand(int keyCode) {
		sendBroadcastButton(keyCode);
	}

	/**
	 * Sends input event with the specified MediaCommand system-wide in form of
	 * Broadcast. Use this method only in case there is no active clients and
	 * you want to try to initialize one. If there is active client, use
	 * {@link RemoteMetadataProvider#sendMediaCommand(MediaCommand)} instead.
	 * Please note that not all players support this.
	 * 
	 * @param command
	 *            MediaCommand to send.
	 */
	public void sendBroadcastMediaCommand(MediaCommand command) {
		switch (command) {
		case REWIND:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_REWIND);
			break;
		case PREVIOUS:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
			break;
		case PLAY:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_PLAY);
			break;
		case PAUSE:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_PAUSE);
			break;
		case PLAY_PAUSE:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			break;
		case STOP:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_STOP);
			break;
		case NEXT:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_NEXT);
			break;
		case FAST_FORWARD:
			sendBroadcastButton(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
			break;
		}
	}

	/*
	 * Should not be used by user. Sends media button click event.
	 */
	private boolean sendButton(int keyCode) {
		KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
		Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		try {
			if (mClientIntent == null) {
				return false;
			}
			mClientIntent.send(mContext, 0, intent);
		} catch (CanceledException e) {
			// will silently fail with false return
			return false;
		}
		keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
		intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		try {
			if (mClientIntent == null) {
				return false;
			}
			mClientIntent.send(mContext, 0, intent);
		} catch (CanceledException e) {
			// will silently fail with false return
			return false;
		}
		return true;
	}

	/**
	 * Sends input event with the specified keycode to current player.
	 * 
	 * @param keyCode
	 *            Button keycode to send.
	 * @return True if event was delivered to player, false otherwise.
	 */
	public boolean sendMediaCommand(int keyCode) {
		if (mClientIntent != null) {
			return sendButton(keyCode);
		}
		// will silently fail with false return if client is missing
		return false;
	}

	/**
	 * Sends input event with the specified MediaCommand to current player.
	 * 
	 * @param command
	 *            MediaCommand enum with necessary action.
	 * @return True if event was delivered to player, false otherwise.
	 */
	public boolean sendMediaCommand(MediaCommand command) {
		if (mClientIntent != null) {
			switch (command) {
			case REWIND:
				return sendButton(KeyEvent.KEYCODE_MEDIA_REWIND);
			case PREVIOUS:
				return sendButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
			case PLAY:
				return sendButton(KeyEvent.KEYCODE_MEDIA_PLAY);
			case PAUSE:
				return sendButton(KeyEvent.KEYCODE_MEDIA_PAUSE);
			case PLAY_PAUSE:
				return sendButton(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
			case STOP:
				return sendButton(KeyEvent.KEYCODE_MEDIA_STOP);
			case NEXT:
				return sendButton(KeyEvent.KEYCODE_MEDIA_NEXT);
			case FAST_FORWARD:
				return sendButton(KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
			}
		}
		// will silently fail with false return if client is missing
		return false;
	}

	/**
	 * Sets current client PendingIntent. Should not be used manually unless you
	 * sure you are holding the right PendingIntent to send events to.
	 */
	public void setCurrentClientPendingIntent(PendingIntent pintent) {
		mClientIntent = pintent;
	}

	/**
	 * Sets looper to be used to process messages. Please note that effect
	 * 
	 * @param looper
	 *            Looper to be used
	 */
	public void setLooper(Looper looper) {
		mIsLooperUsed = true;
		mLooper = looper;
		mShouldUpdateHandler = true;
	}

	/**
	 * Register a callback to be invoked when artwork should be updated.
	 * 
	 * @param l
	 *            The callback that will run.
	 */
	public void setOnArtworkChangeListener(OnArtworkChangeListener l) {
		mArtworkListener = l;
	}

	/**
	 * Register a callback to be invoked when metadata should be updated.
	 * 
	 * @param l
	 *            The callback that will run.
	 */
	public void setOnMetadataChangeListener(OnMetadataChangeListener l) {
		mMetadataListener = l;
	}

	/**
	 * Register a callback to be invoked when playback state should be updated.
	 * 
	 * @param l
	 *            The callback that will run.
	 */
	public void setOnPlaybackStateChangeListener(OnPlaybackStateChangeListener l) {
		mPlaystateListener = l;
	}

	/**
	 * Register a callback to be invoked when remote control features should be
	 * updated.
	 * 
	 * @param l
	 *            The callback that will run.
	 */
	public void setOnRemoteControlFeaturesChangeListener(
			OnRemoteControlFeaturesChangeListener l) {
		mFeaturesListener = l;
	}
}
