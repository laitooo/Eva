package zxc.laitooo.eva.messages;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Laitooo San on 01/07/2019.
 */

public class MessagesHandler {

    SharedPreferences preferences;
    String PREFERENCES_MESSAGES = "messages_eva";
    String MESSAGE = "message_num_";
    String FROM_USER = "from_user_num_";
    String ERROR = "no _message_ here,skip";

    public MessagesHandler(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_MESSAGES,Context.MODE_PRIVATE);
    }

    public int lastMessageIndex(){
        int counter = -1;
        for (int i=0;i<1000;i++){
            String message = preferences.getString(MESSAGE + i,ERROR);
            if (message.equals(ERROR)){
                return counter;
            }else {
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<Message> getMessages(){
        ArrayList<Message> messagesList = new ArrayList<>();
        for (int i=0;i<100;i++){
            String message = preferences.getString(MESSAGE + i,ERROR);
            boolean fromUser = preferences.getBoolean(FROM_USER + i,true);
            if (message.equals(ERROR)){
                break;
            }else {
                messagesList.add(new Message(fromUser,message));
            }
        }
        return messagesList;
    }

    public void addMessage(boolean fromUser,String message){
        int last = lastMessageIndex()+1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MESSAGE + last,message);
        editor.putBoolean(FROM_USER + last,fromUser);
        editor.commit();
    }

    public void addMessage(Message message){
        int last = lastMessageIndex()+1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MESSAGE + last,message.getName());
        editor.putBoolean(FROM_USER + last,message.getIsFromUser());
        editor.commit();
    }

    public void clearMessages(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
