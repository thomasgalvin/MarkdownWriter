Pandoc adds the concept of line blocks to markdown. Line blocks preserve line breaks and leading spaces, but do *not* format the text as code / monosapce. This is useful for poetry and addresses.

To use a line block, start each line with a vertical bar (`|`) and a space:

```markdown
| The limerick packs laughs anatomical
| In space that is quite economical.
|    But the good ones I've seen
|    So seldom are clean
| And the clean ones so seldom are comical
```

```markdown
| 200 Main St.
| Berkeley, CA 94718
```

Both of these examples are from the [Pandoc User's Guide](http://johnmacfarlane.net/pandoc/README.html#line-blocks).