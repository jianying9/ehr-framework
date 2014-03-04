package com.ehr.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.w3c.dom.DOMException;

import com.ehr.framework.logger.LogFactory;
import javax.xml.soap.*;

public class SOAPUtils {

    static final Logger logger = LogFactory.getFrameworkLogger();
    private static SOAPFactory soapFactory;

    static {
        try {
            soapFactory = SOAPFactory.newInstance();
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    public static SOAPElement addHeaderElement(SOAPMessage resp, String localName, String nameSpaceUrl)
            throws SOAPException {
        SOAPHeader soapHeader = resp.getSOAPPart().getEnvelope().getHeader();
        Name headerName = soapFactory.createName(localName, "", nameSpaceUrl);
        SOAPElement headerElement = soapHeader.addHeaderElement(headerName);
        return headerElement;
    }

    public static SOAPElement addBodyElement(SOAPMessage resp, String localName, String nameSpaceUrl)
            throws SOAPException {
        SOAPBody soapBody = resp.getSOAPPart().getEnvelope().getBody();
        Name bodyName = soapFactory.createName(localName, "", nameSpaceUrl);
        SOAPElement bodyElement = soapBody.addBodyElement(bodyName);
        return bodyElement;
    }

    public static SOAPElement addChilderElement(SOAPElement bodyElement, String nodeName, String nodeValue) throws SOAPException {
        SOAPElement propertyRS = bodyElement.addChildElement(nodeName);
        if (nodeValue != null) {
            propertyRS.addTextNode(nodeValue);
        }
        return propertyRS;
    }

    public static SOAPMessage sendSoapMessage(SOAPMessage sm, String url) throws SOAPException, MalformedURLException {
        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection sc = scf.createConnection();
		try
		{
			URL urllocal = new URL(url);
			SOAPMessage responseMessage = sc.call(sm, urllocal);
			return responseMessage;
		}
		finally
		{
			sc.close();
		}
    }

    public static void showResult(SOAPMessage responseMessage) throws SOAPException {
        Iterator<?> childs = responseMessage.getSOAPBody().getChildElements();
        printChild(childs);
    }

    public static void showHeaderResult(SOAPMessage responseMessage) throws SOAPException {
        Iterator<?> childs = responseMessage.getSOAPHeader().getChildElements();
        printChild(childs);
    }

    private static void printChild(Iterator<?> it) {
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof SOAPElement) {
                SOAPElement element = (SOAPElement) obj;
                logger.debug(element.getLocalName() + " = " + element.getValue());
                printChild(element.getChildElements());
            }
        }
    }

    public static String getElementValue(SOAPMessage responseMessage, String childName) throws SOAPException {
//        logger.debug("SOAPMessage.toString()-----" + responseMessage.toString());
        SOAPPart part = responseMessage.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        SOAPBody body = envelope.getBody();
        Iterator<?> childs = body.getChildElements();
        return getChild(childs, childName);
    }

    private static String getChild(Iterator<?> it, String childName) {
        String result = "";
        SOAPElement element = null;
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof SOAPElement) {
                element = (SOAPElement) obj;
                if (element.getLocalName().equals(childName)) {
                    result = element.getValue();
                    break;
                } else {
                    result = getChild(element.getChildElements(), childName);
                }
            }
        }
        return result;
    }

    public static void parserSOAPMessageAndFillReqVO(Object o, Iterator<?> it) throws SecurityException, IllegalArgumentException, DOMException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof SOAPElement) {
                SOAPElement element = (SOAPElement) obj;
                logger.debug(element.getLocalName() + " = " + element.getTextContent());
                setProperty(o, element.getLocalName(), element.getTextContent());
                parserSOAPMessageAndFillReqVO(o, element.getChildElements());
            }
        }
    }

    private static void setProperty(Object vo, String fieldName, String value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try {
            if (vo.getClass().getDeclaredField(fieldName) != null) {
                Method methodOfSet = vo.getClass().getMethod("set" + fieldName, String.class);
                if (methodOfSet != null) {
                    methodOfSet.invoke(vo, value);
                }
            }
        } catch (NoSuchFieldException e) {
            logger.debug("not fount field [" + fieldName + "]");
            logger.debug("message info --->" + e.getMessage());
        }
    }
}
