/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.dao;

/**
 *
 * @author lber
 */
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

/**
 * Auth snippets for documentation.
 *
 * See: https://firebase.google.com/docs/auth/admin
 */
public class AuthFactory {

    //Padrão de projeto singleton, faz com que haja apenas uma instância desse objeto para todo o oprojeto
    private static final AuthFactory instance = new AuthFactory();

    //indica se a aplicação foi autenticada ( se tem permissão para usar a base de dados)
    private static boolean appAutentication;

    public boolean isAppAutentication() {
        if (!appAutentication) {
            appAutentication = authenticateWithPrivileges();
        }
        return appAutentication;
    }

    private AuthFactory() {
    }

    public static AuthFactory getInstanceAuthFactory() {
        return instance;
    }

    public static DatabaseReference reference;

    public void getUserById(String uid) throws InterruptedException, ExecutionException {
        // [START get_user_by_id]
        UserRecord userRecord = FirebaseAuth.getInstance().getUserAsync(uid).get();
        // See the UserRecord reference doc for the contents of userRecord.
        System.out.println("Successfully fetched user data: " + userRecord.getUid());
        // [END get_user_by_id]
    }

    public void getUserByEmail(String email) throws InterruptedException, ExecutionException {
        // [START get_user_by_email]
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmailAsync(email).get();
        // See the UserRecord reference doc for the contents of userRecord.
        System.out.println("Successfully fetched user data: " + userRecord.getEmail());
        // [END get_user_by_email]
    }

    public void getUserByPhoneNumber(String phoneNumber) throws InterruptedException, ExecutionException {
        // [START get_user_by_phone]
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByPhoneNumberAsync(phoneNumber).get();
        // See the UserRecord reference doc for the contents of userRecord.
        System.out.println("Successfully fetched user data: " + userRecord.getPhoneNumber());
        // [END get_user_by_phone]
    }

    public static void createUser() throws InterruptedException, ExecutionException {
        // [START create_user]
        CreateRequest request = new CreateRequest()
                .setEmail("hudsonhdj36@gmail.com")
                .setEmailVerified(false)
                .setPassword("hudson9811")
                .setPhoneNumber("+5584988785181")
                .setDisplayName("John Doe")
                .setPhotoUrl("http://www.example.com/12345678/photo.png")
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUserAsync(request).get();
        System.out.println("Successfully created new user: " + userRecord.getUid());
        // [END create_user]
    }

    public static void createUserWithUid() throws InterruptedException, ExecutionException {
        // [START create_user_with_uid]
        CreateRequest request = new CreateRequest()
                .setUid("lucas-uid")
                .setEmail("bernardotriton@gmail.com")
                .setPhoneNumber("+5584991770750");

        UserRecord userRecord = FirebaseAuth.getInstance().createUserAsync(request).get();
        System.out.println("Successfully created new user: " + userRecord.getUid());
        // [END create_user_with_uid]
    }

    public static void updateUser(String uid) throws InterruptedException, ExecutionException {
        // [START update_user]
        UpdateRequest request = new UpdateRequest(uid)
                .setEmail("lucasbernardo95@gmail.com")
                .setPhoneNumber("+5584991272543")
                .setEmailVerified(true)
                .setPassword("17020295bass")
                .setDisplayName("Jane Doe")
                .setPhotoUrl("http://www.example.com/12345678/photo.png")
                .setDisabled(true);

        UserRecord userRecord = FirebaseAuth.getInstance().updateUserAsync(request).get();
        System.out.println("Successfully updated user: " + userRecord.getUid());
        // [END update_user]
    }

