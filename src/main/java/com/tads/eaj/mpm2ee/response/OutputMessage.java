/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.response;

/**
 *
 * @author Aluno
 */
public class OutputMessage {
    private Integer codigo;
    private String mensagem;

    public OutputMessage(Integer codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public OutputMessage() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
}
