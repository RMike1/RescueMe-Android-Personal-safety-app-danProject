package app.models;

public class Contact {
	
	public String name;
	public String number;
	public String id;
	public boolean isChecked;
	
	public Contact(String id, String name,String number, Boolean isChecked)
	{
		this.id = id;
		this.name = name;
		this.number = number;
		this.isChecked = isChecked;
	}

	public Contact() {
		this.id = "";
		this.name = "";
		this.number = "";
		this.isChecked = false;
	}

	/*public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}*/

}