    public static void setCustomUserClaims(String uid) throws InterruptedException, ExecutionException {
        // [START set_custom_user_claims]
        // Set admin privilege on the user corresponding to uid.
        Map<String, Object> claims = new HashMap();
        claims.put("admin", true);
        FirebaseAuth.getInstance().setCustomUserClaimsAsync(uid, claims).get();
        // The new custom claims will propagate to the user's ID token the
        // next time a new one is issued.
        // [END set_custom_user_claims]

        String idToken = "id_token";
        // [START verify_custom_claims]
        // Verify the ID token first.
        FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
        if (Boolean.TRUE.equals(decoded.getClaims().get("admin"))) {
            // Allow access to requested admin resource.
        }
        // [END verify_custom_claims]

        // [START read_custom_user_claims]
        // Lookup the user associated with the specified uid.
        UserRecord user = FirebaseAuth.getInstance().getUserAsync(uid).get();
        System.out.println(user.getCustomClaims().get("admin"));
        // [END read_custom_user_claims]
    }

    public static void setCustomUserClaimsScript() throws InterruptedException, ExecutionException {
        // [START set_custom_user_claims_script]
        UserRecord user = FirebaseAuth.getInstance()
                .getUserByEmailAsync("user@admin.example.com").get();
        // Confirm user is verified.
        if (user.isEmailVerified()) {
            Map<String, Object> claims = new HashMap();
            claims.put("admin", true);
            FirebaseAuth.getInstance().setCustomUserClaimsAsync(user.getUid(), claims).get();
        }
        // [END set_custom_user_claims_script]
    }

    public static void setCustomUserClaimsInc() throws InterruptedException, ExecutionException {
        // [START set_custom_user_claims_incremental]
        UserRecord user = FirebaseAuth.getInstance()
                .getUserByEmailAsync("user@admin.example.com").get();
        // Add incremental custom claim without overwriting the existing claims.
        Map<String, Object> currentClaims = user.getCustomClaims();
        if (Boolean.TRUE.equals(currentClaims.get("admin"))) {
            // Add level.
            currentClaims.put("level", 10);
            // Add custom claims for additional privileges.
            FirebaseAuth.getInstance().setCustomUserClaimsAsync(user.getUid(), currentClaims).get();
        }
        // [END set_custom_user_claims_incremental]
    }

