package com.whitewoodcity.fxgl.service.icons.rankbadge;

import com.whitewoodcity.flame.ResizableCanvas;
import javafx.scene.paint.Color;

public class Petals extends ResizableCanvas {
  private final int level;

  public Petals(int level) {
    super();
    this.level = level;
  }

  @Override
  public void draw() {
    var gc = getGraphicsContext2D();
    gc.save();
    var width = getWidth();
    var height = getHeight();
    gc.clearRect(0, 0, width, height);
    gc.scale(width / 512.002, height / 512.002);

    fillSVGPath(gc, "3cb54a", "m 119.287,388.062 c -4.552,-2.96 -7.288,-8.016 -7.28,-13.439 V 31.999 h 287.987 v 342.624 c 0.008,5.424 -2.728,10.48 -7.28,13.439 L 256,476.858 Z");
    fillSVGPath(gc, "0e9347", "m 387.995,103.995 c -6.624,0 -11.999,5.376 -11.999,11.999 v 73.197 c 0,5.968 -4.832,10.799 -10.799,10.799 -0.024,0 -0.056,0 -0.08,0 h -1.12 c -6.584,-0.048 -11.951,5.256 -11.999,11.839 0,0.024 0,0.056 0,0.08 v 73.277 c 0,5.968 -4.832,10.799 -10.799,10.799 -0.024,0 -0.056,0 -0.08,0 h -1.04 c -6.624,-0.048 -12.031,5.296 -12.079,11.919 0,0.024 0,0.056 0,0.08 v 35.998 c -0.056,35.566 -18.119,68.693 -47.998,87.996 v 0 c -15.015,9.88 -24.039,26.663 -23.999,44.638 L 392.717,388.06 c 4.168,-2.68 6.856,-7.144 7.28,-12.079 v 0 -271.986 z");
    fillSVGPath(gc, "89c763", "m 112.008,31.999 v 231.989 h 11.999 c 6.624,0 11.999,-5.376 11.999,-11.999 v 0 -81.196 c 0.16,-6.136 5.272,-10.983 11.415,-10.815 0.144,0 0.288,0.008 0.424,0.016 v 0 c 6.624,0.088 12.071,-5.216 12.159,-11.839 0,-0.056 0,-0.104 0,-0.16 V 87.996 c 0,-17.671 14.327,-31.999 31.998,-31.999 h 151.993 c 13.255,0 23.999,-10.743 23.999,-23.999 H 112.008 Z");

    switch (level) {
      case 1 -> {
        fillSVGPath(gc, "0e9347", "M287.44,221.03l-15.999-39.998c-1.624-4.112-6.272-6.12-10.384-4.496  c-2.056,0.816-3.688,2.44-4.496,4.496l-15.999,39.998c-0.76,1.904-0.76,4.016,0,5.92l15.999,39.998  c1.624,4.112,6.272,6.12,10.384,4.496c2.056-0.816,3.688-2.44,4.496-4.496l15.999-39.998  C288.192,225.045,288.192,222.934,287.44,221.03z");
        gc.setFill(Color.web("#2E485D"));
        gc.fillOval(255.985, 359.983, 8, 8);
        gc.fillOval(255.985, 119.994, 8, 8);
        gc.fillOval(319.982, 215.99, 8, 8);
        gc.fillOval(191.988, 215.99, 8, 8);
        gc.fillOval(255.985, 71.997, 8, 8);
        gc.fillOval(255.985, 311.985, 8, 8);
        fillSVGPath(gc, "ffffff", """
          M279.44,213.03l-15.999-39.998c-1.624-4.112-6.272-6.12-10.384-4.496
          c-2.056,0.816-3.688,2.44-4.496,4.496l-15.999,39.998c-0.76,1.904-0.76,4.016,0,5.92l15.999,39.998
          c1.624,4.112,6.272,6.12,10.383,4.496c2.056-0.816,3.688-2.44,4.496-4.496l15.999-39.998
          C280.192,217.046,280.192,214.934,279.44,213.03z""");
        fillSVGPath(gc, "dddfe1", "M263.441,258.948l15.999-39.998c0.76-1.904,0.76-4.016,0-5.92l-15.999-39.998c-1.2-3.04-7.44,0-7.44,10.959v79.996C259.273,263.988,262.225,261.996,263.441,258.948z");
      }
      case 2 -> {
        fillSVGPath(gc, "0e9347", "m 287.44,277.027 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.384,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.752,-1.904 0.752,-4.016 0,-5.92 z m 0,-111.995 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.384,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.752,-1.904 0.752,-4.016 0,-5.92 z");
        gc.setFill(Color.web("#2E485D"));
        gc.fillOval(255.985, 375.982, 8, 8);
        gc.fillOval(255.985, 87.996, 8, 8);
        gc.fillOval(311.982, 159.993, 8, 8);
        gc.fillOval(311.982, 271.987, 8, 8);
        gc.fillOval(207.987, 159.993, 8, 8);
        gc.fillOval(311.982, 215.99, 8, 8);
        gc.fillOval(207.987, 215.99, 8, 8);
        gc.fillOval(207.987, 271.987, 8, 8);
        gc.fillOval(255.985, 55.997, 8, 8);
        gc.fillOval(255.985, 343.984, 8, 8);
        fillSVGPath(gc, "ffffff", "m 279.44,269.027 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.383,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.753,-1.904 0.753,-4.016 10e-4,-5.92 z m 0,-111.994 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.383,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.753,-1.905 0.753,-4.016 10e-4,-5.92 z");
        fillSVGPath(gc, "dddfe1", "m 263.441,202.951 15.999,-39.998 c 0.76,-1.904 0.76,-4.016 0,-5.92 l -15.999,-39.998 c -1.2,-3.04 -7.44,0 -7.44,10.959 v 79.996 c 3.272,0 6.224,-1.992 7.44,-5.039 z m 0,111.994 15.999,-39.998 c 0.76,-1.904 0.76,-4.016 0,-5.92 l -15.999,-39.998 c -1.2,-3.04 -7.44,0 -7.44,10.959 v 79.996 c 3.272,0.001 6.224,-1.991 7.44,-5.039 z");
      }
      case 3 -> {
        fillSVGPath(gc, "0e9347", "m 290.32624,283.073 -16.8,-39.2 c -1.6,-4 0,-8.8 4.8,-10.4 1.6,-0.8 4,-0.8 5.6,0 l 39.2,16.8 c 1.6,0.8 3.2,2.4 4,4 l 16.8,39.2 c 1.6,4 0,8.8 -4.8,10.4 -1.6,0.8 -4,0.8 -5.6,0 l -39.2,-16.8 c -1.6,-0.8 -3.2,-2.4 -4,-4 z M 238.4,282.4 255.2,243.2 c 1.6,-4 0,-8.8 -4.8,-10.4 -1.6,-0.8 -4,-0.8 -5.6,0 l -39.2,16.8 c -1.6,0.8 -3.2,2.4 -4,4 l -16.8,39.2 c -1.6,4 0,8.8 4.8,10.4 1.6,0.8 4,0.8 5.6,0 l 39.2,-16.8 c 1.6,-0.8 3.2,-2.4 4,-4 z m 48.8,-117.6 -16,-40 c -1.6,-4 -6.4,-6.4 -10.4,-4.8 -2.4,0.8 -4,2.4 -4.8,4.8 l -16,40 c -0.8,1.6 -0.8,4 0,5.6 l 16,40 c 1.6,4 6.4,6.4 10.4,4.8 2.4,-0.8 4,-2.4 4.8,-4.8 l 16,-40 c 0.8,-1.6 0.8,-3.2 0,-5.6 z");
        gc.setFill(Color.web("#2E485D"));
        gc.fillOval(256, 376, 8, 8);
        gc.fillOval(256, 88, 8, 8);
        gc.fillOval(312, 208, 8, 8);
        gc.fillOval(256, 288, 8, 8);
        gc.fillOval(208, 208, 8, 8);
        gc.fillOval(256, 56, 8, 8);
        gc.fillOval(256, 344, 8, 8);
        fillSVGPath(gc, "ffffff", "m 281.6,274.4 -16.8,-39.2 c -1.6,-4 0,-8.8 4.8,-10.4 1.6,-0.8 4,-0.8 5.6,0 l 39.2,16.8 c 1.6,0.8 3.2,2.4 4,4 l 16.8,39.2 c 1.6,4 0,8.8 -4.8,10.4 -1.6,0.8 -4,0.8 -5.6,0 l -39.2,-16.8 c -1.6,-0.8 -3.2,-2.4 -4,-4 z m -51.2,0 16.8,-39.2 c 1.6,-4 0,-8.8 -4.8,-10.4 -1.6,-0.8 -4,-0.8 -5.6,0 l -39.2,16.8 c -1.6,0.8 -3.2,2.4 -4,4 l -16.8,39.2 c -1.6,4 0,8.8 4.8,10.4 1.6,0.8 4,0.8 5.6,0 l 39.2,-16.8 c 1.6,-0.8 3.2,-2.4 4,-4 z m 48.8,-117.6 -16,-40 c -1.6,-4 -6.4,-6.4 -10.4,-4.8 -2.4,0.8 -4,2.4 -4.8,4.8 l -16,40 c -0.8,1.6 -0.8,4 0,5.6 l 16,40 c 1.6,4 6.4,6.4 10.4,4.8 2.4,-0.8 4,-2.4 4.8,-4.8 l 16,-40 c 0.8,-1.6 0.8,-3.2 0,-5.6 z");
        fillSVGPath(gc, "dddfe1", "m 263.2,202.2 16,-40 c 0.8,-1.6 0.8,-4 0,-5.6 l -16,-40 c -0.8,-3.2 -7.2,0 -7.2,11.2 v 80 c 3.2,0.2 6.4,-1.4 7.2,-4.6 z m 61.6,93 -39.2,-16.8 c -1.6,-0.8 -3.2,-2.4 -4,-4 l -16.8,-39.2 c -1.6,-3.2 4.8,-4.8 12.8,2.4 l 56,56 c -2.4,2.4 -5.6,3.2 -8.8,1.6 z m -137.6,0 39.2,-16.8 c 1.6,-0.8 3.2,-2.4 4,-4 l 16.8,-39.2 c 1.6,-3.2 -4.8,-4.8 -12.8,2.4 l -56,56 c 2.4,2.4 5.6,3.2 8.8,1.6 z");
      }
      case 4 -> {
        fillSVGPath(gc, "0e9347", "M 316.958,247.429 276.96,231.43 c -4.112,-1.624 -6.12,-6.272 -4.496,-10.384 0.816,-2.056 2.44,-3.688 4.496,-4.496 l 39.998,-15.999 c 1.904,-0.76 4.016,-0.76 5.92,0 l 39.998,15.999 c 4.112,1.624 6.12,6.272 4.496,10.384 -0.816,2.056 -2.44,3.688 -4.496,4.496 l -39.998,15.999 c -1.896,0.76 -4.016,0.76 -5.92,0 z m -105.995,-0.001 39.998,-15.999 c 4.112,-1.624 6.12,-6.272 4.496,-10.384 -0.816,-2.056 -2.44,-3.688 -4.496,-4.496 L 210.963,200.55 c -1.904,-0.76 -4.016,-0.76 -5.92,0 l -39.998,15.999 c -4.112,1.624 -6.12,6.272 -4.496,10.384 0.816,2.056 2.44,3.688 4.496,4.496 l 39.998,15.999 c 1.896,0.76 4.016,0.76 5.92,0 z m 76.477,29.599 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.384,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.752,-1.904 0.752,-4.016 0,-5.92 z m 0,-111.995 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.384,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.752,-1.904 0.752,-4.016 0,-5.92 z");
        gc.setFill(Color.web("#2E485D"));
        gc.fillOval(255.985, 375.982, 8, 8);
        gc.fillOval(255.985, 87.996, 8, 8);
        gc.fillOval(311.982, 159.993, 8, 8);
        gc.fillOval(311.982, 271.987, 8, 8);
        gc.fillOval(207.987, 159.993, 8, 8);
        gc.fillOval(207.987, 271.987, 8, 8);
        gc.fillOval(255.985, 55.997, 8, 8);
        gc.fillOval(255.985, 343.984, 8, 8);
        fillSVGPath(gc, "ffffff", "m 202.964,239.429 39.998,-15.999 c 4.112,-1.624 6.12,-6.272 4.496,-10.384 -0.816,-2.056 -2.44,-3.688 -4.496,-4.496 l -39.998,-15.999 c -1.904,-0.76 -4.016,-0.76 -5.92,0 l -39.998,15.999 c -4.112,1.624 -6.12,6.272 -4.496,10.384 0.816,2.056 2.44,3.688 4.496,4.496 l 39.998,15.999 c 1.896,0.76 4.016,0.76 5.92,0 z m 111.994,0 39.998,-15.999 c 4.112,-1.624 6.12,-6.272 4.496,-10.384 -0.816,-2.056 -2.44,-3.688 -4.496,-4.496 l -39.998,-15.999 c -1.904,-0.76 -4.016,-0.76 -5.92,0 L 269.04,208.55 c -4.112,1.624 -6.12,6.272 -4.496,10.384 0.816,2.056 2.44,3.688 4.496,4.496 l 39.998,15.999 c 1.896,0.76 4.016,0.76 5.92,0 z m -35.518,29.598 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.383,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.753,-1.904 0.753,-4.016 10e-4,-5.92 z m 0,-111.994 -15.999,-39.998 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.383,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.753,-1.905 0.753,-4.016 10e-4,-5.92 z");
        fillSVGPath(gc, "dddfe1", "m 157.046,223.43 39.998,15.999 c 1.904,0.76 4.016,0.76 5.92,0 l 39.998,-15.999 c 3.048,-1.216 5.04,-4.16 5.04,-7.44 h -95.996 c -0.008,3.28 1.992,6.224 5.04,7.44 z m 111.994,0 39.998,15.999 c 1.904,0.76 4.016,0.76 5.92,0 l 39.998,-15.999 c 3.048,-1.216 5.04,-4.16 5.04,-7.44 h -95.995 c -0.008,3.28 1.992,6.224 5.039,7.44 z m -5.599,-20.479 15.999,-39.998 c 0.76,-1.904 0.76,-4.016 0,-5.92 l -15.999,-39.998 c -1.2,-3.04 -7.44,0 -7.44,10.959 v 79.996 c 3.272,0 6.224,-1.992 7.44,-5.039 z m 0,111.994 15.999,-39.998 c 0.76,-1.904 0.76,-4.016 0,-5.92 l -15.999,-39.998 c -1.2,-3.04 -7.44,0 -7.44,10.959 v 79.996 c 3.272,0.001 6.224,-1.991 7.44,-5.039 z");
      }
      default -> {
        fillSVGPath(gc, "0e9347", "m 211.83203,186.74176 -42.98433,2.8559 c -4.41259,0.27382 -7.75861,4.07384 -7.48478,8.48642 0.14073,2.20752 1.18093,4.2615 2.8866,5.66529 l 33.0964,27.57601 c 1.57597,1.31118 3.58459,1.96382 5.63025,1.82938 l 42.98433,-2.85588 c 4.4126,-0.27384 7.75862,-4.07385 7.48479,-8.48644 -0.14074,-2.20753 -1.18094,-4.2615 -2.88662,-5.66529 l -33.09639,-27.57602 c -1.57938,-1.30386 -3.58706,-1.9562 -5.63025,-1.82937 z m -4.62422,76.17081 -10.56677,41.76305 c -1.10314,4.28124 1.47691,8.63776 5.75814,9.7409 2.14297,0.54832 4.41786,0.19374 6.28002,-0.99465 l 36.4537,-22.95509 c 1.734,-1.09366 2.9754,-2.8023 3.47969,-4.78938 l 10.56678,-41.76304 c 1.10313,-4.28125 -1.47692,-8.63778 -5.75816,-9.74091 -2.14297,-0.54832 -4.41785,-0.19374 -6.28002,0.99466 l -36.4537,22.95508 c -1.7281,1.09917 -2.96892,2.807 -3.47968,4.78938 z m 75.61238,27.98954 36.45371,22.95509 c 3.73081,2.37212 8.67139,1.26459 11.04351,-2.46622 1.1837,-1.86864 1.54945,-4.14176 0.99466,-6.28002 L 320.7453,263.34792 c -0.5043,-1.98709 -1.7457,-3.69573 -3.47969,-4.78938 l -36.4537,-22.95509 c -3.73082,-2.37212 -8.67141,-1.26459 -11.04352,2.46623 -1.1837,1.86864 -1.54945,4.14175 -0.99466,6.28002 l 10.56677,41.76304 c 0.51136,1.98318 1.75217,3.69102 3.47969,4.78937 z m 47.73847,-59.19266 33.0964,-27.57602 c 3.4089,-2.81519 3.88231,-7.85621 1.06712,-11.26511 -1.4114,-1.70321 -3.46024,-2.75349 -5.66529,-2.88661 l -42.98432,-2.85589 c -2.04567,-0.13443 -4.0543,0.51821 -5.63026,1.82938 l -33.0964,27.57601 c -3.4089,2.8152 -3.88231,7.85622 -1.06711,11.26512 1.4114,1.70321 3.46023,2.75349 5.66529,2.88661 l 42.98432,2.85589 c 2.04414,0.12651 4.05182,-0.52582 5.63025,-1.82938 z M 287.44,169.03 271.441,129.032 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.384,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 L 287.44,174.95 c 0.752,-1.905 0.752,-4.016 0,-5.92 z");
        gc.setFill(Color.web("#2E485D"));
        gc.fillOval(255.985, 359.983, 8, 8);
        gc.fillOval(255.985, 87.996, 8, 8);
        gc.fillOval(305.98499, 157, 8, 8);
        gc.fillOval(205.98502, 157, 8, 8);
        gc.fillOval(255.985, 301.984998, 8, 8);
        gc.fillOval(323.98499, 245.98499, 8, 8);
        gc.fillOval(187.98501, 245.98499, 8, 8);
        gc.fillOval(255.985, 55.997002, 8, 8);
        fillSVGPath(gc, "ffffff", "m 236.72727,282.90211 -36.45369,22.95508 c -3.73082,2.37213 -8.6714,1.2646 -11.04351,-2.46621 -1.18371,-1.86865 -1.54947,-4.14176 -0.99466,-6.28003 l 10.56678,-41.76303 c 0.50428,-1.98709 1.74569,-3.69574 3.47969,-4.78939 l 36.45369,-22.95508 c 3.73082,-2.37211 8.6714,-1.26459 11.0427,2.46563 1.18371,1.86864 1.54947,4.14176 0.99465,6.28002 l -10.56677,41.76304 c -0.50996,1.98298 -1.75135,3.69163 -3.47888,4.78997 z m -47.40159,-59.18418 -33.09639,-27.57601 c -3.40891,-2.81519 -3.88232,-7.85621 -1.06712,-11.26511 1.4114,-1.70322 3.46023,-2.7535 5.66529,-2.88661 l 42.98432,-2.85589 c 2.04566,-0.13444 4.0543,0.51821 5.63026,1.82938 l 33.09639,27.57601 c 3.4089,2.8152 3.88231,7.85621 1.06743,11.26416 -1.4114,1.70322 -3.46024,2.7535 -5.66529,2.88661 l -42.98433,2.85589 c -2.04351,0.12778 -4.05214,-0.52486 -5.63056,-1.82843 z m 85.49451,59.18418 36.45369,22.95508 c 3.73082,2.37213 8.6714,1.2646 11.04351,-2.46621 1.18371,-1.86865 1.54947,-4.14176 0.99466,-6.28003 l -10.56678,-41.76303 c -0.50428,-1.98709 -1.74569,-3.69574 -3.47969,-4.78939 l -36.45369,-22.95508 c -3.73082,-2.37211 -8.6714,-1.26459 -11.0427,2.46563 -1.18371,1.86864 -1.54947,4.14176 -0.99465,6.28002 l 10.56677,41.76304 c 0.50996,1.98298 1.75135,3.69163 3.47888,4.78997 z m 47.4016,-59.18418 33.09639,-27.57601 c 3.4089,-2.81519 3.88231,-7.85621 1.06712,-11.26511 -1.4114,-1.70322 -3.46023,-2.7535 -5.66529,-2.88661 l -42.98432,-2.85589 c -2.04567,-0.13444 -4.0543,0.51821 -5.63026,1.82938 l -33.09639,27.57601 c -3.4089,2.8152 -3.88231,7.85621 -1.06743,11.26416 1.4114,1.70322 3.46024,2.7535 5.66529,2.88661 l 42.98432,2.85589 c 2.04351,0.12778 4.05214,-0.52486 5.63057,-1.82843 z M 279.44,161.03 263.441,121.032 c -1.624,-4.112 -6.272,-6.12 -10.384,-4.496 -2.056,0.816 -3.688,2.44 -4.496,4.496 l -15.999,39.998 c -0.76,1.904 -0.76,4.016 0,5.92 l 15.999,39.998 c 1.624,4.112 6.272,6.12 10.383,4.496 2.056,-0.816 3.688,-2.44 4.496,-4.496 l 15.999,-39.998 c 0.753,-1.904 0.753,-4.016 10e-4,-5.92 z");
        fillSVGPath(gc, "dddfe1", "m 250.77373,236.3497 -10.56677,41.76304 c -0.50429,1.98709 -1.74569,3.69573 -3.47969,4.78938 l -36.4537,22.95508 c -2.75768,1.75407 -6.01908,-4.37312 0.42247,-13.23913 l 47.02046,-64.71813 c 2.6477,1.92243 3.86504,5.26913 3.05723,8.44976 z m -12.83348,-13.65828 -42.98432,2.8559 c -2.04567,0.13443 -4.0543,-0.51821 -5.63026,-1.82939 l -33.09639,-27.57601 c -2.52039,-2.08068 2.29909,-7.07586 12.72171,-3.68934 l 76.08072,24.72012 c -1.01015,3.11217 -3.81688,5.30412 -7.09146,5.51872 z m 22.83348,13.65828 10.56677,41.76304 c 0.50429,1.98709 1.74569,3.69573 3.47969,4.78938 l 36.4537,22.95508 c 2.75768,1.75407 6.01908,-4.37312 -0.42247,-13.23913 l -47.02046,-64.71813 c -2.6477,1.92243 -3.86504,5.26913 -3.05723,8.44976 z m 12.83348,-13.65828 42.98432,2.8559 c 2.04567,0.13443 4.0543,-0.51821 5.63026,-1.82939 l 33.09639,-27.57601 c 2.52039,-2.08068 -2.29909,-7.07586 -12.72171,-3.68934 l -76.08072,24.72012 c 1.01015,3.11217 3.81688,5.30412 7.09146,5.51872 z M 263.441,206.948 279.44,166.95 c 0.76,-1.904 0.76,-4.016 0,-5.92 l -15.999,-39.998 c -1.2,-3.04 -7.44,0 -7.44,10.959 v 79.996 c 3.272,0.001 6.224,-1.991 7.44,-5.039 z");
      }
    }

    fillSVGPath(gc, "2d4961", "M 399.994,0 H 112.008 C 94.337,0 80.009,14.327 80.009,31.999 v 342.624 c 0.08,16.159 8.288,31.191 21.839,39.998 l 145.433,94.796 c 5.304,3.448 12.135,3.448 17.439,0 l 145.433,-94.556 c 13.551,-8.808 21.759,-23.839 21.839,-39.998 V 31.999 C 431.993,14.327 417.665,0 399.994,0 Z m 0,68.477 c -6.872,-6.24 -15.399,-10.352 -24.559,-11.839 -1.496,-9.2 -5.64,-17.759 -11.919,-24.639 h 36.478 z M 148.486,31.999 c -6.264,6.864 -10.408,15.391 -11.919,24.559 -9.168,1.512 -17.695,5.656 -24.559,11.919 V 31.999 Z m 107.515,444.859 -26.479,-17.199 c 11.047,-4.6 20.327,-12.623 26.479,-22.879 6.152,10.256 15.431,18.279 26.479,22.879 z M 399.994,374.622 c 0.008,5.424 -2.728,10.48 -7.28,13.439 l -91.756,59.917 C 280.063,446.386 263.936,428.939 264,407.98 c 0,-4.416 -3.584,-8 -8,-8 -4.416,0 -8,3.584 -8,8 0.064,20.959 -16.063,38.406 -36.958,39.998 l -91.756,-59.917 c -4.552,-2.96 -7.288,-8.016 -7.28,-13.439 V 103.995 c 0,-17.671 14.327,-31.998 31.998,-31.998 4.416,0 8,-3.584 8,-8 0,-17.671 14.327,-31.999 31.999,-31.999 h 143.993 c 17.671,0 31.999,14.327 31.999,31.999 0,4.416 3.584,8 8,8 17.671,0 31.999,14.327 31.999,31.998 z");
    fillSVGPath(gc, "44637f", "m 80.009,31.999 v 271.987 0 c 8.84,0 15.999,-7.16 15.999,-15.999 V 31.999 c 0,-8.84 7.16,-15.999 15.999,-15.999 h 271.987 c 8.84,0 15.999,-7.16 15.999,-15.999 H 112.008 C 94.329,0 80.009,14.327 80.009,31.999 Z");
    fillSVGPath(gc, "123247", "m 410.154,414.861 c 13.551,-8.808 21.759,-23.839 21.839,-39.998 v -318.866 0 c -8.84,0 -15.999,7.16 -15.999,15.999 v 304.466 c 0,8.04 -4.024,15.551 -10.719,19.999 L 269.28,487.097 c -8.304,5.56 -13.287,14.887 -13.279,24.879 v 0 c 3.096,0.008 6.12,-0.88 8.72,-2.56 z");
    gc.restore();
  }

  public int getLevel() {
    return level;
  }
}
