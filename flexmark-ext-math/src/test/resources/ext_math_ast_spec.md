---
title: Math Formula Extension Spec
author: Dietrich Travkin
version:
date: '2024-05-21'
license: '[CC-BY-SA 4.0](http://creativecommons.org/licenses/by-sa/4.0/)'
...

---

# Math Extension

flexmark-java extension for math formula support in Markdown code.

---

## In-line Formula

Single formula, in-line

```````````````````````````````` example In-line Formula: 1
$E=mc^2$
.
<p>
  <span class="math inline">\(E=mc^2\)</span>
</p>
.
Document[0, 8]
  Paragraph[0, 8]
    MathFormulaInLineNode[0, 8] textOpen:[0, 1, "$"] text:[1, 7, "E=mc^2"] textClose:[7, 8, "$"]
      Text[1, 7] chars:[1, 7, "E=mc^2"]
````````````````````````````````


Single formula, in-line

```````````````````````````````` example In-line Formula: 2
$n! = \prod_{i=1..n}i$
.
<p>
  <span class="math inline">\(n! = \prod_{i=1..n}i\)</span>
</p>
.
Document[0, 22]
  Paragraph[0, 22]
    MathFormulaInLineNode[0, 22] textOpen:[0, 1, "$"] text:[1, 21, "n! = \prod_{i=1..n}i"] textClose:[21, 22, "$"]
      Text[1, 21] chars:[1, 21, "n! =  … ..n}i"]
````````````````````````````````


Formula in paragraph text, in-line

```````````````````````````````` example In-line Formula: 3
Einstein's formula $E=mc^2$ is famous.
.
<p>Einstein's formula 
  <span class="math inline">\(E=mc^2\)</span>
is famous.</p>
.
Document[0, 38]
  Paragraph[0, 38]
    Text[0, 19] chars:[0, 19, "Einst … mula "]
    MathFormulaInLineNode[19, 27] textOpen:[19, 20, "$"] text:[20, 26, "E=mc^2"] textClose:[26, 27, "$"]
      Text[20, 26] chars:[20, 26, "E=mc^2"]
    Text[27, 38] chars:[27, 38, " is f … mous."]
````````````````````````````````


Formula in paragraph text, in-line

```````````````````````````````` example In-line Formula: 4
$H_2O$ and $CO_2$ are chemical molecules.
.
<p>
  <span class="math inline">\(H_2O\)</span>
and 
  <span class="math inline">\(CO_2\)</span>
are chemical molecules.</p>
.
Document[0, 41]
  Paragraph[0, 41]
    MathFormulaInLineNode[0, 6] textOpen:[0, 1, "$"] text:[1, 5, "H_2O"] textClose:[5, 6, "$"]
      Text[1, 5] chars:[1, 5, "H_2O"]
    Text[6, 11] chars:[6, 11, " and "]
    MathFormulaInLineNode[11, 17] textOpen:[11, 12, "$"] text:[12, 16, "CO_2"] textClose:[16, 17, "$"]
      Text[12, 16] chars:[12, 16, "CO_2"]
    Text[17, 41] chars:[17, 41, " are  … ules."]
````````````````````````````````


Formula directly followed by a full stop, in-line

```````````````````````````````` example In-line Formula: 5
computed in $O(n)$.
.
<p>computed in 
  <span class="math inline">\(O(n)\)</span>
.</p>
.
Document[0, 19]
  Paragraph[0, 19]
    Text[0, 12] chars:[0, 12, "compu … d in "]
    MathFormulaInLineNode[12, 18] textOpen:[12, 13, "$"] text:[13, 17, "O(n)"] textClose:[17, 18, "$"]
      Text[13, 17] chars:[13, 17, "O(n)"]
    Text[18, 19] chars:[18, 19, "."]
````````````````````````````````


Formula directly followed by a comma, in-line

```````````````````````````````` example In-line Formula: 6
the modulus $2^{128}$, and nothing else.
.
<p>the modulus 
  <span class="math inline">\(2^{128}\)</span>
