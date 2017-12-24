package com.oilfieldapps.allspark.strokescalculator.data_out;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.custom_adapters.DrillStringResultsAdapter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.DrillString_Results;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.DrillString_Results_DataBase;
import com.oilfieldapps.allspark.strokescalculator.data_in.DSDataDisplay;

import java.util.ArrayList;
import java.util.List;

public class SNV_Results_DrillString extends Fragment {

    //data in
    RecyclerView drill_string_results_recycler_view;

    //data out
    DrillString_Results_DataBase drillString_results_dataBase;
    List<DrillString_Results> drillStringResultsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_and_v_data_out_drill_string, null);

        drill_string_results_recycler_view = view.findViewById(R.id.recycler_view_drill_string_results);

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

        drill_string_results_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        drill_string_results_recycler_view.setLayoutManager(linearLayoutManager);
        DrillStringResultsAdapter drillStringResultsAdapter = new DrillStringResultsAdapter(getContext(), drillStringResultsList);
        drill_string_results_recycler_view.setAdapter(drillStringResultsAdapter);

        return view;
    }

    private boolean shouldBeInverted() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(DSDataDisplay.BIT_CHECK_BOX_STATE, false);
    }

}
