package com.glasstunes.controller;

import android.content.Context;
import android.content.Intent;

public abstract class MusicController {

	private static final class SingletonHolder {
		public static MusicController singleton = new LocalMusicController();
	}

	public static final MusicController getInstance() {
		return SingletonHolder.singleton;
	}

	public abstract void play(Context context);

	public abstract void pause(Context context);

	public abstract void next(Context context);

	public abstract void previous(Context context);

	public abstract void play(Context context, Intent intent);
}
