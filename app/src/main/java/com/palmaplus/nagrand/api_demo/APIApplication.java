package com.palmaplus.nagrand.api_demo;

import android.app.Application;

import com.palmaplus.nagrand.api_demo.utils.Constants;
import com.palmaplus.nagrand.api_demo.utils.FileUtilsTools;
import com.palmaplus.nagrand.core.Engine;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by jian.feng on 2017/5/31.
 */

public class APIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        CrashReport.initCrashReport(this, "c53b8df309", false);

        // copy assets Nagrand/lua to SD Card Nagrand/lua folder
        FileUtilsTools.copyDirToSDCardFromAsserts(this, "Nagrand/lua", "font");
        FileUtilsTools.copyDirToSDCardFromAsserts(this, "Nagrand/lua", "Nagrand/lua");

        // init Engine
        Engine instance = Engine.getInstance();
        instance.startWithLicense(Constants.AppKey, this);

    }
}
