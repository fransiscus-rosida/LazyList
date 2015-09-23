package com.koez7.facts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... urls) {
		//Log.i("HttpAsyncTask Url", "try to get search result [" + urls[0] + "]");
		return GET(urls[0]);
	}

	private String GET(String url) {
		//Log.i("Url", url);
		InputStream inputStream = null;
		String result = "";
		try {
			//HttpClient httpclient = new DefaultHttpClient();
			//HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			//inputStream = httpResponse.getEntity().getContent();
			
			URL factUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)factUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            inputStream=conn.getInputStream();

			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				
			} else {
				result = null;
			}
		} catch (Exception e) {
			/* TODO 
			 * - add error handling
			 */
			Log.e("InputStream", e.getLocalizedMessage());
		}
		return result;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		Log.i("Result PostExecute", result);
		//parseJSon(result);
	}
	
	// convert input stream into string method
	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null) {
			result += line;
		}
		Log.i("ResultInputStreamToString", result);
		inputStream.close();
		return result;
	}
	
	private void parseJSon(String strJson){
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject)jsonParser.parse(strJson);
        JsonArray jsonArr = jo.getAsJsonArray("rows");
        Gson gson = new GsonBuilder().create();
		Fact[] p = gson.fromJson(jsonArr, Fact[].class);
		Log.i("title", p[1].getTitle());
	}
}

