package jery.kara.download.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by CPU10584-local on 03-May-18.
 */

public class DownloadFile extends AsyncTask<String, String, Boolean> {
    private final int timeout = 5000;
    private String url;
    private String desFilePath;
    private Boolean isCanceled;
    private Boolean isSuccess = true;
    private Context mContext;
    private DownloadFileListener downloadFileListener;

    public DownloadFile(Context context, String url, String desFilePath) {
        this.mContext = context;
        this.url = url;
        this.desFilePath = desFilePath;
        isCanceled = false;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        int count;
        isSuccess = true;
        HttpURLConnection conection = null;
        try {
            URL url = new URL(this.url);
            String desPath = this.desFilePath;
            conection = (HttpURLConnection) url.openConnection();
            conection.setRequestProperty("Accept-Encoding", "identity");
            conection.setConnectTimeout(timeout);
            conection.setReadTimeout(timeout);
            if(conection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                int lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(desPath);

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1 && !isCanceled) {
                    total += count;
                    int iProgress = (int) ((total * 100) / lenghtOfFile);
                    if(iProgress > 100) {
                        iProgress = 100;
                    }
                    publishProgress(String.valueOf(iProgress));
                    output.write(data, 0, count);

                    if(!NetworkHelper.isAvailableNetwork(mContext)) {
                        isCanceled = true;
                        isSuccess = false;
                    }
                }
                output.flush();

                output.close();
                input.close();

            }
            isCanceled = true;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            isCanceled = true;
            isSuccess = false;
        } finally {
            if (conection != null) {
                conection.disconnect();
            }
        }

        return isSuccess;

    }

    protected void onProgressUpdate(String... progress) {
        Log.d("Downloading: ", String.valueOf(progress[0]));
        if (downloadFileListener != null) {
            downloadFileListener.onProgress(Integer.parseInt(progress[0]));
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (downloadFileListener != null) {
            if (isCanceled) {
                if (aBoolean) {
                    downloadFileListener.onCompleted("Completed");
                } else {
                    downloadFileListener.onFailed("Failed");
                }
                isCanceled = false;
            } else {
                downloadFileListener.onProgress(0);
            }
        }
    }

    public void cancle(){
        isCanceled = true;
        isSuccess = false;
        downloadFileListener = null;
    }

    public void setDownloadFileListener(DownloadFileListener downloadFileListener) {
        this.downloadFileListener = downloadFileListener;
    }

    public interface DownloadFileListener {
        void onProgress(int iProgress);
        void onCompleted(String url);
        void onFailed(String url);
    }
}