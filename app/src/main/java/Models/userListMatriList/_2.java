package Models.userListMatriList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class _2  implements Serializable {

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("matri_id")
    @Expose
    public String matriId;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("tokan")
    @Expose
    public String tokan;
}
