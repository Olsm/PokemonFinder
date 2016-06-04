package no.woact.stud.smaola14memval14.pokemonfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemName;
    private final ArrayList<Bitmap> image;

    public CustomListAdapter(Activity context, ArrayList<String> itemName, ArrayList<Bitmap> image) {
        super(context, R.layout.mylist, itemName);
        this.context=context;
        this.itemName = itemName;
        this.image = image;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist,null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemName.get(position));
        imageView.setImageBitmap(image.get(position));
        return rowView;

    }
}
