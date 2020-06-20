package com.thegreentech;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

public class DialogNotiFicationClear  extends  Dialog  implements DialogInterface.OnDismissListener {


    Activity mContext;
    TextView btncancel,btnOk;

    public DialogNotiFicationClear( Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.notification_clear_layout);
        btncancel = findViewById(R.id.btncancel);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                dismiss();

            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        clear();

    }


    public void clear() {
        mContext = null;
    }

}
