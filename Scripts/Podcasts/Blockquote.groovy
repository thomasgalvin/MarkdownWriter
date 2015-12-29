import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;

MarkdownDocument doc = currentEditor.getDocument();

int selectionStart = currentEditor.getSelectionStart();
int selectionEnd = currentEditor.getSelectionEnd();

int startLine = currentEditor.getLineOfOffset( selectionStart );
int endLine = currentEditor.getLineOfOffset( selectionEnd );

int newSelectionStart = currentEditor.getLineStartOffset( startLine );
int newSelectionEnd = currentEditor.getLineEndOffset( endLine );


if( newSelectionEnd >= doc.getLength() ){
    newSelectionEnd = doc.getLength();
}

currentEditor.setCaretPosition( newSelectionStart );
currentEditor.moveCaretPosition( newSelectionEnd );

String text = currentEditor.getSelectedText();
String[] lines = text.split( "\n" );

StringBuilder newText = new StringBuilder( text.length() * 2 );

for( int i = 0; i < lines.length; i++ ){
    String line = lines[i];
    
    if( i == 0 ){
        line = StringUtils.camelCase(line);
        
        newText.append( "> ##### " );
        newText.append( line );
        newText.append( " ##### {.slugline}\n" );
        newText.append( ">" );
    }
    else {
        newText.append( "> " );
        newText.append( line );
    }
    
    newText.append( "\n" );
}

currentEditor.replaceSelection( newText.toString() );