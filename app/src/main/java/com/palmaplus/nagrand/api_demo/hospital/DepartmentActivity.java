package com.palmaplus.nagrand.api_demo.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.adapter.CategoryAdapter;
import com.palmaplus.nagrand.api_demo.hospital.adapter.DepartmentAdapter;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;
import com.palmaplus.nagrand.api_demo.utils.DataUtil;
import com.palmaplus.nagrand.api_demo.utils.DateUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-5-29.
 */

public class DepartmentActivity extends AutoLayoutActivity {
    private TextView dateText;

    private RecyclerView categoryRecycler, departmentRecycler;
    private CategoryAdapter categoryAdapter;
    private DepartmentAdapter departmentAdapter;

    private int type; // 1 挂号 2 预约
    ImageView icon_main , icon_last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_department);

        type = getIntent().getIntExtra("type",1);

        initView();

        setData();
    }

    private void setData() {
        List<CategotyBean> categotyBeans = DataUtil.getCategortDadta();
        categoryAdapter.setNewData(categotyBeans);

        departmentAdapter.setNewData(categotyBeans.get(0).getDepartment());
    }

    private void initView() {
        icon_main  = (ImageView) findViewById(R.id.icon_main);
        icon_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(DepartmentActivity.this, HospitalMainActivity.class);
                startActivity(chatIntent);
            }
        });
        icon_last = (ImageView) findViewById(R.id.icon_last);
        icon_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dateText = (TextView) findViewById(R.id.date_text);
        Date date = new Date(System.currentTimeMillis());
        dateText.setText(DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY年MM月DD日) + " " + DateUtil.getDayWeekOfDate1(date));

        categoryRecycler = (RecyclerView) findViewById(R.id.recycler1);
        categoryAdapter = new CategoryAdapter();
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        categoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<CategotyBean> categotyBeans = categoryAdapter.getData();
                for (int i = 0; i < categotyBeans.size(); i++) {
                    CategotyBean categotyBean = categotyBeans.get(i);
                    if(i==position){
                        categotyBean.setSelect(true);
                    }else {
                        categotyBean.setSelect(false);
                    }
                }
                categoryAdapter.notifyDataSetChanged();

                departmentAdapter.setNewData(categotyBeans.get(position).getDepartment());
            }
        });


        departmentRecycler = (RecyclerView) findViewById(R.id.recycler2);
        departmentAdapter = new DepartmentAdapter();
        departmentRecycler.setLayoutManager(new GridLayoutManager(this ,3));
        departmentRecycler.setAdapter(departmentAdapter);

        departmentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent appoint = new Intent(DepartmentActivity.this, AppointmentActivity.class);
                appoint.putExtra("type",type);
                startActivity(appoint);
            }
        });


    }
}
