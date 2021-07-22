package com.binance.api.client.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.AuthenticationInterceptor;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator implements ApiGenerator {

    private final OkHttpClient sharedClient;
    private final Converter.Factory converterFactory = JacksonConverterFactory.create();

    {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder().dispatcher(dispatcher).pingInterval(20, TimeUnit.SECONDS).build();
    }

    @Override
    public <S> S createService(Class<S> serviceClass) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BinanceApiConfig.getApiBaseUrl())
                .addConverterFactory(converterFactory);

        // `adaptedClient` will use its own interceptor, but share thread pool etc with
        // the 'parent' client
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor();
        OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
        retrofitBuilder.client(adaptedClient);

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    @Override
    public <T> T createTestnetService(Class<T> serviceClass) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BinanceApiConfig.getTestnetBaseUrl())
                .addConverterFactory(converterFactory);


        // `adaptedClient` will use its own interceptor, but share thread pool etc with
        // the 'parent' client
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor();

        OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
        retrofitBuilder.client(adaptedClient);

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    @Override
    public BinanceApiWebSocketClient createSocket() {
        return new BinanceApiWebSocketClientImpl(sharedClient);

    }

}