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

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for sentenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sentenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="graph" type="{}graphType" minOccurs="0"/>
 *         &lt;element name="matches" type="{}matchesType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{}idType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sentenceType", propOrder = {
    "graph",
    "matches"
})
public class Sentence implements CorpusUnit {

    protected Graph graph;
    protected Matches matches;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * Gets the value of the graph property.
     * 
     * @return
     *     possible object is
     *     {@link Graph }
     *     
     */
    public Graph getGraph() {
		if (graph == null) {
			graph = new Graph();
		}
        return graph;
    }

    /**
     * Sets the value of the graph property.
     * 
     * @param value
     *     allowed object is
     *     {@link Graph }
     *     
     */
    public void setGraph(Graph value) {
        this.graph = value;
    }

    /**
     * Gets the value of the matches property.
     * 
     * @return
     *     possible object is
     *     {@link Matches }
     *     
     */
    public Matches getMatches() {
        return matches;
    }

    /**
     * Sets the value of the matches property.
     * 
     * @param value
     *     allowed object is
     *     {@link Matches }
     *     
     */
    public void setMatches(Matches value) {
        this.matches = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

	@Override
	public List<CorpusUnit> getSentences() {
		return Collections.singletonList((CorpusUnit) this);
	}

	public List<T> getTerminals() {
		return getGraph().getTerminals().getT();
	}
	
	public void addTerminal(T terminal) {
		getGraph().addTerminal(terminal);
	}

	public void addNonterminal(Nt nonterminal) {
		getGraph().addNonterminal(nonterminal);
	}

	public void setRoot(Nt root) {
		getGraph().setRoot(root);
	}
}