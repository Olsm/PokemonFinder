package no.woact.stud.smaola14memval14.pokemonfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbHandler dbHandler;
    ArrayList<Marker> markerList;
    ArrayList<String> pokemonIdList;
    Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // TODO: Remove this!!!
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHandler = new DbHandler(this);
        utils = new Utils(this);
        markerList = new ArrayList<>();
        pokemonIdList = new ArrayList<>();

        // TODO: Remove before release!
        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 0, 0);

        fixSSLIssue();

        downloadAndDisplayPokemons();
    }

    public void switchToOverview(View v){
        Intent intent = new Intent(this, PokemonOverview.class);
        startActivity(intent);
    }


    private void downloadAndDisplayPokemons() {
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
                            ArrayList<Pokemon> pokemonList = jsonArrayToPokemonList(new JSONArray(jsonString));
                            pokemonList.add(new Pokemon("57348c569295781100ae8906", "Pikachu", "Such Test", "", new LatLng(59.91183658, 10.76162338)));
                            return pokemonList;
                    }

                } catch (IOException | JSONException e) {
                    messageFail(getString(R.string.pokemon_search_error) + "." + getString(R.string.internet_check_reminder));
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

    public void catchPokemonDialog(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.catch_pokemon_title));

        final EditText pokemonInput = new EditText(this);

        pokemonInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(pokemonInput);

        builder.setPositiveButton("Find", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pokemonId;

                pokemonId = pokemonInput.getText().toString();
                findPokemon(pokemonId);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private String connectionInputToString(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();

        String string = sb.toString();
        if (string.charAt(0) != '[') {
            string = "[" + string + "]";
        }

        return string;
    }

    public ArrayList<Pokemon> jsonArrayToPokemonList(JSONArray jsonArray) throws JSONException {
        ArrayList<Pokemon> pokemonList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject pokemon = jsonArray.getJSONObject(i);
            String id = pokemon.getString("_id");
            String name = pokemon.getString("name");
            String hint = "", image = "";
            LatLng lat = null;
            if (pokemon.has("hint")) hint = pokemon.getString("hint");
            if (pokemon.has("imageUrl")) image = pokemon.getString("imageUrl");
            if (pokemon.has("lat")) lat = new LatLng(pokemon.getDouble("lat"), pokemon.getDouble("lng"));
            pokemonList.add(new Pokemon(id, name, hint, image, lat));
        }

        return pokemonList;
    }

    public void updatePokemonMapData(ArrayList<Pokemon> pokemonList) {
        float green = BitmapDescriptorFactory.HUE_GREEN;
        float red = BitmapDescriptorFactory.HUE_RED;

        for (int i = 0; i < pokemonList.size(); i++) {
            Pokemon pokemon = pokemonList.get(i);

            if (pokemonIdList.contains(pokemon.getId())) {
                Marker marker = markerList.get(pokemonIdList.indexOf(pokemon.getId()));
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(green));
            }

            else if (pokemon.getLocation() != null) {
                float color = red;
                if (dbHandler.pokemonInDb(pokemon.getId())) color = green;

                Marker marker = mMap.addMarker(new MarkerOptions().position(pokemon.getLocation())
                        .title(pokemon.getName()).snippet(pokemon.getHint())
                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
                markerList.add(marker);
                pokemonIdList.add(pokemon.getId());

                mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(pokemon.getLocation(), 12.0f));
            }
        }
    }

    public void findPokemon(final String pokemonId) {
        new AsyncTask<Void, Void, ArrayList<Pokemon>>() {
            @Override
            protected ArrayList<Pokemon> doInBackground(final Void... params) {
                boolean problemOccurred = false;
                HttpsURLConnection connection = null;
                ArrayList<Pokemon> pokemonList = null;

                try {
                    connection = (HttpsURLConnection) new URL("https://locations.lehmann.tech/pokemon/" + pokemonId).openConnection();
                    connection.setRequestProperty(getString(R.string.x_token_key), getString(R.string.x_token));
                } catch (IOException e) {
                    problemOccurred = true;
                }

                try {
                    String jsonString = connectionInputToString(connection);
                    pokemonList = jsonArrayToPokemonList(new JSONArray(jsonString));
                    if (!pokemonList.isEmpty()) {
                        Pokemon pokemon = pokemonList.get(0);
                        if (!dbHandler.getPokemonsFromDb().contains(pokemon)) {
                            dbHandler.addPokemon(pokemon);
                            return pokemonList;
                        }
                    }

                } catch (IOException | JSONException e) {
                    int statusCode = 0;
                    if (connection != null) {
                        try {statusCode = connection.getResponseCode();}
                        catch (IOException e1) {statusCode = 0;}
                    }
                    if (statusCode != 420) problemOccurred = true;
                }

                if (problemOccurred)
                    messageFail(getString(R.string.pokemon_search_error) + "." + getString(R.string.internet_check_reminder));

                return pokemonList;
            }

            @Override
            protected void onPostExecute(final ArrayList<Pokemon> pokemonList) {
                super.onPostExecute(pokemonList);
                if(pokemonList != null) {
                    updatePokemonMapData(pokemonList);
                    messageSuccess(getString(R.string.pokemon_message_status_success) + " " + pokemonList.get(0).getName());
                }
                else
                    messageFail(getString(R.string.pokemon_message_status_fail));
            }

        }.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Fix SSL exception, from http://stackoverflow.com/a/24501156
    private void fixSSLIssue() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            messageFail(getString(R.string.connect_error) + "." + getString(R.string.internet_check_reminder));
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {return true;}
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    private void messageFail(String message) {
        messageBox(message, false);
    }

    private void messageSuccess(String message) {
        messageBox(message, true);
    }

    private void messageBox(String message, boolean success) {
        String title = getString(R.string.title_status_fail);
        if (success) title = getString(R.string.title_status_success);
        utils.messageBox(title, message);
    }
}
