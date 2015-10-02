Table headers look kind of like level-two Setext headers. 

```markdown
One     Two     Three     Four
---     ---     -----     ----
1       2       3         4
1       2       3         4
1       2       3         4
```

You can control the alignment by fiddling with the underscore. If the content is flush with the right of the underscores it will be right-justified. The same rule applies for flush-left. If the underscores continue to the left and right of the content, it will be centered.

```markdown
Left     Centered       Right
-----   ----------    -------
12         345             67
12         345             67
12         345             67
12         345             67
```

Tables can have captions:

```markdown
Left     Centered       Right
-----   ----------    -------
12         345             67
12         345             67
12         345             67
12         345             67

Table: A well-justifie table.
```

And they can be ended with another line of dashes, if you want:

```markdown
Left     Centered       Right
-----   ----------    -------
12         345             67
12         345             67
12         345             67
12         345             67
-----   ----------    -------
```

This can be used to omit column headers:

```markdown
-----   ----------    -------
12         345             67
12         345             67
12         345             67
12         345             67
-----   ----------    -------
```