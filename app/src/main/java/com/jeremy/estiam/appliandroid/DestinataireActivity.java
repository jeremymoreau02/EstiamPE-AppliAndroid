package com.jeremy.estiam.appliandroid;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.DestinatairesManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DestinataireActivity extends AppCompatActivity {

    private String masqueUrl ;
    private int idMasque;

    static final int PICK_CONTACT_REQUEST = 10;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 11;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton_M)
    RadioButton radioButton_M;
    @BindView(R.id.radioButton_Mme)
    RadioButton radioButton_Mme;
    @BindView(R.id.radioButton_Mlle)
    RadioButton radioButton_Mlle;
    @BindView(R.id.radioButton_MMme)
    RadioButton radioButton_MMme;
    @BindView(R.id.NomValue)
    EditText nom;
    @BindView(R.id.PrenomValue)
    EditText prenom;
    @BindView(R.id.mobileValue)
    EditText mobile;
    @BindView(R.id.emailValue)
    EditText email;
    @BindView(R.id.rueValue)
    EditText rue;
    @BindView(R.id.CPValue)
    EditText CP;
    @BindView(R.id.VilleValue)
    EditText ville;


    private Uri uriContact;
    private String contactID;

    Destinataires dest = new Destinataires(0, 0, "","","","","","","","");
    int idPhoto = 0;
    int idDest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinataire);

        ButterKnife.bind(this);

        String id = this.getIntent().getExtras().getString("idDestinataire","NULL");

        if(!this.getIntent().getExtras().getString("idPhoto","NULL").equals("NULL")){
            idPhoto=Integer.parseInt(this.getIntent().getExtras().getString("idPhoto","NULL"));
        }

        if(id != "NULL"){
            int idInt = Integer.parseInt(id);
            DestinatairesManager dm = new DestinatairesManager(this);
            dm.open();
            dest = dm.getDestinataire(idInt);
            dm.close();

            ville.setText(dest.getVille());
            CP.setText(dest.getCp());
            rue.setText(dest.getRue());
            nom.setText(dest.getNom());
            prenom.setText(dest.getPrenom());
            mobile.setText(dest.getMobile());
            email.setText(dest.getEmail());

            if(dest.getCivilite()=="Monsieur"){
                radioButton_M.setChecked(true);
                radioButton_Mme .setChecked(false);
                radioButton_Mlle.setChecked(false);
                radioButton_MMme.setChecked(false);
            }else if(dest.getCivilite()=="Madame"){
                radioButton_M.setChecked(false);
                radioButton_Mme .setChecked(true);
                radioButton_Mlle.setChecked(false);
                radioButton_MMme.setChecked(false);
            }else if(dest.getCivilite()=="Mademoiselle"){
                radioButton_M.setChecked(false);
                radioButton_Mme .setChecked(false);
                radioButton_Mlle.setChecked(true);
                radioButton_MMme.setChecked(false);
            }else if(dest.getCivilite()=="Monsieur et Madame"){
                radioButton_M.setChecked(false);
                radioButton_Mme .setChecked(false);
                radioButton_Mlle.setChecked(false);
                radioButton_MMme.setChecked(true);
            }
        }
    }


    @OnClick(R.id.contactImage)
    void onClickContact(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

        }

    }

    @OnClick(R.id.validerContact)
    void onClickValider(){
        Destinataires destinataire = new Destinataires(0, 0,"","","","","","","","");
        boolean bon = true;
        if (!nom.getText().toString().equals("") && !prenom.getText().toString().equals("")&& !email.getText().toString().equals("")&& !mobile.getText().toString().equals("")&&  !CP.getText().toString().equals("")&& !rue.getText().toString().equals("")&& !ville.getText().toString().equals("")) {

            if(!isEmailValid(email.getText().toString())){
                email.setError("Veuillez saisir votre mail sous la forme: example@example.fr ");
                bon=false;
            }
            if(bon){
                destinataire.setIdUser(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));
                destinataire.setVille(ville.getText().toString());
                destinataire.setRue(rue.getText().toString());
                destinataire.setPrenom(prenom.getText().toString());
                destinataire.setNom(nom.getText().toString());
                destinataire.setMobile(mobile.getText().toString());

                SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
                destinataire.setIdUser(Integer.parseInt(sharedPreferences.getString("id", "NULL")));
                destinataire.setEmail(email.getText().toString());
                destinataire.setCp(CP.getText().toString());
                int radioId = radioGroup.getCheckedRadioButtonId();
                switch (radioId){
                    case R.id.radioButton_M:
                        destinataire.setCivilite("Monsieur");
                        break;

                    case R.id.radioButton_Mme:
                        destinataire.setCivilite("Madame");
                        break;

                    case R.id.radioButton_Mlle:
                        destinataire.setCivilite("Mademoiselle");
                        break;

                    case R.id.radioButton_MMme:
                        destinataire.setCivilite("Monsieur et Madame");
                        break;

                    default:
                        break;
                }

                DestinatairesManager dm = new DestinatairesManager(this);
                dm.open();

                Cursor c = dm.getDestinataires(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));

                if(dest.getId() != 0){
                    destinataire.setId(dest.getId());
                    dm.modDestinataires(destinataire);
                }else{
                            dm.addDestinataires(destinataire);


                }


                dm.close();

                Intent intent = new Intent(this, AddDestinataireActivity.class);
                intent.putExtra("UriPhotoString",getIntent().getExtras().getString("UriPhotoString"));
                intent.putExtra("masqueUrl", getIntent().getExtras().getString("masqueUrl"));
                intent.putExtra("idMasque", getIntent().getExtras().getString("idMasque"));
                intent.putExtra("prixMasque", getIntent().getExtras().getString("prixMasque"));
                startActivity(intent);
            }


        } else {
            if(nom.getText().toString().equals("")) nom.setError("Veuillez saisir votre nom ");
            if(prenom.getText().toString().equals("")) prenom.setError("Veuillez saisir votre prenom ");
            if(email.getText().toString().equals("")) email.setError("Veuillez saisir votre email ");
            if(CP.getText().toString().equals("")) CP.setError("Veuillez saisir votre code postal ");
            if(rue.getText().toString().equals("")) rue.setError("Veuillez saisir votre rue");
            if(ville.getText().toString().equals("")) ville.setError("Veuillez saisir votre ville ");
            if(mobile.getText().toString().equals("")) mobile.setError("Veuillez confirmer votre mobile");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            uriContact = data.getData();


                retrieveContact();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT_REQUEST);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void retrieveContact() {
        nom.setText("");
        prenom.setText("");
        email.setText("");
        mobile.setText("");

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(uriContact,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                System.out.println(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                //if(id.equals(uriContact.getPath().split("data/")[1])) {
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (name.contains(" ")) {
                        nom.setText(name.split(" ")[0]);
                        prenom.setText(name.split(" ")[1]);
                    } else {
                        nom.setText(name);
                    }
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));



                // get the phone number
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.Contacts._ID +" = ?", new String[]{id}, null);

                            while(pCur.moveToNext()){
                                String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                mobile.setText(phone);
                            }
                            pCur.close();
                        }



                        // get email and type

                        Cursor emailCur = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.Contacts._ID + " = ?",
                                new String[]{id}, null);
                        while (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            String mail = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            String mailType = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                            email.setText(mail);
                        }
                        emailCur.close();



            }
        }
    }



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
}
