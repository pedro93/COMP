package common;

public enum Type {
	TERM("TERM"), NONTERM("NONTERM"),NULL("NULL"), START("START"), STRING("STRING ELEMENT"), EPSILON("EPSILON VALUE");
	
	private final String name;       

    private Type(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }
	
	@Override 
	public String toString(){ 
		return name; 
	}
}