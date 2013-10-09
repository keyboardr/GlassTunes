package com.glasstunes.cards;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.glasstunes.controller.MusicController;
import com.glasstunes.screenslide.BasicCardFragment;

public class ActionCard extends BasicCardFragment {
	protected static final String ARG_INTENT_URI = null;

	protected static Bundle generateArgs(String display_name, String intent_uri) {
		Bundle args = BasicCardFragment.generateArgs(0, display_name);
		args.putString(ARG_INTENT_URI, intent_uri);
		return args;
	}

	public static ActionCard newInstance(String display_name, String intent_uri) {
		ActionCard frag = new ActionCard();
		Bundle args = generateArgs(display_name, intent_uri);
		frag.setArguments(args);
		return frag;
	}

	protected Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = savedInstanceState == null ? getArguments()
				: savedInstanceState;
		try {
			mIntent = Intent.parseUri(args.getString(ARG_INTENT_URI), 0);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(ARG_INTENT_URI,
				mIntent.toUri(Intent.URI_INTENT_SCHEME));
	}

	@Override
	public void onSelect() {
		super.onSelect();
		if ("com.google.android.music.xdi.intent.PLAY".equals(mIntent
				.getAction())) {
			MusicController.getInstance().play(getActivity(), mIntent);
		} else {
			startActivity(mIntent);
		}
		getActivity().setResult(Activity.RESULT_OK);
		getActivity().finish();
	}
}
