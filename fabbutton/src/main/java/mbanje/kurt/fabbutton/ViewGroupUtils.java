package mbanje.kurt.fabbutton;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kurt on 2015/06/08.
 */
public class ViewGroupUtils {
    private static final ViewGroupUtilsImpl IMPL;

    ViewGroupUtils() {
    }

    public static void offsetDescendantRect(ViewGroup parent, View descendant, Rect rect) {
        IMPL.offsetDescendantRect(parent, descendant, rect);
    }

    public static void getDescendantRect(ViewGroup parent, View descendant, Rect out) {
        out.set(0, 0, descendant.getWidth(), descendant.getHeight());
        offsetDescendantRect(parent, descendant, out);
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if(version >= 11) {
            IMPL = new ViewGroupUtilsImplHoneycomb();
        } else {
            IMPL = new ViewGroupUtilsImplBase();
        }

    }

    private static class ViewGroupUtilsImplHoneycomb implements ViewGroupUtilsImpl {
        private ViewGroupUtilsImplHoneycomb() {
        }

        public void offsetDescendantRect(ViewGroup parent, View child, Rect rect) {
            ViewGroupUtilsHoneycomb.offsetDescendantRect(parent, child, rect);
        }
    }

    private static class ViewGroupUtilsImplBase implements ViewGroupUtilsImpl {
        private ViewGroupUtilsImplBase() {
        }

        public void offsetDescendantRect(ViewGroup parent, View child, Rect rect) {
            parent.offsetDescendantRectToMyCoords(child, rect);
        }
    }

    private interface ViewGroupUtilsImpl {
        void offsetDescendantRect(ViewGroup var1, View var2, Rect var3);
    }
}
