package com.oilfieldapps.allspark.snvcalculator.tanks_volume;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.converters.Converter;

public class VerticalCylindricalTank extends Fragment {

    // data in
    private EditText et_diameter, et_height, et_fluid_level;

    // data out
    private TextView tv_total_volume, tv_fluid_volume;

    // units
    private TextView diameter_units, height_units, fluid_level_units, total_volume_units, fluid_volume_units;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.tanks_volume_ver_cylindrical_tank, null);

        et_diameter = layoutView.findViewById(R.id.tank_vol_vc_diameter_input);
        et_height = layoutView.findViewById(R.id.tank_vol_vc_length_input);
        et_fluid_level = layoutView.findViewById(R.id.tank_vol_vc_fluid_level_input);

        Button calculate = layoutView.findViewById(R.id.tank_volume_vc_calculate);
        Button clear = layoutView.findViewById(R.id.tank_volume_vc_clear);

        tv_total_volume = layoutView.findViewById(R.id.tank_vol_vc_total_volume_result);
        tv_fluid_volume = layoutView.findViewById(R.id.tank_vol_vc_fluid_volume_result);

        diameter_units = layoutView.findViewById(R.id.tank_vol_vc_diameter_units);
        height_units = layoutView.findViewById(R.id.tank_vol_vc_length_units);
        fluid_level_units = layoutView.findViewById(R.id.tank_vol_vc_fluid_level_units);
        total_volume_units = layoutView.findViewById(R.id.tank_vol_vc_total_volume_result_units);
        fluid_volume_units = layoutView.findViewById(R.id.tank_vol_vc_fluid_volume_result_units);

        SetAllUnits();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double diameter = Double.parseDouble(et_diameter.getText().toString());
                    diameter = Converter.diameterConverter(diameter_units.getText().toString(), getResources().getString(R.string.in), diameter);
                    double height = Double.parseDouble(et_height.getText().toString());
                    height = Converter.diameterConverter(height_units.getText().toString(), getResources().getString(R.string.in), height);
                    double fluid_level = Double.parseDouble(et_fluid_level.getText().toString());
                    fluid_level = Converter.diameterConverter(fluid_level_units.getText().toString(), getResources().getString(R.string.in), fluid_level);

                    // !!! need to convert from in3 to bbl or to m3 - depending what are final results units
                    double totalVolume = Math.PI * Math.pow(diameter, 2) / 4 * height;
                    totalVolume = Converter.VolumeConverter("in3", total_volume_units.getText().toString(), totalVolume);
                    double fluidVolume = Math.PI * Math.pow(diameter, 2) / 4 * fluid_level;
                    fluidVolume = Converter.VolumeConverter("in3", fluid_level_units.getText().toString(), fluidVolume);

                    String totalVolumeString = String.valueOf(RoundToTwoDec(totalVolume));
                    String fluidVolumeString = String.valueOf(RoundToTwoDec(fluidVolume));

                    tv_total_volume.setText(totalVolumeString);
                    tv_fluid_volume.setText(fluidVolumeString);
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Warning");
                    builder.setMessage("Please check if all the field contains numbers");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zero = getResources().getString(R.string.zero);
                et_diameter.setText(zero);
                et_height.setText(zero);
                et_fluid_level.setText(zero);
                tv_total_volume.setText(zero);
                tv_fluid_volume.setText(zero);
            }
        });

        return layoutView;
    }

    private double RoundToTwoDec(double numberToRound) {
        double roundedNumber = numberToRound * 100;
        roundedNumber = Math.round(roundedNumber);
        roundedNumber = roundedNumber/100;
        return roundedNumber;
    }

    private void SetAllUnits() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String diameterUnits = sharedPreferences.getString("TANK_DIAMETER_UNITS", getResources().getString(R.string.in));
        String lengthUnits = sharedPreferences.getString("TANK_LENGTH_UNITS", getResources().getString(R.string.in));
        String volumeUnits = sharedPreferences.getString("TANK_VOLUME_UNITS", getResources().getString(R.string.bbl));

        diameter_units.setText(diameterUnits);
        height_units.setText(lengthUnits);
        fluid_level_units.setText(lengthUnits);
        total_volume_units.setText(volumeUnits);
        fluid_volume_units.setText(volumeUnits);

    }

    @Override
    public void onResume() {
        super.onResume();
        SetAllUnits();
    }
}
