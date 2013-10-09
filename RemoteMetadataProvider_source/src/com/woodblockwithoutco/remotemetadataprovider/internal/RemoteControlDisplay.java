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

import java.lang.ref.WeakReference;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.media.IRemoteControlDisplay;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;

/**
 * Implementation of remote interface that will receive metadata updates.
 * Shouldn't be used explicitly by user.
 */
public class RemoteControlDisplay extends IRemoteControlDisplay.Stub {

	public static final int MSG_SET_GENERATION_ID = 103;
	public static final int MSG_SET_METADATA = 101;
	public static final int MSG_SET_TRANSPORT_CONTROLS = 102;
	public static final int MSG_UPDATE_STATE = 100;
	public static final int MSG_SET_ARTWORK = 104;
	/*
	 * The reference should be weak as we can't predict when the process of GC
	 * will happen in remote object.
	 */
	private WeakReference<Handler> mLocalHandler;

	public RemoteControlDisplay(Handler handler) {
		mLocalHandler = new WeakReference<Handler>(handler);
	}

	@Override
	public void setAllMetadata(int generationId, Bundle metadata, Bitmap bitmap) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_METADATA, generationId, 0, metadata)
					.sendToTarget();
			handler.obtainMessage(MSG_SET_ARTWORK, generationId, 0, bitmap)
					.sendToTarget();
		}
	}

	@Override
	public void setArtwork(int generationId, Bitmap bitmap) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_ARTWORK, generationId, 0, bitmap)
					.sendToTarget();
		}
	}

	@Override
	public void setCurrentClientId(int clientGeneration,
			PendingIntent mediaIntent, boolean clearing) throws RemoteException {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_GENERATION_ID, clientGeneration,
					(clearing ? 1 : 0), mediaIntent).sendToTarget();
		}
	}

	@Override
	public void setMetadata(int generationId, Bundle metadata) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_METADATA, generationId, 0, metadata)
					.sendToTarget();
		}
	}

	@Override
	public void setPlaybackState(int generationId, int state,
			long stateChangeTimeMs) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_UPDATE_STATE, generationId, state)
					.sendToTarget();
		}
	}

	@Override
	public void setTransportControlFlags(int generationId, int flags) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_TRANSPORT_CONTROLS, generationId,
					flags).sendToTarget();
		}
	}

	public void setPlaybackState(int generationId, int state,
			long stateChangeTimeMs, long currentPosMs, float speed) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			Bundle infoBundle = new Bundle();
			infoBundle.putFloat("speed", speed);
			infoBundle.putLong("position", currentPosMs);
			handler.obtainMessage(MSG_UPDATE_STATE, generationId, state,
					infoBundle).sendToTarget();
		}
	}

	public void setTransportControlInfo(int generationId, int flags,
			int posCapabilities) {
		Handler handler = mLocalHandler.get();
		if (handler != null) {
			handler.obtainMessage(MSG_SET_TRANSPORT_CONTROLS, generationId,
					flags, posCapabilities).sendToTarget();
		}
	}
}
