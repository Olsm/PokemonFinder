package no.woact.stud.smaola14memval14.pokemonfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Valmir on 31.05.2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Pokemons.db";
    public static final String TABLE_NAME = "pokemon_table";

    public static final String KEY_ID = "ID";
    public static final String KEY_POKEMON_ID = "POKEMON_ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_IMAGE = "IMAGE";
    public static final String KEY_CAPTURED = "CAPTURED";

    private SQLiteDatabase db;


    public DbHandler(Context context){
        super(context, DATABASE_NAME, null, 1);
        db =  this.getWritableDatabase();
    }
    

    public ArrayList<Pokemon> getPokemonsFromDb(){
        ArrayList<Pokemon> resultPokemons = new ArrayList<>();
        String query = ("SELECT * FROM " + TABLE_NAME);

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Pokemon result = new Pokemon(cursor.getString(1),cursor.getString(2),"" ,cursor.getString(3) ,null);
                resultPokemons.add(result);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return resultPokemons;

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE table " + TABLE_NAME + " (" +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_POKEMON_ID+ " TEXT, " + KEY_NAME + " TEXT, " + KEY_IMAGE + " TEXT, " +
                KEY_CAPTURED + " INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Inserts results to table
    public void addPokemon(Pokemon pokemon){
        ContentValues values = new ContentValues();
        values.put(KEY_POKEMON_ID, pokemon.getId());
        values.put(KEY_NAME, pokemon.getName());
        values.put(KEY_IMAGE, pokemon.getImage());
        values.put(KEY_CAPTURED, pokemon.getCaptured());
        db.insert(TABLE_NAME, null, values);
    }



    public boolean pokemonInDb(String pokemonId) {
        String[] args={pokemonId};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+KEY_POKEMON_ID + " = ?", args);
        return(cursor.getCount() == 1);
    }

}
