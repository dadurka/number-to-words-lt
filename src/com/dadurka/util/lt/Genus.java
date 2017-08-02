package com.dadurka.util.lt;

/*
 * Copyright (C) 2017 Virginijus Dadurkevičius (dadurka @ gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

/**
 * Lithuanian genders.
 *  
 * <p>Each enumerated value has an associated integer. This int value addresses
 *  the relevant gender in the multidimensional array of preset Lithuanian
 *  wordforms. It should be used as the 3rd to last index of this array.
 * 
 * <p>E.g. to select FEMININE wordform of particular number we can write something like that:
 * <p>{@code numeralText = NUMBER_1_TO_9[...][...][Genus.FEMININE.getIndex()][...][...];}
 * 
 * <p>The list is comprehensive enough for the real life. Not for academic research :-)
 * 
 * @author      Virginijus Dadurkevičius (dadurka @ gmail.com)
 * @version     1.0
 * @since       1.0
*/
public enum Genus {

/**
 * Masculine (vyriškoji giminė) like "vienas", "antras", "treji"
 */
    MASCULINE            (0),
    
/**
 * Feminine (moteriškoji giminė) like "viena", "antra", "trejos"
 */
    FEMININE             (1);

    private final int index;

    private Genus(int index) {
        this.index = index;
    }

/**
 * @return enum-value-associated int, which addresses the relevant gender
 *  in the multidimensional array of preset Lithuanian wordforms (it
 *  should be used as the 3rd to last index of this array).
 * 
 * <p>E.g. to select FEMININE wordform of particular number we can write something like that:
 * <p>{@code numeralText = NUMBER_1_TO_9[...][...][Genus.FEMININE.getIndex()][...][...];}
 * 
 */
    public int getIndex() {
        return this.index;
    }
/**
 * @return default gender MASCULINE
 */
    public static Genus DEFAULT() {
        return MASCULINE;
    }
}
