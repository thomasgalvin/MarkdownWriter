import com.galvin.markdown.swing.editor.MarkdownDocument;

String underlineToken = "_";

int line = currentEditor.getCaretLineNumber();
int lineStart = currentEditor.getLineStartOffset( line );
int lineEnd = currentEditor.getLineEndOffset( line );

MarkdownDocument doc = currentEditor.getDocument();
if( lineEnd >= doc.getLength() ){
    lineEnd = doc.getLength();
}

currentEditor.setCaretPosition( lineStart );
currentEditor.moveCaretPosition( lineEnd );

String text = currentEditor.getSelectedText();
if( text.endsWith( "\n" ) )
{
  text = text.substring( 0 , text.length() - 1 );
}
int length = text.length();

StringBuilder underline = new StringBuilder( length + 1 );
for( int i = 0; i < length; i++ )
{
  underline.append( underlineToken );
}

StringBuilder newText = new StringBuilder( length * 3 + 2 );
newText.append( text );
newText.append( "\n" );
newText.append( underline );

currentEditor.replaceSelection( newText.toString() );
