package com.jeremy.estiam.appliandroid.models;



        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;

public class PhotosBDD {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "photos.db";

    private static final String TABLE_PHOTOS= "table_photos";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_PATH = "PATH";
    private static final int NUM_COL_PATH = 2;
    private static final String COL_URI = "URI";
    private static final int NUM_COL_URI = 3;

    private SQLiteDatabase bdd;

    private PhotoSQLite photoSQLite;

    public PhotosBDD(Context context){
        //On crée la BDD et sa table
        photoSQLite = new PhotoSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = photoSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertLivre(Photo photo){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NAME, photo.getName());
        values.put(COL_PATH, photo.getPath());
        values.put(COL_URI, photo.getUri().getPath());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_PHOTOS, null, values);
    }

    public int updateLivre(int id, Photo photo){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simplement préciser quel livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NAME, photo.getName());
        values.put(COL_PATH, photo.getPath());
        values.put(COL_URI, photo.getUri().getPath());
        return bdd.update(TABLE_PHOTOS, values, COL_ID + " = " +id, null);
    }

    public int removeLivreWithID(int id){
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_PHOTOS, COL_ID + " = " +id, null);
    }

    public Photo getPhotoWithPath(String path){
        //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_PHOTOS, new String[] {COL_ID, COL_NAME, COL_PATH, COL_URI}, COL_PATH + " LIKE \"" + path +"\"", null, null, null, null);
        return cursorToPhoto(c);
    }

    //Cette méthode permet de convertir un cursor en une photo
    private Photo cursorToPhoto(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Photo photo = new Photo();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        photo.setId(c.getInt(NUM_COL_ID));
        photo.setName(c.getString(NUM_COL_NAME));
        photo.setPath(c.getString(NUM_COL_PATH));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return photo;
    }
}