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

import java.util.Arrays;
import java.util.List;

/**
 * Lithuanian noun.
 *  
 * @author      Virginijus Dadurkevičius (dadurka @ gmail.com)
 * @version     1.0
 * @since       1.0
 */
public class Noun {
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
    public Genus genus;
    public Numerus numerus;
    public Casus casus;
    public boolean unflexible;
    private int flexClass;
    private int endingClass;
    private String stem;
    private String ending;
    
    public Noun (String noun) {
        text = noun;
        genus = Genus.DEFAULT();
        numerus = Numerus.DEFAULT();
        casus = Casus.NOMINATIVE;
        flexClass = 0;
        endingClass = 0;

        int maxMatch = 0;
        int runningBestFlexClass = 0;
        int runningBestEndingClass = 0;
        Genus runningBestGenus = Genus.DEFAULT();
        Numerus runningBestNumerus = Numerus.DEFAULT();
        unflexible = false;

        for (int i = 0; i < NounFlexClasses.length; i++ ) {
            if(NounFlexClasses[i].specialCaseMembers.isEmpty()) {
                for (int j = 0; j < NounFlexClasses[i].endings.length; j++) {
                    for (int k = 0; k < NounFlexClasses[i].endings[j].length; k++) {
                        ending = NounFlexClasses[i].endings[j][k][Casus.NOMINATIVE.getIndex()];
                        if (noun.endsWith(ending)) {
                            if (ending.length() > maxMatch) {
                               maxMatch = ending.length();
                               runningBestFlexClass = i;
                               runningBestGenus = NounFlexClasses[i].gender;
                               runningBestEndingClass = j;
                               runningBestNumerus = (k == 0) ? Numerus.SINGULAR : Numerus.PLURAL;
                           }
                        }
                    }
                }        
            } else {    
                if (NounFlexClasses[i].specialCaseMembers.contains(noun)) {
                    maxMatch = 0;
                    for (int j = 0; j < NounFlexClasses[i].endings.length; j++) {
                        for (int k = 0; k < NounFlexClasses[i].endings[j].length; k++) {
                            ending = NounFlexClasses[i].endings[j][k][Casus.NOMINATIVE.getIndex()];
                            if (noun.endsWith(ending)) {
                                if (ending.length() > maxMatch) {
                                    maxMatch = ending.length();
                                    runningBestFlexClass = i;
                                    runningBestGenus = NounFlexClasses[i].gender;
                                    runningBestEndingClass = j;
                                    runningBestNumerus = (k == 0) ? Numerus.SINGULAR : Numerus.PLURAL;
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
    public void setNumerus(Numerus requestedNumerus) {
        if (requestedNumerus.getIndex() < NounFlexClasses[flexClass].endings[endingClass].length) {
            numerus = requestedNumerus;
            text = stem + NounFlexClasses[flexClass].endings[endingClass][numerus.getIndex()][casus.getIndex()];
        }
    }
    public void setCasus(Casus requestedCasus) {
        if (requestedCasus.getIndex() < NounFlexClasses[flexClass].endings[endingClass][numerus.getIndex()].length) {
            casus = requestedCasus;
            text = stem + NounFlexClasses[flexClass].endings[endingClass][numerus.getIndex()][casus.getIndex()];
        }

    }
}

class NounFlexClass {
    public String className;
    public Genus gender;
    public String [][][] endings;
    public List<String> specialCaseMembers;
    public NounFlexClass (Genus gender, String [] members, String [][][] endings) {
        this.gender = gender;
        this.endings = endings;
        specialCaseMembers = Arrays.asList(members);
    }
}
