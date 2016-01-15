package com.telesens.afanasiev.dao.impl.jaxb;

import com.telesens.afanasiev.dao.impl.jaxb.schemes.BusNetwork;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.PassGenRules;
import com.telesens.afanasiev.dao.impl.jaxb.schemes.RunTimetable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by oleg on 1/13/16.
 */
public class DAOLoaderData {

    public static DAOLoaderData uniqueInstance;
    private String filePassGenRulesXML = "./src/main/resources/xml/load/passgeneration.xml";
    private String fileRunTimetableXML = "./src/main/resources/xml/load/runtimetable.xml";
    private String fileBusNetWorkXML = "./src/main/resources/xml/load/busnetwork.xml";

    private DAOLoaderData() {

    }

    public static DAOLoaderData getInstance() {
        if (uniqueInstance == null) {
            synchronized (DAOLoaderData.class) {
                if (uniqueInstance == null)
                    uniqueInstance = new DAOLoaderData();
            }
        }

        return uniqueInstance;
    }

    public PassGenRules getPassGenRulesData() {
        return (PassGenRules)getData(filePassGenRulesXML, PassGenRules.class);
    }

    public RunTimetable getRunTimetableData() {
        return (RunTimetable)getData(fileRunTimetableXML, RunTimetable.class);
    }

    public BusNetwork getBusNetwork() {
        return (BusNetwork)getData(fileBusNetWorkXML, BusNetwork.class);
    }

    private Object getData(String fileName, Class classParser) {
        Object data = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classParser);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            data = unmarshaller.unmarshal(new FileInputStream(fileName));

        } catch(JAXBException | FileNotFoundException exc) {
            exc.printStackTrace();
        }

        return data;
    }
}
