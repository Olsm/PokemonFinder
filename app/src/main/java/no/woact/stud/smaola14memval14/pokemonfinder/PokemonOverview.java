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
        new AsyncTask<Activity, Void, Object[]>(){
            @Override
            protected Object[] doInBackground(Activity... params) {
                boolean success = false;

                for(Pokemon pokemon : db.getPokemonsFromDb()){
                    if(!itemname.contains(pokemon.getName())){
                        try{
                            imgid.add(generateImage(pokemon.getImage()));
                        }
                        catch (IOException i){
                            return new Object[] {new CustomListAdapter(params[0], itemname, imgid), success};
                        }
                        itemname.add(pokemon.getName());
                    }

                }
                success = true;

                return new Object[]{new CustomListAdapter(params[0], itemname, imgid ), success};

            }


            @Override
            protected void onPostExecute(Object[] result) {
                super.onPostExecute(result);
                boolean success = (boolean) result[1];
                if(success){
                    listViewPokemons = (ListView) findViewById(R.id.listPokemons);
                    listViewPokemons.setAdapter((CustomListAdapter)result[0]);
                }
                else
                    utils.messageBox(getString(R.string.connect_error), getString(R.string.internet_check_reminder));

            }
        }.execute(this);
    }


    public Bitmap generateImage(String imageUrl) throws IOException{
        Bitmap b  = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
        return Bitmap.createScaledBitmap(b, 150,150, true);


    }
}
