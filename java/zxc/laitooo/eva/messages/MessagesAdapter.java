package zxc.laitooo.eva.messages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import zxc.laitooo.eva.R;

/**
 * Created by Zizo on 2/16/2018.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesHolder> {
    private Context c;
    private ArrayList<Message> list;

    public MessagesAdapter(Context context,ArrayList<Message> contacts){
        this.c = context;
        this.list = contacts;
    }

    @Override
    public  MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new MessagesHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesHolder holder, final int position) {
        holder.Name.setText(list.get(position).getName());
        holder.Name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager manager = (ClipboardManager)c.getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setText(list.get(position).getName());
                Toast.makeText(c, "copied to clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getIsFromUser() ? R.layout.message_user : R.layout.message_eva;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
