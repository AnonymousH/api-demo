package com.palmaplus.nagrand.api_demo.fragments.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.fragments.map.BaseMapFragment;
import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.easyapi.Map;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.tools.ViewUtils;
import com.palmaplus.nagrand.view.overlay.ImageOverlay;

import java.util.List;

/**
 * Created by jian.feng on 2017/6/5.
 */

public class CategorySearchFragment extends BaseMapFragment {

    @Override
    public void onInitFragment(Bundle savedInstanceState) {
        super.onInitFragment(savedInstanceState);
        // 获取Map对象
        final Map map = mapView.getMap();

        // 获取放置Widget的ViewGroup
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.control_container);
        Button button = new Button(getContext());
        button.setBackgroundResource(R.mipmap.ic_map_pin_elevator);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams( ViewUtils.dip2px(getContext(), 40),  ViewUtils.dip2px(getContext(), 40));
        layoutParams.setMargins(ViewUtils.dip2px(getContext(), 10), 0, 0, ViewUtils.dip2px(getContext(), 10));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        // 添加一个控制公共设施是否显示的按钮
        relativeLayout.addView(button, layoutParams);

        // 获取放置Overlay的ViewGroup
        ViewGroup container = (ViewGroup) view.findViewById(R.id.overlay_container);
        // 设置这个ViewGroup用于放置Overlay
        map.setOverlayContainer(container);
        // 创建一个用于显示公共设施的图片
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_map_pin_elevator);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.removeAllOverlay();
                // 根据category在Facility查找符合条件的Feature
                List<Feature> features = map.mapView().searchFeature("Facility", "category", new Value(24091000l));
                for (Feature feature : features) {
                    // 获取公共设施的点坐标
                    Coordinate centroid = feature.getCentroid();
                    // 创建一个图片的Overlay
                    ImageOverlay imageOverlay = new ImageOverlay(getContext());
                    // 设置Overlay附属的楼层
                    imageOverlay.mFloorId = map.getFloorId();
                    imageOverlay.setImageBitmap(bitmap);
                    // 初始化Overlay的位置
                    imageOverlay.init(new double[] { centroid.getX(), centroid.getY() });
                    // 添加Overlay
                    map.addOverlay(imageOverlay);
                }
            }
        });
    }
}
