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
        RealmConfiguration config = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Category category = realm.createObject(Category.class, 0);
                        category.setCategoryName("Default");
                    }
                })
                // アプリのアンインストールの手間が省けるけど、公開するときにそのままだと危険！！！
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
