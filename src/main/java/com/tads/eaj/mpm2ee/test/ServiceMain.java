/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.test;

import com.tads.eaj.mpm2ee.dao.ResponseModelDAO;
import com.tads.eaj.mpm2ee.enums.Comandos;
import com.tads.eaj.mpm2ee.model.TrainingModelBuilder;
import com.tads.eaj.mpm2ee.processador.ProcessadorDeTreino;
import com.tads.eaj.mpm2ee.pubsub.Publisher;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author isac3
 */
public class ServiceMain {
    
    public static void main(String[] args){
        
//        List<String> args2 = new ArrayList<String>();
//        args2.add("energia");
//        args2.add("luminosidade");
//        
//        TrainingModelBuilder model = new TrainingModelBuilder.Builder()
//                .firebaseUrl("https://mpm2ee-f8daf.firebaseio.com")
//                .idNode("ESP-7")
//                .targetAttribute("temperatura")
//                .independentVariables(args2)
//                .build();
//        
//        ProcessadorDeTreino.execute(Comandos.TRAINING_LINEAR, model);
    	
    	
    	Publisher.publicar("teste de merda");

      
    }
    
}
