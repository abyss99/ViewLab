package com.naver.viewlabs.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abyss on 2017. 7. 31..
 */

public class DefaultHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            addHeaders(builder);

            return chain.proceed(builder.build());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void addHeaders(Request.Builder builder) {
        builder.header("User-Agent", "NaverWebtoon/1.11.5(Android OS 7.1.1;samsung SM-N950N)");
        builder.header("Accept", "*/*");
        builder.header("Accept-Language", "ko");
        builder.header("Accept-Ranges", "bytes");
        builder.header("Referer", "http://mobilecomicapp.naver.com/wip/mobileappimg/685989/3/asset/phoneghost_episode_3_android.zip");

    }
}