    public void listAllUsers() throws InterruptedException, ExecutionException {
        // [START list_all_users]
        // Start listing users from the beginning, 1000 at a time.
        ListUsersPage page = FirebaseAuth.getInstance().listUsersAsync(null).get();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                System.out.println("User: " + user.getUid());
            }
            page = page.getNextPage();
        }

        // Iterate through all users. This will still retrieve users in batches,
        // buffering no more than 1000 users in memory at a time.
        page = FirebaseAuth.getInstance().listUsersAsync(null).get();
        for (ExportedUserRecord user : page.iterateAll()) {
            System.out.println("User: " + user.getUid());
        }
        // [END list_all_users]
    }

    public static void deleteUser(String uid) throws InterruptedException, ExecutionException {
        // [START delete_user]
        FirebaseAuth.getInstance().deleteUserAsync(uid).get();
        System.out.println("Successfully deleted user.");
        // [END delete_user]
    }

    public void createCustomToken() throws InterruptedException, ExecutionException {
        // [START custom_token]
        String uid = "lucas-uid";

        String customToken = FirebaseAuth.getInstance().createCustomTokenAsync(uid).get();
        // Send token back to client
        // [END custom_token]
        System.out.println("Created custom token: " + customToken);
    }

    public static void createCustomTokenWithClaims() throws InterruptedException, ExecutionException {
        // [START custom_token_with_claims]
        String uid = "lucas-uid";
        Map<String, Object> additionalClaims = new HashMap<String, Object>();
        additionalClaims.put("premiumAccount", true);

        String customToken = FirebaseAuth.getInstance()
                .createCustomTokenAsync(uid, additionalClaims).get();
        // Send token back to client
        // [END custom_token_with_claims]
        System.out.println("Created custom token: " + customToken);
    }

    public static void verifyIdToken(String idToken) throws InterruptedException, ExecutionException {
        // [START verify_id_token]
        // idToken comes from the client app (shown above)
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
        String uid = decodedToken.getUid();
        // [END verify_id_token]
        System.out.println("Decoded ID token from user: " + uid);
    }

    public static void verifyIdTokenCheckRevoked(String idToken) throws InterruptedException, ExecutionException {
        // [START verify_id_token_check_revoked]
        try {
            // Verify the ID token while checking if the token is revoked by passing checkRevoked
            // as true.
            //boolean checkRevoked = true;
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
            // Token is valid and not revoked.
            String uid = decodedToken.getUid();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof FirebaseAuthException) {
                FirebaseAuthException authError = (FirebaseAuthException) e.getCause();
                if (authError.getErrorCode().equals("id-token-revoked")) {
                    // Token has been revoked. Inform the user to reauthenticate or signOut() the user.
                } else {
                    // Token is invalid.
                }
            }
        }
        // [END verify_id_token_check_revoked]
    }

    public static void revokeIdTokens(String idToken) throws InterruptedException, ExecutionException {
        String uid = "lucasUid";
        // [START revoke_tokens]
//    FirebaseAuth.getInstance().revokeRefreshTokensAsync(uid).get();
//    UserRecord user = FirebaseAuth.getInstance().getUserAsync(uid).get();
//    // Convert to seconds as the auth_time in the token claims is in seconds too. 
//    long revocationSecond = user.getTokensValidAfterTimestamp() / 1000;
//    System.out.println("Tokens revoked at: " + revocationSecond);
        // [END revoke_tokens]

        // [START save_revocation_in_db]
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("metadata/" + uid);
        Map<String, Object> userData = new HashMap();
        //userData.put("revokeTime", revocationSecond);
        ref.setValueAsync(userData).get();
        // [END save_revocation_in_db]

    }

    private boolean authenticateWithPrivileges() {
        String uid = "ZqUyhCnHIiVxmWhMtUaxvJRWYbm2"; //AIzaSyAGakZ_EbXIKU-Du86-rEhsadfvnnx8Wig"; 
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("ADICIONAR O DIRETÓRIO DO ARQUIVO DE CREDENCIAIS"); // adicione o caminho do arquivo de autenticação gerado do Auth do Firebase
            Map<String, Object> auth = new HashMap<String, Object>();
            auth.put(uid, "my-service-worker");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("ADICIONAR URL DO FIREBASE") // adicione a url do Firebase
                    .setDatabaseAuthVariableOverride(auth)
                    .build();
            FirebaseApp.initializeApp(options);
            return true;
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(AuthFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(AuthFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        //setReference( FirebaseDatabase.getInstance().getReference() );//indica um caminho para criar a tabela getReference exemplo "admin/energy"
    }

//    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
//        System.out.println("Hello, AuthSnippets!");
//
//        authenticateWithPrivileges();
//        //DatabaseReference usersRef = ref.child("users");//nome da tabela
//
//        Map<String, Usuario> users = new HashMap();
//        users.put("teste6", new Usuario("Fevereiro 02, 1995", "Lucas Bernardo"));
//        users.put("teste7", new Usuario("Janeiro 9, 1906", "Gragas Gordão Hopper"));
//        //getReference().child("users").setValueAsync(users);
//
//        // Smoke test
//        //createUserWithUid();
////        getUserById("lucas-uid");
////        getUserByEmail("bernardotriton@gmail.com");
////        getUserByPhoneNumber("+5584991770750");
////        //updateUser("lucas-uid");
////        //setCustomUserClaims("some-uid");
////        listAllUsers();
////        //deleteUser("some-uid");
////        createCustomToken();
//        //createCustomTokenWithClaims();
//        System.out.println("Done!");
//    }
//    public static DatabaseReference getReference() {
//        return reference;
//    }
//
//    public static void setReference(DatabaseReference reference) {
//        AuthFactory.reference = reference;
//    }
}
