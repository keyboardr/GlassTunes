package com.glasstunes.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.glasstunes.ConfirmationActivity;
import com.glasstunes.R;

public class LocalMusicController extends MusicController {

	protected LocalMusicController() {
	}

	@Override
	public void play(Context context) {
		Intent confirmationIntent = new Intent(context,
				ConfirmationActivity.class);
		confirmationIntent.putExtra(ConfirmationActivity.EXTRA_TEXT,
				context.getString(R.string.av_play));
		Intent playIntent = new Intent(
				"com.android.music.musicservicecommand.togglepause");
		confirmationIntent.putExtra(
				ConfirmationActivity.EXTRA_FOLLOW_ON_INTENT,
				PendingIntent.getBroadcast(context, 0, playIntent, 0));
		context.startActivity(confirmationIntent);

	}

	@Override
	public void pause(Context context) {
		Intent confirmationIntent = new Intent(context,
				ConfirmationActivity.class);
		confirmationIntent.putExtra(ConfirmationActivity.EXTRA_TEXT,
				context.getString(R.string.av_pause));
		Intent pauseIntent = new Intent(
				"com.android.music.musicservicecommand.pause");
		confirmationIntent.putExtra(
				ConfirmationActivity.EXTRA_FOLLOW_ON_INTENT,
				PendingIntent.getBroadcast(context, 0, pauseIntent, 0));
		context.startActivity(confirmationIntent);
	}

	@Override
	public void next(Context context) {
		Intent confirmationIntent = new Intent(context,
				ConfirmationActivity.class);
		confirmationIntent.putExtra(ConfirmationActivity.EXTRA_TEXT,
				context.getString(R.string.av_next));
		Intent nextIntent = new Intent(
				"com.android.music.musicservicecommand.next");
		confirmationIntent.putExtra(
				ConfirmationActivity.EXTRA_FOLLOW_ON_INTENT,
				PendingIntent.getBroadcast(context, 0, nextIntent, 0));
		context.startActivity(confirmationIntent);
	}

	@Override
	public void previous(Context context) {
		Intent confirmationIntent = new Intent(context,
				ConfirmationActivity.class);
		confirmationIntent.putExtra(ConfirmationActivity.EXTRA_TEXT,
				context.getString(R.string.av_previous));
		Intent previousIntent = new Intent(
				"com.android.music.musicservicecommand.previous");
		confirmationIntent.putExtra(
				ConfirmationActivity.EXTRA_FOLLOW_ON_INTENT,
				PendingIntent.getBroadcast(context, 0, previousIntent, 0));
		context.startActivity(confirmationIntent);
	}

	@Override
	public void play(Context context, Intent intent) {
		Intent confirmationIntent = new Intent(context,
				ConfirmationActivity.class);
		confirmationIntent.putExtra(ConfirmationActivity.EXTRA_TEXT,
				context.getString(R.string.av_play));
		context.startActivity(intent);
		confirmationIntent.putExtra(
				ConfirmationActivity.EXTRA_FOLLOW_ON_INTENT,
				PendingIntent.getActivity(context, 0, intent, 0));
		context.startActivity(confirmationIntent);
	}

}
