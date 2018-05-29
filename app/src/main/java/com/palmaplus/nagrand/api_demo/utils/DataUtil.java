package com.palmaplus.nagrand.api_demo.utils;

import com.palmaplus.nagrand.api_demo.hospital.databean.CategotyBean;

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

}
