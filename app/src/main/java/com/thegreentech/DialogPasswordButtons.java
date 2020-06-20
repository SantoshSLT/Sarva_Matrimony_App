package com.thegreentech;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.thegreentech.R;

public class DialogPasswordButtons extends Dialog implements DialogInterface.OnDismissListener
{
    Activity mContext;
    ImageView imgProfileImage;

    Button btnPhotoPassword, btnPhotoPasswordRequest;

    SharedPreferences prefUpdate;
    ProgressDialog progresDialog;
    public static String matri_id,login_matri_id;

    public DialogPasswordButtons(Activity context, String matri_id, ImageView imgProfileImage) {
        super(context);
        this.mContext = context;
        this.matri_id=matri_id;
        this.imgProfileImage=imgProfileImage;

        prefUpdate= PreferenceManager.getDefaultSharedPreferences(mContext);
        login_matri_id=prefUpdate.getString("matri_id","");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_buttons);

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


        btnPhotoPassword = (Button) findViewById(R.id.btnPhotoPassword);
        btnPhotoPasswordRequest = (Button) findViewById(R.id.btnPhotoPasswordRequest);


     /*   btnPhotoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                final DialogPhotoPassword dgnew = new DialogPhotoPassword(mContext,matri_id,imgProfileImage,Photo);
                dgnew.setCancelable(true);
                dgnew.setCanceledOnTouchOutside(true);
                dgnew.show();

            }
        });

        btnPhotoPasswordRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
                final DialogPhotoRequestPassword dgnew = new DialogPhotoRequestPassword(mContext,matri_id,imgProfileImage);
                dgnew.setCancelable(true);
                dgnew.setCanceledOnTouchOutside(true);
                dgnew.show();
            }
        });

*/
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        clear();
    }

    public void clear() {
        mContext = null;
    }



}


