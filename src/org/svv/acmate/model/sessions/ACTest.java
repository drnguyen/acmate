//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.09 at 03:07:48 PM CEST 
//


package org.svv.acmate.model.sessions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://acmate.svv.org/test-results}Request"/>
 *         &lt;element ref="{http://acmate.svv.org/test-results}AccessResponse"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ACTest")
public class ACTest {

    @XmlElement(name = "Request", required = true)
    protected Request request;
    @XmlElement(name = "AccessResponse", required = true)
    protected AccessResponse accessResponse;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link Request }
     *     
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request }
     *     
     */
    public void setRequest(Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the accessResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AccessResponse }
     *     
     */
    public AccessResponse getAccessResponse() {
        return accessResponse;
    }

    /**
     * Sets the value of the accessResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessResponse }
     *     
     */
    public void setAccessResponse(AccessResponse value) {
        this.accessResponse = value;
    }

}
