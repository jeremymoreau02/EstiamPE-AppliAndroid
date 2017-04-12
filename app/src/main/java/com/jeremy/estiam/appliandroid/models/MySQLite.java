package com.jeremy.estiam.appliandroid.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static MySQLite sInstance;

    public static synchronized MySQLite getInstance(Context context) {
        if (sInstance == null) { sInstance = new MySQLite(context); }
        return sInstance;
    }

    private MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la base de données
        // on exécute ici les requêtes de création des tables
        sqLiteDatabase.execSQL(PanierManager.CREATE_TABLE_PANIER);
        sqLiteDatabase.execSQL(DestinatairesManager.CREATE_TABLE_DESTINATAIRES); // création table "animal"
        sqLiteDatabase.execSQL(MessageDestinatairesManager.CREATE_TABLE_MESSAGES);
        sqLiteDatabase.execSQL(PhotoModifieeManager.CREATE_TABLE_PHOTO_MODIFIEE);
        sqLiteDatabase.execSQL(MasksManager.CREATE_TABLE_DESTINATAIRES);
        sqLiteDatabase.execSQL(DimensionsManager.CREATE_TABLE_DESTINATAIRES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Mise à jour de la base de données
        // méthode appelée sur incrémentation de DATABASE_VERSION
        // on peut faire ce qu'on veut ici, comme recréer la base :
        onCreate(sqLiteDatabase);
    }

} // class MySQLite