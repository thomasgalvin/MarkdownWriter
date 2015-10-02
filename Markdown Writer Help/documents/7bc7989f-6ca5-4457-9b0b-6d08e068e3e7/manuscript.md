Markdown allows you to create preformatted (or code) blocks by indenting a paragraph by four spaces (or one tab):

```markdown
    for( int i = 0; i < 10; i++ )
    {
        System.out.println( i );
    }
```

Pandoc also allows fenced code blocks; simple add a line of three or more backticks (\`) or tildes (`~`) above and below the code block:

`````markdown
```
for( int i = 0; i < 10; i++ )
{
    System.out.println( i );
}
```
`````

If you need backticks or tildes inside the fenced block itself, just add a longer line above and below:

``````markdown
`````
```
for( int i = 0; i < 10; i++ )
{
    System.out.println( i );
}
```
`````
``````

Pandoc also supports syntax highlighting:

`````markdown
```java
for( int i = 0; i < 10; i++ )
{
    System.out.println( i );
}
```
`````

Pandoc also supports attributes for fenced code blocks: an ID, the language to highlight, whether or not to number lines, and the line number to start from:

`````markdown
```{#javaExample .java .numberLines startFrom="10"}
for( int i = 0; i < 10; i++ )
{
    System.out.println( i );
}
```
`````
