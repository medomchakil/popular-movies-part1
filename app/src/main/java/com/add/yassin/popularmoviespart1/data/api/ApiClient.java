package com.add.yassin.popularmoviespart1.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yassin Ajdi.
 */
public class ApiClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final OkHttpClient USER1;
    private static MovieApiService exp;
    private static final Object Orecived = new Object();

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        USER1 = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    public static MovieApiService getInstance() {
        synchronized (Orecived) {
            if (exp == null) {
                exp = getRetrofitInstance().create(MovieApiService.class);
            }
            return exp;
        }
    }

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(USER1)
                .build();
    }
}
