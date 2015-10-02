Pandoc can also continue numbering across multiple lists automatically. To do this, start a list with an at sign (`@`). The first item will be numbered one, and subsequent lists will pick up wherever the last list left off.

```markdown
(@)  One
(@)  Two
(@)  Three

Lorem ipsum dol amet sitor, consectetuer adipiscing elit. Donec dui. Integer 
tortor. Praesent adipiscing nibh sit amet lacus. Ut ut lorem et mi dignissim 
condimentum. Maecenas ac lectus quis pede dictum tempor. Proin convallis pede 
non lacus. Etiam nonummy arcu sit amet justo. Nulla et magna a justo mollis 
venenatis. Donec eros. Praesent luctus urna sed mauris. Aliquam ultrices. Donec 
imperdiet, mi eu consequat sollicitudin.

Lorem ipsum dol amet sitor, consectetuer adipiscing elit. Donec dui. Integer 
tortor. Praesent adipiscing nibh sit amet lacus. Ut ut lorem et mi dignissim 
condimentum. Maecenas ac lectus quis pede dictum tempor. Proin convallis pede 
non lacus. Etiam nonummy arcu sit amet justo. Nulla et magna a justo mollis 
venenatis. Donec eros. Praesent luctus urna sed mauris. Aliquam ultrices. Donec 
imperdiet, mi eu consequat sollicitudin.

(@)  Four
(@)  Five
(@)  Six
```