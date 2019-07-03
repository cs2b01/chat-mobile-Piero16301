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

    private RecyclerView mRecyclerView;
    private list_adapter mExampleAdapter;
    private ArrayList<ListContact> mExampleList;
    //private TextView mTextViewResult;
    private RequestQueue mQueue;
    public int idCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //mTextViewCurrent = findViewById(R.id.current_user);
        //mTextViewResult = findViewById(R.id.text_view_result);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(this);

        jsonCurent();
        jsonParse();
    }

    private void jsonCurent() {
        String url = "http://10.0.2.2:8080/current";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            idCurrent = response.getInt("id");
                            String name = response.getString("name");
                            String fullname = response.getString("fullname");
                            setTitle(name + " " + fullname);
                            //mTextViewCurrent.append("Bienvenido " + name + " " + fullname + "\n\n\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void jsonParse() {
        String url = "http://10.0.2.2:8080/users";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                if (user.getInt("id") != idCurrent) {
                                    String name = user.getString("name");
                                    String fullname = user.getString("fullname");
                                    //System.out.println(user.getString("id" + "\n"));
                                    String usuario = name + " " + fullname;

                                    mExampleList.add(new ListContact(usuario));
                                }
                            }
                            mExampleAdapter = new list_adapter(ContactsActivity.this, mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

}
