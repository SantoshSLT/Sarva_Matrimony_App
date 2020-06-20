package Adepters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thegreentech.R;
import com.squareup.picasso.Picasso;

import Models.beanUserSuccessStory;


public class SuccessStoryAdapter extends PagerAdapter
{
	Activity context;
	ViewPager pagerAdvert;
	public  ArrayList<beanUserSuccessStory> arrSuccessStoryList= null;

	public SuccessStoryAdapter(Activity context,  ArrayList<beanUserSuccessStory> arrSuccessStoryList,
			ViewPager pagerNoshrAdvert)
	{
		this.context = context;
		this.arrSuccessStoryList = arrSuccessStoryList;
		this.pagerAdvert = pagerNoshrAdvert;

	}

	@Override
	public int getCount() 
	{
		return arrSuccessStoryList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) 
	{
		LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					
		View row = inflater.inflate(R.layout.item_success_story, null);
		
		if (arrSuccessStoryList != null)
		{
			ImageView imgProfileImage = (ImageView) row.findViewById(R.id.imgProfileImage);
			TextView textBrideName = (TextView) row.findViewById(R.id.textBrideName);
			TextView textGroomName = (TextView) row.findViewById(R.id.textGroomName);
			TextView textAddress = (TextView) row.findViewById(R.id.textAddress);
			TextView textSuccessStory = (TextView) row.findViewById(R.id.textSuccessStory);
			textSuccessStory.setMovementMethod(new ScrollingMovementMethod());

			textBrideName.setText(arrSuccessStoryList.get(position).getBridename().toString().trim()+" & ");
			textGroomName.setText(arrSuccessStoryList.get(position).getGroomname().toString().trim());
			textAddress.setText(arrSuccessStoryList.get(position).getAddress().toString().trim());
			textSuccessStory.setText(arrSuccessStoryList.get(position).getSuccessmessage().toString().trim());


			if(! arrSuccessStoryList.get(position).getWeddingphoto().equalsIgnoreCase(" "))
			{
				Picasso.with(context)
						.load(arrSuccessStoryList.get(position).getWeddingphoto())
						.fit()
						.placeholder(R.drawable.loading1)
						.error(R.drawable.male)
						.into(imgProfileImage);
			}

//			row.setOnClickListener(new View.OnClickListener()
//			{
//				@Override
//				public void onClick(View v)
//				{
//					AppConstants.RESTURENT_ID=arrNoshrAdvert.get(position).getRestaurantId();
//					AppConstants.RESTURENT_SEARCH_NAME= arrNoshrAdvert.get(position).getRestaurantName();
//
//					FragmentSearchResultDetails addDetailFragment=new FragmentSearchResultDetails();
//					//FragmentTransaction transaction=getFragmentManager().beginTransaction();
//					transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//					transaction.replace(R.id.content_frame, addDetailFragment);
//					transaction.addToBackStack(null);
//					transaction.commit();
//				}
//			});
//
		}	

		
		((ViewPager) container).addView(row, 0);
		return row;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		return arg0==(View)arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
	  ((ViewPager) container).removeView((View) object);
	   object=null;
	}
}

