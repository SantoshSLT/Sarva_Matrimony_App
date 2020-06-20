package Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.SuccessContinuation;
import com.thegreentech.AllMatches.MatchesDashboardActivity;
import com.thegreentech.ContactUsActivity;
import com.thegreentech.CurrentMembershipPlanActivity;
import com.thegreentech.ExpressInterestActivity;
import com.thegreentech.FCM.StatusUpdateService;
import com.thegreentech.LaunchActivity;
import com.thegreentech.MainActivity;
import com.thegreentech.ManagePhotoActivity;
import com.thegreentech.MenuProfileEdit;
import com.thegreentech.MessagesActivity;
import com.thegreentech.OnlinemembersActivity;
import com.thegreentech.PhotoPasswordRequestActivity;
import com.thegreentech.SavedSearchResultActivity;
import com.thegreentech.SettingActivity;
import com.thegreentech.ShortedProfileActivity;
import com.thegreentech.SuccessStoryActivity;
import com.thegreentech.UpdateHoroScopePhotosActivity;
import com.thegreentech.UpgradeMembershipPlanActivity;
import com.thegreentech.R;
import com.thegreentech.chat.ChatFragment;
import com.thegreentech.successStory.SuccessStoryDashBoardActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import Adepters.MenuExpandableListAdapter;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import utills.AppConstants;
import utills.ExpandableListMenu;
import utills.Myprefrence;
import utills.NetworkConnection;

import static android.app.Activity.RESULT_OK;


public class FragmentMenu extends Fragment
{
	String TAG="FragmentMenu";
	View rootView;
	Activity activity;
	private static final String ARG_SECTION_NUMBER = "section_number";
    SharedPreferences prefUpdate;

	public ImageView imgUserPhotos;
	TextView textUserCode,textUsername,tvMemberStatus;

    ExpandableListView expandableListView;
    MenuExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    RelativeLayout relMoreCMSView;
    LinearLayout linSlidingDrawer;
    TextView textCMSTitle/*textCMSContaints*/;
    WebView textCMSContaints;
    ProgressBar progressBar1;
    ImageView textClose,ivAddicon;
    Context context;

    String Membership_status = "";

	public FragmentMenu() {
	}

	public static FragmentMenu newInstance(int sectionNumber)
	{
		FragmentMenu fragment = new FragmentMenu();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);

		return fragment;
	}



    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    context = getActivity();
		rootView=inflater.inflate(R.layout.fragment_menu, container,false);
        prefUpdate= PreferenceManager.getDefaultSharedPreferences(getActivity());
        Membership_status = prefUpdate.getString("paid_status","");


		imgUserPhotos = (ImageView) rootView.findViewById(R.id.imgUserPhotos);
        //ivAddicon = (ImageView) rootView.findViewById(R.id.ivAddicon);
        textUserCode = (TextView) rootView.findViewById(R.id.textUserCode);
        textUsername = (TextView) rootView.findViewById(R.id.textUsername);

        tvMemberStatus = rootView.findViewById(R.id.tvMemberStatus);
        tvMemberStatus.setText(Membership_status);

		expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.white));
        expandableListDetail = ExpandableListMenu.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        Collections.sort(expandableListTitle, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                return o1.compareToIgnoreCase(o2);
            }
        });

        expandableListAdapter = new MenuExpandableListAdapter(activity, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        relMoreCMSView=(RelativeLayout) rootView.findViewById(R.id.relMoreCMSView);
        relMoreCMSView.setVisibility(View.GONE);
        linSlidingDrawer=(LinearLayout) rootView.findViewById(R.id.linSlidingDrawer);

        textCMSTitle=(TextView) rootView.findViewById(R.id.textCMSTitle);
        textCMSContaints= rootView.findViewById(R.id.textCMSContaints);
       // textCMSContaints.setMovementMethod(new ScrollingMovementMethod());
        progressBar1=(ProgressBar) rootView.findViewById(R.id.progressBar1);
        textClose= rootView.findViewById(R.id.textClose);

        String UserId= prefUpdate.getString("user_id","");
        if(!UserId.equalsIgnoreCase(""))
        {
            String email=prefUpdate.getString("email","");
            String username=prefUpdate.getString("username","");
            String profile_image=prefUpdate.getString("profile_image","");
            Log.e("profileImage",profile_image);
            String matri_id=prefUpdate.getString("matri_id","");

            textUsername.setText(username);
            textUserCode.setText(matri_id);

            if(!profile_image.equalsIgnoreCase(""))
            {

               Glide.with(getActivity())
                       .load(profile_image)
                       .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile))
                       .into(imgUserPhotos);

            }

        }

      /*  imgEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
            }
        });*/

