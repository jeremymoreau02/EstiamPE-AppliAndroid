package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Adresse;
import com.jeremy.estiam.appliandroid.models.ResponsePerso;
import com.jeremy.estiam.appliandroid.models.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Response;


public class UserInfoActivity extends AppCompatActivity  {
    @BindView(R.id.adresse_layout) View adresse;
    @BindView(R.id.donneespersos_layout) View donnees;
    @BindView(R.id.donneespersos_password_layout) View passwordlayout;
    @BindView(R.id.donneespersos_birthday_value)    EditText birthday;
    @BindView(R.id.donneespersos_confirmpassword_value)    EditText confirmpassword;
    @BindView(R.id.donneespersos_firstname_value)    EditText firstname;
    @BindView(R.id.donneespersos_mail_value)    EditText mail;
    @BindView(R.id.donneespersos_name_value)    EditText name;
    @BindView(R.id.donneespersos_newpassword_value)    EditText newpassword;
    @BindView(R.id.donneespersos_oldpassword_value)    EditText oldpassword;
    @BindView(R.id.donneespersos_pseudo_value)    EditText pseudo;
    @BindView(R.id.adresse_cp_valeur)    EditText cp;
    @BindView(R.id.adresse_rue_valeur)    EditText rue;
    @BindView(R.id.adresse_ville_valeur)    EditText ville;

    String dateBonFormat;
    boolean dateDejaChangee;
    private Pattern pattern;
    private Matcher matcher;
    private String token;

    private boolean adressHasExisted = false;


    protected User user = new User();
    protected Adresse adresseUser = new Adresse();

    private static final String DATE_PATTERN ="([0-9]{2})/([0-9]{2})/([0-9]{4})";
    private static final String DATE_PATTERN_INVERSE ="([0-9]{4})-([0-9]{2})-([0-9]{2})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user_info);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/


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

        UserRecupTask userRecupTask = new UserRecupTask();
        userRecupTask.execute();

