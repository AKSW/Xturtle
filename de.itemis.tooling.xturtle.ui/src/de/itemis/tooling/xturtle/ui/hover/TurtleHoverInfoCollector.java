package de.itemis.tooling.xturtle.ui.hover;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

class TurtleHoverInfoCollector {

	private List<String> htmlDescriptions=new ArrayList<String>();
	private List<String> descriptions=new ArrayList<String>();

	public void addDescriptionUserData(String nullableData){
		if(nullableData!=null){
			List<String> descriptionsToAddToHtml=new ArrayList<String>();
			for (String string : Splitter.on(",,").split(nullableData)) {
				String simplified=simplify(string);
				if(!descriptions.contains(simplified)){
					descriptionsToAddToHtml.add(simplified);
				}
			}
			if(!descriptionsToAddToHtml.isEmpty()){
				descriptions.addAll(descriptionsToAddToHtml);
				addToHtmlDescriptions(descriptionsToAddToHtml);
			}
		}
	}

	private void addToHtmlDescriptions(List<String> descriptionsToAdd){
		StringBuilder b=new StringBuilder("<dl>");
		for (String desc : descriptionsToAdd) {
			b.append("<dd>");
			b.append(desc);
			b.append("</dd>");
		}
		b.append("</dl>");
		htmlDescriptions.add(b.toString());
	}

	//remove surrounding quotation marks and escaped quotes
	private String simplify(String value) {
		String resultString="";
		if(value!=null){
			if(value.startsWith("\"\"\"")){
				resultString=value.substring(3, value.length()-3);
			}else{
				resultString=value.substring(1, value.length()-1);
			}
		}
		return resultString.replaceAll("\\\\\"", "\"").replaceAll("\n", "</br>");
	}
	public String getAsHtml(){
		return Joiner.on("").join(htmlDescriptions);
	}
}
