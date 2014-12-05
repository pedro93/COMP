package common;

public class Structure implements Comparable<Structure>{	 
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
	
	@Override
	public int compareTo(Structure o) {
		//Long.valueOf(l1).compareTo(Long.valueOf(l2));
		return String.valueOf(name).compareTo(String.valueOf(o.name));
	}

}
