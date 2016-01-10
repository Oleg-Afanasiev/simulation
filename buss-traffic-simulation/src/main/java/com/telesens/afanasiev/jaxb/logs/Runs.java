//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.11 at 12:33:46 AM EET 
//


package com.telesens.afanasiev.jaxb.logs;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="log" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="routeId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="routeNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="busNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="timeStart" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="timeFinish" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="passDeliveredCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "log"
})
@XmlRootElement(name = "runs")
public class Runs {

    @XmlElement(required = true)
    protected List<Runs.Log> log;

    /**
     * Gets the value of the log property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the log property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Runs.Log }
     * 
     * 
     */
    public List<Runs.Log> getLog() {
        if (log == null) {
            log = new ArrayList<Runs.Log>();
        }
        return this.log;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="routeId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="routeNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="busNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="timeStart" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="timeFinish" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="passDeliveredCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Log {

        @XmlAttribute(name = "routeId", required = true)
        protected long routeId;
        @XmlAttribute(name = "routeNumber", required = true)
        protected String routeNumber;
        @XmlAttribute(name = "busNumber", required = true)
        protected String busNumber;
        @XmlAttribute(name = "timeStart", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar timeStart;
        @XmlAttribute(name = "timeFinish", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar timeFinish;
        @XmlAttribute(name = "passDeliveredCount", required = true)
        protected int passDeliveredCount;

        /**
         * Gets the value of the routeId property.
         * 
         */
        public long getRouteId() {
            return routeId;
        }

        /**
         * Sets the value of the routeId property.
         * 
         */
        public void setRouteId(long value) {
            this.routeId = value;
        }

        /**
         * Gets the value of the routeNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRouteNumber() {
            return routeNumber;
        }

        /**
         * Sets the value of the routeNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRouteNumber(String value) {
            this.routeNumber = value;
        }

        /**
         * Gets the value of the busNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBusNumber() {
            return busNumber;
        }

        /**
         * Sets the value of the busNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBusNumber(String value) {
            this.busNumber = value;
        }

        /**
         * Gets the value of the timeStart property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTimeStart() {
            return timeStart;
        }

        /**
         * Sets the value of the timeStart property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTimeStart(XMLGregorianCalendar value) {
            this.timeStart = value;
        }

        /**
         * Gets the value of the timeFinish property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTimeFinish() {
            return timeFinish;
        }

        /**
         * Sets the value of the timeFinish property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTimeFinish(XMLGregorianCalendar value) {
            this.timeFinish = value;
        }

        /**
         * Gets the value of the passDeliveredCount property.
         * 
         */
        public int getPassDeliveredCount() {
            return passDeliveredCount;
        }

        /**
         * Sets the value of the passDeliveredCount property.
         * 
         */
        public void setPassDeliveredCount(int value) {
            this.passDeliveredCount = value;
        }

    }

}