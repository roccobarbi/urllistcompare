/**
 * 
 */
package urllistcompare.gui;

import urllistcompare.*;
import urllistcompare.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class provides a GUI to load the files, set up a URL List and access the
 * different functions.
 * 
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class URLListCompare extends JFrame {

	public URLListCompare() {
		// Retrieve the top-level container
		Container cp = getContentPane();

		// Set the layout
		cp.setLayout(new BorderLayout());

		// Top Panel
		JPanel topPanel = new JPanel();
		GroupLayout topLayout = new GroupLayout(topPanel);
		topLayout.setAutoCreateGaps(true);
		topLayout.setAutoCreateContainerGaps(true);

		// Program Status
		JLabel programStatus = new JLabel("Program Status");
		JTextField statusIndicator = new JTextField("INACTIVE", 10);
		statusIndicator.setForeground(Color.red);
		statusIndicator.setBackground(Color.orange);
		statusIndicator.setEditable(false);

		// Extension
		JLabel extension = new JLabel("Extension");
		JTextField extensionIndicator = new JTextField("ON", 4);
		extensionIndicator.setForeground(new Color(85, 107, 47));
		extensionIndicator.setBackground(new Color(0, 255, 0));
		extensionIndicator.setEditable(false);
		JButton extensionToggle = new JButton("Extension Toggle");
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

		// Add elements to top panel layout
		topLayout.setHorizontalGroup(topLayout.createSequentialGroup()
				.addGroup(topLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(topLayout.createSequentialGroup()
								.addComponent(programStatus)
								.addComponent(statusIndicator))
						.addGroup(topLayout.createSequentialGroup()
								.addComponent(extension)
								.addComponent(extensionIndicator)
								.addComponent(extensionToggle))
						.addGroup(topLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(loadFiles)
								.addComponent(loadBinary))));
		topLayout.setVerticalGroup(topLayout.createSequentialGroup().addGroup(
				topLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(programStatus)
				.addComponent(statusIndicator)
				.addComponent(loadFiles))
				.addGroup(topLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(extension)
						.addComponent(extensionIndicator)
						.addComponent(extensionToggle)
						.addComponent(loadBinary)));

		// Top Panel Add
		cp.add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(4, 3));
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(new JLabel("East"), BorderLayout.EAST);
		cp.add(new JLabel("West"), BorderLayout.WEST);

		JTextField hasHeaderA = new JTextField(7);
		hasHeaderA.setText("Header");
		hasHeaderA.setForeground(Color.red);
		hasHeaderA.setBackground(Color.orange);
		hasHeaderA.addMouseListener(new MouseAdapter() {
			@Override
			public void
				mouseClicked(MouseEvent e) {
				JTextField source = (JTextField) e.getSource();
				if (source.getText().equals("Header")) {
					source.setText("Toggled");
					source.setForeground(Color.green);
					source.setBackground(Color.white);
				} else {
					source.setText("Header");
					source.setForeground(Color.red);
					source.setBackground(Color.orange);
				}
			}
		});
		hasHeaderA.setEditable(false);
		centerPanel.add(hasHeaderA);

		// Exit the program when the close-window button clicked
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("URLListCompare"); // "super" JFrame sets title
		setSize(800, 600);
		setVisible(true); // "super" JFrame shows
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
