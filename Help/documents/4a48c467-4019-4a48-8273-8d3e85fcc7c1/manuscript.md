If you don't want to clutter up your document with URLs, you can add links by reference instead. To create a reference link, wrap the link text inside of square brackets (`[` and `]`) and then add *another* set of square brackets with the reference name:

```markdown
This link goes to [Google][GoogleLink]
```

Elsewhere in the document (it doesn't really matter where), define what that reference means:

```markdown
[GoogleLink]: http://www.google.com
```

Reference links can also have tool tips on the second line

```markdown
[GoogleLink]: http://www.google.com
              "A search engine"
```