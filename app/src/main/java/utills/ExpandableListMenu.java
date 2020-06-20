package utills;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListMenu
{
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

//        List<String> arrMyMatches = new ArrayList<String>();
       /* arrMyMatches.add("One way matches");
        arrMyMatches.add("Two way matches");
        arrMyMatches.add("Broader Matches");
        arrMyMatches.add("Preferred Matches");
        arrMyMatches.add("Custom Matches");
*/


        List<String> arrMyProfiles = new ArrayList<String>();
        arrMyProfiles.add("View Profile"); //new add
        arrMyProfiles.add("Edit Profile");
        arrMyProfiles.add("Saved Searches"); // new add
        arrMyProfiles.add("My Messages");  // new add  from Main menu
        arrMyProfiles.add("My Express Interest");
        arrMyProfiles.add("Manage Photo");
        arrMyProfiles.add("Manage Horoscope");

        List<String> arrMembership = new ArrayList<String>();
        arrMembership.add("Membership Plan");
        arrMembership.add("Current Membership Plan");


        List<String> arrProfileDetails = new ArrayList<String>();
        arrProfileDetails.add("Shortlisted Profile");
        arrProfileDetails.add("Blocked Profile");
        arrProfileDetails.add("My Profile Viewed By");
        arrProfileDetails.add("I Visited Profile");
        arrProfileDetails.add("My Mobile No Viewed By");
        arrProfileDetails.add("Photo Password Request");

        List<String> arrMyMatches = new ArrayList<String>();
        List<String> arrMyHome = new ArrayList<String>();
        List<String> arrSettings = new ArrayList<String>();
        List<String> chats = new ArrayList<String>();
        List<String> arrContactUs = new ArrayList<String>();
        List<String> arrLogout = new ArrayList<String>();
        List<String> arrShare = new ArrayList<String>();

        List<String> arrMore = new ArrayList<String>();
        arrMore.add("Success Story");
        arrMore.add("Share Our App");
        arrMore.add("Terms & Conditions");
        arrMore.add("Privacy Policy");
        arrMore.add("Logout");

        expandableListDetail.put("0=My Home", arrMyHome);
        expandableListDetail.put("1=My Profile", arrMyProfiles);
        expandableListDetail.put("2=My Matches", arrMyMatches);
        expandableListDetail.put("3=Membership", arrMembership);
        expandableListDetail.put("4=Profile Details", arrProfileDetails);
        expandableListDetail.put("5=Online Members", chats);
        expandableListDetail.put("6=Settings", arrSettings);
        expandableListDetail.put("7=Contact", arrContactUs);
        expandableListDetail.put("8=More", arrMore);


        return expandableListDetail;
    }
}