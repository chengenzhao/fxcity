package com.whitewoodcity.fxgl.service.icons;

import com.whitewoodcity.flame.ResizableCanvas;
import javafx.scene.paint.Paint;

public class Skip extends ResizableCanvas {

  private final Paint fill;

  public Skip(Paint fill) {
    this.fill = fill;
  }

  @Override
  public void draw() {
    var gc = getGraphicsContext2D();

    var width = getWidth();
    var height = getHeight();
    gc.clearRect(0, 0, width, height);
    gc.scale(width / 16, height / 16); //16 & 16 is viewbox size

    gc.beginPath();
    gc.setFill(fill);
    gc.appendSVGPath("M0 10V2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2zm4.79-6.907A.5.5 0 0 0 4 3.5v5a.5.5 0 0 0 .79.407L7.5 6.972V8.5a.5.5 0 0 0 .79.407L11 6.972V8.5a.5.5 0 0 0 1 0v-5a.5.5 0 0 0-1 0v1.528L8.29 3.093a.5.5 0 0 0-.79.407v1.528L4.79 3.093z");
    gc.fill();
  }
}
