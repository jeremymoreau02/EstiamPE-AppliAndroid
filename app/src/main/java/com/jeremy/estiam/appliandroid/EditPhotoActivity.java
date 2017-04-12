package com.jeremy.estiam.appliandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jeremy.estiam.appliandroid.models.Dimensions;
import com.jeremy.estiam.appliandroid.models.DimensionsManager;
import com.jeremy.estiam.appliandroid.models.Masks;
import com.jeremy.estiam.appliandroid.models.MasksManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class EditPhotoActivity extends AppCompatActivity {
    private Uri PhotoOrigine ;
    private String masqueUrl ;
    private int idMasque;
    private float prixMasque;
    @BindView(R.id.PhotoGrosseEdit)
    ImageView iv;
    List<Dimensions> dimensions = new ArrayList<Dimensions>();
    List<Masks> masks = new ArrayList<Masks>();

    @BindView(R.id.recycler_edit_photo)
    RecyclerView recycler;

    List<Masks> masquesDisplay = new ArrayList<Masks>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        ButterKnife.bind(this);

        PhotoOrigine = Uri.parse(getIntent().getExtras().getString("UriPhotoString"));



        Glide.with(this).load(PhotoOrigine).into(iv);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        String gdh = PhotoOrigine.getPath();
        BitmapFactory.decodeFile(PhotoOrigine.getPath(), o);
        int imageHeight = o.outHeight;
        int imageWidth = o.outWidth;

        DimensionsManager dm = new DimensionsManager(this);
        dm.open();
        Cursor c = dm.getDimensions();

        ArrayList<String> spinnerArray = new ArrayList<String>();

        while(c.moveToNext()){
            Dimensions d = new Dimensions();

            d.setId(c.getInt(c.getColumnIndex( DimensionsManager.KEY_ID)));
            d.setCreatedAt(c.getString(c.getColumnIndex( DimensionsManager.KEY_CREATED)));
            d.setUpdatedAt(c.getString(c.getColumnIndex( DimensionsManager.KEY_UPDATED)));
            d.setHeight(c.getInt(c.getColumnIndex( DimensionsManager.KEY_HEIGHT)));
            d.setWidth(c.getInt(c.getColumnIndex( DimensionsManager.KEY_WIDTH)));
            d.setName(c.getString(c.getColumnIndex( DimensionsManager.KEY_NAME)));
            dimensions.add(d);
            spinnerArray.add(d.getName());
        }
        c.close();

        dm.close();

        MasksManager mm = new MasksManager(this);
        mm.open();
        c = mm.getMasks();

        while(c.moveToNext()){
            Masks d = new Masks();

            d.setId(c.getInt(c.getColumnIndex( MasksManager.KEY_ID)));
            d.setDimensionId(c.getInt(c.getColumnIndex( MasksManager.KEY_ID_DIMENSION)));
            d.setPrice(c.getFloat(c.getColumnIndex( MasksManager.KEY_PRICE)));
            d.setCreatedAt(c.getString(c.getColumnIndex( MasksManager.KEY_CREATED)));
            d.setUpdatedAt(c.getString(c.getColumnIndex( MasksManager.KEY_UPDATED)));
            d.setName(c.getString(c.getColumnIndex( MasksManager.KEY_NAME)));
            d.setFilePath(c.getString(c.getColumnIndex( MasksManager.KEY_FILEPATH)));
            masks.add(d);
            if(d.getDimensionId() == dimensions.get(0).getId()){
                masquesDisplay.add(d);
            }
        }
        c.close();
        mm.close();

        Spinner listeFormats = (Spinner) findViewById(R.id.ListeFormats);


        listeFormats.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerArray));
        listeFormats.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        GridLayoutManager llm = new GridLayoutManager(this, 4);
        recycler.setLayoutManager(llm);

        recycler.addItemDecoration(new SpacesItemDecoration(25));

        recycler.setAdapter(new MasquesAdapter(masquesDisplay));
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

    public class MasquesAdapter extends RecyclerView.Adapter {

        private List<Masks> masques;


        public MasquesAdapter(List<Masks> masques) {
            this.masques = masques;
        }

        public class MasquesViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            Context contexte;

            public MasquesViewHolder(View itemView, Context contexte) {
                super(itemView);


                iv = (ImageView) itemView.findViewById(R.id.imageView5);
                iv.setOnClickListener(new ImageView.OnClickListener() {
                    public void onClick(View v) {
                        for(int i =0; i< masquesDisplay.size(); i++){
                            if(masquesDisplay.get(i).getId() == Integer.parseInt(iv.getTag(R.id.imageView5).toString())){
                                System.out.println(masquesDisplay.get(i).getName());

                                    masqueUrl = "http://193.70.40.193:3000/" + masquesDisplay.get(i).getFilePath();
                                    idMasque = masquesDisplay.get(i).getId();
                                prixMasque = masquesDisplay.get(i).getPrice();

                                    Intent intent = new Intent(EditPhotoActivity.this, AddDestinataireActivity.class);
                                    String dfgh = PhotoOrigine.toString();
                                    intent.putExtra("UriPhotoString", PhotoOrigine.toString());
                                    intent.putExtra("masqueUrl", masqueUrl);
                                    intent.putExtra("idMasque", idMasque);
                                    intent.putExtra("prixMasque", prixMasque);
                                    startActivity(intent);

                                /*try {
                                    new RetrieveFeedTask().execute(new URL("http://193.70.40.193:3000/" + masquesDisplay.get(i).getFilePath()));
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }*/

                            }
                        }
                    }
                });


                this.contexte = contexte;
            }

            public void setMasque(Masks masque) {
                int id= masque.getId();
                iv.setTag(R.id.imageView5, id);
                String url = "http://193.70.40.193:3000/" + masque.getFilePath();
                Glide.with(contexte).load(url).into(iv);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new MasquesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_edit_photo, parent, false), parent.getContext());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MasquesViewHolder) holder).setMasque(masques.get(position));
        }

        @Override
        public int getItemCount() {
            return masques.size();
        }

    }

    class RetrieveFeedTask extends AsyncTask<URL, Void, Bitmap> {

        private Exception exception;

        @Override
        protected Bitmap doInBackground(URL ... url) {
            try {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url[0].openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);


                    Bitmap image = null;
                    try {
                        image = Glide.with(EditPhotoActivity.this).load(PhotoOrigine).asBitmap().into(100,100).get();
                        /*Bitmap result = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(),Bitmap.Config.ARGB_8888);
                        Canvas mCanvas = new Canvas(result);
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                        mCanvas.drawBitmap(image, 0, 0, null);
                        mCanvas.drawBitmap(myBitmap, 0, 0, paint);
                        paint.setXfermode(null);*/
                        BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                        // create our paint and attach the shader to it
                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setShader(shader);
                        paint.setStyle(Paint.Style.FILL);

                        Bitmap alphaMask = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.ALPHA_8);
                        Canvas canvas = new Canvas(alphaMask);
                        canvas.drawBitmap(myBitmap, 0.0f, 0.0f, null);
                        if(null != myBitmap){
                            canvas.drawBitmap(myBitmap, 0, 0, paint);
                            return myBitmap;
                        }
                        return null;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    // Log exception
                    Log.getStackTraceString(e);
                }
                return null;
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if(bitmap != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Glide.with(EditPhotoActivity.this)
                        .load(stream.toByteArray())
                        .asBitmap()
                        .into(iv);
            }


        }
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            masquesDisplay.clear();
            DimensionsManager dm = new DimensionsManager(EditPhotoActivity.this);
            dm.open();
            Cursor c = dm.getDimensions();

            int idDim = 0;
            while(c.moveToNext()){
                String dfgfdh= c.getString(c.getColumnIndex( DimensionsManager.KEY_NAME));
                String zfdg = parent.getItemAtPosition(pos).toString();
                if(c.getString(c.getColumnIndex( DimensionsManager.KEY_NAME)).equals(parent.getItemAtPosition(pos).toString())){
                    idDim = c.getInt(c.getColumnIndex( DimensionsManager.KEY_ID));
                }
            }
            c.close();

            dm.close();

            for(int i=0; i < masks.size(); i++){
                if(masks.get(i).getDimensionId() == idDim){
                    masquesDisplay.add(masks.get(i));
                }
            }
            dm.close();

            recycler.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }


}
