# Number-to-words converter for Lithuanian

Cardinal, ordinal and definite ordinal forms supported.

Adjusts optional countable noun in any declension/case.

Pure __Java__. No non-standard imports (only `java.util.Arrays` and `java.util.List`)

## Synopsis
```
            import com.dadurka.util.lt.convert.Numbers;

            String answer;
            answer=Numbers.number2Words(1958, "metai", "Įn. įvardž.");
            System.out.println(answer);
            answer=Numbers.number2Words(1134561, "litas", "G.");
            System.out.println(answer);
            answer=Numbers.number2Words(1134561, "litas", "G. finansinis");
            System.out.println(answer);
```
Output:
```
tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais
milijoną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą
vieną milijoną vieną šimtą trisdešimt keturis tūkstančius penkis šimtus šešiasdešimt vieną litą
tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais
```
Distributer under LGPL v. 2.1 (or later) licence

Copyright (C) 2017 Virginijus Dadurkevičius (dadurka@gmail.com)