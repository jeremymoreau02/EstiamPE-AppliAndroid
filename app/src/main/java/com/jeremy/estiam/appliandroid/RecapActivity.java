package com.jeremy.estiam.appliandroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RecapActivity extends AppCompatActivity {

    private String PrixTotalStr ="0";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AecMp8-QpjpKm-XVcll6fczz5-EA6et9gcs0aWzzgR6Hna9npc-nJO22MqvIeyw-2hYEDJIp-koPc5jt";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("PHOTO EXPRESSO")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

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
        Panier p = pm.getPanier();
        nbP.setText(String.valueOf(p.getNbPhotos()));
        prixP.setText(String.valueOf(p.getPrixTTC()));

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
                } catch (JSONException e) {
                    System.out.println("an extremely unlikely failure occurred: "+ e);
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
