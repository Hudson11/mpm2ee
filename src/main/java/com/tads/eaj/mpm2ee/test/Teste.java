/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.test;

import com.tads.eaj.mpm2ee.enums.Comandos;
import com.tads.eaj.mpm2ee.model.TrainingModelBuilder;
import com.tads.eaj.mpm2ee.processador.ProcessadorDeTreino;
import com.tads.eaj.mpm2ee.pubsub.Publisher;
import com.tads.eaj.mpm2ee.retrofit.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 *
 * @author berna
 */
public class Teste {

    public static void main(String[] args) {
        System.out.println(Response.Status.CREATED);
        
        // Realizado uma chamada ao Môdulo de Predição
        List<String> args2 = new ArrayList<String>();
        args2.add("PH");
        args2.add("umidade");
        
        TrainingModelBuilder model = new TrainingModelBuilder.Builder()
				.firebaseUrl("https://mpm2ee-f8daf.firebaseio.com")
				.idNode("ESP-1")
				.targetAttribute("temperatura")
				.independentVariables(args2).build();
		
	//	if ( no.getPrediction() ) {
        	ProcessadorDeTreino.execute(Comandos.TRAINING_LINEAR, model);
     //   }
        
    }
}
