/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.service;

import com.tads.eaj.mpm2ee.dao.PoliticaDAO;
import com.tads.eaj.mpm2ee.model.Politica;
import com.tads.eaj.mpm2ee.pubsub.Publisher;
import com.tads.eaj.mpm2ee.response.OutputMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author berna
 */
@Path("/politica")
public class ServicoPolitica {

    /**
     * aplicação de uma política de energia
     *
     * @param politica política a ser aplicada
     * @return código indicando se ocorreu tudo corretamente
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyPolicy(Politica politica) {
        try {
            if (politica == null) {
                return Response
                        .status(Response.Status.NOT_ACCEPTABLE)
                        .entity(new OutputMessage(500, "Política inválida!"))
                        .build();
            } else {
                LocalDate localDate = LocalDate.now();
                LocalDateTime localDateTime = LocalDateTime.now();
                LocalTime localTime = localDateTime.toLocalTime();
                politica.setData(String.valueOf(localDate));
                politica.setHora(String.valueOf(localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));

                if (politica.getPolitica().equalsIgnoreCase("modemsleep") || politica.getPolitica().equalsIgnoreCase("lightsleep")) {
                    Publisher.publicar(politica.getPolitica().toLowerCase());
                } else if (politica.getPolitica().equalsIgnoreCase("deepsleep")) {
                    Publisher.publicar(String.valueOf(politica.getTempo())); //basta setar o tempo
                } else {
                    return Response
                            .status(Response.Status.NOT_ACCEPTABLE)
                            .entity(new OutputMessage(500, "Política a ser aplicada ou tempo inválida(o)!"))
                            .build();
                }

                PoliticaDAO dao = new PoliticaDAO();
                dao.salvar(politica);
                dao.gerarToken();
            }
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }

        return Response
                .status(Response.Status.CREATED)
                .entity(new OutputMessage(200, "Política aplicada com sucesso!"))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        try {
            PoliticaDAO dao = new PoliticaDAO();

            dao.listar();

            dao.gerarToken();

            if (dao.getLista() == null) {
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
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new OutputMessage(500, e.getMessage()))
                    .build();

        }
    }
    
}