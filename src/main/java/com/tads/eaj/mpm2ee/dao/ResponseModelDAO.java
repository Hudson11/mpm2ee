package com.tads.eaj.mpm2ee.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tads.eaj.mpm2ee.model.ResponseModel;

public class ResponseModelDAO {
	
	private static final String child = "RESPONSEMODEL";
	
	private FirebaseDatabase database;
	private DatabaseReference reference;
	
	List<ResponseModel> lista = new ArrayList<ResponseModel>();
	
	// Constructor
	public ResponseModelDAO() {}
	
	// Save Object
	public void save(ResponseModel model, String idReference) {
		
		AuthFactory.getInstanceAuthFactory().isAppAutentication();
		
		database = FirebaseDatabase.getInstance();
		reference = database.getReference(child);
		
		reference.child(idReference + "/" + reference.push().getKey()).setValueAsync(model);
	}
	
	public void findAll(String idNode) {
		
		AuthFactory.getInstanceAuthFactory().isAppAutentication();
		
		database = FirebaseDatabase.getInstance();
		reference = database.getReference(child + "/" + idNode);
				
		reference.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				
				for(DataSnapshot a: snapshot.getChildren()) {
					lista.add(a.getValue(ResponseModel.class));
				}
				
				for(ResponseModel a : lista)
					System.out.println(a.toString());
			}
			
			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				error.getCode();
				error.getMessage();
				error.getDetails();
			}
		});

	}
}
