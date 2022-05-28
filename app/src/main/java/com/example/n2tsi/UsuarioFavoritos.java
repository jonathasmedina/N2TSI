package com.example.n2tsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

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
        searchView.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

        //logout
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(UsuarioFavoritos.this, "Logout", Toast.LENGTH_LONG).show();

                startActivity(new Intent(UsuarioFavoritos.this, MainActivity.class));
            }
        });


        setInfo();

        //todo backspace = refazer a lista;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerAdapter.filtrar(s);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerAdapter.filtrar(s);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

        });
    }

    private void setInfo() {
        Query query;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        filmeArrayListFavoritos.clear();

        query = databaseReference.child(user.getUid()).child("Filmes");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if para aguardar pelo retorno do Firebase, que é assíncrono
                if (dataSnapshot != null) {
                    for (DataSnapshot objDataSnapshot1 : dataSnapshot.getChildren()) {
                        Filme f = objDataSnapshot1.getValue(Filme.class);
                        Log.e("dentro do laço", "dentro");
                        filmeArrayListFavoritos.add(f);
                    }
                    //tem que ser chamado dentro e ao final de onDataChange,
                    //para o await do download dos dados
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