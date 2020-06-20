package Adepters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IntegerRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.thegreentech.R;
import java.util.HashMap;
import java.util.List;

import Fragments.FragmentHome;

public class MenuExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public MenuExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail)
    {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition)
    {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_menu_sub_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition)
    {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,View convertView, ViewGroup parent)
    {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_menu_group, null);
        }
        ImageView imageIcons = (ImageView) convertView.findViewById(R.id.imageIcons);
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(removeFirstChar(listTitle));

        if(listPosition == 0)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_home);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else if (listPosition ==1)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_profile);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.icn_blck_down, 0);

        }else if (listPosition ==2)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_matches);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);

        }else if (listPosition ==3)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_membership);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icn_blck_down, 0);

        }else if (listPosition ==4)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_profiledetail);
            //imageIcons.setBackgroundResource(R.drawable.ic_menu_profiledetail);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icn_blck_down, 0);
        }
        else if (listPosition ==5)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_chat);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        //
        else if (listPosition ==6)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_seting);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        else if (listPosition ==7)
        {
            imageIcons.setVisibility(View.VISIBLE);
            imageIcons.setImageResource(R.drawable.ic_menu_contact);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else if (listPosition ==8)
        {
            imageIcons.setVisibility(View.GONE);
            listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icn_blck_down, 0);
        }

//        convertView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//            }
//        });


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public String removeFirstChar(String str){

        return str.split("=")[1];

    }
}