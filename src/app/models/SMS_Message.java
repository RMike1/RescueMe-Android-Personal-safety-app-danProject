package app.models;

public class SMS_Message {
	
	
	
	public String message;
	public boolean isChecked;
	public int id;
	
	public SMS_Message(String message, boolean isChecked)
	{
		
		this.isChecked = isChecked;
		this.message = message;
		
	}

	public SMS_Message() {
		
		this.isChecked = false;
		this.message = "";
	}

	/*public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}*/

}
