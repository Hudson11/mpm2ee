package com.tads.eaj.mpm2ee.helper;

import java.util.ArrayList;
import java.util.List;

import com.tads.eaj.mpm2ee.dao.ResponseModelDAO;
import com.tads.eaj.mpm2ee.model.ResponseModel;

public class ResponseModelHelper {
	
	private ResponseModelDAO dao;
	
	private String childReference;
	
	private List<String> mensagens;
	
	// Constructor
	public ResponseModelHelper(){}
	
	
	public void reset() {
		mensagens = new ArrayList<String>();
	}
	
	public void saveObject(ResponseModel model) {
		
		reset();
		
		validator(model);
		
		if( hasError() )
			return;
		
		dao = new ResponseModelDAO();
		dao.save(model, childReference);
		
	}
	
	public void createIdReference(String reference) {
		this.childReference = reference;
	}
	
	public void validator(ResponseModel model) {
		if( model.getMethod().isEmpty() )
			mensagens.add("Error Attribute METHOD NULL");
		if( model.getResult().isEmpty() )
			mensagens.add("Error Attribute RESULT NULL");
		if( model.getMeanAttribute() == null )
			mensagens.add("Error Attribute MEANATTRIBUTE IS NULL VALUE");
		if( model.getMeanAbsoluteError()== null )
			mensagens.add("Error Attribute ABSOLUTEERROR IS NULL VALUE");
		if( model.getMeanSquaredError() == null )
			mensagens.add("Error Attribute SQUAREDERROR IS NULL VALUE");
	}
	
	public boolean hasError() {
		return !mensagens.isEmpty();
	}
	
	

}
