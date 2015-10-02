package com.galvin.markdown.swing;

import com.galvin.markdown.model.ProjectIo;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class MarkdownSocket
{

    public static final int PORT = 9326;
    private ServerSocket serverSocket;

    public static boolean alreadyRunning( String[] args )
    {
        try
        {
            Socket socket = new Socket( InetAddress.getByName( null ), PORT );
            
            System.out.println( "Markdown socket args:" );
            for( String arg : args )
            {
                System.out.println( "    " + arg );
                PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
                out.println( arg );
            }

            socket.close();
            return true;
        }
        catch( Throwable t )
        {
            //System.out.println( "MarkdownServer is not already running" );
        }

        return false;
    }

    public MarkdownSocket()
    {
        try
        {
            SocketAddress socketAddress = new InetSocketAddress( InetAddress.getByName( null ), PORT );
            serverSocket = new ServerSocket();
            serverSocket.bind( socketAddress );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public void acceptConnections()
    {
        ListenerThread listenerThread = new ListenerThread();
        listenerThread.start();
    }

    private class ListenerThread
            extends Thread
    {

        @Override
        public void run()
        {
            try
            {
                while( true )
                {
                    Socket clientSocket = serverSocket.accept();
                    SocketThread thread = new SocketThread( clientSocket );
                    thread.start();
                }
            }
            catch( Throwable t )
            {
                t.printStackTrace();
            }
        }
    }

    private class SocketThread
            extends Thread
    {

        private Socket clientSocket;

        public SocketThread( Socket clientSocket )
        {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run()
        {
            System.out.println( "Got a connection from: " + clientSocket.getInetAddress() );

            boolean projectFiles = false;

            try
            {
                String inputLine;
                BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
                System.out.println( "Socket data:" );
                while( ( inputLine = in.readLine() ) != null )
                {
                    System.out.println( "    " + inputLine );
//                    if( inputLine.toLowerCase().endsWith( ProjectIo.PROJECT_STRUCTURE_DOCUMENT ) )
//                    {
                        try
                        {
                            File file = new File( inputLine );
                            if( file.exists() && file.canRead() )
                            {
                                System.out.println( "Opening project file: " + inputLine );
                                MarkdownServer.fileOpen( file );
                            }
                        }
                        catch( Throwable t )
                        {
                        }
//                    }
                }
            }
            catch( Throwable t )
            {
                t.printStackTrace();
            }

            if( !projectFiles )
            {
                MarkdownServer.focusCurrentWindow();
            }
        }
    }
}
