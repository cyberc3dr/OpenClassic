package org.spacehq.openclassic.server.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.spacehq.openclassic.api.OpenClassic;

public class SettingsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public JTextField serverName;
	public JTextField motd;
	public JTextField defaultLevel;
	public JTextField port;
	public JTextField maxPlayers;

	public JCheckBox chckbxVerifyUsers;
	public JCheckBox chckbxShowOnServer;
	public JCheckBox chckbxUseWhitelist;

	public JCheckBox chckbxEnabled;
	public JCheckBox chckbxLiquid;
	public JCheckBox chckbxGrass;
	public JCheckBox chckbxTree;
	public JCheckBox chckbxFalling;
	public JCheckBox chckbxFlower;
	public JCheckBox chckbxMushroom;
	public JCheckBox chckbxSponge;
	private JButton btnNewButton;
	private JButton btnClose;

	/**
	 * Create the frame.
	 */
	public SettingsFrame() {
		setResizable(false);
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 531);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServerName = new JLabel("Server name:");
		lblServerName.setBounds(12, 0, 101, 27);
		contentPane.add(lblServerName);

		JLabel lblMotd = new JLabel("MOTD:");
		lblMotd.setBounds(12, 29, 70, 25);
		contentPane.add(lblMotd);

		JLabel lblMaxPlayers = new JLabel("Max Players:");
		lblMaxPlayers.setBounds(12, 56, 101, 25);
		contentPane.add(lblMaxPlayers);

		JLabel lblDefaultLevel = new JLabel("Default Level:");
		lblDefaultLevel.setBounds(12, 83, 101, 25);
		contentPane.add(lblDefaultLevel);

		JLabel lblNewLabel = new JLabel("Port:");
		lblNewLabel.setBounds(12, 108, 70, 27);
		contentPane.add(lblNewLabel);

		chckbxVerifyUsers = new JCheckBox("Verify Users");
		chckbxVerifyUsers.setBounds(12, 143, 129, 23);
		contentPane.add(chckbxVerifyUsers);

		chckbxShowOnServer = new JCheckBox("Show on Server List");
		chckbxShowOnServer.setBounds(12, 170, 175, 23);
		contentPane.add(chckbxShowOnServer);
		
		chckbxUseWhitelist = new JCheckBox("Use Whitelist");
		chckbxUseWhitelist.setBounds(12, 197, 129, 23);
		contentPane.add(chckbxUseWhitelist);

		serverName = new JTextField();
		serverName.setBounds(119, 0, 195, 29);
		contentPane.add(serverName);
		serverName.setColumns(10);

		motd = new JTextField();
		motd.setBounds(119, 29, 317, 27);
		contentPane.add(motd);
		motd.setColumns(10);

		maxPlayers = new JTextField();
		maxPlayers.setBounds(119, 56, 114, 27);
		contentPane.add(maxPlayers);
		maxPlayers.setColumns(10);

		defaultLevel = new JTextField();
		defaultLevel.setBounds(119, 83, 114, 27);
		contentPane.add(defaultLevel);
		defaultLevel.setColumns(10);

		port = new JTextField();
		port.setBounds(119, 110, 114, 27);
		contentPane.add(port);
		port.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(22, 255, 211, 233);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPhysics = new JLabel("Physics");
		lblPhysics.setBounds(79, 12, 70, 15);
		panel.add(lblPhysics);

		chckbxEnabled = new JCheckBox("Enabled");
		chckbxEnabled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePhysics();
			}
		});

		chckbxEnabled.setBounds(8, 36, 91, 23);
		panel.add(chckbxEnabled);

		chckbxLiquid = new JCheckBox("Liquid");
		chckbxLiquid.setBounds(8, 63, 129, 23);
		panel.add(chckbxLiquid);

		chckbxGrass = new JCheckBox("Grass");
		chckbxGrass.setBounds(8, 90, 129, 23);
		panel.add(chckbxGrass);

		chckbxTree = new JCheckBox("Trees");
		chckbxTree.setBounds(8, 117, 129, 23);
		panel.add(chckbxTree);

		chckbxFalling = new JCheckBox("Falling");
		chckbxFalling.setBounds(8, 144, 129, 23);
		panel.add(chckbxFalling);

		chckbxFlower = new JCheckBox("Flower");
		chckbxFlower.setBounds(8, 171, 129, 23);
		panel.add(chckbxFlower);

		chckbxMushroom = new JCheckBox("Mushroom");
		chckbxMushroom.setBounds(8, 198, 129, 23);
		panel.add(chckbxMushroom);

		chckbxSponge = new JCheckBox("Sponge");
		chckbxSponge.setBounds(119, 35, 84, 23);
		panel.add(chckbxSponge);

		btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenClassic.getServer().getConfig().setValue("info.name", serverName.getText());
				OpenClassic.getServer().getConfig().setValue("info.motd", motd.getText());

				try {
					OpenClassic.getServer().getConfig().setValue("options.port", Integer.parseInt(port.getText()));
				} catch(NumberFormatException ex) {
					OpenClassic.getServer().getConfig().setValue("options.port", 25565);
				}

				OpenClassic.getServer().getConfig().setValue("options.public", chckbxShowOnServer.getModel().isSelected());

				try {
					OpenClassic.getServer().getConfig().setValue("options.max-players", Integer.parseInt(maxPlayers.getText()));
				} catch(NumberFormatException ex) {
					OpenClassic.getServer().getConfig().setValue("options.max-players", 20);
				}

				OpenClassic.getServer().getConfig().setValue("options.online-mode", chckbxVerifyUsers.isSelected());
				OpenClassic.getServer().getConfig().setValue("options.whitelist", chckbxUseWhitelist.isSelected());
				OpenClassic.getServer().getConfig().setValue("options.default-level", defaultLevel.getText());
				OpenClassic.getServer().getConfig().setValue("physics.enabled", chckbxEnabled.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.falling", chckbxGrass.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.flower", chckbxFlower.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.mushroom", chckbxMushroom.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.trees", chckbxTree.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.sponge", chckbxSponge.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.liquid", chckbxLiquid.isSelected());
				OpenClassic.getServer().getConfig().setValue("physics.grass", chckbxGrass.isSelected());
			}
		});

		btnNewButton.setBounds(319, 434, 117, 25);
		contentPane.add(btnNewButton);

		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		btnClose.setBounds(319, 463, 117, 25);
		contentPane.add(btnClose);
	}

	public void changePhysics() {
		chckbxLiquid.setEnabled(chckbxEnabled.isSelected());
		chckbxGrass.setEnabled(chckbxEnabled.isSelected());
		chckbxTree.setEnabled(chckbxEnabled.isSelected());
		chckbxFalling.setEnabled(chckbxEnabled.isSelected());
		chckbxFlower.setEnabled(chckbxEnabled.isSelected());
		chckbxMushroom.setEnabled(chckbxEnabled.isSelected());
		chckbxSponge.setEnabled(chckbxEnabled.isSelected());
	}
}
