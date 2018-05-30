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

public class DownloadFile extends AsyncTask<String, Integer, Boolean> {
    private final int timeout = 5000;
    private Boolean isCanceled;
    private Context mContext;
    private DownloadFileListener downloadFileListener;

    public DownloadFile(Context context) {
        this.mContext = context;
        isCanceled = false;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        int count;
        Boolean isSuccess = true;
        HttpURLConnection conection = null;
        try {
            URL url = new URL(params[0]);
            String desPath = params[1];
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

                while ((count = input.read(data)) != -1) {
                    if(isCanceled || !NetworkHelper.isAvailableNetwork(mContext)) {
                        Log.e("Error: ", "stop");
                        isSuccess = false;
                        break;
                    }
                    total += count;
                    int iProgress = (int) ((total * 100) / lenghtOfFile);
                    if(iProgress > 100) {
                        iProgress = 100;
                    }
                    publishProgress(iProgress);
                    output.write(data, 0, count);
                }
                output.flush();

                output.close();
                input.close();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            isSuccess = false;
        } finally {
            if (conection != null) {
                conection.disconnect();
            }
        }
        return isSuccess;
    }

    protected void onProgressUpdate(Integer... progress) {
        downloadFileListener.onProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        if(isSuccess) {
            downloadFileListener.onCompleted();
        } else {
            if(!isCanceled) {
                downloadFileListener.onFailed();
            }
        }

    }

    public void setDownloadFileListener(DownloadFileListener downloadFileListener) {
        this.downloadFileListener = downloadFileListener;
    }

    public void cancel() {
        isCanceled = true;
    }

    public interface DownloadFileListener {
        void onProgress(int iProgress);
        void onCompleted();
        void onFailed();
    }
}