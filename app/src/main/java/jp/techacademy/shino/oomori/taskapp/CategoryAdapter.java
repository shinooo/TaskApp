package jp.techacademy.shino.oomori.taskapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shino on 2017/04/30.
 */

public class CategoryAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Category> mCategoryArrayList;

    public CategoryAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = context;
    }

    public void setCategoryList(ArrayList<Category> categoryArrayList) {
        mCategoryArrayList = categoryArrayList;
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);

        textView1.setText(mCategoryArrayList.get(position).getCategoryName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO : getViewと同じ内容でいいのか？
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);

        textView1.setText(mCategoryArrayList.get(position).getCategoryName());

        return convertView;
    }

}
