package com.ilhamrhmtkbr.core.utils.tools;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class LangUtil {
    private static final String PREFS_NAME = "lang_prefs";
    private static final String KEY_LANG = "lang_mode";
    private final SharedPreferences prefs;
    public static final String LANG_ID = "in";
    public static final String LANG_EN = "en";

    @Inject
    public LangUtil(@ApplicationContext Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getLang() {
        return prefs.getString(KEY_LANG, LANG_EN);
    }

    public void setLang(String lang) {
        prefs.edit().putString(KEY_LANG, lang).apply();
    }
}