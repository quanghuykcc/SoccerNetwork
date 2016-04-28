package kcc.soccernetwork.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

public class BitmapUri {
	public static Uri getLocalBitmapUri(ImageView imageView, Context context) {
		
	    Drawable drawable = imageView.getDrawable();
	    Bitmap bmp = null;
		Bitmap thumbail = null;
	    if (drawable instanceof BitmapDrawable){
	       	bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
			thumbail = Bitmap.createScaledBitmap(bmp, 300, 300, false);
	    } else {
	       return null;
	    }
	    // Store image to default external storage directory
	    Uri bmpUri = null;
	    try {
	    	File cacheDir = context.getCacheDir();
	        File file =  new File(cacheDir, System.currentTimeMillis() + ".png");
	        file.getParentFile().mkdirs();
	        FileOutputStream out = new FileOutputStream(file);
			thumbail.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.close();
	        bmpUri = Uri.fromFile(file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return bmpUri;
	}
}
