/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.model;

/**
 *
 * @author berna
 */
public class Politica {
    
    private String politica; //política aplicada
    private String data;     //data de aplicação
    private String hora;     //hora de aplicação
    private String tempo;    //tempo em deepsleep

    public Politica() {
    }

    public Politica(String politica, String data, String hora, String tempo) {
        this.politica = politica;
        this.data = data;
        this.hora = hora;
        this.tempo = tempo;
    }

    @Override
    public String toString() {
        return "Politica{" + "politica=" + politica + ", data=" + data + ", hora=" + hora + ", tempo=" + tempo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.politica != null ? this.politica.hashCode() : 0);
        hash = 29 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 29 * hash + (this.hora != null ? this.hora.hashCode() : 0);
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
        final Politica other = (Politica) obj;
        if ((this.politica == null) ? (other.politica != null) : !this.politica.equals(other.politica)) {
            return false;
        }
        if ((this.data == null) ? (other.data != null) : !this.data.equals(other.data)) {
            return false;
        }
        if ((this.hora == null) ? (other.hora != null) : !this.hora.equals(other.hora)) {
            return false;
        }
        return true;
    }

    public String getPolitica() {
        return politica;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
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

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
    
}