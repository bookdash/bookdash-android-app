package org.bookdash.android.presentation.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/08/05 7:30 PM
 */
public class GlideLoadingModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
      //  glide.register(Model.class, Data.class, new MyModelLoader());
    }


}
