package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DestinatairesManager {

    private static final String TABLE_NAME = "destinataires";
    public static final String KEY_ID_DESTINATAIRES="id_destinataires";
    public static final String KEY_ID_PHOTO="id_photo";
    public static final String KEY_ID_MESSAGE="id_message";
    public static final String KEY_ID_USER="id_user";
    public static final String KEY_CIVILITE="civilite";
    public static final String KEY_NOM="nom";
    public static final String KEY_PRENOM="prenom";
    public static final String KEY_MOBILE="mobile";
    public static final String KEY_EMAIL="email";
    public static final String KEY_RUE="rue";
    public static final String KEY_CP="cp";
    public static final String KEY_VILLE="ville";
    public static final String CREATE_TABLE_DESTINATAIRES = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID_DESTINATAIRES+" INTEGER primary key," +
            " "+KEY_ID_PHOTO+" INTEGER," +
            " "+KEY_ID_USER+" INTEGER," +
            " "+KEY_ID_MESSAGE+" INTEGER," +
            " "+KEY_CIVILITE+" TEXT," +
            " "+KEY_NOM+" TEXT," +
            " "+KEY_PRENOM+" TEXT," +
            " "+KEY_MOBILE+" TEXT," +
            " "+KEY_EMAIL+" TEXT," +
            " "+KEY_RUE+" TEXT," +
            " "+KEY_CP+" TEXT," +
            " "+KEY_VILLE+" TEXT" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public DestinatairesManager(Context context)
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

    public long addDestinataires(Destinataires destinataires) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(KEY_CIVILITE, destinataires.getCivilite());
        values.put(KEY_CP, destinataires.getCp());
        values.put(KEY_ID_DESTINATAIRES, destinataires.getId());
        values.put(KEY_ID_USER, destinataires.getIdUser());
        values.put(KEY_EMAIL, destinataires.getEmail());
        values.put(KEY_NOM, destinataires.getNom());
        values.put(KEY_PRENOM, destinataires.getPrenom());
        values.put(KEY_RUE, destinataires.getRue());
        values.put(KEY_VILLE, destinataires.getVille());
        values.put(KEY_ID_PHOTO, destinataires.getIdPhoto());
        values.put(KEY_ID_MESSAGE, destinataires.getIdMessage());
        values.put(KEY_MOBILE, destinataires.getMobile());

        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int modDestinataires(Destinataires destinataires) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(KEY_CIVILITE, destinataires.getCivilite());
        values.put(KEY_CP, destinataires.getCp());
        values.put(KEY_ID_DESTINATAIRES, destinataires.getIdUser());
        values.put(KEY_ID_USER, destinataires.getId());
        values.put(KEY_EMAIL, destinataires.getEmail());
        values.put(KEY_NOM, destinataires.getNom());
        values.put(KEY_PRENOM, destinataires.getPrenom());
        values.put(KEY_RUE, destinataires.getRue());
        values.put(KEY_VILLE, destinataires.getVille());
        values.put(KEY_ID_PHOTO, destinataires.getIdPhoto());
        values.put(KEY_ID_MESSAGE, destinataires.getIdMessage());
        values.put(KEY_MOBILE, destinataires.getMobile());

        String where = KEY_ID_DESTINATAIRES+" = ?";
        String[] whereArgs = {destinataires.getId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supDestinataires(int dest) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = KEY_ID_DESTINATAIRES+" = ?";
        String[] whereArgs = {dest+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public Destinataires getDestinataire(int id) {

        Destinataires d=new Destinataires(0,"","","","","","","","");

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_DESTINATAIRES+"="+id, null);
        if (c.moveToFirst()) {
            d.setId(c.getInt(c.getColumnIndex(KEY_ID_DESTINATAIRES)));
            d.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            d.setCivilite(c.getString(c.getColumnIndex(KEY_CIVILITE)));
            d.setIdPhoto(c.getInt(c.getColumnIndex(KEY_ID_PHOTO)));
            d.setIdUser(c.getInt(c.getColumnIndex(KEY_ID_USER)));
            d.setIdMessage(c.getInt(c.getColumnIndex(KEY_ID_MESSAGE)));
            d.setCp(c.getString(c.getColumnIndex(KEY_CP)));
            d.setMobile(c.getString(c.getColumnIndex(KEY_MOBILE)));
            d.setNom(c.getString(c.getColumnIndex(KEY_NOM)));
            d.setPrenom(c.getString(c.getColumnIndex(KEY_PRENOM)));
            d.setRue(c.getString(c.getColumnIndex(KEY_RUE)));
            d.setVille(c.getString(c.getColumnIndex(KEY_VILLE)));
            c.close();
        }

        return d;
    }

    public Cursor getDestinatairesWithoutPhoto(int idUser) {

        Destinataires d=new Destinataires(0,"","","","","","","","");

        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_PHOTO+"= '0' AND "+KEY_ID_USER + "=" + idUser, null);

    }

    public Cursor getDestinataires(int idUser) {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME +" WHERE "+KEY_ID_USER + "=" + idUser, null);
    }

}