package no.woact.stud.smaola14memval14.pokemonfinder;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Valmir on 03.06.2016.
 */
public class Utils {
    private Context context;

    public Utils(Context context){
        this.context = context;
    }

    public void messageBox(String method, String message)
    {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this.context);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
