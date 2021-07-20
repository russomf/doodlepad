/*
 * Elastic.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2021 Mark F. Russo
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
public class Elastic {

    /**
     * A function to compute a oscillating back and forth as it eases-in
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeIn(double t, double b, double c, double d ) {
        if (t==0) return b; if ((t/=d)==1) return b+c;
        double p=d*.3;
        double a=c;
        double s=p/4;
        return -(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
    }

    /**
     * A function to compute a oscillating back and forth as it eases-out
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeOut(double t, double b, double c, double d) {
        if (t==0) return b; if ((t/=d)==1) return b+c;
        double p=d*.3;
        double a=c;
        double s=p/4;
        return (a*Math.pow(2,-10*t) * Math.sin( (t*d-s)*(2*Math.PI)/p ) + c + b);        
    }

    /**
     * A function to compute a oscillating back and forth as it eases-in
     * and as it eases-out.
     * @param t     Current time
     * @param b     Minimum value
     * @param c     Change in value
     * @param d     Maximum time
     * @return      Easing value
     */
    public static double easeInOut(double t, double b, double c, double d) {
        if (t==0) return b; if ((t/=d/2)==2) return b+c;
        double p=d*(.3*1.5);
        double a=c;
        double s=p/4;
        if (t < 1) return -.5*(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
        return a*Math.pow(2,-10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )*.5 + c + b;
    }
}
