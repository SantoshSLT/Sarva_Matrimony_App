package utills;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.load.resource.gif.StreamGifDecoder;

public class Myprefrence {

    private static final String SECURE_TOKEN = "secure_token";
    private static final String SERVER_KEY = "server_key";
    private static final String DEVICE_TOKEN = "device_token";
    private  static final String APP_KEY="";


    public static void putMyrefralLink(Context ctx, String link) {
        ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit().putString("myref", link).commit();
    }

    public static String getMyrefralLink(Context ctx) {
        return ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .getString("myref", "");
    }

    public static void putRefralID(Context ctx, String ref_id) {
        ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit().putString("ref_id", ref_id).commit();
    }

    public static String getRefralID(Context ctx) {
        return ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .getString("ref_id", "");
    }

    public static void putUid(Context ctx, String user_id) {
        ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit().putString("user_id", user_id).commit();
    }

    public static String getUid(Context ctx) {
        return ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .getString("user_id", "");
    }

    public static void putDownload_bonus(Context ctx, String bouns) {
        ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit().putString("download_bonus",bouns).commit();
    }

    public static String getDownload_bonus(Context ctx) {
        return ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .getString("download_bonus", "");
    }
    public static void putRegistar_bonus(Context ctx, String bouns) {
        ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .edit().putString("reg_bonus",bouns).commit();
    }

    public static String getRegistar_bonus(Context ctx) {
        return ctx.getSharedPreferences("data", Context.MODE_PRIVATE)
                .getString("reg_bonus", "");
    }

    public static void ClearAll(Context ctx)
    {
        putMyrefralLink(ctx,"");
        putRefralID(ctx,"");
        putRegistar_bonus(ctx,"");
        putDownload_bonus(ctx,"");
        putUid(ctx,"");
    }

    public static void saveDeviceToken(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(DEVICE_TOKEN, value);
        editor.apply();
    }

    public static String getDeviceToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEVICE_TOKEN, "");
    }
    public static void saveServerKey(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(SERVER_KEY, value);
        editor.apply();
    }

    public static String getServerKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SERVER_KEY, "");
    }

}
