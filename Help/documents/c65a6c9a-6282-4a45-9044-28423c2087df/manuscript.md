Ordered lists behave like unordered lists; they just begin with enumerators instead of bullets.

```markdown
1.  Acknowledgments
2.  Introduction
3.  Chapter One
    1. What is markdown?
    2. Why use markdown?
    3. What is Pandoc?
    4. Why use Pandoc?
4.  Chapter Two
```

Pandoc expands this functionality to allow upper- and lower- case letters and Roman numerals. These enumerators and be enclosed inside of parentheses or followed by a single  right-parentheses or period. They should be separated from the content of the item by two spaces.

```markdown
(1)  Acknowledgments
(2)  Introduction
(3)  Chapter One
    (A)  What is markdown?
         (i)  John Gruber
         (ii)   Daring Fireball
         (iii)  Based on email syntax
    (B)  Why use markdown?
    (C)  What is Pandoc?
    (D)  Why use Pandoc?
(4)  Chapter Two
```

Pandoc tries to honor the number you start with, too:

```markdown
(9)  Nine
(10)  Ten
      (iv)  Four
      (v)  Five
      (vi) Six
(11)  Eleven
```