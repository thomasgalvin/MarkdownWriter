If a link has an exclamation point immediately in front of it, it will be treated as an image:

```markdown
![](cover-image.jpg)
```

You can also specify alt text and captions for the image:

```markdown
![Caption](cover-image.jpg "Title" )
```

if you want to suppress the caption, add a slash to the end of the line:

```markdown
![Caption won't be displayed](cover-image.jpg "Title" )
```

You can also use reference links:

```markdown
![cover]

[cover]: cover.jpg
```