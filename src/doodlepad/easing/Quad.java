/*
 * Quad.java
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
public class Quad
{
    /**
     * A function to compute a quadratic easing-in
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeIn(double t, double b, double c, double d) {
        return c*(t/=d)*t + b;
    }

    /**
     * A function to compute a quadratic easing-out
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeOut(double t, double b, double c, double d) {
        return -c *(t/=d)*(t-2) + b;
    }

    /**
     * A function to compute a quadratic easing-in and easing-out
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeInOut(double t, double b, double c, double d) {
        if ((t/=d/2) < 1) return c/2*t*t + b;
        return -c/2 * ((--t)*(t-2) - 1) + b;
    }
}