, and nothing else.</p>
.
Document[0, 40]
  Paragraph[0, 40]
    Text[0, 12] chars:[0, 12, "the m … ulus "]
    MathFormulaInLineNode[12, 21] textOpen:[12, 13, "$"] text:[13, 20, "2^{128}"] textClose:[20, 21, "$"]
      Text[13, 20] chars:[13, 20, "2^{128}"]
    Text[21, 40] chars:[21, 40, ", and … else."]
````````````````````````````````


Formulas enclosed in parentheses, in-line

```````````````````````````````` example In-line Formula: 7
one bit ($2^{127}$ vs $2^{128}$) is lost.
.
<p>one bit (
  <span class="math inline">\(2^{127}\)</span>
vs 
  <span class="math inline">\(2^{128}\)</span>
) is lost.</p>
.
Document[0, 41]
  Paragraph[0, 41]
    Text[0, 9] chars:[0, 9, "one bit ("]
    MathFormulaInLineNode[9, 18] textOpen:[9, 10, "$"] text:[10, 17, "2^{127}"] textClose:[17, 18, "$"]
      Text[10, 17] chars:[10, 17, "2^{127}"]
    Text[18, 22] chars:[18, 22, " vs "]
    MathFormulaInLineNode[22, 31] textOpen:[22, 23, "$"] text:[23, 30, "2^{128}"] textClose:[30, 31, "$"]
      Text[23, 30] chars:[23, 30, "2^{128}"]
    Text[31, 41] chars:[31, 41, ") is lost."]
````````````````````````````````


Two formulas in one line where the second one is followed by punctuation, in-line

```````````````````````````````` example In-line Formula: 8
order $2^{126}$ mod $2^{128}$.
.
<p>order 
  <span class="math inline">\(2^{126}\)</span>
mod 
  <span class="math inline">\(2^{128}\)</span>
.</p>
.
Document[0, 30]
  Paragraph[0, 30]
    Text[0, 6] chars:[0, 6, "order "]
    MathFormulaInLineNode[6, 15] textOpen:[6, 7, "$"] text:[7, 14, "2^{126}"] textClose:[14, 15, "$"]
      Text[7, 14] chars:[7, 14, "2^{126}"]
    Text[15, 20] chars:[15, 20, " mod "]
    MathFormulaInLineNode[20, 29] textOpen:[20, 21, "$"] text:[21, 28, "2^{128}"] textClose:[28, 29, "$"]
      Text[21, 28] chars:[21, 28, "2^{128}"]
    Text[29, 30] chars:[29, 30, "."]
````````````````````````````````


Dollar signs used as currency symbols must not be parsed as formula delimiters

```````````````````````````````` example In-line Formula: 9
It costs $5 and not $10 at all.
.
<p>It costs $5 and not $10 at all.</p>
.
Document[0, 31]
  Paragraph[0, 31]
    Text[0, 31] chars:[0, 31, "It co …  all."]
````````````````````````````````


````````````````Formulas directly followed by a colon, in-line

```````````````````````````````` example In-line Formula: 10
given $m$ segments: each of length $l_j$: nothing else.
.
<p>given 
  <span class="math inline">\(m\)</span>
segments: each of length 
  <span class="math inline">\(l_j\)</span>
: nothing else.</p>
.
Document[0, 55]
  Paragraph[0, 55]
    Text[0, 6] chars:[0, 6, "given "]
    MathFormulaInLineNode[6, 9] textOpen:[6, 7, "$"] text:[7, 8, "m"] textClose:[8, 9, "$"]
      Text[7, 8] chars:[7, 8, "m"]
    Text[9, 35] chars:[9, 35, " segm … ngth "]
    MathFormulaInLineNode[35, 40] textOpen:[35, 36, "$"] text:[36, 39, "l_j"] textClose:[39, 40, "$"]
      Text[36, 39] chars:[36, 39, "l_j"]
    Text[40, 55] chars:[40, 55, ": not … else."]
````````````````````````````````

````````````````Formula directly followed by a question mark, in-line

```````````````````````````````` example In-line Formula: 11
is the order really $2^{126}$? yes, it is.
.
<p>is the order really 
  <span class="math inline">\(2^{126}\)</span>
? yes, it is.</p>
.
Document[0, 42]
  Paragraph[0, 42]
    Text[0, 20] chars:[0, 20, "is th … ally "]
    MathFormulaInLineNode[20, 29] textOpen:[20, 21, "$"] text:[21, 28, "2^{126}"] textClose:[28, 29, "$"]
      Text[21, 28] chars:[21, 28, "2^{126}"]
    Text[29, 42] chars:[29, 42, "? yes … t is."]
