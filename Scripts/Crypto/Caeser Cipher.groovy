int selectionStart = currentEditor.getSelectionStart();
String selectedText = currentEditor.getSelectedText();

com.galvin.util.CaesarCipher cipher = new com.galvin.util.CaesarCipher( 3 );
String replacement = cipher.encode( selectedText );

currentEditor.replaceSelection( replacement );
currentEditor.setCaretPosition( selectionStart );
currentEditor.moveCaretPosition( selectionStart + replacement.length() );
