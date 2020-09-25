package com.aokiji.schultegrid.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aokiji.schultegrid.R;
import com.aokiji.schultegrid.utils.ScreenUtil;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {

    private Context mContext;
    private List<Integer> mList;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ButtonAdapter(Context context, List<Integer> list)
    {
        this.mContext = context;
        this.mList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_button, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        int buttonHeight = Math.round((ScreenUtil.getScreenWidth(mContext) - ScreenUtil.dp2px(mContext, 50)) / 5);
        ViewGroup.LayoutParams layoutParams = holder.tvButton.getLayoutParams();
        layoutParams.height = buttonHeight;
        holder.tvButton.requestLayout();

        String value = String.valueOf(mList.get(position));
        holder.tvButton.setText(value);

        if (mOnItemClickListener != null)
        {
            holder.tvButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onItemClick(holder.getLayoutPosition());
                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvButton = itemView.findViewById(R.id.tv_button);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
