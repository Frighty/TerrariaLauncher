package de.iltisnation.terrarialauncher;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JTextArea;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;
import java.awt.Font;

public class LauncherUI {

	private static final String TERRARIA_SERVER_EXE_RELATIVE_PATH = "\\Steam\\SteamApps\\common\\Terraria\\TerrariaServer.exe";
	private static final String LOCK_FILENAME = "\\lock.ed";
	private static final String STOP_SERVER = "Stop server";
	private static final String START_SERVER = "Start server";
	private static final String TERRARIA_GAME_ID = "steam://rungameid/105600";
	private static final String STEAM_EXE_RELATIVE_PATH = "\\Steam\\Steam.exe";
	private static final String DEFAULT_WORLD_RELATIVE_PATH = "\\Documents\\my games\\Terraria\\Worlds\\Parnassus-_Kabinett.wld";
	private JFrame frame;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JTextArea textArea;
	private JButton btnStartClient;
	
	// Process IO
	private Process serverProcess;
	private OutputStreamWriter osw;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LauncherUI window = new LauncherUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LauncherUI() {
		 try {
			UIManager.setLookAndFeel(
			            UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (getLockFile().exists() && serverProcess != null){
		            int result = JOptionPane.showConfirmDialog(frame,
		                "This will also shutdown the server without saving (though it sometimes saves automatically).\n"
		            		+ "So... do you really want to shutdown?",
		                "Warning",
		                JOptionPane.OK_CANCEL_OPTION );
		            if (result == JOptionPane.OK_OPTION){
		            	try {
							stopServer();
			            	System.exit(0);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
				}
				else
					System.exit(0);
			}
		});
		frame.setBounds(100, 100, 564, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 9;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea("");
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		scrollPane.setViewportView(textArea);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textArea.setEditable(false);
		
		JLabel lblNewLabel = new JLabel("Path to world");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 7;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		frame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		textField_1.setText(System.getenv("USERPROFILE") + DEFAULT_WORLD_RELATIVE_PATH);
		
		btnStartClient = new JButton("Start Client");
		btnStartClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process process = new ProcessBuilder(System.getenv("ProgramFiles(X86)") +
							STEAM_EXE_RELATIVE_PATH, TERRARIA_GAME_ID).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnStartClient = new GridBagConstraints();
		gbc_btnStartClient.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartClient.gridx = 4;
		gbc_btnStartClient.gridy = 3;
		frame.getContentPane().add(btnStartClient, gbc_btnStartClient);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            textField_1.setText(file.getAbsolutePath());
		        }
			}
		});
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowse.gridx = 8;
		gbc_btnBrowse.gridy = 1;
		frame.getContentPane().add(btnBrowse, gbc_btnBrowse);
		
		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		frame.getContentPane().add(lblPassword, gbc_lblPassword);
		
		final JButton btnNewButton = new JButton(START_SERVER);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (getLockFile().exists()){
						stopServer();
						textArea.append("Server stopped.\n");
						btnNewButton.setText(START_SERVER);
						btnStartClient.setEnabled(false);
					} 
					else
					{
						String world = textField_1.getText();
						String password = String.valueOf(passwordField.getPassword());
						if (world.length()==0 || password.length()==0){
							textArea.append("You need to specify a world and a password.\n");
							return;
						}
						startServer(world, password);
						btnNewButton.setText(STOP_SERVER);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 3;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
		File lockFile = getLockFile();
		
		// server already running?
		if (lockFile.exists()){
			// ... locally?
			if (serverProcess != null)
				btnNewButton.setText(STOP_SERVER);
			else
				btnNewButton.setEnabled(false); // prevent duplicate starting
			btnStartClient.setEnabled(true);	
			try {
				BufferedReader br = new BufferedReader(new FileReader(lockFile));
				String ip = br.readLine();
				br.close();
				textArea.setText("Server is up and running. IP: " + ip + "\n");
				copyToclipboard(ip);
			    textArea.append("Added server IP ("+ ip +") to your clipboard!\n");
			} catch (FileNotFoundException e1) {
				textArea.setText("Uuups! Error reading lock file: File not found!\n");
			} catch (IOException e1) {
				textArea.setText("Uuups! Error reading lock file (IO stuff).\n");
			}
		}
		else
		{
			btnStartClient.setEnabled(false);
		}
		
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 3;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 2;
		frame.getContentPane().add(passwordField, gbc_passwordField);
		

		
		
		
		JLabel lblMessages = new JLabel("Messages:");
		GridBagConstraints gbc_lblMessages = new GridBagConstraints();
		gbc_lblMessages.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblMessages.insets = new Insets(0, 0, 5, 5);
		gbc_lblMessages.gridx = 0;
		gbc_lblMessages.gridy = 5;
		frame.getContentPane().add(lblMessages, gbc_lblMessages);
		

	}
	
	private File getLockFile() {
		File worldFolder = new File(textField_1.getText()).getParentFile();
		File lockFile = new File(worldFolder.getAbsolutePath() + LOCK_FILENAME);
		return lockFile;
	}
	
	/**
	 * Starts a Terraria server with the given world and password
	 * @param world	World to be started
	 * @param password Password for the world to be started
	 * @return Returns the external IP
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private void startServer(String world, String password)
			throws IOException, MalformedURLException {
		final String exe = System.getenv("ProgramFiles(X86)") + 
				TERRARIA_SERVER_EXE_RELATIVE_PATH;
		final String worldArg = "-world \"" + world + "\"";
		final String passArg = "-pass " + password;
		Thread t = new Thread(){
			public void run(){
				try {
					serverProcess = Runtime.getRuntime().exec(exe + " " + worldArg + " " + passArg);
					OutputStream os = serverProcess.getOutputStream();
				    osw = new OutputStreamWriter(os);
					InputStream is = serverProcess.getInputStream();
				    InputStreamReader isr = new InputStreamReader(is);
				    BufferedReader br = new BufferedReader(isr);
				    String line;
				    while ((line = br.readLine()) != null && !line.startsWith("Listening on")) {
				      textArea.append(line + "\n");
				    }
				    String ip = writeLockFile();
					
				    copyToclipboard(ip);
				    textArea.append("Added server IP ("+ ip +") to your clipboard!\n");
				    
					btnStartClient.setEnabled(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private String writeLockFile() throws IOException,
					MalformedURLException {
				FileWriter fw = new FileWriter(getLockFile());
				URL whatismyip = new URL("http://checkip.amazonaws.com");
				BufferedReader in = new BufferedReader(new InputStreamReader(
				                whatismyip.openStream()));
				String ip = in.readLine();
				fw.write(ip);
				fw.close();
				return ip;
			}
		};
		t.start();
	}
	
	private void stopServer()
			throws IOException {
		//serverProcess.destroy();
		osw.write("exit\n");
		osw.flush();
		try {
			serverProcess.waitFor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File lockFile = getLockFile();
		if (lockFile.exists())
			lockFile.delete();
	}
	
	private void copyToclipboard(String ip) {
		StringSelection selection = new StringSelection(ip);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}
}
