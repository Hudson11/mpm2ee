/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tads.eaj.mpm2ee.serviceInterface.ServiceRegression;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author isac3
 */
public class RetrofitConfig {
    
    Retrofit rt;
    
    public RetrofitConfig(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        Gson gsonConvertFactory = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        this.rt = new Retrofit.Builder()
                .baseUrl("http://localhost:5000/")
                .addConverterFactory(GsonConverterFactory.create(gsonConvertFactory))
                .client(client)
                .build();
    }
  
    public ServiceRegression getServiceRegression(){
        return this.rt.create(ServiceRegression.class);
    }
    
}
