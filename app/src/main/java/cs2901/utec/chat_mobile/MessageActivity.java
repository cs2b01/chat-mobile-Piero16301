package cs2901.utec.chat_mobile;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String username = getIntent().getExtras().get("username").toString();
        setTitle(username);
        mRecyclerView = findViewById(R.id.main_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getChats();
    }

    public void onClickBtnSend(View v) {
        TextView inputText = (TextView)findViewById(R.id.txtMessage);
        postMessage();
        inputText.setText("");
        getChats();
    }

    public void getChats() {
        final String userFromId = getIntent().getExtras().get("user_from_id").toString();
        final String userToId = getIntent().getExtras().get("user_to_id").toString();
        String url = "http://10.0.2.2:8080/chats/<user_from_id>/<user_to_id>";
        url = url.replace("<user_from_id>", userFromId);
        url = url.replace("<user_to_id>", userToId);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("mensajes");

                            JSONArray arraySorted = new JSONArray();

                            List<JSONObject> values = new ArrayList<JSONObject>();
                            for (int i = 0; i < data.length(); i++) {
                                values.add(data.getJSONObject(i));
                            }

                            Collections.sort(values, new Comparator<JSONObject>() {
                                private static final String NAME = "id";

                                @Override
                                public int compare(JSONObject o1, JSONObject o2) {
                                    int value1 = new Integer(1);
                                    int value2 = new Integer(1);

                                    try {
                                        value1 = o1.getInt(NAME);
                                        value2 = o2.getInt(NAME);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (value1 < value2) {
                                        return -1;
                                    } else if (value1 > value2) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });

                            for (int i = 0; i < data.length(); i++) {
                                arraySorted.put(values.get(i));
                            }

                            int uID = Integer.parseInt(userFromId);
                            mAdapter = new MyMessageAdapter(arraySorted, getActivity(), uID);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        queue.add(request);
    }

    public void postMessage() {
        String url = "http://10.0.2.2:8080/messages";
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap();
        final String user_from_id = getIntent().getExtras().get("user_from_id").toString();
        final String user_to_id = getIntent().getExtras().get("user_to_id").toString();
        final String content = ((EditText)findViewById(R.id.txtMessage)).getText().toString();
        params.put("user_from_id", user_from_id);
        params.put("user_to_id", user_to_id);
        params.put("content", content);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getChats();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }

}
