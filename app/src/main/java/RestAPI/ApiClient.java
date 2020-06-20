package RestAPI;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utills.AppConstants;

/**
 * Created by amphee on 29/10/16.
 */

public class ApiClient {

    public static final String BASE_URL = AppConstants.MAIN_URL;

    private static Retrofit retrofit = null;
    static OkHttpClient httpClient = new OkHttpClient.Builder()
            //.retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            //here we can add Interceptor for dynamical adding headers
            .addNetworkInterceptor(new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("test", "test").build();
                    return chain.proceed(request);
                }
            })
            //here we adding Interceptor for full level logging
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            /*Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();*/
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}