        AdressesRecupTask adressesRecupTask = new AdressesRecupTask();
        adressesRecupTask.execute(findViewById(R.id.activity_user_info));

    }



    @OnTextChanged(R.id.donneespersos_birthday_value)
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

    @OnClick(R.id.button_okuserinfo)
    public void onClickModification(View view) {
        boolean bon = true;
         String nomValue = name.getText().toString();
         String prenomValue  = firstname.getText().toString();
         String pseudoValue  = pseudo.getText().toString();
         String mailValue  = mail.getText().toString();
         String dateNaissanceValue  = birthday.getText().toString();
         String passwordNouveauValue  = newpassword.getText().toString();
         String passwordConfirmValue  = confirmpassword.getText().toString();
         String passwordoldValue  = oldpassword.getText().toString();
         String cpValue  = cp.getText().toString();
         String rueValue  = rue.getText().toString();
         String villeValue  = ville.getText().toString();

            if(!dateNaissanceValue.equals("")){
                dateBonFormat=validate(dateNaissanceValue);
                if(dateBonFormat==null){
                    birthday.setError("Date sous le mauvais format");
                }else{
                    if(dateNaissanceValue.equals(""))user.setDateNaissance(dateNaissanceValue);
                }
            }

            if((!passwordoldValue.equals(""))||(!passwordConfirmValue.equals(""))||(!passwordNouveauValue.equals(""))) {
                if(passwordoldValue.equals("")){
                    oldpassword.setError("Champ vide");
                    bon=false;
                }else{
                    if(passwordNouveauValue.equals("")){
                        newpassword.setError("Champ vide");
                        bon=false;
                    }else{
                        if(confirmpassword.getText().toString().equals("")){
                            confirmpassword.setError("Champ vide ");
                            bon=false;
                        }
                    }
                }
                if(passwordoldValue.equals(passwordNouveauValue)) {
                    newpassword.setError("mot de passe inchangé");
                    bon=false;
                }
                if(!confirmpassword.getText().toString().equals(passwordNouveauValue)) {
                    newpassword.setError("mots de passes differents");
                    newpassword.setError("mots de passes differents");
                    bon=false;
                }
            }

        if(bon) {

            if (!mailValue.equals("")) {
                if (!isEmailValid(mail.getText().toString())) {
                    mail.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
                } else {
                    user.setEmail(mailValue);
                }
            }

            if (pseudo.getText().toString().equals("")) user.setPseudo(pseudo.getText().toString());
            if (nomValue.equals("")) user.setNom(nomValue);
            if (prenomValue.equals("")) user.setPrenom(prenomValue);

            if(!((passwordNouveauValue.equals("")||passwordoldValue.equals("")))){
                user.setPassword(passwordNouveauValue);
            }
            user.setOldPassword(passwordoldValue);


            adresseUser.setUserId(user.getUserId());
            if (!cpValue.equals("")) adresseUser.setZC(cpValue);
            if (!rueValue.equals("")) adresseUser.setStreet(rueValue);
            if (!villeValue.equals("")) adresseUser.setCity(villeValue);
            adresseUser.setType("Shipping");

            UserUpdateTask userUpdateTask = new UserUpdateTask();
            userUpdateTask.execute(view);

            if(!(cpValue.equals("")&&rueValue.equals("")&&villeValue.equals(""))){
                AdresseUpdateTask adresseUpdateTask = new AdresseUpdateTask();
                adresseUpdateTask.execute(view);
            }


        }

    }

    public class AdressesRecupTask extends AsyncTask<View, Void , Adresse> {
        @Override
        protected Adresse doInBackground(View... view) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<List<Adresse>> call = apiService.getAdresses(user.getUserId(), token);
            boolean good = false;

            try {
                Response<List<Adresse>> adresseResponse = call.execute();
                List<Adresse> adresses = adresseResponse.body();
                Iterator<Adresse> iterator = adresses.iterator();
                while(iterator.hasNext() && !good){
                    Adresse a = iterator.next();
                    if(a.getType().equals("Shipping")){
                        adresseUser = a;
                        good = true;
                    }
                }

            } catch (Exception e) {
                if(e.getMessage().equals("connect timed out")){
                    Snackbar.make(view[0] ,"Connectez-vous à Internet",Snackbar.LENGTH_LONG).show();
                }else{
                    e.printStackTrace();
                }

                return  null;
            }

            return adresseUser;
        }

        @Override
        protected void onPostExecute(Adresse adresse){
            super.onPostExecute(adresse);
            if((adresse != null)&&(adresse.getId() != 0)){
                adressHasExisted = true;
                if(adresse.getZC()!=null)cp.setText(adresse.getZC());
                if(adresse.getStreet()!=null)rue.setText( adresse.getStreet());
                if(adresse.getCity()!=null)ville.setText( adresse.getCity());
            }
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
                if(user.getNom()!=null)name.setText(user.getNom());
                if(user.getDateNaissance()!=null){
                    user.setDateNaissance(user.getDateNaissance().substring(0,10));
                    String date=user.getDateNaissance();
                    //matcher = Pattern.compile(DATE_PATTERN_INVERSE).matcher(user.getBirthday());
                    String day = date.substring(8,10);
                    String month = date.substring(5,7);
                    String year =date.substring(0,4);
                    String newDate=day+'/'+month+'/'+year;
                    birthday.setText(newDate);
                }
                if(user.getPrenom()!=null)firstname.setText( user.getPrenom());
                if(user.getEmail()!=null)mail.setText( user.getEmail());
                if(user.getPseudo()!=null)pseudo.setText( user.getPseudo());
            }
        }

    }

    public class UserUpdateTask extends AsyncTask<View, Void , Void> {
        protected Void doInBackground(View... views) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<String> call = apiService.updateUser(user.getUserId(), token, user);

            try {
                Response<String> userResponse = call.execute();
                if(userResponse.body().equals("User updated")){
                    Snackbar.make(views[0],"Utilisateur mises à jour", Snackbar.LENGTH_LONG).show();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(views[0],"Erreur", Snackbar.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }



    }

    public class AdresseUpdateTask extends AsyncTask<View, Void , Void> {
        protected Void doInBackground(View... views) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);
            Call<ResponsePerso> call;

            if(adressHasExisted){
                call = apiService.updateAdresse(adresseUser.getId(), token, adresseUser);
            }else{
                call = apiService.createAdresse( token, adresseUser);
            }


            try {
                Response<ResponsePerso> adressResponse = call.execute();
                ResponsePerso rep = adressResponse.body();
                if(rep.getSuccess()){
                    Snackbar.make(views[0],"Adresse mises à jour", Snackbar.LENGTH_LONG).show();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(UserInfoActivity.this, RecyclerActivity.class);
                    startActivity(intent);
                }else{
                    Snackbar.make(views[0],"Erreur", Snackbar.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer!=null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
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

    @OnClick(R.id.donneespersos_texte)
    public void showHideDonnees(){
        if(donnees.getVisibility()==View.VISIBLE){
            donnees.setVisibility(View.GONE);
        }else{
            donnees.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.adresse_texte)
    public void showHideAdresse(){
        if(adresse.getVisibility()==View.VISIBLE){
            adresse.setVisibility(View.GONE);
        }else{
            adresse.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.donneespersos_password_text)
    public void showHidePassword(){
        if(passwordlayout.getVisibility()==View.VISIBLE){
            passwordlayout.setVisibility(View.GONE);
        }else{
            passwordlayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.button_contact)
    public void onClickContact(){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_deconnexion)
    public void onClickDeconnexion(){
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("token","NULL").apply();
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("id","NULL").apply();
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("CreateDate","NULL").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_home)
    public void onClickHome(){
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }






}