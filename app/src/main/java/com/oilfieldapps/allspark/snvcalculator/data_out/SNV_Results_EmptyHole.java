package com.oilfieldapps.allspark.snvcalculator.data_out;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.custom_adapters.HoleResultsAdapter;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleResultsData;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleResults_DataBase;

import java.util.List;

public class SNV_Results_EmptyHole extends Fragment {

    RecyclerView holeDataResultsRecView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_and_v_data_out_empty_hole, null);

        holeDataResultsRecView = view.findViewById(R.id.recView_results_hole);
        HoleResults_DataBase holeResults_dataBase = new HoleResults_DataBase(getContext());
        List<HoleResultsData> holeResultsDatas = holeResults_dataBase.getAllResults();

        holeDataResultsRecView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        holeDataResultsRecView.setLayoutManager(linearLayoutManager);
        HoleResultsAdapter holeResultsAdapter = new HoleResultsAdapter(getContext(), holeResultsDatas);
        holeDataResultsRecView.setAdapter(holeResultsAdapter);

        return view;
    }

}
