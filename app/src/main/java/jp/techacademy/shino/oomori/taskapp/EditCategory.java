package jp.techacademy.shino.oomori.taskapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class EditCategory extends AppCompatActivity {

    private EditText InputCategory;
 //   private Category mCategory;

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    private ListView mListView;
    private CategoryAdapter categoryAdapter;
    private int selectedCategoryID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        //FloatingActionButtonをタップした時の処理
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Category add
                // テキスト入力用Viewの作成
                final EditText editView = new EditText(EditCategory.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditCategory.this);

                dialog.setTitle("カテゴリー名を入力してください");
                dialog.setView(editView);

                // OKボタンの設定
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OKボタンをタップした時の処理をここに記述
                        editCategory(null, editView.getText().toString());
                    }
                });

                // キャンセルボタンの設定
                dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // キャンセルボタンをタップした時の処理をここに記述
                    }
                });
                dialog.show();
            }
        });

        // Realmの設定
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);

        // ListViewの設定
        categoryAdapter = new CategoryAdapter(EditCategory.this);
        mListView = (ListView) findViewById(R.id.listView1);

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する
                // テキスト入力用Viewの作成
                final Category target = (Category) parent.getAdapter().getItem(position);
                final EditText editView = new EditText(EditCategory.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditCategory.this);

                dialog.setTitle("カテゴリー名を入力してください");
                dialog.setView(editView);

                // OKボタンの設定
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OKボタンをタップした時の処理をここに記述
                        editCategory(target, editView.getText().toString());
                    }
                });

                // キャンセルボタンの設定
                dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // キャンセルボタンをタップした時の処理をここに記述
                    }
                });
                dialog.show();
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する
                final Category category = (Category) parent.getAdapter().getItem(position);

                if(position != 0) {
                    // ダイアログを表示する
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCategory.this);

                    builder.setTitle("削除");
                    builder.setMessage(category.getCategoryName() + "を削除しますか");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RealmResults<Category> results = mRealm.where(Category.class).equalTo("id", category.getId()).findAll();

                            mRealm.beginTransaction();
                            // 削除するカテゴリーを持つタスクを更新
                            RealmResults<Task> targets = mRealm.where(Task.class).equalTo("categoryID", category.getId()).findAll();
                            for (Task targetTask : targets) {
                                targetTask.setCategoryId(0);
                            }

                            results.deleteAllFromRealm();
                            mRealm.commitTransaction();

                            reloadListView();
                        }
                    });
                    builder.setNegativeButton("CANCEL", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                return true;
            }
        });

        reloadListView();
    }

    //ListViewの更新
    private void reloadListView() {
        RealmResults<Category> categoryRealmResults;
        categoryRealmResults = mRealm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
        categoryAdapter.setCategoryList((ArrayList<Category>) mRealm.copyFromRealm(categoryRealmResults));
        mListView.setAdapter(categoryAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        categoryAdapter.notifyDataSetChanged();
    }

    //Realmの後処理
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    private void editCategory(Category mCategory, String categoryName){
        // Categoryの追加・更新
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (mCategory == null) {
            // 新規作成の場合
            mCategory = new Category();

            RealmResults<Category> categoryRealmResults = realm.where(Category.class).findAll();

            int identifier;
            if (categoryRealmResults.max("id") != null) {
                identifier = categoryRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mCategory.setId(identifier);
        }
        mCategory.setCategoryName(categoryName);

        realm.copyToRealmOrUpdate(mCategory);
        realm.commitTransaction();

        realm.close();
    }
}
