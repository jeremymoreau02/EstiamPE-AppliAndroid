package com.jeremy.estiam.appliandroid;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.DestinatairesManager;
import com.jeremy.estiam.appliandroid.models.MessageDestinataire;
import com.jeremy.estiam.appliandroid.models.MessageDestinatairesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageDestinataireActivity extends AppCompatActivity {

    @BindView(R.id.editTextMessageDestinataire)
    EditText message;

    String idDestinataire = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_destinataire);

        ButterKnife.bind(this);

        idDestinataire = this.getIntent().getExtras().getString("idDestinataire","NULL");

        if(idDestinataire != "NULL"){
            MessageDestinatairesManager mdm = new MessageDestinatairesManager(this);
            DestinatairesManager dm = new DestinatairesManager(this);
            mdm.open();
            dm.open();
            message.setText(mdm.getMessage(dm.getDestinataire(Integer.parseInt(idDestinataire)).getIdMessage()));
            mdm.close();
            dm.close();
        }
    }

    @OnClick(R.id.boutonValiderMessage)
    void onClickValiderMessage(View v){
        MessageDestinataire md = new MessageDestinataire();
        md.setMessage(message.getText().toString());

        MessageDestinatairesManager mdm = new MessageDestinatairesManager(this);
        mdm.open();
        int id = (int) mdm.addMessage(md);
        mdm.close();
        DestinatairesManager dm = new DestinatairesManager(this);
        dm.open();
        Destinataires dest = dm.getDestinataire(Integer.parseInt(idDestinataire));
        dest.setIdMessage(id);
        dm.modDestinataires(dest);
        dm.close();
        Intent intent = new Intent(this, AddDestinataireActivity.class);
        intent.putExtra("UriPhotoString",getIntent().getExtras().getString("UriPhotoString"));
        startActivity(intent);
    }
}
