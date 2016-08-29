package com.example.apple.newsingit_project.manager.networkmanager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apple on 2016. 8. 29..
 */
public class RedirectInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() == 307) {
            String url = response.header("Location");
            Request nRequest = request.newBuilder()
                    .url(url)
                    .build();
            response = chain.proceed(nRequest);
        }
        return response;
    }
}
