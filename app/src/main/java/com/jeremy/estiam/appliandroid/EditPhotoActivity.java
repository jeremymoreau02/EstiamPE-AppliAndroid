package com.jeremy.estiam.appliandroid;

import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditPhotoActivity extends AppCompatActivity {
    private Uri PhotoOrigine ;
    @BindView(R.id.PhotoGrosseEdit)
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        ButterKnife.bind(this);

        PhotoOrigine = Uri.parse(getIntent().getExtras().getString("UriPhotoString"));
        Glide.with(this).load(PhotoOrigine).into(iv);

    }
}
