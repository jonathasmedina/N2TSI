package com.example.n2tsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//Esta tela faz uma consulta ao Firebase, que retorna uma coleção de Filmes adicionados aos favoritos
//que são representados também em um RecyclerView
//O recyclerAdapter utilizado é o mesmo da tela de consulta na API.

//funciona e é composta de maneira similar à tela de consulta à API (UsuarioLogado),
//contudo com dados vindos do Firebase
public class UsuarioFavoritos extends AppCompatActivity {

    TextView textViewLogout;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Filme> filmeArrayListFavoritos = new ArrayList<>();

    RecyclerAdapter recyclerAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_favoritos);

        recyclerView = findViewById(R.id.recyclerViewFav);
        textViewLogout = findViewById(R.id.textViewLogout);
        searchView = findViewById(R.id.searchViewFav);

        //searchView aberto
        searchView.setIconified(false);
        //retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //botão para efetuar logout
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(UsuarioFavoritos.this, "Logout", Toast.LENGTH_LONG).show();

                startActivity(new Intent(UsuarioFavoritos.this, MainActivity.class));
            }
        });

        //método para consultar o Firebase e popular o arraylist com os dados
        setInfo();

        //novamente, após a primeira exibição (acima), o usuário pode
        //fazer uma consulta de um termo entre os filmes favoritos salvos
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                //método filtrar() definido na classe RecyclerAdapter,
                //parâmetro = o que foi digitado na busca
                //diferente da abordagem da tela UsuarioLogado pois
                //os dados já estão salvos e o array populado, não é necessária nova requisição ou
                //nova busca no banco ou nova url
                recyclerAdapter.filtrar(s);
                //notifica o adapter para alterações na lista
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //igual acima, para digitação sem submit
                recyclerAdapter.filtrar(s);
                //notifica o adapter para alterações na lista
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setInfo() {
        Query query;

        //usuário logado no momento
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //limpando o array para a consulta
        filmeArrayListFavoritos.clear();

        //o caminho da query no Firebase (todos os filmes)
        query = databaseReference.child(user.getUid()).child("Filmes");

        //execução da query. Caso haja dados, cai no método onDataChange
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //este método é assíncrono, se não houver validação dos dados,
                //a lista será montada incorretamente pois não aguarda a consulta
                //assim, o if seguinte é necessário:
                if (dataSnapshot != null) {
                    for (DataSnapshot objDataSnapshot1 : dataSnapshot.getChildren()) {
                        Filme f = objDataSnapshot1.getValue(Filme.class);
                        Log.e("dentro do laço", "dentro");
                        filmeArrayListFavoritos.add(f);
                    }
                    //setRecyclerView() para montagem e configuração da RecyclerView mas
                    //neste caso, setRecyclerView() tem que ser chamado aqui (dentro e ao final de onDataChange),
                    //de forma que é executado somente após os dados acima serem baixados do Firebase
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setRecyclerView() {

        recyclerAdapter = new RecyclerAdapter(filmeArrayListFavoritos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}