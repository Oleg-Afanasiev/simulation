//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.11 at 12:33:51 AM EET 
//


package com.telesens.afanasiev.logs.shemes;

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
 *                 &lt;attribute name="stationId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="busId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="routeId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="timeBusStop" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="busNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="takeInPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="getOffPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="stayPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
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
@XmlRootElement(name = "stations")
public class Stations {

    @XmlElement(required = true)
    protected List<Stations.Log> log;

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
     * {@link Stations.Log }
     * 
     * 
     */
    public List<Stations.Log> getLog() {
        if (log == null) {
            log = new ArrayList<Stations.Log>();
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
     *       &lt;attribute name="stationId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="busId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="routeId" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="timeBusStop" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="busNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="takeInPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="getOffPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="stayPassCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
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

        @XmlAttribute(name = "stationId", required = true)
        protected long stationId;
        @XmlAttribute(name = "busId", required = true)
        protected long busId;
        @XmlAttribute(name = "routeId", required = true)
        protected long routeId;
        @XmlAttribute(name = "timeBusStop", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar timeBusStop;
        @XmlAttribute(name = "busNumber", required = true)
        protected String busNumber;
        @XmlAttribute(name = "takeInPassCount", required = true)
        protected int takeInPassCount;
        @XmlAttribute(name = "getOffPassCount", required = true)
        protected int getOffPassCount;
        @XmlAttribute(name = "stayPassCount", required = true)
        protected int stayPassCount;

        /**
         * Gets the value of the stationId property.
         * 
         */
        public long getStationId() {
            return stationId;
        }

        /**
         * Sets the value of the stationId property.
         * 
         */
        public void setStationId(long value) {
            this.stationId = value;
        }

        /**
         * Gets the value of the busId property.
         * 
         */
        public long getBusId() {
            return busId;
        }

        /**
         * Sets the value of the busId property.
         * 
         */
        public void setBusId(long value) {
            this.busId = value;
        }

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
         * Gets the value of the timeBusStop property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTimeBusStop() {
            return timeBusStop;
        }

        /**
         * Sets the value of the timeBusStop property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTimeBusStop(XMLGregorianCalendar value) {
            this.timeBusStop = value;
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
         * Gets the value of the takeInPassCount property.
         * 
         */
        public int getTakeInPassCount() {
            return takeInPassCount;
        }

        /**
         * Sets the value of the takeInPassCount property.
         * 
         */
        public void setTakeInPassCount(int value) {
            this.takeInPassCount = value;
        }

        /**
         * Gets the value of the getOffPassCount property.
         * 
         */
        public int getGetOffPassCount() {
            return getOffPassCount;
        }

        /**
         * Sets the value of the getOffPassCount property.
         * 
         */
        public void setGetOffPassCount(int value) {
            this.getOffPassCount = value;
        }

        /**
         * Gets the value of the stayPassCount property.
         * 
         */
        public int getStayPassCount() {
            return stayPassCount;
        }

        /**
         * Sets the value of the stayPassCount property.
         * 
         */
        public void setStayPassCount(int value) {
            this.stayPassCount = value;
        }

    }

}
