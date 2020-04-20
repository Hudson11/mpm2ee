/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.model;

import java.util.List;

/**
 *
 * @author Hudson Silva de Borba
 * 
 * exp -> Esta classe é responsável por passar as informações necessárias para o
 * serviço de predição treinar o modelo, ela também serve como body do retorno,
 * o atributo (ResponseModel response) são usados
 * para retorno das informações e não faz parte da criação do objeto.
 */
public class TrainingModelBuilder {
    
    private String firebaseUrl;
    
    private String idNode;
    
    private String targetAttribute;
    
    private List<String> independentVariables;
    
    // Retorno da requisição
    // Não faz parte da criação do objeto apenas para retorno das informações da
    // requisição
    // Motivo da gambiarra -> tentei fazer com o que o retrofit serializasse já
    // como ResponseModel, mas ele apresenta erros, esse método foi o que deu para
    // fazer no momento
    private ResponseModel response;

    public static class Builder{
        
        // requerido (Obrigatório para criação do Objeto)
        private String firebaseUrl;
        private String idNode;
        private String targetAttribute;
        private List<String> independentVariables;
        
        public Builder(){
        }
        
        public Builder firebaseUrl(String url){
            this.firebaseUrl = url;
            return this;
        }
        
        public Builder idNode(String id){
            this.idNode = id;
            return this;
        }
        
        public Builder targetAttribute(String target){
            this.targetAttribute = target;
            return this;
        }
        
        public Builder independentVariables(List<String> args){
            this.independentVariables = args;
            return this;
        }
        
        public TrainingModelBuilder build(){
            return new TrainingModelBuilder(this);
        }
    }
    
    private TrainingModelBuilder(Builder builder){
        this.firebaseUrl = builder.firebaseUrl;
        this.targetAttribute = builder.targetAttribute;
        this.idNode = builder.idNode;
        this.independentVariables = builder.independentVariables;
    }

    @Override
    public String toString() {
        return "TrainingModel{" + "firebaseUrl=" + firebaseUrl + ", idNode=" + 
                idNode + ", targetAttribute=" + targetAttribute + ", independentVariables=" + 
                independentVariables + ", response=" + response.toString() + '}';
    }

    public String getFirebaseUrl() {
        return firebaseUrl;
    }

    public void setFirebaseUrl(String firebaseUrl) {
        this.firebaseUrl = firebaseUrl;
    }

    public String getIdNode() {
        return idNode;
    }

    public void setIdNode(String idNode) {
        this.idNode = idNode;
    }

    public String getTargetAttribute() {
        return targetAttribute;
    }

    public void setTargetAttribute(String targetAttribute) {
        this.targetAttribute = targetAttribute;
    }

    public List<String> getIndependentVariables() {
        return independentVariables;
    }

    public void setIndependentVariables(List<String> independentVariables) {
        this.independentVariables = independentVariables;
    }

    public ResponseModel getResponse() {
        return response;
    }

    public void setResponse(ResponseModel response) {
        this.response = response;
    }
    
}
