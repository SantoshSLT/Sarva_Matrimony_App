package com.thegreentech;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DiscountDialog extends Dialog {

    ImageView ivsrc, ivClose;
    TextView tvCounter;
    String reg_date;
    SimpleDateFormat format;
    //
    CountDownTimer timer;
    Calendar c;
    boolean isads;
    Bitmap bt;

    public DiscountDialog(@NonNull Context context, boolean isads, Bitmap bt) {
        super(context);
        this.isads = isads;
        this.bt = bt;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount_dialog);

        ivClose = findViewById(R.id.ivClose);
        ivsrc = findViewById(R.id.ivsrc);
        tvCounter = findViewById(R.id.tvCounter);
        reg_date = getContext().getSharedPreferences("data", Context.MODE_PRIVATE).getString("join_date", "");
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        ivClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dismiss();
            }
        });
        ivsrc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getContext().startActivity(new Intent(getContext(), UpgradeMembershipPlanActivity.class));
            }
        });


    }

    void getTime() {
        final String str_end = reg_date;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cc = Calendar.getInstance();
            cc.setTime(format.parse(str_end));
            cc.set(Calendar.DAY_OF_MONTH,cc.get(Calendar.DAY_OF_MONTH) + 3);

            final Date end = cc.getTime();
            timer = new CountDownTimer(1000, 1000) {
                public void onTick(long l) {
                }

                public void onFinish() {
                    c = Calendar.getInstance();
                    Date start = new Date(c.getTimeInMillis());
                    if (!printDifference(start, end).equals("Time Out")) {
                        start();
                        tvCounter.setText(printDifference(start, end));
                    } else {
                        dismiss();
                    }
                    Log.d("DFFFF", printDifference(start, end));
                }
            }.start();
        } catch (Exception e) {
            Log.d("ERRRR", e.toString());
        }
    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays <= 0 && elapsedHours <= 0 && elapsedMinutes <= 0 && elapsedSeconds <= 0) {
            return "Time Out";
        } else {

            /*return String.format("%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/
            return String.format("%02d:%02d:%02d:%02d",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        }
    }

    @Override
    public void show() {
        super.show();
        if (!isads) {
            getTime();
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            ivsrc.setImageBitmap(bt);
        }
    }


    @Override
    public void dismiss() {

        if (!isads) {

            if(timer != null)
            {
                timer.cancel();
            }
        }
        super.dismiss();
    }
}
