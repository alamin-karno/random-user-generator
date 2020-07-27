package com.alaminkarno.randomuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button userGeneratorBTN;
    private RequestQueue requestQueue;
    private String URL;
    private TextView nameTV,emailTV,addressTV,phoneTV,birthDayTv;
    private CircleImageView circleImageView;
    private String name,email,address,birthday,phone,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        JSONObjectRequest();

        userGeneratorBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObjectRequest();
            }
        });

        nameTV.setOnClickListener(this);
        emailTV.setOnClickListener(this);
        addressTV.setOnClickListener(this);
        phoneTV.setOnClickListener(this);
        birthDayTv.setOnClickListener(this);

    }

    private void JSONObjectRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray resultsArray = response.getJSONArray("results");

                    JSONObject jsonObject = resultsArray.getJSONObject(0);

                    JSONObject nameObject = jsonObject.getJSONObject("name");
                    name = nameObject.getString("title")+" "+nameObject.getString("first")+" "+nameObject.getString("last");
                    nameTV.setText(name);

                    email = jsonObject.getString("email");
                    emailTV.setText(email);

                    JSONObject dateObject = jsonObject.getJSONObject("dob");
                    birthday = dateObject.getString("age")+" years";
                    birthDayTv.setText(birthday);

                    JSONObject locationObject = jsonObject.getJSONObject("location");
                    JSONObject streetObject = locationObject.getJSONObject("street");
                    address = streetObject.getString("number")+" "+locationObject.getString("city")+", "+locationObject.getString("state")+"-"+
                            locationObject.getString("postcode")+", "+locationObject.getString("country");
                    addressTV.setText(address);

                    phone = jsonObject.getString("phone");
                    phoneTV.setText(phone);

                    JSONObject pictureObject = jsonObject.getJSONObject("picture");
                    image = pictureObject.getString("large");
                    Picasso.get().load(image).into(circleImageView);

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void init() {
        nameTV = findViewById(R.id.userNameTV);
        emailTV = findViewById(R.id.userMailTV);
        addressTV = findViewById(R.id.userAddressTV);
        birthDayTv = findViewById(R.id.userBirthDayTV);
        phoneTV = findViewById(R.id.userPhoneTV);
        circleImageView = findViewById(R.id.profile_image);
        userGeneratorBTN = findViewById(R.id.userGeneratorBTN);
        requestQueue = Volley.newRequestQueue(this);
        URL = "https://randomuser.me/api/";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userNameTV:
                CopyText(nameTV.getText().toString());
                break;
            case R.id.userMailTV:
                CopyText(emailTV.getText().toString());
                break;
            case R.id.userAddressTV:
                CopyText(addressTV.getText().toString());
                break;
            case R.id.userBirthDayTV:
                CopyText(birthDayTv.getText().toString());
                break;
            case R.id.userPhoneTV:
                CopyText(phoneTV.getText().toString());
                break;
        }
    }

    private void CopyText(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text",text);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show();
    }
}