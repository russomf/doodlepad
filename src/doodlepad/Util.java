/*
 * Util.java
 * 
 * Author: Mark F. Russo, Ph.D.
 * Copyright (c) 2012-2016 Mark F. Russo
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
 * A class with useful static utility methods.
 * 
 * @author Mark F. Russo, PhD
 * @version 1.0
 */
public class Util {
    
    /**
     * Constrain a value to the given range.
     * @param val The value to constrain.
     * @param min The minimum possible value.
     * @param max The maximum possible value.
     * @return The constrained value.
     */
    public static int constrain(int val, int min, int max) {
        if (val < min) val = min;
        if (val > max) val = max;
        return val;
    }
    
    /**
     * Constrain a value to the given range.
     * @param val The value to constrain.
     * @param min The minimum possible value.
     * @param max The maximum possible value.
     * @return The constrained value.
     */
    public static double constrain(double val, double min, double max) {
        return Math.max(Math.min(val, max), min);
    }
}
