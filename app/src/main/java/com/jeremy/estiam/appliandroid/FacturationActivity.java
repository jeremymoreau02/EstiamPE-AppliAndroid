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

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Adresse;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.ResponsePerso;
import com.jeremy.estiam.appliandroid.models.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class FacturationActivity extends AppCompatActivity {


    EditText cp;
    EditText rue;
    EditText ville;
    EditText firstname;
     EditText name;
    CheckBox useAdresseCompte;
     CheckBox saveAdresseCompte;

    boolean adressHasExisted = false;

    protected User user = new User();
    protected Adresse adresseUser = new Adresse();
    protected Adresse adresseUserBilling= new Adresse();

    String token;
    PanierManager pm = new PanierManager(this);
    boolean valider = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facturation);

        cp = (EditText)findViewById(R.id.CPFactValue);
        rue = (EditText)findViewById(R.id.rueFactValue);
        ville = (EditText)findViewById(R.id.VilleFactValue);
        firstname = (EditText)findViewById(R.id.PrenomFactValue);
        name = (EditText)findViewById(R.id.NomFactValue);
        useAdresseCompte = (CheckBox)findViewById(R.id.useAdresseCompte);


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
        user.setUserId(Integer.parseInt( sharedPreferences.getString("id", "NULL")));

        token=sharedPreferences.getString("token","NULL");
        user.setToken(token );

        AdressesRecupTask adressesRecupTask = new AdressesRecupTask();
        adressesRecupTask.execute();

    }

    public class AdressesRecupTask extends AsyncTask<Void, Void , Adresse> {
        @Override
        protected Adresse doInBackground(Void... voids) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<List<Adresse>> call = apiService.getAdresses(user.getUserId(), token);

            try {
                Response<List<Adresse>> adresseResponse = call.execute();
                // user = userResponse.body();
                List<Adresse> adresses = adresseResponse.body();
                Iterator<Adresse> iterator = adresses.iterator();
                while(iterator.hasNext()){
                    Adresse a = iterator.next();
                    if(a.getType().equals("Shipping")){
                        adresseUser = a;
                        adresseUser.setUserId(user.getUserId());
                    }
                    if(a.getType().equals("Billing")){
                        adresseUserBilling = a;
                        adresseUserBilling.setUserId(user.getUserId());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return adresseUser;
        }

        @Override
        protected void onPostExecute(Adresse adresse){
            super.onPostExecute(adresse);
                if(valider){
                    pm.open();
                    Panier panier = pm.getPanier(user.getUserId());
                    panier.setBillingAdressId(adresseUserBilling.getId());
                    pm.modPanier(panier);
                    pm.close();
                    Intent intent = new Intent(FacturationActivity.this, LivraisonActivity.class);
                    startActivity(intent);
                }
        }


    }

    public class AdresseUpdateTask extends AsyncTask<View, Void , String> {
        protected String doInBackground(View... views) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);
            Call<ResponsePerso> call;

            if(adresseUserBilling.getId() !=0) {
                call = apiService.updateAdresse(adresseUserBilling.getId(), token, adresseUser);
            }else{
                call = apiService.createAdresse(token, adresseUser);
            }



            try {
                Response<ResponsePerso> adressResponse = call.execute();
                ResponsePerso rep = adressResponse.body();
                if(rep.getSuccess()){
                    Snackbar.make(views[0],"Adresse mises à jour", Snackbar.LENGTH_LONG).show();
                    return "";
                }else{
                    Snackbar.make(views[0],"Erreur", Snackbar.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String str){
            super.onPostExecute(str);
            if(str.equals("")){
                valider = true;
                    AdressesRecupTask adressesRecupTask = new AdressesRecupTask();
                    adressesRecupTask.execute();

            }
        }



    }



    @OnClick(R.id.validerFacturation)
    protected  void  onClickValiderFacturation(View v){
        boolean erreur = false;
        if(useAdresseCompte.isChecked()){
            if(adresseUser.getId() != 0){
                adresseUser.setType("Billing");
                AdresseUpdateTask adresseUpdateTask = new AdresseUpdateTask();
                adresseUpdateTask.execute(v);
            }else{
                Snackbar.make(v,"Le compte ne possède pas d'adresse", Snackbar.LENGTH_LONG).show();
            }




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

                adresseUser.setCity(ville.getText().toString());
                adresseUser.setStreet(rue.getText().toString());
                adresseUser.setZC(cp.getText().toString());
                    adresseUser.setType("Billing");
                    adresseUser.setUserId(user.getUserId());
                    adresseUser.setCreatedAt("");
                    adresseUser.setUpdatedAt("");

                AdresseUpdateTask adresseUpdateTask = new AdresseUpdateTask();
                adresseUpdateTask.execute(v);
            }
        }
    }

    public class UserUpdateTask extends AsyncTask<View, Void , Void> {
        protected Void doInBackground(View... views) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<String> call = apiService.updateUser(user.getUserId(), token, user);

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

            Call<User> call = apiService.getUser(user.getUserId(), user.getToken());

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
                Panier panier = pm.getPanier(user.getUserId());
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
