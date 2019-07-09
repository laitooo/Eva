package zxc.laitooo.eva.records;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

import zxc.laitooo.eva.R;
import zxc.laitooo.eva.messages.MessagesHolder;

/**
 * Created by Laitooo San on 01/07/2019.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsHolder> {

    Context context;
    ArrayList<Record> list;

    public RecordsAdapter(Context context, ArrayList<Record> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecordsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record,parent,false);
        return new RecordsHolder(v);

    }

    @Override
    public void onBindViewHolder(RecordsHolder holder, int position) {
        final Record record = list.get(position);
        holder.Name.setText(FilenameUtils.getBaseName(record.getFile().getName()));
        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(record.getFile()), "audio");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
