package com.example.n2tsi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

//Adapter do Recycler View
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Filme> filmeArrayListLocal;
    ArrayList<Filme> filmeArrayListCopia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RecyclerAdapter(ArrayList<Filme> filmeArrayListLocal_) {
        this.filmeArrayListLocal = filmeArrayListLocal_;
        //duplicar o array original no construtor para manipularmos a lista sem problemas.
        filmeArrayListCopia = new ArrayList<>(filmeArrayListLocal);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewHolder com XML do formato dos itens da lista...
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //método para pegar o valor de cada item e setar nas views da tela;
        String titulo = filmeArrayListLocal.get(position).getTitulo();
        String ano = filmeArrayListLocal.get(position).getAno();
        String imagem = filmeArrayListLocal.get(position).getUrlImg();

        holder.mTextViewTit.setText(titulo);
        holder.mTextViewAno.setText(ano);

        //para abrir uma imagem a partir de uma URL, foi utilizada a biblioteca GLIDE
        //necessária inserção das dependências no arquivo Build.Gradle
        //https://github.com/bumptech/glide
        Glide.with(holder.mImageView.getContext()).load(imagem).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return filmeArrayListLocal.size();
    }

    //class ViewHolder de conexão com os elementos da tela e config do Firebase Realtime Database
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mTextViewTit;
        TextView mTextViewAno;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTit = itemView.findViewById(R.id.textViewTitulo);
            mTextViewAno = itemView.findViewById(R.id.textViewAno);

            mImageView = itemView.findViewById(R.id.imageView);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            //configurando o item da lista para aceitar cliques
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //onClick no item da RecyclerView
            //tem dois comportamentos:
            //1 - se estivermos na tela de listagem da API: confirmação + salvar nos favoritos
            //2 - se estivermos na tela dos favoritos: confirmação + exclusão dos favoritos
            //utilizamos este mesmo RecyclerAdapter para ambas, então necessário validar a tela:

            //1 - se estou na tela UsuarioLogado (listagem da API)
            if (view.getContext().toString().contains("UsuarioLogado")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar filme")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setIcon(R.drawable.ic_baseline_favorite_border_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, salvar no Firebase, método "inserirEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Título salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                                inserirEm(getLayoutPosition());

                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
            //2 - estou na tela de favoritos, remover item
            else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Remover")
                        .setMessage("Confirma remover dos favoritos?")
                        .setIcon(R.drawable.ic_baseline_delete_outline_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, remover do Firebase, método "removerEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Título removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                removerEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
        }

        //inserção no Firebase - filmes favoritos do usuário
        private void inserirEm(int layoutPosition) {
            //id do usuário logado no momento
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //objeto da lista clicado
            Filme f = filmeArrayListLocal.get(layoutPosition);

            //salvo o objeto no Firebase
            //este caminho é totalmente opcional
            //estrutura escolhida para salvar no banco:
            // nó id do usuário --> nó "Filmes --> nós "Títulos de filme" --> valores dos atributos
            databaseReference.child(user.getUid()).
                    child("Filmes").
                    child(f.getTitulo()).
                    setValue(f);
            //todo firebase não aceita no caminho, substituir  '.', '#', '$', '[', e ']'
            //erro ao tentar salvar título de filme com os caracteres acima
        }

    }

    //remover no Firebase - filmes favoritos do usuário
    public void removerEm(int layoutPosition) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Filme f = filmeArrayListLocal.get(layoutPosition);

        //limpa o array para correta renderização da lista
        //após remoção, array será remontado com os valores restantes do firebase
        filmeArrayListLocal.clear();

        databaseReference.child(user.getUid()).child("Filmes").
                child(f.getTitulo()).
                removeValue();
    }

    public void filtrar(String text) {
        //limpando array que monta a lista ao buscar algum termo na searchView
        filmeArrayListLocal.clear();

        //digitou algo e apagou = trazer todos
        //lembrando que filmeArrayListCopia contém toda a informação original
        //(populado no construtor)
        if (text.isEmpty()) {
            filmeArrayListLocal.addAll(filmeArrayListCopia);
        } else {
            //algum texto digitado na busca
            //converte para letra minúscula para não haver distinção
            text = text.toLowerCase();
            //percorre o array com os dados originais (todos os favoritos)
            for (Filme item : filmeArrayListCopia) {
                //caso, nos dados originais, exista o termo procurado, popule o array vazio com o item
                if (item.getTitulo().toLowerCase().contains(text) || item.getAno().toLowerCase().contains(text)) {
                    filmeArrayListLocal.add(item);
                }
            }
        }
    }
}
