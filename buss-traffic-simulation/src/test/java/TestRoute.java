import com.telesens.afanasiev.simulation.Arc;
import com.telesens.afanasiev.simulation.Route;
import com.telesens.afanasiev.simulation.Station;
import com.telesens.afanasiev.simulation.impl.ArcImpl;
import com.telesens.afanasiev.simulation.Direct;
import com.telesens.afanasiev.simulation.impl.RouteImpl;
import com.telesens.afanasiev.simulation.impl.StationImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by oleg on 1/13/16.
 */
public class TestRoute {
    private Station[] station;
    private Station lastStation;
    private List<Arc<Station>> arcs;
    private Random random;

    @Before
    public void init() {
        random = new Random();
        station = new StationImpl[] {
                new StationImpl("Героев Труда"),
                new StationImpl("Караван"),
                new StationImpl("Жилярди"),
                new StationImpl("Герасимовская"),
                new StationImpl("Техникум"),
                new StationImpl("Кольцовская"),
                new StationImpl("Киевская"),
                new StationImpl("Поликлиника"),
                new StationImpl("Фарм. Академия"),
                new StationImpl("Горбатый мост"),
                new StationImpl("Московский пр."),
                new StationImpl("Советская")
        };

        lastStation = new StationImpl("Центральный рынок");

        arcs = new ArrayList<>();

        arcs.add(new ArcImpl<>(station[0], station[1], 5));
        arcs.add(new ArcImpl<>(station[1], station[2], 6));
        arcs.add(new ArcImpl<>(station[2], station[3], 4));
        arcs.add(new ArcImpl<>(station[3], station[4], 3));
        arcs.add(new ArcImpl<>(station[4], station[5], 7));
        arcs.add(new ArcImpl<>(station[5], station[6], 8));
        arcs.add(new ArcImpl<>(station[6], station[7], 9));
        arcs.add(new ArcImpl<>(station[7], station[8], 10));
        arcs.add(new ArcImpl<>(station[8], station[9], 11));
        arcs.add(new ArcImpl<>(station[9], station[10], 12));
        arcs.add(new ArcImpl<>(station[10], station[11], 15));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCreateEmptyNumber() {
        Route<Station> route = new RouteImpl<>("", "", Direct.FORWARD, 0, station[0], arcs.get(0), arcs.get(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCreateNullNumber() {
        Route<Station> route = new RouteImpl<>(null, "", Direct.FORWARD, 0, station[0], arcs.get(0), arcs.get(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCrateEmptyArcs() {
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCrateEmptyListArcs() {
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0],
                new ArrayList<>());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCrateIncorrectFirstArcs() {
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0], arcs.get(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRouteCrateIncorrectSequenceArcs() {
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0],
                arcs.get(0), arcs.get(2));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getNode() {
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0], arcs);

        String stationName;
        String nodeName;
        int k;

        for (int i = 0; i <= arcs.size(); i++) {
            stationName = station[i].getName();
            nodeName = route.getNode(i).getName();
            assertEquals(true, stationName.equals(nodeName));
        }

        for (int i = 0; i <= 1000_000; i++) {
            k = random.nextInt(arcs.size()-1) + 1;
            stationName = station[i % arcs.size()].getName();
            nodeName = route.getNode( (i + k) % arcs.size()).getName();
            assertEquals(false, stationName.equals(nodeName));
        }

        route.getNode(arcs.size() + 1);
    }

    @Test
    public void getFirstNode() {
        Route<Station> route;
        List<Arc<Station>> partArcs = new ArrayList<>();
        String stationName;
        String nodeName;

        for (int i = 0; i < arcs.size(); i++) {
            partArcs.clear();
            for (int j = 0; j <= i; j++)
                partArcs.add(arcs.get(j));

            route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0], partArcs);
            stationName = station[0].getName();
            nodeName = route.getFirstNode().getName();

            assertEquals(true, stationName.equals(nodeName));
        }
    }

    @Test
    public void getLastNode() {
        Route<Station> route;
        List<Arc<Station>> partArcs = new ArrayList<>();
        String stationName;
        String nodeName;

        for (int i = 0; i < arcs.size(); i++) {
            partArcs.clear();
            for (int j = 0; j <= i; j++)
                partArcs.add(arcs.get(j));

            route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0], partArcs);
            stationName = station[i+1].getName();
            nodeName = route.getLastNode().getName();

            assertEquals(true, stationName.equals(nodeName));
        }
    }

    @Test
    public void createLastArc() throws NoSuchFieldException, IllegalAccessException{
        Route<Station> route = new RouteImpl<>("272", "Favorite route", Direct.FORWARD, 0, station[0], arcs);
        String stationName;
        String nodeName;

        route.createLastArc(1000, lastStation, 10);
        stationName = station[0].getName();
        nodeName = route.getFirstNode().getName();
        assertEquals(true, stationName.equals(nodeName));

        for (int i = 0; i < station.length; i++) {
            stationName = station[i].getName();
            nodeName = route.getNode(i).getName();
            assertEquals(true, stationName.equals(nodeName));
        }

        stationName = lastStation.getName();

        nodeName = route.getLastNode().getName();
        assertEquals(true, stationName.equals(nodeName));

        nodeName = route.getNode(station.length).getName();
        assertEquals(true, stationName.equals(nodeName));
    }
}
