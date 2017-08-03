# Number-to-words converter for Lithuanian

Adjusts optional countable noun in any declension/case.

Cardinal, ordinal and definite ordinal forms supported.

Number can be any size (0 to max long), positive or negative.

Financial relevance respected by option. Casual Lithuanian omits "vienas"(one)
with hundreds, thousands etc., but this may not be acceptable in the financial
context.

The package can help you automatically generate Lithuanian text with numbers
(speech synthesis, counters, orders, invoices etc.).

Pure __Java__. No non-standard imports.

## Synopsis
```
    import com.dadurka.util.lt.ConvertNumbers;
    String answer;
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
```
Output:
```
tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais
vieno tūkstančio devynių šimtų penkiasdešimt aštuonių litų
```
Distributed under LGPL v. 2.1 (or later) licence

Respect to Linas Valiukas (https://wiki.pypt.lt) for valuable critics,
comments and suggestions.

Copyright (C) 2017 Virginijus Dadurkevičius (dadurka @ gmail . com)