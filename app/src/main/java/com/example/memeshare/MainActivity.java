package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String currentUrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadMeme();
    }
    private void loadMeme(){
        ProgressBar p1=(ProgressBar)findViewById(R.id.progressBar);
        p1.setVisibility(View.VISIBLE);
// ...

// Instantiate the RequestQueue.
        String url ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    final ImageView i1=(ImageView)findViewById(R.id.memeImageView);
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            currentUrl=response.getString("url");
                            Glide.with(MainActivity.this).load(currentUrl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    p1.setVisibility(View.INVISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    p1.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            }).into(i1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void nextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) {
        Intent i1=new Intent(Intent.ACTION_SEND);
        i1.setType("text/plain");
        i1.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this cool meme "+currentUrl);
        Intent choose=Intent.createChooser(i1,"Share this meme using....");
        startActivity(choose);
    }
}