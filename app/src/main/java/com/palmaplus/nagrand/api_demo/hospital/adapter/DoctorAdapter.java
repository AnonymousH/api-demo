package com.palmaplus.nagrand.api_demo.hospital.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.databean.DoctorBean;

/**
 * Created by Administrator on 2018-5-28.
 */

public class DoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {
    public DoctorAdapter() {
        super(R.layout.item_doctor);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorBean item) {
        helper.setText(R.id.item_doctor_name,item.getName());
        helper.setText(R.id.item_doctor_title,item.getTitle());
        helper.setImageResource(R.id.item_doctor_head , item.getHeadImg());

        if(item.isSelect()){
            helper.setBackgroundColor(R.id.item_doctor_bg , Color.parseColor("#FFFFFF"));
            helper.setTextColor(R.id.item_doctor_name , Color.parseColor("#307FE2"));
        }else {
            helper.setBackgroundColor(R.id.item_doctor_bg , Color.parseColor("#F2F2F2"));
            helper.setTextColor(R.id.item_doctor_name , Color.parseColor("#333333"));
        }
    }
}
