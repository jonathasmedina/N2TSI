package com.example.n2tsi;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

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


//Esta classe é responsável pelo download dos dados da API informada (url)
//Este processo deve ser Assíncrono, por isso:
// - herda de AsyncTask
// - possui os métodos onPreExecute(), doInBackground() e onPostExecute()
public class DownloadDados extends AsyncTask<String, Void, ArrayList<Filme>> {

    public ArrayList<Filme> filmeArrayList = new ArrayList<>();

    //primeiro método a ser executado
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("onPreExecute:", "...Download das informações");
    }
    //segundo método a ser executado
    //recebe a url, conecta, recebe o texto e faz o parse para o arraylist
    //o retorno deste método, que é o arraylistpopulado vai
    //para o método onPostExecute() (opcional)
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

    //método que recebe o texto de retorno da API e faz o parse
    private ArrayList<Filme> getDados(String texto) throws JSONException {

        //endpoint utilizado: https://www.omdbapi.com/?s=car&apikey=fce85cc5
        //a formatação a seguir depende totalmente do retorno da API utilizada.
        //para este exemplo/endpoint, temos:
        // um JSON Object{}, e dentro dele, um JSON Array[] composto de JSON Objects{}

        JSONObject jsonObjectItem_ = new JSONObject(texto);

        JSONArray jsonArray = new JSONArray(jsonObjectItem_.getString("Search"));

        //laço para iterar no JSON Array[]
        //que é composto de JSON Objects{}
        for (int i = 0; jsonArray.length() > i; i++) {
            JSONObject jsonObjectItem = jsonArray.getJSONObject(i);

            //para cada JSON Object = novo objeto e adiciona objeto no array.
            Filme filme = new Filme();
            filme.setTitulo(jsonObjectItem.getString("Title"));
            filme.setAno(jsonObjectItem.getString("Year"));
            filme.setUrlImg(jsonObjectItem.getString("Poster"));

            filmeArrayList.add(filme);

            //limitar tamanho da lista para os testes:
            if (i == 2)
                return filmeArrayList;
        }
        return filmeArrayList;
    }


    @Override
    protected void onPostExecute(ArrayList<Filme> filmes) {
        super.onPostExecute(filmes);
    }




}
