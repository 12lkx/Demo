package com.example.myapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public SpinnerAdapter(Context pContext, List<String> pList){
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater= LayoutInflater.from(mContext);
        view = _LayoutInflater.inflate(R.layout.item,null);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mList.get(i));
        return view;
    }
}
