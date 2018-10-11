package com.mfeldsztejn.despegar;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    private String fileName;

    public MockInterceptor(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String response = ResourceParser.getTestResourceContent(fileName);
        return new Response.Builder()
                .request(chain.request())
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(response)
                .body(ResponseBody.create(MediaType.parse("application/json"), response.getBytes()))
                .build();
    }
}
