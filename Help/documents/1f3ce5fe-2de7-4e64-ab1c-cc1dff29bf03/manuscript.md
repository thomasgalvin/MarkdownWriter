Here's an example of a multi-line table, taken from the [Pandoc User's Guide](http://johnmacfarlane.net/pandoc/README.html#multiline-tables):

```markdown
-------------------------------------------------------------
 Centered   Default           Right Left
  Header    Aligned         Aligned Aligned
----------- ------- --------------- -------------------------
   First    row                12.0 Example of a row that
                                    spans multiple lines.

  Second    row                 5.0 Here's another one. Note
                                    the blank line between
                                    rows.
-------------------------------------------------------------
```

Table: Here's the caption. It, too, may span
multiple lines.

Multi-line tables:

* Begin with a row of dashes
* end with a row of dashes, then a blank line
* Have rows separated by blank lines

Headers are optional:

```markdown
----------- ------- --------------- -------------------------
   First    row                12.0 Example of a row that
                                    spans multiple lines.

  Second    row                 5.0 Here's another one. Note
                                    the blank line between
                                    rows.
----------- ------- --------------- -------------------------

: Here's a multiline table without headers.
```