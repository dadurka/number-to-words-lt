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
 * Options for conversion.
 *  The desired conversion rusult may be of specific:<pre>
 *    TYPE (Lithuanian "skaitvardžio skyrius")
 *    GENDER (Latin "genus, Lithuanian "giminė")
 *    NUMBER (singular or plural, Latin "numerus", Lithuanian "skaičius")
 *    CASE (Latin "Casus, Lithuanian "linksnis")
 *    FINANCIAL RELEVANCE (casual Lithuanian omits "vienas"(one) with hundreds,
 *              thousands etc., but this may not be acceptable in the
 *              financial context)</pre>
 *  Just created Options object is filled with following default values:
 *  cardinal type, masculine, singular, nominative, not financial.
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
public class Options {
    private NumeralType desiredNumeralType = NumeralType.CARDINAL;
    private Genus desiredGenus = Genus.MASCULINE;
    private Numerus desiredNumerus = Numerus.SINGULAR;
    private Casus desiredCasus = Casus.NOMINATIVE;
    private boolean financial = false;

/**
 * @param desiredNumeralType enumerated type of numeral.
 * @return this instance of Options.
 * @see NumeralType
 */
    public Options setNumeralType(NumeralType desiredNumeralType){
        this.desiredNumeralType = desiredNumeralType;
        return this;
    }

/**
 * @param desiredGenus enumerated gender.
 * @return this instance of Options.
 * @see Genus
 */
    public Options setGenus(Genus desiredGenus){
        this.desiredGenus = desiredGenus;
        return this;
    }

/**
 * @param desiredNumerus enumerated number (singular or plural)
 * @return this instance of Options.
 * @see Numerus
 */
    public Options setNumerus(Numerus desiredNumerus){
        this.desiredNumerus = desiredNumerus;
        return this;
    }

/**
 * @param desiredCasus enumerated case (nominative, genitive, ...)
 * @return this instance of Options.
 * @see Casus
 */
    public Options setCasus(Casus desiredCasus){
        this.desiredCasus = desiredCasus;
        return this;
    }

    public Options setFinancial(){
        this.financial = true;
        return this;
    }

    public NumeralType getNumeralType(){
        return desiredNumeralType;
    }

    public Genus getGenus(){
        return desiredGenus;
    }

    public Numerus getNumerus(){
        return desiredNumerus;
    }

    public Casus getCasus(){
        return desiredCasus;
    }

    public boolean getFinancial(){
        return financial;
    }
}
