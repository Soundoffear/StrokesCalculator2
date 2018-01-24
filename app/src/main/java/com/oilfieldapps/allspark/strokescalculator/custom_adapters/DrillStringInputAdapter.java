package com.oilfieldapps.allspark.strokescalculator.custom_adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.Annulus_Data;
import com.oilfieldapps.allspark.strokescalculator.interfaces.OnClickRecyclerViewListener;

import java.util.List;

public class DrillStringInputAdapter extends RecyclerView.Adapter<DrillStringInputAdapter.DrillStringInputViewHolder> {

    private List<Annulus_Data> annulus_data;
    private static OnClickRecyclerViewListener recyclerViewListener;

    public DrillStringInputAdapter(List<Annulus_Data> annulus_data, @Nullable OnClickRecyclerViewListener onClickRecyclerViewListener) {
        this.annulus_data = annulus_data;
        recyclerViewListener = onClickRecyclerViewListener;
    }

    @Override
    public DrillStringInputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutR = R.layout.drill_string_list_view_data_display;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutR, parent, false);
        return new DrillStringInputViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrillStringInputViewHolder holder, int position) {
        holder.tv_partName.setText(annulus_data.get(position).getString_name());
        holder.tv_ds_id.setText(annulus_data.get(position).getString_id());
        holder.tv_ds_od.setText(annulus_data.get(position).getString_od());
        holder.tv_ds_length.setText(annulus_data.get(position).getString_length());

        holder.tv_ds_length_units.setText(annulus_data.get(position).getLength_units());
        holder.tv_ds_id_units.setText(annulus_data.get(position).getDiameter_units());
        holder.tv_ds_od_units.setText(annulus_data.get(position).getDiameter_units());
    }

    @Override
    public int getItemCount() {
        return annulus_data.size();
    }

    class DrillStringInputViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_partName;
        TextView tv_ds_id;
        TextView tv_ds_od;
        TextView tv_ds_length;

        TextView tv_ds_id_units;
        TextView tv_ds_od_units;
        TextView tv_ds_length_units;

        DrillStringInputViewHolder(View itemView) {
            super(itemView);

            tv_partName = itemView.findViewById(R.id.ds_part_name);
            tv_ds_id = itemView.findViewById(R.id.drill_string_tv_input_ds_id);
            tv_ds_od = itemView.findViewById(R.id.drill_string_tv_input_ds_od);
            tv_ds_length = itemView.findViewById(R.id.drill_string_tv_input_ds_length);

            tv_ds_id_units = itemView.findViewById(R.id.drill_string_tv_input_ds_id_unit);
            tv_ds_od_units = itemView.findViewById(R.id.drill_string_tv_input_ds_od_unit);
            tv_ds_length_units = itemView.findViewById(R.id.drill_string_tv_input_ds_length_unit);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewListener.onRecViewClickListener(v, this.getLayoutPosition());
            Log.d("DS_View_holder", "Start Dialog");
        }
    }

}
