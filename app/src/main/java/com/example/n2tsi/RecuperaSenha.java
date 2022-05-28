package com.example.n2tsi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperaSenha extends AppCompatActivity {

    EditText edRecSenha;
    Button btRecSenha_;
    FirebaseAuth mAuthRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);

        edRecSenha  = findViewById(R.id.editRecSenha);
        btRecSenha_  = findViewById(R.id.buttonRecSenha);

        mAuthRec = FirebaseAuth.getInstance();

        btRecSenha_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edRecSenha.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.equals("")){
                    edRecSenha.setError("Preencha corretamente");
                    return;
                }

                //envia e-mail para recuperação de senha
                mAuthRec.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RecuperaSenha.this, "E-mail enviado.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RecuperaSenha.this, MainActivity.class);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(RecuperaSenha.this, "Erro.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}