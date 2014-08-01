package com.fsz570.plus_minus;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	public static String getTimes(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("times", "10");
	}

	public static String getOperators(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("operators", "2");
	}
}