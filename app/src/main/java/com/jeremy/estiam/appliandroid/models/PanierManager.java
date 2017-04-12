package com.jeremy.estiam.appliandroid.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PanierManager {

    private static final String TABLE_NAME = "panier";
    public static final String KEY_ID_PANIER="id_panier";
    public static final String KEY_ID_LIVRAISON="id_livraison";
    public static final String KEY_ID_USER="id_user";
    public static final String KEY_ID_ADRESSE="id_adresse";
    public static final String KEY_STATUS="status";
    public static final String KEY_NB_PHOTOS="nb_photos";
    public static final String KEY_PRIX_HT="prix_ht";
    public static final String KEY_PRIX_TTC="prix_ttc";
    public static final String KEY_PRIX_FDP="prix_fdp";
    public static final String KEY_PRIX_TOTAL="prix_total";
    public static final String KEY_FACTURATION_NOM="facturation_nom";
    public static final String KEY_FACTURATION_PRENOM="facturation_prenom";
    public static final String KEY_FACTURATION_CP="facturation_cp";
    public static final String KEY_FACTURATION_VILLE="facturation_ville";
    public static final String KEY_FACTURATION_RUE="facturation_rue";
    public static final String CREATE_TABLE_PANIER = "CREATE TABLE "+TABLE_NAME+
            " (" +
            " "+KEY_ID_PANIER+" INTEGER primary key," +
            " "+KEY_ID_LIVRAISON+" INTEGER," +
            " "+KEY_ID_USER+" INTEGER," +
            " "+KEY_ID_ADRESSE+" INTEGER," +
            " "+KEY_NB_PHOTOS+" INTEGER," +
            " "+KEY_FACTURATION_NOM+" VARCHAR(50)," +
            " "+KEY_STATUS+" VARCHAR(50)," +
            " "+KEY_FACTURATION_PRENOM+" VARCHAR(50)," +
            " "+KEY_FACTURATION_CP+" VARCHAR(50)," +
            " "+KEY_FACTURATION_VILLE+" VARCHAR(50)," +
            " "+KEY_FACTURATION_RUE+" VARCHAR(50)," +
            " "+KEY_PRIX_HT+" FLOAT(10,4)," +
            " "+KEY_PRIX_TTC+" FLOAT(10,4)," +
            " "+KEY_PRIX_FDP+" FLOAT(10,4)," +
            " "+KEY_PRIX_TOTAL+" FLOAT(10,4)" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public PanierManager(Context context)
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

    public long addPanier(int userId){
        ContentValues values = new ContentValues();
        values.put(KEY_PRIX_TTC, 0);
        values.put(KEY_PRIX_TOTAL, 0);
        values.put(KEY_ID_ADRESSE, 0);
        values.put(KEY_ID_USER, userId);
        values.put(KEY_ID_LIVRAISON, 0);
        values.put(KEY_STATUS, 0);
        values.put(KEY_NB_PHOTOS, 0);
        values.put(KEY_PRIX_HT, 0);
        values.put(KEY_PRIX_FDP,0);
        values.put(KEY_FACTURATION_CP,0);
        values.put(KEY_FACTURATION_VILLE,0);
        values.put(KEY_FACTURATION_RUE,0);
        values.put(KEY_FACTURATION_NOM,0);
        values.put(KEY_FACTURATION_PRENOM,0);
        return db.insert(TABLE_NAME,null,values);

    }

    public int modPanier(Panier panier) {
        // modification d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la requête

        Panier p = getPanier(panier.getUserId());
        panier.setId(p.getId());

        ContentValues values = new ContentValues();
        values.put(KEY_NB_PHOTOS, panier.getNbPhotos());
        values.put(KEY_ID_LIVRAISON, panier.getShippingMethodId());
        values.put(KEY_PRIX_FDP, panier.getFdp());
        values.put(KEY_PRIX_HT, panier.getTotalPriceHT());
        values.put(KEY_PRIX_TOTAL, panier.getPrixTotal());
        values.put(KEY_PRIX_TTC, panier.getPrixTTC());

        values.put(KEY_ID_ADRESSE, panier.getBillingAdressId());
        values.put(KEY_STATUS, panier.getStatus());

        values.put(KEY_FACTURATION_CP,panier.getCpFacturation());
        values.put(KEY_FACTURATION_VILLE,panier.getVilleFacturation());
        values.put(KEY_FACTURATION_RUE,panier.getRueFacturation());
        values.put(KEY_FACTURATION_NOM,panier.getNomFacturation());
        values.put(KEY_FACTURATION_PRENOM,panier.getPrenomFacturation());

        String where = KEY_ID_PANIER+" = ? AND "+ KEY_ID_USER + "=?";
        String[] whereArgs = {panier.getId()+"", panier.getUserId()+""};

        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    public int supPanier(int idUser) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon

        String where = KEY_ID_PANIER+" = ? AND "+ KEY_ID_USER + "=?";
        String[] whereArgs = {getPanier(idUser).getId()+"", getPanier(idUser).getUserId()+""};


        return db.delete(TABLE_NAME, where, whereArgs);
    }


    public Panier getPanier(int idUser) {
        // sélection de tous les enregistrements de la table

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME + " WHERE "+KEY_ID_USER+ "="+idUser, null);
        Panier p = new Panier();
        if (c.moveToFirst()) {
            p.setId(c.getInt(c.getColumnIndex(KEY_ID_PANIER)));
            p.setShippingMethodId(c.getInt(c.getColumnIndex(KEY_ID_LIVRAISON)));
            p.setUserId(c.getInt(c.getColumnIndex(KEY_ID_USER)));
            p.setNbPhotos(c.getInt(c.getColumnIndex(KEY_NB_PHOTOS)));
            p.setFdp(c.getFloat(c.getColumnIndex(KEY_PRIX_FDP)));
            p.setTotalPriceHT(c.getFloat(c.getColumnIndex(KEY_PRIX_HT)));
            p.setPrixTTC(c.getFloat(c.getColumnIndex(KEY_PRIX_TTC)));
            p.setNomFacturation(c.getString(c.getColumnIndex(KEY_FACTURATION_NOM)));
            p.setPrenomFacturation(c.getString(c.getColumnIndex(KEY_FACTURATION_PRENOM)));
            p.setVilleFacturation(c.getString(c.getColumnIndex(KEY_FACTURATION_VILLE)));
            p.setRueFacturation(c.getString(c.getColumnIndex(KEY_FACTURATION_RUE)));
            p.setCpFacturation(c.getString(c.getColumnIndex(KEY_FACTURATION_CP)));
            p.setBillingAdressId(c.getInt(c.getColumnIndex(KEY_ID_ADRESSE)));
            p.setStatus(c.getString(c.getColumnIndex(KEY_STATUS)));

            c.close();
            return p;
        }

        return null;
    }


}