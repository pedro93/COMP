package gui;
import java.io.*;

import javax.swing.*;


public class Console implements Runnable
{
	JTextArea displayPane;
	BufferedReader reader;
	static PrintStream out = System.out;
	static PrintStream err = System.err;

	private Console(JTextArea displayPane, PipedOutputStream pos)
	{
		this.displayPane = displayPane;
		try
		{
			PipedInputStream pis = new PipedInputStream( pos );
			reader = new BufferedReader( new InputStreamReader(pis) );
		}
		catch(IOException e) {}
	}

	public void run()
	{
		String line = null;

		try
		{
			while ((line = reader.readLine()) != null)
			{
				displayPane.append( line + "\n" );
				displayPane.setCaretPosition( displayPane.getDocument().getLength() );

			}

			System.err.println("im here");
		}
		catch (IOException ioe)
		{
			JOptionPane.showMessageDialog(null,
					"Error redirecting output : "+ioe.getMessage());
		}
	}

	public static void redirectOutput(JTextArea displayPane)
	{
		Console.redirectOut(displayPane);
		Console.redirectErr(displayPane);
	}

	public static void redirectOut(JTextArea displayPane)
	{
		PipedOutputStream pos = new PipedOutputStream();
		System.setOut( new PrintStream(pos, true) );
		Console console = new Console(displayPane, pos);
		new Thread(console).start();
	}

	public static void redirectErr(JTextArea displayPane)
	{
		PipedOutputStream pos = new PipedOutputStream();
		System.setErr( new PrintStream(pos, true) );

		Console console = new Console(displayPane, pos);
		new Thread(console).start();
	}

	public static void clear() {
		System.setOut(Console.out);
		System.setErr(Console.err);
	}

}