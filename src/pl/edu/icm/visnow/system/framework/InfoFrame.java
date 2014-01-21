package pl.edu.icm.visnow.system.framework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.edu.icm.visnow.engine.core.Input;
import pl.edu.icm.visnow.engine.core.Output;
import pl.edu.icm.visnow.engine.main.Port;
import pl.edu.icm.visnow.lib.types.VNDataAcceptor;
import pl.edu.icm.visnow.lib.types.VNDataSchema;
import pl.edu.icm.visnow.system.main.VisNow;

/**
 *
 * @author know
 */
public class InfoFrame extends javax.swing.JFrame {

    private Icon infoIcon = UIManager.getIcon("OptionPane.informationIcon");
    private Output currentOutPort = null;
    private ChangeListener refreshListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (currentOutPort != null) {
                refreshContent(currentOutPort);
            }
        }
    };

    /**
     * Creates new form InfoFrame
     */
    public InfoFrame() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource(VisNow.getIconPath())).getImage());
        this.addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentHidden(ComponentEvent e) {
                if(currentOutPort != null) {
                    currentOutPort.setValueChangeListener(null);
                    currentOutPort = null;
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                /* code run when component shown */
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        infoLabel = new JLabel();
        jPanel3 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jPanel4 = new JPanel();
        infoText = new JLabel();
        jPanel5 = new JPanel();
        okButton = new JButton();

        setTitle("Info");
        setPreferredSize(new Dimension(600, 400));

        jPanel1.setLayout(new GridBagLayout());

        jPanel2.setMinimumSize(new Dimension(300, 25));
        jPanel2.setPreferredSize(new Dimension(400, 25));
        jPanel2.setRequestFocusEnabled(false);
        jPanel2.setLayout(new GridBagLayout());

        infoLabel.setText("Contents of");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 10, 5, 10);
        jPanel2.add(infoLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setMinimumSize(new Dimension(300, 200));
        jPanel3.setPreferredSize(new Dimension(450, 300));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setLayout(new BorderLayout());

        jScrollPane1.setBorder(BorderFactory.createEtchedBorder());

        jPanel4.setMinimumSize(new Dimension(300, 300));
        jPanel4.setLayout(new GridBagLayout());

        infoText.setText("no data");
        infoText.setToolTipText("");
        infoText.setVerticalTextPosition(SwingConstants.TOP);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        jPanel4.add(infoText, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel4);

        jPanel3.add(jScrollPane1, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel5.setMinimumSize(new Dimension(300, 25));
        jPanel5.setPreferredSize(new Dimension(400, 25));
        jPanel5.setLayout(new GridLayout(1, 0));

        okButton.setFont(new Font("Dialog", 1, 11)); // NOI18N
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel5.add(okButton);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jPanel5, gridBagConstraints);

        getContentPane().add(jPanel1, BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   private void okButtonActionPerformed(ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
   {//GEN-HEADEREND:event_okButtonActionPerformed
       setVisible(false);
   }//GEN-LAST:event_okButtonActionPerformed
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 460;

    public void showRefreshingContent(int ix, int iy, Output outPort) {
        if(currentOutPort != null) {
            currentOutPort.setValueChangeListener(null);
            currentOutPort = null;
        }
        currentOutPort = outPort;
        currentOutPort.setValueChangeListener(refreshListener);
        refreshContent(outPort);
        infoText.setIcon(infoIcon);
        if (!this.isVisible()) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (ix + DEFAULT_WIDTH > screenSize.getWidth()) {
                ix = (int) screenSize.getWidth() - DEFAULT_WIDTH;
            }
            if (iy + DEFAULT_HEIGHT > screenSize.getHeight()) {
                iy = (int) screenSize.getHeight() - DEFAULT_HEIGHT;
            }
            setBounds(new java.awt.Rectangle(ix, iy, DEFAULT_WIDTH, DEFAULT_HEIGHT));
            setVisible(true);
        } else {
            toFront();
        }
    }

    private void refreshContent(Output outPort) {
        String portName = outPort.getModuleBox().getName() + " -> " + outPort.getName();
        Object object = outPort.getData().getValue();
        infoLabel.setText("Contents of " + portName + ":");
        if (object == null) {
            infoText.setText("no data");
        } else {
            infoText.setText(object.toString());
        }
    }

    public void showPortDetailedInfo(int ix, int iy, Port port) {
        String portName = port.getModuleBox().getName() + " -> " + port.getName();
        infoLabel.setText("Detailed info on " + portName + ":");
        if (port == null) {
            infoText.setText("<no additional info>");
        } else {
            StringBuilder s = new StringBuilder();
            s.append("<html>");
            if (port instanceof Input) {
                Input in = (Input) port;
                s.append("Input port<br><br>");
                s.append("Description:<br>").append(in.getDescription() != null ? in.getDescription() : "<no description>");
                s.append("<br><br>");
                s.append("<table>");
                s.append("<tr><td>Type:</td><td>").append(in.getType().getSimpleName()).append("</td></tr>");
                s.append("<tr></tr>");

                VNDataAcceptor[] acceptors = in.getVNDataAcceptors();
                if (acceptors != null && acceptors.length > 0) {
                    s.append("<tr><td valign=top>Requires:</td><td valign=top>");
                    for (int i = 0; i < acceptors.length; i++) {
                        s.append(acceptors[i].toHtmlString()).append("<br>");
                        if (i < acceptors.length - 1) {
                            s.append("OR<br>");
                        }
                    }
                    s.append("</td></tr>");
                }
                s.append("</table>");
            } else if (port instanceof Output) {
                Output out = (Output) port;
                s.append("Output port<br><br>");
                s.append("Description:<br>").append(out.getDescription() != null ? out.getDescription() : "<no description>");
                s.append("<br><br>");
                s.append("<table>");
                s.append("<tr><td>Type:</td><td>").append(out.getType().getSimpleName()).append("</td></tr>");
                s.append("<tr></tr>");

                VNDataSchema[] schemas = out.getVNDataSchemas();
                if (schemas != null && schemas.length > 0) {
                    s.append("<tr><td valign=top>Produces:</td><td valign=top>");
                    for (int i = 0; i < schemas.length; i++) {
                        s.append(schemas[i].toHtmlString()).append("<br>");
                        if (i < schemas.length - 1) {
                            s.append("OR<br>");
                        }
                    }
                    s.append("</td></tr>");
                }
                s.append("</table>");
            }
            s.append("</html>");
            infoText.setText(s.toString());
        }
        infoText.setIcon(infoIcon);

        if (!this.isVisible()) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (ix + DEFAULT_WIDTH > screenSize.getWidth()) {
                ix = (int) screenSize.getWidth() - DEFAULT_WIDTH;
            }
            if (iy + DEFAULT_HEIGHT > screenSize.getHeight()) {
                iy = (int) screenSize.getHeight() - DEFAULT_HEIGHT;
            }
            setBounds(new java.awt.Rectangle(ix, iy, DEFAULT_WIDTH, DEFAULT_HEIGHT));
            setVisible(true);
        } else {
            toFront();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel infoLabel;
    private JLabel infoText;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JScrollPane jScrollPane1;
    private JButton okButton;
    // End of variables declaration//GEN-END:variables
}