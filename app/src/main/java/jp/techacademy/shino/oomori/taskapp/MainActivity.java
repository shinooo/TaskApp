package jp.techacademy.shino.oomori.taskapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_TASK = "jp.techacademy.shino.oomori.taskapp.TASK";

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView(selectedCategoryID);
            setCategorySpinner();
        }
    };

    private ListView mListView;
    private TaskAdapter mTaskAdapter;
    private Spinner categorySpinner;
    private CategoryAdapter categoryAdapter;
    private int selectedCategoryID = 0;

    private View.OnClickListener mOnCreateCategoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, EditCategory.class);
            startActivity(intent);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        findViewById(R.id.input_category_button).setOnClickListener(mOnCreateCategoryClickListener);

        //FloatingActionButtonをタップした時の処理
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });

        // Realmの設定
        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(mRealmListener);

        // ListViewの設定
        mTaskAdapter = new TaskAdapter(MainActivity.this);
        mListView = (ListView) findViewById(R.id.listView1);

        // ListViewをタップしたときの処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                Task task = (Task) parent.getAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                intent.putExtra(EXTRA_TASK, task.getId());

                startActivity(intent);
            }
        });

        // ListViewを長押ししたときの処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // タスクを削除する
                final Task task = (Task) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("削除");
                builder.setMessage(task.getTitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Task> results = mRealm.where(Task.class).equalTo("id", task.getId()).findAll();

                        mRealm.beginTransaction();
                        results.deleteAllFromRealm();
                        mRealm.commitTransaction();

                        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class);
                        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                MainActivity.this,
                                task.getId(),
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(resultPendingIntent);

                        reloadListView(selectedCategoryID);
                    }
                });
                builder.setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        setCategorySpinner();

            selectedCategoryID = 0;

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    Category selectedCategory = (Category) categoryAdapter.getItem(position);
                    selectedCategoryID = selectedCategory.getId();
                    reloadListView(selectedCategoryID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                    selectedCategoryID = 0;
                    reloadListView(selectedCategoryID);
                }
            });

        reloadListView(selectedCategoryID);
    }

    private void setCategorySpinner() {
        RealmResults<Category> categoryRealmResults;
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        categoryRealmResults = mRealm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
        categoryAdapter = new CategoryAdapter(MainActivity.this);
        categoryAdapter.setCategoryList((ArrayList<Category>) mRealm.copyFromRealm(categoryRealmResults));
        categorySpinner.setAdapter(categoryAdapter);
    }

    //ListViewの更新
    private void reloadListView(int selectCategoryId) {
        RealmResults<Task> taskRealmResults;
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        if(selectCategoryId == 0) {
            taskRealmResults = mRealm.where(Task.class).findAllSorted("date", Sort.DESCENDING);
        }else {
            taskRealmResults = mRealm.where(Task.class).equalTo("categoryID", selectCategoryId).findAllSorted("date", Sort.DESCENDING);
        }
        // 上記の結果を、TaskList としてセットする
        mTaskAdapter.setTaskList((ArrayList<Task>) mRealm.copyFromRealm(taskRealmResults));
        // TaskのListView用のアダプタに渡す
        mListView.setAdapter(mTaskAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTaskAdapter.notifyDataSetChanged();
    }

    //Realmの後処理
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
