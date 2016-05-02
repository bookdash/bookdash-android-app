package org.bookdash.android.presentation.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;


public class BindingUtils {

    @BindingAdapter({"bind:imageUrlWeb"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter("bind:specialTag")
    public static void setSpecialTag(View view, Object value) {
        view.setTag(value);
    }

    @BindingAdapter("bind:contentScrim")
    public static void setContentScrim(View view, Object value) {
        view.setTag(value);
    }

    @BindingAdapter({"bind:parseImageFile"})
    public static void loadImageFromParse(final ImageView view, Object parseFile) {
        if (parseFile == null) {
            return;
        }
        ParseFile parseFileCast = (ParseFile) parseFile;

        parseFileCast.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    Log.d("BindingUtils", "We've got data in data.");

                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    view.setImageBitmap(bmp);

                } else {
                    Log.d("BindingUtils", "There was a problem downloading the data.");
                }
            }
        });
    }
}
