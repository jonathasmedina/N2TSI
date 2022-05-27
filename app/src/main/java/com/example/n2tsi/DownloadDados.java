package com.example.n2tsi;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DownloadDados extends AsyncTask<String, Void, ArrayList<Filme>> {

    public ArrayList<Filme> filmeArrayList = new ArrayList<>();

    public ArrayList<Filme> getFilmeArrayList() {
        return filmeArrayList;
    }

    @Override
    protected ArrayList<Filme> doInBackground(String... strings) {
        String urlString = strings[0];
        URL url;

        try {
            url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(15000000);
            httpURLConnection.connect();

            InputStream resposta = httpURLConnection.getInputStream();
            String texto = new Scanner(resposta).useDelimiter("\\A").next();

            if (texto!=null) {
                filmeArrayList = getDados(texto);
                return filmeArrayList;

            }else
                return null;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filmeArrayList;
    }

    private ArrayList<Filme> getDados(String texto) throws JSONException {
        //endpoint: https://www.omdbapi.com/?s=car&apikey=fce85cc5

        JSONObject jsonObjectItem_ = new JSONObject(texto);

        JSONArray jsonArray = new JSONArray(jsonObjectItem_.getString("Search"));

        for (int i = 0; jsonArray.length() > i; i++) {
            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
            Filme filme = new Filme();
            filme.setTitulo(jsonObjectItem.getString("Title"));
            filme.setAno(jsonObjectItem.getString("Year"));
            filme.setUrlImg(jsonObjectItem.getString("Poster"));

            filmeArrayList.add(filme);

            //limitar 5 filmes:
            if (i == 2)
                return filmeArrayList;
        }
        return filmeArrayList;
    }

}
