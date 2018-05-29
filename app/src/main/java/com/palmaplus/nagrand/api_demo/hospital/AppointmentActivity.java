package com.palmaplus.nagrand.api_demo.hospital;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.adapter.DoctorAdapter;
import com.palmaplus.nagrand.api_demo.hospital.databean.DoctorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-28.
 */

public class AppointmentActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    DoctorAdapter doctorAdapter;

    ImageView main_head;
    TextView main_name;
    TextView main_good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        initView();

        getData();
    }

    private void getData() {
        List<DoctorBean> doctorBeanList = new ArrayList<>();
        DoctorBean doctorBean = new DoctorBean();
        doctorBean.setHeadImg(R.mipmap.doctor1);
        doctorBean.setName("庄志刚");
        doctorBean.setTitle("主任医师");
        doctorBean.setGood("擅长： 乳腺癌、乳腺肉癌、乳腺良性肿瘤，以及恶性肿瘤的综合治疗");
        doctorBeanList.add(doctorBean);

        DoctorBean doctorBean2 = new DoctorBean();
        doctorBean2.setHeadImg(R.mipmap.doctor2);
        doctorBean2.setName("李正东");
        doctorBean2.setTitle("副主任医师");
        doctorBean2.setGood("擅长： 乳腺良恶性疾病诊治");
        doctorBeanList.add(doctorBean2);

        doctorAdapter.setNewData(doctorBeanList);

        setFirstData(doctorBean);
    }

    private void setFirstData(DoctorBean doctorBean) {
        main_head.setImageResource(doctorBean.getHeadImg());
        main_name.setText(doctorBean.getName());
        main_good.setText(doctorBean.getGood());
    }

    private void initView() {
        main_head = (ImageView) findViewById(R.id.main_head);
        main_name = (TextView) findViewById(R.id.main_name);
        main_good = (TextView) findViewById(R.id.main_good);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        doctorAdapter = new DoctorAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(doctorAdapter);

        doctorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setFirstData(doctorAdapter.getItem(position));
            }
        });
    }
}
