/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.model;

import java.util.Date;

/**
 *
 * @author Hudson Silva de Borba
 */
public class ResponseModel {
    
    private String method;
    
    private String result;
    
    private Double meanSquaredError;
    
    private Double meanAbsoluteError;
    
    private Double meanAttribute;

    public ResponseModel(String method, String result, Double meanSquaredError, Double meanAbsoluteError, Double meanAttribute) {
        this.method = method;
        this.result = result;
        this.meanSquaredError = meanSquaredError;
        this.meanAbsoluteError = meanAbsoluteError;
        this.meanAttribute = meanAttribute;
    }
    
    public ResponseModel(){}

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Double getMeanSquaredError() {
        return meanSquaredError;
    }

    public void setMeanSquaredError(Double meanSquaredError) {
        this.meanSquaredError = meanSquaredError;
    }

    public Double getMeanAbsoluteError() {
        return meanAbsoluteError;
    }

    public void setMeanAbsoluteError(Double meanAbsoluteError) {
        this.meanAbsoluteError = meanAbsoluteError;
    }

	public Double getMeanAttribute() {
		return meanAttribute;
	}

	public void setMeanAttribute(Double meanAttribute) {
		this.meanAttribute = meanAttribute;
	}

	@Override
    public String toString() {
        return "ResponseModel{" + "method=" + method + ", result=" + result + ", meanSquaredError=" + meanSquaredError + ", meanAbsoluteError=" + meanAbsoluteError + '}';
    }

}
