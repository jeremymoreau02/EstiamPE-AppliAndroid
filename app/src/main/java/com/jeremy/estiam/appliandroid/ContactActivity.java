package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Message;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_when_connected, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Home_drawer:
                Intent intent = new Intent(this, RecyclerActivity.class);
                startActivity(intent);
                return true;
            case R.id.Parametres_drawer:
                intent = new Intent(this, UserInfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.Panier_drawer:
                intent = new Intent(this, PanierActivity.class);
                startActivity(intent);
                return true;
            case R.id.Contact_drawer:
                intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.Deconnection_drawer:
                this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("token", "NULL").apply();
                this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("id", "NULL").apply();
                this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("CreateDate", "NULL").apply();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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


    }

}
