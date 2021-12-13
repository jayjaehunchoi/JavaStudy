package subway.repository;

import org.junit.jupiter.api.Test;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StationRepositoryTest {

    @Test
    void 역_최초_생성() {
        List<Station> stations = StationRepository.stations();

        assertThat(stations.size()).isEqualTo(7);
    }

}