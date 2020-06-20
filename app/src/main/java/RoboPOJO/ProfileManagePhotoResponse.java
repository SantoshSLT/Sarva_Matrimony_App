package RoboPOJO;

import com.google.gson.annotations.SerializedName;

public class ProfileManagePhotoResponse{

	@SerializedName("image")
	private String image;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ProfileManagePhotoResponse{" + 
			"image = '" + image + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}