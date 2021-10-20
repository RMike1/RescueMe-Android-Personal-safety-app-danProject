package app.adapters;


import java.util.List;



import app.rescueMe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import app.models.Contact;

public class ContactAdapter extends ArrayAdapter<Contact>{
	
	private Context context;
    List<Contact> contacts;
    TextView name;
    TextView number;
    CheckBox checkToSend;
    ImageView image;

	public ContactAdapter(Context context, List<Contact> contacts)
	{
		super(context, R.layout.row_contact, contacts);
        this.context = context;
        this.contacts = contacts;
        
    }
	
	
	public int getCount() {
        return contacts.size();
    }


	public Contact getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.row_contact, parent,false);
		Contact contact = contacts.get(position);
		name = (TextView) view.findViewById(R.id.row_name);
		number = (TextView) view.findViewById(R.id.row_message);
		image = (ImageView) view.findViewById(R.id.check_numToSend);
		contact = (Contact) getItem(position);
		if(contact.isChecked)
			image.setBackgroundResource(R.drawable.sos_red);//set siren icon to contact 
		/*else{
			image.setBackgroundResource(R.drawable.sos_grey);
		}*/
		name.setText(contact.name);
		number.setText(contact.number);
		
		

		
		return view;
	}
	
	
	
	
	}
	

