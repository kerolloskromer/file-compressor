[![](https://jitpack.io/v/kerolloskromer/file-compressor.svg)](https://jitpack.io/#kerolloskromer/file-compressor)

# File Compressor

Helper Utility to easily compress files with convenient callbacks

## Currently Supported Files

- [x] JPG
- [x] JPEG
- [x] PNG

## Getting Started

1- Add in your root build.gradle at the end of repositories
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
2- Add the dependency in your app build.gradle file
```
  dependencies {
    implementation 'com.github.kerolloskromer:file-compressor:<latest-version>'
  }
```
3- Start compression task
```
FileCompressor fileCompressor = new FileCompressor(context, compressionListener, delete);
fileCompressor.execute(file);
```
"delete" is boolean set to true if you want to delete the new compressed file when you are done with it

4- Listen on compression task
```
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
```
5- DO NOT forget to dispose
```
  @Override
  protected void onDestroy() {
    if (fileCompressor != null) {
      fileCompressor.dispose();
    }
    super.onDestroy();
  }
```
## Report Bug / Issue / Improvement

Please feel free to report bug , issue or improvement - see the [Issues](https://github.com/kerolloskromer/file-compressor/issues) section fisrt to prevent duplicates.
Also, if you know how to fix this issue please feel free to fork this repo and make a pull request and i will gladly review and merge and add you as a contributer :)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
