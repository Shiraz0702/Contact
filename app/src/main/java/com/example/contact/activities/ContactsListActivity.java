package com.example.contact.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.contact.R;
import com.example.contact.adapter.ContactsItemAdapter;
import com.example.contact.config.StatusApp;
import com.example.contact.model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.contact.utils.Network.isNetworkAvailable;

public class ContactsListActivity extends AppCompatActivity {

    @BindView(R.id.myProgressBar) ProgressBar progressBar;
    @BindView(R.id.list) ListView listView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private Activity activity = this;
    private RequestQueue requestQueue;
    private StatusApp statusApp;
    private List<Contact> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(this);
        statusApp = new StatusApp(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_1), getResources().getColor(R.color.swipe_2), getResources().getColor(R.color.swipe_3));
        if (!isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Async().execute();
                }
            });
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (!isNetworkAvailable(activity)) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    new Async().execute();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                View view1 = view.findViewById(R.id.logo_iv);
                Intent intent = new Intent(activity, ContactActivity.class);
                bundle.putSerializable("contact", list.get(position));
                intent.putExtras(bundle);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(ContactsListActivity.this,
                                view1,  "key");
                ActivityCompat.startActivity(ContactsListActivity.this, intent, options.toBundle());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        statusApp.setLogin(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    class Async extends AsyncTask<String, String, ContactsItemAdapter> {
        private final String key = "https://api.myjson.com/bins/m637o";
        ContactsItemAdapter adapter = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ContactsItemAdapter contactsItemAdapter) {
            super.onPostExecute(contactsItemAdapter);
        }

        @Override
        protected ContactsItemAdapter doInBackground(String... strings) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, key, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("people");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Contact contact = new Contact();
                        try {
                            contact.setName(jsonArray.getJSONObject(i).optString("name"));
                            contact.setSurname(jsonArray.getJSONObject(i).optString("surname"));
                            contact.setPhone(jsonArray.getJSONObject(i).optString("phone"));
                            contact.setEmail(jsonArray.getJSONObject(i).optString("email"));
                            contact.setImage(jsonArray.getJSONObject(i).optString("image"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        list.add(contact);
                    }

                    adapter = new ContactsItemAdapter(activity, list);
                    listView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(jsonObjectRequest);
            return adapter;
        }
    }
}
