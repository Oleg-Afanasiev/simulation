package com.telesens.afanasiev.reporter;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.jaxb.schemes.log.Runs;
import com.telesens.afanasiev.reporter.interfaces.ContentXmlWriter;
import com.telesens.afanasiev.reporter.interfaces.RunLogGetter;
import com.telesens.afanasiev.reporter.unit.RunLogUnit;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by oleg on 1/11/16.
 */
public class RouteXmlWriter implements ContentXmlWriter {
    private RunLogGetter logGetter;

    public RouteXmlWriter() {
        logGetter = LogCollector.getInstance();
    }

    @Override
    public void saveLogElements(Marshaller marshaller, XMLStreamWriter writer) throws JAXBException {
        Runs.Log runLog;
        RunLogUnit runLogUnit;
        while(!logGetter.isFinishCollect() || !logGetter.isEmptyRunLogsQueue()) {
            if (!logGetter.isEmptyRunLogsQueue()) {
                runLogUnit = logGetter.pollRunLog();
                runLog = new Runs.Log();

                runLog.setRouteId(runLogUnit.getRouteId());
                runLog.setRouteNumber(runLogUnit.getRouteNumber());
                runLog.setBusNumber(runLogUnit.getBusNumber());
                runLog.setTimeStart(DateTimeHelper.dateToXMLGregorianCalendar(runLogUnit.getTimeStart()));
                runLog.setTimeFinish(DateTimeHelper.dateToXMLGregorianCalendar(runLogUnit.getTimeFinish()));
                runLog.setPassDeliveredCount(runLogUnit.getPassDeliveredCount());

                JAXBElement<Runs.Log> element = new JAXBElement<>(QName.valueOf(Runs.Log.class.getSimpleName().toLowerCase()), Runs.Log.class, runLog);
                marshaller.marshal(element, writer);
            }
        } // while
    }
}
