import com.galvin.markdown.swing.editor.MarkdownDocument;

final String TOKEN = "*";

int line = currentEditor.getCaretLineNumber();
int lineStart = currentEditor.getLineStartOffset( line );

MarkdownDocument doc = currentEditor.getDocument();
int lineEnd = currentEditor.getLineEndOffset( line );
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
int length = text.length() + 6;

StringBuilder hash = new StringBuilder( length + 1 );
for( int i = 0; i < length; i++ )
{
  hash.append( TOKEN );
}

StringBuilder newText = new StringBuilder( length * 3 + 2 );
newText.append( hash );
newText.append( "  \n" );
newText.append( TOKEN );
newText.append( TOKEN );
newText.append( " " );
newText.append( text );
newText.append( " " );
newText.append( TOKEN );
newText.append( TOKEN );
newText.append( " \n" );
newText.append( hash );
newText.append( "  \n" );

currentEditor.replaceSelection( newText.toString() );
