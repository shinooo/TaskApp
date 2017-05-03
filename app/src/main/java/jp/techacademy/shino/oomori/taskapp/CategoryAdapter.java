package jp.techacademy.shino.oomori.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Shino on 2017/04/30.
 */

public class CategoryAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Task> mCategoryArrayList;

    public CategoryAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskList(ArrayList<Task> taskArrayList) {
        mCategoryArrayList = taskArrayList;
    }

    // アイテム（データ）の数を返す
    @Override
    public int getCount() {
        return mCategoryArrayList.size();
    }

    // アイテム（データ）を返す
    @Override
    public Object getItem(int position) {
        return mCategoryArrayList.get(position);
    }

    // アイテム（データ）のIDを返す
    @Override
    public long getItemId(int position) {
        return mCategoryArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


}
