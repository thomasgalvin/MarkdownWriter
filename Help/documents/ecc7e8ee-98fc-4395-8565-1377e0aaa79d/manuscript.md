If you want to put indented code immediately after a list, or if you want two consecutive lists to follow each other, you need to insert a non-indented HTML comment between them. This breaks up the indenting, telling the markdown processor what you mean, but doesn't produce any visible content when exported.

*     One
*     Two
*     Three

<!-- end of list -->

    for( int i = 0; i < 10; i++ )

* One
* Two
* Three

<!-- end of list -->

* One
* Two
* Three