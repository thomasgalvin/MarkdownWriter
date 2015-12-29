import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;

int line = currentEditor.getCaretLineNumber();
int lineStart = currentEditor.getLineStartOffset( line );

MarkdownDocument doc = currentEditor.getDocument();
int lineEnd = currentEditor.getLineEndOffset( line );
if( lineEnd >= doc.getLength() ){
    lineEnd = doc.getLength();
}

currentEditor.setCaretPosition( lineStart );
currentEditor.moveCaretPosition( lineEnd );

boolean newline = false;
String text = currentEditor.getSelectedText();
if( text.endsWith( "\n" ) )
{
  text = text.substring( 0 , text.length() - 1 );
  newline = true;
}
text = StringUtils.camelCase(text);


StringBuilder newText = new StringBuilder( 24 + text.length() );
newText.append( "##### " );
newText.append( text );
newText.append( " ##### {.slugline}" );

if( newline ){
    newText.append( "\n" );
}

currentEditor.replaceSelection( newText.toString() );
