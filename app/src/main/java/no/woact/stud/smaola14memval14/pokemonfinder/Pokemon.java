package no.woact.stud.smaola14memval14.pokemonfinder;

import com.google.android.gms.maps.model.LatLng;

public class Pokemon {
    private String id;
    private String name;
    private String hint;
    private String image;
    private LatLng location;

    //Constructor
    public Pokemon(String id, String name, String hint, String image,  LatLng location){
        this.id = id;
        this.name = name;
        this.hint = hint;
        this.image = image;
        this.location = location;
    }

    //Get methods

    public String getId(){
        return this.id;
    }

    public String getImage() {
        return this.image;
    }
    public String getName(){
        return this.name;
    }

    public String getHint(){
        return this.hint;
    }

    public LatLng getLocation(){
        return this.location;
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hint='" + hint + '\'' +
                ", location=" + location +
                " image="   + image + '}';
    }

    @Override
    public boolean equals(Object other) {
        return this.toString().equals(other.toString());
    }
}
