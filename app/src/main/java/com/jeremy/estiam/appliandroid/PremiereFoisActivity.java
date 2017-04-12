package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Dimensions;
import com.jeremy.estiam.appliandroid.models.DimensionsManager;
import com.jeremy.estiam.appliandroid.models.Masks;
import com.jeremy.estiam.appliandroid.models.MasksManager;
import com.jeremy.estiam.appliandroid.models.MySQLite;
import com.jeremy.estiam.appliandroid.models.Shipping;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PremiereFoisActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premiere_fois);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);



        if(PremiereFoisActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).contains("DejaUtilise")){
            Intent intent = new Intent(PremiereFoisActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.accueil_button)
    public void onClickSuivant(View view) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("DejaUtilise", "oui").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }





    public void finish(){


    }

}

