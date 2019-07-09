package zxc.laitooo.eva.messages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zxc.laitooo.eva.R;

/**
 * Created by Laitooo San on 30/06/2019.
 */

public class MessagesHolder extends RecyclerView.ViewHolder{

    RelativeLayout Layout;
    TextView Name;

    public MessagesHolder(View itemView) {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.messageText);
        Layout = (RelativeLayout) itemView.findViewById(R.id.layout_message);
    }
}
