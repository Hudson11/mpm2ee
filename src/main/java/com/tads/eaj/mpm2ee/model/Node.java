/*
 * Classe que representa um nó da rede (NodeMcu ou arduino).
 */
package com.tads.eaj.mpm2ee.model;

import java.util.List;

/**
 *
 * @author Lucas
 */

public class Node {
    
    private String id;
    private String regiao;
    private String data;
    private String hora;
    private String duracao;
    private String politica;
    private String idReference;
    private Boolean prediction;
    private List<Sensor> sensores;

    public Node() {
    }

    public Node(String id, String regiao, String data, String hora, String duracao, String politica, List<Sensor> sensores, String idReference) {
        this.id = id;
        this.regiao = regiao;
        this.data = data;
        this.hora = hora;
        this.duracao = duracao;
        this.sensores = sensores;
        this.politica = politica;
        this.idReference = idReference;
    }
    
    // Construtor utilizado para cadastro no firebase (Sem os dados de sensores)..
    public Node(String id, String regiao, String data, String hora, String duracao, String politica){
        this.id = id;
        this.regiao = regiao;
        this.data = data;
        this.hora = hora;
        this.duracao = duracao;
        this.politica = politica;
    }

    public String getIdReference() {
        return idReference;
    }

    public void setIdReference(String idReference) {
        this.idReference = idReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Boolean getPrediction() {
		return prediction;
	}

	public void setPrediction(Boolean prediction) {
		this.prediction = prediction;
	}

	@Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.regiao != null ? this.regiao.hashCode() : 0);
        hash = 59 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 59 * hash + (this.hora != null ? this.hora.hashCode() : 0);
        hash = 59 * hash + (this.duracao != null ? this.duracao.hashCode() : 0);
        hash = 59 * hash + (this.politica != null ? this.politica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.regiao == null) ? (other.regiao != null) : !this.regiao.equals(other.regiao)) {
            return false;
        }
        if ((this.data == null) ? (other.data != null) : !this.data.equals(other.data)) {
            return false;
        }
        if ((this.hora == null) ? (other.hora != null) : !this.hora.equals(other.hora)) {
            return false;
        }
        if ((this.duracao == null) ? (other.duracao != null) : !this.duracao.equals(other.duracao)) {
            return false;
        }
        if ((this.politica == null) ? (other.politica != null) : !this.politica.equals(other.politica)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + ", regiao=" + regiao + ", data=" + data + ", hora=" + hora + ", duracao=" + duracao + ", politica=" + politica + ", sensores=" + sensores + '}';
    }
    
}