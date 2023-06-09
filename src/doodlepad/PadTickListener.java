/*
 * PadTickListener.java
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

/**
 * The interface to be implemented when handling PadTickListener events.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public interface PadTickListener
{
    /**
     * The onTick method is invoked by PadTickListeners
     * @param when The difference in milliseconds between the timestamp of when this event occurred and midnight, January 1, 1970 UTC.
     */
    void onTick(long when);
}
