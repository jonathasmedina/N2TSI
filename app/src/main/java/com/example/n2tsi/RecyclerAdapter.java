package com.example.n2tsi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Filme> filmeArrayListLocal = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RecyclerAdapter(ArrayList<Filme> filmeArrayListLocal_) {
        this.filmeArrayListLocal = filmeArrayListLocal_;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//método para pegar o valor e jogar nas views da tela;
        String titulo = filmeArrayListLocal.get(position).getTitulo();
        String ano = filmeArrayListLocal.get(position).getAno();
        String imagem = filmeArrayListLocal.get(position).getUrlImg();

        holder.mTextViewTit.setText(titulo);
        holder.mTextViewAno.setText(ano);
//        holder.mImageView.setImageResource(imagem);

        //abrir imagem a partir de url.
        Glide.with(holder.mImageView.getContext()).load(imagem).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return filmeArrayListLocal.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextViewTit;
        TextView mTextViewAno;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTit = itemView.findViewById(R.id.textViewTitulo);
            mTextViewAno = itemView.findViewById(R.id.textViewAno);

            mImageView = itemView.findViewById(R.id.imageView);

            firebaseDatabase = FirebaseDatabase.getInstance();
//            firebaseDatabase.setPersistenceEnabled(true);
            databaseReference = firebaseDatabase.getReference();


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //removeAt(getLayoutPosition());
            inserirEm(getLayoutPosition());
            Toast.makeText(view.getContext(), "Título salvo nos favoritos.", Toast.LENGTH_SHORT).show();
        }

        private void inserirEm(int layoutPosition) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Filme f = filmeArrayListLocal.get(layoutPosition);

            databaseReference.child(user.getUid()).
                    child("Filmes").
                    child(f.getTitulo()).
                    setValue(f);
        }

        private void removeAt(int layoutPosition) {
            filmeArrayListLocal.remove(layoutPosition);
            notifyItemRemoved(layoutPosition);
            notifyItemRangeChanged(layoutPosition, filmeArrayListLocal.size());
        }
    }

    public void filtrar(String text) {
        ArrayList<Filme> filmeArrayListCopia = new ArrayList<>(filmeArrayListLocal);

        //limpando array da lista
        filmeArrayListLocal.clear();

        if(text.isEmpty()) {
            filmeArrayListLocal.addAll(filmeArrayListCopia);
        }
        else{
            text = text.toLowerCase();
            for(Filme item: filmeArrayListCopia){
                if(item.getTitulo().toLowerCase().contains(text) || item.getAno().toLowerCase().contains(text)){
                    filmeArrayListLocal.add(item);
                }
            }
        }
        notifyDataSetChanged();
        }
}
