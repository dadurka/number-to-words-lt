/*
 * Copyright (C) 2017 Virginijus Dadurkevičius (dadurka@gmail.com)
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
package com.dadurka.util.lt.convert;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Virginijus Dadurkevičius (dadurka@gmail.com)
 */
public class Numbers {

    private static int desiredNumeralType;
    private static int desiredGenus;
    private static int desiredNumerus;
    private static int desiredCasus;
    private static boolean financial;

    public static String number2Words (long number, String countableNoun) {
        return Numbers.number2Words (number, countableNoun, "");
    }
    public static String number2Words (long number) {
        return Numbers.number2Words (number, "", "");
    }
    public static String number2Words (long number, String countableNoun, String options) {

        Noun noun = new Noun(countableNoun);
        ParseParameters(options);

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
        
        noun.setCasus(desiredCasus);

        if (countableNoun.equals("")) {
            atomWords[0].setGenus(desiredGenus);
        } else {
            atomWords[0].setGenus(noun.genus);
        }
        
        if ((desiredNumeralType == NumeralType.CARDINAL) || 
            (desiredNumeralType == NumeralType.CARDINAL_PLURAL)) {
            for (int i = 0; i < numAtomWord; i++) {
                    atomWords[i].setCasus(desiredCasus);
            }
            for (int i = 0; i < numAtomWord - 1; i++) {
                if (atomWords[i].canPreSupressCasus && atomWords[i + 1].acceptsCasusSupression) {
                    atomWords[i + 1].setCasus(Casus.DEFAULT);
                }
            }
            if ((atomWords[0].atomValue > 0 && atomWords[0].atomValue <= 9) &&
                (noun.numerus == Numerus.PLURAL)) {
                atomWords[0].setType(NumeralType.CARDINAL_PLURAL);
            }

            if (atomWords[0].demandsPlural) noun.setNumerus(Numerus.PLURAL);
            if (atomWords[0].demandsGenitive) noun.setCasus(Casus.GENITIVE);
        }
        
        if ((desiredNumeralType == NumeralType.ORDINAL) || 
            (desiredNumeralType == NumeralType.ORDINAL_DEFINITE)) {
            atomWords[0].setType(desiredNumeralType);
            if (noun.numerus == Numerus.DEFAULT) {
                noun.setNumerus(desiredNumerus);
            } else {
                desiredNumerus = noun.numerus;                
            }
            atomWords[0].setNumerus(desiredNumerus);
            atomWords[0].setCasus(desiredCasus);
        }
        for (int i = numAtomWord - 1; i > 0; i--) {
            if (atomWords[i].demandsPlural && atomWords[i - 1].acceptsPluralDemand) {
                atomWords[i - 1].setNumerus(Numerus.PLURAL);
            }
            if (atomWords[i].demandsGenitive && atomWords[i - 1].acceptsGenitiveDemand) {
                atomWords[i - 1].setCasus(Casus.GENITIVE);
            }
            if (atomWords[i].canSupressCasus && atomWords[i - 1].acceptsCasusSupression) {
                atomWords[i - 1].setCasus(Casus.DEFAULT);
            }
        }
    // it's time to build the final phrase    
        String tmpPhrase = "";
        if (number < 0L) tmpPhrase = "minus";
        for (int i = numAtomWord - 1; i >= 0; i--)  {
            if (financial || !atomWords[i].optional) {
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

    private static void ParseParameters(String options) {
        String optionsLc = options.toLowerCase();
    // Which numeral type is required for conversion via options?
    //  - Cardinal (DEFAULT), e.g. "vienas", "dvylika", "šimtas du"
    //  - Ordinal, e.g. "pirmas", "dvyliktas", "šimtas antras"
    //  - Definite Ordinal, e.g. "pirmasis", "dvyliktasis", "šimtas antrasis"
        desiredNumeralType = NumeralType.CARDINAL;
        if (optionsLc.startsWith("įv") || optionsLc.contains(" įv") ||
            optionsLc.startsWith("def") || optionsLc.contains(" def")) {
                desiredNumeralType = NumeralType.ORDINAL_DEFINITE;
        } else if (
            optionsLc.startsWith("kel") || optionsLc.contains(" kel") ||
            optionsLc.startsWith("ord") || optionsLc.contains(" ord")) {
                desiredNumeralType = NumeralType.ORDINAL;
        }
    // Which number (latin "numerus") is required?
    //  - Singular (DEFAULT)
    //  - Plural
        desiredNumerus = Numerus.SINGULAR;
        if (optionsLc.startsWith("dgs") || optionsLc.contains(" dgs") ||
            optionsLc.startsWith("daug") || optionsLc.contains(" daug") ||
            optionsLc.startsWith("pl") || optionsLc.contains(" pl")) {
                desiredNumerus = Numerus.PLURAL;
        }
    // Which gender (latin "genus") is required?
    //  - Masculine (DEFAULT)
    //  - Feminine
        desiredGenus = Genus.MASCULINE;
        if (optionsLc.startsWith("mot") || optionsLc.contains(" mot") ||
            optionsLc.startsWith("m.") || optionsLc.contains(" m.") ||
            optionsLc.startsWith("fem") || optionsLc.contains(" fem")) {
                desiredGenus = Genus.FEMININE;
        }
    // Which case (latin "casus") is required?
    //  - Nominative/Vardininkas (DEFAULT)
    //  - Genitive/Kilmininkas
    //  - Dative/Naudininkas
    //  - Accusative/Galininkas
    //  - Instrumental/Įnagininkas
    //  - Locative/Vietininkas
        desiredCasus = Casus.NOMINATIVE;
        if (options.startsWith("V.") || options.contains(" V.") ||
            optionsLc.startsWith("var") || optionsLc.contains(" var") ||
            optionsLc.startsWith("nom") || optionsLc.contains(" nom")) {
                desiredCasus = Casus.NOMINATIVE;
        } else if (
            options.startsWith("K.") || options.contains(" K.") ||
            optionsLc.startsWith("kil") || optionsLc.contains(" kil") ||
            optionsLc.startsWith("gen") || optionsLc.contains(" gen")) {
                desiredCasus = Casus.GENITIVE;
        } else if (
            options.startsWith("N.") || options.contains(" N.") ||
            optionsLc.startsWith("nau") || optionsLc.contains(" nau") ||
            optionsLc.startsWith("dat") || optionsLc.contains(" dat")) {
                desiredCasus = Casus.DATIVE;
        } else if (
            options.startsWith("G.") || options.contains(" G.") ||
            optionsLc.startsWith("gal") || optionsLc.contains(" gal") ||
            optionsLc.startsWith("acc") || optionsLc.contains(" acc")) {
                desiredCasus = Casus.ACCUSATIVE;
        } else if (
            options.startsWith("Įn.") || options.contains(" Įn.") ||
            optionsLc.startsWith("įna") || optionsLc.contains(" įna") ||
            optionsLc.startsWith("ina") || optionsLc.contains(" ina") ||
            optionsLc.startsWith("ins") || optionsLc.contains(" ins")) {
                desiredCasus = Casus.INSTRUMENTAL;
        } else if (
            options.startsWith("Vt.") || options.contains(" Vt.") ||
            optionsLc.startsWith("vie") || optionsLc.contains(" vie") ||
            optionsLc.startsWith("loc") || optionsLc.contains(" loc")) {
                desiredCasus = Casus.LOCATIVE;
        }
        financial = false;
        if (optionsLc.startsWith("fin") || optionsLc.contains(" fin") ||
            optionsLc.startsWith("prec") || optionsLc.contains(" prec") ||
            optionsLc.startsWith("tik") || optionsLc.contains(" tik")) {
                financial = true;
        }        
    }

/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String answer;
        answer=Numbers.number2Words(1958, "metai", "Įn. įvardž.");
        System.out.println(answer);
        answer=Numbers.number2Words(1134561, "litas", "G.");
        System.out.println(answer);
        answer=Numbers.number2Words(1134561, "litas", "G. finansinis");
        System.out.println(answer);
        assert Numbers.number2Words(1958, "metai", "Įn. įvardž.").equals(
            "tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais") :
            "Something went wrong...";
        assert Numbers.number2Words(1134561, "litas", "G.").equals(
            "milijoną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą") :
            "Something went wrong...";
        assert Numbers.number2Words(1134561, "litas", "G. fin").equals(
            "vieną milijoną vieną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą") :
            "Something went wrong...";
    }

}

class NumeralType {
    public static final int CARDINAL = 0;
    public static final int ORDINAL = 1;
    public static final int ORDINAL_DEFINITE = 2;
    public static final int CARDINAL_PLURAL = 3;
    public static final int DEFAULT = CARDINAL;
}
class Genus {
    public static final int MASCULINE = 0;
    public static final int FEMININE = 1;
    public static final int DEFAULT = MASCULINE;
}
class Numerus {
    public static final int SINGULAR = 0;
    public static final int PLURAL = 1;
    public static final int DEFAULT = SINGULAR;
}
class Casus {
    public static final int NOMINATIVE = 0;
    public static final int GENITIVE = 1;
    public static final int DATIVE = 2;
    public static final int ACCUSATIVE = 3;
    public static final int INSTRUMENTAL = 4;
    public static final int LOCATIVE = 5;
    public static final int DEFAULT = NOMINATIVE;
}
class NounFlexClass {
    public String className;
    public int gender;
    public String [][][] endings;
    public List<String> specialCaseMembers;
    public NounFlexClass (int gender, String [] members, String [][][] endings) {
        this.gender = gender;
        this.endings = endings;
        specialCaseMembers = Arrays.asList(members);
    }
}
class NumAtomWord {
    static final String [][][][][] NUMBER_1_TO_9 = {
	{	{{
			{"vienas", "vieno", "vienam", "vieną", "vienu", "viename"}
		},{
			{"viena", "vienos", "vienai", "vieną", "viena", "vienoje"}
		}},{{
			{"pirmas", "pirmo", "pirmam", "pirmą", "pirmu", "pirmame"},
			{"pirmi", "pirmų", "pirmiems", "pirmus", "pirmais", "pirmuose"}
		},{
			{"pirma", "pirmos", "pirmai", "pirmą", "pirma", "pirmoje"},
			{"pirmos", "pirmų", "pirmoms", "pirmas", "pirmomis", "pirmose"}
		}},{{
			{"pirmasis", "pirmojo", "pirmajam", "pirmąjį", "pirmuoju", "pirmajame"},
			{"pirmieji", "pirmųjų", "pirmiesiems", "pirmuosius", "pirmaisiais", "pirmuosiuose"}
		},{
			{"pirmoji", "pirmosios", "pirmajai", "pirmąją", "pirmąja", "pirmojoje"},
			{"pirmosios", "pirmųjų", "pirmosioms", "pirmąsias", "pirmosiomis", "pirmosiose"}
		}},{{
			{"vieneri", "vienerių", "vieneriems", "vienerius", "vieneriais", "vieneriuose"}
		},{
			{"vienerios", "vienerių", "vienerioms", "vienerias", "vieneriomis", "vieneriose"}
		}}
	},{	{{
			{"du", "dviejų", "dviem", "du", "dviem", "dviejuose"}
		},{
			{"dvi", "dviejų", "dviem", "dvi", "dviem", "dviejose"}
		}},{{
			{"antras", "antro", "antram", "antrą", "antru", "antrame"},
			{"antri", "antrų", "antriems", "antrus", "antrais", "antruose"}
		},{
			{"antra", "antros", "antrai", "antrą", "antra", "antroje"},
			{"antros", "antrų", "antroms", "antras", "antromis", "antrose"}
		}},{{
			{"antrasis", "antrojo", "antrajam", "antrąjį", "antruoju", "antrajame"},
			{"antrieji", "antrųjų", "antriesiems", "antruosius", "antraisiais", "antruosiuose"}
		},{
			{"antroji", "antrosios", "antrajai", "antrąją", "antrąja", "antrojoje"},
			{"antrosios", "antrųjų", "antrosioms", "antrąsias", "antrosiomis", "antrosiose"}
		}},{{
			{"dveji", "dvejų", "dvejiems", "dvejus", "dvejais", "dvejuose"}
		},{
			{"dvejos", "dvejų", "dvejoms", "dvejas", "dvejomis", "dvejose"}
		}}
	},{	{{
			{"trys", "trijų", "trims", "tris", "trimis", "trijuose"}
		},{
			{"trys", "trijų", "trims", "tris", "trimis", "trijose"}
		}},{{
			{"trečias", "trečio", "trečiam", "trečią", "trečiu", "trečiame"},
			{"treti", "trečių", "tretiems", "trečius", "trečiais", "trečiuose"}
		},{
			{"trečia", "trečios", "trečiai", "trečią", "trečia", "trečioje"},
			{"trečios", "trečių", "trečioms", "trečias", "trečiomis", "trečiose"}
		}},{{
			{"trečiasis", "trečiojo", "trečiajam", "trečiąjį", "trečiuoju", "trečiajame"},
			{"tretieji", "trečiųjų", "tretiesiems", "trečiuosius", "trečiaisiais", "trečiuosiuose"}
		},{
			{"trečioji", "trečiosios", "trečiajai", "trečiąją", "trečiąja", "trečiojoje"},
			{"trečiosios", "trečiųjų", "trečiosioms", "trečiąsias", "trečiosiomis", "trečiosiose"}
		}},{{
			{"treji", "trejų", "trejiems", "trejus", "trejais", "trejuose"},
		},{
			{"trejos", "trejų", "trejoms", "trejas", "trejomis", "trejose"}
		}}
	},{	{{
			{"keturi", "keturių", "keturiems", "keturis", "keturiais", "keturiuose"}
		},{
			{"keturios", "keturių", "keturioms", "keturias", "keturiomis", "keturiose"}
		}},{{
			{"ketvirtas", "ketvirto", "ketvirtam", "ketvirtą", "ketvirtu", "ketvirtame"},
			{"ketvirti", "ketvirtų", "ketvirtiems", "ketvirtus", "ketvirtais", "ketvirtuose"}
		},{
			{"ketvirta", "ketvirtos", "ketvirtai", "ketvirtą", "ketvirta", "ketvirtoje"},
			{"ketvirtos", "ketvirtų", "ketvirtoms", "ketvirtas", "ketvirtomis", "ketvirtose"}
		}},{{
			{"ketvirtasis", "ketvirtojo", "ketvirtajam", "ketvirtąjį", "ketvirtuoju", "ketvirtajame"},
			{"ketvirtieji", "ketvirtųjų", "ketvirtiesiems", "ketvirtuosius", "ketvirtaisiais", "ketvirtuosiuose"}
		},{
			{"ketvirtoji", "ketvirtosios", "ketvirtajai", "ketvirtąją", "ketvirtąja", "ketvirtojoje"},
			{"ketvirtosios", "ketvirtųjų", "ketvirtosioms", "ketvirtąsias", "ketvirtosiomis", "ketvirtosiose"}
		}},{{
			{"ketveri", "ketverių", "ketveriems", "ketverius", "ketveriais", "ketveriuose"}
		},{
			{"ketverios", "ketverių", "ketverioms", "ketverias", "ketveriomis", "ketveriose"}
		}}
	},{	{{
			{"penki", "penkių", "penkiems", "penkis", "penkiais", "penkiuose"}
		},{
			{"penkios", "penkių", "penkioms", "penkias", "penkiomis", "penkiose"}
		}},{{
			{"penktas", "penkto", "penktam", "penktą", "penktu", "penktame"},
			{"penkti", "penktų", "penktiems", "penktus", "penktais", "penktuose"}
		},{
			{"penkta", "penktos", "penktai", "penktą", "penkta", "penktoje"},
			{"penktos", "penktų", "penktoms", "penktas", "penktomis", "penktose"}
		}},{{
			{"penktasis", "penktojo", "penktajam", "penktąjį", "penktuoju", "penktajame"},
			{"penktieji", "penktųjų", "penktiesiems", "penktuosius", "penktaisiais", "penktuosiuose"}
		},{
			{"penktoji", "penktosios", "penktajai", "penktąją", "penktąja", "penktojoje"},
			{"penktosios", "penktųjų", "penktosioms", "penktąsias", "penktosiomis", "penktosiose"}
		}},{{
			{"penkeri", "penkerių", "penkeriems", "penkerius", "penkeriais", "penkeriuose"}
		},{
			{"penkerios", "penkerių", "penkerioms", "penkerias", "penkeriomis", "penkeriose"}
		}}
	},{	{{
			{"šeši", "šešių", "šešiems", "šešis", "šešiais", "šešiuose"}
		},{
			{"šešios", "šešių", "šešioms", "šešias", "šešiomis", "šešiose"}
		}},{{
			{"šeštas", "šešto", "šeštam", "šeštą", "šeštu", "šeštame"},
			{"šešti", "šeštų", "šeštiems", "šeštus", "šeštais", "šeštuose"}
		},{
			{"šešta", "šeštos", "šeštai", "šeštą", "šešta", "šeštoje"},
			{"šeštos", "šeštų", "šeštoms", "šeštas", "šeštomis", "šeštose"}
		}},{{
			{"šeštasis", "šeštojo", "šeštajam", "šeštąjį", "šeštuoju", "šeštajame"},
			{"šeštieji", "šeštųjų", "šeštiesiems", "šeštuosius", "šeštaisiais", "šeštuosiuose"}
		},{
			{"šeštoji", "šeštosios", "šeštajai", "šeštąją", "šeštąja", "šeštojoje"},
			{"šeštosios", "šeštųjų", "šeštosioms", "šeštąsias", "šeštosiomis", "šeštosiose"}
		}},{{
			{"šešeri", "šešerių", "šešeriems", "šešerius", "šešeriais", "šešeriuose"}
		},{
			{"šešerios", "šešerių", "šešerioms", "šešerias", "šešeriomis", "šešeriose"}
		}}
	},{	{{
			{"septyni", "septynių", "septyniems", "septynis", "septyniais", "septyniuose"}
		},{
			{"septynios", "septynių", "septynioms", "septynias", "septyniomis", "septyniose"}
		}},{{
			{"septintas", "septinto", "septintam", "septintą", "septintu", "septintame"},
			{"septinti", "septintų", "septintiems", "septintus", "septintais", "septintuose"}
		},{
			{"septinta", "septintos", "septintai", "septintą", "septinta", "septintoje"},
			{"septintos", "septintų", "septintoms", "septintas", "septintomis", "septintose"}
		}},{{
			{"septintasis", "septintojo", "septintajam", "septintąjį", "septintuoju", "septintajame"},
			{"septintieji", "septintųjų", "septintiesiems", "septintuosius", "septintaisiais", "septintuosiuose"}
		},{
			{"septintoji", "septintosios", "septintajai", "septintąją", "septintąja", "septintojoje"},
			{"septintosios", "septintųjų", "septintosioms", "septintąsias", "septintosiomis", "septintosiose"}
		}},{{
			{"septyneri", "septynerių", "septyneriems", "septynerius", "septyneriais", "septyneriuose"}
		},{
			{"septynerios", "septynerių", "septynerioms", "septynerias", "septyneriomis", "septyneriose"}
		}}
	},{	{{
			{"aštuoni", "aštuonių", "aštuoniems", "aštuonis", "aštuoniais", "aštuoniuose"}
		},{
			{"aštuonios", "aštuonių", "aštuonioms", "aštuonias", "aštuoniomis", "aštuoniose"}
		}},{{
			{"aštuntas", "aštunto", "aštuntam", "aštuntą", "aštuntu", "aštuntame"},
			{"aštunti", "aštuntų", "aštuntiems", "aštuntus", "aštuntais", "aštuntuose"}
		},{
			{"aštunta", "aštuntos", "aštuntai", "aštuntą", "aštunta", "aštuntoje"},
			{"aštuntos", "aštuntų", "aštuntoms", "aštuntas", "aštuntomis", "aštuntose"}
		}},{{
			{"aštuntasis", "aštuntojo", "aštuntajam", "aštuntąjį", "aštuntuoju", "aštuntajame"},
			{"aštuntieji", "aštuntųjų", "aštuntiesiems", "aštuntuosius", "aštuntaisiais", "aštuntuosiuose"}
		},{
			{"aštuntoji", "aštuntosios", "aštuntajai", "aštuntąją", "aštuntąja", "aštuntojoje"},
			{"aštuntosios", "aštuntųjų", "aštuntosioms", "aštuntąsias", "aštuntosiomis", "septintosiose"}
		}},{{
			{"aštuoneri", "aštuonerių", "aštuoneriems", "aštuonerius", "aštuoneriais", "aštuoneriuose"}
		},{
			{"aštuonerios", "aštuonerių", "aštuonerioms", "aštuonerias", "aštuoneriomis", "aštuoneriose"}
		}}
	},{	{{
			{"devyni", "devynių", "devyniems", "devynis", "devyniais", "devyniuose"},
		},{
			{"devynios", "devynių", "devynioms", "devynias", "devyniomis", "devyniose"},
		}},{{
			{"devintas", "devinto", "devintam", "devintą", "devintu", "devintame"},
			{"devinti", "devintų", "devintiems", "devintus", "devintais", "devintuose"}
		},{
			{"devinta", "devintos", "devintai", "devintą", "devinta", "devintoje"},
			{"devintos", "devintų", "devintoms", "devintas", "devintomis", "devintose"}
		}},{{
			{"devintasis", "devintojo", "devintajam", "devintąjį", "devintuoju", "devintajame"},
			{"devintieji", "devintųjų", "devintiesiems", "devintuosius", "devintaisiais", "devintuosiuose"}
		},{
			{"devintoji", "devintosios", "devintajai", "devintąją", "devintąja", "devintojoje"},
			{"devintosios", "devintųjų", "devintosioms", "devintąsias", "devintosiomis", "devintosiose"}
		}},{{
			{"devyneri", "devynerių", "devyneriems", "devynerius", "devyneriais", "devyneriuose"}
		},{
			{"devynerios", "devynerių", "devynerioms", "devynerias", "devyneriomis", "devyneriose"}
		}}
	}
    };
    static final String [][][][][] NUMBER_10_TO_19 = {
	{	{{
			{"dešimt", "dešimties", "dešimčiai", "dešimtį", "dešimtimi", "dešimtyje"}
		}},{{
			{"dešimtas", "dešimto", "dešimtam", "dešimtą", "dešimtu", "dešimtame"},
			{"dešimti", "dešimtų", "dešimtiems", "dešimtus", "dešimtais", "dešimtuose"},
		},{
			{"dešimta", "dešimtos", "dešimtai", "dešimtą", "dešimta", "dešimtoje"},
			{"dešimtos", "dešimtų", "dešimtoms", "dešimtas", "dešimtomis", "dešimtose"}
		}},{{
			{"dešimtasis", "dešimtojo", "dešimtajam", "dešimtąjį", "dešimtuoju", "dešimtajame"},
			{"dešimtieji", "dešimtųjų", "dešimtiesiems", "dešimtuosius", "dešimtaisiais", "dešimtuosiuose"}
		},{
			{"dešimtoji", "dešimtosios", "dešimtajai", "dešimtąją", "dešimtąja", "dešimtojoje"},
			{"dešimtosios", "dešimtųjų", "dešimtosioms", "dešimtąsias", "dešimtosiomis", "dešimtosiose"}
		}}
	},{	{{
			{"vienuolika", "vienuolikos", "vienuolikai", "vienuolika", "vienuolika", "vienuolikoje"}
		}},{{
			{"vienuoliktas", "vienuolikto", "vienuoliktam", "vienuoliktą", "vienuoliktu", "vienuoliktame"},
			{"vienuolikti", "vienuoliktų", "vienuoliktiems", "vienuoliktus", "vienuoliktais", "vienuoliktuose"}
		},{
			{"vienuolikta", "vienuoliktos", "vienuoliktai", "vienuoliktą", "vienuolikta", "vienuoliktoje"},
			{"vienuoliktos", "vienuoliktų", "vienuoliktoms", "vienuoliktas", "vienuoliktomis", "vienuoliktose"}
		}},{{
			{"vienuoliktasis", "vienuoliktojo", "vienuoliktajam", "vienuoliktąjį", "vienuoliktuoju", "vienuoliktajame"},
			{"vienuoliktieji", "vienuoliktųjų", "vienuoliktiesiems", "vienuoliktuosius", "vienuoliktaisiais", "vienuoliktuosiuose"}
		},{
			{"vienuoliktoji", "vienuoliktosios", "vienuoliktajai", "vienuoliktąją", "vienuoliktąja", "vienuoliktojoje"},
			{"vienuoliktosios", "vienuoliktųjų", "vienuoliktosioms", "vienuoliktąsias", "vienuoliktosiomis", "vienuoliktosiose"}
		}}
	},{	{{
			{"dvylika", "dvylikos", "dvylikai", "dvylika", "dvylika", "dvylikoje"}
		}},{{
			{"dvyliktas", "dvylikto", "dvyliktam", "dvyliktą", "dvyliktu", "dvyliktame"},
			{"dvylikti", "dvyliktų", "dvyliktiems", "dvyliktus", "dvyliktais", "dvyliktuose"},
		},{
			{"dvylikta", "dvyliktos", "dvyliktai", "dvyliktą", "dvylikta", "dvyliktoje"},
			{"dvyliktos", "dvyliktų", "dvyliktoms", "dvyliktas", "dvyliktomis", "dvyliktose"}
		}},{{
			{"dvyliktasis", "dvyliktojo", "dvyliktajam", "dvyliktąjį", "dvyliktuoju", "dvyliktajame"},
			{"dvyliktieji", "dvyliktųjų", "dvyliktiesiems", "dvyliktuosius", "dvyliktaisiais", "dvyliktuosiuose"}
		},{
			{"dvyliktoji", "dvyliktosios", "dvyliktajai", "dvyliktąją", "dvyliktąja", "dvyliktojoje"},
			{"dvyliktosios", "dvyliktųjų", "dvyliktosioms", "dvyliktąsias", "dvyliktosiomis", "dvyliktosiose"}
		}}
	},{	{{
			{"trylika", "trylikos", "trylikai", "trylika", "trylika", "trylikoje"}
		}},{{
			{"tryliktas", "trylikto", "tryliktam", "tryliktą", "tryliktu", "tryliktame"},
			{"trylikti", "tryliktų", "tryliktiems", "tryliktus", "tryliktais", "tryliktuose"},
		},{
			{"trylikta", "tryliktos", "tryliktai", "tryliktą", "trylikta", "tryliktoje"},
			{"tryliktos", "tryliktų", "tryliktoms", "tryliktas", "tryliktomis", "tryliktose"}
		}},{{
			{"tryliktasis", "tryliktojo", "tryliktajam", "tryliktąjį", "tryliktuoju", "tryliktajame"},
			{"tryliktieji", "tryliktųjų", "tryliktiesiems", "tryliktuosius", "tryliktaisiais", "tryliktuosiuose"}
		},{
			{"tryliktoji", "tryliktosios", "tryliktajai", "tryliktąją", "tryliktąja", "tryliktojoje"},
			{"tryliktosios", "tryliktųjų", "tryliktosioms", "tryliktąsias", "tryliktosiomis", "tryliktosiose"}
		}}
	},{	{{
			{"keturiolika", "keturiolikos", "keturiolikai", "keturiolika", "keturiolika", "keturiolikoje"}
		}},{{
			{"keturioliktas", "keturiolikto", "keturioliktam", "keturioliktą", "keturioliktu", "keturioliktame"},
			{"keturiolikti", "keturioliktų", "keturioliktiems", "keturioliktus", "keturioliktais", "keturioliktuose"},
		},{
			{"keturiolikta", "keturioliktos", "keturioliktai", "keturioliktą", "keturiolikta", "keturioliktoje"},
			{"keturioliktos", "keturioliktų", "keturioliktoms", "keturioliktas", "keturioliktomis", "keturioliktose"}
		}},{{
			{"keturioliktasis", "keturioliktojo", "keturioliktajam", "keturioliktąjį", "keturioliktuoju", "keturioliktajame"},
			{"keturioliktieji", "keturioliktųjų", "keturioliktiesiems", "keturioliktuosius", "keturioliktaisiais", "keturioliktuosiuose"}
		},{
			{"keturioliktoji", "keturioliktosios", "keturioliktajai", "keturioliktąją", "keturioliktąja", "keturioliktojoje"},
			{"keturioliktosios", "keturioliktųjų", "keturioliktosioms", "keturioliktąsias", "keturioliktosiomis", "keturioliktosiose"}
		}}
	},{	{{
			{"penkiolika", "penkiolikos", "penkiolikai", "penkiolika", "penkiolika", "penkiolikoje"}
		}},{{
			{"penkioliktas", "penkiolikto", "penkioliktam", "penkioliktą", "penkioliktu", "penkioliktame"},
			{"penkiolikti", "penkioliktų", "penkioliktiems", "penkioliktus", "penkioliktais", "penkioliktuose"},
		},{
			{"penkiolikta", "penkioliktos", "penkioliktai", "penkioliktą", "penkiolikta", "penkioliktoje"},
			{"penkioliktos", "penkioliktų", "penkioliktoms", "penkioliktas", "penkioliktomis", "penkioliktose"}
		}},{{
			{"penkioliktasis", "penkioliktojo", "penkioliktajam", "penkioliktąjį", "penkioliktuoju", "penkioliktajame"},
			{"penkioliktieji", "penkioliktųjų", "penkioliktiesiems", "penkioliktuosius", "penkioliktaisiais", "penkioliktuosiuose"}
		},{
			{"penkioliktoji", "penkioliktosios", "penkioliktajai", "penkioliktąją", "penkioliktąja", "penkioliktojoje"},
			{"penkioliktosios", "penkioliktųjų", "penkioliktosioms", "penkioliktąsias", "penkioliktosiomis", "penkioliktosiose"}
		}}
	},{	{{
			{"šešiolika", "šešiolikos", "šešiolikai", "šešiolika", "šešiolika", "šešiolikoje"}
		}},{{
			{"šešioliktas", "šešiolikto", "šešioliktam", "šešioliktą", "šešioliktu", "šešioliktame"},
			{"šešiolikti", "šešioliktų", "šešioliktiems", "šešioliktus", "šešioliktais", "šešioliktuose"},
		},{
			{"šešiolikta", "šešioliktos", "šešioliktai", "šešioliktą", "šešiolikta", "šešioliktoje"},
			{"šešioliktos", "šešioliktų", "šešioliktoms", "šešioliktas", "šešioliktomis", "šešioliktose"}
		}},{{
			{"šešioliktasis", "šešioliktojo", "šešioliktajam", "šešioliktąjį", "šešioliktuoju", "šešioliktajame"},
			{"šešioliktieji", "šešioliktųjų", "šešioliktiesiems", "šešioliktuosius", "šešioliktaisiais", "šešioliktuosiuose"}
		},{
			{"šešioliktoji", "šešioliktosios", "šešioliktajai", "šešioliktąją", "šešioliktąja", "šešioliktojoje"},
			{"šešioliktosios", "šešioliktųjų", "šešioliktosioms", "šešioliktąsias", "šešioliktosiomis", "šešioliktosiose"}
		}}
	},{	{{
			{"septyniolika", "septyniolikos", "septyniolikai", "septyniolika", "septyniolika", "septyniolikoje"}
		}},{{
			{"septynioliktas", "septyniolikto", "septynioliktam", "septynioliktą", "septynioliktu", "septynioliktame"},
			{"septyniolikti", "septynioliktų", "septynioliktiems", "septynioliktus", "septynioliktais", "septynioliktuose"},
		},{
			{"septyniolikta", "septynioliktos", "septynioliktai", "septynioliktą", "septyniolikta", "septynioliktoje"},
			{"septynioliktos", "septynioliktų", "septynioliktoms", "septynioliktas", "septynioliktomis", "septynioliktose"}
		}},{{
			{"septynioliktasis", "septynioliktojo", "septynioliktajam", "septynioliktąjį", "septynioliktuoju", "septynioliktajame"},
			{"septynioliktieji", "septynioliktųjų", "septynioliktiesiems", "septynioliktuosius", "septynioliktaisiais", "septynioliktuosiuose"}
		},{
			{"septynioliktoji", "septynioliktosios", "septynioliktajai", "septynioliktąją", "septynioliktąja", "septynioliktojoje"},
			{"septynioliktosios", "septynioliktųjų", "septynioliktosioms", "septynioliktąsias", "septynioliktosiomis", "septynioliktosiose"}
		}}
	},{	{{
			{"aštuoniolika", "aštuoniolikos", "aštuoniolikai", "aštuoniolika", "aštuoniolika", "aštuoniolikoje"}
		}},{{
			{"aštuonioliktas", "aštuoniolikto", "aštuonioliktam", "aštuonioliktą", "aštuonioliktu", "aštuonioliktame"},
			{"aštuoniolikti", "aštuonioliktų", "aštuonioliktiems", "aštuonioliktus", "aštuonioliktais", "aštuonioliktuose"},
		},{
			{"aštuoniolikta", "aštuonioliktos", "aštuonioliktai", "aštuonioliktą", "aštuoniolikta", "aštuonioliktoje"},
			{"aštuonioliktos", "aštuonioliktų", "aštuonioliktoms", "aštuonioliktas", "aštuonioliktomis", "aštuonioliktose"}
		}},{{
			{"aštuonioliktasis", "aštuonioliktojo", "aštuonioliktajam", "aštuonioliktąjį", "aštuonioliktuoju", "aštuonioliktajame"},
			{"aštuonioliktieji", "aštuonioliktųjų", "aštuonioliktiesiems", "aštuonioliktuosius", "aštuonioliktaisiais", "aštuonioliktuosiuose"}
		},{
			{"aštuonioliktoji", "aštuonioliktosios", "aštuonioliktajai", "aštuonioliktąją", "aštuonioliktąja", "aštuonioliktojoje"},
			{"aštuonioliktosios", "aštuonioliktųjų", "aštuonioliktosioms", "aštuonioliktąsias", "aštuonioliktosiomis", "aštuonioliktosiose"}
		}}
	},{	{{
			{"devyniolika", "devyniolikos", "devyniolikai", "devyniolika", "devyniolika", "devyniolikoje"}
		}},{{
			{"devynioliktas", "devyniolikto", "devynioliktam", "devynioliktą", "devynioliktu", "devynioliktame"},
			{"devyniolikti", "devynioliktų", "devynioliktiems", "devynioliktus", "devynioliktais", "devynioliktuose"},
		},{
			{"devyniolikta", "devynioliktos", "devynioliktai", "devynioliktą", "devyniolikta", "devynioliktoje"},
			{"devynioliktos", "devynioliktų", "devynioliktoms", "devynioliktas", "devynioliktomis", "devynioliktose"}
		}},{{
			{"devynioliktasis", "devynioliktojo", "devynioliktajam", "devynioliktąjį", "devynioliktuoju", "devynioliktajame"},
			{"devynioliktieji", "devynioliktųjų", "devynioliktiesiems", "devynioliktuosius", "devynioliktaisiais", "devynioliktuosiuose"}
		},{
			{"devynioliktoji", "devynioliktosios", "devynioliktajai", "devynioliktąją", "devynioliktąja", "devynioliktojoje"},
			{"devynioliktosios", "devynioliktųjų", "devynioliktosioms", "devynioliktąsias", "devynioliktosiomis", "devynioliktosiose"}
		}}
	}
    };
    static final String [][][][][] NUMBER_10_SCALE = {
	{	{{
			{"dvidešimt", "dvidešimties", "dvidešimčiai", "dvidešimtį", "dvidešimtimi", "dvidešimtyje"}
		}},{{
			{"dvidešimtas", "dvidešimto", "dvidešimtam", "dvidešimtą", "dvidešimtu", "dvidešimtame"},
			{"dvidešimti", "dvidešimtų", "dvidešimtiems", "dvidešimtus", "dvidešimtais", "dvidešimtuose"}
		},{
			{"dvidešimta", "dvidešimtos", "dvidešimtai", "dvidešimtą", "dvidešimta", "dvidešimtoje"},
			{"dvidešimtos", "dvidešimtų", "dvidešimtoms", "dvidešimtas", "dvidešimtomis", "dvidešimtose"}
		}},{{
			{"dvidešimtasis", "dvidešimtojo", "dvidešimtajam", "dvidešimtąjį", "dvidešimtuoju", "dvidešimtajame"},
			{"dvidešimtieji", "dvidešimtųjų", "dvidešimtiesiems", "dvidešimtuosius", "dvidešimtaisiais", "dvidešimtuosiuose"}
		},{
			{"dvidešimtoji", "dvidešimtosios", "dvidešimtajai", "dvidešimtąją", "dvidešimtąja", "dvidešimtojoje"},
			{"dvidešimtosios", "dvidešimtųjų", "dvidešimtosioms", "dvidešimtąsias", "dvidešimtosiomis", "dvidešimtosiose"}
		}}
	},{	{{
			{"trisdešimt", "trisdešimties", "trisdešimčiai", "trisdešimtį", "trisdešimtimi", "trisdešimtyje"}
		}},{{
			{"trisdešimtas", "trisdešimto", "trisdešimtam", "trisdešimtą", "trisdešimtu", "trisdešimtame"},
			{"trisdešimti", "trisdešimtų", "trisdešimtiems", "trisdešimtus", "trisdešimtais", "trisdešimtuose"},
		},{
			{"trisdešimta", "trisdešimtos", "trisdešimtai", "trisdešimtą", "trisdešimta", "trisdešimtoje"},
			{"trisdešimtos", "trisdešimtų", "trisdešimtoms", "trisdešimtas", "trisdešimtomis", "trisdešimtose"}
		}},{{
			{"trisdešimtasis", "trisdešimtojo", "trisdešimtajam", "trisdešimtąjį", "trisdešimtuoju", "trisdešimtajame"},
			{"trisdešimtieji", "trisdešimtųjų", "trisdešimtiesiems", "trisdešimtuosius", "trisdešimtaisiais", "trisdešimtuosiuose"}
		},{
			{"trisdešimtoji", "trisdešimtosios", "trisdešimtajai", "trisdešimtąją", "trisdešimtąja", "trisdešimtojoje"},
			{"trisdešimtosios", "trisdešimtųjų", "trisdešimtosioms", "trisdešimtąsias", "trisdešimtosiomis", "trisdešimtosiose"}
		}}
	},{	{{
			{"keturiasdešimt", "keturiasdešimties", "keturiasdešimčiai", "keturiasdešimtį", "keturiasdešimtimi", "keturiasdešimtyje"}
		}},{{
			{"keturiasdešimtas", "keturiasdešimto", "keturiasdešimtam", "keturiasdešimtą", "keturiasdešimtu", "keturiasdešimtame"},
			{"keturiasdešimti", "keturiasdešimtų", "keturiasdešimtiems", "keturiasdešimtus", "keturiasdešimtais", "keturiasdešimtuose"},
		},{
			{"keturiasdešimta", "keturiasdešimtos", "keturiasdešimtai", "keturiasdešimtą", "keturiasdešimta", "keturiasdešimtoje"},
			{"keturiasdešimtos", "keturiasdešimtų", "keturiasdešimtoms", "keturiasdešimtas", "keturiasdešimtomis", "keturiasdešimtose"}
		}},{{
			{"keturiasdešimtasis", "keturiasdešimtojo", "keturiasdešimtajam", "keturiasdešimtąjį", "keturiasdešimtuoju", "keturiasdešimtajame"},
			{"keturiasdešimtieji", "keturiasdešimtųjų", "keturiasdešimtiesiems", "keturiasdešimtuosius", "keturiasdešimtaisiais", "keturiasdešimtuosiuose"}
		},{
			{"keturiasdešimtoji", "keturiasdešimtosios", "keturiasdešimtajai", "keturiasdešimtąją", "keturiasdešimtąja", "keturiasdešimtojoje"},
			{"keturiasdešimtosios", "keturiasdešimtųjų", "keturiasdešimtosioms", "keturiasdešimtąsias", "keturiasdešimtosiomis", "keturiasdešimtosiose"}
		}}
	},{	{{
			{"penkiasdešimt", "penkiasdešimties", "penkiasdešimčiai", "penkiasdešimtį", "penkiasdešimtimi", "penkiasdešimtyje"}
		}},{{
			{"penkiasdešimtas", "penkiasdešimto", "penkiasdešimtam", "penkiasdešimtą", "penkiasdešimtu", "penkiasdešimtame"},
			{"penkiasdešimti", "penkiasdešimtų", "penkiasdešimtiems", "penkiasdešimtus", "penkiasdešimtais", "penkiasdešimtuose"},
		},{
			{"penkiasdešimta", "penkiasdešimtos", "penkiasdešimtai", "penkiasdešimtą", "penkiasdešimta", "penkiasdešimtoje"},
			{"penkiasdešimtos", "penkiasdešimtų", "penkiasdešimtoms", "penkiasdešimtas", "penkiasdešimtomis", "penkiasdešimtose"}
		}},{{
			{"penkiasdešimtasis", "penkiasdešimtojo", "penkiasdešimtajam", "penkiasdešimtąjį", "penkiasdešimtuoju", "penkiasdešimtajame"},
			{"penkiasdešimtieji", "penkiasdešimtųjų", "penkiasdešimtiesiems", "penkiasdešimtuosius", "penkiasdešimtaisiais", "penkiasdešimtuosiuose"}
		},{
			{"penkiasdešimtoji", "penkiasdešimtosios", "penkiasdešimtajai", "penkiasdešimtąją", "penkiasdešimtąja", "penkiasdešimtojoje"},
			{"penkiasdešimtosios", "penkiasdešimtųjų", "penkiasdešimtosioms", "penkiasdešimtąsias", "penkiasdešimtosiomis", "penkiasdešimtosiose"}
		}}
	},{	{{
			{"šešiasdešimt", "šešiasdešimties", "šešiasdešimčiai", "šešiasdešimtį", "šešiasdešimtimi", "šešiasdešimtyje"}
		}},{{
			{"šešiasdešimtas", "šešiasdešimto", "šešiasdešimtam", "šešiasdešimtą", "šešiasdešimtu", "šešiasdešimtame"},
			{"šešiasdešimti", "šešiasdešimtų", "šešiasdešimtiems", "šešiasdešimtus", "šešiasdešimtais", "šešiasdešimtuose"},
		},{
			{"šešiasdešimta", "šešiasdešimtos", "šešiasdešimtai", "šešiasdešimtą", "šešiasdešimta", "šešiasdešimtoje"},
			{"šešiasdešimtos", "šešiasdešimtų", "šešiasdešimtoms", "šešiasdešimtas", "šešiasdešimtomis", "šešiasdešimtose"}
		}},{{
			{"šešiasdešimtasis", "šešiasdešimtojo", "šešiasdešimtajam", "šešiasdešimtąjį", "šešiasdešimtuoju", "šešiasdešimtajame"},
			{"šešiasdešimtieji", "šešiasdešimtųjų", "šešiasdešimtiesiems", "šešiasdešimtuosius", "šešiasdešimtaisiais", "šešiasdešimtuosiuose"}
		},{
			{"šešiasdešimtoji", "šešiasdešimtosios", "šešiasdešimtajai", "šešiasdešimtąją", "šešiasdešimtąja", "šešiasdešimtojoje"},
			{"šešiasdešimtosios", "šešiasdešimtųjų", "šešiasdešimtosioms", "šešiasdešimtąsias", "šešiasdešimtosiomis", "šešiasdešimtosiose"}
		}}
	},{	{{
			{"septyniasdešimt", "septyniasdešimties", "septyniasdešimčiai", "septyniasdešimtį", "septyniasdešimtimi", "septyniasdešimtyje"}
		}},{{
			{"septyniasdešimtas", "septyniasdešimto", "septyniasdešimtam", "septyniasdešimtą", "septyniasdešimtu", "septyniasdešimtame"},
			{"septyniasdešimti", "septyniasdešimtų", "septyniasdešimtiems", "septyniasdešimtus", "septyniasdešimtais", "septyniasdešimtuose"},
		},{
			{"septyniasdešimta", "septyniasdešimtos", "septyniasdešimtai", "septyniasdešimtą", "septyniasdešimta", "septyniasdešimtoje"},
			{"septyniasdešimtos", "septyniasdešimtų", "septyniasdešimtoms", "septyniasdešimtas", "septyniasdešimtomis", "septyniasdešimtose"}
		}},{{
			{"septyniasdešimtasis", "septyniasdešimtojo", "septyniasdešimtajam", "septyniasdešimtąjį", "septyniasdešimtuoju", "septyniasdešimtajame"},
			{"septyniasdešimtieji", "septyniasdešimtųjų", "septyniasdešimtiesiems", "septyniasdešimtuosius", "septyniasdešimtaisiais", "septyniasdešimtuosiuose"}
		},{
			{"septyniasdešimtoji", "septyniasdešimtosios", "septyniasdešimtajai", "septyniasdešimtąją", "septyniasdešimtąja", "septyniasdešimtojoje"},
			{"septyniasdešimtosios", "septyniasdešimtųjų", "septyniasdešimtosioms", "septyniasdešimtąsias", "septyniasdešimtosiomis", "septyniasdešimtosiose"}
		}}
	},{	{{
			{"aštuoniasdešimt", "aštuoniasdešimties", "aštuoniasdešimčiai", "aštuoniasdešimtį", "aštuoniasdešimtimi", "aštuoniasdešimtyje"}
		}},{{
			{"aštuoniasdešimtas", "aštuoniasdešimto", "aštuoniasdešimtam", "aštuoniasdešimtą", "aštuoniasdešimtu", "aštuoniasdešimtame"},
			{"aštuoniasdešimti", "aštuoniasdešimtų", "aštuoniasdešimtiems", "aštuoniasdešimtus", "aštuoniasdešimtais", "aštuoniasdešimtuose"},
		},{
			{"aštuoniasdešimta", "aštuoniasdešimtos", "aštuoniasdešimtai", "aštuoniasdešimtą", "aštuoniasdešimta", "aštuoniasdešimtoje"},
			{"aštuoniasdešimtos", "aštuoniasdešimtų", "aštuoniasdešimtoms", "aštuoniasdešimtas", "aštuoniasdešimtomis", "aštuoniasdešimtose"}
		}},{{
			{"aštuoniasdešimtasis", "aštuoniasdešimtojo", "aštuoniasdešimtajam", "aštuoniasdešimtąjį", "aštuoniasdešimtuoju", "aštuoniasdešimtajame"},
			{"aštuoniasdešimtieji", "aštuoniasdešimtųjų", "aštuoniasdešimtiesiems", "aštuoniasdešimtuosius", "aštuoniasdešimtaisiais", "aštuoniasdešimtuosiuose"}
		},{
			{"aštuoniasdešimtoji", "aštuoniasdešimtosios", "aštuoniasdešimtajai", "aštuoniasdešimtąją", "aštuoniasdešimtąja", "aštuoniasdešimtojoje"},
			{"aštuoniasdešimtosios", "aštuoniasdešimtųjų", "aštuoniasdešimtosioms", "aštuoniasdešimtąsias", "aštuoniasdešimtosiomis", "aštuoniasdešimtosiose"}
		}}
	},{	{{
			{"devyniasdešimt", "devyniasdešimties", "devyniasdešimčiai", "devyniasdešimtį", "devyniasdešimtimi", "devyniasdešimtyje"}
		}},{{
			{"devyniasdešimtas", "devyniasdešimto", "devyniasdešimtam", "devyniasdešimtą", "devyniasdešimtu", "devyniasdešimtame"},
			{"devyniasdešimti", "devyniasdešimtų", "devyniasdešimtiems", "devyniasdešimtus", "devyniasdešimtais", "devyniasdešimtuose"},
		},{
			{"devyniasdešimta", "devyniasdešimtos", "devyniasdešimtai", "devyniasdešimtą", "devyniasdešimta", "devyniasdešimtoje"},
			{"devyniasdešimtos", "devyniasdešimtų", "devyniasdešimtoms", "devyniasdešimtas", "devyniasdešimtomis", "devyniasdešimtose"}
		}},{{
			{"devyniasdešimtasis", "devyniasdešimtojo", "devyniasdešimtajam", "devyniasdešimtąjį", "devyniasdešimtuoju", "devyniasdešimtajame"},
			{"devyniasdešimtieji", "devyniasdešimtųjų", "devyniasdešimtiesiems", "devyniasdešimtuosius", "devyniasdešimtaisiais", "devyniasdešimtuosiuose"}
		},{
			{"devyniasdešimtoji", "devyniasdešimtosios", "devyniasdešimtajai", "devyniasdešimtąją", "devyniasdešimtąja", "devyniasdešimtojoje"},
			{"devyniasdešimtosios", "devyniasdešimtųjų", "devyniasdešimtosioms", "devyniasdešimtąsias", "devyniasdešimtosiomis", "devyniasdešimtosiose"}
		}}
	}
    };
    static final String [][][][][] NUMBER_100 = {
	{	{{
			{"šimtas", "šimto", "šimtui", "šimtą", "šimtu", "šimte"},
			{"šimtai", "šimtų", "šimtams", "šimtus", "šimtais", "šimtuose"}
		}},{{
			{"šimtas", "šimto", "šimtam", "šimtą", "šimtu", "šimtame"},
			{"šimti", "šimtų", "šimtiems", "šimtus", "šimtais", "šimtuose"}
		},{
			{"šimta", "šimtos", "šimtai", "šimtą", "šimta", "šimtoje"},
			{"šimtos", "šimtų", "šimtoms", "šimtas", "šimtomis", "šimtose"}
		}},{{
			{"šimtasis", "šimtojo", "šimtajam", "šimtąjį", "šimtuoju", "šimtajame"},
			{"šimtieji", "šimtųjų", "šimtiesiems", "šimtuosius", "šimtaisiais", "šimtuosiuose"}
		},{
			{"šimtoji", "šimtosios", "šimtajai", "šimtąją", "šimtąja", "šimtojoje"},
			{"šimtosios", "šimtųjų", "šimtosioms", "šimtąsias", "šimtosiomis", "šimtosiose"}
		}}
	}
    };
    static final String [][][][][] NUMBER_1000_SCALE = {
	{	{{
			{"tūkstantis", "tūkstančio", "tūkstančiui", "tūkstantį", "tūkstančiu", "tūkstantyje"},
			{"tūkstančiai", "tūkstančių", "tūkstančiams", "tūkstančius", "tūkstančiais", "tūkstančiuose"}
		}},{{
			{"tūkstantas", "tūkstanto", "tūkstantam", "tūkstantą", "tūkstantu", "tūkstantame"},
			{"tūkstanti", "tūkstantų", "tūkstantiems", "tūkstantus", "tūkstantais", "tūkstantuose"}
		},{
			{"tūkstanta", "tūkstantos", "tūkstantai", "tūkstantą", "tūkstanta", "tūkstantoje"},
			{"tūkstantos", "tūkstantų", "tūkstantoms", "tūkstantas", "tūkstantomis", "tūkstantose"}
		}},{{
			{"tūkstantasis", "tūkstantojo", "tūkstantajam", "tūkstantąjį", "tūkstantuoju", "tūkstantajame"},
			{"tūkstantieji", "tūkstantųjų", "tūkstantiesiems", "tūkstantuosius", "tūkstantaisiais", "tūkstantuosiuose"}
		},{
			{"tūkstantoji", "tūkstantosios", "tūkstantajai", "tūkstantąją", "tūkstantąja", "tūkstantojoje"},
			{"tūkstantosios", "tūkstantųjų", "tūkstantosioms", "tūkstantąsias", "tūkstantosiomis", "tūkstantosiose"}
		}}
	},{	{{
			{"milijonas", "milijono", "milijonui", "milijoną", "milijonu", "milijone"},
			{"milijonai", "milijonų", "milijonams", "milijonus", "milijonais", "milijonuose"}
		}},{{
			{"milijonas", "milijono", "milijonam", "milijoną", "milijonu", "milijoname"},
			{"milijoni", "milijonų", "milijoniems", "milijonus", "milijonais", "milijonuose"}
		},{
			{"milijona", "milijonos", "milijonai", "milijoną", "milijona", "milijonoje"},
			{"milijonos", "milijonų", "milijonoms", "milijonas", "milijonomis", "milijonose"}
		}},{{
			{"milijonasis", "milijonojo", "milijonajam", "milijonąjį", "milijonuoju", "milijonajame"},
			{"milijonieji", "milijonųjų", "milijoniesiems", "milijonuosius", "milijonaisiais", "milijonuosiuose"}
		},{
			{"milijonoji", "milijonosios", "milijonajai", "milijonąją", "milijonąja", "milijonojoje"},
			{"milijonosios", "milijonųjų", "milijonosioms", "milijonąsias", "milijonosiomis", "milijonosiose"}
		}}
	},{	{{
			{"milijardas", "milijardo", "milijardui", "milijardą", "milijardu", "milijarde"},
			{"milijardai", "milijardų", "milijardams", "milijardus", "milijardais", "milijarduose"}
		}},{{
			{"milijardas", "milijardo", "milijardam", "milijardą", "milijardu", "milijardame"},
			{"milijardi", "milijardų", "milijardiems", "milijardus", "milijardais", "milijarduose"}
		},{
			{"milijarda", "milijardos", "milijardai", "milijardą", "milijarda", "milijardoje"},
			{"milijardos", "milijardų", "milijardoms", "milijardas", "milijardomis", "milijardose"}
		}},{{
			{"milijardasis", "milijardojo", "milijardajam", "milijardąjį", "milijarduoju", "milijardajame"},
			{"milijardieji", "milijardųjų", "milijardiesiems", "milijarduosius", "milijardaisiais", "milijarduosiuose"}
		},{
			{"milijardoji", "milijardosios", "milijardajai", "milijardąją", "milijardąja", "milijardojoje"},
			{"milijardosios", "milijardųjų", "milijardosioms", "milijardąsias", "milijardosiomis", "milijardosiose"}
		}}
	},{	{{
			{"trilijonas", "trilijono", "trilijonui", "trilijoną", "trilijonu", "trilijone"},
			{"trilijonai", "trilijonų", "trilijonams", "trilijonus", "trilijonais", "trilijonuose"}
		}},{{
			{"trilijonas", "trilijono", "trilijonam", "trilijoną", "trilijonu", "trilijoname"},
			{"trilijoni", "trilijonų", "trilijoniems", "trilijonus", "trilijonais", "trilijonuose"}
		},{
			{"trilijona", "trilijonos", "trilijonai", "trilijoną", "trilijona", "trilijonoje"},
			{"trilijonos", "trilijonų", "trilijonoms", "trilijonas", "trilijonomis", "trilijonose"}
		}},{{
			{"trilijonasis", "trilijonojo", "trilijonajam", "trilijonąjį", "trilijonuoju", "trilijonajame"},
			{"trilijonieji", "trilijonųjų", "trilijoniesiems", "trilijonuosius", "trilijonaisiais", "trilijonuosiuose"}
		},{
			{"trilijonoji", "trilijonosios", "trilijonajai", "trilijonąją", "trilijonąja", "trilijonojoje"},
			{"trilijonosios", "trilijonųjų", "trilijonosioms", "trilijonąsias", "trilijonosiomis", "trilijonosiose"}
		}}
	},{	{{
			{"kvadrilijonas", "kvadrilijono", "kvadrilijonui", "kvadrilijoną", "kvadrilijonu", "kvadrilijone"},
			{"kvadrilijonai", "kvadrilijonų", "kvadrilijonams", "kvadrilijonus", "kvadrilijonais", "kvadrilijonuose"}
		}},{{
			{"kvadrilijonas", "kvadrilijono", "kvadrilijonam", "kvadrilijoną", "kvadrilijonu", "kvadrilijoname"},
			{"kvadrilijoni", "kvadrilijonų", "kvadrilijoniems", "kvadrilijonus", "kvadrilijonais", "kvadrilijonuose"}
		},{
			{"kvadrilijona", "kvadrilijonos", "kvadrilijonai", "kvadrilijoną", "kvadrilijona", "kvadrilijonoje"},
			{"kvadrilijonos", "kvadrilijonų", "kvadrilijonoms", "kvadrilijonas", "kvadrilijonomis", "kvadrilijonose"}
		}},{{
			{"kvadrilijonasis", "kvadrilijonojo", "kvadrilijonajam", "kvadrilijonąjį", "kvadrilijonuoju", "kvadrilijonajame"},
			{"kvadrilijonieji", "kvadrilijonųjų", "kvadrilijoniesiems", "kvadrilijonuosius", "kvadrilijonaisiais", "kvadrilijonuosiuose"}
		},{
			{"kvadrilijonoji", "kvadrilijonosios", "kvadrilijonajai", "kvadrilijonąją", "kvadrilijonąja", "kvadrilijonojoje"},
			{"kvadrilijonosios", "kvadrilijonųjų", "kvadrilijonosioms", "kvadrilijonąsias", "kvadrilijonosiomis", "kvadrilijonosiose"}
		}}
	},{	{{
			{"kvintilijonas", "kvintilijono", "kvintilijonui", "kvintilijoną", "kvintilijonu", "kvintilijone"},
			{"kvintilijonai", "kvintilijonų", "kvintilijonams", "kvintilijonus", "kvintilijonais", "kvintilijonuose"}
		}},{{
			{"kvintilijonas", "kvintilijono", "kvintilijonam", "kvintilijoną", "kvintilijonu", "kvintilijoname"},
			{"kvintilijoni", "kvintilijonų", "kvintilijoniems", "kvintilijonus", "kvintilijonais", "kvintilijonuose"}
		},{
			{"kvintilijona", "kvintilijonos", "kvintilijonai", "kvintilijoną", "kvintilijona", "kvintilijonoje"},
			{"kvintilijonos", "kvintilijonų", "kvintilijonoms", "kvintilijonas", "kvintilijonomis", "kvintilijonose"}
		}},{{
			{"kvintilijonasis", "kvintilijonojo", "kvintilijonajam", "kvintilijonąjį", "kvintilijonuoju", "kvintilijonajame"},
			{"kvintilijonieji", "kvintilijonųjų", "kvintilijoniesiems", "kvintilijonuosius", "kvintilijonaisiais", "kvintilijonuosiuose"}
		},{
			{"kvintilijonoji", "kvintilijonosios", "kvintilijonajai", "kvintilijonąją", "kvintilijonąja", "kvintilijonojoje"},
			{"kvintilijonosios", "kvintilijonųjų", "kvintilijonosioms", "kvintilijonąsias", "kvintilijonosiomis", "kvintilijonosiose"}
		}}
	}
    };
    static final String [][][][][] ZERO = {
	{	{{
			{"nulis", "nulio", "nuliui", "nulį", "nuliu", "nulyje"}
		}},{{
			{"nulinis", "nulinio", "nuliniam", "nulinį", "nuliniu", "nuliniame"},
			{"nuliniai", "nulinių", "nuliniems", "nulinius", "nuliniais", "nuliniuose"}
		},{
			{"nulinė", "nulinės", "nulinei", "nulinę", "nuline", "nulinėje"},
			{"nulinės", "nulinių", "nulinėms", "nulines", "nulinėmis", "nulinėse"}
		}}
	}
    };
    public String atomText;
    public long atomValue;
    public boolean demandsPlural;
    public boolean demandsGenitive;
    public boolean canSupressCasus;
    public boolean canPreSupressCasus;
    public boolean acceptsPluralDemand;
    public boolean acceptsGenitiveDemand;
    public boolean acceptsCasusSupression;
    public boolean optional;
    private String [][][][] forms;
    private int requestedType;
    private int requestedGenus;
    private int requestedNumerus;
    private int requestedCasus;
    private int idxType;
    private int idxGenus;
    private int idxNumerus;
    private int idxCasus;
    
    public NumAtomWord (int number) {
        this ((long) number);
    }
    public NumAtomWord (long number) {
        demandsPlural = true;
        demandsGenitive = true;
        canSupressCasus = false;
        canPreSupressCasus = false;
        acceptsPluralDemand = false;
        acceptsGenitiveDemand = false;
        acceptsCasusSupression = false;
        optional = false;
        if (number >= 1 && number <= 9) {
            if (number == 1) {
                demandsPlural = false;
            }
            demandsGenitive = false;
            canPreSupressCasus = true;
            forms = NUMBER_1_TO_9 [(int) number - 1];
        }
        else if (number >= 10 && number <= 19) {
            forms = NUMBER_10_TO_19 [(int) number - 10];
        }
        else if ((number >= 20 && number <= 90) && number%10 == 0) {
            acceptsCasusSupression = true;
            forms = NUMBER_10_SCALE [(int) (number / 10) - 2];
        }
        else if (number == 100) {
            canSupressCasus = true;
            acceptsPluralDemand = true;
            acceptsGenitiveDemand = true;
            forms = NUMBER_100 [0];
        }
        else if (number >= 1000) {
            double deg1000 = Math.log10((double) number) / 3;
            if (deg1000 == (int) deg1000) {
                canSupressCasus = true;
                acceptsPluralDemand = true;
                acceptsGenitiveDemand = true;
                forms = NUMBER_1000_SCALE[((int) deg1000) - 1];
            }
        }
        else if (number == 0) {
            forms = ZERO [0];
        }
        if (forms != null) {
            atomValue = number;
            requestedType = NumeralType.DEFAULT;
            requestedGenus = Genus.DEFAULT;
            requestedNumerus = Numerus.DEFAULT;
            requestedCasus = Casus.DEFAULT;
            idxType = (requestedType < forms.length) ? requestedType : 0;
            idxGenus = (requestedGenus < forms[idxType].length) ? requestedGenus : 0;
            idxNumerus = (idxNumerus < forms[idxType][idxGenus].length) ? requestedNumerus : 0;
            idxCasus = (requestedCasus < forms[idxType][idxGenus][idxNumerus].length) ? requestedCasus : 0;
            atomText = forms[idxType][idxGenus][idxNumerus][idxCasus];
        }
        else {
            atomText = "";
            atomValue = number;
        }
    }

    public void setType(int requestedType) {
        this.requestedType = requestedType;
        idxType = (forms != null) ? ((requestedType >= 0 && requestedType < forms.length) ? requestedType : idxType) : 0;
        if (idxType != requestedType && requestedType == NumeralType.ORDINAL_DEFINITE) {
            idxType = (forms != null) ? ((NumeralType.ORDINAL >= 0 && NumeralType.ORDINAL < forms.length) ? NumeralType.ORDINAL : idxType) : 0;
        }
        idxGenus = (forms != null) ? ((requestedGenus >= 0 && requestedGenus < forms[idxType].length) ? requestedGenus : 0) : 0;
        idxNumerus = (forms != null) ? ((requestedNumerus >= 0 && requestedNumerus < forms[idxType][idxGenus].length) ? requestedNumerus : 0) : 0;
        idxCasus = (forms != null) ? ((requestedCasus >= 0 && requestedCasus < forms[idxType][idxGenus][idxNumerus].length) ? requestedCasus : 0) : 0;
        atomText = (forms != null) ? forms[idxType][idxGenus][idxNumerus][idxCasus] : "";
        if (idxType == requestedType && (requestedType == NumeralType.ORDINAL || requestedType == NumeralType.ORDINAL_DEFINITE)) {
            demandsPlural = false;
            demandsGenitive = false;
            acceptsPluralDemand = false;
            acceptsGenitiveDemand = false;
            acceptsCasusSupression = false;
        }
    }
    public void setGenus(int requestedGenus) {
        this.requestedGenus = requestedGenus;
        idxGenus = (forms != null) ? ((requestedGenus >= 0 && requestedGenus < forms[idxType].length) ? requestedGenus : idxGenus) : 0;
        idxNumerus = (forms != null) ? ((requestedNumerus >= 0 && requestedNumerus < forms[idxType][idxGenus].length) ? requestedNumerus : 0) : 0;
        idxCasus = (forms != null) ? ((requestedCasus >= 0 && requestedCasus < forms[idxType][idxGenus][idxNumerus].length) ? requestedCasus : 0) : 0;
        atomText = (forms != null) ? forms[idxType][idxGenus][idxNumerus][idxCasus] : "";
    }
    public void setNumerus(int requestedNumerus) {
        this.requestedNumerus = requestedNumerus;
        idxNumerus = (forms != null) ? ((requestedNumerus >= 0 && requestedNumerus < forms[idxType][idxGenus].length) ? requestedNumerus : idxNumerus) : 0;
        idxCasus = (forms != null) ? ((requestedCasus >= 0 && requestedCasus < forms[idxType][idxGenus][idxNumerus].length) ? requestedCasus : 0) : 0;
        atomText = (forms != null) ? forms[idxType][idxGenus][idxNumerus][idxCasus] : "";
    }
    public void setCasus(int requestedCasus) {
        this.requestedCasus = requestedCasus;
        idxCasus = (forms != null) ? ((requestedCasus >= 0 && requestedCasus < forms[idxType][idxGenus][idxNumerus].length) ? requestedCasus : idxCasus) : 0;
        atomText = (forms != null) ? forms[idxType][idxGenus][idxNumerus][idxCasus] : "";
    }
}
class Noun {
    private static final NounFlexClass [] NounFlexClasses = new NounFlexClass [] {
        new NounFlexClass(
            Genus.MASCULINE,
            new String [] {},
            new String [][][] {{
                {"čias", "čio", "čiui", "čią", "čiu", "tyje"},
                {"čiai", "čių", "čiams", "čius", "čiais", "čiuose"}
            },{
                {"ias", "io", "iui", "ią", "iu", "yje"},
                {"iai", "ių", "iams", "ius", "iais", "iuose"}
            },{
                {"jas", "jo", "jui", "ją", "ju", "juje"},
                {"jai", "jų", "jams", "jus", "jais", "juose"}
            },{
                {"as", "o", "ui", "ą", "u", "e"},
                {"ai", "ų", "ams", "us", "ais", "uose"}
            },{
                {"dis", "džio", "džiui", "dį", "džiu", "dyje"},
                {"džiai", "džių", "džiams", "džius", "džiais", "džiuose"}
            },{
                {"dys", "džio", "džiui", "dį", "džiu", "dyje"},
                {"džiai", "džių", "džiams", "džius", "džiais", "džiuose"}
            },{
                {"tis", "čio", "čiui", "tį", "čiu", "tyje"},
                {"čiai", "čių", "čiams", "čius", "čiais", "čiuose"}
            },{
                {"tys", "čio", "čiui", "tį", "čiu", "tyje"},
                {"čiai", "čių", "čiams", "čius", "čiais", "čiuose"}
            },{
                {"is", "io", "iui", "į", "iu", "yje"},
                {"iai", "ių", "iams", "ius", "iais", "iuose"}
            },{
                {"ys", "io", "iui", "į", "iu", "yje"},
                {"iai", "ių", "iams", "ius", "iais", "iuose"},
            },{
                {"ogus", "ogaus", "ogui", "ogų", "ogumi", "oguje"},
                {"onės", "onių", "onėms", "ones", "onėmis", "onėse"}
            },{
                {"ius", "iaus", "iui", "ių", "iumi", "iuje"},
                {"iai", "ių", "iams", "ius", "iais", "iuose"}
            },{
                {"jus", "jaus", "jui", "jų", "jumi", "juje"},
                {"jai", "jų", "jams", "jus", "jais", "juose"}
            },{
                {"us", "aus", "ui", "ų", "umi", "uje"},
                {"ūs", "ų", "ums", "us", "umis", "uose"}
            },{
                {"uo", "enio", "eniui", "enį", "eniu", "enyje"},
                {"enys", "enų", "enims", "enis", "enimis", "enyse"}
            }}
        ),
        // Masc noun class DEDE
        new NounFlexClass(
            Genus.MASCULINE,
            new String [] {
                "buožė", "dailidė", "dėdė", "dėdulė", "dučė", "kamikadzė", "tėtė"
            },
            new String [][][] {{
                {"tė", "tės", "tei", "tę", "te", "tėje"},
                {"tės", "čių", "tėms", "tes", "tėmis", "tėse"}
            },{
                {"dė", "dės", "dei", "dę", "de", "dėje"},
                {"dės", "džių", "dėms", "des", "dėmis", "dėse"}
            },{
                {"ė", "ės", "ei", "ę", "e", "ėje"},
                {"ės", "ių", "ėms", "es", "ėmis", "ėse"}
            }}
        ),
        // Fem noun class GENERAL
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {},
            new String [][][] {{
                {"tė", "tės", "tei", "tę", "te", "tėje"},
                {"tės", "čių", "tėms", "tes", "tėmis", "tėse"}
            },{
                {"dė", "dės", "dei", "dę", "de", "dėje"},
                {"dės", "džių", "dėms", "des", "dėmis", "dėse"}
            },{
                {"ė", "ės", "ei", "ę", "e", "ėje"},
                {"ės", "ių", "ėms", "es", "ėmis", "ėse"}
            },{
                {"a", "os", "ai", "ą", "a", "oje"},
                {"os", "ų", "oms", "as", "omis", "ose"}
            }}
        ),
        // Fem noun class SESUO
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {
                "sesuo"
            },
            new String [][][] {{
                {"uo", "ers", "eriai", "erį", "erimi", "eryje"},
                {"erys", "erų", "erims", "eris", "erimis", "eryse"}
            }}
        ),
        // Fem noun class GRANDIS
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {
                "blandis", "gardis", "grandis", "kandis", "liaudis", "šerdis",
                "širdis", "rūdys"        
            },
            new String [][][] {{
                {"is", "ies", "žiai", "į", "imi", "yje"},
                {"ys", "žių", "ims", "is", "imis", "yse"}
            }}
        ),
        // Fem noun class MOTERIS
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {
                "kriaunis", "krūtis", "moteris", "naktis", "obelis", "pažastis",
                "pušis", "pušnis", "šaknis", "šlaunis", "žąsis", "žuvis",
                "durys", "grindys", "kepenys", "šermenys", "smegenys"
            },
            new String [][][] {{
                {"is", "ies", "iai", "į", "imi", "yje"},
                {"ys", "ų", "ims", "is", "imis", "yse"}
            }}
        ),
        // Fem noun class AKIS
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {
                "akis", "angis", "anglis", "ašis", "avis", "blužnis", "dalis",
                "geležis", "gyvuonis", "kirkšnis", "krosnis", "krūsnis", "kulkšnis",
                "kulnis", "kulšis", "kvapsnis", "landuonis", "lūšis", "nosis",
                "omenis", "pašalis", "pilis", "plėnis", "pusnis", "rūšis",
                "skobnis", "šalis", "šalvis", "šūsnis", "tošis", "tulžis", "ugnis",
                "usnis", "vilnis", "vinis", "žiauberis", "traknys", "trūnys"
            },
            new String [][][] {{
                {"is", "ies", "iai", "į", "imi", "yje"},
                {"ys", "ių", "ims", "is", "imis", "yse"}
            }}
        ),
        // Fem noun class ANTIS
        new NounFlexClass(
            Genus.FEMININE,
            new String [] {
                "akstis", "ankštis", "antis", "apgaubtis", "apgintis", "apibrėžtis",
                "apimtis", "aprėptis", "apsiaustis", "apskritis", "apsuptis",
                "aptartis", "ateitis", "atimtis", "atitiktis", "atmintis",
                "atskirtis", "atspirtis", "atvertis", "baigtis", "bendratis",
                "bjaurastis", "buitis", "būtis", "dabartis", "dėtis", "dingstis",
                "džiūtis", "eigastis", "esatis", "gaišatis", "gaištis",
                "garankštis", "gentis", "gimtis", "gintis", "gyvastis", "graužatis",
                "griūtis", "grūstis", "įdėtis", "įdiegtis", "įeitis", "ietis",
                "įgertis", "iltis", "imtis", "išeitis", "išimtis", "išmintis",
                "išpažintis", "išskirtis", "ištremtis", "išvestis", "įtemptis",
                "įterptis", "įtrauktis", "įvestis", "jaunatis", "jotis", "jungtis",
                "kamšatis", "kartis", "keltis", "kibirkštis", "kiltis", "klėtis",
                "kliautis", "klintis", "kliūtis", "klotis", "kreiptis", "kryptis",
                "krytis", "kumštis", "lemtis", "lygtis", "lytis", "liūtis",
                "maišatis", "makštis", "mintis", "mirtis", "neapibrėžtis",
                "neatitiktis", "nebūtis", "nerimastis", "netektis", "neviltis",
                "nytis", "nutartis", "padėtis", "paleistis", "paltis", "parengtis",
                "parinktis", "pasirinktis", "paskirtis", "paslaptis", "paslėptis",
                "pasmerktis", "paspirtis", "pateiktis", "patirtis", "pažintis",
                "pentis", "pertrauktis", "pilnatis", "pintis", "pirtis", "pjūtis",
                "praeitis", "pragaištis", "prakeiktis", "prapultis", "pražūtis",
                "priežastis", "prigimtis", "prijungtis", "rakštis", "rimtis",
                "rūgštis", "rungtis", "sagtis", "savastis", "senatis", "skiltis",
                "skirtis", "slaptis", "slinktis", "smiltis", "sotis", "sprūstis",
                "spūstis", "sritis", "stotis", "sudėtis", "sueitis", "sugaištis",
                "sugertis", "suimtis", "sukaktis", "sukauptis", "sumaištis",
                "sutaptis", "sutartis", "sutelktis", "svirtis", "šlytis", "tartis",
                "tremtis", "triktis", "trintis", "tūžmastis", "uolektis",
                "užduotis", "užmarštis", "užvertis", "valtis", "vienatis", "viltis",
                "votis", "žebenkštis", "žūtis", "dėtys", "klintys", "rūdys",
                "sultys", "žiotys"
            },
            new String [][][] {{
                {"tis", "ties", "čiai", "tį", "timi", "tyje"},
                {"tys", "čių", "tims", "tis", "timis", "tyse"}
            }}
        )
    };
    public String text;
    public int genus;
    public int numerus;
    public int casus;
    public boolean unflexible;
    private int flexClass;
    private int endingClass;
    private String stem;
    private String ending;
    
    public Noun (String noun) {
        text = noun;
        genus = Genus.DEFAULT;
        numerus = Numerus.DEFAULT;
        casus = Casus.NOMINATIVE;
        flexClass = 0;
        endingClass = 0;

        int maxMatch = 0;
        int runningBestFlexClass = 0;
        int runningBestEndingClass = 0;
        int runningBestGenus = Genus.DEFAULT;
        int runningBestNumerus = Numerus.DEFAULT;
        unflexible = false;

        for (int i = 0; i < NounFlexClasses.length; i++ ) {
            if(NounFlexClasses[i].specialCaseMembers.isEmpty()) {
                for (int j = 0; j < NounFlexClasses[i].endings.length; j++) {
                    for (int k = Numerus.SINGULAR; k <= Numerus.PLURAL; k++) {
                        ending = NounFlexClasses[i].endings[j][k][Casus.NOMINATIVE];
                        if (noun.endsWith(ending)) {
                            if (ending.length() > maxMatch) {
                               maxMatch = ending.length();
                               runningBestFlexClass = i;
                               runningBestGenus = NounFlexClasses[i].gender;
                               runningBestEndingClass = j;
                               runningBestNumerus = k;
                           }
                        }
                    }
                }        
            } else {    
                if (NounFlexClasses[i].specialCaseMembers.contains(noun)) {
                    maxMatch = 0;
                    for (int j = 0; j < NounFlexClasses[i].endings.length; j++) {
                        for (int k = Numerus.SINGULAR; k <= Numerus.PLURAL; k++) {
                            ending = NounFlexClasses[i].endings[j][k][Casus.NOMINATIVE];
                            if (noun.endsWith(ending)) {
                                if (ending.length() > maxMatch) {
                                    maxMatch = ending.length();
                                    runningBestFlexClass = i;
                                    runningBestGenus = NounFlexClasses[i].gender;
                                    runningBestEndingClass = j;
                                    runningBestNumerus = k;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }    
        if (maxMatch > 0) {
            stem = noun.substring(0, noun.length() - maxMatch );
            flexClass = runningBestFlexClass;        
            genus = runningBestGenus;
            endingClass = runningBestEndingClass;
            numerus = runningBestNumerus;
        } else {
            unflexible= true;
        }
    }
    public void setNumerus(int requestedNumerus) {
        if (requestedNumerus > 0 &&
             requestedNumerus < NounFlexClasses[flexClass].endings[endingClass].length) {
            numerus = requestedNumerus;
            text = stem + NounFlexClasses[flexClass].endings[endingClass][numerus][casus];
        }
    }
    public void setCasus(int requestedCasus) {
        if (requestedCasus > 0 &&
             requestedCasus < NounFlexClasses[flexClass].endings[endingClass][numerus].length) {
            casus = requestedCasus;
            text = stem + NounFlexClasses[flexClass].endings[endingClass][numerus][casus];
        }

    }
}