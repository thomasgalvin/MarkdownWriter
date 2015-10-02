To add a footnote, use the following syntax:

```markdown
This needs to be clarified in a footnote[^f1], and so does this[^f2].

[^f1]: The first footnote

[^f2]: The second footnote.
```

You can use the same kind of block-level indenting in footnotes as you do in lists.

Pandoc also allows you to specify footnotes inline:

```markdown
This needs to be clarified in a footnote^[And here that footnote is].
```