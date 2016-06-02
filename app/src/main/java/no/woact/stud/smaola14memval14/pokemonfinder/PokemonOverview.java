package no.woact.stud.smaola14memval14.pokemonfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        /*imgid.add(generateImage("http://vignette4.wikia.nocookie.net/pokemon/images/5/5f/025Pikachu_OS_anime_11.png/revision/latest?cb=20150717063951"));
        itemname.add("JAMIACABOY");

        CustomListAdapter cAdapter = new CustomListAdapter(this, itemname, imgid );
        listViewPokemons = (ListView) findViewById(R.id.listPokemons);
        listViewPokemons.setAdapter(cAdapter);*/




        imgid = new ArrayList<>();
        itemname = new ArrayList<>();
        db = new DbHandler(this);
        listPokemons = new ArrayList<>();
        for(Pokemon pokemon : db.getPokemonsFromDb()){
            listPokemons.add("Pokemon: " + pokemon.getName() + pokemon.getImage());
            imgid.add(generateImage(pokemon.getImage()));
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
            m.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
