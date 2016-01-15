package com.telesens.afanasiev.model.reporter;

import com.telesens.afanasiev.model.reporter.interfaces.ContentXmlWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by oleg on 1/8/16.
 */
public class XmlWriter implements Runnable {
    private String dstXML;
    private Class rootClass;
    private Class logClass;
    private JAXBContext jaxbContext;
    private ContentXmlWriter contentXmlWriter;

    public XmlWriter(String dstXML, Class rootClass, Class logClass, ContentXmlWriter contentXmlWriter) {
        this.dstXML = dstXML;
        this.rootClass = rootClass;
        this.logClass = logClass;
        this.contentXmlWriter = contentXmlWriter;
    }

    @Override
    public void run() {
        writeToXML();
    }

    private void writeToXML() {

        try {
            FileOutputStream fos = new  FileOutputStream(dstXML);
            XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(fos);

            jaxbContext = JAXBContext.newInstance(logClass);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            writer.writeStartDocument();
            writer.writeStartElement(rootClass.getSimpleName().toLowerCase());

            contentXmlWriter.saveLogElements(marshaller, writer);

            writer.writeEndDocument();
            writer.close();

        } catch(JAXBException | IOException | XMLStreamException exc) {
            exc.printStackTrace();
        }
    }
}
