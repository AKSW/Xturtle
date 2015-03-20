/*******************************************************************************
 * Copyright (c) 2013 AKSW Xturtle Project, itemis AG (http://www.itemis.eu).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package de.itemis.tooling.xturtle.validation

import com.google.common.base.Optional
import com.google.common.base.Preconditions
import java.math.BigInteger
import org.eclipse.emf.ecore.xml.type.impl.XMLTypeFactoryImpl
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil
import org.eclipse.xtext.naming.QualifiedName

class XsdTypeValidator {
	
	static val fac=XMLTypeFactoryImpl::eINSTANCE
	
	def static Optional<String> getXsdError(String origValue, QualifiedName typeQname){
		var Optional<String> result=Optional::absent
		if(origValue==null){
			return result
		}
		val value=stripQuotes(origValue)
		if("http://www.w3.org/2001/XMLSchema#".equals(typeQname.firstSegment)){
			try{
				switch(typeQname.lastSegment){
//					case "string":result=checkString(value)
//					case "token":result=checkToken(value)
//					case "language":result=checkLanguage(value)
//					case "anyURI":
					case "normalizedString":Preconditions::checkArgument(value.equals(fac.createNormalizedString(value)))
					case "boolean":fac.createBoolean(value)
					case "decimal":fac.createDecimal(value)
					case "float":fac.createFloat(value)
					case "double":fac.createDouble(value)
					case "integer":fac.createInteger(value)
					case "positiveInteger":Preconditions::checkArgument(noWhiteSpace(value) && fac.createPositiveInteger(value).longValue>0)
					case "nonPositiveInteger":Preconditions::checkArgument(noWhiteSpace(value) && fac.createNonPositiveInteger(value).longValue<=0)
					case "negativeInteger":Preconditions::checkArgument(noWhiteSpace(value) && fac.createNegativeInteger(value).longValue<0)
					case "nonNegativeInteger":Preconditions::checkArgument(noWhiteSpace(value) && fac.createNonNegativeInteger(value).longValue>=0)
					case "long":{Preconditions::checkArgument(noWhiteSpace(value)) fac.createLong(value)}
					case "int":{Preconditions::checkArgument(noWhiteSpace(value)) fac.createInt(value)}
					case "short":{Preconditions::checkArgument(noWhiteSpace(value)) fac.createShort(value)}
					case "byte":{Preconditions::checkArgument(noWhiteSpace(value)) fac.createByte(value)}
					case "unsignedLong":{val longValue=fac.createUnsignedLong(value) Preconditions::checkArgument(noWhiteSpace(value) &&longValue.compareTo(BigInteger::ZERO) >=0 && longValue.compareTo(new BigInteger("18446744073709551615"))<=0)}
					case "unsignedInt":{val longValue=fac.createUnsignedInt(value) Preconditions::checkArgument(noWhiteSpace(value) &&longValue>=0 && longValue<=4294967295L)}
					case "unsignedShort":{val longValue=fac.createUnsignedShort(value) Preconditions::checkArgument(noWhiteSpace(value) &&longValue>=0 && longValue<=65535)}
					case "unsignedByte":{val longValue=fac.createUnsignedShort(value) Preconditions::checkArgument(noWhiteSpace(value) &&longValue>=0 && longValue<=255)}
					case "dateTime": fac.createDateTime(value)
					case "time": fac.createTime(value)
					case "date": fac.createDate(value)
					case "gYearMonth": fac.createGYearMonth(value)
					case "gYear": fac.createGYear(value)
					case "gMonthDay": fac.createGMonthDay(value)
					case "gDay": fac.createGDay(value)
					case "gMonth": fac.createGMonth(value)
					case "hexBinary": fac.createHexBinary(value)
					case "base64Binary": fac.createBase64Binary(value)
				}
			} catch(Exception e){
				result=Optional::of("not a valid "+typeQname.lastSegment)
			}
		}
		return result
	}

	def static String stripQuotes(String string) {
		var boolean longString=false
		if(string.length>5 && string.charAt(1)==string.charAt(0)){longString=true}
		if(longString){
			string.substring(3,string.length-3)
		}else{
			string.substring(1,string.length-1)
		}
	}


	def static boolean noWhiteSpace(String string){
		XMLTypeUtil::normalize(string,true).equals(string)
	}

//
//	def static Optional<String> checkToken(String string) {
//		val value=fac.createToken(string)
//		if(!value.equals(string)){
//			Optional::of("Token contains illegal character")
//		} else{
//			null
//		}
//	}
//
//	def static Optional<String> checkLanguage(String string) {
//		val value=fac.createLanguage(string)
//		if(!value.equals(string)){
//			Optional::of("Language contains illegal character")
//		} else{
//			null
//		}
//	}
}