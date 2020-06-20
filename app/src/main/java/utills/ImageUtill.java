package utills;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ImageUtill {
    private static final int DEFAULT_MIN_WIDTH_QUALITY = 50;        // min pixels
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";
    // ProgressDialog mProgress= new ProgressDialog(getApplicationContext());


    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    public static String saveImgPath ;

    public static Intent getGalleryIntenr(Context context) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return pickIntent;
    }
    public static Intent getVideoIntenr(Context context) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        return pickIntent;
    }

    public static String getVideoPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    public static Intent getCameraIntent(Context context) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT > 21) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider_path", getTempFile()));
        } else {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));

        }
        return takePhotoIntent;
    }

    public static Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Uri bmpUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider_path", getTempFile());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, bmpUri);
        } else {

            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
        }

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }
        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }


    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) {
        Log.d(TAG, "getImageFromResult, resultCode: " + resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile();
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            filePath = selectedImage.getPath();
            Log.d(TAG, "selectedImage: " + selectedImage);

            bm = getImageResized(context, selectedImage);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);
        }
        return bm;
    }

    public static String filePath = "";


    private static File getTempFile() {
        File imageFile = new File(Environment.getExternalStorageDirectory(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = sampleSize;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);
        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());
        return actuallyUsableBitmap;
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }//End of try-catch block
        return result;
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }

    public static String getImagePath() {
        return filePath;
    }

    public static String getPathFromUri(Context context, Uri uri) {

        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getBitmapPath(Bitmap bmp, Context mContext) {
        try {
            String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bmp, "Title", null);
            Uri tempUri = Uri.parse(path);
            Cursor cursor = mContext.getContentResolver().query(tempUri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap DownloadImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;

        int responseCode = -1;
        try{

            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                return bmp;
            }

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
        return bmp;
    }





    public static class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start

        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog

            if(result!=null){
                // Display the downloaded image into ImageView


                // Save bitmap to internal storage
                //   Uri imageInternalUri = saveImageToInternalStorage(result);_____________________________________//
                // Set the ImageView image from internal storage
                //  mImageViewInternal.setImageURI(imageInternalUri);
            }else {
                // Notify user that an error occurred while downloading image
                // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    public static URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
//    protected static Uri saveImageToInternalStorage(Bitmap bitmap){
//        // Initialize ContextWrapper
//        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
//
//        // Initializing a new file
//        // The bellow line return a directory in internal storage
//        File file = wrapper.getDir("Images",MODE_PRIVATE);
//
//        // Create a file to save the image
//        file = new File(file, "UniqueFileName"+".jpg");
//
//        try{
//            // Initialize a new OutputStream
//            OutputStream stream = null;
//
//            // If the output file exists, it can be replaced or appended to it
//            stream = new FileOutputStream(file);
//
//            // Compress the bitmap
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//
//            // Flushes the stream
//            stream.flush();
//
//            // Closes the stream
//            stream.close();
//
//        }catch (IOException e) // Catch the exception
//        {
//            e.printStackTrace();
//        }
//
//        // Parse the gallery image url to uri
//        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
//        Log.e("Uri_img",savedImageURI.toString());
//
//        saveImgPath =savedImageURI.toString();
//        // Return the saved image Uri
//        return savedImageURI;
//    }
}



