package com.oilfieldapps.allspark.snvcalculator.data_in;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.custom_adapters.DrillStringInputAdapter;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Data;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_DataBase;
import com.oilfieldapps.allspark.snvcalculator.interfaces.OnClickRecyclerViewListener;

import java.util.ArrayList;
import java.util.List;


public class DSDataDisplay extends Fragment implements OnClickRecyclerViewListener {

    public RecyclerView recView_dsData;
    public DrillStringInputAdapter drillStringInputAdapter;
    public List<Annulus_Data> annulusDataList;
    private ConstraintLayout constraintLayout;
    private Annulus_DataBase annulus_dataBase;

    private CheckBox fromBit_CheckBox;
    private PopupWindow ds_popupWindow;
    private Annulus_Data annulusData;

    private String diameter_chosen_units;
    private String length_chosen_units;

    public DSDataDisplay() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ds_data, container, false);

        recView_dsData = view.findViewById(R.id.recView_dsData);
        recView_dsData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recView_dsData.setLayoutManager(linearLayoutManager);
        FloatingActionButton ds_add_fab = view.findViewById(R.id.dsData_input_fab);
        constraintLayout = view.findViewById(R.id.ds_const_layout);
        fromBit_CheckBox = view.findViewById(R.id.cb_startInputFromBit);
        annulus_dataBase = new Annulus_DataBase(getContext());
        annulusDataList = new ArrayList<>();
        annulusDataList = annulus_dataBase.getAllItems();

        fromBit_CheckBox.setChecked(restoreCheckBoxState());

        fromBit_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveCheckBoxState(isChecked);
                if (isChecked) {
                    List<Annulus_Data> invertedAnnulusData = new ArrayList<>();
                    for (int i = (annulusDataList.size() - 1); i >= 0; i--) {
                        invertedAnnulusData.add(annulusDataList.get(i));
                    }
                    annulusDataList = invertedAnnulusData;
                    drillStringInputAdapter = new DrillStringInputAdapter(annulusDataList, null);
                    recView_dsData.setAdapter(drillStringInputAdapter);
                } else {
                    annulusDataList = new ArrayList<>();
                    annulusDataList = annulus_dataBase.getAllItems();
                    drillStringInputAdapter = new DrillStringInputAdapter(annulusDataList, null);
                    recView_dsData.setAdapter(drillStringInputAdapter);
                }
            }
        });

        if (fromBit_CheckBox.isChecked()) {
            List<Annulus_Data> invertedAnnulusData = new ArrayList<>();
            for (int i = (annulusDataList.size() - 1); i >= 0; i--) {
                invertedAnnulusData.add(annulusDataList.get(i));
            }
            annulusDataList = invertedAnnulusData;
        }

        drillStringInputAdapter = new DrillStringInputAdapter(annulusDataList, this);
        recView_dsData.setAdapter(drillStringInputAdapter);

        ds_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDS_PopUp();
            }
        });

        return view;
    }


    public static final String BIT_CHECK_BOX_STATE = "bit-checkbox";

    private void saveCheckBoxState(boolean isChecked) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putBoolean(BIT_CHECK_BOX_STATE, isChecked);
        sEditor.apply();
    }

    private boolean restoreCheckBoxState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isCheckedBox = sharedPreferences.getBoolean(BIT_CHECK_BOX_STATE, false);
        Toast.makeText(getContext(), String.valueOf(isCheckedBox), Toast.LENGTH_SHORT).show();
        return isCheckedBox;
    }

    private void CreateDS_PopUp() {

        //TODO Add option 'calculate remaining Drill String length' to force calculation to bottom

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popUpView = inflater.inflate(R.layout.popup_window_drill_string_add_new, null);

        ds_popupWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (Build.VERSION.SDK_INT >= 21) {
            ds_popupWindow.setElevation(10);
        }

        Button addBTN = popUpView.findViewById(R.id.drill_string_add_item_button);
        Button dismissBTN = popUpView.findViewById(R.id.drill_string_dismiss_item_button);

        final EditText ds_name = popUpView.findViewById(R.id.drill_string_et_input_name);
        final EditText ds_id = popUpView.findViewById(R.id.drill_string_et_input_ds_id);
        final EditText ds_od = popUpView.findViewById(R.id.drill_string_et_input_ds_od);
        final EditText ds_length = popUpView.findViewById(R.id.drill_string_et_input_ds_length);
        // Set proper units in PopUp window
        TextView ds_id_units = popUpView.findViewById(R.id.drill_string_et_input_ds_id_unit);
        TextView ds_od_units = popUpView.findViewById(R.id.drill_string_et_input_ds_od_unit);
        TextView ds_length_units = popUpView.findViewById(R.id.drill_string_et_input_ds_length_unit);

        //get units from preferences
        SharedPreferences unitsPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        diameter_chosen_units = unitsPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        length_chosen_units = unitsPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));
        ds_id_units.setText(diameter_chosen_units);
        ds_od_units.setText(diameter_chosen_units);
        ds_length_units.setText(length_chosen_units);

        ds_popupWindow.setFocusable(true);
        ds_popupWindow.update();

        ds_popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        dismissBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ds_popupWindow.dismiss();
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfValueIn(ds_name) && checkIfValueIn(ds_id) && checkIfValueIn(ds_od) && checkIfValueIn(ds_length)) {
                    try {
                        String name = ds_name.getText().toString();
                        String id = ds_id.getText().toString();
                        String od = ds_od.getText().toString();
                        String length = ds_length.getText().toString();
                        annulusData = new Annulus_Data(name, id, od, length, diameter_chosen_units, length_chosen_units);
                        annulus_dataBase.addItemIntoDS_DB(annulusData);
                        if (!fromBit_CheckBox.isChecked()) {
                            annulusDataList.add(annulusData);
                        } else {
                            annulusDataList.add(0, annulusData);
                        }
                        drillStringInputAdapter = new DrillStringInputAdapter(annulusDataList, DSDataDisplay.this);
                        recView_dsData.setAdapter(drillStringInputAdapter);
                        ds_popupWindow.dismiss();
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
            }
        });

    }

    private boolean checkIfValueIn(EditText dataInput) {
        if (TextUtils.isEmpty(dataInput.getText().toString())) {
            dataInput.setError("Please enter value");
            return false;
        } else {
            dataInput.setError(null);
        }
        return true;
    }

    public void UpdateData_PopUp(final int i, final String nameString, String[] oldData) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View updatePopUp = layoutInflater.inflate(R.layout.popup_window_drill_string_update_existing, null);

        ds_popupWindow = new PopupWindow(updatePopUp, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (Build.VERSION.SDK_INT >= 21) {
            ds_popupWindow.setElevation(10);
        }

        Button updateBTN = updatePopUp.findViewById(R.id.drill_string_update_item_button);
        Button cancelBTN = updatePopUp.findViewById(R.id.drill_string_dismiss_item_button);

        final EditText name = updatePopUp.findViewById(R.id.drill_string_et_update_name);
        final EditText id = updatePopUp.findViewById(R.id.drill_string_et_update_ds_id);
        final EditText od = updatePopUp.findViewById(R.id.drill_string_et_update_ds_od);
        final EditText length = updatePopUp.findViewById(R.id.drill_string_et_update_ds_length);

        // Set proper units in PopUp window
        TextView ds_id_units = updatePopUp.findViewById(R.id.drill_string_et_input_ds_id_unit);
        TextView ds_od_units = updatePopUp.findViewById(R.id.drill_string_et_input_ds_od_unit);
        TextView ds_length_units = updatePopUp.findViewById(R.id.drill_string_et_input_ds_length_unit);

        //get units from preferences
        SharedPreferences unitsPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        diameter_chosen_units = unitsPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        length_chosen_units = unitsPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));
        ds_id_units.setText(diameter_chosen_units);
        ds_od_units.setText(diameter_chosen_units);
        ds_length_units.setText(length_chosen_units);

        name.setText(nameString);
        id.setText(oldData[0]);
        od.setText(oldData[1]);
        length.setText(oldData[2]);

        ds_popupWindow.setFocusable(true);
        ds_popupWindow.update();
        ds_popupWindow.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfValueIn(name) && checkIfValueIn(id) && checkIfValueIn(od) && checkIfValueIn(length)) {
                    try {
                        String stringName = name.getText().toString();
                        String stringID = id.getText().toString();
                        String stringOD = od.getText().toString();
                        String stringLength = length.getText().toString();

                        annulusData = new Annulus_Data(stringName, stringID, stringOD, stringLength, diameter_chosen_units, length_chosen_units);
                        annulus_dataBase.updateDB(annulusData, nameString);
                        annulusDataList.get(i).setString_name(stringName);
                        annulusDataList.get(i).setString_id(stringID);
                        annulusDataList.get(i).setString_od(stringOD);
                        annulusDataList.get(i).setString_length(stringLength);
                        recView_dsData.setAdapter(drillStringInputAdapter);
                        ds_popupWindow.dismiss();
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
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ds_popupWindow.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("RESUME:", "Resuming Activity");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String diameter_units = sharedPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        String length_units = sharedPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));
        boolean checkBoxState = sharedPreferences.getBoolean(BIT_CHECK_BOX_STATE, false);

        fromBit_CheckBox.setChecked(checkBoxState);

        annulus_dataBase.updateDB_units(diameter_units, length_units);
        for (int i = 0; i < annulusDataList.size(); i++) {
            annulusDataList.get(i).setDiameter_units(diameter_units);
            annulusDataList.get(i).setLength_units(length_units);
        }
        recView_dsData.setAdapter(drillStringInputAdapter);

    }

    @Override
    public void onRecViewClickListener(View v, final int pos) {
        TextView nameTV = v.findViewById(R.id.ds_part_name);
        TextView idTV = v.findViewById(R.id.drill_string_tv_input_ds_id);
        TextView odTV = v.findViewById(R.id.drill_string_tv_input_ds_od);
        TextView lengthTV = v.findViewById(R.id.drill_string_tv_input_ds_length);
        final String name = nameTV.getText().toString();
        String idString = idTV.getText().toString();
        String odString = odTV.getText().toString();
        String lengthString = lengthTV.getText().toString();
        final String[] oldDataString = {idString, odString, lengthString};

        Log.d("DS_DATA_DISPLAY", "Start Dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("What would you like to do?");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("NAME: ", name + " pos " + String.valueOf(pos));
                UpdateData_PopUp(pos, name, oldDataString);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                annulusDataList.remove(pos);
                annulus_dataBase.deleteFromDB(name);
                recView_dsData.setAdapter(drillStringInputAdapter);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
