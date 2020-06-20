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

import Fragments.FragmentSettingBlockMembers;
import Fragments.FragmentSettingChangePassword;
import Fragments.FragmentSettingContact;
import Fragments.FragmentSettingPhotos;

public class SettingActivity extends AppCompatActivity   {

    ImageView btnBack;
    TextView textviewHeaderText,textviewSignUp;

    public TabLayout tabLayout;
    public TabLayout.Tab tab;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    //private ArrayList<String> menu_list;

    private ViewPager mViewPager;
   /* private int[] userTabIcons =
    {
            R.drawable.ic_photos,

            R.drawable.ic_contact_us1,
            R.drawable.ic_settings1,
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack=(ImageView)findViewById(R.id.btnBack);
        textviewHeaderText=(TextView)findViewById(R.id.textviewHeaderText);
        textviewSignUp=(TextView)findViewById(R.id.textviewSignUp);

        textviewHeaderText.setText("SETTINGS");
        btnBack.setVisibility(View.VISIBLE);
        textviewSignUp.setVisibility(View.GONE);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container1);

//        menu_list = new ArrayList<>();
//        menu_list.add("Menu");
//        menu_list.add("Home");
//        menu_list.add("Search");
//        menu_list.add("Message");


        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

         //setupUserTabIcons(tabLayout);


        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager)
        {
                @Override
                public void onTabSelected(TabLayout.Tab tab)
                {
                    mViewPager.setCurrentItem(tab.getPosition());

                   Log.i("Selected_tab_Setting",tab.getPosition()+"");
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab)
                {
                    mViewPager.setCurrentItem(tab.getPosition());//

                }
                @Override
                public void onTabReselected(TabLayout.Tab tab)
                {
                    mViewPager.setCurrentItem(tab.getPosition());
                }

            }
        );

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("Fragments", "ProfileEdit");
        startActivity(intent);

    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

/*
    private void setupUserTabIcons(TabLayout tabLayout)
    {
        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
        tabLayout.getTabAt(1).setIcon(userTabIcons[2]);
        tabLayout.getTabAt(2).setIcon(userTabIcons[3]);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {

            switch (position)
            {
                case 0:
                    return FragmentSettingChangePassword .newInstance(position);
            /*    case 1:
                    return FragmentSettingBlockMembers.newInstance(position);*/
                case 1:
                    return FragmentSettingPhotos.newInstance(position);
                case 2:
                    return FragmentSettingContact.newInstance(position);

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "CHANGE PASSWORD";
                case 1:
                    return "PHOTO PRIVACY SETTINGS";
                case 2:
                    return "CONTACT VIEW SETTINGS";


            }
            return null;
        }
    }


}
