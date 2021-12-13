package subway.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShortestPathFinderTest {

    @Test
    void 최단경로_성공() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        List<Station> minDistanceRoute = shortestPathFinder.findMinDistanceRoute(new Station("교대역"), new Station("양재역"));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(minDistanceRoute).contains(new Station("교대역"), new Station("강남역"), new Station("양재역"));
            softAssertions.assertThat(minDistanceRoute.size()).isEqualTo(3);
        });
    }

    @Test
    void 최단시간_성공() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        List<Station> minTimeRoute = shortestPathFinder.findMinTimeRoute(new Station("교대역"), new Station("양재역"));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(minTimeRoute).contains(new Station("교대역"), new Station("남부터미널역"), new Station("양재역"));
            softAssertions.assertThat(minTimeRoute.size()).isEqualTo(3);
        });
    }

    @Test

}