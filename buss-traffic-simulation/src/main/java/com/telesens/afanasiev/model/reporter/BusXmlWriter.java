package com.telesens.afanasiev.model.reporter;

import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.logs.shemes.Buses;
import com.telesens.afanasiev.model.reporter.interfaces.BusLogGetter;
import com.telesens.afanasiev.model.reporter.unit.BusLogUnit;
import com.telesens.afanasiev.model.reporter.interfaces.ContentXmlWriter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by oleg on 1/10/16.
 */
public class BusXmlWriter implements ContentXmlWriter {
    private BusLogGetter logGetter;

    public BusXmlWriter() {
        logGetter = LogCollector.getInstance();
    }

    @Override
    public void saveLogElements(Marshaller marshaller, XMLStreamWriter writer) throws JAXBException {
        Buses.Log busLog;
        BusLogUnit busLogUnit;
        while(!logGetter.isFinishCollect() || !logGetter.isEmptyBusLogsQueue()) {
            if (!logGetter.isEmptyBusLogsQueue()) {
                busLogUnit = logGetter.pollBusLog();
                busLog = new Buses.Log();

                //busLog.setBusID(busLogUnit.);
                busLog.setBusCapacity(busLogUnit.getBusCapacity());
                busLog.setBusNumber(busLogUnit.getBusNumber());
                busLog.setTimeStop(DateTimeHelper.dateToXMLGregorianCalendar(busLogUnit.getTimeStop()));
                busLog.setRouteId(busLogUnit.getRouteId());
                busLog.setStationId(busLogUnit.getStationId());
                busLog.setGetOffPassCount(busLogUnit.getGetOffPassCount());
                busLog.setTakeInPassCount(busLogUnit.getTakeInPassCount());
                busLog.setInsidePassCount(busLogUnit.getInsidePassCount());

                JAXBElement<Buses.Log> element = new JAXBElement<>(QName.valueOf(Buses.Log.class.getSimpleName().toLowerCase()), Buses.Log.class, busLog);
                marshaller.marshal(element, writer);
            }
        } // while
    }
}
