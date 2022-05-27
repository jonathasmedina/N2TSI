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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Filme> filmeArrayListLocal = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Filme> filmeArrayListLocal) {
        this.filmeArrayListLocal = filmeArrayListLocal;
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

//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "posição= " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            removeAt(getLayoutPosition());
        }

        private void removeAt(int layoutPosition) {
            filmeArrayListLocal.remove(layoutPosition);
            notifyItemRemoved(layoutPosition);
            notifyItemRangeChanged(layoutPosition, filmeArrayListLocal.size());
        }
    }
    }