package utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

	Context context;

	public NetworkConnection(Context context) {
		super();
		this.context = context;
	}



	public static boolean hasConnection(Context context) {
		boolean isConnected = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = cm.getActiveNetworkInfo();

		if (activeInfo != null ) {

		}

		if (activeInfo != null
				&& (activeInfo.isConnected() || activeInfo.isConnectedOrConnecting())) {



			switch (activeInfo.getType()) {
				case ConnectivityManager.TYPE_MOBILE:
					//SLog.i("TYPE_MOBILE : "+ ConnectivityManager.TYPE_MOBILE);
					isConnected = true;
					break;
				case ConnectivityManager.TYPE_WIFI:
					//SLog.i("TYPE_WIFI : " + ConnectivityManager.TYPE_WIFI);
					isConnected = true;
					break;
				case ConnectivityManager.TYPE_WIMAX:
					//SLog.i("TYPE_WIMAX : " + ConnectivityManager.TYPE_WIMAX);
					isConnected = true;
					break;
				case ConnectivityManager.TYPE_ETHERNET:
					//SLog.i("TYPE_ETHERNET : "+ ConnectivityManager.TYPE_ETHERNET);
					isConnected = true;
					break;

				default:
					isConnected = false;
					break;
			}
		}


		return isConnected;
	}
}
