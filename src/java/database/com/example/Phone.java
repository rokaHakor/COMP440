package database.com.example;

public class Phone {

	private int id;
	private String type;
	private String phoneNumber;
	private int contactId;

	public Phone(int id, String type, String phoneNumber, int contactId) {
		this.id = id;
		this.type = type;
		this.phoneNumber = phoneNumber;
		this.contactId = contactId;
	}

	public Phone(String line){
		String[] values = line.split(",");
		for (int x = 0; x < values.length; x++) {
			switch(x) {
				case 0:
					id = Integer.parseInt(values[x]);
					break;
				case 1:
					type = values[x];
					break;
				case 2:
					phoneNumber = values[x];
					break;
				case 3:
					contactId = Integer.parseInt(values[x]);
					break;
			}
		}
	}

	@Override
	public String toString() {
		return id + "," + type + "," + phoneNumber + "," + contactId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
}
