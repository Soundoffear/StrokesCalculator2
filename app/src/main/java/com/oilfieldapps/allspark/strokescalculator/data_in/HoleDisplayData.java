package com.oilfieldapps.allspark.strokescalculator.data_in;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.custom_adapters.HoleDataInputAdapter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData_DataBase;
import com.oilfieldapps.allspark.strokescalculator.interfaces.OnClickRecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

public class HoleDisplayData extends Fragment implements OnClickRecyclerViewListener {

    public HoleDisplayData() {

    }

    public List<HoleData> itemsData;
    public RecyclerView recViewData;
    public HoleDataInputAdapter holeDataInputAdapter;
    public FloatingActionButton fabButton;
    public ConstraintLayout constLayout;
    private HoleData holeData;
    private HoleData_DataBase holeData_dataBase;
    private Context tContext;
    private PopupWindow tPopUpWindow;

    private String diameter_chosen_units;
    private String length_chosen_units;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hole_data, container, false);

        recViewData = view.findViewById(R.id.recView_holeData);
        recViewData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recViewData.setLayoutManager(linearLayoutManager);
        fabButton = view.findViewById(R.id.floatingActionButton_hole_data);
        constLayout = view.findViewById(R.id.hole_layout);
        holeData_dataBase = new HoleData_DataBase(getContext());
        tContext = getContext();
        itemsData = new ArrayList<>();
        itemsData = holeData_dataBase.getAllItem();
        holeDataInputAdapter = new HoleDataInputAdapter(itemsData, this);
        recViewData.setAdapter(holeDataInputAdapter);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreatePopUp();

            }
        });

        return view;
    }

    private void CreatePopUp() {
        LayoutInflater inflater = (LayoutInflater) tContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popUpView = inflater.inflate(R.layout.popup_window_layout_hole, null);

        tPopUpWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(Build.VERSION.SDK_INT >= 21) {
            tPopUpWindow.setElevation(10);
        }

        Button addButton = popUpView.findViewById(R.id.add_data);
        Button dismissButton = popUpView.findViewById(R.id.dismiss);

        final EditText inputName = popUpView.findViewById(R.id.input_name);
        final EditText inputID = popUpView.findViewById(R.id.input_ID);
        final EditText inputOD = popUpView.findViewById(R.id.input_OD);
        final EditText input_EndMD = popUpView.findViewById(R.id.input_end_md);
        final EditText input_TopMD = popUpView.findViewById(R.id.input_top_md);

        TextView idUnits = popUpView.findViewById(R.id.input_ID_units);
        TextView odUnits = popUpView.findViewById(R.id.input_OD_units);
        TextView end_MD_units = popUpView.findViewById(R.id.input_end_md_units);
        TextView top_MD_units = popUpView.findViewById(R.id.input_top_md_units);

        SharedPreferences unitsPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        diameter_chosen_units = unitsPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        length_chosen_units = unitsPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));
        idUnits.setText(diameter_chosen_units);
        odUnits.setText(diameter_chosen_units);
        end_MD_units.setText(length_chosen_units);
        top_MD_units.setText(length_chosen_units);

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPopUpWindow.dismiss();
            }
        });

        tPopUpWindow.setFocusable(true);
        tPopUpWindow.update();

        tPopUpWindow.showAtLocation(constLayout, Gravity.CENTER, 0, 0);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String string_inputName = inputName.getText().toString();
                    String string_inputID = inputID.getText().toString();
                    String string_inputOD = inputOD.getText().toString();
                    String string_inputEndMD = input_EndMD.getText().toString();
                    String string_inputTopMD = input_TopMD.getText().toString();

                    holeData = new HoleData(string_inputName, string_inputID, string_inputOD, string_inputEndMD, string_inputTopMD, diameter_chosen_units, length_chosen_units);
                    holeData_dataBase.addItemToHoleDesign(holeData);
                    itemsData.add(holeData);
                    recViewData.setAdapter(holeDataInputAdapter);
                    tPopUpWindow.dismiss();
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
    }
    private void UpdateData(final String name, final int i, String[] oldData) {
        LayoutInflater inflater = (LayoutInflater) tContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popUpView = inflater.inflate(R.layout.popup_window_layout_hole_update, null);

        tPopUpWindow = new PopupWindow(popUpView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(Build.VERSION.SDK_INT >= 21) {
            tPopUpWindow.setElevation(10);
        }

        Button addButton = popUpView.findViewById(R.id.add_data);
        Button dismissButton = popUpView.findViewById(R.id.dismiss);

        final EditText inputName = popUpView.findViewById(R.id.input_name);
        final EditText inputID = popUpView.findViewById(R.id.input_ID);
        final EditText inputOD = popUpView.findViewById(R.id.input_OD);
        final EditText input_EndMD = popUpView.findViewById(R.id.input_end_md);
        final EditText input_TopMD = popUpView.findViewById(R.id.input_top_md);
        inputName.setText(name);
        inputID.setText(oldData[0]);
        inputOD.setText(oldData[1]);
        input_TopMD.setText(oldData[2]);
        input_EndMD.setText(oldData[3]);

        TextView idUnits = popUpView.findViewById(R.id.input_ID_units);
        TextView odUnits = popUpView.findViewById(R.id.input_OD_units);
        TextView end_MD_units = popUpView.findViewById(R.id.input_end_md_units);
        TextView top_MD_units = popUpView.findViewById(R.id.input_top_md_units);

        SharedPreferences unitsPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        diameter_chosen_units = unitsPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        length_chosen_units = unitsPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));
        idUnits.setText(diameter_chosen_units);
        odUnits.setText(diameter_chosen_units);
        end_MD_units.setText(length_chosen_units);
        top_MD_units.setText(length_chosen_units);

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPopUpWindow.dismiss();
            }
        });

        tPopUpWindow.setFocusable(true);
        tPopUpWindow.update();

        tPopUpWindow.showAtLocation(constLayout, Gravity.CENTER, 0, 0);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String string_inputName = inputName.getText().toString();
                    String string_inputID = inputID.getText().toString();
                    String string_inputOD = inputOD.getText().toString();
                    String string_inputEndMD = input_EndMD.getText().toString();
                    String string_inputTopMD = input_TopMD.getText().toString();

                    holeData = new HoleData(string_inputName, string_inputID, string_inputOD, string_inputEndMD, string_inputTopMD, diameter_chosen_units, length_chosen_units);
                    holeData_dataBase.updateDatabase(holeData, name);
                    itemsData.get(i).setName(string_inputName);
                    itemsData.get(i).setInput_id(string_inputID);
                    itemsData.get(i).setInput_od(string_inputOD);
                    itemsData.get(i).setInput_end_md(string_inputEndMD);
                    itemsData.get(i).setInput_top_md(string_inputTopMD);
                    recViewData.setAdapter(holeDataInputAdapter);
                    tPopUpWindow.dismiss();
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
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences unitsPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        diameter_chosen_units = unitsPreferences.getString("SNV_DIAMETER_UNITS", getResources().getString(R.string.in));
        length_chosen_units = unitsPreferences.getString("SNV_LENGTH_UNITS", getResources().getString(R.string.feet));

        holeData_dataBase.updateUnits(diameter_chosen_units, length_chosen_units);
        for(int i =0 ; i < itemsData.size(); i++) {
            itemsData.get(i).setInput_diameter_unit(diameter_chosen_units);
            itemsData.get(i).setInput_length_unit(length_chosen_units);
        }
        recViewData.setAdapter(holeDataInputAdapter);
    }

    @Override
    public void onRecViewClickListener(View v, final int pos) {
        TextView nameTV = v.findViewById(R.id.itemNameLV);
        TextView idTV = v.findViewById(R.id.input_ID_LV_ET);
        TextView odTV = v.findViewById(R.id.input_OD_LV_ET);
        TextView topMD = v.findViewById(R.id.input_top_md_TV);
        TextView endMD = v.findViewById(R.id.input_end_md_TV);
        final String name = nameTV.getText().toString();
        String idString = idTV.getText().toString();
        String odString = odTV.getText().toString();
        String topMDString = topMD.getText().toString();
        String endMDString = endMD.getText().toString();
        final String[] oldDataStrings = {idString, odString, topMDString, endMDString};

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
                UpdateData(name, pos, oldDataStrings);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holeData_dataBase.removeFromDatabase(name);
                itemsData.remove(pos);
                recViewData.setAdapter(holeDataInputAdapter);
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
