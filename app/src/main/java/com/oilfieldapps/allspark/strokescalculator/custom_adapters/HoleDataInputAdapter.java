package com.oilfieldapps.allspark.strokescalculator.custom_adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilfieldapps.allspark.strokescalculator.R;
import com.oilfieldapps.allspark.strokescalculator.data_and_databases.HoleData;
import com.oilfieldapps.allspark.strokescalculator.interfaces.OnClickRecyclerViewListener;

import java.util.List;

public class HoleDataInputAdapter extends RecyclerView.Adapter<HoleDataInputAdapter.HoleDataInputViewHolder> {

    private List<HoleData> holeDataList;
    public static OnClickRecyclerViewListener onClickRecyclerViewListener;

    public HoleDataInputAdapter(List<HoleData> holeData, @Nullable OnClickRecyclerViewListener onClickRecyclerViewListener1) {
        this.holeDataList = holeData;
        onClickRecyclerViewListener = onClickRecyclerViewListener1;
    }

    @Override
    public HoleDataInputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutR = R.layout.hole_data_list_view_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutR, parent, false);
        return new HoleDataInputViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HoleDataInputViewHolder holder, int position) {

        holder.tv_partName.setText(holeDataList.get(position).getName());
        holder.tv_ID.setText(holeDataList.get(position).getInput_id());
        holder.tv_OD.setText(holeDataList.get(position).getInput_od());
        holder.tv_top_MD.setText(holeDataList.get(position).getInput_top_md());
        holder.tv_bot_MD.setText(holeDataList.get(position).getInput_end_md());

        holder.tv_ID_unit.setText(holeDataList.get(position).getInput_diameter_unit());
        holder.tv_OD_unit.setText(holeDataList.get(position).getInput_diameter_unit());
        holder.tv_top_MD_unit.setText(holeDataList.get(position).getInput_length_unit());
        holder.tv_bot_MD_unit.setText(holeDataList.get(position).getInput_length_unit());

    }

    @Override
    public int getItemCount() {
        return holeDataList.size();
    }

    class HoleDataInputViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_partName;
        TextView tv_ID;
        TextView tv_OD;
        TextView tv_top_MD;
        TextView tv_bot_MD;

        TextView tv_ID_unit;
        TextView tv_OD_unit;
        TextView tv_top_MD_unit;
        TextView tv_bot_MD_unit;

        HoleDataInputViewHolder(View itemView) {
            super(itemView);

            tv_partName = itemView.findViewById(R.id.itemNameLV);
            tv_ID = itemView.findViewById(R.id.input_ID_LV_ET);
            tv_OD = itemView.findViewById(R.id.input_OD_LV_ET);
            tv_top_MD = itemView.findViewById(R.id.input_top_md_TV);
            tv_bot_MD = itemView.findViewById(R.id.input_end_md_TV);

            tv_ID_unit = itemView.findViewById(R.id.input_ID_LV_TV_units);
            tv_OD_unit = itemView.findViewById(R.id.input_OD_LV_ET_units);
            tv_top_MD_unit = itemView.findViewById(R.id.input_top_md_TV_units);
            tv_bot_MD_unit = itemView.findViewById(R.id.input_end_md_TV_units);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickRecyclerViewListener.onRecViewClickListener(v, this.getLayoutPosition());
        }
    }

}
