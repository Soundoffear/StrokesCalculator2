package com.oilfieldapps.allspark.snvcalculator.custom_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.HoleResultsData;

import java.util.List;

public class HoleResultsAdapter extends RecyclerView.Adapter<HoleResultsAdapter.HoleResultsViewHolder> {

    private Context hContext;
    private List<HoleResultsData> holeResultsData;

    public HoleResultsAdapter(Context hContext, List<HoleResultsData> holeResultsData) {
        this.hContext = hContext;
        this.holeResultsData = holeResultsData;
    }

    @Override
    public HoleResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutR = R.layout.s_and_v_data_out_empty_hole_list_view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutR, parent, false);

        return new HoleResultsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HoleResultsViewHolder holder, int position) {

        holder.tv_hName.setText(holeResultsData.get(position).getName_hole_results());
        holder.tv_hVolumeResult.setText(holeResultsData.get(position).getVolume_hole_results());
        holder.tv_hVolumeUnits.setText(holeResultsData.get(position).getVolume_hole_results_units());

    }

    @Override
    public int getItemCount() {
        return holeResultsData.size();
    }

    class HoleResultsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_hName;
        TextView tv_hVolumeResult;
        TextView tv_hVolumeUnits;

        public HoleResultsViewHolder(View itemView) {
            super(itemView);

            tv_hName = itemView.findViewById(R.id.list_view_hole_results_no_pipe_name);
            tv_hVolumeResult = itemView.findViewById(R.id.hole_result_no_pipe_volume);
            tv_hVolumeUnits = itemView.findViewById(R.id.hole_result_no_pipe_volume_units);
        }

    }
}
