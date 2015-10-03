int selectionStart = currentEditor.getSelectionStart();
String selectedText = currentEditor.getSelectedText();

String replacement = com.galvin.util.Rot13.rot13( selectedText );

currentEditor.replaceSelection( replacement );
currentEditor.setCaretPosition( selectionStart );
currentEditor.moveCaretPosition( selectionStart + replacement.length() );
