package com.koez7.facts;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    
    ListView list;
    LazyImageLoadAdapter adapter;
    String factUrl;
    String factTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        factUrl = "https://dl.dropboxusercontent.com/u/746330/facts.json";
        
        list=(ListView)findViewById(R.id.list);
        
        factTitle = fetchFacts(factUrl);
        setTitle(factTitle);
        
        genFacts();
        
        Button b=(Button)findViewById(R.id.button1);
        b.setOnClickListener(listener);
    }
    
    @Override
    public void onDestroy()
    {
    	// Remove adapter refference from list
        list.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener=new OnClickListener(){
        @Override
        public void onClick(View arg0) {
        	
        	//Refresh cache directory downloaded images
        	fetchFacts(factUrl);
            
        	genFacts();
            adapter.imageLoader.clearCache();
            adapter.notifyDataSetChanged();
        }
    };
    
    
    public void onItemClick(int mPosition)
    {
    	String tempValues = FactContainer.getImgRef(mPosition);
    	
    	Toast.makeText(MainActivity.this, 
    			"Image URL : "+tempValues, 
    			Toast.LENGTH_LONG)
    	.show();
    }
    
    // Image urls used in LazyImageLoadAdapter.java file
    
    private String fetchFacts(String strUrl){
		String strResult = null;
		String strFactTitle = null;
		
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// check network 
		if (networkInfo != null && networkInfo.isConnected()) {
			HttpAsyncTask factTask = new HttpAsyncTask();
			factTask
					.execute(strUrl);

			try {
				strResult = factTask.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getBaseContext(), "No Internet Connection!",Toast.LENGTH_LONG).show();
			
		}
		Log.i("strREsult", strResult);
		
		//strResult = "{\"title\":\"About Canada\",\"rows\":[	{	\"title\":\"Beavers\",	\"description\":\"Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony\",	\"imageHref\":\"http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg\"	},	{	\"title\":\"Flag\",	\"description\":null,	\"imageHref\":\"http://images.findicons.com/files/icons/662/world_flag/128/flag_of_canada.png\"	},	{	\"title\":\"Transportation\",	\"description\":\"It is a well known fact that polar bears are the main mode of transportation in Canada. They consume far less gas and have the added benefit of being difficult to steal.\",	\"imageHref\":\"http://1.bp.blogspot.com/_VZVOmYVm68Q/SMkzZzkGXKI/AAAAAAAAADQ/U89miaCkcyo/s400/the_golden_compass_still.jpg\"	},	{	\"title\":\"Hockey Night in Canada\",	\"description\":\"These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.\",	\"imageHref\":\"http://fyimusic.ca/wp-content/uploads/2008/06/hockey-night-in-canada.thumbnail.jpg\"	},	{	\"title\":\"Eh\",	\"description\":\"A chiefly Canadian interrogative utterance, usually expressing surprise or doubt or seeking confirmation.\",	\"imageHref\":null	},	{	\"title\":\"Housing\",	\"description\":\"Warmer than you might think.\",	\"imageHref\":\"http://icons.iconarchive.com/icons/iconshock/alaska/256/Igloo-icon.png\"	},	{	\"title\":\"Public Shame\",	\"description\":\" Sadly it's true.\",	\"imageHref\":\"http://static.guim.co.uk/sys-images/Music/Pix/site_furniture/2007/04/19/avril_lavigne.jpg\"	},	{	\"title\":null,	\"description\":null,	\"imageHref\":null	},	{	\"title\":\"Space Program\",	\"description\":\"Canada hopes to soon launch a man to the moon.\",	\"imageHref\":\"http://files.turbosquid.com/Preview/Content_2009_07_14__10_25_15/trebucheta.jpgdf3f3bf4-935d-40ff-84b2-6ce718a327a9Larger.jpg\"	},	{	\"title\":\"Meese\",	\"description\":\"A moose is a common sight in Canada. Tall and majestic, they represent many of the values which Canadians imagine that they possess. They grow up to 2.7 metres long and can weigh over 700 kg. They swim at 10 km/h. Moose antlers weigh roughly 20 kg. The plural of moose is actually 'meese', despite what most dictionaries, encyclopedias, and experts will tell you.\",	\"imageHref\":\"http://caroldeckerwildlifeartstudio.net/wp-content/uploads/2011/04/IMG_2418%20majestic%20moose%201%20copy%20(Small)-96x96.jpg\"	},	{	\"title\":\"Geography\",	\"description\":\"It's really big.\",	\"imageHref\":null	},	{	\"title\":\"Kittens...\",	\"description\":\"?are illegal. Cats are fine.\",	\"imageHref\":\"http://www.donegalhimalayans.com/images/That%20fish%20was%20this%20big.jpg\"	},	{	\"title\":\"Mounties\",	\"description\":\"They are the law. They are also Canada's foreign espionage service. Subtle.\",	\"imageHref\":\"http://3.bp.blogspot.com/__mokxbTmuJM/RnWuJ6cE9cI/AAAAAAAAATw/6z3m3w9JDiU/s400/019843_31.jpg\"	},	{	\"title\":\"Language\",	\"description\":\"Nous parlons tous les langues importants.\",	\"imageHref\":null	}]}";
		try {
			JSONObject jsonRootObject = new JSONObject(strResult);
			strFactTitle = jsonRootObject.optString("title").toString();
			
			// Get the instance of JSONArray that contains JSONObjects
			JSONArray jsonArray = jsonRootObject.optJSONArray("rows");
			int totalFact = jsonArray.length();
			FactContainer.initFact(totalFact);
			
			final ArrayList<String> list = new ArrayList<String>();
		    
			// Iterate the jsonArray and print the info of JSONObjects
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				String title = jsonObject.optString("title").toString();
				String description = jsonObject.optString("description").toString();
				String imageHref = jsonObject.optString("imageHref").toString();
				
				list.add(title);
				
				FactContainer.setFact(i, title, description, imageHref);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("Error Fetch Facts", e.getMessage());
		}
		return strFactTitle;
	}
    
    private void genFacts(){
    	// Create custom adapter for listview
        adapter=new LazyImageLoadAdapter(this, FactContainer.getFact());
        
        //Set adapter to listview
        list.setAdapter(adapter);
    }
    
    
}