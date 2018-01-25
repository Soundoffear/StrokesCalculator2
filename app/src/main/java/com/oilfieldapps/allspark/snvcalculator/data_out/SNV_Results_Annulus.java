package com.oilfieldapps.allspark.snvcalculator.data_out;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.custom_adapters.AnnularResultsAdapter;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Results;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Results_DataBase;

import java.util.List;


public class SNV_Results_Annulus extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_and_v_data_out_annular, null);

        RecyclerView recyclerView = view.findViewById(R.id.annular_out_recycler_view);

        Annulus_Results_DataBase annulusResults_dataBase = new Annulus_Results_DataBase(getContext());

        List<Annulus_Results> annulusResultsList = annulusResults_dataBase.getAllItem();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        AnnularResultsAdapter annularResultsAdapter = new AnnularResultsAdapter(getContext(), annulusResultsList);
        recyclerView.setAdapter(annularResultsAdapter);

        return view;
    }

}
