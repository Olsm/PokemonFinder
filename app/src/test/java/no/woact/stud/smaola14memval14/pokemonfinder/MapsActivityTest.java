package no.woact.stud.smaola14memval14.pokemonfinder;

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
        assertEquals("Pokemon{id='5735a3376d4c35dab3946e06', name='Charmander', hint='Watch out for flambeed dishes', location=lat/lng: (59.9116586,10.7596282), captured=false}", pokemon1.toString());
        assertEquals("Pokemon{id='5735a3dc6d4c35dab3946e07', name='Mew', hint='No trespassing! But maybe just a little', location=lat/lng: (59.9190987,10.7395646), captured=false}", pokemon2.toString());
    }
}
