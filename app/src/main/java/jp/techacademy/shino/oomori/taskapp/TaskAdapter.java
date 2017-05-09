package jp.techacademy.shino.oomori.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Shino on 2017/04/09.
 */

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Task> mTaskArrayList;
    private Realm realm;
    RealmResults<Category> categoryRealmResults;

    public TaskAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        realm = Realm.getDefaultInstance();
        realm.close();
        categoryRealmResults = realm.where(Category.class).findAllSorted("id");
    }

    public void setTaskList(ArrayList<Task> taskArrayList) {
        mTaskArrayList = taskArrayList;
    }

    // アイテム（データ）の数を返す
    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    // アイテム（データ）を返す
    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    // アイテム（データ）のIDを返す
    @Override
    public long getItemId(int position) {
        return mTaskArrayList.get(position).getId();
    }

    // Viewを返す
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        textView1.setText(mTaskArrayList.get(position).getTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskArrayList.get(position).getDate();
        String category = categoryRealmResults.get(mTaskArrayList.get(position).getCategoryId()).getCategoryName();
        textView2.setText(simpleDateFormat.format(date) + "  " + category);

        return convertView;
    }

}
