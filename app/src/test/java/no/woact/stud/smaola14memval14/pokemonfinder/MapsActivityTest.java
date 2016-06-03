package no.woact.stud.smaola14memval14.pokemonfinder;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MapsActivityTest {
    MapsActivity mapsActivity;

    @Before
    public void setUp() {
        mapsActivity = new MapsActivity();
    }

    @Test
    public void testJsonArrayToPokemonList() throws Exception {
        String jsonString = "[{\"_id\":\"5735a3376d4c35dab3946e06\",\"name\":\"Charmander\",\"lat\":59.9116586,\"lng\":10.7596282,\"hint\":\"Watch out for flambeed dishes\"},{\"_id\":\"5735a3dc6d4c35dab3946e07\",\"name\":\"Mew\",\"lat\":59.9190987,\"lng\":10.7395646,\"hint\":\"No trespassing! But maybe just a little\"}]";
        JSONArray jsonArray = new JSONArray(jsonString);

        ArrayList<Pokemon> pokemonList = mapsActivity.jsonArrayToPokemonList(jsonArray);
        Pokemon pokemon1 = pokemonList.get(0);
        Pokemon pokemon2 = pokemonList.get(1);

        assertEquals(2, pokemonList.size());
        assertEquals(new Pokemon("5735a3376d4c35dab3946e06", "Charmander", "Watch out for flambeed dishes", "", new LatLng(59.9116586,10.7596282)), pokemon1);
        assertEquals(new Pokemon("5735a3dc6d4c35dab3946e07", "Mew", "No trespassing! But maybe just a little", "", new LatLng(59.9190987,10.7395646)), pokemon2);
    }
}
