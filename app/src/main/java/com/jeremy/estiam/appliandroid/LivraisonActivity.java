package com.jeremy.estiam.appliandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LivraisonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livraison);
    }


    public void choisirLivraison(View view) {
        Intent intent = new Intent(this, DestinataireActivity.class);
        startActivity(intent);

    }
}
