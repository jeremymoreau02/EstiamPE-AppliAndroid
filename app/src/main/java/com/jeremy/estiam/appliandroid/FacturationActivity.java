package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FacturationActivity extends AppCompatActivity {


    @BindView(R.id.CPFactValue) EditText cp;
    @BindView(R.id.rueFactValue)    EditText rue;
    @BindView(R.id.VilleFactValue)    EditText ville;
    @BindView(R.id.PrenomFactValue)    EditText firstname;
    @BindView(R.id.NomFactValue)    EditText name;
    @BindView(R.id.useAdresseCompte)
    CheckBox useAdresseCompte;
    @BindView(R.id.saveAdresseCompte)    CheckBox saveAdresseCompte;

    protected User user = new User();
    String token;
    PanierManager pm = new PanierManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setContentView(R.layout.activity_facturation);

        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
        Date date = new Date();
        String dateStr = sharedPreferences.getString("CreateDate", "NULL");
        if(!dateStr.equals("NULL")){
            Date date2 = null;
            try {
                date2 = dateFormat.parse(dateStr);

                Long date3 = date.getTime()-date2.getTime();
                System.out.println(date3);
                if (date3 > 86400000 ) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        user.setId(Integer.parseInt( sharedPreferences.getString("id", "NULL")));

        token=sharedPreferences.getString("token","NULL");
        user.setToken(token );
    }

    @OnClick(R.id.validerFacturation)
    protected  void  onClickValiderFacturation(View v){
        boolean erreur = false;
        if(useAdresseCompte.isChecked()){

            UserRecupTask userRecupTask = new UserRecupTask();
            userRecupTask.execute();

        }else {
            if(cp.getText().toString().equals("")){
               cp.setError("Veuillez indiquer un code postal");
                erreur=true;
            }
            if(rue.getText().toString().equals("")){
                rue.setError("Veuillez indiquer une rue");
                erreur=true;
            }

            if(ville.getText().toString().equals("")){
                ville.setError("Veuillez indiquer une ville");
                erreur=true;
            }

            if(name.getText().toString().equals("")){
                name.setError("Veuillez indiquer un nom");
                erreur=true;
            }

            if(firstname.getText().toString().equals("")){
                cp.setError("Veuillez indiquer un prénom");
                erreur=true;
            }

            if(!erreur){
                pm.open();
                Panier panier = pm.getPanier();
                panier.setCpFacturation(cp.getText().toString());
                panier.setRueFacturation(rue.getText().toString());
                panier.setVilleFacturation(ville.getText().toString());
                panier.setNomFacturation(name.getText().toString());
                panier.setPrenomFacturation(firstname.getText().toString());
                pm.modPanier(panier);
                pm.close();

                if(saveAdresseCompte.isChecked()){
                   /* user.set
                    UserUpdateTask userUpdateTask = new UserUpdateTask();
                    userUpdateTask.execute();*/
                }
            }
        }
    }

    public class UserUpdateTask extends AsyncTask<View, Void , Void> {
        protected Void doInBackground(View... views) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<String> call = apiService.updateUser(user.getId(), token, user);

            try {
                Response<String> userResponse = call.execute();
                if (userResponse.body().equals("User updated")) {

                } else {
                    Snackbar.make(views[0], "Erreur de mise à jour de l'adresse", Snackbar.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
    public class UserRecupTask extends AsyncTask<Void, Void , User> {
        @Override
        protected User doInBackground(Void... voids) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.getUser(user.getId(), user.getToken());

            try {
                Response<User> userResponse = call.execute();
                user = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return user;
        }

        @Override
        protected void onPostExecute(User user){
            super.onPostExecute(user);
            if(user != null){

                pm.open();
                Panier panier = pm.getPanier();
                panier.setCpFacturation(cp.getText().toString());
                panier.setRueFacturation(rue.getText().toString());
                panier.setVilleFacturation(ville.getText().toString());
                panier.setNomFacturation(name.getText().toString());
                panier.setPrenomFacturation(firstname.getText().toString());
                pm.modPanier(panier);
                pm.close();
            }
        }

    }
}
