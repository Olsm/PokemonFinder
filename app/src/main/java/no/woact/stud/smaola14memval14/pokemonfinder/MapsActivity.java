package no.woact.stud.smaola14memval14.pokemonfinder;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHandler = new DbHandler(this);

        downloadAndDisplayData();
    }

    private void downloadAndDisplayData() {
        new AsyncTask<Void, Void, ArrayList<Pokemon>>() {
            @Override
            protected ArrayList<Pokemon> doInBackground(final Void... params) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    connection.connect();
                    int statusCode = connection.getResponseCode();

                    switch (statusCode) {
                        case 200:
                        case 201:
                            String jsonString = connectionInputToString(connection);
                            return jsonArrayToPokemonList(new JSONArray(jsonString));
                    }

                } catch (IOException | JSONException e) {
                    messageBox("doInBackground", "Could not search for pokemons. Please make sure you have internet and restart the ap");
                }
                return null;
            }

            @Override
            protected void onPostExecute(final ArrayList<Pokemon> pokemonList) {
                super.onPostExecute(pokemonList);
                updatePokemonMapData(pokemonList);
            }
        }.execute();
    }

    private String connectionInputToString(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        return sb.toString();
    }

    public ArrayList<Pokemon> jsonArrayToPokemonList(JSONArray jsonArray) throws JSONException {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject pokemon = jsonArray.getJSONObject(i);
            String id = pokemon.getString("_id");
            String name = pokemon.getString("name");
            String hint = pokemon.getString("hint");
            LatLng lat = new LatLng(pokemon.getDouble("lat"), pokemon.getDouble("lng"));
            pokemonList.add(new Pokemon(id, name, hint, lat));
        }

        return pokemonList;
    }

    public void updatePokemonMapData(ArrayList<Pokemon> pokemonList) {
        for (Pokemon pokemon : pokemonList) {
            float color = BitmapDescriptorFactory.HUE_RED;
            if (dbHandler.pokemonInDb(pokemon.getId())) color = BitmapDescriptorFactory.HUE_GREEN;
            mMap.addMarker(new MarkerOptions().position(pokemon.getLocation())
                    .title(pokemon.getName()).snippet(pokemon.getHint())
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
        }
        Pokemon pLast = pokemonList.get(pokemonList.size()-1);
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(pLast.getLocation(), 12.0f));
    }

    public Pokemon findPokemon(String pokemonId) {
        Pokemon pokemon = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/pokemon/" + pokemonId).openConnection();
            connection.setRequestProperty("X-Token", "token");
            connection.connect();
            int statusCode = connection.getResponseCode();

            switch (statusCode) {
                case 200:
                case 201:
                    String jsonString = connectionInputToString(connection);
                    ArrayList<Pokemon> pokemonList = jsonArrayToPokemonList(new JSONArray(jsonString));
                    if (pokemonList.size() == 1) {
                        pokemon = pokemonList.get(0);
                        dbHandler.addPokemon(pokemon);
                    }
            }

        } catch (IOException | JSONException e) {
            messageBox("findPokemon", "Could not search for pokemon. Please make sure you have internet and restart the ap");
        }
        return pokemon;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Code below from http://stackoverflow.com/a/18143773
    //*********************************************************
    //generic dialog, takes in the method name and error message
    //*********************************************************
    private void messageBox(String method, String message)
    {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
