package com.palmaplus.nagrand.api_demo.utils;

import com.palmaplus.nagrand.api_demo.R;
import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;
import com.palmaplus.nagrand.api_demo.hospital.databean.DoctorBean;
import com.palmaplus.nagrand.api_demo.hospital.databean.FloorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-5-29.
 */

public class DataUtil {

    public static List<CategotyBean> getCategortDadta(){
        List<CategotyBean> list = new ArrayList<>();

        CategotyBean categotyBean = new CategotyBean();
        categotyBean.setCategory("外科");
        categotyBean.setSelect(true);
        List<String> depart = new ArrayList<>();
        depart.add("乳腺科");
        categotyBean.setDepartment(depart);
        list.add(categotyBean);

        CategotyBean categotyBean2 = new CategotyBean();
        categotyBean2.setCategory("妇产科");
        List<String> depart2 = new ArrayList<>();
        depart2.add("妇科");
        depart2.add("生殖医学科");
        depart2.add("内分泌专题");
        depart2.add("计划生育");
        categotyBean2.setDepartment(depart2);
        list.add(categotyBean2);

        return list;
    }


    public static List<DoctorBean> getDoctorDadta(){
        List<DoctorBean> doctorBeanList = new ArrayList<>();
        DoctorBean doctorBean = new DoctorBean();
        doctorBean.setHeadImg(R.mipmap.doctor1);
        doctorBean.setSelect(true);
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

        return doctorBeanList;
    }

    public static List<FloorBean> getFloorData(){
        List<FloorBean> floorBeans = new ArrayList<>();
        floorBeans.add(new FloorBean("F5",306021L));
        floorBeans.add(new FloorBean("F4",305797L));
        floorBeans.add(new FloorBean("F3",305571L));
        floorBeans.add(new FloorBean("F2",305342L));
        floorBeans.add(new FloorBean("F1",305092L));
        floorBeans.add(new FloorBean("B1",304929L));

        return floorBeans;
    }

    public static String  getFloorName(long floorID){

        String name ="";
        if(floorID == 304929L){
           name = "地下一楼";
        }else if(floorID == 305092L){
            name = "一楼";
        }else if(floorID == 305342L){
            name = "二楼";
        }else if(floorID == 305571L){
            name = "三楼";
        }else if(floorID == 305797L){
            name = "四楼";
        }else if(floorID == 306021L){
            name = "五楼";
        }

        return name;
    }

    public static String getTime(int lbsLength) {

        int min = lbsLength/60;
        int miao = lbsLength%60;

        if (min>0){
            return min+"分"+miao+"秒";
        }else {
            return miao+"秒";
        }
    }
}
