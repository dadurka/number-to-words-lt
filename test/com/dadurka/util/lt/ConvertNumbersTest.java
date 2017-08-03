/*
 * Copyright (C) 2017 virgis.
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
package com.dadurka.util.lt;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author virgis
 */
public class ConvertNumbersTest {
    
    public ConvertNumbersTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of number2Words method, of class ConvertNumbers.
     */
    @Test
    public void testNumber2Words_long() {
        System.out.println("number2Words(long)");
        long number = 123456L;
        String expResult = "šimtas dvidešimt trys tūkstančiai keturi šimtai penkiasdešimt šeši";
        String result = ConvertNumbers.number2Words(number);
        assertEquals(expResult, result);
    }

    /**
     * Test of number2Words method, of class ConvertNumbers.
     */
    @Test
    public void testNumber2Words_long_String() {
        System.out.println("number2Words(long string)");
        long number = 1234567L;
        String countableNoun = "medis";
        String expResult = "milijonas du šimtai trisdešimt keturi tūkstančiai penki šimtai šešiasdešimt septyni medžiai";
        String result = ConvertNumbers.number2Words(number, countableNoun);
        assertEquals(expResult, result);
    }

    /**
     * Test of number2Words method, of class ConvertNumbers.
     */
    @Test
    public void testNumber2Words_3args_1() {
        System.out.println("number2Words(long, string, string");
        long number = 1234560L;
        String countableNoun = "doleris";
        String optionsCmdLine = "Įn. fin";
        String expResult = "vienu milijonu dviem šimtais trisdešimt keturiais tūkstančiais penkiais šimtais šešiasdešimt dolerių";
        String result = ConvertNumbers.number2Words(number, countableNoun, optionsCmdLine);
        assertEquals(expResult, result);
    }

    /**
     * Test of number2Words method, of class ConvertNumbers.
     */
    @Test
    public void testNumber2Words_3args_2() {
        System.out.println("number2Words(long, string, options)");
        long number = 1958;
        String countableNoun = "metai";
        Options options = new Options();
        String expResult = "tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais";
        String result = ConvertNumbers.number2Words(number, countableNoun, options
                .setNumeralType(NumeralType.ORDINAL_DEFINITE)
                .setCasus(Casus.INSTRUMENTAL)
        );
        assertEquals(expResult, result);
        number = 30;
        countableNoun = "metai";
        options = new Options();
        expResult = "trisdešimčia metų";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.INSTRUMENTAL)
        );
        assertEquals(expResult, result);
        number = 3;
        countableNoun = "kelnės";
        options = new Options();
        expResult = "trejose kelnėse";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.LOCATIVE)
        );
        assertEquals(expResult, result);
        number = 120;
        countableNoun = "sekundė";
        options = new Options();
        expResult = "šimtas dvidešimtoje sekundėje";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setNumeralType(NumeralType.ORDINAL)
                .setCasus(Casus.LOCATIVE)
        );
        assertEquals(expResult, result);
        number = 120;
        countableNoun = "medis";
        options = new Options();
        expResult = "šimtas dvidešimtame medyje";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setNumeralType(NumeralType.ORDINAL)
                .setCasus(Casus.LOCATIVE)
        );
        assertEquals(expResult, result);
        number = 1101;
        countableNoun = "litas";
        options = new Options();
        expResult = "tūkstančio šimto vieno lito";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setNumeralType(NumeralType.CARDINAL)
                .setCasus(Casus.GENITIVE)
        );
        assertEquals(expResult, result);
        number = 1101;
        countableNoun = "litas";
        options = new Options();
        expResult = "vieno tūkstančio vieno šimto vieno lito";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.GENITIVE)
                .setFinancial()
        );
        assertEquals(expResult, result);
        number = 0;
        countableNoun = "litas";
        options = new Options();
        expResult = "nulio litų";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.GENITIVE)
                .setFinancial()
        );
        assertEquals(expResult, result);
        number = -110;
        countableNoun = "litas";
        options = new Options();
        expResult = "minus šimtas dešimt litų";
        result = ConvertNumbers.number2Words(number, countableNoun, options);
        assertEquals(expResult, result);
        number = 111;
        countableNoun = "";
        options = new Options();
        expResult = "šimtas vienuoliktosiomis";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.INSTRUMENTAL)
                .setNumerus(Numerus.PLURAL)
                .setGenus(Genus.FEMININE)
                .setNumeralType(NumeralType.ORDINAL_DEFINITE)
        );
        assertEquals(expResult, result);
        number = 111;
        countableNoun = "metinės";
        options = new Options();
        expResult = "šimtas vienuoliktosiomis metinėmis";
        result = ConvertNumbers.number2Words(number, countableNoun, options
                .setCasus(Casus.INSTRUMENTAL)
                .setNumeralType(NumeralType.ORDINAL_DEFINITE)
        );
        assertEquals(expResult, result);
    }

}
