/*
 * PadKeyListener.java
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
 * The interface to be implemented when handling PadKeyListener events.
 * 
 * @author Mark F. Russo, Ph.D.
 * @version 1.0
 */
public interface PadKeyListener
{
    /**
     * The onKeyTyped method is invoked by PadKeyListeners
     * @param keyChar The char typed.
     */
    void onKeyTyped(char keyChar);
    
    /**
     * The onKeyPressed method is invoked by PadKeyListeners
     * @param keyText A String describing the keyCode, such as "HOME", "F1" or "A".
     * @param keyModifiers A String describing the modifier key(s), such as "Shift", or "Ctrl+Shift".
     */
    void onKeyPressed(String keyText, String keyModifiers);
    
    /**
     * The onKeyReleased method is invoked by PadKeyListeners
     * @param keyText A String describing the keyCode, such as "HOME", "F1" or "A".
     * @param keyModifiers A String describing the modifier key(s), such as "Shift", or "Ctrl+Shift".
     */
    void onKeyReleased(String keyText, String keyModifiers);
}
