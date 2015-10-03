int line = currentEditor.getCaretLineNumber();
int lineStart = currentEditor.getLineStartOffset( line );
int lineEnd = currentEditor.getLineEndOffset( line );
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
  hash.append( "*" );
}

StringBuilder newText = new StringBuilder( length * 3 + 2 );
newText.append( hash );
newText.append( "  \n** " );
newText.append( text );
newText.append( " **  \n" );
newText.append( hash );
newText.append( "  \n" );

currentEditor.replaceSelection( newText.toString() );
