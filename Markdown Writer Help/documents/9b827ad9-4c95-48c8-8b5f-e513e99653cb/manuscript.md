Pandoc allows you to specify three pieces of document metadata: the title, a list of authors, and the date. To do this, start a document with three lines beginning with modulus (`%`) signs:

```markdown
% Title
% Author One; Author Two
% March 2013
```

All of these are optional.

```markdown
% Title
% 
% March 2013
```

Markdown Writer will generate this for you, based on the metadata contained in the project itself.