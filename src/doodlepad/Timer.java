/*
 * Timer.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2024 Mark F. Russo
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

import java.awt.event.*;
import java.util.ArrayList;

/**
 * A class that implements a stand-alone, non-graphical Timer object.
 * Provides for multiple Timers in a single program.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public class Timer {

    /**
     * Reference to a javax Swing Timer object
     */
    private javax.swing.Timer timer = null;

    /**
     * The tick rate as ticks per second
     */
    private double tickRate = 60.0;

    /**
     * Private fields that hold event handlers assigned using method references.
     * See for example setTickHandler()
     */
    private TimerEventHandler startHandler = null;
    private TimerEventHandler tickHandler  = null;
    private TimerEventHandler stopHandler  = null;

    /**
     * Lists of custom listeners registered to receive this Timer object's events
     */
    private java.util.List<TimerEventListener> startListeners = new ArrayList<>();
    private java.util.List<TimerEventListener> tickListeners  = new ArrayList<>();
    private java.util.List<TimerEventListener> stopListeners  = new ArrayList<>();

    /**
     * Nullary constructor setting default tick rate
     */
    public Timer() {
        setTickRate(tickRate);      // Start with a default timer setup
    }

    /**
     * Timer constructor with initial tick rate parameter
     * @param tps Ticks per second
     */
    public Timer(double tps) {
        setTickRate(tps);      // Start with a default timer setup
    }

    /**
     * Set the rate at which to fire onTick events
     * @param tps Ticks per second
     */
    public final void setTickRate(double tps) {
        // Check for validity of tick rate
        if (tps <= 0.0) {
            throw new IllegalArgumentException("tick rate cannot be less than or equal to 0");
        }
        
        // Set up new timer
        tickRate = tps;
        int delay = (int)(1000.0/tps);
        if (timer != null) timer.stop();
        timer = new javax.swing.Timer(delay, actionListener);
        timer.stop();
    }

    /**
     * Return the current rate at which the timer ticks
     * @return A double indicating ticks per second.
     */
    final public double getTickRate() {
        return tickRate;
    }

    /**
     * Start the timer firing onTick events.
     */
    public void startTimer() {
        // If invalid tickRate, fix.
        if (tickRate < 1.0) tickRate = 1.0;
        
        // If no timer, create one.
        if (timer == null) {
            setTickRate(tickRate);
        }
        
        // Make sure running
        timer.start();
    }
    
    /**
     * Stop timer
     */
    public void stopTimer() {
        if (timer != null) timer.stop();
    }
    
    /**
     * Toggle the state of the timer. Started/Stopped.
     */
    public void toggleTimer() {
        if (this.isRunning()) {
            this.stopTimer();
        } else {
            this.startTimer();
        }
    }

    /**
     * Determine if the timer is running
     * @return True or false indicating if the timer is running.
     */
    public boolean isRunning() {
        if (timer == null) {
            return false;
        } else {
            return timer.isRunning();
        }
    }

    /**
     * Interface used by methods that assign timer event handlers
     * given a method reference as a parameter.
     */
    public interface TimerEventHandler {
        public void f(Timer tmr, long when);
    }

    /**
     * Assign an onStart event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setStartHandler( TimerEventHandler handler ) {
        this.startHandler = handler;
    }

    /**
     * Assign an onTick event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setTickHandler( TimerEventHandler handler ) {
        this.tickHandler = handler;
    }

    /**
     * Assign an onStop event handler using a method reference.
     * @param handler Method reference to an event handler
     */
    public void setStopHandler( TimerEventHandler handler ) {
        this.stopHandler = handler;
    }

    /**
     * A method that can be overridden to handle the timer`s tick event.
     * The timer tick rate is set with the setTickRate() method. 
     * The timer is started by invoking the startTimer() method.
     * The timer is stopped by invoking the stopTimer() method.
     * @param when The difference in milliseconds between the timestamp of when this event occurred and midnight, January 1, 1970 UTC.
     */
    public void onTick(long when) {
        // Also, override to implement.
        if (tickHandler != null) {
            tickHandler.f(this, when);
        }
    }

    /**
     * A method that can be overridden to handle the timer`s start event.
     * The timer is started by invoking the startTimer() method.
     * @param when The difference in milliseconds between the timestamp of when this event occurred 
     * and midnight, January 1, 1970 UTC.
     */
    public void onStart(long when) {
        // Also, override to implement.
        if (startHandler != null) {
            startHandler.f(this, when);
        }
    }

    /**
     * A method that can be overridden to handle the timer`s stop event.
     * The timer is stopped by invoking the stopTimer() method.
     * @param when The difference in milliseconds between the timestamp of when this event occurred 
     * and midnight, January 1, 1970 UTC.
     */
    public void onStop(long when) {
        // Also, override to implement.
        if (stopHandler != null) {
            stopHandler.f(this, when);
        }
    }

    /**
     * Add object to the list of items that are notified on Timer start action.
     * @param o An object that implements the TimerTickListener interface.
     */
    public void addStartListener(TimerEventListener o) {
        startListeners.add(o);
    }
    
    /**
     * Remove object from Timer's start action listener list.
     * @param o The TimerEventListener object to be removed.
     */
    public void removeStartListener(TimerEventListener o) {
        startListeners.remove(o);
    }

    /**
     * Add object to the list of items that are notified on Timer's tick action.
     * @param o An object that implements the ActionListener interface.
     */
    public void addTickListener(TimerEventListener o) {
        tickListeners.add(o);
    }
    
    /**
     * Remove object from Timer's tick action listener list.
     * @param o The ActionListener object to be removed.
     */
    public void removeTickListener(TimerEventListener o) {
        tickListeners.remove(o);
    }

    /**
     * Add object to the list of items that are notified on Timer stop action.
     * @param o An object that implements the TimerEventListener interface.
     */
    public void addStopListener(TimerEventListener o) {
        stopListeners.add(o);
    }
    
    /**
     * Remove object from Timer's start action listener list.
     * @param o The TimerTickListener object to be removed.
     */
    public void removeStopListener(TimerEventListener o) {
        stopListeners.remove(o);
    }

    /**
     * Stop internal Timer when garbage collected.
     * @throws java.lang.Throwable May throw exceptions 
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            stopTimer();                // Stop timer, in case it is running
        } finally {
            // super.finalize();           // Deprecated. Always call base class implementation
        }
    }

    /**
     * Inner class to handle action listener events
     */
    private final ActionListener actionListener = new ActionListener() {
        /**
         * Inform all Shapes that receive events about the tick event.
         * @param e The ActionEvent object describing the action performed.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            long when = e.getWhen();
            
            // Delegate to any subclass overriding methods
            onTick(when);
            
            // Delegate to any registered TimerTickListeners
            for (TimerEventListener s : tickListeners) {
                s.onTick(when);
            }
        }
    };
}
