# Number-to-words converter for Lithuanian

Cardinal, ordinal and definite ordinal forms supported.

Adjusts optional countable noun in any declension/case.

Pure __Java__. No non-standard imports (only `java.util.Arrays` and `java.util.List`)

## Synopsis
```
              String answer;
              answer=number2Word(1958, "metai", "Įn. įvardž.");
              System.out.println(answer);
```
Output:
```
tūkstantis devyni šimtai penkiasdešimt aštuntaisiais metais
```
