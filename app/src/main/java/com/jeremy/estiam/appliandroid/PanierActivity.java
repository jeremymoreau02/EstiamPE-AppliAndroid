package com.jeremy.estiam.appliandroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.DestinatairesManager;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.PhotoModifiee;
import com.jeremy.estiam.appliandroid.models.PhotoModifieeManager;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;


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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_panier);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        dm.open();

        Cursor c = dm.getPhotosModifiees();

        while(c.moveToNext()){
            PhotoModifiee s = new PhotoModifiee();
            s.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            s.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            s.setIdFormat(c.getInt(c.getColumnIndex(KEY_FORMAT)));
            s.setNbPhotos(c.getInt(c.getColumnIndex(KEY_NB)));
            s.setPrix(c.getFloat(c.getColumnIndex(KEY_PRIX)));
            s.setIdMasque(c.getInt(c.getColumnIndex(KEY_MASQUE)));
            s.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            s.setUriFinale(c.getString(c.getColumnIndex(KEY_URI_FINALE)));
            s.setUriOrigine(c.getString(c.getColumnIndex(KEY_URI_ORIGINE)));
            array.add(s);
        }
        c.close();
        dm.close();

        PanierManager pm = new PanierManager(this);
        pm.open();
        Panier panier = pm.getPanier();
        valueNbPhotos = (TextView) findViewById(R.id.valueNbPhotos);
        valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
        valueHT.setText(String.valueOf(panier.getPrixHT())+"€");
        valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");

        pm.close();

        mRecyclerView.setAdapter(new PhotoModifieeAdapter(array));

    }

    public class PhotoModifieeAdapter extends RecyclerView.Adapter<PhotoModifieeAdapter.PhotoModifieeViewHolder> {

        private List<PhotoModifiee> photoModifiees;

        public class PhotoModifieeViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            ImageButton imageDelete;
            TextView description;
            TextView quantite;
            TextView prix;

            public PhotoModifieeViewHolder(final View itemView) {
                super(itemView);

                if(itemView != null) {
                    iv = (ImageView) itemView.findViewById(R.id.imagePhotoPanier);
                    imageDelete = (ImageButton) itemView.findViewById(R.id.supprimerPhotoPanier);
                    description = (TextView) itemView.findViewById(R.id.descriptionPanier);
                    quantite = (TextView) itemView.findViewById(R.id.quantitePhotoPanier);
                    prix = (TextView) itemView.findViewById(R.id.prixPhotoPanier);

                    imageDelete.setOnClickListener(new ImageButton.OnClickListener() {
                        public void onClick(View v) {

                            dm.open();
                            Object tag = imageDelete.getTag(R.id.supprimerPhotoPanier);
                            String str = tag.toString();
                            dm.supPhotoModifiee(Integer.parseInt(str.split(",")[0].substring(1)));
                            dm.close();
                            array.remove(Integer.parseInt(str.split(", ")[1].split("]")[0]));
                            mRecyclerView.getAdapter().notifyDataSetChanged();

                            PanierManager pm = new PanierManager(itemView.getContext());
                            pm.open();
                            Panier panier = new Panier();
                            panier=pm.getPanier();
                            panier.setNbPhotos(panier.getNbPhotos()-1);
                            pm.modPanier(panier);

                            valueNbPhotos.setText(String.valueOf(panier.getNbPhotos()));
                            valueHT.setText(String.valueOf(panier.getPrixHT())+"€");
                            valueTTC.setText(String.valueOf(panier.getPrixTTC())+"€");

                            pm.close();

                        }
                    });
                }

            }

                public void setPhotoModifiee(PhotoModifiee d, int position) {
                    List<Integer> liste = new ArrayList<>();
                    liste.add(d.getId());
                    liste.add(position);
                    imageDelete.setTag(R.id.supprimerPhotoPanier, liste);

                    iv.setImageURI(Uri.parse("content://media" + d.getUriOrigine()));
                    description.setText(d.getDescription());
                    quantite.setText("1");
                    prix.setText(String.valueOf(d.getPrix())+"€");
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

    @OnClick(R.id.button_homePanier)
    public void onClickHomePanier(View view){
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_validerPanier)
    public void onClickValiderPanier(View view){
        Intent intent = new Intent(this, FacturationActivity.class);
        startActivity(intent);
    }

}
