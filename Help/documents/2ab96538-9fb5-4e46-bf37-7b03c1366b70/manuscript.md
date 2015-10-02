Pandoc supports the following citation database formats:

* MODS
* BibLaTeX
* BibTeX
* RIS
* EndNote
* EndNote XML
* MEDLINE
* Copac
* JSON citeproc

Citations take the following format, all taken from the [Pandoc User's Guide](http://johnmacfarlane.net/pandoc/README.html#citations)

```markdown
Blah blah [see @doe99, pp. 33-35; also @smith04, ch. 1].

Blah blah [@doe99, pp. 33-35, 38-39 and *passim*].

Blah blah [@smith04; @doe99].
```
You can suppress the author (if you mention the name is the text, for example):

```markdown
Smith says blah [-@smith04].
```

And you do citations in-line, as well:

```markdown
@smith04 says blah.

@smith04 [p. 33] says blah.
```