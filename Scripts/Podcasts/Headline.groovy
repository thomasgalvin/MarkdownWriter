import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;

int line = currentEditor.getCaretLineNumber();
int lineStart = currentEditor.getLineStartOffset( line );
int lineEnd = currentEditor.getLineEndOffset( line );

MarkdownDocument doc = currentEditor.getDocument();
if( lineEnd >= doc.getLength() ){
    lineEnd = doc.getLength();
}

currentEditor.setCaretPosition( lineStart );
currentEditor.moveCaretPosition( lineEnd );

boolean newline = false;
String text = currentEditor.getSelectedText();
if( text.endsWith( "\n" ) )
{
  newline = true;
  text = text.substring( 0 , text.length() - 1 );
}

text = StringUtils.camelCase(text);
int length = text.length();

StringBuilder newText = new StringBuilder();
newText.append( "### " );
newText.append( text );
newText.append( " ###" );

if( newline ){
    newText.append( "\n" );
}

currentEditor.replaceSelection( newText.toString() );
