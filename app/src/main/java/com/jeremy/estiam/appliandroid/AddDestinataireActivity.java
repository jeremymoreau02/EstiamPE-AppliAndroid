package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jeremy.estiam.appliandroid.adapters.MyAdapter;
import com.jeremy.estiam.appliandroid.models.Destinataires;
import com.jeremy.estiam.appliandroid.models.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class AddDestinataireActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Uri PhotoOrigineUri;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Destinataires> array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destinataire);

        PhotoOrigineUri = Uri.parse(getIntent().getExtras().getString("UriPhotoString"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_destinataire);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new DestinatairesAdapter(array));
    }

    public class DestinatairesAdapter extends RecyclerView.Adapter {

        private List<Destinataires> destinataires;


        public DestinatairesAdapter(List<Destinataires> destinataires) {
            this.destinataires = destinataires;
        }

        public class DestinatairesViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            Context contexte;

            public DestinatairesViewHolder(View itemView, Context contexte) {
                super(itemView);

                iv = (ImageView) itemView.findViewById(R.id.imageView2);

                this.contexte = contexte;
            }

            public void setDestinataires(Destinataires destinataires) {
//                Uri uriPhoto= photo.getUri();
//                iv.setTag(R.id.imageView2, uriPhoto);
//                Log.v("ughlmj45", uriPhoto.toString());
//                Glide.with(contexte).load(photo.getUri()).into(iv);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new DestinatairesAdapter.DestinatairesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_cell_recycler, parent, false), parent.getContext());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((DestinatairesAdapter.DestinatairesViewHolder) holder).setDestinataires(destinataires.get(position));
        }

        @Override
        public int getItemCount() {
            return destinataires.size();
        }

    }

    @OnClick(R.id.buttonAddDestinataire)
    public void addDestinataire(View view) {
        Intent intent = new Intent(this, DestinataireActivity.class);
        intent.putExtra("UriPhotoString", PhotoOrigineUri.getPath());
        startActivity(intent);

    }


}


