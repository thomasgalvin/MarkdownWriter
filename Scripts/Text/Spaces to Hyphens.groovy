int selectionStart = currentEditor.getSelectionStart();
String selectedText = currentEditor.getSelectedText();

StringBuilder builder = new StringBuilder( selectedText );
com.galvin.util.StringUtils.replaceAll( builder, " ", "-" );

currentEditor.replaceSelection( builder.toString() );
currentEditor.setCaretPosition( selectionStart );
currentEditor.moveCaretPosition( selectionStart + builder.length() );
