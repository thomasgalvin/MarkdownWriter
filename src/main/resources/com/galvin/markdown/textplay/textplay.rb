#!/usr/bin/ruby

# Textplay -- A plain-text conversion tool for screenwriters
#
# Version 0.5.1 - 2012-11-08
# Copyright (c) 2006 Oliver Taylor
# <http://olivertaylor.net/textplay/>
#
# Textplay was build and tested by Oliver using Ruby 1.8.7
# on Mac OS X 10.7 and Final Draft 8.0. It works for me, but I can't
# promise it won't delete your documents or worse; use at your own risk,
# be careful with your data, backup regularly, etc.

# TEXTPLAY LICENCE
#
# Textplay is free software, available under a BSD-style
# open source license.
#
# Copyright 2006, Oliver Taylor http://olivertaylor.net/ All rights
# reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# * Redistributions of source code must retain the above copyright
# notice, this list of conditions and the following disclaimer.
#
# * Redistributions in binary form must reproduce the above copyright
# notice, this list of conditions and the following disclaimer in the
# documentation and/or other materials provided with the distribution.
#
# * Neither the name "Textplay" nor the names of its
# contributors may be used to endorse or promote products derived from
# this software without specific prior written permission.
#
# This software is provided by the copyright holders and contributors
# "as is" and any express or implied warranties, including, but not
# limited to, the implied warranties of merchantability and fitness for
# a particular purpose are disclaimed. In no event shall the copyright
# owner or contributors be liable for any direct, indirect, incidental,
# special, exemplary, or consequential damages (including, but not
# limited to, procurement of substitute goods or services; loss of use,
# data, or profits; or business interruption) however caused and on any
# theory of liability, whether in contract, strict liability, or tort
# (including negligence or otherwise) arising in any way out of the use
# of this software, even if advised of the possibility of such damage.
#
# ---------------------------------------------------------------------

# WHAT IS TEXTPLAY?

# Textplay is a ruby script that converts Fountain (http://fountain.io)
# formatted files to HTML, FDX, and (if you have Prince XML installed) PDF.

# Q. How does Textplay interperate text differently from the Fountain spec?

# 1. Textplay is smart about what consitutes a transition. Usually, there's
#    no need to escape transitions, but you can. "CUT TO:", "FADE TO BLACK.",
#    "FADE UP:", are all valid transitions.

# 2. An action line that contains no lower-case letters is converted to a
#    slugline. Thus there is no need to escape a line that just contains
#    "BACK IN THE HOUSE", but you can.

# 3. Lines that begin with "//" are interperated as notes. This was done to
#    provide backward-compatibility with Textplay/Screenbundle documents.

# Q. What parts of the Fountain spec are not supported?

# 1. Scene Numbers.
# 2. Dual Dialog blocks.
# 3. Title Pages.
# 4. Empty lines inside [[notes]].

# Q. Why aren't they?
# A. I'm not a good enough coder.

# ------------------------------------------------------------------

# SETTINGS

title = "A Screenplay"
font = "Courier Prime"

# Courier Prime is a modern replacement for Courier.
# More info here: http://quoteunquoteapps.com/courierprime/

# Another great drop-in replacement font for Courier is Pitch.
# More info on pitch can be found here: http://klim.co.nz/retail-fonts/pitch/
# Your layout and page-count will be identical to using Courier
# when using Pitch.

# ------------------------------------------------------------------

# HOW TEXTPLAY WORKS

# This script works on your text in 5 phases:

# Phase 1 allows the user to define conversion options on the command-line.
# Phase 2 defines header and footer values for HTML and FDX conversion.
#         These blocks of text will be wrapped-around the transformed text
#         when the output file is generated.
# Phase 3 converts the input text to an interal xml markup in preperation for
#         further transformation.
# Phase 4 converts the internal xml markup to the markup requested by the user.
# Phase 5 dumps the result to STDOUT or an output file specified by the user.

# ----- PHASE 1 -----------------------------------------------------

require 'optparse'
require 'fcntl'

# Setup the options parser
options = {}
optparse = OptionParser.new do|opts|

	# This is the help banner, which just explains the command syntax
	opts.banner = "
Usage: textplay [options] [input-path] [output-path]

* The first argument is always an input-path.
* The second argument is always an output-path.
* If there's no input-path textplay reads from STDIN.
* If there's no output-path textplay prints to STDOUT, PDFs go to /temp.

Options:
"

	# The default conversion type is HTML, if the '-f' option is set, convert to FDX
	options[:fdx] = false
	opts.on( '-f', '--fdx', "Convert to Final Draft .fdx" ) do
		options[:fdx] = true
	end

	# if the '-p' option is set pass the html output to PrinceXML
	options[:pdf] = false
	opts.on( '-p', '--pdf', "Convert to PDF - REQUIRES PrinceXML" ) do
		options[:pdf] = true
	end

	# if the '-x' option is set use the internal xml format for export
	options[:xml] = false
	opts.on( '-x', '--xml', "Output as raw XML - for debugging" ) do
		options[:xml] = true
	end

end

# Parse the options and remove them from ARGV
optparse.parse!



# ---------- INPUT / OUTPUT ---------#

# Some of this is redundant but I prefer specifying every contingency in case I need to control each step

# if there's a 2nd argument (output path)
if ARGV[1]

  # read the first argument as an input file
  text = File.read(ARGV[0])

# if there's only 1 argument (an input path)
elsif ARGV[0]

  # read the first argument as an input file
  text = File.read(ARGV[0])

# if no input and no output
else

  # check to see if anything is in SDTIN
  if STDIN.fcntl(Fcntl::F_GETFL, 0) == 0
    # if so, read it
    text = STDIN.read 
  else
    # if nothing is in STDIN print the options banner
    puts optparse
    exit(-1)
  end

end



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
if options[:fdx] == true
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
elsif options[:xml] == true
  text = text
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

# If an output file is specified then dump preformatted and converted
# text to that file.
if ARGV[1]
	# create a new file at the path suppied by the user
	newFile = File.new(ARGV[1], "w+")
	# if FDX, then do this
	if options[:fdx] == true
		newFile.puts fdxStart
		newFile.puts text
		newFile.puts fdxEnd
	# if PDF, then do this
	elsif options[:pdf] == true
		# create a new temp file for processing
		File.open('/tmp/textplay.html', "w+") do |htmlFile|
		# place the transformed text in the new file
		htmlFile.puts htmlStart
		htmlFile.puts text
		htmlFile.puts htmlEnd
		  end
		# run prince
		system "prince /tmp/textplay.html #{ARGV[1]}"
	elsif options[:xml] == true
    # output xml if requested
		newFile.puts text
	else
    # otherwise fallback to HTML
		newFile.puts htmlStart
		newFile.puts text
		newFile.puts htmlEnd
	end
else
# If no output file is specified then pass the output to STDOUT.
	if options[:fdx] == true
		puts fdxStart
		puts text
		puts fdxEnd
	# in the case of a PDF, create a file in the /tmp dir and open it directly from there
	elsif options[:pdf] == true
		# create a new temp file for processing
		File.open('/tmp/textplay.html', "w+") do |htmlFile|
		# place the transformed text in the new file
		htmlFile.puts htmlStart
		htmlFile.puts text
		htmlFile.puts htmlEnd
			end
		# run prince on that new file
		system "prince /tmp/textplay.html; open /tmp/textplay.pdf"
	elsif options[:xml] == true
    # output xml if requested
		puts text
	else
    # otherwise fallback to HTML
		puts htmlStart
		puts text
		puts htmlEnd
	end
end
