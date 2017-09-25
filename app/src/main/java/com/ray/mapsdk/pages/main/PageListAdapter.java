package com.ray.mapsdk.pages.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ray.mapsdk.R;
import com.ray.mapsdk.base.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author      : leixing
 * Date        : 2017-09-25
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : xxx
 */

public class PageListAdapter extends RecyclerView.Adapter<PageListAdapter.PageListViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List<Page> mPages;
    private OnItemClickListener mOnItemClickListener;

    public PageListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPages = new ArrayList<>();
    }

    public void set(List<Page> pages) {
        mPages.clear();
        if (pages != null) {
            mPages.addAll(pages);
        }
        notifyDataSetChanged();
    }

    @Override
    public PageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.view_page_list_item, parent, false);
        return new PageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PageListViewHolder holder, int position) {
        Page page = mPages.get(position);
        holder.tvName.setText(page.getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
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
        return mPages.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    static class PageListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        PageListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
