package org.spacehq.openclassic.server.ui;

import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.spacehq.openclassic.api.OpenClassic;

@SuppressWarnings("rawtypes")
public class ServerFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public JTextPane log;
	public JTextField commandBox;
	public DefaultListModel players = new DefaultListModel();
	public DefaultListModel levels = new DefaultListModel();

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public ServerFrame(final GuiConsoleManager manager) {
		setResizable(false);
		setTitle("OpenClassic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				OpenClassic.getServer().shutdown();
			}
		});

		log = new JTextPane();
		log.setFont(new Font("Dialog", Font.PLAIN, 12));
		log.setEditable(false);
		log.setEditorKit(new HTMLEditorKit());
		log.setStyledDocument(new HTMLDocument());
		// log.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(log);
		scroll.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scroll.setBounds(5, 5, 545, 428);
		contentPane.add(scroll);
		// log.setColumns(10);

		commandBox = new JTextField();
		commandBox.setBounds(5, 437, 545, 27);
		contentPane.add(commandBox);
		commandBox.setColumns(10);

		final JList levels = new JList(this.levels);
		levels.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		levels.setBounds(571, 212, 115, 195);

		final JPopupMenu menu = new JPopupMenu();
		JMenuItem unload = new JMenuItem("Unload Level");
		unload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(OpenClassic.getServer() != null) {
					OpenClassic.getServer().unloadLevel((String) levels.getSelectedValue());
				}
			}
		});

		menu.add(unload);
		levels.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				this.check(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				this.check(e);
			}

			public void check(MouseEvent e) {
				if(e.isPopupTrigger() && levels.locationToIndex(e.getPoint()) != -1 && levels.locationToIndex(e.getPoint()) < levels.getModel().getSize()) {
					levels.setSelectedIndex(levels.locationToIndex(e.getPoint()));
					menu.show(levels, e.getX(), e.getY());
				}
			}
		});

		contentPane.add(levels);

		final JList players = new JList(this.players);
		players.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		players.setBounds(571, 5, 115, 195);

		final JPopupMenu pmenu = new JPopupMenu();
		JMenuItem kick = new JMenuItem("Kick Player");
		kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(OpenClassic.getServer() != null && OpenClassic.getServer().getPlayer((String) players.getSelectedValue()) != null) {
					OpenClassic.getServer().getPlayer((String) players.getSelectedValue()).disconnect("Kicked by console!");
				}
			}
		});

		pmenu.add(kick);
		players.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				this.check(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				this.check(e);
			}

			public void check(MouseEvent e) {
				if(e.isPopupTrigger() && players.locationToIndex(e.getPoint()) != -1 && players.locationToIndex(e.getPoint()) < players.getModel().getSize()) {
					players.setSelectedIndex(players.locationToIndex(e.getPoint()));
					pmenu.show(players, e.getX(), e.getY());
				}
			}
		});

		contentPane.add(players);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(commandBox.getText() != null && commandBox.getText().length() > 0 && OpenClassic.getServer() != null) {
					OpenClassic.getServer().processCommand(ConsoleManager.SENDER, commandBox.getText());
					commandBox.setText("");
				}
			}
		});

		btnSend.setBounds(569, 445, 117, 19);
		contentPane.add(btnSend);
		getRootPane().setDefaultButton(btnSend);

		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsFrame frame = new SettingsFrame();

				frame.serverName.setText(OpenClassic.getGame().getConfig().getString("info.name"));
				frame.motd.setText(OpenClassic.getGame().getConfig().getString("info.motd"));
				frame.port.setText(String.valueOf(OpenClassic.getGame().getConfig().getInteger("options.port")));
				frame.chckbxShowOnServer.setSelected(OpenClassic.getGame().getConfig().getBoolean("options.public"));
				frame.maxPlayers.setText(String.valueOf(OpenClassic.getGame().getConfig().getInteger("options.max-players")));
				frame.chckbxVerifyUsers.setSelected(OpenClassic.getGame().getConfig().getBoolean("options.online-mode"));
				frame.chckbxUseWhitelist.setSelected(OpenClassic.getGame().getConfig().getBoolean("options.whitelist"));
				frame.defaultLevel.setText(OpenClassic.getGame().getConfig().getString("options.default-level"));
				frame.chckbxEnabled.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.enabled"));
				frame.chckbxFalling.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.falling"));
				frame.chckbxFlower.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.flower"));
				frame.chckbxMushroom.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.mushroom"));
				frame.chckbxTree.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.trees"));
				frame.chckbxSponge.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.sponge"));
				frame.chckbxLiquid.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.liquid"));
				frame.chckbxGrass.setSelected(OpenClassic.getGame().getConfig().getBoolean("physics.grass"));
				frame.changePhysics();

				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				frame.setLocation((gd.getDisplayMode().getWidth() - frame.getWidth()) / 2, (gd.getDisplayMode().getHeight() - frame.getHeight()) / 2);
				frame.setVisible(true);
			}
		});

		btnSettings.setBounds(569, 419, 117, 19);
		contentPane.add(btnSettings);
	}
}
