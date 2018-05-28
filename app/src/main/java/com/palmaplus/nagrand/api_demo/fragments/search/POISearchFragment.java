package com.palmaplus.nagrand.api_demo.fragments.search;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.DataUtil;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.view.adapter.DataListAdapter;

/**
 * Created by jian.feng on 2017/6/2.
 */

public class POISearchFragment extends BaseMapFragment {

    private DataListAdapter<LocationModel> dataListAdapter;
    private ListView listView;
    private long selectId;

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();
        // 获取放置Widget的ViewGroup
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.control_container);
        // 将控件放入ViewGroup中
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.poi_search, relativeLayout);

        listView = (ListView) inflate.findViewById(R.id.search_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map.mapView().resetOriginStyle("Area", selectId);
                listView.setVisibility(View.GONE);
                LocationModel item = dataListAdapter.getItem(position);
                // 获取选中LocationModel的id
                selectId = LocationModel.id.get(item);
                // 根据id查找对应的Feature
                Feature feature = map.mapView().selectFeature(selectId);
                // 根据id设置Area层对应的Feature的显示颜色
                map.mapView().setRenderableColor("Area", selectId, Color.BLUE);
                // 移动地图到对应的Feature
                map.mapView().moveToFeature(feature, true, 1000);
                Toast.makeText(
                        getContext(),
                        String.format("%s -- %s -- %s", LocationModel.id.get(item) + "", LocationModel.display.get(item), LocationModel.address.get(item) == null ? "" : LocationModel.address.get(item)),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        EditText editText = (EditText) inflate.findViewById(R.id.search_edit_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // 根据关键字搜索
                    map.dataSource().search(v.getText().toString(), 0, 10, new long[] { map.getFloorId() }, null, new DataSource.OnRequestDataEventListener<LocationPagingList>() {
                        @Override
                        public void onRequestDataEvent(DataSource.ResourceState resourceState, LocationPagingList locationPagingList) {
                            if (resourceState != DataSource.ResourceState.OK && resourceState != DataSource.ResourceState.CACHE) {
                                return;
                            }
                            // 设置数据
                            searchResult(locationPagingList);
                        }
                    });
                }
                return false;
            }
        });
    }

    private void searchResult(LocationPagingList locationPagingList) {
        listView.setVisibility(View.VISIBLE);
        if (dataListAdapter == null) {
            dataListAdapter = new DataListAdapter<LocationModel>(getContext(), 0, locationPagingList) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView text;
                    text = new TextView(this.getContext());
                    // 获取当前的LocationMode信息
                    LocationModel item = this.getItem(position);
                    // 获取需要高亮的字段
                    String[] keys = DataUtil.highLight(item);
                    // 获取需要显示的名称
                    String s = LocationModel.display.get(item);
                    // 在显示名称中查找需要高亮字段，并返回他们的索引值
                    int[] offset = DataUtil.getOffset(s, keys);
                    Spannable spannable = Spannable.Factory.getInstance().newSpannable(s);
                    for (int j = 0; j < offset.length; j += 2) {
                        // 高亮
                        spannable.setSpan(new BackgroundColorSpan(0xFFFFFF00), offset[j], offset[j + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    text.setText(spannable);
                    return text;
                }
            };
            listView.setAdapter(dataListAdapter);
        } else {
            dataListAdapter.setDataList(locationPagingList);
            dataListAdapter.notifyDataSetChanged();
        }
    }
}
