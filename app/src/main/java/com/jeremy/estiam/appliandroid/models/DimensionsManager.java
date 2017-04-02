package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.List;

public class DimensionsManager {

    public static final String TABLE_NAME = "dimensions";
    public static final String KEY_ID="id";
    public static final String KEY_WIDTH="width";
    public static final String KEY_HEIGHT="height";
    public static final String KEY_NAME="name";
    public static final String KEY_CREATED="created";
    public static final String KEY_UPDATED="updated";
    public static final String CREATE_TABLE_DESTINATAIRES = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID+" INTEGER primary key," +
            " "+KEY_WIDTH+" INTEGER," +
            " "+KEY_HEIGHT+" INTEGER," +
            " "+KEY_NAME+" TEXT," +
            " "+KEY_CREATED+" TEXT," +
            " "+KEY_UPDATED+" TEXT" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public DimensionsManager(Context context)
    {
        maBaseSQLite = MySQLite.getInstance(context);
        int a =1+2;
    }

    public void open()
    {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close()
    {
        //on ferme l'accès à la BDD
        db.close();
    }

    public void addDimensions(List<Dimensions> dimensions) {
        // Ajout d'un enregistrement dans la table

        Iterator<Dimensions> it = dimensions.iterator();
        while(it.hasNext()){
            Dimensions d = it.next();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, d.getId());
            values.put(KEY_WIDTH, d.getWidth());
            values.put(KEY_HEIGHT, d.getHeight());
            values.put(KEY_NAME, d.getName());
            values.put(KEY_CREATED, d.getCreatedAt());
            values.put(KEY_UPDATED, d.getUpdatedAt());

            // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
            db.insert(TABLE_NAME,null,values);
        }

    }

    public int supAllDimensions() {
        String where = "";
        String[] whereArgs = {};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Cursor getDimensions() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME + ";", null);
    }

}