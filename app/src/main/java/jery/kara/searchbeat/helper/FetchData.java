package jery.kara.searchbeat.helper;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jery.kara.searchbeat.BeatInfo;
import jery.kara.searchbeat.SearchBeatDialog;

/**
 * Created by CPU11341-local on 20-Apr-18.
 */

public class FetchData extends AsyncTask<String,Void,Void> {
    String data ="";
    BeatInfo beatInfo;
    List<BeatInfo> resultBeatArray;
    SearchBeatDialog searchBeatDialog;

    public FetchData(SearchBeatDialog searchBeatDialog) {
        this.searchBeatDialog = searchBeatDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        resultBeatArray = new ArrayList<>();
        searchBeatDialog.lbl_loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject result = (new JSONObject(data)).getJSONObject("result");
            JSONArray JA = result.getJSONArray("songs");
            for(int i =0 ;i <JA.length(); i++){
                beatInfo = new BeatInfo();
                JSONObject JO = JA.getJSONObject(i);
                beatInfo.id = JO.getInt("songId");
                beatInfo.title = JO.getString("songName");
                beatInfo.author = JO.getString("singerName");
                beatInfo.beatDownloadLink = JO.getString("songUrl");
                beatInfo.lyricDownloadLink = JO.getString("lyricUrl");
                beatInfo.beatLocalPath = Environment.getExternalStorageDirectory() + "/TalkTV/" + beatInfo.id + ".mp3";
                beatInfo.lyricLocalPath = Environment.getExternalStorageDirectory() + "/TalkTV/" + beatInfo.id + ".lrc";
                resultBeatArray.add(beatInfo);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        searchBeatDialog.updateData(resultBeatArray);
        searchBeatDialog.lbl_loading.setVisibility(View.GONE);
    }
}
