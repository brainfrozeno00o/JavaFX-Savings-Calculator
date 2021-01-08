import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SavingsCalculator extends Application{

    @Override
    public void start(Stage stage){
        BorderPane mainLayout = new BorderPane();

        NumberAxis xAxis = new NumberAxis(0, 20, 1); //say max of 20 years, will be editable in the future
        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Savings Counter");

        VBox topLayer = new VBox();
        topLayer.setSpacing(10);

        GridPane monthlySavingsSection = new GridPane();
        Label monthlySavingsLabel = new Label("Input your monthly savings here: ");
        TextField monthlySavingsTextField = new TextField("25");

        XYChart.Series<Number, Number> monthlySavingsSeries = new XYChart.Series();
        for (int i = 0; i <= (int) xAxis.getUpperBound(); ++i){
            monthlySavingsSeries.getData().add(new XYChart.Data(i, (Integer.valueOf(monthlySavingsTextField.getText()) * i * 12)));
        }

        monthlySavingsTextField.textProperty().addListener((observable, old_val, new_val) -> {
            lineChart.setAnimated(false);

            monthlySavingsSeries.getData().clear();
            for(int i = 0; i <= (int) xAxis.getUpperBound(); ++i){
                if (!new_val.isBlank()){
                    monthlySavingsSeries.getData().add(new XYChart.Data(i, (Integer.valueOf(new_val) * i * 12)));
                } else {
                    monthlySavingsSeries.getData().add(new XYChart.Data(0, 0));
                }
            }

            lineChart.setAnimated(true);
        });

        monthlySavingsSection.add(monthlySavingsLabel, 0, 0);
        monthlySavingsSection.add(monthlySavingsTextField, 1, 0);
        monthlySavingsSection.setHgap(3);

        monthlySavingsSeries.setName("Monthly Savings");

        lineChart.getData().add(monthlySavingsSeries);

        GridPane yearlyInterestSection = new GridPane();
        Label yearlyInterestLabel = new Label("Input your interest here (max up to 10%): ");
        TextField yearlySavingsTextField = new TextField("0");
        Label percentLabel = new Label("%");
        
        Label warningLabel = new Label("You cannot put an interest rate greater than 10%");

        XYChart.Series<Number, Number> yearlyInterestSeries = new XYChart.Series();

        yearlySavingsTextField.textProperty().addListener((observable, old_val, new_val) -> {
            lineChart.setAnimated(false);

            double previousInterest = 0.0;
            double setInterestValue = 0.0;
            yearlyInterestSeries.getData().clear();
            for(int i = 0; i <= (int) xAxis.getUpperBound(); ++i){
                if (!new_val.isBlank()){
                    if(Double.valueOf(new_val) > 10.0){
                        warningLabel.setVisible(true);
                        setInterestValue = Double.valueOf(old_val);
                    } else {
                        warningLabel.setVisible(false);
                        setInterestValue = Double.valueOf(new_val);                 
                    }
                    double currentMonthlySavings = i * Integer.valueOf(monthlySavingsTextField.getText()) * 12;
                    double currentSavingsInterest = currentMonthlySavings * (setInterestValue / 100.0);
                    double currentFinalInterest = currentMonthlySavings + currentSavingsInterest + previousInterest;
                    yearlyInterestSeries.getData().add(new XYChart.Data(i, currentFinalInterest));
                    previousInterest += currentFinalInterest * (setInterestValue / 100.0);   
                } else {
                    warningLabel.setVisible(false);
                    yearlyInterestSeries.getData().add(new XYChart.Data(0, 0));
                }
            }

            lineChart.setAnimated(true);
        });

        lineChart.getData().add(yearlyInterestSeries);

        yearlyInterestSection.add(yearlyInterestLabel, 0, 0);
        yearlyInterestSection.add(yearlySavingsTextField, 1, 0);
        yearlyInterestSection.add(percentLabel, 2, 0);
        yearlyInterestSection.setHgap(3);

        yearlyInterestSeries.setName("Yearly Interest");

        GridPane warningSection = new GridPane();

        warningLabel.setTextFill(Color.web("#FF0000"));
        warningLabel.setVisible(false);

        warningSection.add(warningLabel, 0, 0);

        topLayer.getChildren().addAll(monthlySavingsSection, yearlyInterestSection, warningSection);

        mainLayout.setTop(topLayer);
        mainLayout.setCenter(lineChart);

        mainLayout.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
