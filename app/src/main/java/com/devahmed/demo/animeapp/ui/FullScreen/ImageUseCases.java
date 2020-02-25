package com.devahmed.demo.animeapp.ui.FullScreen;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.devahmed.demo.animeapp.R;
import com.devahmed.demo.animeapp.common.BaseObservableMvcView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Calendar;

public class ImageUseCases extends BaseObservableMvcView<ImageUseCases.Listener> {

    private final String[] STORAGE_PERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private String imageAbsolutePath;
    private Activity context;

    public ImageUseCases(Activity activity) {
        this.context = activity;
    }

    public interface Listener{
        void onImageDonwloaded();
    }

    public void showDownloadDialogThenDownloadTheImage(final String imageURL){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_download);
        final AVLoadingIndicatorView mProgressBar;
        final ImageView sucessIcon;
        final TextView downloading;
        mProgressBar = dialog.findViewById(R.id.aviDownloading);
        downloading = dialog.findViewById(R.id.downloading);
        sucessIcon = dialog.findViewById(R.id.sucess_icon);
        mProgressBar.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        downloadImage(imageURL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String SucessString = context.getResources().getString(R.string.sucess);
                String CanceledString = context.getResources().getString(R.string.canceled);
                mProgressBar.setVisibility(View.INVISIBLE);
                downloading.setText(SucessString);
                sucessIcon.setVisibility(View.VISIBLE);
                notifyImageDownloadedSuccessfully();
            }
        }, 2000);
    }

    public void showDownloadDialogThenSetImageAsBackground(final String imageURL){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_set_as_wallpaper);
        final AVLoadingIndicatorView mProgressBar;
        final TextView downloading;
        mProgressBar = dialog.findViewById(R.id.aviProcessing);
        downloading = dialog.findViewById(R.id.Processing);
        mProgressBar.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        downloadImageAndSetBackground(imageURL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String SucessString = context.getResources().getString(R.string.sucess);
                String CanceledString = context.getResources().getString(R.string.canceled);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }else{
                    Toast.makeText(context, CanceledString, Toast.LENGTH_SHORT).show();
                }

            }
        }, 1300);
    }

    public void showDownloadDialogThenShareImage(final String imageURL){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_set_as_wallpaper);
        final AVLoadingIndicatorView mProgressBar;
        final TextView downloading;
        mProgressBar = dialog.findViewById(R.id.aviProcessing);
        downloading = dialog.findViewById(R.id.Processing);
        mProgressBar.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        downloadImageAndShareIt(imageURL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String SucessString = context.getResources().getString(R.string.sucess);
                String CanceledString = context.getResources().getString(R.string.canceled);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }else{
                    Toast.makeText(context, CanceledString, Toast.LENGTH_SHORT).show();
                }
            }
        }, 1300);
    }


    private void downloadImage(final String imageURL){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(imageURL);
                bitmap = getResizedBitmap(bitmap , 1024 , 780);
                saveImage(bitmap , "Cutes" + Calendar.getInstance().getTimeInMillis());
            }
        });
        thread.start();

    }
    private void downloadImageAndSetBackground(final String imageURL){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(imageURL);
                bitmap = getResizedBitmap(bitmap , 1024 , 780);
                saveImage(bitmap , "Cutes" + Calendar.getInstance().getTimeInMillis());
                if(imageAbsolutePath != null){
                    Uri contentURI = getImageContentUri(context, imageAbsolutePath);
                    Intent intent = new Intent(WallpaperManager
                            .getInstance(context)
                            .getCropAndSetWallpaperIntent(contentURI));
                    context.startActivity(Intent.createChooser(intent , "Set as"));
                }
            }
        });
        thread.start();

    }

    private void downloadImageAndShareIt (final String imageURL){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(imageURL);
                bitmap = getResizedBitmap(bitmap , 1024 , 780);
                saveImage(bitmap , "Cutes" + Calendar.getInstance().getTimeInMillis());
                if(imageAbsolutePath != null){
                    shareImage(imageAbsolutePath);
                }
            }
        });
        thread.start();

    }

    public static Uri getImageContentUri(Context context, String absPath) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[] { MediaStore.Images.Media._ID }
                , MediaStore.Images.Media.DATA + "=? "
                , new String[] { absPath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    public void askForStoragePermission(){
        ActivityCompat.requestPermissions(context, STORAGE_PERMISSION, REQUEST_CODE_PERMISSIONS);
    }
    public boolean StoragePermissionGranted(){

        if(ContextCompat.checkSelfPermission(context, STORAGE_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED){
            return false;
        }

        return true;
    }

    //checks network
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    private void shareImage(String imagePath) {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");



        File imageFileToShare = new File(imagePath);

        Uri photoURI = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider"
                , imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, photoURI);

        context.startActivity(Intent.createChooser(share, "Share Image!"));
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString() + "/Wallpapers";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        //get absolutepath to send to WallpaperManager
        imageAbsolutePath = file.getAbsolutePath();
        if (file.exists()) file.delete();
        //Add these images to gallery to show them
        galleryAddPic(file);
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    void notifyImageDownloadedSuccessfully(){
        for(Listener listener : getmListeners()){
            listener.onImageDonwloaded();
        }
    }
}
