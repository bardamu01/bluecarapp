package com.bluecar;

/**
 * Created by IntelliJ IDEA.
 * Date: 6/10/12
 * Time: 10:32 PM
 */

public class BlueCar {

    public final static int MAX_SPEED = 15;

    public final static byte CMD_STOP = 0x00;
    public final static byte CMD_STRAIGHT_FORWARD = 0x10;
    public final static byte CMD_STRAIGHT_BACKWARD = 0x20;
    public final static byte CMD_LEFT_NO_DRIVE = 0x30;
    public final static byte CMD_RIGHT_NO_DRIVE = 0x40;
    public final static byte CMD_LEFT_FORWARD = 0x50;
    public final static byte CMD_RIGHT_FORWARD = 0x60;
    public final static byte CMD_LEFT_BACKWARD = 0x70;
    public final static int CMD_RIGHT_BACKWARD = 0x80;

}
