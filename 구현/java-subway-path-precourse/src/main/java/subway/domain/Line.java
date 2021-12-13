package subway.domain;

import subway.utils.ConstantMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line {
    private String name;
    private List<Station> stations = new ArrayList<>();

    public Line(String name, List<Station> stations) {
        this.name = name;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public Station getStationByName(String name) {
        return stations.stream()
                .filter(station ->
                        station.getName().equals(name))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                        ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_STATION_NOT_ENROLL_LINE));
    }
}
