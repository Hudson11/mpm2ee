/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.processador;

import com.tads.eaj.mpm2ee.dao.ResponseModelDAO;
import com.tads.eaj.mpm2ee.enums.Comandos;
import com.tads.eaj.mpm2ee.helper.ResponseModelHelper;
import com.tads.eaj.mpm2ee.model.ResponseModel;
import com.tads.eaj.mpm2ee.model.TrainingModelBuilder;
import com.tads.eaj.mpm2ee.pubsub.Publisher;
import com.tads.eaj.mpm2ee.retrofit.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author Hudson Silva
 */
public class ProcessadorDeTreino {
    
    private static TrainingModelBuilder trainingModel;
    
    private final static String DEEP_SLEEP 	= "deep-sleep";
    private final static String MODEN_SLEEP = "moden-sleep";
    private final static String LIGHT_SLEEP = "light-sleep";
    
    private final static String MENSAGEM_PADRAO = "ERRO, MODELO ESTÁ ACIMA DO ACEITÁVEL";
    
    private final static Double MARGIN_PERCENTUAL_DEEPSLEEP 	= 5.0;
    private final static Double MARGIN_PERCENTUAL_LIGHTSLEEP 	= 15.0;
    private final static Double MARGIN_PERCENTUAL_MODEMSLEEP 	= 20.0;
    
    
    /*
        Executa a Funcionalidade de acordo com o Comando Passado
        :param: 1 (comando) -> Indica qual a funcionalidade que o processador executará
        :param: 2 (model) -> Objeto com as informações necessárias para execução dos treinos...
    */
    public static void execute(Comandos comando, TrainingModelBuilder model){
        
        Call<TrainingModelBuilder> call = null;
        
        switch(comando){
            
            case TRAINING_LINEAR:
                call = new RetrofitConfig().getServiceRegression().trainingModelLinear(model);
                break;
            case TRAINING_MLP:
                call = new RetrofitConfig().getServiceRegression().trainingModelMlp(model);
                break;
            case TRAINING_SVR:
                call = new RetrofitConfig().getServiceRegression().trainingModelSvr(model);
                break;
            default:
                break;
            
        }
        
        executeAsyncTraining(call, model);
        
    }
    
    private static void executeAsyncTraining(Call<TrainingModelBuilder> call, TrainingModelBuilder model){
    	final TrainingModelBuilder mb = model;
        call.enqueue(new Callback<TrainingModelBuilder>() {
            @Override
            public void onResponse(Call<TrainingModelBuilder> call, Response<TrainingModelBuilder> response) {
                if(response.isSuccessful()){
                    setTrainingModel(response.body());
                    callBack(getTrainingModel());
                    
                    ResponseModelHelper helper = new ResponseModelHelper();
                    helper.createIdReference(mb.getIdNode());
                    helper.saveObject(getTrainingModel().getResponse());
                }
                else{
                	response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<TrainingModelBuilder> call, Throwable thrwbl) {
                thrwbl.printStackTrace();
            }
        });
    }
    
    // CallBack Function
    /*
        Responsável por interpretar o resultado vindo modelo de predição.
        Aplica uma metodologia específica.
    */
    private static void callBack(TrainingModelBuilder trainingModel){
        
        if(trainingModel == null)
            return;
        
        String mensagem = MENSAGEM_PADRAO;
        
        Double percentualDeErroAbsolute = ( trainingModel.getResponse().getMeanAbsoluteError() * 100 ) / trainingModel.getResponse().getMeanAttribute();
        Double percentualDeErroSquared = ( trainingModel.getResponse().getMeanSquaredError() * 100 ) / trainingModel.getResponse().getMeanAttribute();
        Double media = (percentualDeErroAbsolute + percentualDeErroSquared) / 2;
        
        if(media <= MARGIN_PERCENTUAL_DEEPSLEEP) {
        	mensagem = DEEP_SLEEP;
        }
        else if(media <= MARGIN_PERCENTUAL_LIGHTSLEEP && media > MARGIN_PERCENTUAL_DEEPSLEEP) {
        	mensagem = LIGHT_SLEEP;
        }
        else if(media <= MARGIN_PERCENTUAL_MODEMSLEEP && media > MARGIN_PERCENTUAL_LIGHTSLEEP) {
        	mensagem = MODEN_SLEEP;
        }
        else {
        	Publisher.publicar(mensagem);
        }
    }
    
    public static TrainingModelBuilder getTrainingModel() {
        return trainingModel;
    }

    private static void setTrainingModel(TrainingModelBuilder trainingModel) {
        ProcessadorDeTreino.trainingModel = trainingModel;
    }
    
}
