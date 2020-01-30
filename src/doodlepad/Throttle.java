/*
 * Throttle.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2020 Mark F. Russo
 * 
 * This file is part of DoodlePad
 * 
 * DoodlePad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DoodlePad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DoodlePad.  If not, see <http://www.gnu.org/licenses/>.
 */

package doodlepad;

/**
 * A class that attempts to smooth out looping animation by measuring and
 * executing the appropriate delay between frames.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Throttle
{
    private long start;
    private long frameDuration;
    
    /**
     * Default constructor. Set frame rate to 60 frames per second.
     */
    public Throttle() {
        this(60L);
    }
    
    /**
     * Constructor that allows frame rate to be set.
     * @param fps Frames per second
     */
    public Throttle(long fps)
    {
        frameDuration = 1000000000L / fps;
        start = System.nanoTime();
    }

    /**
     * Reset the start time, which is used to measure frame duration.
     */
    public void start() {
        start = System.nanoTime();
    }
    
    /**
     * Compute the amount of time remaining in the current frame duration
     * and sleep until the next frame begins.
     */
    public void delay() {
        long stop  = System.nanoTime();
        long diff  = stop - start;
        long delay = frameDuration - diff;
        
        if (delay > 0) {
            try {   // (delay / 1000000L) -> nanoseconds to milliseconds
                Thread.sleep(delay / 1000000L, (int) (delay % 1000000L));
            } catch (InterruptedException ex) { }
        }
        start += frameDuration;
    }
}

