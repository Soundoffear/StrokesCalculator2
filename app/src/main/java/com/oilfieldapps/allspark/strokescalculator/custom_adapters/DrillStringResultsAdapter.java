package com.oilfieldapps.allspark.strokescalculator.custom_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.DrillString_Results;

import java.util.List;

/**
 * Created by soundoffear on 24/12/2017.
 */

public class DrillStringResultsAdapter extends RecyclerView.Adapter<DrillStringResultsAdapter.DrillStringResultsViewHolder> {

    Context dsContext;
    List<DrillString_Results> drillString_resultsList;

    public DrillStringResultsAdapter(Context dsContext, List<DrillString_Results> drillString_resultsList) {
        this.dsContext = dsContext;
        this.drillString_resultsList = drillString_resultsList;
    }

    @Override
    public DrillStringResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutR = R.layout.s_and_v_data_out_drill_string_list_view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutR, parent, false);

        return new DrillStringResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrillStringResultsViewHolder holder, int position) {

        holder.tv_result_name.setText(drillString_resultsList.get(position).getDrillString_name());
        holder.tv_volume_result.setText(drillString_resultsList.get(position).getDrillString_volume());
        holder.tv_strokes_result.setText(drillString_resultsList.get(position).getDrillString_strokes());
        holder.tv_volume_unit.setText(drillString_resultsList.get(position).getDrillString_volume_units());

    }

    @Override
    public int getItemCount() {
        return drillString_resultsList.size();
    }

    class DrillStringResultsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_result_name;
        TextView tv_volume_result;
        TextView tv_volume_unit;
        TextView tv_strokes_result;

        DrillStringResultsViewHolder(View itemView) {
            super(itemView);

            tv_result_name = itemView.findViewById(R.id.drill_string_results_name);
            tv_volume_result = itemView.findViewById(R.id.drill_string_results_volume);
            tv_volume_unit = itemView.findViewById(R.id.drill_string_results_volume_units);
            tv_strokes_result = itemView.findViewById(R.id.drill_string_results_strokes);

        }

    }

}
