package subway.repository;

import subway.domain.Section;
import subway.domain.Station;
import subway.utils.ConstantMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionRepository {
   private static final List<Section> sectionList = new ArrayList<>();

   static {
       sectionList.add(new Section(new Station("교대역"), new Station("강남역"), 2, 3));
       sectionList.add(new Section(new Station("강남역"), new Station("역삼역"), 2, 3));
       sectionList.add(new Section(new Station("교대역"), new Station("남부터미널역"), 3, 2));
       sectionList.add(new Section(new Station("남부터미널역"), new Station("양재역"), 6, 5));
       sectionList.add(new Section(new Station("양재역"), new Station("매봉역"), 1, 1));
       sectionList.add(new Section(new Station("강남역"), new Station("양재역"), 2, 8));
       sectionList.add(new Section(new Station("양재역"), new Station("양재시민의숲역"), 10, 3));
   }

   public static List<Section> sections() {
       return Collections.unmodifiableList(sectionList);
   }

   public static Section findByStartEnd(Station start, Station end) {
       return sectionList.stream()
               .filter(section -> section.getStart().equals(start) && section.getEnd().equals(end) ||
                       section.getStart().equals(end) && section.getEnd().equals(start))
               .findFirst()
               .orElseThrow(() -> new IllegalArgumentException(ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_NO_ROUTE));
   }

}
