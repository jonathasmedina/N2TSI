package com.example.n2tsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edEmail, edSenha;
    TextView tvRecSenha, tvCriaUsuario;
    Button btLogar;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edEmail  = findViewById(R.id.editTextEmail);
        edSenha  = findViewById(R.id.editTextSenha);
        tvCriaUsuario  = findViewById(R.id.textViewCriaUsuario);
        tvRecSenha  = findViewById(R.id.textViewEsqueciSenha);
        btLogar  = findViewById(R.id.buttonLogin);
        progressBar  = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        tvCriaUsuario.setOnClickListener(this);
        tvRecSenha.setOnClickListener(this);
        btLogar.setOnClickListener(this);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin:
                logar();
                break;
            case R.id.textViewEsqueciSenha:
                i = new Intent(MainActivity.this, RecuperaSenha.class);
                startActivity(i);
                break;
            case R.id.textViewCriaUsuario:
                i = new Intent(MainActivity.this, CriaUsuario.class);
                startActivity(i);
                break;
        }
    }

    private void logar() {
        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();

        if(email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Preencha corretamente");
            edEmail.requestFocus();
            return;
        }

        if(senha.equals("")){
            edSenha.setError("Preencha corretamente");
            edSenha.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //verificar cadastro via email (link para clicar e ativar cadastro)
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                  //  if(user.isEmailVerified()){
                        //se já verificou via email, redireciona login
                        i = new Intent(MainActivity.this, UsuarioLogado.class);
                        startActivity(i);
                  //  }
                  //  else {
                   //     Toast.makeText(MainActivity.this, "Verifique conta via email.", Toast.LENGTH_LONG).show();
                   //     user.sendEmailVerification();
                 //   }
                }
                else //login falhou
                    Toast.makeText(MainActivity.this, "Erro ao logar", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
        });


    }
}