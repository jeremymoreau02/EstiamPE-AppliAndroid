package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhotoModifieeManager {

    private static final String TABLE_NAME = "photo_modifiee";
    public static final String KEY_ID="id";
    public static final String KEY_ID_PANIER="id_panier";
    public static final String KEY_ID_USER="id_user";
    public static final String KEY_URI_ORIGINE="uri_origine";
    public static final String KEY_URI_FINALE="uri_finale";
    public static final String KEY_FORMAT="format";
    public static final String KEY_MASQUE="masque";
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_NB="nb";
    public static final String KEY_PRIX="prix";
    public static final String KEY_NAME="name";
    public static final String CREATE_TABLE_PHOTO_MODIFIEE = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID+" INTEGER primary key," +
            " "+KEY_ID_PANIER+" INTEGER," +
            " "+KEY_ID_USER+" INTEGER," +
            " "+KEY_FORMAT+" INTEGER," +
            " "+KEY_MASQUE+" INTEGER," +
            " "+KEY_NB+" INTEGER," +
            " "+KEY_URI_ORIGINE+" TEXT," +
            " "+KEY_URI_FINALE+" TEXT," +
            " "+KEY_DESCRIPTION+" TEXT," +
            " "+KEY_NAME+" TEXT," +
            " "+KEY_PRIX+"  FLOAT(10,4)" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public PhotoModifieeManager(Context context)
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

    public long addPhotoModifiee(PhotoModifiee pm) {
        // Ajout d'un enregistrement dans la table

        ContentValues values = new ContentValues();
        if(pm.getPhotoId() != 0){
            values.put(KEY_ID, pm.getPhotoId());
        }
        values.put(KEY_FORMAT, pm.getIdFormat());
        values.put(KEY_MASQUE, pm.getMaskId());
        values.put(KEY_NB, pm.getNbPhotos());
        values.put(KEY_URI_ORIGINE, pm.getUriOrigine());
        values.put(KEY_URI_FINALE, pm.getUriFinale());
        values.put(KEY_DESCRIPTION, pm.getDescription());
        values.put(KEY_NAME, pm.getName());
        values.put(KEY_PRIX, pm.getPrix());
        values.put(KEY_ID_PANIER, pm.getIdPanier());
        values.put(KEY_ID_USER, pm.getIdUser());
        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int modPhotoModifiee(PhotoModifiee pm) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        ContentValues values = new ContentValues();
        values.put(KEY_FORMAT, pm.getIdFormat());
        values.put(KEY_MASQUE, pm.getMaskId());
        values.put(KEY_NB, pm.getNbPhotos());
        values.put(KEY_URI_ORIGINE, pm.getUriOrigine());
        values.put(KEY_URI_FINALE, pm.getUriFinale());
        values.put(KEY_DESCRIPTION, pm.getDescription());
        values.put(KEY_NAME, pm.getName());
        values.put(KEY_PRIX, pm.getPrix());
        values.put(KEY_ID_PANIER, pm.getIdPanier());
        values.put(KEY_ID_USER, pm.getIdUser());

        String where = KEY_ID+" = ?";
        String[] whereArgs = {pm.getPhotoId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supPhotoModifiee(int id) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = KEY_ID+" = ?";
        String[] whereArgs = {id+""};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public int supAllPhotoModifiee() {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = "";
        String[] whereArgs = {};

        return db.delete(TABLE_NAME, where, whereArgs);
    }

    public PhotoModifiee getPhotoModifiee(int id) {

        PhotoModifiee s = new PhotoModifiee();

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID+"="+id, null);
        if (c.moveToFirst()) {
            s.setPhotoId(c.getInt(c.getColumnIndex(KEY_ID)));
            s.setIdPanier(c.getInt(c.getColumnIndex(KEY_ID_PANIER)));
            s.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            s.setIdFormat(c.getInt(c.getColumnIndex(KEY_FORMAT)));
            s.setNbPhotos(c.getInt(c.getColumnIndex(KEY_NB)));
            s.setPrix(c.getFloat(c.getColumnIndex(KEY_PRIX)));
            s.setMaskId(c.getInt(c.getColumnIndex(KEY_MASQUE)));
            s.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            s.setUriFinale(c.getString(c.getColumnIndex(KEY_URI_FINALE)));
            s.setUriOrigine(c.getString(c.getColumnIndex(KEY_URI_ORIGINE)));
            c.close();
        }

        return s;
    }

    public Cursor getPhotosModifiees(int idUser) {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_USER +"= "+idUser, null);
    }

}