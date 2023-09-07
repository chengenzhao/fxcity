package fyi.sample.javafx;

import com.whitewoodcity.javafx.binding.XBindings;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Reduce extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    var label = new Label();
    label.setAlignment(Pos.CENTER);
    label.textProperty().bind(XBindings.reduce(stage.widthProperty(), stage.heightProperty(),
      (w,h) -> "Area is " + (w.doubleValue() * h.doubleValue())));

    stage.setScene(new Scene(label, 800, 600));
    stage.show();
  }
}
