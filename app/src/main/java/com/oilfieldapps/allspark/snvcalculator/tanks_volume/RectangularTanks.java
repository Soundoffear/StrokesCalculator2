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

public class RectangularTanks extends Fragment {

    // Data in
    private EditText et_height, et_width, et_length, et_fluid_level;

    // Data out
    private TextView tv_total_volume, tv_fluid_volume;

    //Units
    private TextView height_units, width_units, length_units, fluid_level_units;
    private TextView total_volume_units, fluid_volume_units;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.tanks_volume_rectangular_tanks, null);

        et_height = layoutView.findViewById(R.id.tank_vol_rect_height_input);
        et_width = layoutView.findViewById(R.id.tank_vol_rect_width_input);
        et_length = layoutView.findViewById(R.id.tank_vol_rect_length_input);
        et_fluid_level = layoutView.findViewById(R.id.tank_vol_rect_fluid_level_input);

        tv_total_volume = layoutView.findViewById(R.id.tank_vol_rect_total_volume_result);
        tv_fluid_volume = layoutView.findViewById(R.id.tank_vol_rect_fluid_volume_result);

        Button calculate = layoutView.findViewById(R.id.tank_volume_rect_calculate);
        Button clear = layoutView.findViewById(R.id.tank_volume_rect_clear);

        height_units = layoutView.findViewById(R.id.tank_vol_rect_height_units);
        width_units = layoutView.findViewById(R.id.tank_vol_rect_width_units);
        length_units = layoutView.findViewById(R.id.tank_vol_rect_length_units);
        fluid_level_units = layoutView.findViewById(R.id.tank_vol_rect_fluid_level_units);
        total_volume_units = layoutView.findViewById(R.id.tank_vol_rect_total_volume_result_units);
        fluid_volume_units = layoutView.findViewById(R.id.tank_vol_rect_fluid_volume_result_units);

        final String oldLHW_unit = getResources().getString(R.string.in);

        SetAllUnits();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double height = Double.parseDouble(et_height.getText().toString());
                    height = Converter.diameterConverter(height_units.getText().toString(), oldLHW_unit, height);
                    double width = Double.parseDouble(et_width.getText().toString());
                    width = Converter.diameterConverter(width_units.getText().toString(), oldLHW_unit, width);
                    double length = Double.parseDouble(et_length.getText().toString());
                    length = Converter.diameterConverter(length_units.getText().toString(), oldLHW_unit, length);
                    double fluid_level = Double.parseDouble(et_fluid_level.getText().toString());
                    fluid_level = Converter.diameterConverter(fluid_level_units.getText().toString(), oldLHW_unit, fluid_level);

                    // !!! need to convert from in3 to bbl or to m3 - depending what are final results units
                    double totalVolume = height * width * length;
                    totalVolume = Converter.VolumeConverter("in3", total_volume_units.getText().toString(), totalVolume);
                    double fluidVolume = width * length * fluid_level;
                    fluidVolume = Converter.VolumeConverter("in3", fluid_volume_units.getText().toString(), fluidVolume);

                    tv_total_volume.setText(String.valueOf(RoundToTwoDec(totalVolume)));
                    tv_fluid_volume.setText(String.valueOf(RoundToTwoDec(fluidVolume)));
                } catch (NumberFormatException nfe) {
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
                et_height.setText(getResources().getString(R.string.zero));
                et_width.setText(getResources().getString(R.string.zero));
                et_length.setText(getResources().getString(R.string.zero));
                et_fluid_level.setText(getResources().getString(R.string.zero));
                tv_total_volume.setText(getResources().getString(R.string.zero));
                tv_fluid_volume.setText(getResources().getString(R.string.zero));
            }
        });

        return layoutView;
    }

    public void SetAllUnits() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String lengthUnits = sharedPreferences.getString("TANK_LENGTH_UNITS", getResources().getString(R.string.in));
        String volumeUnits = sharedPreferences.getString("TANK_VOLUME_UNITS", getResources().getString(R.string.bbl));

        height_units.setText(lengthUnits);
        width_units.setText(lengthUnits);
        length_units.setText(lengthUnits);
        fluid_level_units.setText(lengthUnits);
        total_volume_units.setText(volumeUnits);
        fluid_volume_units.setText(volumeUnits);
    }

    @Override
    public void onResume() {
        super.onResume();
        SetAllUnits();
    }

    private double RoundToTwoDec(double numberToRound) {
        double roundedNumber = numberToRound * 100;
        roundedNumber = Math.round(roundedNumber);
        roundedNumber = roundedNumber/100;
        return roundedNumber;
    }
}
