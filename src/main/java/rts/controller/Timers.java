package rts.controller;

import java.util.Timer;

public class Timers
{

    private static Timer animTimer;

    public static Timer animTimer()
    {
        return animTimer;
    }

    public static void init()
    {
        animTimer = new Timer();

    }

}
