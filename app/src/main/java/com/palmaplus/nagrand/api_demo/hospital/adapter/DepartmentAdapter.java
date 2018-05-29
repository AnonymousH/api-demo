package com.palmaplus.nagrand.api_demo.hospital.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;

/**
 * Created by Administrator on 2018-5-29.
 */

public class DepartmentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public DepartmentAdapter() {
        super(R.layout.item_department);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_depart_name ,item);
    }
}
