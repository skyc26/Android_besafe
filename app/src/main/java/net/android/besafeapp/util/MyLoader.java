package net.android.besafeapp.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

public class MyLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
      //  Picasso.with(context).load((String) path).into((ImageView) imageView);
        Picasso.with(context).load((Integer) path).into((ImageView) imageView);
    }
}
