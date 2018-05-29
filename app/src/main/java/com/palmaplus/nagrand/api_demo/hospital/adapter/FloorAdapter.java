package com.palmaplus.nagrand.api_demo.hospital.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;
import com.palmaplus.nagrand.api_demo.hospital.databean.FloorBean;

import java.util.List;

/**
 * Created by Administrator on 2018-5-29.
 */

public class FloorAdapter extends BaseQuickAdapter<FloorBean, BaseViewHolder> {
    public FloorAdapter() {
        super(R.layout.item_floor);
    }

    @Override
    protected void convert(BaseViewHolder helper, FloorBean item) {
        helper.setText(R.id.item_floor_txt,item.getFloorName());
    }
}
