package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.NoSSLv3SocketFactory;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.User;
import com.jeremy.estiam.appliandroid.models.UserConnection;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity{

    @BindView(R.id.login_editText)
    EditText loginView;
    @BindView(R.id.password_edittext)
    EditText passwordView;

    User user= new User();
    UserConnection userConnection = new UserConnection();
    String password;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @OnClick(R.id.Sinscrire_button)
    public void onClickSinscrire(View view) {
        Intent intent = new Intent(this, InscriptionActivity.class);
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
        protected User doInBackground(View... view ) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.connection(userConnection);

            User userRes= new User();

            try {
                Response<User> userResponse = call.execute();
                userRes = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            user=userRes;
            if(user.getPrenom()==null){
                Snackbar.make(view[0] ,"identifiants incorrects",Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(view[0] ,user.getEmail(),Snackbar.LENGTH_LONG).show();
            }


            return userRes;
        }

        protected void onPostExecute(User result) {
            Log.w("ghtfytgr", "rfgfd");
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



}
