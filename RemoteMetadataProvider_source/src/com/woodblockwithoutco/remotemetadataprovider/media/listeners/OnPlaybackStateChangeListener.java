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
package com.woodblockwithoutco.remotemetadataprovider.media.listeners;

import com.woodblockwithoutco.remotemetadataprovider.media.enums.PlayState;

public interface OnPlaybackStateChangeListener {

	/**
	 * Called when playback state was changed. For example, this method will be
	 * called with parameter {@link PlayState#PAUSED} when playback is paused.
	 * 
	 * @param playbackState
	 *            Possible values of playbackState are listed in enum class
	 *            PlayState
	 */
	public void onPlaybackStateChanged(PlayState playbackState);
}
