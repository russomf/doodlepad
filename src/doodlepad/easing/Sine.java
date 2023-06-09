/*
 * Sine.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2024 Mark F. Russo
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

 * You should have received a copy of the GNU General Public License
 * along with DoodlePad.  If not, see <http://www.gnu.org/licenses/>.
 */

package doodlepad.easing;

/**
 * Robert Penner's easing functions in Java
 * (See https://github.com/jesusgollonet/processing-penner-easing)
 * 
 * @version 1.0
 */
public class Sine {
    /**
     * A function to compute an easing-in that follows a sine curve
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeIn(double t,double b , double c, double d) {
        return -c * Math.cos(t/d * (Math.PI/2)) + c + b;
    }

    /**
     * A function to compute an easing-out that follows a sine curve
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeOut(double t,double b , double c, double d) {
        return c * Math.sin(t/d * (Math.PI/2)) + b;        
    }

    /**
     * A function to compute an easing-in and easing-out that follows a sine curve
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeInOut(double t,double b , double c, double d) {
        return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
    }
}
