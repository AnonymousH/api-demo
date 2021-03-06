package com.palmaplus.nagrand.api_demo.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018-5-27.
 */

public class JsonUtil {

    static String ttt = "\n" +
            "{\"init\":[{\"浪琴\":\"1.3370434291925892E7, 3542229.86390874\"},\n" +
            "{\"通灵珠宝\":\"1.3370451969896778E7, 3542232.2930621826\"},\n" +
            "{\"古名\":\"1.3370456333574757E7, 3542232.051210376\"},\n" +
            "{\"中国黄金\":\"1.3370461299425587E7, 3542230.518815967\"},\n" +
            "{\"天梭\":\"1.3370428658556446E7, 3542225.1971454346\"},\n" +
            "{\"曼卡龙珠宝\":\"1.337044154156731E7, 3542221.2159595215\"},\n" +
            "{\"皇室太古\":\"1.3370451623629078E7, 3542222.724245044\"},\n" +
            "{\"明牌\":\"1.3370446749758229E7, 3542218.3605060303\"},\n" +
            "{\"老凤祥\":\"1.3370433444574818E7, 3542216.700456592\"},\n" +
            "{\"周大福\":\"1.3370462303743824E7, 3542221.178201648\"},\n" +
            "{\"潮宏基\":\"1.337046163519524E7, 3542207.42968938\"},\n" +
            "{\"石风轩\":\"1.33704632602105E7, 3542198.2695865356\"},\n" +
            "{\"佰草集\":\"1.3370438672945485E7, 3542197.772745105\"},\n" +
            "{\"鸳鸯\":\"1.3370398782930836E7, 3542196.0001468384\"},\n" +
            "{\"卡西欧\":\"1.3370402806978688E7, 3542187.5985355102\"},\n" +
            "{\"雪花秀\":\"1.3370443628252491E7, 3542190.9100894653\"},\n" +
            "{\"翡翠物语\":\"1.3370459241014943E7, 3542191.9877490723\"},\n" +
            "{\"宝岛眼镜\":\"1.3370453058977589E7, 3542186.735116931\"},\n" +
            "{\"时尚表\":\"1.3370446095324025E7, 3542182.882318469\"},\n" +
            "{\"自然堂\":\"1.337043091695641E7, 3542177.85421178\"},\n" +
            "{\"欧舒丹\":\"1.3370391408777699E7, 3542178.9703693115\"},\n" +
            "{\"雅诗兰黛\":\"1.3370392548822865E7, 3542155.755945178\"},\n" +
            "{\"资生堂\":\"1.3370402132013783E7, 3542157.420492145\"},\n" +
            "{\"卡诗\":\"1.3370411511057362E7, 3542155.6614246094\"},\n" +
            "{\"王品台塑牛排\":\"1.3370437680765614E7, 3542080.839216205\"},\n" +
            "{\"满记甜品\":\"1.3370403752596363E7, 3542044.383089038\"},\n" +
            "{\"卡洛意大利餐厅\":\"1.3370423303766713E7, 3542046.7195758545\"},\n" +
            "{\"香水\":\"1.3370409918825611E7, 3542191.8784198486\"},\n" +
            "{\"燕格格\":\"1.3370349312777027E7, 3542167.801401111\"},\n" +
            "{\"星巴克二店\":\"1.3370349312777027E7, 3542167.801401111\"},\n" +
            "{\"拉亚汉堡\":\"1.337035520015572E7, 3542036.5176105225\"},\n" +
            "{\"惠人芳\":\"1.33703322570443E7, 3542062.2842158997\"},\n" +
            "{\"手表\":\"1.3370310951570973E7, 3542117.782144519\"},\n" +
            "{\"三星体验店\":\"1.3370277630957112E7, 3542152.2320613586\"},\n" +
            "{\"悦诗风吟\":\"1.33702807517495E7, 3542126.429773303\"},\n" +
            "{\"海马体照相馆\":\"1.3370319044722065E7, 3542034.5667667114\"},\n" +
            "{\"香圃 \":\"1.3370310213705525E7, 3542061.2589015686\"},\n" +
            "{\"保氏酸奶\":\"1.337030185739468E7, 3542038.1808032715\"},\n" +
            "{\"海洋生活\":\"1.3370279515184864E7, 3542057.2746791565\"},\n" +
            "{\"丝芙兰\":\"1.3370292532228932E7, 3542097.351048442\"},\n" +
            "{\"银泰置地\":\"1.3370260294893727E7, 3542110.2011918747\"},\n" +
            "{\"芳香市集\":\"1.3370257517141804E7, 3542082.400600406\"},\n" +
            "{\"哈根达斯\":\"1.3370243741846547E7, 3542052.096155139\"},\n" +
            "{\"潘多拉\":\"1.3370210254869923E7, 3542087.0831088745\"},\n" +
            "{\"小美汇美甲美瞳护理\":\"1.337022680341576E7, 3542048.4036502563\"},\n" +
            "{\"翠华餐厅\":\"1.3370184320215687E7, 3541995.842882129\"},\n" +
            "{\"小勇士\":\"1.3370201899637684E7, 3542023.3537845337\"},\n" +
            "{\"蜜果\":\"1.3370210162966236E7, 3542005.5164661133\"},\n" +
            "{\"赛百味\":\"1.3370213422228321E7, 3541997.847795459\"},\n" +
            "{\"贡茶\":\"1.337021813783501E7, 3541993.0709094726\"},\n" +
            "{\"单农\":\"1.3370160764498219E7, 3542089.8597125732\"},\n" +
            "{\"星巴克一店\":\"1.3370133858461842E7, 3542084.446877452\"},\n" +
            "{\"碧桂园西江月\":\"1.337019812814568E7, 3542147.594415637\"},\n" +
            "{\"华硕体验店\":\"1.337019812814568E7, 3542147.594415637\"}]}";

    public static Map<String,double[]> jsonToMap()throws Exception{
        Map<String,double[]> rtn = new HashMap<>();
        JSONArray kvs = (JSONArray)new JSONObject(ttt).get("init");
        for(int i = 0;i < kvs.length();++i){
            JSONObject kv = (JSONObject)kvs.get(i);
            Iterator ks = kv.keys();
            String k = (String)ks.next();
            String v = (String)kv.get(k);
            String[] xy = v.split(",");
            double x = Double.valueOf(xy[0]);
            double y = Double.valueOf(xy[1]);
            rtn.put(k.trim(),new double[]{x,y});
        }
        return rtn;
    }

}
