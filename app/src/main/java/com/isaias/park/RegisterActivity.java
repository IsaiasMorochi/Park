package com.isaias.park;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password;
    Button register;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //instancia de autenticacion
        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userE = email.getText().toString();
                String passE = password.getText().toString();

                if (TextUtils.isEmpty(userE)){
                    Toast.makeText(getApplicationContext(), "Coloca un email", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(passE)){
                    Toast.makeText(getApplicationContext(), "Coloca un password", Toast.LENGTH_SHORT).show();
                }

                //metodo para comunicar con firebase, recibe 2 string email,pass
                //Listener para habisarnos q si esta bien o esta mal hecho
                auth.createUserWithEmailAndPassword(userE,passE)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Cuando el usuario ya se logre obtenerlo para la BD
                                Toast.makeText(getApplicationContext(), "Se ha creado el usuario", Toast.LENGTH_SHORT).show();

                                //al ser tareas, preguntamos al Task si este fue exitoso, si no lo fue entonces
                                if (!task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Tenemos un problema", Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(RegisterActivity.this, Home.class);
                                startActivity(intent);

                                //cerrar ventana de register
                                finish();
                            }
                        });
            }
        });
    }
}
