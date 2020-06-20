package com.thegreentech;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thegreentech.R;

import java.util.ArrayList;

import Fragments.FragmentPhotoRequestReceived;
import Fragments.FragmentPhotoRequestSent;

public class PhotoPasswordRequestActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView textviewHeaderText, textviewSignUp;
    String noty = "";
    public TabLayout tabLayout;
    public TabLayout.Tab tab;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<String> menu_list;
    private final String TAG = PhotoPasswordRequestActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_password_request);

        mViewPager = (ViewPager) findViewById(R.id.container1);
        noty = getIntent().getStringExtra("noti");

        menu_list = new ArrayList<>();
        menu_list.add("PHOTO REQ RECEIVED");
        menu_list.add("PHOTO REQ SENT");


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(1);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textviewHeaderText = (TextView) findViewById(R.id.textviewHeaderText);
        textviewSignUp = (TextView) findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("PHOTO REQUEST");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);


        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   mViewPager.setCurrentItem(tab.getPosition());

                                                   Log.i("Selected_tab_photos", tab.getPosition() + "");
                                               }

                                               @Override
                                               public void onTabUnselected(TabLayout.Tab tab) {
                                                   mViewPager.setCurrentItem(tab.getPosition());

                                               }

                                               @Override
                                               public void onTabReselected(TabLayout.Tab tab) {
                                                   mViewPager.setCurrentItem(tab.getPosition());
                                               }

                                           }
        );

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (noty.equalsIgnoreCase("photo_req")) {
            Intent intent = new Intent(PhotoPasswordRequestActivity.this, FragmentNotification.class);
            startActivity(intent);
            finish();
        }
        else if (noty.equalsIgnoreCase("photo_pass_req"))
        {
            Intent intent = new Intent(PhotoPasswordRequestActivity.this, FragmentNotification.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(PhotoPasswordRequestActivity.this, MainActivity.class);
            intent.putExtra("Fragments", "ProfileEdit");
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return FragmentPhotoRequestReceived.newInstance(position + 1);
                case 1:
                    return FragmentPhotoRequestSent.newInstance(position + 1);

                default:
                    return null;
            }


        }

        @Override
        public int getCount() {
            return menu_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return menu_list.get(position);
        }
    }


}
