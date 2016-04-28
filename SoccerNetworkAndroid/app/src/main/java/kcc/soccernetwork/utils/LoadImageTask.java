package kcc.soccernetwork.utils;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImageTask extends AsyncTask<String, Void, Void> {
    private ImageView imageView;
    private int defaultImageID;
    private Context context;
    public LoadImageTask(Context context, ImageView imageView, int defaultImageID) {
        this.context = context;
        this.imageView = imageView;
        this.defaultImageID = defaultImageID;
    }
    @Override
    protected Void doInBackground(String... params) {
        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.DisplayImage(params[0], defaultImageID, imageView);
        return null;
    }


}