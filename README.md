# Number-to-words converter for Lithuanian

Cardinal, ordinal and definite ordinal forms supported.

Adjusts optional countable noun in any declension/case.

Pure __Java__. No non-standard imports (only `java.util.Arrays` and `java.util.List`)

## Synopsis
```
            import com.dadurka.util.lt.ConvertNumbers;

            String answer;
            answer=ConvertNumbers.number2Words(1958, "metai", new Options().setNumeralType(NumeralType.ORDINAL_DEFINITE).setCasus(Casus.INSTRUMENTAL));
            System.out.println(answer);
```
Output:
```
tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais
```
Distributed under LGPL v. 2.1 (or later) licence

Copyright (C) 2017 Virginijus Dadurkevičius (dadurka@gmail.com)