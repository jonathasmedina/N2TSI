package com.example.n2tsi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


//Esta tela exibe uma coleção de filmes vindos da API "OMDBAPI"
//Para acesso aos dados, necessário criar uma Key de acesso em http://www.omdbapi.com/apikey.aspx
//Documentação: https://www.omdbapi.com/
//Exemplo de endpoint para consulta de um filme: https://www.omdbapi.com/?s=movie&apikey=fce85cc5
//Key gratuita = 1000 requisições por dia.
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

        //recuperando dados do usuário logado para exibir na tela
        textViewFav.setText("Gerenciar favoritos - " + FirebaseAuth.getInstance()
                .getCurrentUser().getEmail());

        //searchView sempre aberto
        searchView.setIconified(false);
        //retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView.clearFocus();

        try {
            //setInfo() = método que popula o arraylist com dados vindos da API;
            //Obs:
            //- throws/try/catch exigido pelo get(), que é assíncrono.
            //- envio da string "movie" para trazer alguma coleção de dados por padrão.
            setInfo("movie");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //ao término do método setInto, com o arraylist populado, montar RecyclerView e Adapter
        setRecyclerView();

        //após a primeira execução do método que popula e monta a lista (feito automaticamente)
        //o usuário pode querer buscar por um termo no SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //método para submit
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    //executamos novamente o método setInfo(), agora com o termo da busca
                    //isto vai gerar uma nova consulta com uma nova URL e
                    //um novo conjunto de dados no arraylist
                    setInfo(s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //"avisar" o adapter para atualização da RecyclerView
                recyclerAdapter.notifyDataSetChanged();

                return true;
            }

            //método ao alterar o texto - similar ao TextWatcher
            @Override
            public boolean onQueryTextChange(String s) {
                //considerar que aqui cada caractere digitado = uma requisição na API
                //a rotina abaixo espera a digitação do usuário para as requisições (400 milisegundos)

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

        //ir para a tela com os favoritos do usuário:
        textViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UsuarioLogado.this, UsuarioFavoritos.class);
                startActivity(i);
            }
        });

    }

    private void setInfo(String busca) throws ExecutionException, InterruptedException {

        //Obs: busca = 3 caracteres no mínimo. Regra da API.

        //se o campo de busca for alterado e limpo na sequência, considerar valor padrão "movies"
        if (busca.trim().equals(""))
            busca = "movies";

        //limpa o array
        filmeArrayList.clear();

        //monta a url de busca com a key
        String url = "https://www.omdbapi.com/?s=" + busca + "&apikey=fce85cc5";

        //executa a classe DownloadDados, que retorna o array populado e popula o array local
        //get() = executando de forma assíncrona, ou seja, aguarda a consulta e os dados
        //para popular o array
        filmeArrayList.addAll(new DownloadDados().execute(url).get());

        Log.e("informações baixadas:", String.valueOf(filmeArrayList.size()));
    }

    private void setRecyclerView() {

        //instanciando a classe RecyclerAdapter com o arraylist populado
        recyclerAdapter = new RecyclerAdapter(filmeArrayList);

        //setando a recyclerView para correta exibição com os itens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}