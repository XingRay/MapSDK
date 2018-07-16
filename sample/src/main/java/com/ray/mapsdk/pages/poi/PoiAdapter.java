package com.ray.mapsdk.pages.poi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ray.lib_map.entity.Poi;
import com.ray.mapsdk.R;
import com.ray.mapsdk.extern.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@qq.com
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final List<PoiWrapper> mPois;
    private OnItemClickListener mOnItemClickListener;


    PoiAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mPois = new ArrayList<>();
    }

    void set(List<PoiWrapper> pois) {
        mPois.clear();
        if (pois != null) {
            mPois.addAll(pois);
        }
        notifyDataSetChanged();
    }

    void clear() {
        mPois.clear();
        notifyDataSetChanged();
    }

    void select(int position) {
        for (int i = 0, size = mPois.size(); i < size; i++) {
            PoiWrapper poiWrapper = mPois.get(i);
            poiWrapper.setSelected(i == position);
        }
        notifyDataSetChanged();
    }

    Poi getSelectedPoi() {
        for (PoiWrapper poiWrapper : mPois) {
            if (poiWrapper.isSelected()) {
                return poiWrapper.getPoi();
            }
        }
        return null;
    }

    @Override
    public PoiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.view_poi_list_item, parent, false);
        return new PoiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PoiViewHolder holder, int position) {
        PoiWrapper poiWrapper = mPois.get(position);
        Poi poi = poiWrapper.getPoi();
        boolean selected = poiWrapper.isSelected();

        holder.tvName.setText(poi.getName());
        holder.tvAddress.setText(poi.getAddress());
        holder.tvPosition.setText(String.format("(%1$s, %2$s)", poi.getLatitude(), poi.getLongitude()));
        holder.ivSelect.setVisibility(selected ? View.VISIBLE : View.GONE);

        holder.rlItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPois.size();
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class PoiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.rl_item_view)
        RelativeLayout rlItemView;

        PoiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
