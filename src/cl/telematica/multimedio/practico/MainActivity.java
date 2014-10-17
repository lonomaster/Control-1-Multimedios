package cl.telematica.multimedio.practico;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.telematica.multimedio.practico.Detalles;
import cl.telematica.multimedio.practico.MainActivity;
import cl.telematica.multimedio.practico.app.AppController;
import cl.telematica.multimedio.practico.model.Element;
import cl.telematica.multimedio.practico.adater.CustomListAdapter;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String url = "http://www.mocky.io/v2/5440667984d353f103f697c0";
	private ProgressDialog pDialog;
	private List<Element> elementos = new ArrayList<Element>();
	private ListView listView;
	private CustomListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		adapter = new CustomListAdapter(this, elementos);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	                 public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
	            	
	            	
	          	  Object o = adapter.getItem(position);
	                               Element obj_itemDetails = (Element)o;
	                             //  Toast.makeText(MainActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();

	      
	            	
	            	//Creamos el Intent
	            	Intent intent = new Intent(MainActivity.this, Detalles.class);
	            	
	            	//Creamos la informaci�n a pasar entre actividades
	            	Bundle b = new Bundle(); 
	            	b.putString("image", obj_itemDetails.getImage());
	                        	
	            	//A�adimos la informaci�n al intent
	            	intent.putExtras(b);
	            	
	            	//Iniciamos la nueva actividad
	                startActivity(intent);   
	             }  
	      });
		JsonArrayRequest request = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d("Json", response.toString());	
						hidePDialog();
						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								Element elem = new Element();
								elem.setTitle(obj.getString("title"));
								elem.setImage(obj.getString("image"));
								elem.setPoints(obj.getString("points"));
	


								// adding movie to movies array
								elementos.add(elem);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						adapter.notifyDataSetChanged();
					
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Error", "Error: " + error.getMessage());

					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(request);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

}
