package com.dadurka.util.lt;

/*
 * Copyright (C) 2017 Virginijus Dadurkevičius (dadurka@gmail . com)
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
 * Converter for Lithuanian numerals.
 * Implements static function {@code number2Words} as number-to-words converter
 *  for Lithuanian, adjusts optional countable noun in any declension/case,
 *  supports cardinal, ordinal and definite ordinal forms
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
public class ConvertNumbers {

/**
 * Reduced number-to-words converter for Lithuanian
 *  <p>SYNOPSIS
 *  <pre>{@code
 *      answer=ConvertNumbers.number2Words(
 *              2017
 *      );
 *  }</pre>
 *  Output:
 *  <p>{@code du tūkstančiai septyniolika}
 * @param number        number to be converted (any int/long, zero or negative).
 * @return string of textual representation of a number in Lithuanian language.
 *
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
    public static String number2Words (long number) {
        Options options = new Options();
        return ConvertNumbers.number2Words (number, "", options);
    }
/**
 * Reduced number-to-words converter for Lithuanian with optional countable
 *  noun adjustment.
 *  <p>SYNOPSIS
 *  <pre>{@code
 *      answer=ConvertNumbers.number2Words(
 *              2017,
 *              "metai"
 *      );
 *  }</pre>
 *  Output:
 *  <p>{@code du tūkstančiai septyniolika metų}
 * @param number        number to be converted (any int/long, zero or negative).
 * @param countableNoun adjustable countable noun in Lithuanian.
 *  Should be of canonical form – singular (if possible) and nominative case,  
 *  e.g. "litas", "sekundė", "lankytojas". The gender will be assessed automatically.
 *  Use plural canonical form if singular isn't possible, e.g. "metai". In this case
 *  requested numeral type will be automatically adjusted to cardinal plural
 *  (dauginis skaitvardis).
 * @return string of textual representation of a number in Lithuanian language,
 *  adjusts optional countable noun.
 *
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
    public static String number2Words (long number, String countableNoun) {
        Options options = new Options();
        return ConvertNumbers.number2Words (number, countableNoun, options);
    }
/**
 * Simplified number-to-words converter for Lithuanian with optional countable
 *  noun adjustment in any declension/case and support for cardinal, ordinal and
 *  definite ordinal forms.
 *  <p>SYNOPSIS
 *  <pre>{@code
 *      answer=ConvertNumbers.number2Words(
 *              2017,
 *              "metai",
 *              "įvardžiuotinis įnagininkas"
 *      );
 *  }</pre>
 *  Output:
 *  <p>{@code du tūkstančiai septynioliktaisiais metais}
 * @param number        number to be converted (any int/long, zero or negative).
 * @param countableNoun adjustable countable noun in Lithuanian.
 *  Should be of canonical form – singular (if possible) and nominative case,  
 *  e.g. "litas", "sekundė", "lankytojas". The gender will be assessed automatically.
 *  Use plural canonical form if singular isn't possible, e.g. "metai". In this case
 *  requested numeral type will be automatically adjusted to cardinal plural
 *  (dauginis skaitvardis).
 * @param optionsCmdLine string as space-separated pseudo-CommandLine options
 *  for conversion: numeral type, gender, number (singular or plural), case
 *  and financial obeyance of the returned text.
 *  All the options can be written in any (Enlish or Lithuanian) language,
 *  in full or abreviated forms, with or without diacritics, any order (e.g. use 
 *  "Įn.", "In." or "instr" if instrumental case is needed, use "finansinis",
 *  "financial" or simply "fin" if financial obeyance of the returned text is needed).
 *  Don't set the options which are unclear, seems unnecessary or non-relevant :-)
 * @return string of textual representation of a number in Lithuanian language,
 *  adjusts optional countable noun in any declension/case, 
 *  supports cardinal, ordinal and definite ordinal forms.
 *
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
    public static String number2Words (long number, String countableNoun, String optionsCmdLine) {
        Options options = ParseParameters(optionsCmdLine);
        return ConvertNumbers.number2Words (number, countableNoun, options);
    }
/**
 * Main number-to-words converter for Lithuanian with optional countable noun
 *  adjustment in any declension/case and support for cardinal, ordinal and
 *  definite ordinal forms.
 *  <p>SYNOPSIS
 *  <pre>{@code
 *      answer=ConvertNumbers.number2Words(
 *              2017,
 *              "metai",
 *              new Options().setNumeralType(NumeralType.ORDINAL_DEFINITE).setCasus(Casus.INSTRUMENTAL)
 *      );
 *  }</pre>
 *  Output:
 *  <p>{@code du tūkstančiai septynioliktaisiais metais}
 * @param number        number to be converted (any int/long, zero or negative).
 * @param countableNoun adjustable countable noun in Lithuanian.
 *  Should be of canonical form – singular (if possible) and nominative case,  
 *  e.g. "litas", "sekundė", "lankytojas". The gender will be assessed automatically.
 *  Use plural canonical form if singular isn't possible, e.g. "metai". In this case
 *  requested numeral type will be automatically adjusted to cardinal plural
 *  (dauginis skaitvardis).
 * @param options       class-container of requested options for conversion:
 *  numeral type, gender, number (singular or plural), case
 *  and financial obeyance of the returned text.
 *  Just created Options object is filled with following default values:
 *  cardinal type, masculine, singular, nominative, not financial. Don't set the
 *  options values which are unclear, seems unnecessary or non-relevant :-)
 * @return string of textual representation of a number in Lithuanian language,
 *  adjusts optional countable noun in any declension/case, 
 *  supports cardinal, ordinal and definite ordinal forms.
 *
 * @see Options
 * 
 * @author      Virginijus Dadurkevičius (dadurka @ gmail . com)
 * @version     1.0
 * @since       1.0
 */
