int selectionStart = currentEditor.getSelectionStart();
String selectedText = currentEditor.getSelectedText();

StringBuilder builder = new StringBuilder( selectedText.length() );

boolean whitespaceMode = true;
for( char c : selectedText.toCharArray() )
{
    if( whitespaceMode )
    {
        if( !Character.isWhitespace( c ) )
        {
            c = Character.toUpperCase( c );
            builder.append( "<b>" );
            builder.append( c );
            builder.append( "</b>" );
            whitespaceMode = false;
        }
        else
        {
            builder.append( c );
        }
    }
    else
    {
        if( Character.isWhitespace( c ) )
        {
            whitespaceMode = true;
        }
        builder.append( c );
    }
}

currentEditor.replaceSelection( builder.toString() );
currentEditor.setCaretPosition( selectionStart );
currentEditor.moveCaretPosition( selectionStart + builder.length() );
