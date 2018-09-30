package com.mfeldsztejn.despegar.dtos;

import retrofit2.Response;

public class APIError extends Throwable {
    private int statusCode;
    private Response response;

    public APIError(int code, Response response) {
        statusCode = code;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Response getResponse() {
        return response;
    }
}