````````````````````````````````

````````````````Formula directly followed by an exclamation mark, in-line

```````````````````````````````` example In-line Formula: 12
the base must be odd $b$! always.
.
<p>the base must be odd 
  <span class="math inline">\(b\)</span>
! always.</p>
.
Document[0, 33]
  Paragraph[0, 33]
    Text[0, 21] chars:[0, 21, "the b …  odd "]
    MathFormulaInLineNode[21, 24] textOpen:[21, 22, "$"] text:[22, 23, "b"] textClose:[23, 24, "$"]
      Text[22, 23] chars:[22, 23, "b"]
    Text[24, 33] chars:[24, 33, "! always."]
````````````````````````````````

````````````````Formulas directly preceded by a hyphen, in-line

```````````````````````````````` example In-line Formula: 13
a degree-$d$ polynomial of length-$n$ strings.
.
<p>a degree-
  <span class="math inline">\(d\)</span>
polynomial of length-
  <span class="math inline">\(n\)</span>
strings.</p>
.
Document[0, 46]
  Paragraph[0, 46]
    Text[0, 9] chars:[0, 9, "a degree-"]
    MathFormulaInLineNode[9, 12] textOpen:[9, 10, "$"] text:[10, 11, "d"] textClose:[11, 12, "$"]
      Text[10, 11] chars:[10, 11, "d"]
    Text[12, 34] chars:[12, 34, " poly … ngth-"]
    MathFormulaInLineNode[34, 37] textOpen:[34, 35, "$"] text:[35, 36, "n"] textClose:[36, 37, "$"]
      Text[35, 36] chars:[35, 36, "n"]
    Text[37, 46] chars:[37, 46, " strings."]
````````````````````````````````

````````````````Formulas enclosed in braces and quotes, in-line

```````````````````````````````` example In-line Formula: 14
braces {$y$} and quotes "$z$" also work.
.
<p>braces {
  <span class="math inline">\(y\)</span>
} and quotes &quot;
  <span class="math inline">\(z\)</span>
&quot; also work.</p>
.
Document[0, 40]
  Paragraph[0, 40]
    Text[0, 8] chars:[0, 8, "braces {"]
    MathFormulaInLineNode[8, 11] textOpen:[8, 9, "$"] text:[9, 10, "y"] textClose:[10, 11, "$"]
      Text[9, 10] chars:[9, 10, "y"]
    Text[11, 25] chars:[11, 25, "} and … tes \""]
    MathFormulaInLineNode[25, 28] textOpen:[25, 26, "$"] text:[26, 27, "z"] textClose:[27, 28, "$"]
      Text[26, 27] chars:[26, 27, "z"]
    Text[28, 40] chars:[28, 40, "\" als … work."]
````````````````````````````````

````````````````Formulas directly followed by semicolon, slash and hyphen, in-line

```````````````````````````````` example In-line Formula: 15
semicolon $a$; slash $b$/ and dash $c$-end.
.
<p>semicolon 
  <span class="math inline">\(a\)</span>
; slash 
  <span class="math inline">\(b\)</span>
/ and dash 
  <span class="math inline">\(c\)</span>
-end.</p>
.
Document[0, 43]
  Paragraph[0, 43]
    Text[0, 10] chars:[0, 10, "semicolon "]
    MathFormulaInLineNode[10, 13] textOpen:[10, 11, "$"] text:[11, 12, "a"] textClose:[12, 13, "$"]
      Text[11, 12] chars:[11, 12, "a"]
    Text[13, 21] chars:[13, 21, "; slash "]
    MathFormulaInLineNode[21, 24] textOpen:[21, 22, "$"] text:[22, 23, "b"] textClose:[23, 24, "$"]
      Text[22, 23] chars:[22, 23, "b"]
    Text[24, 35] chars:[24, 35, "/ and … dash "]
    MathFormulaInLineNode[35, 38] textOpen:[35, 36, "$"] text:[36, 37, "c"] textClose:[37, 38, "$"]
      Text[36, 37] chars:[36, 37, "c"]
    Text[38, 43] chars:[38, 43, "-end."]
