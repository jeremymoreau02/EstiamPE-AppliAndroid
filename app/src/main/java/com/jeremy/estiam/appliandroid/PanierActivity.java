package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.PhotoModifiee;
import com.jeremy.estiam.appliandroid.models.PhotoModifieeManager;

import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_DESCRIPTION;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_FORMAT;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_ID;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_MASQUE;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_NAME;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_NB;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_PRIX;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_URI_FINALE;
import static com.jeremy.estiam.appliandroid.models.PhotoModifieeManager.KEY_URI_ORIGINE;

public class PanierActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private int userId ;
    List<PhotoModifiee> array = new ArrayList<>();
    PhotoModifieeManager dm = new PhotoModifieeManager(this);

    @BindView(R.id.valueNbPhotos)
    TextView valueNbPhotos;
    @BindView(R.id.valueHT)
    TextView valueHT;
    @BindView(R.id.valueTTC)
    TextView valueTTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        ButterKnife.bind(this);

        userId= Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_panier);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        dm.open();

        Cursor c = dm.getPhotosModifiees(userId);

        while(c.moveToNext()){
            PhotoModifiee s = new PhotoModifiee();
            s.setPhotoId(c.getInt(c.getColumnIndex(KEY_ID)));
            s.setIdUser(userId);
            s.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            s.setIdFormat(c.getInt(c.getColumnIndex(KEY_FORMAT)));
            s.setNbPhotos(c.getInt(c.getColumnIndex(KEY_NB)));
            s.setPrix(c.getFloat(c.getColumnIndex(KEY_PRIX)));
            s.setMaskId(c.getInt(c.getColumnIndex(KEY_MASQUE)));
            s.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            s.setUriFinale(c.getString(c.getColumnIndex(KEY_URI_FINALE)));
            s.setUriOrigine(c.getString(c.getColumnIndex(KEY_URI_ORIGINE)));
            array.add(s);
        }
        c.close();
        dm.close();

        PanierManager pm = new PanierManager(this);
        pm.open();
        Panier panier = pm.getPanier(userId);
        valueNbPhotos = (TextView) findViewById(R.id.valueNbPhotos);
        valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
        valueHT.setText(String.valueOf(panier.getTotalPriceHT())+"€");
        valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");

        pm.close();

        mRecyclerView.setAdapter(new PhotoModifieeAdapter(array));

    }

    public class PhotoModifieeAdapter extends RecyclerView.Adapter<PhotoModifieeAdapter.PhotoModifieeViewHolder> {

        private List<PhotoModifiee> photoModifiees;

        public class PhotoModifieeViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            ImageButton imageDelete;
            ImageView ivMasque;
            TextView quantite;
            TextView prix;
            Button plus;
            Button moins;

            public PhotoModifieeViewHolder(final View itemView) {
                super(itemView);

                if(itemView != null) {
                    iv = (ImageView) itemView.findViewById(R.id.imagePhotoPanier);
                    imageDelete = (ImageButton) itemView.findViewById(R.id.supprimerPhotoPanier);
                    ivMasque = (ImageView) itemView.findViewById(R.id.masquePanier);
                    quantite = (TextView) itemView.findViewById(R.id.quantitePhotoPanier);
                    prix = (TextView) itemView.findViewById(R.id.prixPhotoPanier);
                    plus = (Button) itemView.findViewById(R.id.buttonplus);
                    moins = (Button) itemView.findViewById(R.id.buttonmoins);

                    plus.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            String str =  plus.getTag(R.id.buttonplus).toString();
                            dm.open();
                            PhotoModifiee photo = dm.getPhotoModifiee(Integer.parseInt(str.split(",")[0].substring(1)));
                            if(photo.getNbPhotos()<5) {
                                photo.setNbPhotos(photo.getNbPhotos() + 1);
                                dm.modPhotoModifiee(photo);
                                PanierManager pm = new PanierManager(itemView.getContext());
                                pm.open();
                                Panier panier = new Panier();
                                panier = pm.getPanier(userId);
                                panier.setNbPhotos(panier.getNbPhotos()+1);
                                panier.setTotalPriceHT(panier.getTotalPriceHT() + photo.getPrix());
                                BigDecimal bd = new BigDecimal(panier.getTotalPriceHT() * (float) 1.206);
                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                                panier.setPrixTTC(bd.floatValue());
                                pm.modPanier(panier);
                                pm.close();

                                valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
                                valueHT.setText(String.valueOf(panier.getTotalPriceHT())+"€");
                                valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");
                                quantite.setText(String.valueOf(photo.getNbPhotos()));
                                prix.setText(String.valueOf(photo.getPrix()*photo.getNbPhotos())+"€");
                            }

                            dm.close();

                        }
                    });

                    moins.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            String str =  moins.getTag(R.id.buttonmoins).toString();
                            dm.open();
                            PhotoModifiee photo = dm.getPhotoModifiee(Integer.parseInt(str.split(",")[0].substring(1)));
                            if(photo.getNbPhotos()>1) {
                                photo.setNbPhotos(photo.getNbPhotos() -1 );
                                dm.modPhotoModifiee(photo);
                                PanierManager pm = new PanierManager(itemView.getContext());
                                pm.open();
                                Panier panier = new Panier();
                                panier = pm.getPanier(userId);
                                panier.setNbPhotos(panier.getNbPhotos()-1);
                                panier.setTotalPriceHT(panier.getTotalPriceHT() - photo.getPrix());
                                BigDecimal bd = new BigDecimal(panier.getTotalPriceHT() * (float) 1.206);
                                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                                panier.setPrixTTC(bd.floatValue());
                                pm.modPanier(panier);
                                pm.close();

                                valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
                                valueHT.setText(String.valueOf(panier.getTotalPriceHT())+"€");
                                valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");
                                quantite.setText(String.valueOf(photo.getNbPhotos()));
                                prix.setText(String.valueOf(photo.getPrix())+"€");
                            }
                            dm.close();
                        }
                    });

                    imageDelete.setOnClickListener(new ImageButton.OnClickListener() {
                        public void onClick(View v) {

                            dm.open();
                            Object tag = imageDelete.getTag(R.id.supprimerPhotoPanier);
                            String str = tag.toString();


                            array.remove(Integer.parseInt(str.split(", ")[1].split("]")[0]));
                            mRecyclerView.getAdapter().notifyDataSetChanged();

                            PanierManager pm = new PanierManager(itemView.getContext());
                            pm.open();
                            Panier panier = new Panier();
                            panier=pm.getPanier(userId);
                            PhotoModifiee p = new PhotoModifiee();
                            p = dm.getPhotoModifiee((Integer.parseInt(str.split(",")[0].substring(1))));
                            panier.setNbPhotos(panier.getNbPhotos()-p.getNbPhotos());
                            panier.setTotalPriceHT(panier.getTotalPriceHT() - (p.getPrix()*p.getNbPhotos()));
                            BigDecimal bd = new BigDecimal(panier.getTotalPriceHT() * (float) 1.206);
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                            panier.setPrixTTC(bd.floatValue());
                            pm.modPanier(panier);
                            dm.supPhotoModifiee(Integer.parseInt(str.split(",")[0].substring(1)));

                            valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
                            valueHT.setText(String.valueOf(panier.getTotalPriceHT())+"€");
                            valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");

                            dm.close();
                            pm.close();

                        }
                    });
                }

            }

                public void setPhotoModifiee(PhotoModifiee d, int position) {
                    List<Integer> liste = new ArrayList<>();
                    liste.add(d.getPhotoId());
                    liste.add(position);
                    imageDelete.setTag(R.id.supprimerPhotoPanier, liste);
                    plus.setTag(R.id.buttonplus, liste);
                    moins.setTag(R.id.buttonmoins, liste);

                    iv.setImageURI(Uri.parse( d.getUriOrigine()));
                    quantite.setText(String.valueOf(d.getNbPhotos()));
                    prix.setText(String.valueOf(d.getPrix())+"€");

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) new URL(d.getUriFinale()).openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    connection.setDoInput(true);
                    try {
                        connection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream input = null;
                    try {
                        input = connection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    if(myBitmap != null){
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Glide.with(PanierActivity.this)
                                .load(stream.toByteArray())
                                .asBitmap()
                                .into(ivMasque);
                    }
            }


        }

        public PhotoModifieeAdapter(List<PhotoModifiee> pms) {
            this.photoModifiees = pms;
        }

        @Override
        public PhotoModifieeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_panier, parent, false);

            return new PhotoModifieeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PhotoModifieeViewHolder holder, int position) {
            PhotoModifiee d = photoModifiees.get(position);
            ((PhotoModifieeViewHolder) holder).setPhotoModifiee(photoModifiees.get(position), position);
        }

        @Override
        public int getItemCount() {
            return photoModifiees.size();
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

    @OnClick(R.id.button_validerPanier)
    public void onClickValiderPanier(View view){
        Intent intent = new Intent(this, FacturationActivity.class);
        startActivity(intent);
    }


}
