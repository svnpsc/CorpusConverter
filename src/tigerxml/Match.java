//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.15 at 01:54:55 PM MEZ 
//
// Based on the XML schmema: TigerXMLSubcorpus.xsd
// Reference: http://www.coli.uni-saarland.de/projects/salsa/
//


package tigerxml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for matchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="matchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="variable" type="{}varType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="subgraph" use="required" type="{}idrefType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "matchType", propOrder = {
    "variable"
})
public class Match {

    @XmlElement(required = true)
    protected List<Var> variable;
    @XmlAttribute(required = true)
    @XmlIDREF
    protected Object subgraph;

    /**
     * Gets the value of the variable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the variable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVariable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Var }
     * 
     * 
     */
    public List<Var> getVariable() {
        if (variable == null) {
            variable = new ArrayList<Var>();
        }
        return this.variable;
    }

    /**
     * Gets the value of the subgraph property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSubgraph() {
        return subgraph;
    }

    /**
     * Sets the value of the subgraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSubgraph(Object value) {
        this.subgraph = value;
    }

}