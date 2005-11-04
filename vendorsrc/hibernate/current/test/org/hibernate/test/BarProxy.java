//$Id: BarProxy.java,v 1.3 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

public interface BarProxy extends AbstractProxy {
	public void setBaz(Baz arg0);
	public Baz getBaz();
	public void setBarComponent(FooComponent arg0);
	public FooComponent getBarComponent();
	//public void setBarString(String arg0);
	public String getBarString();
	public Object getObject();
	public void setObject(Object o);
}





