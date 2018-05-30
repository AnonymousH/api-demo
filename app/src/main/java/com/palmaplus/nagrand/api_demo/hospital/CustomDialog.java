package com.palmaplus.nagrand.api_demo.hospital;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.palmaplus.nagrand.api_demo.R;

/**
 * Created by Administrator on 2018-5-29.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
    }
}
