package subway.domain;

public class Section {
    private Station start;
    private Station end;
    private int distance;
    private int time;

    public Section(Station start, Station end, int distance, int time) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.time = time;
    }

    public Station getStart() {
        return start;
    }

    public Station getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }
}
