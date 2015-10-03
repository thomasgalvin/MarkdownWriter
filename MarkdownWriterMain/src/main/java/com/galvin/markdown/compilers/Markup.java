package com.galvin.markdown.compilers;

public class Markup
{
    public static final String FORMAT_COMPLETE = "format: complete";
    
    public static final String LINE_BREAK = "  \n";
    public static final String PARAGRAPH_BREAK = "\n\n";
    public static final String PAGE_BREAK = "\n\n<div style='page-break-after: always' ></div>\n\n";
    public static final String SMALL_CENTERED_HR = "\n\n<hr style=\"text-align: center; width:25%; overflow:hidden;\" />\n\n";
    public static final String HR = "\n\n-----\n\n";
    
    public static final String SPACE = " ";
    
    public static final String HEADER = "#";
    public static final String H1 = "# ";
    public static final String H2 = "## ";
    public static final String H3 = "### ";
    public static final String H4 = "#### ";
    public static final String H5 = "##### ";
    public static final String H6 = "###### ";
    
    public static final String TABLE_OF_CONTENTS_HEADER_LINK_START = "[";
    public static final String TABLE_OF_CONTENTS_HEADER_LINK_END = "][]";
    
    public static final String BOLD = "**";
    public static final String UNDERLINE = "_";
    public static final String ITALIC = "*";
    public static final String CODE = "`";
    public static final String STRIKETHROUGH = "~~";
    public static final String END_STRIKETHROUGH = "~~";
    public static final String SMALL = "<small markdown=1>";
    public static final String END_SMALL = "</small>";
    public static final String BLOCKQUOTE = "> ";
    
    public static final String BULLET = "* ";
    public static final String LIST_INDENT = "    ";
    
    public static final String CENTER_START = "<div style=\"text-align:center\">";
    public static final String CENTER_END = "</div>";
    
    public static final String SUPERSCRIPT_START = "^";
    public static final String SUPERSCRIPT_END = "^";
    
    public static final String SUBSCRIPT_START = "~";
    public static final String SUBSCRIPT_END = "~";
    
    public static final String STYLE_START = "<style type=\"text/css\">\n";
    public static final String STYLE_END = "\n</style>\n\n";
    
    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES = "";
    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES_FILE = "";
    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES_FOLDER = "";
    
//    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES = "{ .generated-header }";
//    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES_FILE = "{ .generated-header-file }";
//    public static final String PANDOC_GENERATED_TITLE_ATTRIBUTES_FOLDER = "{ .generated-header-folder }";
}
