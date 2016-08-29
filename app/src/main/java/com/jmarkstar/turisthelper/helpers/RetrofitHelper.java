package com.jmarkstar.turisthelper.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jmarkstar.turisthelper.models.Session;
import com.jmarkstar.turisthelper.models.Venue;
import com.jmarkstar.turisthelper.models.VenuePhoto;
import com.jmarkstar.turisthelper.models.deserialize.SessionDeserializer;
import com.jmarkstar.turisthelper.models.deserialize.VenueDeserializer;
import com.jmarkstar.turisthelper.models.deserialize.VenuePhotoDeserializer;
import com.jmarkstar.turisthelper.models.deserialize.VenuePhotoResponseDeserializer;
import com.jmarkstar.turisthelper.models.deserialize.VenueResponseDeserializer;
import com.jmarkstar.turisthelper.services.response.VenuePhotoResponse;
import com.jmarkstar.turisthelper.services.response.VenueResponse;
import com.jmarkstar.turisthelper.utils.Constant;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import io.realm.Realm;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Singleton for Retorfit
 * Created by jmarkstar on 27/08/2016.
 */
public class RetrofitHelper {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            OkHttpClient.Builder httpClient =
                    new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {

                    String accessToken = null;

                    Realm realm = Realm.getDefaultInstance();
                    Session session = realm.where(Session.class).findFirst();
                    if(session != null){
                        accessToken = session.getToken();
                    }

                    SimpleDateFormat format = new SimpleDateFormat(Constant.V_DATE_FORMAT, Locale.getDefault());
                    String v = format.format( new Date());

                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter(Constant.TOKEN_PARAMETER, accessToken)
                    .addQueryParameter(Constant.V_PARAMETER, v)
                    .build();

                    Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Session.class, new SessionDeserializer())
                .registerTypeAdapter(VenueResponse.class, new VenueResponseDeserializer())
                .registerTypeAdapter(Venue.class, new VenueDeserializer())
                .registerTypeAdapter(VenuePhoto.class, new VenuePhotoDeserializer())
                .registerTypeAdapter(VenuePhotoResponse.class, new VenuePhotoResponseDeserializer())
                .create();

            retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        }
        return retrofit;
    }
}
