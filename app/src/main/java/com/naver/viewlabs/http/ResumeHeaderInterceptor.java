package com.naver.viewlabs.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abyss on 2017. 7. 31..
 */

public class ResumeHeaderInterceptor implements Interceptor {

    private long mResumeBytes;

    public ResumeHeaderInterceptor(long resumeBytes) {
        mResumeBytes = resumeBytes;
    }

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
        builder.header("Accept-Ranges", "bytes");
        builder.header("Range", "bytes=" + mResumeBytes + "-");

    }
}
