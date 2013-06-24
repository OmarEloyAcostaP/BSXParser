package bsxParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * BSXParser is a guided API for XML files that integrates an information support in order to
 * save information between events.
 */

public class BsxParser {
	private Writer writer;
	private XMLStreamWriter xmlStreamWriter;
	private Reader reader;
	private XMLStreamReader xmlStreamReader;
	private Attributes attributes;

	public BsxParser(String input, String output) {
		try {
			this.reader = new FileReader(input);
			this.xmlStreamReader = XMLInputFactory.newInstance()
					.createXMLStreamReader(reader);
			this.writer = new FileWriter(output);
			this.xmlStreamWriter = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(writer);
			this.attributes = new Attributes();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close. It method is needed to be executed at the end of the BSXParser usage.
	 */
	public void close() {
		try {
			this.xmlStreamReader.close();
			this.reader.close();
			this.xmlStreamWriter.flush();
			this.xmlStreamWriter.close();
			this.writer.flush();
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------

	/**
	 * Check out whether there are more events. 
	 * Returns true if there are more parsing events and false 
	 * if there are no more events. This method will return 
	 * false if the current state of the BSXParser is the end of the
	 * document.
	 * 
	 * @return true if there are more events, false otherwise 
	 */
	public boolean hasNext() {
		try {
			return this.xmlStreamReader.hasNext();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Get next parsing event
	 * 
	 * @return the integer code corresponding to the current parse event 
	 */
	public int next(){
		try {
			return this.xmlStreamReader.next();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Returns the (local) name of the current event
	 * 
	 * @return the localName.
	 */
	public String getLocalName() {
		return this.xmlStreamReader.getLocalName();
	}

	/**
	 * Returns the current value of the parse event as a string
	 * 
	 * @return the current text or null 
	 */
	public String getText() {
		return this.xmlStreamReader.getText();
	}

	/**
	 * Returns the localName of the attribute at the provided index
	 * 
	 * @param index the position of the attribute.
	 * @return the localName of the attribute.
	 */
	public String getAttributeLocalName(int index) {
		return this.xmlStreamReader.getAttributeLocalName(index);
	}

	/**
	 * Get the target of a processing instruction
	 * 
	 * @return the target or null
	 */
	public String getPITarget() {
		return this.xmlStreamReader.getPITarget();
	}

	/**
	 * Get the data section of a processing instruction
	 * 
	 * @return the data or null
	 */
	public String getPIData() {
		return this.xmlStreamReader.getPIData();
	}

	/**
	 * returns the number of attributes
	 * 
	 * @return returns the number of attributes 
	 */
	public int getAttributeCount() {
		return this.xmlStreamReader.getAttributeCount();
	}

	/**
	 * Returns the value of the attribute at the index
	 *
	 * @param index  the position of the attribute  
	 * @return the attribute value
	 */
	public String getAttributeValue(int index) {
		return this.xmlStreamReader.getAttributeValue(index);
	}
	
	/**
	 * Returns the index of the attribute at the name
	 * 
	 * @param name the name of the attribute
	 * @return the index of the attribute or null
	 */
	public int getIndex(String name){
		for (int index = 0 ; index < getAttributeCount(); index++){
			if (getAttributeLocalName(index).equals(name))
				return index;
		}
		return -1;
	}

	// ----------------------------------------------------------
	
	/**
	 * Adds a register to the information support
	 * 
	 * @param name the name of the register to add
	 * @param value the value of the register to add
	 */
	public void addRegister(String name, String value) {
		this.attributes.put(new Name(name), value);
	}

	/**
	 * Removes a register from the information support
	 * 
	 * @param name the name of the register to be removed
	 */
	public void removeRegister(String name) {
		this.attributes.remove(new Name(name));
	}

	/**
	 * Returns the values of the register at the name
	 * 
	 * @param name the name of the register
	 * @return the value of the register
	 */
	public String getRegisterValue(String name) {
		return this.attributes.getValue(new Name(name));
	}

	// ----------------------------------------------------------

	/**
	 * Write text to the output
	 * 
	 * @param text the values to write
	 * @throws XMLStreamException
	 */
	public void writeCharacters(String text) throws XMLStreamException {
		this.xmlStreamWriter.writeCharacters(text);
	}

	/**
	 * Writes a start tag to the output. All writeStartElement methods open a
	 * new scope in the internal namespace context. Writing the corresponding
	 * EndElement causes the scope to be closed.
	 * 
	 * @param localName local name of the tag, may not be null
	 * @throws XMLStreamException
	 */
	public void writeStartElement(String localName) throws XMLStreamException {
		this.xmlStreamWriter.writeStartElement(localName);
	}

	/**
	 * Writes an xml comment with the data enclosed
	 * 
	 * @param comment the data contained in the comment, may be null
	 * @throws XMLStreamException
	 */
	public void writeComment(String comment) throws XMLStreamException {
		this.xmlStreamWriter.writeComment(comment);
	}
	
	/**
	 * Writes an attribute to the output stream without a prefix.
	 * 
	 * @param localName the local name of the attribute
	 * @param value the value of the attribute
	 * @throws XMLStreamException
	 */
	public void writeAttribute(String localName, String value)
			throws XMLStreamException {
		this.xmlStreamWriter.writeAttribute(localName, value);
	}

	/**
	 * 	Writes a processing instruction
	 * 
	 * @param piTarget the target of the processing instruction, may not be null
	 * @param piData the data contained in the processing instruction, may not be null 
	 * @throws XMLStreamException
	 */
	public void writeProcessingInstruction(String piTarget, String piData)
			throws XMLStreamException {
		this.xmlStreamWriter.writeProcessingInstruction(piTarget, piData);
	}

	/**
	 * Closes any start tags and writes corresponding end tags.
	 * 
	 * @throws XMLStreamException
	 */
	public void writeEndDocument() throws XMLStreamException {
		this.xmlStreamWriter.writeEndDocument();
	}

	/**
	 * Write the XML Declaration. Defaults the XML version to 1.0, and the encoding to utf-8
	 * 
	 * @throws XMLStreamException
	 */
	public void writeStartDocument() throws XMLStreamException {
		this.xmlStreamWriter.writeStartDocument();
	}

	/**
	 * Closes any start tags and writes corresponding end tags.
	 * 
	 * @throws XMLStreamException
	 */
	public void writeEndElement() throws XMLStreamException {
		this.xmlStreamWriter.writeEndElement();
	}
}
