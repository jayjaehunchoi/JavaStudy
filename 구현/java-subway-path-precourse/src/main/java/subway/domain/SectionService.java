package subway.domain;

import subway.repository.SectionRepository;

import java.util.List;

public class SectionService {

    public static int calculateTime(List<Station> stations) {
        int totalTime = 0;
        for (int i = 0; i < stations.size() - 1; i++) {
            Section findSection = SectionRepository.findByStartEnd(stations.get(i), stations.get(i + 1));
            totalTime += findSection.getTime();
        }
        return totalTime;
    }

    public static int calculateDistance(List<Station> stations) {
        int totalDistance = 0;
        for (int i = 0; i < stations.size() - 1; i++) {
            Section findSection = SectionRepository.findByStartEnd(stations.get(i), stations.get(i + 1));
            totalDistance += findSection.getDistance();
        }
        return totalDistance;
    }
}
