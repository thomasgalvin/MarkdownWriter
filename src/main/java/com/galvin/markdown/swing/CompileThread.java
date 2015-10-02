package com.galvin.markdown.swing;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.CompileResults;
import com.galvin.markdown.compilers.MarkdownCompiler;
import com.galvin.markdown.swing.dialogs.ExportCompleteDialog;
import java.util.List;
import javax.swing.JOptionPane;

public class CompileThread
        extends Thread
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Controller controller;
    private MarkdownCompiler compiler;
    private CompileOptions compileOptions;

    public CompileThread( Controller controller, MarkdownCompiler compiler, CompileOptions compileOptions )
    {
        this.controller = controller;
        this.compiler = compiler;
        this.compileOptions = compileOptions;
    }

    @Override
    public void run()
    {
        try
        {
            List<CompileResults> results = compiler.compile( compileOptions );
            
            if( !results.isEmpty() && !interrupted() )
            {
                ExportCompleteDialog dialog = new ExportCompleteDialog( results );
                dialog.setVisible( true );
            }
            else if( !interrupted() )
            {
                JOptionPane.showMessageDialog( controller.getPopupWindowOwner(),
                                               messages.dialogMessageCompileNoResults(),
                                               messages.dialogMessageCompileNoResultsTitle(),
                                               JOptionPane.INFORMATION_MESSAGE );
            }
        }
        catch( Throwable t )
        {
            JOptionPane.showMessageDialog( controller.getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }
}