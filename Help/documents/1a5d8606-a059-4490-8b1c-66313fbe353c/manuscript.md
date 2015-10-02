Grid tables use `=` to separate the headers from the body, and do not support alignment. Here's the example from the [Pandoc User's Guide](http://johnmacfarlane.net/pandoc/README.html#grid-tables):

```markdown
: Sample grid table.

+---------------+---------------+--------------------+
| Fruit         | Price         | Advantages         |
+===============+===============+====================+
| Bananas       | $1.34         | - built-in wrapper |
|               |               | - bright color     |
+---------------+---------------+--------------------+
| Oranges       | $2.10         | - cures scurvy     |
|               |               | - tasty            |
+---------------+---------------+--------------------+
```