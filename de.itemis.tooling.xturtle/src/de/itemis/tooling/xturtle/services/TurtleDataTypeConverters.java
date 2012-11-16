package de.itemis.tooling.xturtle.services;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;

import com.google.inject.Singleton;

@Singleton
public class TurtleDataTypeConverters extends DefaultTerminalConverters {

	IValueConverter<String> uriConverter=new IValueConverter<String>(){

		public String toValue(String string, INode node)
				throws ValueConverterException {
			if(string.charAt(0)!='<' || string.charAt(string.length()-1)!='>'){
				throw new ValueConverterException("Uri must have <...>-format", node, null);
			}
			return string.substring(1, string.length()-1);
		}

		public String toString(String value) throws ValueConverterException {
			return "<"+value+">";
		}
		
	} ;
	
	@ValueConverter(rule = "URI")
	public IValueConverter<String> STRING() {
		return uriConverter;
	}

	@SuppressWarnings("unchecked")
	@ValueConverter(rule = "STRING")
	public IValueConverter<String> forString() {
		//we do not process the strings further, so we can skip complicated transformations
		return (IValueConverter<String>)IValueConverter.NO_OP_CONVERTER;
	}

	IValueConverter<String> colonNameConverter=new IValueConverter<String>(){

		public String toValue(String string, INode node)
				throws ValueConverterException {
			if(string==null || string.length()==0 ||string.charAt(0)!=':'){
				throw new ValueConverterException("expecting ':' in QName", node, null);
			}
			return string.substring(1, string.length());
		}

		public String toString(String value) throws ValueConverterException {
			return ":"+value;
		}
		
	} ;
	
	@ValueConverter(rule = "ColonName")
	public IValueConverter<String> forColonName() {
		return colonNameConverter;
	}

}
