package com.palmaplus.nagrand.api_demo.hospital;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.adapter.CategoryAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_department);

        initView();

        setData();
    }

    private void setData() {
        List<CategotyBean> categotyBeans = DataUtil.getCategortDadta();
        categoryAdapter.setNewData(categotyBeans);

    }

    private void initView() {

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
            }
        });


        departmentRecycler = (RecyclerView) findViewById(R.id.recycler2);

    }
}
