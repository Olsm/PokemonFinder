package no.woact.stud.smaola14memval14.pokemonfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.fitness.request.ListClaimedBleDevicesRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valmir on 31.05.2016.
 */
public class DbHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Pokemons.db";
    public static final String TABLE_NAME = "pokemon_table";

    public static final String KEY_ID = "ID";
    public static final String KEY_POKEMON_ID = "POKEMON_ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_HINT = "HINT";
    public static final String KEY_CAPTURED = "CAPTURED";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";




    public DbHandler(Context context){
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE table " + TABLE_NAME + " (" +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_POKEMON_ID+ " TEXT, " + KEY_NAME + " TEXT, " + KEY_HINT + " TEXT, " +
                KEY_CAPTURED + " INTEGER, " + KEY_LATITUDE + " REAL, " + KEY_LONGITUDE + " REAL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Inserts results to table
    public void addResult(String pokemonId, String name, String hint,
                          LatLng location, boolean captured){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POKEMON_ID, pokemonId);
        values.put(KEY_NAME, name);
        values.put(KEY_HINT, hint);
        values.put(KEY_LATITUDE, location.latitude);
        values.put(KEY_LONGITUDE, location.longitude);
        values.put(KEY_CAPTURED, captured);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Pokemon> getPokemonsFromDb(){
        List<Pokemon> resultPokemons = new ArrayList<>();
        String query = ("SELECT * FROM " + TABLE_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Pokemon result = new Pokemon(cursor.getString(1),cursor.getString(2), cursor.getString(3), new LatLng(cursor.getDouble(5), cursor.getDouble(6)));
                resultPokemons.add(result);
            }while (cursor.moveToNext());
        }
        
        return resultPokemons;

    }

}
