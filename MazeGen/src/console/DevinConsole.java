/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package console;

/**
 * DevinConsole.java
 * 
 * @author				Devin Barry
 * @date 				25/10/2012
 * @lastModification	25/10/2012
 *  
 * DevinConsole is a class that creates a separate output window which
 * is designed to act just like a console output.
 * It can print messages which are sent it, just like System.out.
 * 
 * This class differs from Console in that it does not have a static
 * PrintStream as its output, and thus each time this class is
 * instantiated, a new seperate print stream is created. This allows
 * the use of multiple windows of output.
 * 
 * Code here is almost identical to Console, but there is no static
 * output.
 */

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DevinConsole extends JPanel implements ActionListener {
	
	private String windowName = "Devin Console";

	// All serializable objects need a serialVersionUID
	private static final long serialVersionUID = 1L;
	private PrintStream out; //the output stream to print to

	private JButton resetButton, testButton;
	JTextArea log;
	
	/**
	 * Constructor which allows window name to be customized
	 * @param windowName
	 */
	public DevinConsole(String windowName) {
		this(); //call the default constructor
		//then set the window name
		this.windowName = windowName;
	}

	/**
	 * Default constructor
	 */
	public DevinConsole() {
		super(new BorderLayout());

		// Create the log first, because the action listeners need to refer to it.
		log = new JTextArea(10, 50);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create the PrintStream and make sure it auto-flushes
		out = new PrintStream(new TextAreaOutputStream(log), true);

		resetButton = new JButton("Clear Console");
		resetButton.addActionListener(this);
		testButton = new JButton("Test");
		testButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(resetButton);
		buttonPanel.add(testButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	// When the user clicks on the various buttons, these events are handled
	// here
	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle resetButton action.
		if (e.getSource() == resetButton) {
			log.setText(null);
			log.setCaretPosition(log.getDocument().getLength());
		}
		if (e.getSource() == testButton) {
			out.println("Testing Testing 123");
			log.setCaretPosition(log.getDocument().getLength());
		}
		log.setCaretPosition(log.getDocument().getLength());
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame(windowName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the console to the window.
		frame.add(this);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public void createConsole() {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
	
	/**
	 * Get the PrintStream for this DevinConsole
	 * @return
	 */
	public PrintStream getPrintStream() {
		return out;
	}

	// Main method used for testing this class separately
	public static void main(String[] args) {
		DevinConsole dc1 = new DevinConsole();
		dc1.createConsole();
		DevinConsole dc2 = new DevinConsole();
		dc2.createConsole();
		PrintStream basic1 = dc1.getPrintStream();
		PrintStream basic2 = dc2.getPrintStream();
		// Sleep for 5s to give console time to show
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
		}

		System.out.println("First Test Print");
		basic1.println("Hello World From SystemJ");
		basic1.println("LineTest");
		basic2.println("Hello World From SystemJ");
		basic2.println("LineTest");
		// DevinConsole.console.flush();
		// DevinConsole.console.close();
		// System.out.println("Closed");
	}

}
