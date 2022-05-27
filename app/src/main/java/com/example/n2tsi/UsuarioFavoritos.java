package com.example.n2tsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UsuarioFavoritos extends AppCompatActivity {

    EditText edBuscar;
    TextView textViewLogout;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Filme> filmeArrayListFavoritos = new ArrayList<>();
    String busca = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_favoritos);

        recyclerView = findViewById(R.id.recyclerViewFav);
        textViewLogout = findViewById(R.id.textViewLogout);
        searchView = findViewById(R.id.searchViewFav);
        //todo montar filtro na tela de favoritos

        //logout
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(UsuarioFavoritos.this, "Logout", Toast.LENGTH_LONG).show();

                startActivity(new Intent(UsuarioFavoritos.this, MainActivity.class));
            }
        });


        setInfo("movie");
        setRecyclerView();


    }

    private void setInfo(String movie) {
        //todo recuperar informação do firebase, popular array e exibir a recycler
        //info do firebase
    }


    private void setRecyclerView() {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(filmeArrayListFavoritos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        //todo no adapter, implementar swipe nesta dela - excluir dos favoritos
    }
}