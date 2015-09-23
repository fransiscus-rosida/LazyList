# LazyList

A simple library to display images in Android ListView. Images are being downloaded asynchronously in the background. Images are being cached on SD card and in memory. Can also be used for GridView and just to display images into an ImageView.

## Basic Usage
``` java
ImageLoader imageLoader=new ImageLoader(context);
...
imageLoader.DisplayImage(url, imageView);
```
Don't forget to add the following permissions to your AndroidManifest.xml:

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

