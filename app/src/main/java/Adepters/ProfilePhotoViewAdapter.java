package Adepters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thegreentech.R;

import java.util.ArrayList;
import java.util.List;

import Models.beanPhotos;

public class ProfilePhotoViewAdapter extends PagerAdapter{

    Activity activity;
    ArrayList<beanPhotos> defflist;


    public ProfilePhotoViewAdapter(Activity activity, ArrayList<beanPhotos> defflist) {

        this.activity = activity;
        this.defflist = defflist;
    }

    @Override
    public int getCount() {
        return defflist.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.photo_view_list, container, false);
        ImageView button = layout.findViewById(R.id.ivPhoto);

        Glide.with(activity)
                .load(defflist.get(position).getImageURL())
                .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                .into(button);
        container.addView(layout);
        return layout;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}