````````````````````````````````

````````````````Dollar signs used as currency symbols next to punctuation must not be parsed as formula delimiters

```````````````````````````````` example In-line Formula: 16
It costs $5, not $10! Really $20?
.
<p>It costs $5, not $10! Really $20?</p>
.
Document[0, 33]
  Paragraph[0, 33]
    Text[0, 33] chars:[0, 33, "It co …  $20?"]
````````````````````````````````


A single dollar sign in text must be rendered as is

```````````````````````````````` example In-line Formula: 17
text with $ in it
.
<p>text with $ in it</p>
.
Document[0, 17]
  Paragraph[0, 17]
    Text[0, 17] chars:[0, 17, "text  … in it"]
````````````````````````````````

An environment variable in text must be rendered as is

```````````````````````````````` example In-line Formula: 18
an environment variable $WORKSPACE in text
.
<p>an environment variable $WORKSPACE in text</p>
.
Document[0, 42]
  Paragraph[0, 42]
    Text[0, 42] chars:[0, 42, "an en …  text"]
````````````````````````````````

Several environment variables in text must be rendered as is

```````````````````````````````` example In-line Formula: 19
use $HOME and $WORKSPACE, then $PATH.
.
<p>use $HOME and $WORKSPACE, then $PATH.</p>
.
Document[0, 37]
  Paragraph[0, 37]
    Text[0, 37] chars:[0, 37, "use $ … PATH."]
````````````````````````````````

A single dollar sign must not prevent a later formula from being parsed

```````````````````````````````` example In-line Formula: 20
the price is 5 $ and the formula $x^2$ still works.
.
<p>the price is 5 $ and the formula 
  <span class="math inline">\(x^2\)</span>
still works.</p>
.
Document[0, 51]
  Paragraph[0, 51]
    Text[0, 33] chars:[0, 33, "the p … mula "]
    MathFormulaInLineNode[33, 38] textOpen:[33, 34, "$"] text:[34, 37, "x^2"] textClose:[37, 38, "$"]
      Text[34, 37] chars:[34, 37, "x^2"]
    Text[38, 51] chars:[38, 51, " stil … orks."]
````````````````````````````````

An unterminated formula must be rendered as is

```````````````````````````````` example In-line Formula: 21
an incomplete formula $x^2 in text
.
<p>an incomplete formula $x^2 in text</p>
.
Document[0, 34]
  Paragraph[0, 34]
    Text[0, 34] chars:[0, 34, "an in …  text"]
````````````````````````````````


---

## Display Mode Formula

Single display mode formula

```````````````````````````````` example Display Mode Formula: 1
$$\sum_{i=1}^{n}=\frac{n(n+1)}{2}$$
.
<p>
  <span class="math display">\[\sum_{i=1}^{n}=\frac{n(n+1)}{2}\]</span>
</p>
.
Document[0, 35]
  MathFormulaDisplayModeNode[0, 35]
    Text[2, 33] chars:[2, 33, "\sum_ … )}{2}"]
````````````````````````````````


Paragraph and multiple display mode formula

```````````````````````````````` example Display Mode Formula: 2
Other expressions:
$$a_0+{1\over a_1+
      {1\over a_2+
        {1 \over a_3 + 
           {1 \over a_4}}}}$$

$$x_{1/2} = -\frac{p}{2}\pm \sqrt{\frac{p^2}{4}-q}$$
.
<p>Other expressions:</p>
<p>
  <span class="math display">\[a_0+{1\over a_1+
  {1\over a_2+
  {1 \over a_3 + 
  {1 \over a_4}}}}\]</span>
</p>
<p>
  <span class="math display">\[x_{1/2} = -\frac{p}{2}\pm \sqrt{\frac{p^2}{4}-q}\]</span>
</p>
.
Document[0, 164]
  Paragraph[0, 19]
    Text[0, 18] chars:[0, 18, "Other … ions:"]
  MathFormulaDisplayModeNode[19, 111]
    Text[21, 108] chars:[21, 108, "a_0+{ … 4}}}}"]
  MathFormulaDisplayModeNode[112, 164]
    Text[114, 162] chars:[114, 162, "x_{1/ … 4}-q}"]