public static String number2Words (long number, String countableNoun, Options options) {

        Noun noun = new Noun(countableNoun);

        int nGroups = 1 + ((int) Math.log10((double) Math.max(1, Math.abs(number)))) / 3;
        int [][] parsedNumber = new int[nGroups][3];

        long tmpNumber = Math.abs(number);
        for (int i = 0; i < nGroups; i++) {
            for (int j = 0; j < 3; j++){
                parsedNumber[i][j] = (int) (tmpNumber % 10L);
                tmpNumber = tmpNumber / 10L;
            }
        }

        NumAtomWord [] atomWords = new NumAtomWord [5 * nGroups];

        int numAtomWord = 0;
        long groupImpicitValue = 1;
        for (int i = 0; i < nGroups; i++) {
            if (i > 0) groupImpicitValue = groupImpicitValue * 1000;
            int units = parsedNumber[i][0];
            int tens = parsedNumber[i][1];
            int tensPlus = 10 * tens + units;
            int hundreds = parsedNumber[i][2];
            int hundredsPlus = 100 * hundreds + 10 * tens + units;
            if (hundredsPlus > 0) {
                if (groupImpicitValue != 1) {
                    atomWords[numAtomWord++] = new NumAtomWord(groupImpicitValue);
                }
                if ((tensPlus >=1 && tensPlus <= 19) || (units == 0 && tens != 0)) {
                    atomWords[numAtomWord++] = new NumAtomWord(tensPlus);
                    if (hundredsPlus == 1 && i != 0) {
                        atomWords[numAtomWord - 1].optional = true;
                    }
                }
                else if (tensPlus >= 20) {
                    if (units > 0) {
                        atomWords[numAtomWord++] = new NumAtomWord(units);
                    }
                    if (tens > 0) {
                        atomWords[numAtomWord++] = new NumAtomWord(10 * tens);
                    }
                }
                if (hundreds > 0) {
                    atomWords[numAtomWord++] = new NumAtomWord(100);
                    atomWords[numAtomWord++] = new NumAtomWord(hundreds);
                    if (hundreds == 1) {
                        atomWords[numAtomWord - 1].optional = true;
                    }
                }
            } else if (number == 0L) {
                    atomWords[numAtomWord++] = new NumAtomWord(0);            
            }
        }
        
        noun.setCasus(options.getCasus());

        if (countableNoun.equals("")) {
            atomWords[0].setGenus(options.getGenus());
        } else {
            atomWords[0].setGenus(noun.genus);
        }
        
        if ((options.getNumeralType() == NumeralType.CARDINAL) || 
            (options.getNumeralType() == NumeralType.CARDINAL_PLURAL)) {
            for (int i = 0; i < numAtomWord; i++) {
                    atomWords[i].setCasus(options.getCasus());
            }
            for (int i = 0; i < numAtomWord - 1; i++) {
                if (atomWords[i].canPreSupressCasus && atomWords[i + 1].acceptsCasusSupression) {
                    atomWords[i + 1].setCasus(Casus.DEFAULT());
                }
            }
            if ((atomWords[0].atomValue > 0 && atomWords[0].atomValue <= 9) &&
                (noun.numerus == Numerus.PLURAL)) {
                atomWords[0].setType(NumeralType.CARDINAL_PLURAL);
            }

            if (atomWords[0].demandsPlural) noun.setNumerus(Numerus.PLURAL);
            if (atomWords[0].demandsGenitive) noun.setCasus(Casus.GENITIVE);
        }
        
        if ((options.getNumeralType() == NumeralType.ORDINAL) || 
            (options.getNumeralType() == NumeralType.ORDINAL_DEFINITE)) {
            atomWords[0].setType(options.getNumeralType());
            if (noun.numerus == Numerus.DEFAULT()) {
                noun.setNumerus(options.getNumerus());
                atomWords[0].setNumerus(options.getNumerus());
            } else {
                atomWords[0].setNumerus(noun.numerus);
            }
            
            atomWords[0].setCasus(options.getCasus());
        }
        for (int i = numAtomWord - 1; i > 0; i--) {
            if (atomWords[i].demandsPlural && atomWords[i - 1].acceptsPluralDemand) {
                atomWords[i - 1].setNumerus(Numerus.PLURAL);
            }
            if (atomWords[i].demandsGenitive && atomWords[i - 1].acceptsGenitiveDemand) {
                atomWords[i - 1].setCasus(Casus.GENITIVE);
            }
            if (atomWords[i].canSupressCasus && atomWords[i - 1].acceptsCasusSupression) {
                atomWords[i - 1].setCasus(Casus.DEFAULT());
            }
        }
    // it's time to build the final phrase    
        String tmpPhrase = "";
        if (number < 0L) tmpPhrase = "minus";
        for (int i = numAtomWord - 1; i >= 0; i--)  {
            if (options.getFinancial() || !atomWords[i].optional) {
                if (tmpPhrase.equals("")) {
                    tmpPhrase += atomWords[i].atomText;
                } else {
                    tmpPhrase += " " + atomWords[i].atomText;
                }
            }
        }  

        if (countableNoun.equals("")) {
            return tmpPhrase;
        } else {
            return tmpPhrase + " " + noun.text;
        }

    }

    private static Options ParseParameters(String optionsCmdLine) {
        Options options = new Options();
        String optionsCmdLineLc = optionsCmdLine.toLowerCase();
    // Which numeral type is required for conversion via options?
    //  - Cardinal (DEFAULT), e.g. "vienas", "dvylika", "šimtas du"
    //  - Ordinal, e.g. "pirmas", "dvyliktas", "šimtas antras"
    //  - Definite Ordinal, e.g. "pirmasis", "dvyliktasis", "šimtas antrasis"
        if (optionsCmdLineLc.startsWith("įv") || optionsCmdLineLc.contains(" įv") ||
            optionsCmdLineLc.startsWith("def") || optionsCmdLineLc.contains(" def")) {
                options.setNumeralType(NumeralType.ORDINAL_DEFINITE);
        } else if (
            optionsCmdLineLc.startsWith("kel") || optionsCmdLineLc.contains(" kel") ||
            optionsCmdLineLc.startsWith("ord") || optionsCmdLineLc.contains(" ord")) {
                options.setNumeralType(NumeralType.ORDINAL);
        }
    // Which number (latin "numerus") is required?
    //  - Singular (DEFAULT)
    //  - Plural
        if (optionsCmdLineLc.startsWith("dgs") || optionsCmdLineLc.contains(" dgs") ||
            optionsCmdLineLc.startsWith("daug") || optionsCmdLineLc.contains(" daug") ||
            optionsCmdLineLc.startsWith("pl") || optionsCmdLineLc.contains(" pl")) {
                options.setNumerus(Numerus.PLURAL);
        }
    // Which gender (latin "genus") is required?
    //  - Masculine (DEFAULT)
    //  - Feminine
        if (optionsCmdLineLc.startsWith("mot") || optionsCmdLineLc.contains(" mot") ||
            optionsCmdLineLc.startsWith("m.") || optionsCmdLineLc.contains(" m.") ||
            optionsCmdLineLc.startsWith("fem") || optionsCmdLineLc.contains(" fem")) {
                options.setGenus(Genus.FEMININE);
        }
    // Which case (latin "casus") is required?
    //  - Nominative/Vardininkas (DEFAULT)
    //  - Genitive/Kilmininkas
    //  - Dative/Naudininkas
    //  - Accusative/Galininkas
    //  - Instrumental/Įnagininkas
    //  - Locative/Vietininkas
        if (optionsCmdLine.startsWith("V.") || optionsCmdLine.contains(" V.") ||
            optionsCmdLineLc.startsWith("var") || optionsCmdLineLc.contains(" var") ||
            optionsCmdLineLc.startsWith("nom") || optionsCmdLineLc.contains(" nom")) {
                options.setCasus(Casus.NOMINATIVE);
        } else if (
            optionsCmdLine.startsWith("K.") || optionsCmdLine.contains(" K.") ||
            optionsCmdLineLc.startsWith("kil") || optionsCmdLineLc.contains(" kil") ||
            optionsCmdLineLc.startsWith("gen") || optionsCmdLineLc.contains(" gen")) {
                options.setCasus(Casus.GENITIVE);
        } else if (
            optionsCmdLine.startsWith("N.") || optionsCmdLine.contains(" N.") ||
            optionsCmdLineLc.startsWith("nau") || optionsCmdLineLc.contains(" nau") ||
            optionsCmdLineLc.startsWith("dat") || optionsCmdLineLc.contains(" dat")) {
                options.setCasus(Casus.DATIVE);
        } else if (
            optionsCmdLine.startsWith("G.") || optionsCmdLine.contains(" G.") ||
            optionsCmdLineLc.startsWith("gal") || optionsCmdLineLc.contains(" gal") ||
            optionsCmdLineLc.startsWith("acc") || optionsCmdLineLc.contains(" acc")) {
                options.setCasus(Casus.ACCUSATIVE);
        } else if (
            optionsCmdLine.startsWith("Įn.") || optionsCmdLine.contains(" Įn.") ||
            optionsCmdLineLc.startsWith("įna") || optionsCmdLineLc.contains(" įna") ||
            optionsCmdLineLc.startsWith("ina") || optionsCmdLineLc.contains(" ina") ||
            optionsCmdLineLc.startsWith("ins") || optionsCmdLineLc.contains(" ins")) {
                options.setCasus(Casus.INSTRUMENTAL);
        } else if (
            optionsCmdLine.startsWith("Vt.") || optionsCmdLine.contains(" Vt.") ||
            optionsCmdLineLc.startsWith("vie") || optionsCmdLineLc.contains(" vie") ||
            optionsCmdLineLc.startsWith("loc") || optionsCmdLineLc.contains(" loc")) {
                options.setCasus(Casus.LOCATIVE);
        }
        if (optionsCmdLineLc.startsWith("fin") || optionsCmdLineLc.contains(" fin") ||
            optionsCmdLineLc.startsWith("prec") || optionsCmdLineLc.contains(" prec") ||
            optionsCmdLineLc.startsWith("tik") || optionsCmdLineLc.contains(" tik")) {
                options.setFinancial();
        }        
        return options;
    }

/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String answer;
        answer=ConvertNumbers.number2Words(123456);
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1234567, "medis");
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1234560, "doleris", "Įn. fin");
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1958, "metai", "Įn. įvardž.");
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1134562, "litas", "G.");
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1134561, "litas", "G. finansinis");
        System.out.println(answer);
        assert ConvertNumbers.number2Words(1958, "metai", "Įn. įvardž.").equals(
            "tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais") :
            "Something went wrong...";
        assert ConvertNumbers.number2Words(1134561, "litas", "G.").equals(
            "milijoną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą") :
            "Something went wrong...";
        assert ConvertNumbers.number2Words(1134561, "litas", "G. fin").equals(
            "vieną milijoną vieną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą") :
            "Something went wrong...";
        answer=ConvertNumbers.number2Words(1958, "metai", new Options()
                .setNumeralType(NumeralType.ORDINAL_DEFINITE)
                .setCasus(Casus.INSTRUMENTAL)
        );
        System.out.println(answer);
        answer=ConvertNumbers.number2Words(1958, "litas", new Options()
                .setCasus(Casus.GENITIVE)
                .setFinancial()
        );
        System.out.println(answer);
    
    }

}
