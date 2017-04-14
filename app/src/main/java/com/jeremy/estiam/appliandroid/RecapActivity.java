package com.jeremy.estiam.appliandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.DelivererCreated;
import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.DestinatairesManager;
import com.jeremy.estiam.appliandroid.models.MessageDestinataire;
import com.jeremy.estiam.appliandroid.models.MessageDestinatairesManager;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.PhotoCreated;
import com.jeremy.estiam.appliandroid.models.PhotoModifiee;
import com.jeremy.estiam.appliandroid.models.PhotoModifieeManager;
import com.jeremy.estiam.appliandroid.models.ResponsePerso;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class RecapActivity extends AppCompatActivity {

    private int userId ;
    private String PrixTotalStr ="0";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "Ad4F8uGowH6taKwDCfObC8knVOgEpldfK8ktWueWnFovUG5LwQJo9jBx9YBC7WQEuoWG6uZ_NpX2O-KH";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("PHOTO EXPRESSO")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        userId = Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL"));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        TextView delaiL = (TextView) findViewById(R.id.recapLivraisonDelai);
        TextView nameL = (TextView) findViewById(R.id.recapLivraisonName);
        TextView prixL = (TextView) findViewById(R.id.recapLivraisonPrix);
        TextView nbP = (TextView) findViewById(R.id.recapNbPhotos);
        TextView prixP = (TextView) findViewById(R.id.recapPhotosPrix);
        TextView prixTotal = (TextView) findViewById(R.id.recapPrixTotal);

        delaiL.setText(getIntent().getExtras().get("delaiLivraison").toString()+"jours");
        nameL.setText(getIntent().getExtras().get("nameLivraison").toString());
        prixL.setText(getIntent().getExtras().get("prixLivraison").toString()+"€");

        PanierManager pm = new PanierManager(this);
        pm.open();
        Panier p = pm.getPanier(userId);
        nbP.setText(String.valueOf(p.getNbPhotos()));
        prixP.setText(String.valueOf(p.getPrixTTC())+"€");

        float pt = Float.parseFloat(getIntent().getExtras().get("prixLivraison").toString()) + p.getPrixTTC();
        p.setPrixTotal(pt);
        pm.modPanier(p);
        pm.close();

        PrixTotalStr = String.valueOf(pt);
        prixTotal.setText(PrixTotalStr+"€");

        findViewById(R.id.paypalBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(PrixTotalStr), "EUR", "sample item",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(RecapActivity.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });
    }

    private PayPalOAuthScopes getOauthScopes() {
        Set scopes = new HashSet(Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS));
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    System.out.println(confirm.toJSONObject().toString(4));
                    System.out.println(confirm.getPayment().toJSONObject().toString(4));
                    //TODO: envoyer 'confirm' et si possible confirm.getPayment() à votre server pour la vérification
                    confirm.getPayment();
                    Toast.makeText(getApplicationContext(), "Paiement bien effectué", Toast.LENGTH_LONG).show();
                    PanierManager estebann = new PanierManager(this);
                    estebann.open();
                    Panier panier = estebann.getPanier(userId);
                    estebann.supPanier(userId);
                    estebann.close();
                    DestinatairesManager pogba = new DestinatairesManager(this);
                    pogba.open();
                    Cursor destinatairesCursor = pogba.getDestinataires(userId);
                    List<Destinataires> destinataires = new ArrayList<Destinataires>();
                    while(destinatairesCursor.moveToNext()){
                        Destinataires d = new Destinataires(0, 0, "", "" ,"" ,"" ,"","","","");
                        d.setId(destinatairesCursor.getInt(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_ID_DESTINATAIRES)));
                        d.setIdUser(destinatairesCursor.getInt(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_ID_USER)));
                        d.setCivilite(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_CIVILITE)));
                        d.setCp(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_CP)));
                        d.setEmail(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_EMAIL)));
                        d.setIdMessage(destinatairesCursor.getInt(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_ID_MESSAGE)));
                        d.setIdPhoto(destinatairesCursor.getInt(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_ID_PHOTO)));
                        d.setMobile(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_MOBILE)));
                        d.setRue(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_RUE)));
                        d.setVille(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_VILLE)));
                        d.setNom(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_NOM)));
                        d.setPrenom(destinatairesCursor.getString(destinatairesCursor.getColumnIndex(DestinatairesManager.KEY_PRENOM)));

                        ApiService apiService = new ServiceGenerator().createService(ApiService.class);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        Call<DelivererCreated> call = apiService.addDeliverer(this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).getString("token", "NULL"), d);
                        try {
                            DelivererCreated delivererCreated = call.execute().body();
                            if(delivererCreated.isSuccess())
                                d.setId(delivererCreated.getDelivererId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        destinataires.add(d);
                    }

                    destinatairesCursor.close();
                    pogba.supAllDestinataires();
                    pogba.close();
                    MessageDestinatairesManager littleB = new MessageDestinatairesManager(this);
                    littleB.open();
                    Cursor messagesCursor = littleB.getMessages();
                    List<MessageDestinataire> messageDestinataires = new ArrayList<MessageDestinataire>();
                    while(messagesCursor.moveToNext()){
                        MessageDestinataire d = new MessageDestinataire();
                        d.setID(messagesCursor.getInt(messagesCursor.getColumnIndex(MessageDestinatairesManager.KEY_ID_MESSAGE)));
                        d.setMessage(messagesCursor.getString(messagesCursor.getColumnIndex(MessageDestinatairesManager.KEY_MESSAGE)));
                        messageDestinataires.add(d);
                    }

                    messagesCursor.close();
                    littleB.supAllDestinataires();
                    littleB.close();
                    PhotoModifieeManager pmm = new PhotoModifieeManager(this);
                    pmm.open();
                    Cursor photos = pmm.getPhotosModifiees(userId);
                    List<PhotoModifiee> photoModifiees = new ArrayList<PhotoModifiee>();
                    while(photos.moveToNext()){
                        PhotoModifiee d = new PhotoModifiee();
                        d.setName("");
                        d.setDescription("");
                        d.setIdFormat(0);
                        d.setPhotoId(photos.getInt(photos.getColumnIndex(PhotoModifieeManager.KEY_ID)));
                        d.setIdPanier(0);
                        d.setIdUser(0);
                        d.setMaskId(photos.getInt(photos.getColumnIndex(PhotoModifieeManager.KEY_MASQUE)));
                        d.setNbPhotos(photos.getInt(photos.getColumnIndex(PhotoModifieeManager.KEY_NB)));
                        d.setPrix(0);
                        d.setUriOrigine("");
                        d.setUriFinale("");
                        photoModifiees.add(d);
                    }

                    photos.close();
                    pmm.supAllPhotoModifiee();
                    pmm.close();

                    for(int i = 0; i < photoModifiees.size();i++){
                        List<Destinataires> dest = new ArrayList<Destinataires>();
                        for(int j =0; j<destinataires.size();j++){
                            if(destinataires.get(j).getIdPhoto() == photoModifiees.get(i).getPhotoId()){
                                if(destinataires.get(j).getIdMessage() > 0){
                                    for(int k =0; k<messageDestinataires.size();k++){
                                        if(messageDestinataires.get(k).getID() == destinataires.get(j).getIdMessage()){
                                            destinataires.get(j).setMessage(messageDestinataires.get(k).getMessage());
                                        }
                                    }
                                }else{
                                    destinataires.get(j).setMessage("");
                                }

                                dest.add(destinataires.get(j));
                            }
                        }
                        photoModifiees.get(i).setDeliverers(dest);
                        File file = new File(Uri.parse(photoModifiees.get(i).getUriOrigine()).getPath());
                        RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
                        ApiService apiService = new ServiceGenerator().createService(ApiService.class);
                        Call<PhotoCreated> call = apiService.setPhoto(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token","NULL"), fbody, Integer.parseInt( this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "0")));
                        try {
                            PhotoCreated response = call.execute().body();
                            if(response.isSuccess()){
                                Intent intent = new Intent(this, RecyclerActivity.class);
                                startActivity(intent);
                            }else{
                                System.out.println(response.getMessage());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    panier.setItems(photoModifiees);
                    panier.setStatus("Pending");

                    ApiService apiService = new ServiceGenerator().createService(ApiService.class);
                    Call<ResponsePerso> call = apiService.createOrder(this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).getString("token", "NULL"), panier);
                    try {
                        ResponsePerso response = call.execute().body();
                        if(response.getSuccess()){
                            Intent intent = new Intent(this, RecyclerActivity.class);
                            startActivity(intent);
                        }else{
                            System.out.println(response.getError());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    System.out.println("an extremely unlikely failure occurred: "+ e);
                    Toast.makeText(getApplicationContext(), "Impossible d'effectuer le paiement", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
