package com.dadurka.util.lt;

/*
 * Copyright (C) 2017 Virginijus Dadurkevičius (dadurka @ gmail . com)
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
 * Lithuanian cases (nominative, genitive, ...).
 *  
 * <p>Each enumerated value has an associated integer. This int value addresses
 *  the relevant case (e.g. dative) in the multidimensional array of preset
 *  Lithuanian wordforms. It should be used as the last index of this array.
 * 
 * <p>E.g. to select DATIVE wordform of particular number we can write something like that:
 * <p>{@code numeralText = forms[...][...][...][Casus.DATIVE.getIndex()];}
 * 
 * <p>The list is comprehensive enough for the real life. Not for academic research :-)
 * 
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
*/
public enum Casus {

/**
 * Nominative (vardininkas) like "vienas", "du", "trys"
 */
    NOMINATIVE          (0),
    
/**
 * Genitive (kilmininkas) like "vieno", "dviejų", "trijų"
 */
    GENITIVE            (1),
    
/**
 * Dative (naudininkas) like "vienam", "dviem", "trims"
 */
    DATIVE              (2),
    
/**
 * Accusative (galininkas) like "vieną", "du", "tris"
 */
    ACCUSATIVE          (3),
    
/**
 * Instrumental (įnagininkas) like "vienu", "dviem", "trimis"
 */
    INSTRUMENTAL        (4),
    
/**
 * Locative (vietininkas) like "viename", "dviejuose", "trijuose"
 */
    LOCATIVE            (5);
    
    private final int index;

    private Casus(int index) {
        this.index = index;
    }

/**
 * @return enum-value-associated int, which addresses the relevant case (say, dative)
 *  in the multidimensional array of preset Lithuanian wordforms (it should be
 *  used as the last index of this array).
 * 
 *  <p>E.g. to select DATIVE wordform of particular number we can write something like that:
 *  <p>{@code numeralText = forms[...][...][...][Casus.DATIVE.getIndex()];}
 * 
 */
    public int getIndex() {
        return this.index;
    }
/**
 * @return default case NOMINATINE
 */
    public static Casus DEFAULT() {
        return NOMINATIVE;
    }
}
