If there's something you want to do that isn't supported by markdown, chances are you can pull it off by writing HTML. You don't need to do anything special to make this work: just add your tags in like you were writing any other web page.

While Markdown is designed to be compiled into HTML, Pandoc has more wide-reaching goals, so it's variant of markdown provides many non-HTML ways of representing document elements like lists, footnotes, and citations.

Pandoc will interpret markdown commands inside of HTML, except inside of `<script>` and `<style>` tags.