package com.oilfieldapps.allspark.strokescalculator.data_out;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.custom_adapters.DrillString_results_adapter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.DrillString_Results;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.DrillString_Results_DataBase;
import com.oilfieldapps.allspark.strokescalculator.data_in.DSDataDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allspark on 10/09/2017.
 */

public class SNV_Results_DrillString extends Fragment {

    //data in
    ListView drill_string_results_list_view;

    //data out
    DrillString_Results_DataBase drillString_results_dataBase;
    DrillString_results_adapter drillString_results_adapter;
    List<DrillString_Results> drillStringResultsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_and_v_data_out_drill_string, null);

        drill_string_results_list_view = view.findViewById(R.id.list_view_drill_string_results);

        drillString_results_dataBase = new DrillString_Results_DataBase(getContext());
        drillStringResultsList = drillString_results_dataBase.getAllItems();

        if(shouldBeInverted()){
            List<DrillString_Results> invertedDSResults = new ArrayList<>();
            for(int i = (drillStringResultsList.size() - 2); i >= 0; i--) {
                invertedDSResults.add(drillStringResultsList.get(i));
            }
            invertedDSResults.add(drillStringResultsList.get(drillStringResultsList.size() -1));
            drillStringResultsList = invertedDSResults;
        }

        drillString_results_adapter = new DrillString_results_adapter(getContext(), drillStringResultsList);
        drill_string_results_list_view.setAdapter(drillString_results_adapter);

        return view;
    }

    private boolean shouldBeInverted() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isChecked = sharedPreferences.getBoolean(DSDataDisplay.BIT_CHECK_BOX_STATE, false);
        return isChecked;
    }

}
