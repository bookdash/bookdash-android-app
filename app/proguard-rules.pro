# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/rebecca/android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings

-keep class me.zhanghai.android.materialprogressbar.** { *; }
-keep class com.joanzapata.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

-keepattributes SourceFile,LineNumberTable

-keepattributes Signature
-keepattributes InnerClass
-keep class com.squareup.okhttp.** {*;}
-keep class za.co.riggaroo.materialhelptutorial.view.** { *;}
-keep class mbanje.kurt.fabbutton.** {*;}
-keep class com.google.android.gms.** {*;}
-keep class android.support.v7.** {*;}

-keep class android.support.design.** {*;}
-keep class android.support.v4.** {*;}
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class org.bookdash.android.domain.pojo.gson.** { *; }

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class org.bookdash.android.domain.model.** {
  *;
}
-keep class android.arch.lifecycle.** { *; }