package subway.controller;

import subway.view.InputView;
import subway.view.OutputView;

import java.util.Scanner;

public class Controller {
    private MainFeature mainFeature = null;
    public void run(Scanner scanner) {
        while (mainFeature != MainFeature.QUIT) {
            OutputView.printMain();
            mainFeature = selectMainFeature(scanner);
            if(mainFeature != MainFeature.QUIT) {
                doRouteFeature(scanner);
            }
        }
    }

    private void doRouteFeature(Scanner scanner) {
        try{
            FindRouteFeature
                    .getFeatureByInput(mainFeature.apply(scanner))
                    .accept(scanner);
        }catch (IllegalArgumentException e) {
            OutputView.printGuideMessage(e.getMessage());
            doRouteFeature(scanner);
        }
    }

    private MainFeature selectMainFeature(Scanner scanner) {
        try{
           mainFeature = MainFeature.getMainFeatureByInput(InputView.insertFeature(scanner));
        }catch (IllegalArgumentException e) {
            OutputView.printGuideMessage(e.getMessage());
            return selectMainFeature(scanner);
        }
        return mainFeature;
    }
}
