package zxc.laitooo.eva;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zizo on 2/16/2018.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesHolder> {
    private Context c;
    private ArrayList<Messages> list;

    public MessagesAdapter(Context context,ArrayList<Messages> contacts){
        this.c = context;
        this.list = contacts;
    }

    @Override
    public  MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message,parent,false);
        return new MessagesHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesHolder holder, int position) {
        holder.Name.setText(list.get(position).getName());
        if (list.get(position).getIsFromUser()){
            holder.Name.setGravity(Gravity.RIGHT);
            //holder.Name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }else {
            holder.Name.setGravity(Gravity.LEFT);
            //holder.Name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessagesHolder extends RecyclerView.ViewHolder{
        TextView Name;

        public MessagesHolder(View itemView) {
            super(itemView);
            Name = (TextView)itemView.findViewById(R.id.messageText);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
