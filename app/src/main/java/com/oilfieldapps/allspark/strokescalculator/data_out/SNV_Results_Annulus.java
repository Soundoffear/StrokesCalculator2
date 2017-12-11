package com.oilfieldapps.allspark.strokescalculator.data_out;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.custom_adapters.Annulus_results_adapter;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Results;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Results_DataBase;

import java.util.List;


public class SNV_Results_Annulus extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_and_v_data_out_annular, null);

        ListView output_listView_drillString = view.findViewById(R.id.list_view_annulus_results);
        Annulus_Results_DataBase annulusResults_dataBase = new Annulus_Results_DataBase(getContext());

        List<Annulus_Results> annulusResultsList = annulusResults_dataBase.getAllItem();
        Annulus_results_adapter annulus_results_adapter = new Annulus_results_adapter(getContext(), annulusResultsList);
        output_listView_drillString.setAdapter(annulus_results_adapter);

        return view;
    }

}
