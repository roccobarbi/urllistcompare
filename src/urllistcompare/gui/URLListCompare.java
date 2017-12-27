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

	public URLListCompare() {

		programStatus = new JLabel("Program Status");
		extension = new JLabel("Extension");

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

		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		GroupLayout.ParallelGroup hGroup = layout
				.createParallelGroup(Alignment.LEADING);

		hGroup.addGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(programStatus).addComponent(extension))
				.addGroup(layout.createParallelGroup()
						.addComponent(statusIndicator)
						.addComponent(extensionIndicator))
				.addGroup(layout.createParallelGroup()
						.addComponent(extensionToggle))
				.addGroup(layout.createParallelGroup().addComponent(loadFiles)
						.addComponent(loadBinary)));
		layout.setHorizontalGroup(hGroup);
		layout.linkSize(SwingConstants.HORIZONTAL, loadBinary, loadFiles,
				statusIndicator, extensionIndicator, extensionToggle);

		// Vertical
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup().addComponent(programStatus)
				.addComponent(statusIndicator).addComponent(loadFiles));
		vGroup.addGroup(layout.createParallelGroup().addComponent(extension)
				.addComponent(extensionIndicator).addComponent(extensionToggle)
				.addComponent(loadBinary));
		layout.setVerticalGroup(vGroup);
		layout.linkSize(SwingConstants.VERTICAL, loadBinary, loadFiles,
				statusIndicator, extensionIndicator, extensionToggle);

		// frame.add(panel);

		JPanel panel2 = new JPanel();
		BorderLayout fLayout = new BorderLayout();
		panel2.setLayout(fLayout);
		panel2.add(panel, BorderLayout.NORTH);
		panel2.add(new JLabel("second frame"), BorderLayout.SOUTH);
		frame.add(panel2);
		
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
