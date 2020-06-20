package RestAPI;

import java.util.Map;

import Models.GeneralBean;
import Models.beanUserSuccessStory;
import RoboPOJO.ProfileManagePhotoResponse;
import RoboPOJO.ProfileUpdateResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by amphee on 29/10/16.
 */


public interface ApiInterface {

    @Multipart
    @POST("signup_step4.php")
    public Call<ProfileUpdateResponse> postUpdateProfile(
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part image);


    @Multipart
    @POST("horoscope_upload.php")
    public Call<ProfileUpdateResponse> postUpdateHoroskopyProfile(
            @Part("matri_id") RequestBody user_id,
            @Part MultipartBody.Part image);

    @Multipart
    @POST("upload_photo.php")
    public Call<ProfileManagePhotoResponse> postManagePhotoProfile(
            @Part("matri_id") RequestBody user_id,
            @Part MultipartBody.Part image,
            @Part("index") RequestBody index);

    @Multipart
    @POST("post_success_story.php")
    Call<GeneralBean> AddSuccessStory(
            @Part("brideid") RequestBody bride_id,
            @Part("bridename") RequestBody bride_name,
            @Part("groomid") RequestBody groom_id,
            @Part("groomname") RequestBody groom_name,
            @Part("engagementdate") RequestBody engagement_date,
            @Part("marriagedate") RequestBody marreiage_date,
            @Part("succstory") RequestBody success_story,
            @Part("address") RequestBody address,
            @Part("country") RequestBody country,
            @Part MultipartBody.Part image);

/*
    @Multipart
    @POST("user/updateProfile")
    public Call<ProfileUpdateResponse> postUpdateProfileWithoutImage(
            @Part("token") RequestBody token,
            @Part("first_name") RequestBody fname,
            @Part("last_name") RequestBody lname,
            @Part("mobile_phone") RequestBody mobile,
            @Part("email") RequestBody email);

*/

   /* @Multipart
    @POST("job/add-new-job")
        //@FormUrlEncoded
    Call<JobAddResponse> postNewJob(
            @Part("token") RequestBody token,
            @Part("post_by") RequestBody jobPostBy,
            @Part("category") RequestBody jobCategory,
            @Part("title") RequestBody jobTitle,
            @Part("price") RequestBody price,
            @Part("contact_person") RequestBody contactPerson,
            @Part("description") RequestBody description,

            @Part("contact_type_show") RequestBody contact_type_show,
            @Part("postal_code") RequestBody postal_code,
            @Part("city") RequestBody city,
            @Part("locality") RequestBody locality,
            @Part("due_date") RequestBody due_date,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude);
            //@Part MultipartBody.Part image);*/


}