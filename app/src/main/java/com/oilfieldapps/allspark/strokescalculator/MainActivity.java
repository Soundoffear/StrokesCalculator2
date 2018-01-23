package com.oilfieldapps.allspark.strokescalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    Toolbar main_toolbar;

    ImageButton pump_output_btn;
    ImageButton snv_btn;
    ImageButton av_btn;
    ImageButton tank_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        AdView mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FirstRunApp();

        main_toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(main_toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.app_name));
        main_toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));

        pump_output_btn = findViewById(R.id.pump_output);
        snv_btn = findViewById(R.id.strokes_and_volume_calculations);
        av_btn = findViewById(R.id.annular_velocity);
        tank_button = findViewById(R.id.tank_volume);

        pump_output_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output_intent = new Intent(MainActivity.this, PumpOutput.class);
                startActivity(output_intent);
            }
        });

        snv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent snv_intent = new Intent(MainActivity.this, SandV_Free.class);
                startActivity(snv_intent);
            }
        });

        av_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent av_intent = new Intent(MainActivity.this, AnnularVelocity.class);
                startActivity(av_intent);
            }
        });

        tank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tank_intent = new Intent(MainActivity.this, Tanks_Volume.class);
                startActivity(tank_intent);
            }
        });
    }

    private void FirstRunApp() {

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(sharedPreferences.getBoolean("FIRST_RUN", true)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Please Read");
            builder.setMessage(R.string.first_run_message);
            builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRST_RUN", false);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("FIRST_RUN", true);
                    editor.apply();
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
