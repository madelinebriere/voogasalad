package XML.xstream.classes;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import XML.xstream.interfaces.VoogaSerializer;

public class XStreamHelper implements VoogaSerializer{
	
	private XStream xstream;
	
	public XStreamHelper(){
		xstream = new XStream(new DomDriver());
	}

	@Override
	public String getXMLStringFromObject(Object o) {
		return xstream.toXML(o);
	}

	@Override
	public <C> C makeObjectFromXMLString(String XMLString, Class<C> clazz) {
		try{
			return clazz.cast(xstream.fromXML(XMLString));
		} catch (Exception e){
			return null;
		}
	}
}