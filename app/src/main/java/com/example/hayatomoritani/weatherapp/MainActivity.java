package com.example.hayatomoritani.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hayatomoritani.weatherapp.DragAndDrop.DragAndDropMain;
import com.example.hayatomoritani.weatherapp.GoogleSuggest.Suggests;
import com.example.hayatomoritani.weatherapp.data.Channel;
import com.example.hayatomoritani.weatherapp.data.Item;
import com.example.hayatomoritani.weatherapp.musicMode.MusicMain;
import com.example.hayatomoritani.weatherapp.service.WeatherServiceCallback;
import com.example.hayatomoritani.weatherapp.service.YahooWeatherService;
import com.example.hayatomoritani.weatherapp.tabDemo1.ShinTabMode;
import com.example.hayatomoritani.weatherapp.tabDemo2.TabMode;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback{

    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private YahooWeatherService service;
    private WebView webView;
    private EditText urlText;
    private Button goButton;
    private Button suggestButton;
    private Button dragModeButton;
    private Button tabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView)findViewById(R.id.ConditionTextView);
        locationTextView =(TextView)findViewById(R.id.locationTextView);

        service = new YahooWeatherService(this);

        service.refreshWeather("Seattle, WA");
        urlText = (EditText) findViewById(R.id.url_field);
        goButton = (Button) findViewById(R.id.go_button);
        suggestButton = (Button)findViewById(R.id.suggestButton);
        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Suggests.class);
                startActivity(i);
            }
        });

        dragModeButton = (Button)findViewById(R.id.DragNDropButton);
        dragModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DragAndDropMain.class);
                startActivity(i);
            }
        });
        // Setup event handlers
        goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openBrowser();
            }
        });
        urlText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    openBrowser();
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        webView = (WebView) findViewById(R.id.web_view);
    }

    private void openBrowser() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlText.getText().toString());
    }

    public void accessToYahooSite(View view){
        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse("https://www.yahoo.com/?ilc=401"));
        startActivity(myWebLink);
    }

    public void tabButtonClicked(View view){
        Intent tab = new Intent(this, ShinTabMode.class);
        startActivity(tab);
    }

    public void musicClicked(View view){
        Intent tab = new Intent(this, MusicMain.class);
        startActivity(tab);
    }

    @Override
    public void serviceSuccess(Channel channel) {

        Item item = channel.getItem();

        locationTextView.setText(service.getLocation());
        temperatureTextView.setText(item.getCondition().getTemperature()+" \u00b0 "+channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
    }

    @Override
    public void serviceFailure(Exception e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);

    }
}
