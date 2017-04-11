package jp.techacademy.shino.oomori.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Shino on 2017/04/09.
 */

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<RealmModel> mTaskList;

    public TaskAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskList(List<RealmModel> taskList) {
        mTaskList = taskList;
    }

    // アイテム（データ）の数を返す
    @Override
    public int getCount() {
        return mTaskList.size();
    }

    // アイテム（データ）を返す
    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    // アイテム（データ）のIDを返す
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Viewを返す
    // TODO : Viewを返さないといけない理由
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        // 後でTaskクラスから情報を取得するように変更する
        textView1.setText(mTaskList.get(position));

        return convertView;
    }
}
