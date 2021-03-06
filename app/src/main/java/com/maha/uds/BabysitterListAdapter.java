package com.maha.uds;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maha.uds.Model.AccountModel;

import java.util.List;

public class BabysitterListAdapter extends RecyclerView.Adapter<BabysitterListAdapter.ViewHolder> {

    private List<AccountModel> babysitterList;
    private List<String> keyList;
    private Context mContext;


    public BabysitterListAdapter(List<AccountModel> babysitterList , List<String> keyList, Context context){
        this.babysitterList = babysitterList;
        this.keyList = keyList;
        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.babysitter_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AccountModel babysitter = babysitterList.get(position);
        String kay = keyList.get(position);
        holder.name.setText(babysitter.getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,Gallery.class);
                intent.putExtra("babysitterKey", keyList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

         TextView name;
         CardView mCardView;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            name=itemView.findViewById(R.id.nameView);
            mCardView = itemView.findViewById(R.id.cardView);




        }

    }

}
