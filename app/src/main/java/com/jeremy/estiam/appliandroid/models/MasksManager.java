package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.List;

public class MasksManager {

    public static final String TABLE_NAME = "masks";
    public static final String KEY_ID="id";
    public static final String KEY_ID_DIMENSION="id_dimension";
    public static final String KEY_PRICE="price";
    public static final String KEY_NAME="name";
    public static final String KEY_FILEPATH="filePath";
    public static final String KEY_CREATED="created";
    public static final String KEY_UPDATED="updated";
    public static final String CREATE_TABLE_DESTINATAIRES = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID+" INTEGER primary key," +
            " "+KEY_ID_DIMENSION+" INTEGER," +
            " "+KEY_PRICE+" FLOAT(10,4)," +
            " "+KEY_FILEPATH+" TEXT," +
            " "+KEY_NAME+" TEXT," +
            " "+KEY_CREATED+" TEXT," +
            " "+KEY_UPDATED+" TEXT" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public MasksManager(Context context)
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

    public void addMasks(List<Masks> masks) {
        // Ajout d'un enregistrement dans la table

        Iterator<Masks> it = masks.iterator();
        while(it.hasNext()){
            Masks m = it.next();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, m.getId());
            values.put(KEY_ID_DIMENSION, m.getDimensionId());
            values.put(KEY_FILEPATH, m.getFilePath());
            values.put(KEY_PRICE, m.getPrice());
            values.put(KEY_NAME, m.getName());
            values.put(KEY_CREATED, m.getCreatedAt());
            values.put(KEY_UPDATED, m.getUpdatedAt());

            // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas m'erreur
            db.insert(TABLE_NAME,null,values);
        }

    }

    public int supAllMasks() {
        String where = "";
        String[] whereArgs = {};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Cursor getMasks() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME + ";", null);
    }

}