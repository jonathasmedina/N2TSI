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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class UsuarioLogado extends AppCompatActivity {

    EditText edBuscar;
    Button btlogout;
    TextView textViewFav;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Filme> filmeArrayList = new ArrayList<>();
    String busca = "";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_logado);

        recyclerView = findViewById(R.id.recyclerView);
        textViewFav = findViewById(R.id.textViewFav);
        searchView = findViewById(R.id.searchView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();


        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UsuarioLogado.this, UsuarioFavoritos.class);
                startActivity(i);
            }
        });

        //searchView aberto
        searchView.setIconified(false);
        searchView.clearFocus();

        setInfo("movie");
        setRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus(); //fecha teclado
                setInfo(s);
                setRecyclerView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void setInfo(String busca) {

        filmeArrayList.clear();

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