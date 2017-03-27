package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremy.estiam.appliandroid.api.ApiService;
import com.jeremy.estiam.appliandroid.api.ServiceGenerator;
import com.jeremy.estiam.appliandroid.models.Adresse;
import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
import com.jeremy.estiam.appliandroid.models.Shipping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LivraisonActivity extends AppCompatActivity {

    private List<Shipping> methodes;
    private RecyclerView recycler;
    private int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livraison);

        userId = Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL"));

        recycler=(RecyclerView) findViewById(R.id.recycler_livraison);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);



        ShippingRecupTask shippingRecupTask = new ShippingRecupTask();
        shippingRecupTask.execute();
    }

    public class MethodesAdapter extends RecyclerView.Adapter<MethodesAdapter.MethodesViewHolder> {

        private List<Shipping> methodes;

        public class MethodesViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView delai;
            TextView prix;
            View iv;

            public MethodesViewHolder(View itemView) {
                super(itemView);

                iv = itemView;
                name = (TextView) itemView.findViewById(R.id.nomLivraison);
                delai = (TextView) itemView.findViewById(R.id.delaiLivraison);
                prix = (TextView) itemView.findViewById(R.id.prixLivraison);


                iv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        PanierManager pm = new PanierManager(LivraisonActivity.this);
                        pm.open();
                        Panier p =  pm.getPanier(userId);
                        p.setIdLivraison(Integer.parseInt(name.getTag(R.id.nomLivraison).toString()));
                        pm.modPanier(p);
                        pm.close();
                        Intent intent = new Intent(LivraisonActivity.this, RecapActivity.class);
                        intent.putExtra("nameLivraison",methodes.get(p.getIdLivraison()).getName());
                        intent.putExtra("prixLivraison",methodes.get(p.getIdLivraison()).getPrice());
                        intent.putExtra("delaiLivraison",methodes.get(p.getIdLivraison()).getShippingDuration());
                        startActivity(intent);
                    }
                });

            }

            public void setMethodes(Shipping s, int position) {
                name.setText(s.getName());
                delai.setText(String.valueOf(s.getShippingDuration())+"jours");
                prix.setText(String.valueOf(s.getPrice())+"€");
                name.setTag(R.id.nomLivraison, position);
            }


        }

        public MethodesAdapter(List<Shipping> methodes) {
            this.methodes = methodes;
        }

        @Override
        public MethodesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_livraison, parent, false);

            return new MethodesAdapter.MethodesViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MethodesViewHolder holder, int position) {
            Shipping s = methodes.get(position);
            ((MethodesViewHolder) holder).setMethodes(methodes.get(position), position);
        }

        @Override
        public int getItemCount() {
            return methodes.size();
        }

    }

    public class ShippingRecupTask extends AsyncTask<Void, Void , String> {
        @Override
        protected String doInBackground(Void... voids) {

            ApiService apiService = new ServiceGenerator().createService(ApiService.class);

            Call<List<Shipping>> call = apiService.getMethodes( LivraisonActivity.this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("token","NULL"));

            try {
                Response<List<Shipping>> shippingResponse = call.execute();
                // user = userResponse.body();
                List<Shipping> shipping = shippingResponse.body();
                methodes=shipping;


            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String fg){
            recycler.setAdapter(new MethodesAdapter(methodes));
           // recycler.getAdapter().notifyDataSetChanged();
        }

    }

}
