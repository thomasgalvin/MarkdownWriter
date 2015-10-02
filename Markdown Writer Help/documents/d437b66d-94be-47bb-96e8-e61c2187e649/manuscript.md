You can create ATX Headers by adding `#` signs to the start of a line:

```markdown
# Header One
## Header Two
### Header Three
#### Header Four
##### Header Five
###### Header Six
```

If you want, you can add `#` signs to the end of the lines, too.

```markdown
# Header One #
## Header Two ##
### Header Three ###
#### Header Four ####
##### Header Five #####
###### Header Six ######
```

Pandoc automatically generates identifiers for your headers.

```markdown
# Header One #
```

Becomes:

```html
<h1 id="header-one"><a href="#header-one">Header One</a></h1>
```

Pandoc also allows you to specify what identifier a header will receive:

```markdown
# Header One {#foo}
```

Becomes

```html
<h1 id="foo"><a href="#foo">Header One</a></h1>
```

Pandoc can be configured to automatically number sections. This can be suppressed by adding a lone hyphen to the end of a header:

```markdown
# Header One {-}
```