package enum_posting;

import java.util.Arrays;
import java.util.List;

public enum Level {
    LEVEL1("레벨1", Arrays.asList(Mission.RACINGCAR, Mission.LOTTO)),
    LEVEL2("레벨2", Arrays.asList(Mission.CHESS, Mission.BLACKJACK));

    private final String name;
    private final List<Mission> missions;

    Level(String name, List<Mission> missions) {
        this.name = name;
        this.missions = missions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("의 미션은 ");
        missions.forEach((mission -> sb.append(mission.getName())
                .append(" ")));
        return sb.toString();
    }
}
