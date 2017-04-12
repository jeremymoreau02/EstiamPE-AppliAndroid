package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Dimensions;
import com.jeremy.estiam.appliandroid.models.DimensionsManager;
import com.jeremy.estiam.appliandroid.models.Masks;
import com.jeremy.estiam.appliandroid.models.MasksManager;
import com.jeremy.estiam.appliandroid.models.User;
import com.jeremy.estiam.appliandroid.models.UserConnection;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity{

    @BindView(R.id.login_editText)
    EditText loginView;
    @BindView(R.id.password_edittext)
    EditText passwordView;

    User user= new User();
    UserConnection userConnection = new UserConnection();
    String password;
    String login;
    boolean coGood = false;

    List<Masks> masks;
    List<Dimensions> dimensions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                if (date3 < 86400000 ) {
                    Intent intent = new Intent(this, RecyclerActivity.class);
                    startActivity(intent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        ButterKnife.bind(this);



    }

    @OnClick(R.id.Sinscrire_button)
    public void onClickSinscrire(View view) {
        Intent intent = new Intent(this, InscriptionActivity.class);
        intent.putExtra("idUser", user.getUserId());
        startActivity(intent);

    }

    @OnClick(R.id.connect_button)
    public void onClickConnexion(View view) {
        final boolean[] response = {true};
        password=passwordView.getText().toString();
        login=loginView.getText().toString();



        if (!password.equals("") && !login.equals("")) {
            userConnection.setPassword(password);
            userConnection.setPseudo(login);

            UserTask userTask = new UserTask();
            userTask.execute( view);



        } else {
            if(passwordView.getText().toString().equals("")) passwordView.setError("Veuillez saisir votre mot de passe  ");
            if(loginView.getText().toString().equals("")) loginView.setError("Veuillez saisir votre login  ");
        }


    }

    public class UserTask extends AsyncTask<View, Void , User> {
        @Override
        protected User doInBackground( View... view ) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.connection(userConnection);

            User userRes= new User();

            try {
                Response<User> userResponse = call.execute();
                userRes = userResponse.body();
                user=userRes;
                if(user.getToken()==null){
                    Snackbar.make(view[0] ,"identifiants incorrects",Snackbar.LENGTH_LONG).show();
                }else{
                    coGood = true;
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("token", user.getToken()).apply();
                    sharedPreferences.edit().putString("id", Integer.toString(user.getUserId())).apply();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
                    Date date = new Date();
                    sharedPreferences.edit().putString("CreateDate", dateFormat.format(date)).apply();

                    masksRecupTask mrt = new masksRecupTask();
                    mrt.execute();


                }
            } catch (IOException e) {
                e.printStackTrace();
                Snackbar.make(view[0] ,"Connectez-vous à Internet",Snackbar.LENGTH_LONG).show();
            }




            return userRes;
        }

        protected void onPostExecute(User result) {
            Log.w("ghtfytgr", "rfgfd");
        }
    }


    @Override
    public void onBackPressed() {
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
                if (date3 < 86400000 ) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        super.onBackPressed();
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    public class masksRecupTask extends AsyncTask<Void, Void , List<Masks>> {
        @Override
        protected List<Masks> doInBackground(Void... voids) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            String token = LoginActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token", "NULL");

            Call<List<Masks>> call = apiService.getMasks(token);

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

            Call<List<Dimensions>> call = apiService.getDimensions( LoginActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token","NULL"));

            try {
                Response<List<Dimensions>> shippingResponse = call.execute();
                // user = userResponse.body();
                dimensions = shippingResponse.body();
                if((masks != null)&&(dimensions!=null)){
                    MasksManager mm = new MasksManager(LoginActivity.this);
                    mm.open();
                    mm.addMasks(masks);
                    mm.close();
                    DimensionsManager dm = new DimensionsManager(LoginActivity.this);
                    dm.open();
                    dm.addDimensions(dimensions);
                    dm.close();

                    Intent intent = new Intent(LoginActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
    }



}
