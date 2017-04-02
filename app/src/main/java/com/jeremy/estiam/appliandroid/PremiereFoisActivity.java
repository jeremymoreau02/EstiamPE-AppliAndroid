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

    List<Masks> masks;
    List<Dimensions> dimensions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premiere_fois);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);

        masksRecupTask mrt = new masksRecupTask();
        mrt.execute();

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



public class masksRecupTask extends AsyncTask<Void, Void , List<Masks>> {
    @Override
    protected List<Masks> doInBackground(Void... voids) {

        ApiService apiService = new ServiceGenerator().createService(ApiService.class);

        Call<List<Masks>> call = apiService.getMasks(PremiereFoisActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token", "NULL"));

        try {
            Response<List<Masks>> masksResponse = call.execute();
            // user = userResponse.body();
            masks = masksResponse.body();
            dimensionsRecupTask drt = new dimensionsRecupTask();
            drt.execute();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}



    public class dimensionsRecupTask extends AsyncTask<Void, Void , String> {
        @Override
        protected String doInBackground(Void... voids) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<List<Dimensions>> call = apiService.getDimensions( PremiereFoisActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token","NULL"));

            try {
                Response<List<Dimensions>> shippingResponse = call.execute();
                // user = userResponse.body();
                dimensions = shippingResponse.body();
                if((masks != null)&&(dimensions!=null)){
                    MasksManager mm = new MasksManager(PremiereFoisActivity.this);
                    mm.open();
                    mm.addMasks(masks);
                    mm.close();
                    DimensionsManager dm = new DimensionsManager(PremiereFoisActivity.this);
                    dm.open();
                    dm.addDimensions(dimensions);
                    dm.close();
                }

                if(PremiereFoisActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).contains("DejaUtilise")){
                    Intent intent = new Intent(PremiereFoisActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
    }

    public void finish(){


    }

}

