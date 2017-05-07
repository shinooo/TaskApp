package jp.techacademy.shino.oomori.taskapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditCategory extends AppCompatActivity {

    private EditText InputCategory;
    private Category mCategory;

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editCategory();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Spinner testSpinner = (Spinner) findViewById(R.id.test_spinner);
        String spinnerItems[] = {"Spinner","Spinner1","Spinner2","Spinner3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        testSpinner.setAdapter(adapter);


        /*
        // ActionBarを設定する
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        */

//        findViewById(R.id.add_category_button).setOnClickListener(mOnDoneClickListener);
//        InputCategory = (EditText)findViewById(R.id.category_text);

    }

    private void editCategory(){
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

        String category = InputCategory.getText().toString();

        mCategory.setCategoryName(category);

        realm.copyToRealmOrUpdate(mCategory);
        realm.commitTransaction();

        realm.close();

    }
}
