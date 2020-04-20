/*
 * A classe sensor representa um sensor da rede com seu dado coletado
 */
package com.tads.eaj.mpm2ee.model;

/**
 *
 * @author Lucas
 */
public class Sensor {

    private String tipo;
    private String dado;

    public Sensor() {
    }

    public Sensor(String tipo, String dado) {
        this.tipo = tipo;
        this.dado = dado;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    @Override
    public String toString() {
        return "Sensor{" + "tipo=" + tipo + ", dado=" + dado + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.tipo != null ? this.tipo.hashCode() : 0);
        hash = 67 * hash + (this.dado != null ? this.dado.hashCode() : 0);
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
        final Sensor other = (Sensor) obj;
        if ((this.tipo == null) ? (other.tipo != null) : !this.tipo.equals(other.tipo)) {
            return false;
        }
        if ((this.dado == null) ? (other.dado != null) : !this.dado.equals(other.dado)) {
            return false;
        }
        return true;
    }
    
}