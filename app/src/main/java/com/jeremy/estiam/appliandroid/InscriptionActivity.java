package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class InscriptionActivity extends AppCompatActivity  {

    @BindView(R.id.nom_edit)
    EditText nom;
    @BindView(R.id.prenom_edit)
    EditText prenom;
    @BindView(R.id.pseudo__edit)
    EditText pseudo;
    @BindView(R.id.mail_edit)
    EditText mail;
    @BindView(R.id.dateNaissance_edit)
    EditText dateNaissance;
    @BindView(R.id.passwordNouveau_edit)
    EditText passwordNouveau;
    @BindView(R.id.passwordConfirm_edit)
    EditText passwordConfirm;

    String dateBonFormat;
    boolean dateDejaChangee;
    private Pattern pattern;
    private Matcher matcher;

    private String nomValue;
    private String prenomValue;
    private String pseudoValue;
    private String mailValue;
    private String dateNaissanceValue;
    private String passwordNouveauValue;
    private String passwordConfirmValue;

    private User user = new User();

    private static final String DATE_PATTERN ="([0-9]{2})/([0-9]{2})/([0-9]{4})";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);


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

    @OnTextChanged(R.id.dateNaissance_edit)
    protected void handleTextChange(Editable editable) {
        if(!dateDejaChangee){
            if((editable.toString().length()==2)||(editable.toString().length()==5)){
                editable.append('/');
            }
            if((editable.toString().length()==10)){
                dateDejaChangee=true;
            }
        }else{
            if((editable.toString().length()==3)||(editable.toString().length()==6)){
                editable.delete(editable.toString().length(),editable.toString().length());
            }
            if((editable.toString().length()==0)){
                dateDejaChangee=false;
            }
        }
    }

    @OnClick(R.id.inscription_button)
    public void onClickInscription(View view) {
        boolean bon = true;

        if (!nom.getText().toString().equals("") && !prenom.getText().toString().equals("")&& !pseudo.getText().toString().equals("")&& !mail.getText().toString().equals("")&& !dateNaissance.getText().toString().equals("")&& !passwordNouveau.getText().toString().equals("")&& !passwordConfirm.getText().toString().equals("")) {
            dateBonFormat=validate(dateNaissance.getText().toString());
            if(dateBonFormat==null){
                dateNaissance.setError("Date sous ce format: jour/mois/année ");
                bon=false;
            }

            if(!passwordNouveau.getText().toString().equals(passwordConfirm.getText().toString())){
                passwordConfirm.setError("Mots de passes non identiques ");
                bon=false;
            }
            if(!isEmailValid(mail.getText().toString())){
                mail.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
                bon=false;
            }
            if(bon){
                user.setPseudo(pseudo.getText().toString());
                user.setPassword(passwordNouveau.getText().toString());
                user.setBirthday(dateBonFormat);
                user.setEmail(mail.getText().toString());
                user.setNom(nom.getText().toString());
                user.setPrenom(prenom.getText().toString());


                UserInscriptionTask userInscriptionTask = new UserInscriptionTask();
                userInscriptionTask.execute( view);
            }


        } else {
            if(nom.getText().toString().equals("")) nom.setError("Veuillez saisir votre nom ");
            if(prenom.getText().toString().equals("")) prenom.setError("Veuillez saisir votre prenom ");
            if(pseudo.getText().toString().equals("")) pseudo.setError("Veuillez saisir votre pseudo ");
            if(mail.getText().toString().equals("")) mail.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
            if(dateNaissance.getText().toString().equals("")) dateNaissance.setError("Veuillez saisir votre date de naissance ");
            if(passwordNouveau.getText().toString().equals("")) passwordNouveau.setError("Veuillez saisir votre mot de passe ");
            if(passwordConfirm.getText().toString().equals("")) passwordConfirm.setError("Veuillez confirmer votre mot de passe ");
        }
    }

    public class UserInscriptionTask extends AsyncTask<View, Void , User> {
        protected User doInBackground(View... view ) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.inscription(user);

            User userRes= new User();

            try {
                Response<User> userResponse = call.execute();
                user = userResponse.body();
                if(user.getMessage()!=null){
                    Snackbar.make(view[0] ,userRes.getMessage(),Snackbar.LENGTH_LONG).show();
                }else{
                    if(user.getToken()==null){
                        Snackbar.make(view[0] ,"inscription échouée",Snackbar.LENGTH_LONG).show();
                    }else{
                        SharedPreferences sharedPreferences = InscriptionActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("token", user.getToken()).apply();

                        sharedPreferences.edit().putString("id", Integer.toString(user.getId())).apply();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
                        Calendar date = new GregorianCalendar();
                        sharedPreferences.edit().putString("CreateDate", dateFormat.format(date.getTime())).apply();



                        Intent intent = new Intent(InscriptionActivity.this, RecyclerActivity.class);
                        startActivity(intent);
                    }
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

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @OnClick(R.id.SeConnecter_button)
    public void onClickSeConnecter(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

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

    /**
     * Validate date format with regular expression
     * @param date date address for validation
     * @return true valid date format, false invalid date format
     */
    public String validate(final String date){

        matcher = Pattern.compile(DATE_PATTERN).matcher(date);
        String newDate;
        String day ;
        String month;
        int year ;

        if(matcher.matches()){
            matcher.reset();

            if(matcher.find()){
                day = matcher.group(1);
                month = matcher.group(2);
                year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return null; // only 1,3,5,7,8,10,12 has 31 days
                }

                else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return null;
                        }
                    }
                    else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return null;
                        }
                    }
                }
            }

            else{
                return null;
            }
        }
        else{
            return null;
        }
        Calendar c = Calendar.getInstance();
        int year2 = c.get(Calendar.YEAR);
        if((Integer.parseInt(day)<31)&&(Integer.parseInt(month)<12)&&(year<year2)&&(Integer.parseInt(day)>0)&&(Integer.parseInt(month)>0)&&(year>0)){
            newDate=matcher.group(3)+'-'+month+'-'+day;
            return newDate;
        }else{
            return null;
        }
    }
}

