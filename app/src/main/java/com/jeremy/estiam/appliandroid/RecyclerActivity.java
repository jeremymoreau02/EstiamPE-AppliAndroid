package com.jeremy.estiam.appliandroid;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jeremy.estiam.appliandroid.models.Panier;
import com.jeremy.estiam.appliandroid.models.PanierManager;
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


public class RecyclerActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTO= 21;
    static final int MY_PERMISSIONS_REQUEST_WRITE_PHOTO= 22;
    private int PICK_IMAGE_REQUEST = 1;
    private int SELECT_PICTURE = 2;
    private Uri imageUri;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_WRITE_PHOTO = 5;

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    List<Photo> array = new ArrayList<>();
    @BindView(R.id.photos_recycler_view)
    RecyclerView recycler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
        Date date = new Date();
        String dateStr = sharedPreferences.getString("CreateDate", "NULL");
        System.out.println(sharedPreferences.getString("id", "NULL"));
        if (!dateStr.equals(sharedPreferences.getString("id", "NULL"))) {
            Date date2 = null;
            try {
                date2 = dateFormat.parse(dateStr);

                Long date3 = date.getTime() - date2.getTime();
                System.out.println(date3);
                if (date3 > 86400000) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        PanierManager pm = new PanierManager(this);
        pm.open();
        Panier p =pm.getPanier(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));
        if((p==null)||(p.getId()==0)){
            pm.addPanier(Integer.parseInt(this.getSharedPreferences("InfosUtilisateur", Context.MODE_PRIVATE).getString("id", "NULL")));
        }

        pm.close();
        GridLayoutManager llm = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(llm);

        recycler.addItemDecoration(new SpacesItemDecoration(25));

        recycler.setAdapter(new PhotoAdapter(array));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Recycler Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class PhotoAdapter extends RecyclerView.Adapter {

        private List<Photo> photos;


        public PhotoAdapter(List<Photo> photos) {
            this.photos = photos;
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            Context contexte;

            public PhotoViewHolder(View itemView, Context contexte) {
                super(itemView);


                iv = (ImageView) itemView.findViewById(R.id.imageView2);
                iv.setOnClickListener(new ImageView.OnClickListener() {
                    public void onClick(View v) {

                        Log.v("ughlmj", iv.getTag(R.id.imageView2).toString());
                        Intent intent = new Intent(RecyclerActivity.this, EditPhotoActivity.class);
                        intent.putExtra("UriPhotoString", iv.getTag(R.id.imageView2).toString());
                        startActivity(intent);
                    }
                });


                this.contexte = contexte;
            }

            public void setPhoto(Photo photo) {
                Uri uriPhoto= photo.getUri();
                iv.setTag(R.id.imageView2, uriPhoto);
                Log.v("ughlmj45", uriPhoto.toString());
                Glide.with(contexte).load(photo.getUri()).into(iv);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_cell_recycler, parent, false), parent.getContext());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((PhotoViewHolder) holder).setPhoto(photos.get(position));
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
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {



                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_TAKE_PHOTO);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }else{
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                }



            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_TAKE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(mCurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                    startActivityForResult(mediaScanIntent,REQUEST_WRITE_PHOTO);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
            Photo p = new Photo("chat", uri);
            Log.v("imageid", Integer.toString(p.getId()));
            array.add(p);
            recycler.getAdapter().notifyDataSetChanged();

        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {



                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_TAKE_PHOTO);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                array.add(new Photo("efg",contentUri));
                recycler.getAdapter().notifyDataSetChanged();

            }


        }else if (requestCode == REQUEST_TAKE_PHOTO){
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            array.add(new Photo("efg",contentUri));
            recycler.getAdapter().notifyDataSetChanged();
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

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

}
