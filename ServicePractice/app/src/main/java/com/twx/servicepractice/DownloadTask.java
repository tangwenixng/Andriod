package com.twx.servicepractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by twx on 2017/3/31.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSE = 2;
    private static final int TYPE_CANCELED = 3;

    private DownloadListener listener;

    private int lastProgress;

    long fileLength;

    private boolean isCanceled=false,isPause=false;


    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String downloadUrl = params[0];

        String filename = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directory+filename);
        if (file.exists()) {
             fileLength = file.length();
        }
        long cotentLength = getContentLength(downloadUrl);

        if (cotentLength==0)
            return TYPE_FAILED;
        else if (cotentLength==fileLength)
            return  TYPE_SUCCESS;
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().addHeader("RANGE","bytes="+fileLength+"-").url(downloadUrl).build();
        RandomAccessFile random = null;
        InputStream inputStream = null;
        try {
            Response respone = client.newCall(request).execute();
            if (respone != null) {
                random = new RandomAccessFile(file, "rw");
                random.seek(fileLength);

                inputStream = respone.body().byteStream();
                byte[] arr = new byte[1024];

                int len,total =0;
                while ((len = inputStream.read(arr)) != -1) {
                    if (isPause)
                        return TYPE_PAUSE;
                    if (isCanceled) {
                        file.delete();
                        return TYPE_CANCELED;
                    }
                    total+=len;
                    random.write(arr,0,len);
                    int progress = (int) (((total+fileLength)/cotentLength)*100);
                    publishProgress(progress);
                }
               respone.body().close();
                return TYPE_SUCCESS;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (random != null) {
                try {
                    random.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSE:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
        }
        super.onPostExecute(integer);
    }

    public long getContentLength(String downloadUrl) {
        long contentLength = 0;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                contentLength = response.body().contentLength();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
        return contentLength;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    public void pauseDownload() {
        isPause = true;
    }
}
