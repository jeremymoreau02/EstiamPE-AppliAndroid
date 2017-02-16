package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageDestinatairesManager {

    private static final String TABLE_NAME = "messages_destinataires";
    public static final String KEY_ID_MESSAGE="id_message";
    public static final String KEY_MESSAGE="message";
    public static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID_MESSAGE+" INTEGER primary key," +
            " "+KEY_MESSAGE+" TEXT" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public MessageDestinatairesManager(Context context)
    {
        maBaseSQLite = MySQLite.getInstance(context);
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

    public long addMessage(MessageDestinataire message) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, message.getMessage());
        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int modMessage(MessageDestinataire message) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, message.getMessage());

        String where = KEY_ID_MESSAGE+" = ?";
        String[] whereArgs = {message.getID()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supDestinataires(int mess) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = KEY_ID_MESSAGE+" = ?";
        String[] whereArgs = {mess+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public String getMessage(int id) {

        String s = "";

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_MESSAGE+"="+id, null);
        if (c.moveToFirst()) {
            s=c.getString(c.getColumnIndex(KEY_MESSAGE));
            c.close();
        }

        return s;
    }

    public Cursor getMessages() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

}