````````````````````````````````


---

## Mixed

Multiple formulas in in-line and in display mode

```````````````````````````````` example Mixed: 1
## Mathematical expressions

$H_2O$ and $CO_2$ are chemical molecules.

$n! = \prod_{i=1..n}i$

Einstein's formula $E=mc^2$ is famous.

Formula in display mode (with LaTeX commands):

$$\sum_{i=1}^{n}=\frac{n(n+1)}{2}$$


Other expressions:
$$a_0+{1\over a_1+
      {1\over a_2+
        {1 \over a_3 + 
           {1 \over a_4}}}}$$

$$x_{1/2} = -\frac{p}{2}\pm \sqrt{\frac{p^2}{4}-q}$$


## PlantUML diagrams
.
<h2>Mathematical expressions</h2>
<p>
  <span class="math inline">\(H_2O\)</span>
and 
  <span class="math inline">\(CO_2\)</span>
are chemical molecules.</p>
<p>
  <span class="math inline">\(n! = \prod_{i=1..n}i\)</span>
</p>
<p>Einstein's formula 
  <span class="math inline">\(E=mc^2\)</span>
is famous.</p>
<p>Formula in display mode (with LaTeX commands):</p>
<p>
  <span class="math display">\[\sum_{i=1}^{n}=\frac{n(n+1)}{2}\]</span>
</p>
<p>Other expressions:</p>
<p>
  <span class="math display">\[a_0+{1\over a_1+
  {1\over a_2+
  {1 \over a_3 + 
  {1 \over a_4}}}}\]</span>
</p>
<p>
  <span class="math display">\[x_{1/2} = -\frac{p}{2}\pm \sqrt{\frac{p^2}{4}-q}\]</span>
</p>
<h2>PlantUML diagrams</h2>
.
Document[0, 409]
  Heading[0, 27] textOpen:[0, 2, "##"] text:[3, 27, "Mathematical expressions"]
    Text[3, 27] chars:[3, 27, "Mathe … sions"]
  Paragraph[29, 71] isTrailingBlankLine
    MathFormulaInLineNode[29, 35] textOpen:[29, 30, "$"] text:[30, 34, "H_2O"] textClose:[34, 35, "$"]
      Text[30, 34] chars:[30, 34, "H_2O"]
    Text[35, 40] chars:[35, 40, " and "]
    MathFormulaInLineNode[40, 46] textOpen:[40, 41, "$"] text:[41, 45, "CO_2"] textClose:[45, 46, "$"]
      Text[41, 45] chars:[41, 45, "CO_2"]
    Text[46, 70] chars:[46, 70, " are  … ules."]
  Paragraph[72, 95] isTrailingBlankLine
    MathFormulaInLineNode[72, 94] textOpen:[72, 73, "$"] text:[73, 93, "n! = \prod_{i=1..n}i"] textClose:[93, 94, "$"]
      Text[73, 93] chars:[73, 93, "n! =  … ..n}i"]
  Paragraph[96, 135] isTrailingBlankLine
    Text[96, 115] chars:[96, 115, "Einst … mula "]
    MathFormulaInLineNode[115, 123] textOpen:[115, 116, "$"] text:[116, 122, "E=mc^2"] textClose:[122, 123, "$"]
      Text[116, 122] chars:[116, 122, "E=mc^2"]
    Text[123, 134] chars:[123, 134, " is f … mous."]
  Paragraph[136, 183] isTrailingBlankLine
    Text[136, 182] chars:[136, 182, "Formu … nds):"]
  MathFormulaDisplayModeNode[184, 220]
    Text[186, 217] chars:[186, 217, "\sum_ … )}{2}"]
  Paragraph[222, 241]
    Text[222, 240] chars:[222, 240, "Other … ions:"]
  MathFormulaDisplayModeNode[241, 333]
    Text[243, 330] chars:[243, 330, "a_0+{ … 4}}}}"]
  MathFormulaDisplayModeNode[334, 387]
    Text[336, 384] chars:[336, 384, "x_{1/ … 4}-q}"]
  Heading[389, 409] textOpen:[389, 391, "##"] text:[392, 409, "PlantUML diagrams"]
    Text[392, 409] chars:[392, 409, "Plant … grams"]
````````````````````````````````


