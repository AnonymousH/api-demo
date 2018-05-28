package com.palmaplus.nagrand.api_demo.data;

import com.palmaplus.nagrand.api_demo.fragments.control.CompassFragment;
import com.palmaplus.nagrand.api_demo.fragments.control.FloorControlFragment;
import com.palmaplus.nagrand.api_demo.fragments.control.SwitchControlFragment;
import com.palmaplus.nagrand.api_demo.fragments.control.ZoomControlFragment;
import com.palmaplus.nagrand.api_demo.fragments.dynamic.DynamicNaviRecalcFragment;
import com.palmaplus.nagrand.api_demo.fragments.dynamic.DynamicnaviFragment;
import com.palmaplus.nagrand.api_demo.fragments.event.SingleEventFragment;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.api_demo.fragments.map.MapFloorChangeFragment;
import com.palmaplus.nagrand.api_demo.fragments.map.MapOperationFragment;
import com.palmaplus.nagrand.api_demo.fragments.map.MapPositionFragment;
import com.palmaplus.nagrand.api_demo.fragments.navi.NaviLengthFragment;
import com.palmaplus.nagrand.api_demo.fragments.navi.NaviTextFragment;
import com.palmaplus.nagrand.api_demo.fragments.navi.StartEndNaviFragment;
import com.palmaplus.nagrand.api_demo.fragments.overlay.ImageOverlayFragment;
import com.palmaplus.nagrand.api_demo.fragments.overlay.PositionOverlayFragment;
import com.palmaplus.nagrand.api_demo.fragments.overlay.TextOverlayFragment;
import com.palmaplus.nagrand.api_demo.fragments.search.CategorySearchFragment;
import com.palmaplus.nagrand.api_demo.fragments.search.POISearchFragment;
import com.palmaplus.nagrand.api_demo.model.FunctionItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jian.feng on 2017/6/1.
 */

public class FunctionListDataProvider {

    public static Map<FunctionItem, List<FunctionItem>> provideFunctionList() {
        return new LinkedHashMap<FunctionItem, List<FunctionItem>>() {
            {
                FunctionItem group = new FunctionItem("地图显示", null);
                List<FunctionItem> items = new ArrayList<>();
                items.add(new FunctionItem("地图基本显示", BaseMapFragment.class));
                items.add(new FunctionItem("地图初始化位置", MapPositionFragment.class));
                items.add(new FunctionItem("地图操作", MapOperationFragment.class));
                items.add(new FunctionItem("切换楼层", MapFloorChangeFragment.class));
                put(group, items);

                group = new FunctionItem("搜索", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("poi搜索", POISearchFragment.class));
                items.add(new FunctionItem("分类别搜索", CategorySearchFragment.class));
                put(group, items);

                group = new FunctionItem("控件", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("缩放控件", ZoomControlFragment.class));
                items.add(new FunctionItem("指北针", CompassFragment.class));
                items.add(new FunctionItem("2D/3D切换", SwitchControlFragment.class));
                items.add(new FunctionItem("楼层切换控件", FloorControlFragment.class));
                put(group, items);

                group = new FunctionItem("覆盖物", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("文字覆盖物", TextOverlayFragment.class));
                items.add(new FunctionItem("图片覆盖物", ImageOverlayFragment.class));
                items.add(new FunctionItem("定位点", PositionOverlayFragment.class));
                put(group, items);

                group = new FunctionItem("事件", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("单击事件", SingleEventFragment.class));
                put(group, items);

                group = new FunctionItem("路径规划", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("起终点规划", StartEndNaviFragment.class));
                items.add(new FunctionItem("路径文字提醒", NaviTextFragment.class));
                items.add(new FunctionItem("路径长度计算", NaviLengthFragment.class));
                put(group, items);

                group = new FunctionItem("导航", null);
                items = new ArrayList<>();
                items.add(new FunctionItem("动态导航", DynamicnaviFragment.class));
                items.add(new FunctionItem("路网吸附和重新规划", DynamicNaviRecalcFragment.class));
                put(group, items);
            }
        };
    }
}
