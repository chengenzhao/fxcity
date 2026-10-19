package com.whitewoodcity.javafx.jvg;

import javafx.geometry.Bounds;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Shape;

/**
 * Utility for calculating LinearGradient projection parameter t,
 * used to judge whether a shape renders as a solid stop color,
 * or a valid gradient across its interior.
 */
public class LinearGradientUtil {

  /** Small epsilon to avoid floating point precision false positives */
  private static final double EPS = 1e-9;

  /**
   * Calculate t value for a single sample point
   * @param lg           LinearGradient instance, only supports proportional = true
   * @param localBounds  Shape's local bounds (use shape.getBoundsInLocal(), NOT getBoundsInParent())
   * @param pxLocal      Point X coordinate in the shape's local coordinate system
   * @param pyLocal      Point Y coordinate in the shape's local coordinate system
   * @return projection t value along the gradient axis
   */
  public static double calcT(LinearGradient lg, Bounds localBounds, double pxLocal, double pyLocal) {
    // When proportional = true: start/end are already normalized relative to the shape's bounding box
    double sRx = lg.getStartX();
    double sRy = lg.getStartY();
    double eRx = lg.getEndX();
    double eRy = lg.getEndY();

    // Convert shape local pixel coordinates to normalized relative coordinates (0~1 = bounding box interior)
    double pRx = (pxLocal - localBounds.getMinX()) / localBounds.getWidth();
    double pRy = (pyLocal - localBounds.getMinY()) / localBounds.getHeight();

    // Gradient axis direction vector
    double dx = eRx - sRx;
    double dy = eRy - sRy;
    // Vector from gradient start to sample point
    double spx = pRx - sRx;
    double spy = pRy - sRy;

    // Dot product projection formula
    double dot = spx * dx + spy * dy;
    double lenSq = dx * dx + dy * dy;

    // Guard against division by zero if gradient start and end points coincide
    if (lenSq < EPS) return 0;
    return dot / lenSq;
  }

  /**
   * Region classification for a shape under a NO_CYCLE linear gradient
   */
  public enum TRegion {
    ALL_LESS_0,
    ALL_GREATER_1,
    HAS_GRADIENT
  }
  /**
   * Sample the 4 corners of the shape's bounding box to classify gradient behavior.
   * Since t is a linear function, its extrema over the bounding box always land on the 4 corners,
   * so no full pixel traversal is needed for judgment.
   */
  public static TRegion checkShapeGradientRegion(Shape shape, LinearGradient lg) {
    if (!lg.isProportional()) {
      throw new IllegalArgumentException("This check only supports proportional = true mode");
    }

    Bounds b = shape.getBoundsInLocal();
    double minX = b.getMinX();
    double maxX = b.getMaxX();
    double minY = b.getMinY();
    double maxY = b.getMaxY();

    // Four corner points in shape local coordinates
    double[][] corners = {
        {minX, minY},
        {maxX, minY},
        {minX, maxY},
        {maxX, maxY}
    };

    boolean hasLess0 = false;
    boolean hasGreater1 = false;
    boolean hasInRange = false;

    for (double[] pt : corners) {
      double t = calcT(lg, b, pt[0], pt[1]);
      if (t < -EPS) {
        hasLess0 = true;
      } else if (t > 1 + EPS) {
        hasGreater1 = true;
      } else {
        hasInRange = true;
      }
    }

    // If any point falls inside [0,1], the shape will show a gradient
    if (hasInRange) return TRegion.HAS_GRADIENT;
    // All points on the start side of the axis
    if (hasLess0 && !hasGreater1) return TRegion.ALL_LESS_0;
    // All points on the end side of the axis
    if (hasGreater1 && !hasLess0) return TRegion.ALL_GREATER_1;

    // Mixed t<0 and t>1 across corners means the axis crosses the bounding box -> valid gradient
    return TRegion.HAS_GRADIENT;
  }
}
