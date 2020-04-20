/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.service;

import com.tads.eaj.mpm2ee.dao.NodeDAO;
import com.tads.eaj.mpm2ee.enums.Comandos;
import com.tads.eaj.mpm2ee.model.Node;
import com.tads.eaj.mpm2ee.model.TrainingModelBuilder;
import com.tads.eaj.mpm2ee.processador.ProcessadorDeTreino;
import com.tads.eaj.mpm2ee.response.OutputMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lucas
 */
@Path("/esp")
public class ServicoESP {
    
    NodeDAO dao = new NodeDAO();

    /**
     * Método para criação de um no. Recebe um .json de um node e armazena no
     * banco.
     *
     * @param no .json de um node no formado:
     *
     * {
     * "id": "ESP1", "regiao": "R4", "energia": 4.4, "sensores": [ { "tipo":
     * "Nivel de água", "dado": "20" }, { "tipo": "Temperatura da água", "dado":
     * "25" }, { "tipo": "PH", "dado": "10" } ] }
     *
     * @return reposta informando se a operação foi bem sucedida ou não.
     */
    @SuppressWarnings("unused")
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Node no) {
        try {
            System.out.println("Dado recebido: " + no.toString());
            if (no == null) {
                return Response
                        .status(Response.Status.NOT_ACCEPTABLE)
                        .entity(new OutputMessage(500, "Objeto inválido!"))
                        .build();
            } else {
                LocalDate localDate = LocalDate.now();
                LocalDateTime localDateTime = LocalDateTime.now();
                LocalTime localTime = localDateTime.toLocalTime();
                no.setData(String.valueOf(localDate));
                no.setHora(String.valueOf(localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));

                //salva o dado do nó no banco
                this.dao = new NodeDAO();
                dao.salvarNode(no);
                dao.gerarToken();
                
				
				if ( no.getPrediction() ) {
					
					// Realizado uma chamada ao Môdulo de Predição
	                List<String> args2 = new ArrayList<String>();
	                args2.add("PH");
	                args2.add("umidade");
	                
					TrainingModelBuilder model = new TrainingModelBuilder.Builder()
							.firebaseUrl("URL DO FIREBASE") // PODE ADICIONAR ESSA INFORMAÇÃO EM UMA CONSTANTE NO PROJETO
							.idNode(no.getId())
							.targetAttribute("atributo alvo")
							.independentVariables(args2).build();
					
                	ProcessadorDeTreino.execute(Comandos.TRAINING_LINEAR, model);
                }
                
            }
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }

        return Response
                .status(Response.Status.CREATED)
                .entity(no)
                .build();

    }
    
    /**
     * Método para criação de um no. Recebe um .json de um node e armazena no
     * banco.
     *
     * @param no .json de um node no formado:
     *
     * {
     * "id": "ESP1", "regiao": "R4", "energia": 4.4, "sensores": [ { "tipo":
     * "Nivel de água", "dado": "20" }, { "tipo": "Temperatura da água", "dado":
     * "25" }, { "tipo": "PH", "dado": "10" } ] }
     *
     * @return reposta informando se a operação foi bem sucedida ou não.
     */
    
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/dado")
//    public Response create(Sensor sensor){
//        try{
//            if(sensor == null){
//                return Response
//                        .status(Response.Status.NOT_ACCEPTABLE)
//                        .entity(new OutputMessage(500, "Objeto inválido"))
//                        .build();
//            }
//            
//            this.dao = new NodeDAO();
//            dao.salvarSensor(sensor);
//        } catch (Exception e){
//            return Response
//                    .status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity(new OutputMessage(500, e.getMessage()))
//                    .build();
//        }
//        
//        return Response
//                    .status(Response.Status.ACCEPTED)
//                    .entity(sensor)
//                    .build();
//    }

    /**
     * Método para deletar um objeto pelo id informado
     *
     * @param id identificador do objeto buscado
     * @return resposta do servidor refernete a operação de delete se ocorreu
     * com sucesso ou não.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {

        if (id == null || id.equals("")) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();

        }
        try {
            NodeDAO dao = new NodeDAO();
            dao.excluir(id);
            dao.gerarToken();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }
        return Response
                .status(Response.Status.OK)
                .entity(new OutputMessage(200, "Objeto removido."))
                .build();

    }

    /**
     * Método para atualizar um node do banco de dados
     *
     * @param id identificador do objetoo a ser atualizado
     * @param no json do objeto contendo seus atributos
     * @return resposta contendo uma mensagem e um código indicando se deu certo
     * ou não.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Node no) {

        if (id == null || id.equals("")) {
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .entity(new OutputMessage(500, "ID inválido"))
                    .build();
        }
        try {
            NodeDAO dao = new NodeDAO();
            dao.atualizar(id, no);
            dao.gerarToken();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }

        return Response
                .status(Response.Status.OK)
                .entity(no)
                .build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listById(@PathParam("id") String id) {
        try {

            if (id == null || id.equals("")) {
                return Response
                        .status(Response.Status.NO_CONTENT)
                        .entity(new OutputMessage(500, "ID inválido"))
                        .build();

            } else {
                NodeDAO dao = new NodeDAO();
                dao.listById(id);
                dao.gerarToken();
                return Response
                        .status(Response.Status.OK)
                        .entity(dao.getLista()) //objeto retornado do banco
                        .build();

            }
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        try {
            NodeDAO dao = new NodeDAO();

            dao.listar();

            dao.gerarToken();

            if (dao.getLista() == null) {
                System.out.println("Entrou no IF");
                return Response
                        .status(Response.Status.NO_CONTENT)
                        .entity(new OutputMessage(500, "Nenhum objeto encontrado no banco"))
                        .build();

            } else {
                return Response
                        .status(Response.Status.OK)
                        .entity(dao.getLista())
                        .build();

            }
        } catch (Exception e) {
            System.out.println("Exception e");
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }
    }

}
