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
		cp.setLayout(new BorderLayout(10,4));
		
		// main phone buttons
		JPanel centerPanel = new JPanel(new GridLayout(4, 3));
		cp.add(centerPanel, BorderLayout.CENTER);
		cp.add(new JLabel("East"), BorderLayout.EAST);
		cp.add(new JLabel("West"), BorderLayout.WEST);
		
		JTextField hasHeaderA = new JTextField(7);
		hasHeaderA.setText("Header");
		hasHeaderA.setForeground(Color.red);
		hasHeaderA.setBackground(Color.orange);
		hasHeaderA.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
            	JTextField source = (JTextField) e.getSource();
                if(source.getText().equals("Header")) {
                	source.setText("Toggled");
                	source.setForeground(Color.green);
                	source.setBackground(Color.white);
                }
                else {
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
