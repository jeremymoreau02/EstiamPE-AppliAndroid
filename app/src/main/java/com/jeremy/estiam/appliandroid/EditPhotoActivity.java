package com.jeremy.estiam.appliandroid;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jeremy.estiam.appliandroid.models.Dimensions;
import com.jeremy.estiam.appliandroid.models.DimensionsManager;
import com.jeremy.estiam.appliandroid.models.Masks;
import com.jeremy.estiam.appliandroid.models.MasksManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPhotoActivity extends AppCompatActivity {
    private Uri PhotoOrigine ;
    @BindView(R.id.PhotoGrosseEdit)
    ImageView iv;
    List<Dimensions> dimensions;
    List<Masks> masks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        ButterKnife.bind(this);

        PhotoOrigine = Uri.parse(getIntent().getExtras().getString("UriPhotoString"));

        Glide.with(this).load(PhotoOrigine).into(iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            System.out.println(iv.getDrawable().getLayoutDirection());
        }

       /* DimensionsManager dm = new DimensionsManager(this);
        dm.open();
        Cursor c = dm.getDimensions();

        while(c.moveToNext()){
            Dimensions d = new Dimensions();

            d.setId(c.getInt(c.getColumnIndex( DimensionsManager.KEY_ID)));
            d.setCreatedAt(c.getString(c.getColumnIndex( DimensionsManager.KEY_CREATED)));
            d.setUpdatedAt(c.getString(c.getColumnIndex( DimensionsManager.KEY_UPDATED)));
            d.setHeight(c.getInt(c.getColumnIndex( DimensionsManager.KEY_HEIGHT)));
            d.setWidth(c.getInt(c.getColumnIndex( DimensionsManager.KEY_WIDTH)));
            d.setName(c.getString(c.getColumnIndex( DimensionsManager.KEY_NAME)));
            dimensions.add(d);
        }
        c.close();

        dm.close();

        MasksManager mm = new MasksManager(this);
        mm.open();
        c = mm.getMasks();

        while(c.moveToNext()){
            Masks d = new Masks();

            d.setId(c.getInt(c.getColumnIndex( MasksManager.KEY_ID)));
            d.setId(c.getInt(c.getColumnIndex( MasksManager.KEY_ID_DIMENSION)));
            d.setPrice(c.getFloat(c.getColumnIndex( MasksManager.KEY_PRICE)));
            d.setCreatedAt(c.getString(c.getColumnIndex( MasksManager.KEY_CREATED)));
            d.setUpdatedAt(c.getString(c.getColumnIndex( MasksManager.KEY_UPDATED)));
            d.setName(c.getString(c.getColumnIndex( MasksManager.KEY_NAME)));
            d.setFilePath(c.getString(c.getColumnIndex( MasksManager.KEY_FILEPATH)));
            masks.add(d);
        }
        c.close();
        mm.close();*/
    }

    @OnClick(R.id.buttonEditPhoto)
    public void addDestinataire(View view) {
        Intent intent = new Intent(this, AddDestinataireActivity.class);
        String dfgh = PhotoOrigine.toString();
        intent.putExtra("UriPhotoString", PhotoOrigine.toString());
        startActivity(intent);

    }
}
