package com.telesens.afanasiev.reporter.interfaces;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by oleg on 1/10/16.
 */
public interface ContentXmlWriter {
    void saveLogElements(Marshaller marshaller, XMLStreamWriter writer) throws JAXBException;
}
