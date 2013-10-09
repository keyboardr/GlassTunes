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

import java.util.List;

import com.woodblockwithoutco.remotemetadataprovider.media.enums.RemoteControlFeature;

public interface OnRemoteControlFeaturesChangeListener {

	/**
	 * Called when information about player was changed.
	 * 
	 * @param usesFeatures
	 *            List containing features which are used by player. For
	 *            example, if list contains
	 *            {@link RemoteControlFeature#USES_FAST_FORWARD} and
	 *            {@link RemoteControlFeature#USES_REWIND}, then the player
	 *            supports rewinding and fast forwarding.
	 */
	public void onFeaturesChanged(List<RemoteControlFeature> usesFeatures);
}
