package com.example.pm1_ejercicio22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1_ejercicio22.Interfaces.Interfaces;
import com.example.pm1_ejercicio22.Models.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityRetrofit extends AppCompatActivity {

    ListView listVRetrofit;
    ArrayList<String> titles = new ArrayList<>();
    ArrayAdapter adl;

    Button btnBuscar, btnTodo;
    EditText txtBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);


        listVRetrofit = (ListView) findViewById(R.id.listViewJsonVolley);
        txtBuscar = (EditText) findViewById(R.id.txtBuscarJsonVolley);
        btnBuscar = (Button) findViewById(R.id.btnBuscarJsonVolley);
        btnTodo = (Button) findViewById(R.id.btnTodoJsonVolley);
        setListRe();

        obtenerLosUsuarios();

    }





    private void setListRe(){
        btnTodo.setOnClickListener(v -> obtenerLosUsuarios());
        btnBuscar.setOnClickListener(v -> GETUsuario());
    }


    private void GETUsuario(){

        if(txtBuscar.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "INGRESAR UN ID PARA PODER BUSCAR UN REGISTRO", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Interfaces interfacesUsers = retrofit.create(Interfaces.class);

        titles = new ArrayList<>();

        Call<Usuario> request = interfacesUsers.getUsuario(txtBuscar.getText().toString());

        request.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if(response.body() != null){
                    Usuario usuario = response.body();

                    titles.add(usuario.title);

                    adl = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);

                    listVRetrofit.setAdapter(adl);
                }else {
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS EL REGISTRO NO EXISTE", Toast.LENGTH_SHORT).show();
                    adl = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
                    listVRetrofit.setAdapter(adl);
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR: AL LEER DATOS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerLosUsuarios() {

        txtBuscar.setText(null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Interfaces interfacesUsers = retrofit.create(Interfaces.class);

        Call<List<Usuario>> request = interfacesUsers.getUsuarios();

        titles = new ArrayList<>();

        request.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                for(Usuario usuario: response.body()){
                    titles.add(usuario.getTitle());
                }

                adl = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titles);

                listVRetrofit.setAdapter(adl);
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });


    }


}