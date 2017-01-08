package com.jeremy.estiam.appliandroid.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeremy on 07/01/2017.
 */

public class PhotoSQLite extends SQLiteOpenHelper {

    private static final String TABLE_PHOTOS = "table_photos";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_PATH = "PATH";
    private static final String COL_URI = "URI";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_PHOTOS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_PATH + " TEXT NOT NULL,"  + COL_URI +" TEXT NOT NULL);";


    public PhotoSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on crée la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut faire ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_PHOTOS + ";");
        onCreate(db);
    }
}
