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
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * This class provides a GUI to load the files, set up a URL List and access the
 * different functions.
 * 
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class URLListCompare extends JFrame
{

  private final JLabel programStatus;
  private final JLabel extension;

  JFrame frame; // Main frame
  JPanel mainPanel; // Main panel
  BorderLayout mainLayout; // Main layout

  JPanel topPanel; // Top panel
  GroupLayout topLayout; // Layout for the top panel

  public URLListCompare()
  {

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
    extensionToggle.setToolTipText("Toggle file extensions in the URL List.");
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

    GroupLayout.ParallelGroup hGroup = topLayout
        .createParallelGroup(Alignment.LEADING);

    hGroup.addGroup(topLayout.createSequentialGroup()
        .addGroup(topLayout.createParallelGroup().addComponent(programStatus)
            .addComponent(extension))
        .addGroup(topLayout.createParallelGroup().addComponent(statusIndicator)
            .addComponent(extensionIndicator))
        .addGroup(
            topLayout.createParallelGroup().addComponent(extensionToggle)));
    topLayout.setHorizontalGroup(hGroup);
    topLayout.linkSize(SwingConstants.HORIZONTAL, statusIndicator,
        extensionIndicator, extensionToggle);

    // Vertical
    GroupLayout.SequentialGroup vGroup = topLayout.createSequentialGroup();
    vGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER).addComponent(programStatus)
        .addComponent(statusIndicator));
    vGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER).addComponent(extension)
        .addComponent(extensionIndicator).addComponent(extensionToggle));
    topLayout.setVerticalGroup(vGroup);
    topLayout.linkSize(SwingConstants.VERTICAL, statusIndicator,
        extensionIndicator, extensionToggle);

    // BUTTONS TOP PANEL
    JPanel topButtonP = new JPanel();
    GroupLayout topBPL = new GroupLayout(topButtonP);
    topButtonP.setLayout(topBPL);
    topBPL.setHorizontalGroup(topBPL.createParallelGroup(Alignment.TRAILING)
        .addComponent(loadFiles).addComponent(loadBinary));
    topLayout.linkSize(SwingConstants.HORIZONTAL, loadFiles, loadBinary);
    topBPL.setVerticalGroup(topBPL.createSequentialGroup()
        .addComponent(loadFiles).addComponent(loadBinary));
    topLayout.linkSize(SwingConstants.VERTICAL, loadFiles, loadBinary);

    // SUPER TOP PANEL
    JPanel superTop = new JPanel();
    BorderLayout stLayout = new BorderLayout();
    superTop.setLayout(stLayout);
    superTop.add(topPanel, BorderLayout.WEST);
    superTop.add(topButtonP, BorderLayout.EAST);

    // MAIN PANEL;
    mainPanel = new JPanel();
    mainLayout = new BorderLayout();
    mainPanel.setLayout(mainLayout);
    // mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(superTop, BorderLayout.NORTH);
    mainPanel.add(new JLabel("second frame"), BorderLayout.SOUTH);

    // MAIN FRAME
    frame = new JFrame();
    frame.add(mainPanel);
    frame.setTitle("Rocco Barbini's URL List Compare");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(800, 600)); // Can't be made smaller than
                                                   // 800 x 600
    // frame.setSize(1024, 768); // Make it run 1024 x 768 by default
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Make it run full screen by
                                                   // default
    frame.setVisible(true);
  }

  /**
   * @param args
   */
  public static void
    main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
        if ("Nimbus".equals(info.getName()))
        {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }
    catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Run GUI codes in Event-Dispatching thread for thread-safety
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void
        run()
      {
        new URLListCompare(); // Let the constructor do the job
      }
    });
  }

}
