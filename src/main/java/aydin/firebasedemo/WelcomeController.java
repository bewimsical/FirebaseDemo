package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class WelcomeController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneNumberTextField;


    @FXML
    private Button registerButton;

    @FXML
    private Button writeButton;


    @FXML
    void registerButtonClicked(ActionEvent event) {
        registerUser();
    }

    @FXML
    void writeButtonClicked(ActionEvent event) throws IOException {
        if (loginUser()){
            System.out.println("login successful!");
            DemoApp.setRoot("primary");
        }
        else {
            System.out.println("login failed");
        }
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(emailTextField.getText())
                .setEmailVerified(false)
                .setPassword(passwordTextField.getText())
                .setPhoneNumber("+" + phoneNumberTextField.getText())
                .setDisplayName(emailTextField.getText())
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = DemoApp.fauth.createUser(request);
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                    + " check Firebase > Authentication > Users tab");

            addData(userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating a new user in the firebase");
            return false;
        }

    }

    public boolean loginUser(){
        UserRecord userRecord;
        try {
            userRecord = DemoApp.fauth.getUserByEmail(emailTextField.getText());
            System.out.println("Successfully fetched user data: " + userRecord.getEmail());
            String pw = readFirebase(userRecord.getUid());
            if (pw.equals(passwordTextField.getText())) {
                return true;
            }else {
                System.out.println("passwords do not match");
                return false;
            }
        } catch (FirebaseAuthException e) {
            System.out.println("Error logging user in the firebase");
            return false;
        }
    }

    public void addData(String id) {
        DocumentReference docRef = DemoApp.fstore.collection("Users").document(id);
        Map<String, Object> data = new HashMap<>();
        data.put("password", passwordTextField.getText());
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public String readFirebase(String uuid){
        DocumentReference docRef = DemoApp.fstore.collection("Users").document(uuid);
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
// block on response
        DocumentSnapshot document = null;
        try {
            document = future.get();
            User user = null;
            if (document.exists()) {
                // convert document to POJO
                user = document.toObject(User.class);
                System.out.println(user.getPassword());
                return user.getPassword();
            } else {
                System.out.println("No such document!");
                return "";
            }
        } catch (InterruptedException | ExecutionException e) {
            return "";
        }
    }
}

