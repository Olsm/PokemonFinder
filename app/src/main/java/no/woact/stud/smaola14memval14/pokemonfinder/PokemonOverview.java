package no.woact.stud.smaola14memval14.pokemonfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PokemonOverview extends AppCompatActivity {
    DbHandler db;
    ListView listViewPokemons;
    ListAdapter adapter;
    ArrayList<String> listPokemons;
    ArrayList<Bitmap> imgid;
    ArrayList<String> itemname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_overview);
        db = new DbHandler(this);

        collectImages();

    }

    public void collectImages(){
        new AsyncTask<Void, Void, ArrayList<Bitmap>>(){
            @Override
            protected ArrayList<Bitmap> doInBackground(Void... params) {
                ArrayList<Bitmap> imageCollection = new ArrayList<>();
                for(Pokemon pokemon : db.getPokemonsFromDb()){
                    imageCollection.add(generateImage(pokemon.getImage()));
                }
                return imageCollection;
            }

            @Override
            protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
                super.onPostExecute(bitmaps);
                showPokemonList(bitmaps);
            }
        }.execute();
    }

    public void showPokemonList(ArrayList<Bitmap> bitmaps){
        imgid = new ArrayList<>();
        imgid = bitmaps;
        itemname = new ArrayList<>();
        listPokemons = new ArrayList<>();
        for(Pokemon pokemon : db.getPokemonsFromDb()){
            listPokemons.add("Pokemon: " + pokemon.getName() + pokemon.getImage());
            itemname.add(pokemon.getName());
        }
        CustomListAdapter cAdapter = new CustomListAdapter(this, itemname, imgid );
        listViewPokemons = (ListView) findViewById(R.id.listPokemons);
        listViewPokemons.setAdapter(cAdapter);
    }

    public Bitmap generateImage(String imageUrl){
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
        } catch (MalformedURLException m) {
            new Utils(this).messageBox("URL failure", "Failed to parse URL.");
        } catch (IOException e) {
            new Utils(this).messageBox("Image failure", "Failed getting image. Check connection");
        }
        return null;

    }
}
