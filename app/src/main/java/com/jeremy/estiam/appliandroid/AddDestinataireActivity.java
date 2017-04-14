package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import android.support.design.widget.Snackbar;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Adresse;
import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.DestinatairesManager;
import com.jeremy.estiam.appliandroid.models.MasksManager;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.PhotoCreated;
import com.jeremy.estiam.appliandroid.models.PhotoModifiee;
import com.jeremy.estiam.appliandroid.models.PhotoModifieeManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddDestinataireActivity extends AppCompatActivity {

    private String masqueUrl ;
    private int idMasque;
    private float prixMasque;

    private RecyclerView mRecyclerView;
    private String PhotoOrigineUri;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Destinataires> array = new ArrayList<>();
    DestinatairesManager dm = new DestinatairesManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destinataire);

        PhotoOrigineUri = getIntent().getExtras().getString("UriPhotoString");
        masqueUrl = getIntent().getExtras().getString("masqueUrl");
        idMasque = getIntent().getExtras().getInt("idMasque");
        prixMasque = getIntent().getExtras().getFloat("prixMasque");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_destinataire);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        dm.open();

        Cursor c = dm.getDestinatairesWithoutPhoto(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));


        while(c.moveToNext()){
            Destinataires d = new Destinataires(0, 0, "","","","","","","","");

            d.setId(c.getInt(c.getColumnIndex( DestinatairesManager.KEY_ID_DESTINATAIRES)));
            d.setEmail(c.getString(c.getColumnIndex(DestinatairesManager.KEY_EMAIL)));
            d.setCivilite(c.getString(c.getColumnIndex(DestinatairesManager.KEY_CIVILITE)));
            d.setIdPhoto(c.getInt(c.getColumnIndex(DestinatairesManager.KEY_ID_PHOTO)));
            d.setCp(c.getString(c.getColumnIndex(DestinatairesManager.KEY_CP)));
            d.setMobile(c.getString(c.getColumnIndex(DestinatairesManager.KEY_MOBILE)));
            d.setNom(c.getString(c.getColumnIndex(DestinatairesManager.KEY_NOM)));
            d.setPrenom(c.getString(c.getColumnIndex(DestinatairesManager.KEY_PRENOM)));
            d.setRue(c.getString(c.getColumnIndex(DestinatairesManager.KEY_RUE)));
            d.setVille(c.getString(c.getColumnIndex(DestinatairesManager.KEY_VILLE)));
            d.setIdUser(c.getInt(c.getColumnIndex(DestinatairesManager.KEY_ID_USER)));
            d.setIdMessage(c.getInt(c.getColumnIndex(DestinatairesManager.KEY_ID_MESSAGE)));
            array.add(d);
        }
        c.close();
        dm.close();

        mRecyclerView.setAdapter(new DestinatairesAdapter(array));
    }

    public class DestinatairesAdapter extends RecyclerView.Adapter<DestinatairesAdapter.DestinatairesViewHolder> {

        private List<Destinataires> destinataires;

        public class DestinatairesViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            ImageButton imageEdit;
            ImageButton imageDelete;

            public DestinatairesViewHolder(View itemView) {
                super(itemView);

                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox3);
                imageEdit = (ImageButton) itemView.findViewById(R.id.imageButton2);
                imageDelete = (ImageButton) itemView.findViewById(R.id.imageButton3);

                imageEdit.setOnClickListener(new ImageButton.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(AddDestinataireActivity.this, MessageDestinataireActivity.class);
                        intent.putExtra("UriPhotoString", PhotoOrigineUri);
                        intent.putExtra("idDestinataire",  imageEdit.getTag(R.id.imageButton2).toString());;
                        intent.putExtra("masqueUrl", masqueUrl);
                        intent.putExtra("idMasque", idMasque);
                        intent.putExtra("prixMasque", prixMasque);
                        startActivity(intent);
                    }
                });

                imageDelete.setOnClickListener(new ImageButton.OnClickListener() {
                    public void onClick(View v) {

                        dm.open();
                        Object tag = imageDelete.getTag(R.id.imageButton3);
                        String str = tag.toString();
                        dm.supDestinataires(Integer.parseInt(str.split(",")[0].substring(1)));
                        dm.close();
                        array.remove(Integer.parseInt(str.split(", ")[1].split("]")[0]));
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

                checkBox.setOnClickListener(new CheckBox.OnClickListener() {
                    public void onClick(View v) {

                        Destinataires d = array.get(Integer.parseInt(checkBox.getTag(R.id.checkBox3).toString()));
                        if(checkBox.isChecked()){
                            d.setSelected(true);
                        }else{
                            d.setSelected(false);
                        }

                        array.set(Integer.parseInt(checkBox.getTag(R.id.checkBox3).toString()),d);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

            }

            public void setDestinataire(Destinataires d, int position) {
                checkBox.setText(d.getNom()+" "+d.getPrenom());
                checkBox.setTag(R.id.checkBox3, position);
                imageEdit.setTag(R.id.imageButton2, d.getId());
                List<Integer> liste = new ArrayList<>();
                liste.add(d.getId());
                liste.add(position);
                imageDelete.setTag(R.id.imageButton3, liste);
            }


        }

        public DestinatairesAdapter(List<Destinataires> destinataires) {
            this.destinataires = destinataires;
        }

        @Override
        public DestinatairesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_destinataireadd, parent, false);

            return new DestinatairesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DestinatairesViewHolder holder, int position) {
            Destinataires d = destinataires.get(position);
            ((DestinatairesViewHolder) holder).setDestinataire(destinataires.get(position), position);
        }

        @Override
        public int getItemCount() {
            return destinataires.size();
        }

    }

    @OnClick(R.id.buttonAddDestinataire)
    public void addDestinataire(View view) {
        Intent intent = new Intent(this, DestinataireActivity.class);
        intent.putExtra("UriPhotoString", PhotoOrigineUri);
        intent.putExtra("masqueUrl", masqueUrl);
        intent.putExtra("idMasque", idMasque);
        intent.putExtra("prixMasque", prixMasque);
        startActivity(intent);

    }

    @OnClick(R.id.ajoutPanier)
    public void addtopanier(View view) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);

        if(array.size()>0) {

            File file = new File(PhotoOrigineUri);
            RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);
            ApiService apiService = new ServiceGenerator().createService(ApiService.class);
            Call<PhotoCreated> call = apiService.setPhoto(sharedPreferences.getString("token", "NULL"), fbody, Integer.parseInt(sharedPreferences.getString("id", "0")));

            Iterator<Destinataires> iterator = array.iterator();
            DestinatairesManager dm = new DestinatairesManager(this);
            PhotoModifiee pm = new PhotoModifiee();
            pm.setUriFinale(masqueUrl);
            pm.setMaskId(idMasque);
            pm.setPrix(prixMasque);
            PanierManager panierManager = new PanierManager(this);
            panierManager.open();
            Panier panier = panierManager.getPanier(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));
            panier.setNbPhotos(panier.getNbPhotos() + 1);
            panier.setTotalPriceHT(panier.getTotalPriceHT() + pm.getPrix());

            BigDecimal bd = new BigDecimal(panier.getTotalPriceHT() * (float) 1.206);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

            panier.setPrixTTC(bd.floatValue());
            pm.setUriOrigine(PhotoOrigineUri);
            pm.setIdPanier(panier.getId());
            pm.setIdUser(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));
            PhotoModifieeManager pmm = new PhotoModifieeManager(this);
            pmm.open();
            long id = pmm.addPhotoModifiee(pm);

            dm.open();
            while (iterator.hasNext()) {
                Destinataires d = iterator.next();
                if (d.isSelected()) {
                    d.setIdPhoto((int) id);
                    dm.modDestinataires(d);
                }
            }

            panierManager.modPanier(panier);
            panierManager.close();
            dm.close();


            panierManager.close();
            pmm.close();
            Intent intent = new Intent(this, PanierActivity.class);
            startActivity(intent);
        }else{
            Snackbar.make(view ,"Veuillez ajouter des destinataires",Snackbar.LENGTH_LONG).show();
        }

    }




}


