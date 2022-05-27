package com.example.n2tsi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class UsuarioLogado extends AppCompatActivity {

    EditText edBuscar;
    Button btlogout;
    RecyclerView recyclerView;
    ArrayList<Filme> filmeArrayList = new ArrayList<>();
    String busca = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_logado);

        btlogout = findViewById(R.id.buttonLogout);
        recyclerView = findViewById(R.id.recyclerView);
        edBuscar = findViewById(R.id.edBusca);


        setInfo();

        setRecyclerView();

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(UsuarioLogado.this, "Logout", Toast.LENGTH_LONG).show();

                startActivity(new Intent(UsuarioLogado.this, MainActivity.class));
            }
        });

    }

    private void setInfo() {
                if(edBuscar.getText().toString().equals("")){
                    busca = "movie";
                }
                else
                    busca = edBuscar.getText().toString();

                String url = "https://www.omdbapi.com/?s=" + busca + "&apikey=fce85cc5";
                DownloadDados dados = (DownloadDados) new DownloadDados().execute(url);

                filmeArrayList = dados.getFilmeArrayList();
    }

    private void setRecyclerView() {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(filmeArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

}