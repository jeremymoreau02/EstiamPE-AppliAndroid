package com.jeremy.estiam.appliandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremy.estiam.appliandroid.models.Photo;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecyclerActivity extends AppCompatActivity  {
    private int PICK_IMAGE_REQUEST = 1;
    private int SELECT_PICTURE = 2;
    private Uri imageUri;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    static final int REQUEST_TAKE_PHOTO = 2;

    List<Photo> array = new ArrayList<>();
    @BindView(R.id.photos_recycler_view)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
        Date date = new Date();
        String dateStr = sharedPreferences.getString("CreateDate", "NULL");
        if(!dateStr.equals("NULL")){
            Date date2 = null;
            try {
                date2 = dateFormat.parse(dateStr);

                Long date3 = date.getTime()-date2.getTime();
                System.out.println(date3);
                if (date3 > 86400000 ) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        GridLayoutManager llm = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(llm);

        recycler.setAdapter(new PhotoAdapter(array));
    }

    public class PhotoAdapter extends RecyclerView.Adapter{

        private List<Photo> photos ;


        public PhotoAdapter(List<Photo> photos) {
            this.photos = photos;
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            Context contexte;

            public PhotoViewHolder(View itemView, Context contexte) {
                super(itemView);

                iv=  (ImageView) itemView.findViewById(R.id.imageView2);
                iv.setOnClickListener(new ImageView.OnClickListener(){
                    public void onClick(View v) {

                        Log.v("ughlmj", v.toString());
                        v.toString();
                    }
                });
                this.contexte=contexte;
            }

            public void setPhoto(Photo photo){
                Log.v("image", Integer.toString(photo.getId()));
                Glide.with(contexte).load(photo.getUri()).into(iv);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_cell_recycler, parent, false), parent.getContext());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((PhotoViewHolder) holder).setPhoto( photos.get(position));
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

    }

    @OnClick(R.id.addphoto_button)
    public void addPhoto(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @OnClick(R.id.newphoto_button)
    public void newPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.jeremy.estiam.appliandroid.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Photo p = new Photo("chat" , uri);
            Log.v("imageid", Integer.toString(p.getId()));
            array.add(p);
            recycler.getAdapter().notifyDataSetChanged();

        }
    }

    @OnClick(R.id.button_contact)
    public void onClickContact(){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_deconnexion)
    public void onClickDeconnexion(){
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("token","NULL").apply();
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("id","NULL").apply();
        this.getSharedPreferences("InfosUtilisateur", MODE_PRIVATE).edit().putString("CreateDate","NULL").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.button_parametres)
    public void onClickUserInfo(){
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

}
