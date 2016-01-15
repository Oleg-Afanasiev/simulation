package com.telesens.afanasiev.model.reporter;

import com.telesens.afanasiev.model.helper.DateTimeHelper;
import com.telesens.afanasiev.logs.shemes.Passengers;
import com.telesens.afanasiev.model.reporter.interfaces.ContentXmlWriter;
import com.telesens.afanasiev.model.reporter.interfaces.PassengerLogGetter;
import com.telesens.afanasiev.model.reporter.unit.PassengerLogUnit;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

/**
 * Created by oleg on 1/11/16.
 */
public class PassengerXmlWriter implements ContentXmlWriter {
    private PassengerLogGetter logGetter;

    public PassengerXmlWriter() {
        logGetter = LogCollector.getInstance();
    }

    @Override
    public void saveLogElements(Marshaller marshaller, XMLStreamWriter writer) throws JAXBException {
        Passengers.Log passLog;
        PassengerLogUnit passLogUnit;
        while(!logGetter.isFinishCollect() || !logGetter.isEmptyPassLogsQueue()) {
            if (!logGetter.isEmptyPassLogsQueue()) {
                passLogUnit = logGetter.pollPassLog();
                passLog = new Passengers.Log();

                passLog.setPassengerId(passLogUnit.getPassengerId());
                passLog.setStationId(passLogUnit.getStationId());
                passLog.setBusNumber(passLogUnit.getBusNumber());
                passLog.setTimeComeIn(DateTimeHelper.dateToXMLGregorianCalendar(passLogUnit.getTimeComeIn()));
                passLog.setTimeGoOut(DateTimeHelper.dateToXMLGregorianCalendar(passLogUnit.getTimeGoOut()));

                JAXBElement<Passengers.Log> element = new JAXBElement<>(
                        QName.valueOf(Passengers.Log.class.getSimpleName().toLowerCase()), Passengers.Log.class, passLog);
                marshaller.marshal(element, writer);
            }
        } // while
    }
}
