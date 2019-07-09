package zxc.laitooo.eva.records;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import zxc.laitooo.eva.R;

/**
 * Created by Laitooo San on 01/07/2019.
 */

public class RecordsHolder extends RecyclerView.ViewHolder {

    TextView Name;

    public RecordsHolder(View itemView) {
        super(itemView);
        Name = (TextView) itemView.findViewById(R.id.name_record);
    }

}
