package subway.controller;

import subway.domain.SectionService;
import subway.domain.ShortestPathFinder;
import subway.domain.Station;
import subway.repository.StationRepository;
import subway.utils.ConstantMessages;
import subway.view.InputView;
import subway.view.OutputView;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public enum FindRouteFeature {
    MIN_DISTANCE("1", (scanner) -> {
        Station start = StationRepository.findByStationName(InputView.insertStart(scanner));
        Station end = StationRepository.findByStationName(InputView.insertEnd(scanner));
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        List<Station> stations = shortestPathFinder.findMinDistanceRoute(start, end);
        OutputView.printFindResult(stations, SectionService.calculateDistance(stations), SectionService.calculateTime(stations));
    }),
    MIN_TIME("2", (scanner) -> {
        Station start = StationRepository.findByStationName(InputView.insertStart(scanner));
        Station end = StationRepository.findByStationName(InputView.insertEnd(scanner));
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        List<Station> stations = shortestPathFinder.findMinTimeRoute(start, end);
        OutputView.printFindResult(stations, SectionService.calculateDistance(stations), SectionService.calculateTime(stations));
    }),
    BACK("B", scanner -> {});

    private String selection;
    private Consumer<Scanner> consumer;

    FindRouteFeature(String selection, Consumer<Scanner> consumer) {
        this.selection = selection;
        this.consumer = consumer;
    }

    public String getSelection() {
        return selection;
    }

    public Consumer<Scanner> getConsumer() {
        return consumer;
    }

    public static FindRouteFeature getFeatureByInput(String input) {
        return Arrays.stream(FindRouteFeature.values())
                .filter(value -> value.getSelection().equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_NO_FEATURE));
    }

    public void accept(Scanner scanner) {
        getConsumer().accept(scanner);
    }
}
