package terrarialauncher;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class LauncherUI {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblServerIp = new JLabel("Server IP");
		GridBagConstraints gbc_lblServerIp = new GridBagConstraints();
		gbc_lblServerIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblServerIp.anchor = GridBagConstraints.EAST;
		gbc_lblServerIp.gridx = 1;
		gbc_lblServerIp.gridy = 2;
		frame.getContentPane().add(lblServerIp, gbc_lblServerIp);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnLoadMine = new JButton("Load mine");
		btnLoadMine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL whatismyip;
				try {
					whatismyip = new URL("http://checkip.amazonaws.com");

					BufferedReader in = new BufferedReader(new InputStreamReader(
					                whatismyip.openStream()));
	
					String ip = in.readLine(); //you get the IP as a String
					System.out.println(ip);
					textField.setText(ip);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnLoadMine = new GridBagConstraints();
		gbc_btnLoadMine.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoadMine.gridx = 3;
		gbc_btnLoadMine.gridy = 2;
		frame.getContentPane().add(btnLoadMine, gbc_btnLoadMine);
		
		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 3;
		frame.getContentPane().add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 3;
		frame.getContentPane().add(passwordField, gbc_passwordField);
		
		JButton btnNewButton = new JButton("Launch");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// run  -password
				try {
					Process process = new ProcessBuilder(
							"C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Terraria\\Terraria.exe").start();//,
							//"-password " + passwordField.getPassword().toString()).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 5;
		frame.getContentPane().add(btnNewButton, gbc_btnNewButton);
	}

}
