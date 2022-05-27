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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CriaUsuario extends AppCompatActivity {

    EditText edNome_, edEmail_, edSenha_;
    Button btCria;

    FirebaseAuth mAuthCria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_usuario);

        edNome_  = findViewById(R.id.editCriaNome);
        edEmail_  = findViewById(R.id.editCrieEmail);
        edSenha_  = findViewById(R.id.editCriaSenha);
        btCria  = findViewById(R.id.buttonCria);

        mAuthCria = FirebaseAuth.getInstance();

        btCria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarUSuario(edEmail_.getText().toString(), edSenha_.getText().toString());
            }
        });

    }

    private void criarUSuario(String email, String senha) {

        if(edNome_.getText().toString().equals("")){
            edNome_.setError("Preencha corretamente");
            edNome_.requestFocus();
            return;
        }

        if(email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail_.setError("Preencha corretamente");
            edEmail_.requestFocus();
            return;
        }

        if(senha.equals("")){
            edSenha_.setError("Preencha corretamente");
            edSenha_.requestFocus();
            return;
        }

        mAuthCria.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Usuário criado com sucesso", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CriaUsuario.this, MainActivity.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(), "Erro ao criar usuário", Toast.LENGTH_LONG).show();
            }
        });
    }
}