/**
 * 
 */
package urllistcompare.gui;

import urllistcompare.*;
import urllistcompare.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

/**
 * This class provides a GUI to load the files, set up a URL List and access the
 * different functions.
 * 
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class URLListCompare extends JFrame {

	private final JLabel programStatus;
	private final JLabel extension;

	JFrame frame; // Main frame
	JPanel mainPanel; // Main panel
	BorderLayout mainLayout; // Main layout

	JPanel topPanel; // Top panel
	GroupLayout topLayout; // Layout for the top panel

	public URLListCompare() {

		programStatus = new JLabel("Program Status");
		extension = new JLabel("Extension");

		// TOP PANEL ELEMENTS

		// Program Status
		JTextField statusIndicator = new JTextField("INACTIVE", 10);
		statusIndicator.setForeground(Color.red);
		statusIndicator.setBackground(Color.orange);
		statusIndicator.setEditable(false);

		// Extension
		JTextField extensionIndicator = new JTextField("ON", 4);
		extensionIndicator.setForeground(new Color(85, 107, 47));
		extensionIndicator.setBackground(new Color(0, 255, 0));
		extensionIndicator.setEditable(false);

		JButton extensionToggle = new JButton("Toggle");
		extensionToggle
				.setToolTipText("Toggle file extensions in the URL List.");
		extensionToggle.setEnabled(true);
		extensionToggle.setPreferredSize(new Dimension(100, 20));

		// Load Files
		JButton loadFiles = new JButton("Load Files");
		loadFiles.setToolTipText(
				"Open the screen that manages the operations needed to load"
						+ " the input csv files and create a URL List.");
		loadFiles.setEnabled(true); // Default when opening the program
		loadFiles.setPreferredSize(new Dimension(100, 20));

		// Load Binary
		JButton loadBinary = new JButton("Load Binary");
		loadBinary.setToolTipText(
				"Open the screen that manages the operations needed to load"
						+ " the input binary file from a previous URL List.");
		loadBinary.setEnabled(true); // Default when opening the program
		loadBinary.setPreferredSize(new Dimension(100, 20));

		// TOP PANEL

		topPanel = new JPanel();
		topLayout = new GroupLayout(topPanel);
		topPanel.setLayout(topLayout);
		
		/*
		JPanel topButtonP = new JPanel();
		GroupLayout topBPL = new GroupLayout(topButtonP);
		topButtonP.setLayout(topBPL);
		topBPL.setHorizontalGroup(topBPL.createParallelGroup(Alignment.TRAILING)
				.addComponent(loadFiles)
				.addComponent(loadBinary));
				*/

		GroupLayout.ParallelGroup hGroup = topLayout
				.createParallelGroup(Alignment.LEADING);

		hGroup.addGroup(topLayout.createSequentialGroup()
				.addGroup(topLayout.createParallelGroup()
						.addComponent(programStatus)
						.addComponent(extension))
				.addGroup(topLayout.createParallelGroup()
						.addComponent(statusIndicator)
						.addComponent(extensionIndicator))
				.addGroup(topLayout.createParallelGroup()
						.addComponent(extensionToggle))
				.addGroup(topLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(loadFiles)
						.addComponent(loadBinary)));
		topLayout.setHorizontalGroup(hGroup);
		topLayout.linkSize(SwingConstants.HORIZONTAL, loadBinary, loadFiles,
				statusIndicator, extensionIndicator, extensionToggle);

		// Vertical
		GroupLayout.SequentialGroup vGroup = topLayout.createSequentialGroup();
		vGroup.addGroup(
				topLayout.createParallelGroup().addComponent(programStatus)
						.addComponent(statusIndicator).addComponent(loadFiles));
		vGroup.addGroup(topLayout.createParallelGroup().addComponent(extension)
				.addComponent(extensionIndicator).addComponent(extensionToggle)
				.addComponent(loadBinary));
		topLayout.setVerticalGroup(vGroup);
		topLayout.linkSize(SwingConstants.VERTICAL, loadBinary, loadFiles,
				statusIndicator, extensionIndicator, extensionToggle);

		// MAIN PANEL;
		mainPanel = new JPanel();
		mainLayout = new BorderLayout();
		mainPanel.setLayout(mainLayout);
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(new JLabel("second frame"), BorderLayout.SOUTH);

		// MAIN FRAME
		frame = new JFrame();
		frame.add(mainPanel);
		frame.setTitle("Rocco Barbini's URL List Compare");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024, 768);
		frame.setVisible(true);

	}

	/**
	 * @param args
	 */
	public static void
		main(String[] args) {
		// Run GUI codes in Event-Dispatching thread for thread-safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void
				run() {
				new URLListCompare(); // Let the constructor do the job
			}
		});
	}

}
