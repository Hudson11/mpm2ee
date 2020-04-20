/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.serviceInterface;


import com.tads.eaj.mpm2ee.model.ResponseModel;
import com.tads.eaj.mpm2ee.model.TrainingModelBuilder;
import java.util.List;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * @author Hudson Silva de Borba
 */
public interface ServiceRegression {
    
    @POST("prediction/linearRegression")
    Call<TrainingModelBuilder> trainingModelLinear(@Body TrainingModelBuilder model);
    
    @POST("prediction/svrRegression")
    Call<TrainingModelBuilder> trainingModelSvr(@Body TrainingModelBuilder model);
    
    @POST("prediction/mlpRegression")
    Call<TrainingModelBuilder> trainingModelMlp(@Body TrainingModelBuilder model);
    
    @POST("prediction/linearRegression/data")
    Call<ResponseModel> equationLinear(@Body List<Double> list);
    
    @POST("prediction/srvRegression/data")
    Call<ResponseModel> equationSvr(@Body List<Double> list);
    
    @POST("prediction/mlpRegression/data")
    Call<ResponseModel> equationMlp(@Body List<Double> list);
    
    @POST("login")
    Call<HashMap<String, Object>> logout(@Body HashMap<String, String> bodyData);
    
}
