int selectionStart = currentEditor.getSelectionStart();
String selectedText = currentEditor.getSelectedText();

String replacement = com.galvin.util.MorseCode.encodeToSimpleString( selectedText );

currentEditor.replaceSelection( replacement );
currentEditor.setCaretPosition( selectionStart );
currentEditor.moveCaretPosition( selectionStart + replacement.length() );
