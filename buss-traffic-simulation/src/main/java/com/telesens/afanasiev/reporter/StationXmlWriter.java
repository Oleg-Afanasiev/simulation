package com.telesens.afanasiev.reporter;

import com.telesens.afanasiev.helper.DateTimeHelper;
import com.telesens.afanasiev.jaxb.logs.Stations;
import com.telesens.afanasiev.reporter.interfaces.ContentXmlWriter;
import com.telesens.afanasiev.reporter.interfaces.StationLogGetter;
import com.telesens.afanasiev.reporter.unit.StationLogUnit;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by oleg on 1/11/16.
 */
public class StationXmlWriter implements ContentXmlWriter {
    private StationLogGetter logGetter;

    public StationXmlWriter() {
        logGetter = LogCollector.getInstance();
    }

    @Override
    public void saveLogElements(Marshaller marshaller, XMLStreamWriter writer) throws JAXBException {
        Stations.Log stationLog;
        StationLogUnit stationLogUnit;
        while(!logGetter.isFinishCollect() || !logGetter.isEmptyStationLogsQueue()) {
            if (!logGetter.isEmptyStationLogsQueue()) {
                stationLogUnit = logGetter.pollStationLog();
                stationLog = new Stations.Log();

                stationLog.setStationId(stationLogUnit.getStationId());
                stationLog.setBusId(stationLogUnit.getBusId());
                stationLog.setRouteId(stationLogUnit.getRouteId());
                stationLog.setTimeBusStop(DateTimeHelper.timeToXMLGregorianCalendar(stationLogUnit.getTimeBusStop()));
                stationLog.setBusNumber(stationLogUnit.getBusNumber());
                stationLog.setTakeInPassCount(stationLogUnit.getTakeInPassCount());
                stationLog.setGetOffPassCount(stationLogUnit.getGetOffPassCount());
                stationLog.setStayPassCount(stationLogUnit.getStayPassCount());

                JAXBElement<Stations.Log> element = new JAXBElement<>(QName.valueOf(Stations.Log.class.getSimpleName().toLowerCase()), Stations.Log.class, stationLog);
                marshaller.marshal(element, writer);
            }
        } // while
    }
}
