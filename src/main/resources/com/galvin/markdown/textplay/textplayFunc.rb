require 'optparse'
require 'fcntl'

def textplayExport( inputFile, outputFile, fdx )
    # ----- PHASE 1 -----------------------------------------------------
    
    text = File.read( inputFile )
    
    title = "A Screenplay"
    font = "Courier Prime"
    
    # ----- PHASE 2 -----------------------------------------------------
    
    # HTML page structure and CSS
    htmlStart = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"
    \"http://www.w3.org/TR/html4/strict.dtd\">
    <html>
    <head>
    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">
    <title>#{title}</title>
    <meta name=\"generator\" content=\"Textplay\">
    <style type=\"text/css\" media=\"all\">
    
      /* ---------- PAGE STYLES ---------- */
    
      /* all page margins are maximums */
      @page {
      size: 8.5in 11in;
      margin-top:1in;
      margin-right:1in;
      margin-bottom:.5in;
      margin-left:1.5in;
      }
      /* This makes the page-counter start on the first page of the screenplay */
      div#screenplay {
      counter-reset: page 1;
      page: Screenplay;
      prince-page-group: start;
      }
      @page Screenplay {
      /* Page Numbers */
      @top-right-corner {
      font: 12pt \"#{font}\", courier;
      content: counter(page)\".\";
      vertical-align: bottom;
      padding-bottom: 1em;
      }
      /* Define Header */
      @top-left {
      content: \"\";
      font: italic 10pt Georgia;
      color: #888;
      vertical-align: bottom;
      padding-bottom: 1.3em;
      }
      /* Define Footer */
      @bottom-left {
      content: \"\";
      font: italic 10pt Georgia;
      color: #888;
      vertical-align:top;
      padding-top:0;
      }
      }
      /* removes the header and page-numbers from the first page */
      @page Screenplay:first {
      @top-right-corner { content: normal; }
      @top-left { content: normal; }
      }
      /* These control where page-breaks can and cannot happen */
      p {
      orphans: 2;
      widows: 2;
      }
      dl {
      page-break-inside:avoid;
      }
      dt, h2, h5 {
      page-break-after: avoid;
      }
      dd.parenthetical {
      orphans: 3;
      widows: 3;
      page-break-before: avoid;
      page-break-after: avoid;
      }
      dd {
      page-break-before:avoid;
      }
      div.page-break {
      page-break-after:always;
      }
    
      /* by default Prince bookmarks all headings, no thanks */
      h3, h4, h5, h6 {
      prince-bookmark-level: none;
      }
    
      /* ---------- COMMON LAYOUT ---------- */
    
      body {
      font-family: \"#{font}\", courier;
      font-size: 12pt;
      line-height: 1;
      }
      #screenplay {
      width: 6in;
      margin:0 auto;
      }
      p.center {
      text-align:center;
      margin-left:0;
      width:100%;
      }
      p {
      margin-top:12pt;
      margin-bottom:12pt;
      margin-left:0;
      width:auto;
      white-space: pre-wrap;
      }
            
      /*Character Names*/
            
      dt {
      font-weight:normal;
      margin-top:1em;
      margin-left:2in;
      width:4in;
      }
            
      /*Parentheticals*/
            
      dd.parenthetical {
      margin-left:1.6in;
      text-indent:-.12in;
      width: 2in;
      }
            
      /*Dialogue*/
            
      dd {
      margin:0;
      margin-left: 1in;
      width: 3.5in;
      line-height: inherit;
      }
            
      /* Sluglines and Transitions */
            
      h1,h2,h3,h4,h5,h6 {
      font-weight: normal;
      font-size: 12pt;
      margin-top: 1em;
      margin-bottom: 1em;
      text-transform:uppercase;
      }
            
      /* Full Sluglines */
            
      h2 {
      width: inherit;
      margin-top: 12pt;
      margin-bottom: 12pt;
      margin-left: 0;
      text-decoration:none;
      font-weight: bold;
      }
            
      /* Right Transitions */
            
      h3 {
      margin-left: 4in;
      width: 2in;
      }
            
      /* Left Transitions */
            
      h4 {
    
      }
            
      /* Goldman Sluglines */
            
      h5 {
      font-weight: bold;
      }
            
      span.underline {
      text-decoration:underline;
      }
      .comment {
      display:none
      }
    </style>
    </head>
    <body>
    
    <div id=\"screenplay\">
    "
    
    # HTML footer
    htmlEnd = '
    </div><!-- end screenplay -->
    </body>
    </html>
    '
    
    # Final Draft's XML header
    fdxStart = '<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
    <FinalDraft DocumentType="Script" Template="No" Version="1">
    <Content>
    '
    # Final Draft's XML footer
    fdxEnd = '</Content>
    </FinalDraft>
    '

    # ----- PHASE 3 -----------------------------------------------------
    
    # Misc Encoding
    text = text.gsub(/^[ \t]*([=-]{3,})[ \t]*$/, '<page-break />')
    text = text.gsub(/&/, '&#38;')
    text = text.gsub(/([^-])--([^-])/, '\1&#8209;&#8209;\2')
    
    # -------- fountain escapes
    # force lines ending with {to: } to be action instead of transition
    text = text.gsub(/^(.+ )TO: +$/, "\n"+'<action>\1TO:</action>')
    
    # escape caps lines => action - when two spaces are at end of line
    text = text.gsub(/^[\ \t]*\n([\ \t]*[^\na-z]+)  $/, "\n"+'<action>\1</action>')
    
    # Boneyard
    text = text.gsub(/\/\*(.|\n)+?\*\//, '<secret />')
    
    # Fountain [[notes]]
    text = text.gsub(/\[\[(.|\n)+\]\]/,'<secret />')
    
    # Boneyards and Notes are removed from the text because it is impossible
    # to prevent additional transformations inside them when newlines are present.
    # And since Fountain is an archival-format, there's no need to maintain notes
    # across conversions (FDX being an exception, but I'm not good enough of a
    # programmer to do that - see the Highland or Slugline apps if you need that).
    
    # Fountain Rules
    text = text.gsub(/^[\ \t]*>[ ]*(.+?)[ ]*<[\ \t]*$/, '<center>\1</center>')
    text = text.gsub(/^[\ \t]*\>[ \t]*(.*)$/,'<transition>\1</transition>')
    text = text.gsub(/^\.(?!\.)[\ \t]*(.*)$/, '<slug>\1</slug>')
    text = text.gsub(/\\\*/, '&#42;')
    
    # Strip-out Fountain Sections and Synopses
    text = text.gsub(/^#+[ \t]+(.*)/,'<note>\1</note>')
    text = text.gsub(/^=[ \t]+(.*)/,'<note>\1</note>')
    # these need not be completely removed simply because they do not span multiple lines
    
    # Textplay/Screenbundle comments
    text = text.gsub(/^[ \t]*\/\/\s?(.*)$/, '<note>\1</note>')
    
    
    # -------- Transitions
    # Left-Transitions
    text = text.gsub(/
      # Require preceding empty line or beginning of document
      (^[\ \t]* \n | \A)
      # 1 or more words, a space
      ^[\ \t]* (  \w+(?:\ \w+)* [\ ]
      # One of these words
      (UP|IN|OUT|BLACK|WITH)  (\ ON)?
      # Ending with transition punctuation
      ([\.\:][\ ]*)    )\n
      # trailing empty line
      ^[\ \t]*$
    /x, "\n"+'<transition>\2</transition>'+"\n")
    
    # Right-Transitions
    text = text.gsub(/
    # Require preceding empty line or beginning of document
      (^[\ \t]* \n | \A)
    # 1 or more words, a space
      ^[\ \t]* (  \w+(?:\ \w+)* [\ ]
    # The word "TO"
      (TO)
    # Ending in a colon
      (\:)$)\n
      # trailing empty line
      ^[\ \t]*$
    /x, "\n"+'<transition>\2</transition>'+"\n")
    
    
    # ------- Dialogue
    # IDENTIFY AND TAG A DIALOGUE-BLOCK
    text = text.gsub(/
    # Require preceding empty line
    ^[\ \t]* \n
    # Character Name
    ^[\ \t]* [^a-z\n\t]+ \n
    # Dialogue
    (^[\ \t]* .+ \n)+
    # Require trailing empty line
    ^[\ \t]*$
    /x, "\n"+'<dialogue>'+'\0'+'</dialogue>'+"\n")
    
    # SEARCH THE DIALOGUE-BLOCK FOR CHARACTERS
    text = text.gsub(/<dialogue>\n(.|\n)+?<\/dialogue>/x){|character|
        character.gsub(/(<dialogue>\n)[ \t]*([^a-z\n]+)(?=\n)/, '\1<character>\2</character>')
    }
    
    # SEARCH THE DIALOGUE-BLOCK FOR PARENTHETICALS
    text = text.gsub(/<dialogue>\n(.|\n)+?<\/dialogue>/x){|paren|
        paren.gsub(/^[ \t]*(\([^\)]+\))[ \t]*(?=\n)/, '<paren>\1</paren>')
    }
    
    # SEARCH THE DIALOGUE-BLOCK FOR DIALOG
    text = text.gsub(/<dialogue>\n(.|\n)+?<\/dialogue>/x){|talk|
        talk.gsub(/^[ \t]*([^<\n]+)$/, '<talk>\1</talk>')
    }
    
    
    # ------- Scene Headings
    
    # FULLY-FORMED SLUGLINES
    text = text.gsub(/
    # Require leading empty line - or the beginning of file
    (?i:^\A | ^[\ \t]* \n)
    # Respect leading whitespace
    ^[\ \t]*
    # Standard prefixes, allowing for bold-italic
    ((?:[\*\_]+)?(i\.?\/e|int\.?\/ext|ext|int|est)
    # A separator between prefix and location
    (\ +|.\ ?).*) \n
    # Require trailing empty line
    ^[\ \t]* \n
    /xi, "\n"+'<sceneheading>\1</sceneheading>'+"\n\n")
    
    # GOLDMAN SLUGLINES
    text = text.gsub(/
    # Require leading empty line
    ^[\ \t]* \n
    # Any line with all-uppercase
    ^[ \t]*(?=\S)([^a-z\<\>\n]+)$
    /x, "\n"+'<slug>\1</slug>')
    
    
    # ------- Misc
    
    # Any untagged paragraph gets tagged as 'action'
    text = text.gsub(/^([^\n\<].*)/, '<action>\1</action>')
    
    # Bold, Italic, Underline
    text = text.gsub(/([ \t\-_:;>])\*{3}([^\*\n]+)\*{3}(?=[ \t\)\]<\-_&;:?!.,])/, '\1<b><i>\2</i></b>')
    text = text.gsub(/([ \t\-_:;>])\*{2}([^\*\n]+)\*{2}(?=[ \t\)\]<\-_&;:?!.,])/, '\1<b>\2</b>')
    text = text.gsub(/([ \t\-_:;>])\*{1}([^\*\n]+)\*{1}(?=[ \t\)\]<\-_&;:?!.,])/, '\1<i>\2</i>')
    text = text.gsub(/([ \t\-\*:;>])\_{1}([^\_\n]+)\_{1}(?=[ \t\)\]<\-\*&;:?!.,])/, '\1<u>\2</u>')
    
    # This cleans up action paragraphs with line-breaks.
    text = text.gsub(/<\/action>[ \t]*(\n)[ \t]*<action>/,'\1')
    
    # Convert tabs to 4 spaces
    text = text.gsub(/<action>(.|\n)+?<\/action>/x){|tabs|
        tabs.gsub(/\t/, '    ')
    }
    
    # This cleans up line-breaks within dialogue blocks
    text = text.gsub(/<\/talk>[ \t]*(\n)[ \t]*<talk>/,'\1')
    
    
    
    
    # ----- PHASE 4 ---------------------------------------------------
    
    # And here we markup the text according to the set options
    
    # Final Draft formatting
    if fdx == true
      text = text.gsub(/<secret \/>/, '')
      text = text.gsub(/<note>/, '<Paragraph><ScriptNote><Text>')
      text = text.gsub(/<\/note>/, '</Text></ScriptNote></Paragraph>')
      text = text.gsub(/<b>/, '<Text Style="Bold">')
      text = text.gsub(/<\/b>/, '</Text>')
      text = text.gsub(/<u>/, '<Text Style="Underline">')
      text = text.gsub(/<\/u>/, '</Text>')
      text = text.gsub(/<i>/, '<Text Style="Italic">')
      text = text.gsub(/<\/i>/, '</Text>')
      text = text.gsub(/<page-break \/>/, '<Paragraph Type="Action" StartsNewPage="Yes"><Text></Text></Paragraph>')
      text = text.gsub(/<transition>/, '<Paragraph Type="Transition"><Text>')
      text = text.gsub(/<\/transition>/, '</Text></Paragraph>')
      text = text.gsub(/<(sceneheading|slug)>/, '<Paragraph Type="Scene Heading"><Text>')
      text = text.gsub(/<\/(sceneheading|slug)>/, '</Text></Paragraph>')
      text = text.gsub(/<center>/, '<Paragraph Type="Action" Alignment="Center"><Text>')
      text = text.gsub(/<\/center>/, '</Text></Paragraph>')
      text = text.gsub(/<\/?dialogue>/,'')
      text = text.gsub(/<character>/, '<Paragraph Type="Character"><Text>')
      text = text.gsub(/<\/character>/, '</Text></Paragraph>')
      text = text.gsub(/<paren>/, '<Paragraph Type="Parenthetical"><Text>')
      text = text.gsub(/<\/paren>/, '</Text></Paragraph>')
      text = text.gsub(/<talk>/, '<Paragraph Type="Dialogue"><Text>')
      text = text.gsub(/<\/talk>/, '</Text></Paragraph>')
      text = text.gsub(/<action>/, '<Paragraph Type="Action"><Text>')
      text = text.gsub(/<\/action>/, '</Text></Paragraph>')
    else
      # default HTML formatting  
      text = text.gsub(/<note>/, '<p class="comment">')
      text = text.gsub(/<\/note>/, '</p>')
      text = text.gsub(/<secret \/>/, '')
      text = text.gsub(/<page-break \/>/, '<div class="page-break"></div>')
      text = text.gsub(/<transition>/, '<h3 class="right-transition">')
      text = text.gsub(/<\/transition>/, '</h3>')
      text = text.gsub(/<sceneheading>/, '<h2 class="full-slugline">')
      text = text.gsub(/<\/sceneheading>/, '</h2>')
      text = text.gsub(/<slug>/, '<h5 class="goldman-slugline">')
      text = text.gsub(/<\/slug>/, '</h5>')
      text = text.gsub(/<center>/, '<p class="center">')
      text = text.gsub(/<\/center>/, '</p>')
      text = text.gsub(/<dialogue>/,'<dl>')
      text = text.gsub(/<\/dialogue>/,'</dl>')
      text = text.gsub(/<character>/, '<dt class="character">')
      text = text.gsub(/<\/character>/, '</dt>')
      text = text.gsub(/<paren>/, '<dd class="parenthetical">')
      text = text.gsub(/<\/paren>/, '</dd>')
      text = text.gsub(/<talk>/, '<dd class="dialogue">')
      text = text.gsub(/<\/talk>/, '</dd>')
      text = text.gsub(/<action>/, '<p class="action">')
      text = text.gsub(/<\/action>/, '</p>')
    end
    
    # ----- PHASE 5 -----------------------------------------------------
    newFile = File.new(outputFile, "w+")
    
    if fdx == true
        newFile.puts fdxStart
		newFile.puts text
		newFile.puts fdxEnd
	else
	    newFile.puts htmlStart
		newFile.puts text
		newFile.puts htmlEnd
	end
end