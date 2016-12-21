package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Message;
import com.jeremy.estiam.appliandroid.models.User;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {
    @BindView(R.id.valueMessage)
    EditText valeurMessage;
    @BindView(R.id.valueObjet)
    EditText valeurObjet;

    protected Message message = new Message();
    protected User user = new User();
    protected String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("id", "NULL").equals("")||sharedPreferences.getString("id", "NULL").equals("NULL")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        user.setId(Integer.parseInt( sharedPreferences.getString("id", "NULL")));

        token=sharedPreferences.getString("token","NULL");
        user.setToken(sharedPreferences.getString("token","NULL") );

        UserRecupTask userRecupTask = new UserRecupTask();
        userRecupTask.execute();
    }

    @OnClick(R.id.okMessage)
    public void onClickMessage(View view) {
        boolean bon = true;
         if(valeurMessage.getText().toString().equals("")){
             valeurMessage.setError("Veuillez insérer un message ");
             bon=false;
         }

        if(valeurObjet.getText().toString().equals("")){
            valeurObjet.setError("Veuillez préciser un objet ");
            bon=false;
        }
        if(bon){
            message.setMessage(valeurMessage.getText().toString());
            message.setSubject(valeurObjet.getText().toString());
            message.setEmail(user.getEmail());
            ContactTask contactTask = new ContactTask();
            contactTask.execute( view);
        }

    }

    public class ContactTask extends AsyncTask<View, Void , Void> {
        @Override
        protected Void doInBackground( View... view ) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<String> call = apiService.sendMessage(token, message);

            try {
                Response<String> userResponse = call.execute();
                if(userResponse.body().equals("The message has been sent to the administrator")){
                    Snackbar.make(view[0],"Message envoyé", Snackbar.LENGTH_LONG).show();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ContactActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(view[0],"Erreur", Snackbar.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    @OnClick(R.id.button_parametres)
    public void onClickUserInfo(){
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_deconnexion)
    public void onClickDeconnexion(){
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("token","NULL").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_home)
    public void onClickHome(){
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
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


    }

}
