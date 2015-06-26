package util;

public enum EType {
	BORDER ("Border"),
	MIDDLE ("Middle"),
	MIDDLE_LEFT ("Middle Left"),
	BORDER_SINUS ("Border_Sinus");
	
	public String type;
	
	EType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
