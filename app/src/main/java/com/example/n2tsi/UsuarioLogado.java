package com.example.n2tsi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class UsuarioLogado extends AppCompatActivity {

    TextView textViewFav;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Filme> filmeArrayList = new ArrayList<>();

    Handler handler = new Handler();

    RecyclerAdapter recyclerAdapter;

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
        databaseReference = firebaseDatabase.getReference();

        textViewFav.setText("Gerenciar favoritos - " + FirebaseAuth.getInstance()
                .getCurrentUser().getEmail());

        //searchView aberto
        searchView.setIconified(false);
        searchView.clearFocus();

        try {
            setInfo("movie");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    setInfo(s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recyclerAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //considerar que aqui cada caractere digitado = uma requisição na API
                // esperar digitação do usuário para as requisições (400 milisegundos)

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // código igual ao submit acima:
                        try {
                            setInfo(s);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }, 400);

                return true;
            }
        });

        //salvar nos favoritos ao clicar:
        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UsuarioLogado.this, UsuarioFavoritos.class);
                startActivity(i);
            }
        });

    }

    private void setInfo(String busca) throws ExecutionException, InterruptedException {

        //busca = 3 caracteres no mínimo. Regra da API.

        //se o campo de busca for alterado e limpo na sequência, considerar valor padrão "movies"
        if(busca.trim().equals(""))
            busca = "movies";

        filmeArrayList.clear();

        String url = "https://www.omdbapi.com/?s=" + busca + "&apikey=fce85cc5";

        filmeArrayList.addAll(new DownloadDados().execute(url).get());

        Log.e("informações baixadas:", String.valueOf(filmeArrayList.size()));
    }

    private void setRecyclerView() {

        recyclerAdapter = new RecyclerAdapter(filmeArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

}