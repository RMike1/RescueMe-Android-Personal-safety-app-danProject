package app.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import app.models.SMS_Message;

import app.rescueMe.R;

public class TemplateAdapter extends ArrayAdapter<SMS_Message>{

	
	private Context context;
    List<SMS_Message> templates;
    TextView msg;
    ImageView image;
    
    CheckBox checkToSend;

	public TemplateAdapter(Context context, List<SMS_Message> templates)
	{
		super(context, R.layout.row_sms_template, templates);
        this.context = context;
        this.templates = templates;
        
    }
	
	public int getCount() {
        return templates.size();
    } 
	
	public SMS_Message getItem(int position) {
		return templates.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.row_sms_template, parent,false);
		SMS_Message message = templates.get(position);
		msg = (TextView) view.findViewById(R.id.row_message);
		image = (ImageView) view.findViewById(R.id.check_smsToSend);
		
		message = (SMS_Message) getItem(position);
		
		if(message.isChecked)
			image.setBackgroundResource(R.drawable.sms_icon_blue);//set sms icon to chosen sms
//		else{
//			image.setBackgroundResource(R.drawable.sms_icon_grey);
//		}
		
		msg.setText(message.message);
		view.setId(message.id);
		
		
		return view;
	}
	
}
