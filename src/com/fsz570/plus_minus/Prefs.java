package com.fsz570.plus_minus;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//此設定 preference 得重寫
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
	
	public static boolean getEndless(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("endless", false);
	}
	
	public static boolean getHowToPlay(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("howToPlay", false);
	}
	

}