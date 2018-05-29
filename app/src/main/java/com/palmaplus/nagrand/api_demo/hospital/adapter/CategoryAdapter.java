package com.palmaplus.nagrand.api_demo.hospital.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;
import com.palmaplus.nagrand.api_demo.hospital.databean.DoctorBean;

/**
 * Created by Administrator on 2018-5-29.
 */

public class CategoryAdapter extends BaseQuickAdapter<CategotyBean, BaseViewHolder> {


    public CategoryAdapter() {
        super(R.layout.item_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategotyBean item) {
        if(item.isSelect()){
            helper.setBackgroundColor(R.id.category_bg , Color.parseColor("#FFFFFF"));
            helper.setTextColor(R.id.item_category_name , Color.parseColor("#307FE2"));
        }else {
            helper.setBackgroundColor(R.id.category_bg , Color.parseColor("#F2F2F2"));
            helper.setTextColor(R.id.item_category_name , Color.parseColor("#333333"));
        }

        helper.setText(R.id.item_category_name ,item.getCategory());


    }
}
