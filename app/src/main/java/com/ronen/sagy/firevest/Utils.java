package com.ronen.sagy.firevest;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    private static final String TAG = "Utils";

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(Resources.getSystem(), x);
        } catch (Exception e) {
            Log.e(TAG, "drawableFromUrl: ", e);
        }
        return null;
    }

    public static float functionNormalize(int max, int min, int value) {
        int intermediateValue = max - min;
        value -= intermediateValue;
        float var = Math.abs((float) value / (float) intermediateValue);
        return Math.abs((float) value / (float) intermediateValue);
    }
//    public static List<Users> loadProfiles(Context context) {
//        try {
//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
////            JSONArray array = new JSONArray(loadJSONFromAsset(context, "profiles.json"));
////            List<Users> profileList = new ArrayList<>();
////            for (int i = 0; i < array.length(); i++) {
////                Users profile = gson.fromJson(array.getString(i), Users.class);
////                profileList.add(profile);
////            }
//            startUpsLists()
//            return profileList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json;
        InputStream is;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG, "path " + jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public interface OnItemCreated {
        void itemCreated(String title);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}