package com.palmaplus.nagrand.api_demo.fragments.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.base.BaseFragment;
import com.palmaplus.nagrand.api_demo.utils.Constants;
import com.palmaplus.nagrand.view.MapView;

/**
 * Created by jian.feng on 2017/6/2.
 */

public class BaseMapFragment extends BaseFragment {

    protected MapView mapView;

    protected Button start_reconized;

    @Override
    public View provideView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_map, container, false);
        start_reconized = (Button) view.findViewById(R.id.start_reconized);

        return view;
    }

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取MapView
        mapView = (MapView) view.findViewById(R.id.mapView);
        // 通过MapView获取Map对象，并且根据MapID渲染地图
        mapView.getMap().startWithMapID(Constants.mapId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁地图
        mapView.drop();
    }
}
