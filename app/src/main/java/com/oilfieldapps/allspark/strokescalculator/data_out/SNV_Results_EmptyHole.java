package com.oilfieldapps.allspark.strokescalculator.data_out;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.converters.Converter;
import com.oilfieldapps.allspark.strokescalculator.custom_adapters.Hole_results_no_pipe_adapter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData_DataBase;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleResultsData;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleResults_DataBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allspark on 07/09/2017.
 */

public class SNV_Results_EmptyHole extends Fragment {

    ListView holeDataResultsListView;

    Hole_results_no_pipe_adapter hole_results_no_pipe_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_and_v_data_out_empty_hole, null);

        holeDataResultsListView = view.findViewById(R.id.listView_results_hole);

        HoleResults_DataBase holeResults_dataBase = new HoleResults_DataBase(getContext());

        List<HoleResultsData> holeResultsDatas = holeResults_dataBase.getAllResults();

        hole_results_no_pipe_adapter = new Hole_results_no_pipe_adapter(getContext(), holeResultsDatas);
        holeDataResultsListView.setAdapter(hole_results_no_pipe_adapter);

        return view;
    }

}
