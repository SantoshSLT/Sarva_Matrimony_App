package com.thegreentech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import com.thegreentech.FCM.StatusUpdateService;
import com.thegreentech.chat.ChatFragment;
import com.thegreentech.chat.ChatingActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Fragments.FragmentHome;
import Fragments.FragmentMenu;
import Fragments.FragmentInbox;
import Fragments.FragmentSearch;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import utills.AppConstants;
import utills.Myprefrence;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    public static TabLayout tabLayout;
    public static TabLayout.Tab tab;
    private static String DEEP_LINK_URL = "https://example11.com/deeplink?myid=";
    SharedPreferences prefUpdate;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String user_id = "";
    public static ArrayList<String> menu_list;
    String TypeFrg = "";
    public static ViewPager mViewPager;
    private int[] userTabIconsHover = {

            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_chat1,
            R.drawable.ic_inbox,
            R.drawable.ic_menu,
    };
    private int[] userTabIcons = {

            R.drawable.ic_home1,
            R.drawable.ic_search1,
            R.drawable.icn_chat,
            R.drawable.ic_inbox1,
            R.drawable.ic_menu1,
    };

    public String className = "";

    FrameLayout fabCounterFrame;
    FloatingActionButton fab;
    TextView tvCounter;
    String MatriId="",Gender="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //start service
        startService(new Intent(getApplicationContext(), StatusUpdateService.class));
        //
        prefUpdate = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        user_id = prefUpdate.getString("user_id", "");
        MatriId = prefUpdate.getString("matri_id", "");
        Gender = prefUpdate.getString("gender", "");


        initilize();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);




        if (getIntent().hasExtra("classname")) {
            className = getIntent().getStringExtra("classname");
        } else {
            className = "";
        }

        if (getIntent().hasExtra("Fragments")) {
            TypeFrg = getIntent().getStringExtra("Fragments");
        } else {
            TypeFrg = "";
        }


        isDiscount();


    }


    public void initilize(){
        mViewPager = (ViewPager) findViewById(R.id.container);
        fabCounterFrame = findViewById(R.id.fabCounterFrame);
        fab = findViewById(R.id.fab);
        tvCounter = findViewById(R.id.tvCounter);

        getNotificationCounter(MatriId,Gender);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  FragmentNotification.newInstance();

                Intent intent = new Intent(MainActivity.this, FragmentNotification.class);
                startActivity(intent);
            }
        });

        menu_list = new ArrayList<>();

        menu_list.add("Home");
        menu_list.add("Search");
        menu_list.add("Chat");
        menu_list.add("Inbox");
        menu_list.add("Menu");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        setupUserTabIcons(tabLayout);
        setupUserTabIconsNew(tabLayout);


        Myprefrence.putUid(getApplicationContext(), user_id);

        if (!user_id.equalsIgnoreCase("")) {
            if (getIntent().hasExtra("screenId")) {
                String sid = getIntent().getStringExtra("screenId");
                switch (sid) {
                    case "201":
                        startActivity(new Intent(getApplicationContext(), ChatingActivity.class));
                        break;
                    case "202":
                        Intent intent = new Intent(getApplicationContext(), FragmentNotification.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        break;


                }
            }
        } else {

            startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
            finishAffinity();
        }

        int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_MAIN, 0);
        if (selectedTabPos > 0) {
            tab = tabLayout.getTabAt(selectedTabPos);
            tab.select();
        } else {
            tab = tabLayout.getTabAt(0);
            tab.select();
        }

        if (TypeFrg.equalsIgnoreCase("ProfileEdit")) {
            fab.setVisibility(View.GONE);
            tvCounter.setVisibility(View.GONE);
            tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
            mViewPager.setCurrentItem(4);
            tabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(userTabIcons[i]);
            }
            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());

                    SharedPreferences.Editor editor = prefUpdate.edit();
                    editor.putInt(AppConstants.SELECTED_TAB_MAIN, tab.getPosition());
                    editor.commit();

                    Log.i("Selected_tab_MainAc", tab.getPosition() + "");

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());//

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //mSectionsPagerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        fab.setVisibility(View.VISIBLE);
                        tvCounter.setVisibility(View.VISIBLE);
                    }
                    if (position == 1) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 2) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 3) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 4) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else if (TypeFrg.equalsIgnoreCase("search_frag")) {
            fab.setVisibility(View.GONE);
            tvCounter.setVisibility(View.GONE);
            tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
            mViewPager.setCurrentItem(1);
            tabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(userTabIcons[i]);
            }
            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());

                    SharedPreferences.Editor editor = prefUpdate.edit();
                    editor.putInt(AppConstants.SELECTED_TAB_MAIN, tab.getPosition());
                    editor.commit();

                    Log.i("Selected_tab_MainAc", tab.getPosition() + "");

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());//

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //mSectionsPagerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        fab.setVisibility(View.VISIBLE);
                        tvCounter.setVisibility(View.VISIBLE);
                    }
                    if (position == 1) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 2) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 3) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 4) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            fab.setVisibility(View.VISIBLE);
            tvCounter.setVisibility(View.VISIBLE);
            tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
            mViewPager.setCurrentItem(0);
            tabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(userTabIcons[i]);
            }
            tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());

                    SharedPreferences.Editor editor = prefUpdate.edit();
                    editor.putInt(AppConstants.SELECTED_TAB_MAIN, tab.getPosition());
                    editor.commit();

                    Log.i("Selected_tab_MainAc", tab.getPosition() + "");

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());//

                    if (tab.getPosition() == 0) {
                        fabCounterFrame.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 1) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 2) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);

                    } else if (tab.getPosition() == 3) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
                    } else if (tab.getPosition() == 4) {
                        fabCounterFrame.setVisibility(View.GONE);
                        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
                        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
                        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
                        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
                        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //mSectionsPagerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        fab.setVisibility(View.VISIBLE);
                        tvCounter.setVisibility(View.VISIBLE);
                    }
                    if (position == 1) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 2) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 3) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }
                    if (position == 4) {
                        fab.setVisibility(View.GONE);
                        tvCounter.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
    private void isDiscount() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email_id", user_id);
        client.post(AppConstants.MAIN_URL + "user_referal_bonus.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                try {
                    JSONObject obj = new JSONObject(responseString);

                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        if (responseData.has("1")) {
                            Iterator<String> resIter = responseData.keys();

                            while (resIter.hasNext()) {

                                String key = resIter.next();
                                JSONObject resItem = responseData.getJSONObject(key);
                                if (resItem.getString("ref_download").equalsIgnoreCase("Yes")) ;
                                {
                                    String ss = resItem.getString("ref_download_bonus");
                                    Myprefrence.putDownload_bonus(MainActivity.this, ss);
                                }
                                if (resItem.getString("ref_register").equalsIgnoreCase("Yes")) ;
                                {
                                    String ss = resItem.getString("ref_register_bonus");
                                    Myprefrence.putRegistar_bonus(MainActivity.this, ss);
                                }
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), StatusUpdateService.class));
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (AppConstants.fromNotification) {
            case "exp_interest":
                mViewPager.setCurrentItem(4);
                break;
            case "photo_req":
                mViewPager.setCurrentItem(4);
                break;
            case "chk_contact":
                mViewPager.setCurrentItem(4);
                break;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    private void setupUserTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(userTabIcons[0]);
        tabLayout.getTabAt(1).setIcon(userTabIcons[1]);
        tabLayout.getTabAt(2).setIcon(userTabIcons[2]);
        tabLayout.getTabAt(3).setIcon(userTabIcons[3]);
        tabLayout.getTabAt(4).setIcon(userTabIcons[4]);
    }

    private void setupUserTabIconsNew(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(userTabIconsHover[0]);
        tabLayout.getTabAt(1).setIcon(userTabIconsHover[1]);
        tabLayout.getTabAt(2).setIcon(userTabIconsHover[2]);
        tabLayout.getTabAt(3).setIcon(userTabIconsHover[3]);
        tabLayout.getTabAt(4).setIcon(userTabIconsHover[4]);
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
                    return FragmentHome.newInstance(position);
                case 1:
                    return FragmentSearch.newInstance(position);
                case 2:
                    return ChatFragment.newInstance(position);
                case 3:
                    return FragmentInbox.newInstance(position);
                case 4:
                    return FragmentMenu.newInstance(position);

                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return menu_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menu_list.get(position);
        }
    }


    @Override
    public void onBackPressed() {

        if (className.equalsIgnoreCase("LoginActivity")) {
            System.exit(1);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to Exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void buildDeepLink(@NonNull Uri deepLink, int minVersion) {

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(deepLink)
                .setDynamicLinkDomain("jodimate.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            final Uri shortLink = task.getResult().getShortLink();
                            //linkViewSend.setText(shortLink.toString());
                            //Toast.makeText(fabMainActivity.this, shortLink.toString(), Toast.LENGTH_SHORT).show();
                            Myprefrence.putMyrefralLink(getApplicationContext(), shortLink.toString());

                        } else {
                            // Error
                            // ...
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void getNotificationCounter(String MatriId, String Gender)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramMatriId = params[0];
                String paramGender = params[1];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +"app_notification_count.php";
                Log.e("URL_Recent", "== "+URL);
                HttpPost httpPost = new HttpPost(URL);

                BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramMatriId);
                BasicNameValuePair GenderPair = new BasicNameValuePair("gender",paramGender);

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);
                nameValuePairList.add(GenderPair);

                try
                {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));
                    try
                    {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null)
                        {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                    } catch (ClientProtocolException cpe) {
                        System.out.println("Firstption caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe)
                    {
                        System.out.println("Secondption caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }
                } catch (Exception uee) //UnsupportedEncodingException
                {
                    System.out.println("Anption given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);

                Log.e("Recent_Listing", "=="+result);
                try
                {
                    JSONObject obj = new JSONObject(result);
                    ArrayList<String> tokans = new ArrayList<>();
                    tokans.clear();
                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");

                        String notifi_counter = responseData.getString("notification_count");
                        tvCounter.setText(notifi_counter);
                        Log.e("counter",notifi_counter);

                    }else
                    {
                        tvCounter.setText("0");
                        String msgError=obj.getString("message");
                        Log.e("erroorr_____",msgError);
                    }


                } catch (Exception t)
                {
                    Log.e("mfgklnfg",t.getMessage());

                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(MatriId,Gender);
    }

}
