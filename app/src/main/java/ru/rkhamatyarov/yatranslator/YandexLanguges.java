package ru.rkhamatyarov.yatranslator;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Asus on 18.04.2017.
 */

class YandexLanguges {
    private String[] dirs;
    private Map<String, String> langs;

    public String[] getDirs() {
        return dirs;
    }

    public void setDirs(String[] dirs) {
        this.dirs = dirs;
    }

    public Map<String, String> getLangs() {
        return langs;
    }

    public void setLangs(Map<String, String> langs) {
        this.langs = langs;
    }
}