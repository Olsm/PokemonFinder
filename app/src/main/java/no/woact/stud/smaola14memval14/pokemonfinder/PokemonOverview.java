package no.woact.stud.smaola14memval14.pokemonfinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PokemonOverview extends AppCompatActivity {
    DbHandler db;
    ListView listViewPokemons;
    ArrayList<Bitmap> imgid;
    ArrayList<String> itemname;
    Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_overview);
        utils = new Utils(this);
        db = new DbHandler(this);
        imgid = new ArrayList<>();
        itemname = new ArrayList<>();
        showPokemonListView();

    }

    public void showPokemonListView(){
        new AsyncTask<Activity, Void, CustomListAdapter>(){
            @Override
            protected CustomListAdapter doInBackground(Activity... params) {

                for(Pokemon pokemon : db.getPokemonsFromDb()){
                    if(!itemname.contains(pokemon.getName())){
                        imgid.add(generateImage(pokemon.getImage()));
                        itemname.add(pokemon.getName());
                    }

                }
                return new CustomListAdapter(params[0], itemname, imgid );

            }


            @Override
            protected void onPostExecute(CustomListAdapter cAdapter) {
                super.onPostExecute(cAdapter);
                listViewPokemons = (ListView) findViewById(R.id.listPokemons);
                listViewPokemons.setAdapter(cAdapter);
            }
        }.execute(this);
    }


    public Bitmap generateImage(String imageUrl){
        try {
            Bitmap b  = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
            return b.createScaledBitmap(b, 150,150, true);
        } catch (MalformedURLException m) {
            utils.messageBox("URL failure", "Failed to parse URL.");
        } catch (IOException e) {
            utils.messageBox("Image failure", "Failed getting image. Check connection");
        }
        return null;

    }
}
