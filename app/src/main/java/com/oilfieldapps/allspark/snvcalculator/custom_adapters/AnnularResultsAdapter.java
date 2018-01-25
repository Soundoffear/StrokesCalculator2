package com.oilfieldapps.allspark.snvcalculator.custom_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilfieldapps.allspark.snvcalculator.R;
import com.oilfieldapps.allspark.snvcalculator.data_and_databases.Annulus_Results;

import java.util.List;

public class AnnularResultsAdapter extends RecyclerView.Adapter<AnnularResultsAdapter.AnnularResultsViewHolder> {

    private Context snvContext;
    private List<Annulus_Results> annulus_resultsList;

    public AnnularResultsAdapter(Context snvContext, List<Annulus_Results> annulus_resultsList) {
        this.snvContext = snvContext;
        this.annulus_resultsList = annulus_resultsList;
    }

    @Override
    public AnnularResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutR = R.layout.s_and_v_data_out_annular_list_view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutR, parent, false);
        return new AnnularResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnnularResultsViewHolder holder, int position) {
        holder.ann_title_volume.setText(annulus_resultsList.get(position).getDs_part_name());
        holder.ann_result_volume.setText(annulus_resultsList.get(position).getDs_part_volume());
        holder.ann_result_strokes.setText(annulus_resultsList.get(position).getDs_part_strokes());
        holder.ann_volume_units.setText(annulus_resultsList.get(position).getDs_part_volume_units());
    }

    @Override
    public int getItemCount() {
        return annulus_resultsList.size();
    }

    class AnnularResultsViewHolder extends RecyclerView.ViewHolder {

        TextView ann_title_volume;
        TextView ann_result_volume;
        TextView ann_result_strokes;
        TextView ann_volume_units;

        AnnularResultsViewHolder(View itemView) {
            super(itemView);

            ann_title_volume = itemView.findViewById(R.id.recycler_view_annulus_results_name);
            ann_result_volume = itemView.findViewById(R.id.annulus_result_volume);
            ann_result_strokes = itemView.findViewById(R.id.annulus_result_strokes);
            ann_volume_units = itemView.findViewById(R.id.annulus_result_volume_units);
        }

    }

}
