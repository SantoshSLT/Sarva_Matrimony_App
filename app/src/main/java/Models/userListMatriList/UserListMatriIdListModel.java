package Models.userListMatriList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserListMatriIdListModel implements Serializable {

    @SerializedName("responseData")
    @Expose
    public ResponseData responseData;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
}
