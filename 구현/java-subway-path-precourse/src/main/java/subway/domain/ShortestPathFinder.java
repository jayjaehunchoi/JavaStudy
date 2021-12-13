package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.utils.ConstantMessages;

import java.util.List;

public class ShortestPathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public ShortestPathFinder() {
        initGraph();
        addVertexes();
    }

    public List<Station> findMinTimeRoute(Station start, Station end) {
        validateDuplicateStation(start, end);
        addTimeWeightOnGraph();
        return getBestPath(start, end);
    }

    public List<Station> findMinDistanceRoute(Station start, Station end) {
        addDistanceWeightOnGraph();
        return getBestPath(start, end);
    }
    
    private void initGraph() {
        addVertexes();
        addTimeWeightOnGraph();
    }

    private void addTimeWeightOnGraph() {
        SectionRepository.sections()
                .forEach(section -> 
                        setEdges(
                                graph, 
                                section.getStart(), 
                                section.getEnd(), 
                                section.getTime()
                        )
                );
    }

    private void addDistanceWeightOnGraph() {
        SectionRepository.sections()
                .forEach(section ->
                        setEdges(
                                graph,
                                section.getStart(),
                                section.getEnd(),
                                section.getDistance()
                        )
                );
    }

    private List<Station> getBestPath(Station start, Station end) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(start, end);
        validateRoute(path);
        return path.getVertexList();
    }

    private void addVertexes() {
        StationRepository.stations()
                .forEach(station -> graph.addVertex(station));
    }

    private void validateRoute(GraphPath path) {
        if(path == null) {
            throw new IllegalArgumentException(ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_NO_ROUTE);
        }
    }

    private void validateDuplicateStation(Station start, Station end) {
        if(start.equals(end)){
            throw new IllegalArgumentException(ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_STATION_NAME_SAME);
        }
    }

    private void setEdges(
            WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            Station start,
            Station end,
            int weight
    ) {
        graph.setEdgeWeight(graph.addEdge(start, end), weight);
    }
    
}
