package com.roi.teammeet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roi.teammeet.model.User;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText etFName, etLName, etPhone, etEmail, etPass;
    Button btnSignUp;

    String fName,lName, phone, email, pass;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        btnSignUp.setOnClickListener(this);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
    }

    private void initViews() {
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etPhone = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSignUp){
            fName=etFName.getText().toString();
            lName=etLName.getText().toString();
            phone=etPhone.getText().toString();
            email=etEmail.getText().toString();
            pass=etPass.getText().toString();


            //check if registration is valid
            Boolean isValid=true;
            if (fName.length()<2){
                etFName.setError("שם פרטי קצר מדי");
                isValid = false;
            }
            if (lName.length()<2){
                etLName.setError("שם משפחה קצר מדי");
                isValid = false;
            }
            if (phone.length()<9||phone.length()>10){
                etPhone.setError("מספר הטלפון לא תקין");
                isValid = false;
            }
            if (!email.contains("@")){
                etEmail.setError("כתובת האימייל לא תקינה");
                isValid = false;
            }
            if(pass.length()<6){
                etPass.setError("הסיסמה קצרה מדי");
                isValid = false;
            }
            if(pass.length()>20){
                etPass.setError("הסיסמה ארוכה מדי");
                isValid = false;
            }

            if (isValid==true){

                etFName.setText("");
                etLName.setText("");
                etPhone.setText("");
                etEmail.setText("");
                etPass.setText("");

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser fireuser = mAuth.getCurrentUser();
                                    User newUser=new User(fireuser.getUid(), fName, lName,phone, email,pass);
                                    myRef.child(fireuser.getUid()).setValue(newUser);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString("email", email);
                                    editor.putString("password", pass);

                                    editor.commit();
                                    Intent goLog=new Intent(getApplicationContext(), Register.class);
                                    startActivity(goLog);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                                // ...
                            }
                        });
            }
        }
    }
}