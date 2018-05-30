package com.palmaplus.nagrand.api_demo.hospital;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.adapter.DoctorAdapter;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;
import com.palmaplus.nagrand.api_demo.hospital.databean.DoctorBean;
import com.palmaplus.nagrand.api_demo.utils.DataUtil;
import com.palmaplus.nagrand.api_demo.utils.PopWindowUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-28.
 */

public class AppointmentActivity extends AutoLayoutActivity {


    RecyclerView recyclerView;

    DoctorAdapter doctorAdapter;

    ImageView icon_main , icon_last;
    ImageView main_head;
    TextView main_name;
    TextView main_good;
    LinearLayout appoint_layout;
    private PopupWindow popupWindow;
    LinearLayout date_select_layout;
    TextView afternoon , morning;

    private int type; // 1 挂号 2 预约

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        type = getIntent().getIntExtra("type",1);

        initView();

        getData();
    }

    private void getData() {
        List<DoctorBean> doctorBeanList = DataUtil.getDoctorDadta();
        doctorAdapter.setNewData(doctorBeanList);

        setFirstData(doctorBeanList.get(0));
    }

    private void setFirstData(DoctorBean doctorBean) {
        main_head.setImageResource(doctorBean.getHeadImg());
        main_name.setText(doctorBean.getName());
        main_good.setText(doctorBean.getGood());
    }

    private void initView() {
        appoint_layout  = (LinearLayout) findViewById(R.id.appoint_layout);
        date_select_layout  = (LinearLayout) findViewById(R.id.date_select_layout);
        date_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPop();
            }
        });
        icon_main  = (ImageView) findViewById(R.id.icon_main);
        icon_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(AppointmentActivity.this, HospitalMainActivity.class);
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
        main_head = (ImageView) findViewById(R.id.main_head);
        main_name = (TextView) findViewById(R.id.main_name);
        main_good = (TextView) findViewById(R.id.main_good);
        afternoon  = (TextView) findViewById(R.id.afternoon);
        morning = (TextView) findViewById(R.id.morning);

        if(type ==1){
            morning.setText("挂号");
            afternoon.setText("挂号");
            appoint_layout.setVisibility(View.GONE);
        }else {
            morning.setText("预约");
            afternoon.setText("预约");
            appoint_layout.setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        doctorAdapter = new DoctorAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(doctorAdapter);

        doctorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                List<DoctorBean> doctorBeanList = doctorAdapter.getData();
                for (int i = 0; i < doctorBeanList.size(); i++) {
                    DoctorBean categotyBean = doctorBeanList.get(i);
                    if(i==position){
                        categotyBean.setSelect(true);
                    }else {
                        categotyBean.setSelect(false);
                    }
                }
                doctorAdapter.notifyDataSetChanged();

                setFirstData(doctorAdapter.getItem(position));
            }
        });
    }

    private void showPop() {
        View view = LayoutInflater.from(AppointmentActivity.this).inflate(
                R.layout.layout_pop_item, null);

        PopWindowUtil.getInstance().makePopupWindow(this,date_select_layout,view , Color.parseColor("0x00ffffff"));

    }
}
