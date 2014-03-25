enum Type {
	TERM("TERM"), NONTERM("NONTERM"),NULL("NULL");
	
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

public class Structure {	 
	public String name;
	public Type type;
	public int line;
	public int column;

	public Structure(String name, Type type,int line,int column)
	{
		this.name=name;
		this.type=type;
		this.line=line;
		this.column=column;
	}
	public Structure()
	{
		name="";
		type=Type.NULL;
		line=0;
		column=0;
	}
	
	@Override
	public String toString() {
		return "{"+name+"|"+type+"|"+line+"|"+column+"}";
	}
	
	@Override
	public int hashCode() {
		return name.hashCode()+type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
	    Structure u=(Structure) obj;
		return (name.equals(u.name) && type==u.type);
	}

}