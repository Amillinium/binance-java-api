package com.binance.api.client.security;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.binance.api.client.constant.BinanceApiConstants;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * A request interceptor that injects the API Key Header into requests, and signs messages, whenever required.
 */
public class AuthenticationInterceptor implements Interceptor {



    public AuthenticationInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder newRequestBuilder = original.newBuilder();



        boolean isApiKeyRequired = original.header(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY) != null;
        boolean isSignatureRequired = original.header(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED) != null;
        newRequestBuilder.removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY)
            .removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED);

        final String apiKey = original.header("api");
        final String secret = original.header("secret");

        newRequestBuilder.removeHeader("api").removeHeader("secret");


        // Endpoint requires sending a valid API-KEY
        if (isApiKeyRequired || isSignatureRequired) {
            newRequestBuilder.addHeader(BinanceApiConstants.API_KEY_HEADER, apiKey);
        }

        // Endpoint requires signing the payload
        if (isSignatureRequired) {
            String payload = original.url().query();
            if (!StringUtils.isEmpty(payload)) {
                String signature = HmacSHA256Signer.sign(payload, secret);
                HttpUrl signedUrl = original.url().newBuilder().addQueryParameter("signature", signature).build();
                newRequestBuilder.url(signedUrl);
            }
        }

        // Build new request after adding the necessary authentication information
        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);
    }

    /**
     * Extracts the request body into a String.
     *
     * @return request body as a string
     */
    @SuppressWarnings("unused")
    private static String bodyToString(RequestBody request) {
        try (final Buffer buffer = new Buffer()) {
            final RequestBody copy = request;
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}