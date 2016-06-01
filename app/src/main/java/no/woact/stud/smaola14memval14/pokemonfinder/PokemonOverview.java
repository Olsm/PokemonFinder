package no.woact.stud.smaola14memval14.pokemonfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PokemonOverview extends AppCompatActivity {
    DbHandler db;
    ListView listViewPokemons;
    ListAdapter adapter;
    ArrayList<String> listPokemons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_overview);
        db = new DbHandler(this);
        listPokemons = new ArrayList<>();
        for(Pokemon pokemon : db.getPokemonsFromDb()){
            listPokemons.add("Pokemon: " + pokemon.getName());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listPokemons);
        listViewPokemons = (ListView) findViewById(R.id.listPokemons);
        listViewPokemons.setAdapter(adapter);

    }
}
