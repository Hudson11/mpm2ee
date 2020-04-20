/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
    Alterações Feitas dia 21/06/2019 por Hudson Silva
*/
package com.tads.eaj.mpm2ee.dao;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tads.eaj.mpm2ee.model.Node;
import com.tads.eaj.mpm2ee.model.Sensor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berna
 */
public class NodeDAO {

    private Node no;
    private String key;//chave gerada quando o dado é salvo no banco
    private List<Node> lista = new ArrayList<Node>();

    private List<Node> listaNode;

    private final String TABELA = "Nodes";//nome da tabela

    private FirebaseDatabase mFirebase;
    private DatabaseReference ref;

    public static List<Node> listaNodeFirebase = new ArrayList<Node>();
    
    /*
        Funcionamento Ok
        -> Responsável por receber as informações do Corpo e postar no Firebase Database.
    */
    public void salvarNode(Node newNode) throws InterruptedException, ExecutionException {

        AuthFactory.getInstanceAuthFactory().isAppAutentication();//registra o app
        mFirebase = FirebaseDatabase.getInstance();
        ref = mFirebase.getReference(TABELA);


        String id = newNode.getId();
        String idReference = ref.push().getKey();
        newNode.setIdReference(idReference);
        ref.child(id+"/"+newNode.getIdReference()).setValueAsync(newNode);

    }
    
    /*
        Funcionamento Ok
        -> Lista todos os dados de um Nó específico na rede.
    */
    public void listById(final String id){
        AuthFactory.getInstanceAuthFactory().isAppAutentication();//registra o app
        mFirebase = FirebaseDatabase.getInstance();
        ref = mFirebase.getReference(TABELA+"/"+id);
        
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                lista.clear();
                for (DataSnapshot data : ds.getChildren()) {
                    Node a = data.getValue(Node.class);
                    lista.add(a);
                }
            }
            @Override
            public void onCancelled(DatabaseError de) {
                de.getMessage();
                de.toString();
                de.toException();
                de.getDetails();
                de.getCode();
            }
        });
    }

    public void buscar(String field, String value) throws InterruptedException, ExecutionException {
        no = null;
        AuthFactory.getInstanceAuthFactory().isAppAutentication();
        Query query = getReferenceDataBase().child(TABELA).orderByChild(field).equalTo(value);
        gerarToken();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot ds, String string) {
                System.out.println("ds: " + ds.getValue(Node.class));
                setNo(ds.getValue(Node.class));
                key = ds.getKey();
            }

            @Override
            public void onChildChanged(DataSnapshot ds, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onChildRemoved(DataSnapshot ds) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onChildMoved(DataSnapshot ds, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onCancelled(DatabaseError de) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void buscarPorId(String id) {
        no = null;
        AuthFactory.getInstanceAuthFactory().isAppAutentication();
        Query query = getReferenceDataBase().child(TABELA).orderByChild("id").equalTo(id);
        gerarToken();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot ds, String string) {
                
                setNo(ds.getValue(Node.class));
                key = ds.getKey();
                System.out.println("dado: " + ds.getValue());
                System.out.println("no: " + no.toString() + " key " + key);
                for(DataSnapshot data: ds.getChildren()){
                    Node node = data.getValue(Node.class);
                    System.out.println("node: " + node.toString());
                }
////                  for(DataSnapshot data: ds.getChildren()){
////                      setNo(data.getValue(Node))
////                  }

                  
            }
                

            @Override
            public void onChildChanged(DataSnapshot ds, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onChildRemoved(DataSnapshot ds) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onChildMoved(DataSnapshot ds, String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onCancelled(DatabaseError de) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void listar() throws InterruptedException, ExecutionException {
        

    }

    public void excluir(final String id) throws InterruptedException, ExecutionException {
        no = null;
        key = null;
        buscarPorId(id);

        gerarToken();
        if (!key.equals("")) {
            System.out.println("\nEncontrado: " + getNo().toString() + "\nkey " + key + "\n");
            getReferenceDataBase(TABELA + "/" + key).removeValueAsync();
        } else {
            System.out.println("Objeto não encontrado");
        }

    }

    public void atualizar(final String id, Node node) {
        no = null;
        key = null;

        buscarPorId(id);
        gerarToken();

        if (!key.equals("")) {
            System.out.println("node old: " + getNo());
            System.out.println("node new: " + node);
            setNo(node);
            Map<String, Object> childUpdates = new HashMap();
            childUpdates.put(TABELA + "/" + key, getNo());

            getReferenceDataBase().updateChildrenAsync(childUpdates);
        } else {
            System.out.println("Erro ao tentar atualizar");
        }

    }

    public static String getUserById(String uid) throws InterruptedException, ExecutionException {

        UserRecord userRecord = FirebaseAuth.getInstance().getUserAsync(uid).get();
        System.out.println("Successfully fetched user data: " + userRecord.getUid());
        return userRecord.getUid();

    }

    public static void listAllUsers() throws InterruptedException, ExecutionException {
        ListUsersPage page = FirebaseAuth.getInstance().listUsersAsync(null).get();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                System.out.println("User: " + user.getUid() + "\n" + user.getEmail());
            }
            page = page.getNextPage();
        }
        page = FirebaseAuth.getInstance().listUsersAsync(null).get();
        for (ExportedUserRecord user : page.iterateAll()) {
            System.out.println("User: " + user.getUid());
        }
    }

    private static void createCustomToken(String uid) throws InterruptedException, ExecutionException {

        String customToken = FirebaseAuth.getInstance().createCustomTokenAsync("ZqUyhCnHIiVxmWhMtUaxvJRWYbm2").get();
        System.out.println("Created custom token: " + customToken);
    }

    public void gerarToken() {
        try {
            listAllUsers();
            createCustomToken("ZqUyhCnHIiVxmWhMtUaxvJRWYbm2");
        } catch (InterruptedException ex) {
            Logger.getLogger(NodeDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(NodeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DatabaseReference getReferenceDataBase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getReferenceDataBase(String caminho) {
        return FirebaseDatabase.getInstance().getReference(caminho);
    }

    public Node getNo() {
        return no;
    }

    public void setNo(Node no) {
        this.no = no;
    }

    public List<Node> getLista() {
        return lista;
    }

    public void setLista(List<Node> lista) {
        this.lista = lista;
    }

    /**
     * retorna a key de identificação do objeto após ser salvo no banco
     *
     * @retur key gerada pelo banco ao inserir um node
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