//_____GROUP___
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
        {
            @Override
            public void onGroupExpand(int groupPosition)
            {
                String selectedMenuName=expandableListTitle.get(groupPosition);

                if(selectedMenuName.equalsIgnoreCase("0=My Home"))
                {
                    MainActivity.tab = MainActivity.tabLayout.getTabAt(0);
                    MainActivity.tab.select();

                }
                else if(selectedMenuName.equalsIgnoreCase("5=Online Members"))
                {
                    startActivity(new Intent(getActivity(), OnlinemembersActivity.class));
//                    MainActivity.tab = MainActivity.tabLayout.getTabAt(2);
//                    MainActivity.tab.select();

                }
                else if(selectedMenuName.equalsIgnoreCase("6=Settings"))
                {
                    activity.startActivity(new Intent(activity, SettingActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();

                }
                else if(selectedMenuName.equalsIgnoreCase("7=Contact"))
                {
                    activity.startActivity(new Intent(activity, ContactUsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();

                }

                else if(selectedMenuName.equalsIgnoreCase("2=My Matches"))
                {

                    activity.startActivity(new Intent(activity, MatchesDashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();

                }



//____________________________________________OLD_______________________________________________________

                if(selectedMenuName.equalsIgnoreCase("-2=My Matches")) {
                    MainActivity.tab = MainActivity.tabLayout.getTabAt(1);
                    MainActivity.tab.select();

                    FragmentHome.tab = FragmentHome.tabLayout.getTabAt(1);
                    FragmentHome.tab.select();

                }
                else if(selectedMenuName.equalsIgnoreCase("7=Referral share"))
                {
                    String url = "Hey,use my referral link and download this app! \n"+Myprefrence.getMyrefralLink(getContext());
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
                    startActivity(shareIntent);
                }
                else if(selectedMenuName.equalsIgnoreCase("6=Share"))
                {

                }
            }
        });
//___Child____
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
            {

                String selectedMenu=expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
//___MyProfile__________
                if(selectedMenu.equalsIgnoreCase("View Profile"))
                {
                    Intent intent =  new Intent(activity, MenuProfileEdit.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EditProfile","Edit");
                    startActivityForResult(intent,1);

                }
                else if(selectedMenu.equalsIgnoreCase("Edit Profile"))
                {

                    Intent intent =  new Intent(activity, MenuProfileEdit.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EditProfile","Edit");
                    startActivity(intent);
                    activity.finish();

                }
               else if(selectedMenu.equalsIgnoreCase("Saved Searches"))
                {
                    Intent newIntent= new Intent(activity, SavedSearchResultActivity.class);
                    newIntent.putExtra("search_Saved","menu_search_Saved");
                    startActivity(newIntent);
                }

                else if(selectedMenu.equalsIgnoreCase("My Messages"))
                {
                    Intent intent = new Intent(activity, MessagesActivity.class);
                    intent.putExtra("noti", "other");
                    activity.startActivity(intent);
                }
                else if(selectedMenu.equalsIgnoreCase("My Express Interest"))
                {
                    Intent intent = new Intent(activity, ExpressInterestActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("noti", "other");
                    activity.startActivity(intent);
                }
                else if(selectedMenu.equalsIgnoreCase("Manage Photo"))
                {
                    activity.startActivity(new Intent(activity, ManagePhotoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("Manage Horoscope"))
                {
                    activity.startActivity(new Intent(activity, UpdateHoroScopePhotosActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();

                }

//____MemberShip___
                else if(selectedMenu.equalsIgnoreCase("Membership Plan"))
                {
                    activity.startActivity(new Intent(activity, UpgradeMembershipPlanActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("Current Membership Plan"))
                {
                    activity.startActivity(new Intent(activity, CurrentMembershipPlanActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    activity.finish();
                }
//____ProfileDetail_____
                else if(selectedMenu.equalsIgnoreCase("Shortlisted Profile"))
                {
                    Intent newIntent= new Intent(activity,ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle","Shortlisted Members");
                    newIntent.putExtra("PageType","1");
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                    //activity.startActivity(new Intent(activity, ShortedProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    //activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("Blocked Profile"))
                {
                    Intent newIntent= new Intent(activity,ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle","Blocked Members");
                    newIntent.putExtra("PageType","2");
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("My Profile Viewed By"))
                {
                    Intent newIntent= new Intent(activity,ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle","Profile Viewed By");
                    newIntent.putExtra("PageType","3");
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("I Visited Profile"))
                {
                    Intent newIntent= new Intent(activity,ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle","I Visited Profiles");
                    newIntent.putExtra("PageType","4");
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("My Mobile No Viewed By"))
                {
                    Intent newIntent= new Intent(activity,ShortedProfileActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("HeaderTitle","Mobile No Viewed By");
                    newIntent.putExtra("PageType","5");
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                }
                else if(selectedMenu.equalsIgnoreCase("Photo Password Request"))
                {
                    Intent newIntent= new Intent(activity,PhotoPasswordRequestActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.putExtra("noti", "other");
                    activity.startActivity(newIntent);
                    activity.finish();
                }
//___More_____
                else if(selectedMenu.equalsIgnoreCase("Success Story"))
                {
                    Intent newIntent= new Intent(activity, SuccessStoryDashBoardActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(newIntent);
                    activity.finish();

                   /* Intent newIntent= new Intent(activity,SuccessStoryActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(newIntent);
                    activity.finish();*/
                }
                else if(selectedMenu.equalsIgnoreCase("Share Our App"))
                {
                    String url = "Hey, download this app! \n"+"https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
                    startActivity(shareIntent);
                }
                else if(selectedMenu.equalsIgnoreCase("Terms & Conditions"))
                {
                    relMoreCMSView.setVisibility(View.VISIBLE);

                    linSlidingDrawer.setVisibility(View.VISIBLE);
                    //textCMSContaints.setText(activity.getResources().getString(R.string.Terms_and_Conditions));
                    Animation bottomUp = AnimationUtils.loadAnimation(activity,  R.anim.slide_up_dialog);
                    linSlidingDrawer.startAnimation(bottomUp) ;


                    if (NetworkConnection.hasConnection(getActivity())){
                        setStaticDataRequest("terms.php");
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }
                else if(selectedMenu.equalsIgnoreCase("Privacy Policy"))
                {
                    relMoreCMSView.setVisibility(View.VISIBLE);

                    linSlidingDrawer.setVisibility(View.VISIBLE);
                    textCMSTitle.setText("Privacy Policy");
                    //textCMSContaints.setText(activity.getResources().getString(R.string.Privacy_Policy));
                    Animation bottomUp = AnimationUtils.loadAnimation(activity,  R.anim.slide_up_dialog);
                    linSlidingDrawer.startAnimation(bottomUp) ;
                    if (NetworkConnection.hasConnection(getActivity())){
                        setStaticDataRequest("privecy.php");
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }
                else if(selectedMenu.equalsIgnoreCase("Logout"))
                {

                    if (NetworkConnection.hasConnection(getActivity())){
                        LogoutAlert();
                    }else
                    {
                        AppConstants.CheckConnection(getActivity());
                    }
                }

                return false;
            }
        });

        textClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Animation bottomDown = AnimationUtils.loadAnimation(activity,  R.anim.slide_out_down);
                linSlidingDrawer.startAnimation(bottomDown) ;
                linSlidingDrawer.setVisibility(View.GONE);

                relMoreCMSView.startAnimation(bottomDown) ;
                relMoreCMSView.setVisibility(View.GONE);
            }
        });

        relMoreCMSView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
		return rootView;

	}





	@Override
	public void onAttach(Activity activity1)
	{
		super.onAttach(activity1);
		activity=activity1;


		Log.e("call onAttach","1");
	}


    public void LogoutAlert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getResources().getString(R.string.app_name))
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Logout",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        getActivity().stopService(new Intent(getActivity(), StatusUpdateService.class));
                        SharedPreferences.Editor editor=prefUpdate.edit();
                        editor.putString("user_id","");
                        editor.putString("email","");
                        editor.putString("profile_image","");
                        editor.putString("matri_id","");
                        editor.putString("username","");
                        editor.putString("gender","");
                        editor.commit();
                        Myprefrence.ClearAll(getActivity());
                        getActivity().getSharedPreferences("data", Context.MODE_PRIVATE)
                                .edit().clear().commit();
                        Intent intLogin= new Intent(activity, LaunchActivity.class);
                        startActivity(intLogin);
                        activity.finish();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void setStaticDataRequest(final String APIStatus)
    {
        progressBar1.setVisibility(View.VISIBLE);

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                //String paramsMatriId = params[0];

                HttpClient httpClient = new DefaultHttpClient();

                String URL= AppConstants.MAIN_URL +APIStatus;
                Log.e("contact_details", "== "+URL);

                HttpPost httpPost = new HttpPost(URL);

                //BasicNameValuePair MatriIdPair = new BasicNameValuePair("matri_id", paramsMatriId);

                /*List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(MatriIdPair);*/


                try
                {
                    /*UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
                    httpPost.setEntity(urlEncodedFormEntity);
                    Log.e("Parametters Array=", "== "+(nameValuePairList.toString().trim().replaceAll(",","&")));*/
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

                Log.e("contact_details", "=="+result);

                try
                {
                    JSONObject obj = new JSONObject(result);

                    String status=obj.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject responseData = obj.getJSONObject("responseData");
                        String page_name=responseData.getString("page_name");
                        String cms_content=responseData.getString("cms_content");

                        //textCMSTitle.setText(""+page_name);
                        //textCMSContaints.setText(""+cms_content);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            textCMSTitle.setText(Html.fromHtml(page_name,Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            textCMSTitle.setText(Html.fromHtml(page_name));
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                          //  textCMSContaints.setText(Html.fromHtml(cms_content,Html.FROM_HTML_MODE_LEGACY));
                            textCMSContaints.getSettings().setJavaScriptEnabled(true);
                            textCMSContaints.loadDataWithBaseURL("", cms_content, "text/html", "UTF-8", "");

                        } else {
                            textCMSContaints.getSettings().setJavaScriptEnabled(true);
                            textCMSContaints.loadDataWithBaseURL("", cms_content, "text/html", "UTF-8", "");

                            //textCMSContaints.setText(Html.fromHtml(cms_content));
                        }

                    }else
                    {
                        String msgError=obj.getString("message");
//                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                        builder.setMessage(""+msgError).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int id)
//                            {
//                                dialog.dismiss();
//                            }
//                        });
//                        AlertDialog alert = builder.create();
//                        alert.show();;
                    }


                   progressBar1.setVisibility(View.GONE);
                } catch (Throwable t)
                {
                    progressBar1.setVisibility(View.GONE);
                }
                progressBar1.setVisibility(View.GONE);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(APIStatus);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK && requestCode==1)
        {

        }
    }

}
