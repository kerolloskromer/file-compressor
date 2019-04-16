package com.kromer.filecompressor.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.kromer.filecompressor.FileCompressor;
import java.io.File;

public class MainActivity extends AppCompatActivity {

  FileCompressor fileCompressor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    File file = new File("");

    fileCompressor =
        new FileCompressor(MainActivity.this, compressionListener, true);
    fileCompressor.execute(file);
  }

  FileCompressor.CompressionListener compressionListener =
      new FileCompressor.CompressionListener() {
        @Override
        public void onStart() {
          // task started
        }

        @Override
        public void onSuccess(File compressedFile) {
          // task succeeded
        }

        @Override
        public void onFailure() {
          // task failed
        }
      };

  @Override
  protected void onDestroy() {
    if (fileCompressor != null) {
      fileCompressor.dispose();
    }
    super.onDestroy();
  }
}