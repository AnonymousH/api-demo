package com.palmaplus.nagrand.api_demo.widgets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jian.feng on 2017/5/31.
 */

public abstract class GroupAdapter<T> extends BaseAdapter {

    private final int groupResId;
    private final int itemResId;
    private final Context context;
    private Map<T, List<T>> mapping;
    private List<T> objs;
    private List<T> keys;
//    private SparseArray<T> keys;

    public GroupAdapter(Context context, Map<T, List<T>> mapping, int groupResId, int itemResId) {
        this.context = context;
        this.mapping = mapping;
        this.groupResId = groupResId;
        this.itemResId = itemResId;
        objs = new ArrayList();
        keys = new ArrayList();
        fresh();
    }

    public void setMapping(Map<T, List<T>> mapping) {
        this.mapping = mapping;
        this.fresh();
    }

    public Map<T, List<T>> getMapping() {
        return mapping;
    }

    private void fresh() {
        keys.clear();
        objs.clear();
//        keys = new SparseArray<>();
        for (Map.Entry<T, List<T>> entry : mapping.entrySet()) {
//            keys.put(objs.size(), entry.getKey());
            keys.add(entry.getKey());
            objs.add(entry.getKey());
            objs.addAll(entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return objs.size();
    }

    @Override
    public T getItem(int position) {
        return objs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        T key = getItem(position);
        return !keys.contains(key);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        T key = getItem(position);
        if (keys.contains(key)) {
            view = LayoutInflater.from(context).inflate(groupResId, null);
            bindGroup(key, view);
        } else {
            view = LayoutInflater.from(context).inflate(itemResId, null);
            bindView(getItem(position), view);
        }
        return view;
    }

    public abstract void bindView(T t, View view);
    public abstract void bindGroup(T s, View view);
}
