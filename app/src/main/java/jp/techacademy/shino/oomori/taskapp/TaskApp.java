package jp.techacademy.shino.oomori.taskapp;

import android.app.Application;

import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Shino on 2017/04/11.
 */

public class TaskApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        Realm realm = Realm.getDefaultInstance();
        RealmResults categoryRealmResults = realm.where(Category.class).findAll();
        RealmResults taskRealmResults = realm.where(Task.class).findAll();

        if (categoryRealmResults.isEmpty()) {
            realm.beginTransaction();
            // 新規作成の場合
            Category category = realm.createObject(Category.class,0);
            category.setCategoryName("Default");
            realm.commitTransaction();
        }

        realm.close();
    }
}
