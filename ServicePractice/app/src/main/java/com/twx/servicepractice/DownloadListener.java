package com.twx.servicepractice;

/**
 * Created by twx on 2017/3/31.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
