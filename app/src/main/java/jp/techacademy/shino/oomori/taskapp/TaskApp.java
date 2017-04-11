package jp.techacademy.shino.oomori.taskapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Shino on 2017/04/11.
 */

public class TaskApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
