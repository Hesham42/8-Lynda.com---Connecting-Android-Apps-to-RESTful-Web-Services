package com.example.guinness.flowerwithgui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guinness.flowerwithgui.Model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.example.guinness.flowerwithgui.ParsingBYJASON.PHOTOS_BASE_URL;

/**
 * Created by guinness on 25/11/16.
 */

public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context context;

    private List<Flower> flowerList;

    public FlowerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.itemtool, parent, false);


// Display Flower name in the textView widget

        Flower flower = flowerList.get(position);

        TextView tv = (TextView) view.findViewById(R.id.texItemTool);
        tv.setText(flower.getName());
//Display now the image BY Bitmap
        if (flower.getBitmap() != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
            imageView.setImageBitmap(flower.getBitmap());

        } else {
            FlowerAndView Conatiner = new FlowerAndView();
            Conatiner.flower=flower;
            Conatiner.view=view;

            ImageLoader loader =new ImageLoader();
            loader.execute(Conatiner);

        }
        return view;
    }


}

class FlowerAndView {
    public Flower flower;
    public View view;
    public Bitmap bitmap;

}

class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

    @Override
    protected FlowerAndView doInBackground(FlowerAndView... flowerAndViews) {
        FlowerAndView container = flowerAndViews[0];
        Flower flower = container.flower;
        try {
            String imageUrl = PHOTOS_BASE_URL + flower.getPhoto();
            InputStream in = (InputStream) new URL(imageUrl).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            flower.setBitmap(bitmap);
            in.close();
            container.bitmap = bitmap;
            return container;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    @Override
    protected void onPostExecute(FlowerAndView flowerAndView) {

        ImageView imageView = (ImageView) flowerAndView.view.findViewById(R.id.imageView1);
        imageView.setImageBitmap(flowerAndView.bitmap);
        flowerAndView.flower.setBitmap(flowerAndView.bitmap);


    }
}



