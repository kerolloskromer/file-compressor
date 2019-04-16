package com.kromer.filecompressor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Kerollos Kromer on 17-Mar-19.
 */
class FileUtils {

  static Uri getUriForFile(Context context, File file) {
    Uri fileURI;
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      fileURI = FileProvider.getUriForFile(context,
          context.getApplicationContext().getPackageName() + ".fileprovider",
          file);
    } else {
      fileURI = Uri.fromFile(file);
    }
    return fileURI;
  }

  /**
   * This method is responsible for solving the rotation issue if exist. Also scale the images to
   * 1024x1024 resolution
   *
   * @param context The current context
   * @param selectedImage The Image URI
   * @return Bitmap image results
   * @throws IOException exception
   */
  static Bitmap handleSamplingBitmap(Context context, Uri selectedImage)
      throws IOException {
    int MAX_HEIGHT = 1024;
    int MAX_WIDTH = 1024;

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
    BitmapFactory.decodeStream(imageStream, null, options);
    imageStream.close();

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    imageStream = context.getContentResolver().openInputStream(selectedImage);
    Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
    imageStream.close();

    return img;
  }

  /**
   * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
   * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
   * the closest inSampleSize that will result in the final decoded bitmap having a width and
   * height equal to or larger than the requested width and height. This implementation does not
   * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
   * results in a larger bitmap which isn't as useful for caching purposes.
   *
   * @param options An options object with out* params already populated (run through a decode*
   * method with inJustDecodeBounds==true
   * @param reqWidth The requested width of the resulting bitmap
   * @param reqHeight The requested height of the resulting bitmap
   * @return The value to be used for inSampleSize
   */
  private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
      int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
      // Calculate ratios of height and width to requested height and width
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
      // with both dimensions larger than or equal to the requested height and width.
      inSampleSize = heightRatio < widthRatio
          ? heightRatio
          : widthRatio;

      // This offers some additional logic in case the image has a strange
      // aspect ratio. For example, a panorama may have a much larger
      // width than height. In these cases the total pixels might still
      // end up being too large to fit comfortably in memory, so we should
      // be more aggressive with sample down the image (=larger inSampleSize).

      final float totalPixels = width * height;

      // Anything more than 2x the requested pixels we'll sample down further
      final float totalReqPixelsCap = reqWidth * reqHeight * 2;

      while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++;
      }
    }
    return inSampleSize;
  }

  static String getExtension(String uri) {
    if (uri != null) {
      int dot = uri.lastIndexOf(".");
      if (dot >= 0) {
        return uri.substring(dot).toLowerCase();
      }
    }
    return "";
  }

  static String getCurrentDateTime() {
    DateFormat dfDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    String date = dfDate.format(Calendar.getInstance().getTime());

    DateFormat dfTime = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
    String time = dfTime.format(Calendar.getInstance().getTime());

    return date + time;
  }
}