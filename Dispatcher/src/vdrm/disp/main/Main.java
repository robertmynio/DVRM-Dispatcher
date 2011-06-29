package vdrm.disp.main;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import vdrm.base.data.IServer;
import vdrm.base.data.ITask;
import vdrm.base.impl.BaseCommon;
import vdrm.disp.dispatcher.Dispatcher;
import javax.swing.JDialog;

public class Main extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private Dispatcher dispatcher = null;  //  @jve:decl-index=0:
	private JDialog jErrorDialog = null;
	private JButton jErrorButton = null;
	private JTextField jTextField1 = null;
	private JButton jButtonStart = null;
	private JButton jButtonEnd = null;
	private JLabel jLabelConfig = null;
	private JCheckBox jCheckBoxPrediction = null;
	private JLabel jLabelPrediction = null;
	private JLabel jLabelMigration = null;
	private JLabel jLabelOpenNebula = null;
	private JCheckBox jCheckBoxMigration = null;
	private JCheckBox jCheckBoxONService = null;
	private JLabel jLabelPower = null;
	private JCheckBox jCheckBoxPower = null;
	private JLabel jLabelLogger = null;
	private JCheckBox jCheckBoxLogger = null;
	private JDialog jDialog = null;  //  @jve:decl-index=0:visual-constraint="380,66"
	private JPanel jContentPane1 = null;
	private JButton jButton = null;
	private JLabel jLabel = null;

	
	/**
	 * This is the default constructor
	 */
	public Main() {
		super();
		initialize();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		        // Perhaps ask user if they want to save any unsaved files first.
		        System.exit(0); 
		    }
		});
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 325);
		this.setContentPane(getJContentPane());
		this.setTitle("Dispatcher");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelLogger = new JLabel();
			jLabelLogger.setBounds(new Rectangle(110, 195, 125, 25));
			jLabelLogger.setText("Enable logging");
			jLabelPower = new JLabel();
			jLabelPower.setBounds(new Rectangle(110, 160, 125, 25));
			jLabelPower.setText("Enable PowerService");
			jLabelOpenNebula = new JLabel();
			jLabelOpenNebula.setBounds(new Rectangle(110, 125, 125, 25));
			jLabelOpenNebula.setText("Enable OpenNebula");
			jLabelMigration = new JLabel();
			jLabelMigration.setBounds(new Rectangle(110, 90, 125, 25));
			jLabelMigration.setText("Enable migration");
			jLabelPrediction = new JLabel();
			jLabelPrediction.setBounds(new Rectangle(110, 55, 125, 25));
			jLabelPrediction.setText("Enable prediction");
			jLabelConfig = new JLabel();
			jLabelConfig.setBounds(new Rectangle(20, 20, 91, 25));
			jLabelConfig.setText("Config file path:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJTextField1(), null);
			jContentPane.add(getJButtonStart(), null);
			jContentPane.add(getJButtonEnd(), null);
			jContentPane.add(jLabelConfig, null);
			jContentPane.add(getJCheckBoxPrediction(), null);
			jContentPane.add(jLabelPrediction, null);
			jContentPane.add(jLabelMigration, null);
			jContentPane.add(jLabelOpenNebula, null);
			jContentPane.add(getJCheckBoxMigration(), null);
			jContentPane.add(getJCheckBoxONService(), null);
			jContentPane.add(jLabelPower, null);
			jContentPane.add(getJCheckBoxPower(), null);
			jContentPane.add(jLabelLogger, null);
			jContentPane.add(getJCheckBoxLogger(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
			jTextField1.setSize(160, 25);
			jTextField1.setLocation(new Point(110, 20));
		}
		return jTextField1;
	}

	/**
	 * This method initializes jButtonStart	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setBounds(new Rectangle(37, 245, 80, 30));
			jButtonStart.setText("Start");
			jButtonStart.addActionListener(this);
		}
		return jButtonStart;
	}

	/**
	 * This method initializes jButtonEnd	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEnd() {
		if (jButtonEnd == null) {
			jButtonEnd = new JButton();
			jButtonEnd.setBounds(new Rectangle(160, 245, 80, 30));
			jButtonEnd.setText("End");
			jButtonEnd.setEnabled(false);
			jButtonEnd.addActionListener(this);
		}
		return jButtonEnd;
	}

	/**
	 * This method initializes jCheckBoxPrediction	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPrediction() {
		if (jCheckBoxPrediction == null) {
			jCheckBoxPrediction = new JCheckBox();
			jCheckBoxPrediction.setBounds(new Rectangle(240, 55, 21, 21));
			jCheckBoxPrediction.setSelected(true);
		}
		return jCheckBoxPrediction;
	}

	/**
	 * This method initializes jCheckBoxMigration	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxMigration() {
		if (jCheckBoxMigration == null) {
			jCheckBoxMigration = new JCheckBox();
			jCheckBoxMigration.setBounds(new Rectangle(240, 90, 21, 21));
			jCheckBoxMigration.setSelected(true);
		}
		return jCheckBoxMigration;
	}

	/**
	 * This method initializes jCheckBoxONService	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxONService() {
		if (jCheckBoxONService == null) {
			jCheckBoxONService = new JCheckBox();
			jCheckBoxONService.setBounds(new Rectangle(240, 125, 21, 21));
			jCheckBoxONService.setSelected(false);
		}
		return jCheckBoxONService;
	}

	/**
	 * This method initializes jCheckBoxPower	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxPower() {
		if (jCheckBoxPower == null) {
			jCheckBoxPower = new JCheckBox();
			jCheckBoxPower.setBounds(new Rectangle(240, 160, 21, 21));
			jCheckBoxPower.setSelected(false);
		}
		return jCheckBoxPower;
	}

	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == jButtonEnd) {
			dispatcher.stopTheDispatcher();
			jButtonEnd.setEnabled(false);
			jButtonStart.setEnabled(true);
		}
		if(arg0.getSource() == jErrorButton) {
			jErrorDialog.setVisible(false);
			jButtonStart.setEnabled(true);
			jButtonEnd.setEnabled(false);
		}
		if(arg0.getSource() == jButtonStart) {
			//jButtonStart.setEnabled(false);
			jButtonEnd.setEnabled(true);
			// CREATE A BOOLEAN FOR THIS!
			boolean migration = jCheckBoxMigration.isSelected();
			
			BaseCommon.Instance().PredictionEnabled = jCheckBoxPrediction.isSelected();
			BaseCommon.Instance().ONEnabled = jCheckBoxONService.isSelected();
			BaseCommon.Instance().PSEnabled = jCheckBoxPower.isSelected();
			BaseCommon.Instance().logEnabled = jCheckBoxLogger.isSelected();
			
			String path = jTextField1.getText();
			File f = new File(path);
			boolean success = true;
			if(f.exists()){
				if(dispatcher == null)
					success = startDispatcher(path);
				else{
					dispatcher.ParseWorkloadXML(path);
					success = true;
				}
			}else
				success = false;
			if(!success) {
				jErrorDialog = getJDialog();
				jErrorButton = getJButton();
				jErrorDialog.setVisible(true);
			}
		}		
	}
	
	private boolean startDispatcher(String path) {
		JaxbConfig configuration = null;
		JaxbServers tempServers = null;
		JaxbTasks tempTasks = null;
		JaxbHistory tempHistory = null;
		ArrayList<ITask> history = null;
		ArrayList<IServer> servers = null;
		ArrayList<ITask> tasks = null;
		
		Unmarshaller um;
		JAXBContext context;
		boolean ioException = false;

		
		//1. load configuration file
		try {
			context = JAXBContext.newInstance(JaxbConfig.class);
			um = context.createUnmarshaller();
			configuration = (JaxbConfig) um.unmarshal(new FileReader(path));
		} catch (IOException e1) {
			ioException = true;
		} catch (Exception ee2) {
			ioException = true;
		} 
		
		//2. parse configuration file and load servers, tasks and history

		//2.1. first load the servers
		try {
			context = JAXBContext.newInstance(JaxbServers.class);
			um = context.createUnmarshaller();
			tempServers = (JaxbServers) um.unmarshal(new FileReader(configuration.getServersPath()));
		} catch (IOException e1) {
			ioException = true;
		} catch (Exception ee2) {
			ioException = true;
		}
		
		//2.2. next load the tasks
		try {
			context = JAXBContext.newInstance(JaxbTasks.class);
			um = context.createUnmarshaller();
			tempTasks = (JaxbTasks) um.unmarshal(new FileReader(configuration.getTasksPath()));
		} catch (IOException e1) {
			ioException = true;
		} catch (Exception ee2) {
			ioException = true;
		}
		
		//2.3. last but not least, load the history
		try {
			context = JAXBContext.newInstance(JaxbHistory.class);
			um = context.createUnmarshaller();
			tempHistory = (JaxbHistory) um.unmarshal(new FileReader(configuration.getHistoryPath()));
		} catch (IOException e1) {
			ioException = true;
		} catch (Exception ee2) {
			ioException = true;
		}
		
		if (ioException == true) {
			return false;
		}
		
		//3. conversions
		tasks = new ArrayList<ITask>();
		tasks.addAll(tempTasks.getTaskList());
		servers = new ArrayList<IServer>();
		servers.addAll(tempServers.getServerList());
		history = new ArrayList<ITask>();
		for(Integer i : tempHistory.getTaskHistory()) {
			history.add(tasks.get(i));
		}

		
		//4. initialize Dispatcher and run it
		dispatcher = Dispatcher.Instance();
		dispatcher.worker.initialize(servers, tasks, history);
		dispatcher.ParseWorkloadXML(configuration.getWorkloadPath());
		return true;
	}

	/**
	 * This method initializes jCheckBoxLogger	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxLogger() {
		if (jCheckBoxLogger == null) {
			jCheckBoxLogger = new JCheckBox();
			jCheckBoxLogger.setBounds(new Rectangle(240, 195, 21, 21));
		}
		return jCheckBoxLogger;
	}

	/**
	 * This method initializes jDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	private JDialog getJDialog() {
		if (jDialog == null) {
			jDialog = new JDialog(this);
			jDialog.setSize(new Dimension(176, 113));
			jDialog.setTitle("Error");
			jDialog.setContentPane(getJContentPane1());
		}
		return jDialog;
	}

	/**
	 * This method initializes jContentPane1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane1() {
		if (jContentPane1 == null) {
			jLabel = new JLabel();
			jLabel.setText("Invalid config file path!");
			jLabel.setBounds(new Rectangle(16, 9, 131, 23));
			jContentPane1 = new JPanel();
			jContentPane1.setLayout(null);
			jContentPane1.add(getJButton(), null);
			jContentPane1.add(jLabel, null);
		}
		return jContentPane1;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(40, 40, 81, 27));
			jButton.setText("OK");
			jButton.addActionListener(this);
		}
		return jButton;
	}
}  //  @jve:decl-index=0:visual-constraint="46,22"
