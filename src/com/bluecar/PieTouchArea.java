package com.bluecar;

/**
 * Created by IntelliJ IDEA.
 * Date: 6/16/12
 * Time: 11:34 AM
 *
 * Maps a touch event to the pie slice that was touched.
 *
 * Coordinates:
 * ------> X
 * |
 * |
 * v
 * Y
 *
 */

public class PieTouchArea {

    private int radius;
    private int slices;
    private Point position;  //
    private double rotation; //

    /**
     * @param radius the radius of the pie
     * @param slices the number of pie slices
     * @param position the position of the center of the pie relative to existing coordinates
     * @param rotation degrees by which the areas can be rotated; can be negative; should not exceed 360/slices
     *
     */
    public PieTouchArea(int radius, int slices, Point position, double rotation){
        this.radius = radius;
        this.slices = slices;
        if (position == null)
            this.position = new Point(0,0);
        else
            this.position = position;
        this.rotation = rotation;

    }

    public int getArea(Point touchedPoint){
        double distance = this.position.distance(touchedPoint);

        // anything outside of the radius is not of interest
        if ( distance < 0 || distance > radius)
            return -1;

        double adjustedY = position.y - touchedPoint.y;
        double adjustedX = touchedPoint.x - position.x;

        // -90 degrees to +90 degrees range
        double degrees = Math.toDegrees(Math.atan(adjustedX / adjustedY));

        //map to 0:360 degrees
        if (adjustedY < 0)
            degrees += 180;
        else
            if (adjustedX < 0)
                degrees += 360;

        // compensate for rotation
        degrees = degrees - rotation;
        if (degrees < 0)
            degrees = 360 + degrees;
        if (degrees > 360)
            degrees = 360 - degrees;

        return (int) ( degrees / (360/ slices) );
    }

    public Point getPosition() {
        return position;
    }
}
