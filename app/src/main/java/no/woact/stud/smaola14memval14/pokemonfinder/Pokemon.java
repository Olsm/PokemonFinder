package no.woact.stud.smaola14memval14.pokemonfinder;

public class Pokemon {
    private String id;
    private String name;
    private String hint;
    private double longitude;
    private double latitude;
    private boolean captured;

    //Constructor
    public Pokemon(String id, String name, String hint, double longitude, double latitude){
        this.id = id;
        this.name = name;
        this.hint = hint;
        this.longitude = longitude;
        this.latitude = latitude;
        this.captured = false;
    }

    //Get methods

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public String getHint(){
        return this.hint;
    }

    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }

    public boolean getCaptured(){
        return this.captured;
    }

    public void setCaptured(boolean captured){
        this.captured = captured;
    }



}
