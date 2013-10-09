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

public interface OnMetadataChangeListener {

	/**
	 * Called when remote metadata was updated.
	 * 
	 * @param artist
	 *            Artist of current song. May be null if wasn't specified by
	 *            player. Some players use albumArtist parameter instead.
	 * @param title
	 *            Title of current song. May be null if wasn't specified by
	 *            player.
	 * @param album
	 *            Current song album title. May be null if wasn't specified by
	 *            player.
	 * @param albumArtist
	 *            Current song album artist. May be null if wasn't specified by
	 *            player. Some players use this parameter instead of artist
	 *            parameter.
	 * @param duration
	 *            Song duration in milliseconds.
	 */
	public void onMetadataChanged(String artist, String title, String album, String albumArtist, long duration);
}
