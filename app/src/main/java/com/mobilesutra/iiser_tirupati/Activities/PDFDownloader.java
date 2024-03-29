package com.mobilesutra.iiser_tirupati.Activities;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class PDFDownloader{
    private static final int  MEGABYTE = 1024 * 1024;

    public static int downloadFile(String fileUrl, File directory){

        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            urlConnection.setConnectTimeout (50*1000);
            // give it 15 seconds to respond
            urlConnection.setReadTimeout(50*1000);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            return 1;
        }catch (SocketTimeoutException e) {
            // e.printStackTrace();
            Log.d("Mainactivity","SocketTimeoutException");

            return 0;
        }  catch (FileNotFoundException e) {
            // e.printStackTrace();
            Log.d("Mainactivity","Filenotfoundexception");
            return 0;
        } catch (MalformedURLException e) {
            // e.printStackTrace();
            Log.d("Mainactivity","MalformedURLException");
            return 0;
        }catch (IOException e) {
            // e.printStackTrace();
            Log.d("Mainactivity","IOException");
//        	 Toast.makeText(Downloader.this,
//    				 "Your internet connection is slow..", Toast.LENGTH_LONG)
//    				 .show();
            return 0;
        }
    }

}
