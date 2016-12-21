package com.jeremy.estiam.appliandroid;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.models.User;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.adresse_layout)
    View adresse;
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

    private String nomValue = name.getText().toString();
    private String prenomValue  = firstname.getText().toString();
    private String pseudoValue  = pseudo.getText().toString();
    private String mailValue  = mail.getText().toString();
    private String dateNaissanceValue  = birthday.getText().toString();
    private String passwordNouveauValue  = newpassword.getText().toString();
    private String passwordConfirmValue  = confirmpassword.getText().toString();
    private String passwordoldValue  = oldpassword.getText().toString();
    private String cpValue  = cp.getText().toString();
    private String rueValue  = rue.getText().toString();
    private String villeValue  = ville.getText().toString();

    protected User user = new User();

    private static final String DATE_PATTERN ="([0-9]{2})/([0-9]{2})/([0-9]{4})";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        UserRecupTask userRecupTask = new UserRecupTask();
        userRecupTask.execute();
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
    public void onClickModification(View view) {
        boolean bon = true;

        if (!name.getText().toString().equals("") && !firstname.getText().toString().equals("")&& !pseudo.getText().toString().equals("")&& !mail.getText().toString().equals("")&& !birthday.getText().toString().equals("")&& !newpassword.getText().toString().equals("")&& !confirmpassword.getText().toString().equals("")&& !cp.getText().toString().equals("")&& !rue.getText().toString().equals("")&& !ville.getText().toString().equals("")) {
            dateBonFormat=validate(dateNaissanceValue);
            if(dateBonFormat==null){
                birthday.setError("Date sous ce format: jour/mois/année ");
                bon=false;
            }

            if((!passwordoldValue.equals(""))||(!passwordConfirmValue.equals(""))||(!passwordNouveauValue.equals(""))) {
                if(!passwordoldValue.equals(user.getPassword())){
                    oldpassword.setError("Mot de passe incorrect");
                    bon=false;
                }else{
                    if(passwordNouveauValue.equals(user.getPassword())){
                        newpassword.setError("Mot de passe inchangé");
                        bon=false;
                    }else{
                        if(!newpassword.getText().toString().equals(confirmpassword.getText().toString())){
                            confirmpassword.setError("Mots de passes non identiques ");
                            bon=false;
                        }
                    }
                }
            }


            if(!isEmailValid(mail.getText().toString())){
                mail.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
                bon=false;
            }
            if(bon){
                user.setPseudo(pseudo.getText().toString());
                user.setCp(cpValue);
                user.setRue(rueValue);
                user.setVille(villeValue);
                user.setBirthday(dateNaissanceValue);
                user.setEmail(mailValue);
                user.setNom(nomValue);
                user.setPrenom(prenomValue);

                if((!passwordoldValue.equals(""))||(!passwordConfirmValue.equals(""))||(!passwordNouveauValue.equals(""))) {
                    user.setPassword(passwordNouveauValue);
                }
                UserUpdateTask userUpdateTask = new UserUpdateTask();
                userUpdateTask.execute();
            }


        } else {
            if(nomValue.equals("")) name.setError("Veuillez saisir votre nom ");
            if(prenomValue.equals("")) firstname.setError("Veuillez saisir votre prenom ");
            if(pseudoValue.equals("")) pseudo.setError("Veuillez saisir votre pseudo ");
            if(mailValue.equals("")) mail.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
            if(dateNaissanceValue.equals("")) birthday.setError("Veuillez saisir votre date de naissance ");
            if(cpValue.equals("")) cp.setError("Veuillez saisir votre code postal ");
            if(rueValue.equals("")) rue.setError("Veuillez saisir votre rue ");
            if(villeValue.equals("")) ville.setError("Veuillez confirmer votre mot de passesaisir votre ville ");
        }
    }

    public class UserRecupTask extends AsyncTask<Void, Void , Void> {
        protected void doInBackground() {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.updateUser(user.getId());

            try {
                Response<User> userResponse = call.execute();
                user = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            name.setText()= user.getName();
            birthday.setText()= user.getBirthday();
            firstname.setText()= user.getFirstname();
            mail.setText()= user.getMail();
            pseudo.setText()= user.getPseudo();
            cp.setText()= user.getCp();
            rue.setText()= user.getRue();
            ville.setText()= user.getVille();
        }

    }

    public class UserUpdateTask extends AsyncTask<Void, Void , Void> {
        protected void doInBackground() {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<User> call = apiService.updateUser(user);

            try {
                Response<User> userResponse = call.execute();
                user = userResponse.body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            name.setText()= user.getName();
            birthday.setText()= user.getBirthday();
            firstname.setText()= user.getFirstname();
            mail.setText()= user.getMail();
            pseudo.setText()= user.getPseudo();
            cp.setText()= user.getCp();
            rue.setText()= user.getRue();
            ville.setText()= user.getVille();
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


}
