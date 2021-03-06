/**
 * Flym
 * <p/>
 * Copyright (c) 2012-2015 Frederic Julian
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.yanus171.feedexfork.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LongSparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.yanus171.feedexfork.MainApplication;
import ru.yanus171.feedexfork.R;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class UiUtils {

    static private final LongSparseArray<Bitmap> FAVICON_CACHE = new LongSparseArray<>();

    static public void setPreferenceTheme(Activity a) {
        if (PrefUtils.IsLightTheme())
            a.setTheme(R.style.Theme_Light);
        else
            a.setTheme(R.style.Theme_Dark);

    }

    static public int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, MainApplication.getContext().getResources().getDisplayMetrics());
    }
    static public int mmToPixel(int mm) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, MainApplication.getContext().getResources().getDisplayMetrics());
    }
    static public void addEmptyFooterView(ListView listView, int dp) {
        View view = new View(listView.getContext());
        view.setMinimumHeight(dpToPixel(dp));
        view.setClickable(true);
        listView.addFooterView(view);
    }

    static public void showMessage(@NonNull Activity activity, @StringRes int messageId) {
        showMessage(activity, activity.getString(messageId));
    }

    static public void toast(@NonNull Context context, @StringRes int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
    }

    static public void SetFontSize(TextView textView) {
        textView.setTextSize(COMPLEX_UNIT_DIP, 18 + PrefUtils.getFontSizeEntryList() );
    }

    static public void showMessage(@NonNull Activity activity, @NonNull String message) {
        View coordinatorLayout = activity.findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make((coordinatorLayout != null ? coordinatorLayout : activity.findViewById(android.R.id.content)), message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundResource(R.color.material_grey_900);
        snackbar.show();
    }

    static public Bitmap getFaviconBitmap(long feedId, Cursor cursor, int iconCursorPos) {
        Bitmap bitmap = UiUtils.FAVICON_CACHE.get(feedId);
        if (bitmap == null) {
            byte[] iconBytes = cursor.getBlob(iconCursorPos);
            if (iconBytes != null && iconBytes.length > 0) {
                bitmap = UiUtils.getScaledBitmap(iconBytes, 18);
                UiUtils.FAVICON_CACHE.put(feedId, bitmap);
            }
        }
        return bitmap;
    }

    static public Bitmap getScaledBitmap(byte[] iconBytes, int sizeInDp) {
        if (iconBytes != null && iconBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
            if (bitmap != null && bitmap.getWidth() != 0 && bitmap.getHeight() != 0) {
                int bitmapSizeInDip = UiUtils.dpToPixel(sizeInDp);
                if (bitmap.getHeight() != bitmapSizeInDip) {
                    Bitmap tmp = bitmap;
                    bitmap = Bitmap.createScaledBitmap(tmp, bitmapSizeInDip, bitmapSizeInDip, false);
                    tmp.recycle();
                }

                return bitmap;
            }
        }

        return null;
    }

    private static Handler mHandler = null;
    public static void RunOnGuiThread( final Runnable r ) {
        if ( mHandler == null )
            mHandler  = new Handler(Looper.getMainLooper());
        synchronized ( mHandler ) {
            mHandler.post( r );
        }

    }
    public static void RunOnGuiThread( final Runnable r, int delay ) {
        if ( mHandler == null )
            mHandler  = new Handler(Looper.getMainLooper());
        synchronized ( mHandler ) {
            mHandler.postDelayed( r, delay );
        }

    }
    public static void RunOnGuiThreadInFront( final Runnable r ) {
        if ( mHandler == null )
            mHandler  = new Handler(Looper.getMainLooper());
        synchronized ( mHandler ) {
            mHandler.postAtFrontOfQueue( r );
        }

    }

    public static void HideButtonText(View rootView, int ID, boolean transparent) {
        TextView btn = rootView.findViewById(ID);
        if ( transparent )
            btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setText("");
    }



}
