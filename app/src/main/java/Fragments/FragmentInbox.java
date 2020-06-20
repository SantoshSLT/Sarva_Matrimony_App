package Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thegreentech.R;

import utills.AppConstants;


public class FragmentInbox extends Fragment {
    public static TabLayout tabLayout;
    public static TabLayout.Tab tab;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean isViewShown = false;
    SharedPreferences prefUpdate;
    FloatingActionButton fab;

    String TAG = "FragmentInbox";
    String matri_id = "";
    Fragment fmm;
    ProgressDialog progresDialog;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentInbox() {
    }

    public static FragmentInbox newInstance(int sectionNumber) {
        FragmentInbox fragment = new FragmentInbox();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_inbox, container, false);

        prefUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity());
        matri_id = prefUpdate.getString("matri_id", "");

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(0);

        tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
        fab = inflatedView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        if (!isViewShown) {
            setData();
        }

        int selectedTabPos = prefUpdate.getInt(AppConstants.SELECTED_TAB_MESSAGE, 0);
        if (selectedTabPos > 0) {
            tab = tabLayout.getTabAt(selectedTabPos);
            tab.select();
        } else {
            tab = tabLayout.getTabAt(0);
            tab.select();
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                fab.setVisibility(View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/*

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

*/

        return inflatedView;

    }

    public void onResume() {
        super.onResume();
        Log.e("call onResume", "Message");

        switch (AppConstants.fromNotification)
        {
            case "exp_interest":
                mViewPager.setCurrentItem(1);
                AppConstants.fromNotification = "";
                break;
            case "photo_req":
                mViewPager.setCurrentItem(3);
                AppConstants.fromNotification = "";
                break;
            case "chk_contact":
                mViewPager.setCurrentItem(5);
                AppConstants.fromNotification = "";
                break;
        }

    }

    private void setData() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getView() != null) {
            isViewShown = true;
            mViewPager.getAdapter().notifyDataSetChanged();

        } else {
            isViewShown = false;
        }
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    fmm = new FragmentInterestAccept();
                    return new FragmentInterestAccept();
                case 1:
                    fmm = FragmentInterestReceived.newInstance(position + 1);
                    return FragmentInterestReceived.newInstance(position + 1);
                case 2:
                    fmm = FragmentInterestSent.newInstance(position + 1);
                    return FragmentInterestSent.newInstance(position + 1);
                case 3:
                    fmm = FragmentPhotoRequestReceived.newInstance(position + 1);
                    return FragmentPhotoRequestReceived.newInstance(position + 1);
                case 4:
                    fmm = FragmentPhotoRequestSent.newInstance(position + 1);
                    return FragmentPhotoRequestSent.newInstance(position + 1);
                case 5:
                    fmm = new FrameMobilenumber_viewdby();
                    return new FrameMobilenumber_viewdby();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 6;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Interest Accepted";
                case 1:
                    return "Interest Received";
                case 2:
                    return "Interest Sent";
                case 3:
                    return "Photo Request Received";
                case 4:
                    return "Photo Request sent";
                case 5:
                    return "Viewed My Number";

            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

}
