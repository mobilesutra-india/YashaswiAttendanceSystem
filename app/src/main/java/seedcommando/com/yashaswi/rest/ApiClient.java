package seedcommando.com.yashaswi.rest;




import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import seedcommando.com.yashaswi.constantclass.EmpowerApplication;

/**
 * Created by commando4 on 1/2/2018.
 */

public class ApiClient {
     //seedmanagement.cloudapp.net //192.168.1.54 //125.99.42.40//125.99.69.58
    public static final String BASE_URL = EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("serviceUrl"));
    private static Retrofit retrofit = null;



    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();


    public static Retrofit getClient() {
        if (retrofit==null) {
            try {
                //"http://125.99.69.58/Empower_Mobile/app/Api/"
                Log.d("", "getClient_BASE_URL: "+BASE_URL);

                retrofit = new Retrofit.Builder()

                        .baseUrl(BASE_URL)

                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                        .client(client)
                        .build();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return retrofit;
    }
}
