package com.oilfieldapps.allspark.snvcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.oilfieldapps.allspark.snvcalculator.menus.TankVol_Menu;
import com.oilfieldapps.allspark.snvcalculator.tanks_volume.HorizontalCyrindricalTanks;
import com.oilfieldapps.allspark.snvcalculator.tanks_volume.RectangularTanks;
import com.oilfieldapps.allspark.snvcalculator.tanks_volume.VerticalCylindricalTank;

public class Tanks_Volume extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tanks_volume_main);

        AdView adView = findViewById(R.id.tanks_vol_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Toolbar toolbar = findViewById(R.id.tanks_volume_toolbar);
        TabLayout tabLayout = findViewById(R.id.tanks_volume_tabLayout);
        ViewPager viewPager = findViewById(R.id.tanks_volume_view_pager);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Tanks Volume");

        viewPager.setAdapter(new ViewPagerAdapterTanks(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tank_volume_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tank_units_menu) {
            Intent tank_menu_intent = new Intent(Tanks_Volume.this, TankVol_Menu.class);
            startActivity(tank_menu_intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapterTanks extends FragmentPagerAdapter {

        ViewPagerAdapterTanks(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new HorizontalCyrindricalTanks();
                case 1:
                    return new VerticalCylindricalTank();
                case 2:
                    return new RectangularTanks();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Horizontal Cylindrical Tank";
                case 1:
                    return "Vertical Cylindrical Tank";
                case 2:
                    return "Rectangular Tank";
            }

            return null;
        }
    }


}
