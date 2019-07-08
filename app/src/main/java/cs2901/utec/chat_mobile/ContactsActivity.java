package cs2901.utec.chat_mobile;

import android.app.Activity;
import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import org.json.JSONException;
import org.w3c.dom.Text;

import android.view.View;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    public String idCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        idCurrent = getIntent().getExtras().get("user_id").toString();
        mRecyclerView = findViewById(R.id.main_recycler_view);
        setTitle(getIntent().getExtras().get("name").toString() + " " + getIntent().getExtras().get("fullname").toString() + " (@" + getIntent().getExtras().get("username").toString() + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUsers();
    }

    public Activity getActivity() {
        return this;
    }

    public void getUsers() {
        String url = "http://10.0.2.2:8080/users";
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap();
        JSONObject parameters = new JSONObject(params);
        final String userId = getIntent().getExtras().get("user_id").toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("usuarios");
                            mAdapter = new ChatAdapter(data, getActivity(), userId);
                            mRecyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },  new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
        queue.add(jsonObjectRequest);
    }

}
