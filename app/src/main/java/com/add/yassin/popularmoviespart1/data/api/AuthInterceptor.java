package com.add.yassin.popularmoviespart1.data.api;

import com.add.yassin.popularmoviespart1.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain x) throws IOException {
        Request request = x.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build();
        request = request.newBuilder().url(url).build();
        return x.proceed(request);
    }
}
