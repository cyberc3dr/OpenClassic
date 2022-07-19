package org.spacehq.openclassic.client.gui;

import org.apache.commons.io.IOUtils;
import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.PasswordTextBox;
import org.spacehq.openclassic.api.gui.base.StateButton;
import org.spacehq.openclassic.api.gui.base.TextBox;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.util.HTTPUtil;
import org.spacehq.openclassic.client.util.Server;
import org.spacehq.openclassic.client.util.ServerDataStore;
import org.spacehq.openclassic.client.util.cookie.Cookie;
import org.spacehq.openclassic.client.util.cookie.CookieList;
import org.spacehq.openclassic.game.util.InternalConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.URLEncoder;

public class LoginScreen extends GuiComponent {

	public LoginScreen() {
		super("loginscreen");
	}

	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("login", this.getWidth() / 2 - 200, this.getHeight() / 4 + this.getHeight() / 2, 196, 40, OpenClassic.getGame().getTranslator().translate("gui.login.login")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				String user = getComponent("username", TextBox.class).getText();
				String pass = getComponent("password", TextBox.class).getText();

				if(getComponent("remember", StateButton.class).getState().equals(OpenClassic.getGame().getTranslator().translate("gui.yes"))) {
					BufferedWriter writer = null;
					try {
						writer = new BufferedWriter(new FileWriter(getLoginFile(true)));
						writer.write(user);
						writer.newLine();
						writer.write(pass);
					} catch(IOException e) {
						OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("gui.login.fail-create"));
						e.printStackTrace();
					} finally {
						IOUtils.closeQuietly(writer);
					}
				} else if(getLoginFile(false).exists()) {
					getLoginFile(false).delete();
				}

				OpenClassic.getClient().getProgressBar().setVisible(true);
				OpenClassic.getClient().getProgressBar().setTitle(OpenClassic.getGame().getTranslator().translate("progress-bar.loading"));
				OpenClassic.getClient().getProgressBar().setSubtitle(OpenClassic.getGame().getTranslator().translate("gui.login.logging-in"));
				OpenClassic.getClient().getProgressBar().setProgress(-1);
				OpenClassic.getClient().getProgressBar().render();
				if(!auth(user, pass)) {
					getComponent("title", Label.class).setText(Color.RED + OpenClassic.getGame().getTranslator().translate("gui.login.failed"));
					OpenClassic.getClient().getProgressBar().setVisible(false);
					return;
				}

				OpenClassic.getClient().getProgressBar().setVisible(false);
				OpenClassic.getClient().setActiveComponent(new MainMenuScreen());
			}
		}));
		
		this.attachComponent(new Button("playoffline", this.getWidth() / 2 + 4, this.getHeight() / 4 + this.getHeight() / 2, 196, 40, OpenClassic.getGame().getTranslator().translate("gui.login.play-offline")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new MainMenuScreen());
			}
		}));
		
		this.attachComponent(new StateButton("remember", this.getWidth() / 2 - 200, this.getHeight() / 4 + this.getHeight() / 2 + 48, OpenClassic.getGame().getTranslator().translate("gui.login.remember")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				((StateButton) button).setState(((StateButton) button).getState().equals(OpenClassic.getGame().getTranslator().translate("gui.yes")) ? OpenClassic.getGame().getTranslator().translate("gui.no") : OpenClassic.getGame().getTranslator().translate("gui.yes"));
			}
		}));
		
		this.attachComponent(new TextBox("username", this.getWidth() / 2 - 200, this.getHeight() / 2 - 20, 64));
		this.attachComponent(new PasswordTextBox("password", this.getWidth() / 2 - 200, this.getHeight() / 2 + 32, 64));
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, OpenClassic.getGame().getTranslator().translate("gui.login.enter"), true));
		this.attachComponent(new Label("enteruser", this.getWidth() / 2 - 304, this.getHeight() / 2 - 12, OpenClassic.getGame().getTranslator().translate("gui.login.user"), true));
		this.attachComponent(new Label("enterpass", this.getWidth() / 2 - 304, this.getHeight() / 2 + 40, OpenClassic.getGame().getTranslator().translate("gui.login.pass"), true));
		
		this.getComponent("remember", StateButton.class).setState(this.getLoginFile(false).exists() ? OpenClassic.getGame().getTranslator().translate("gui.yes") : OpenClassic.getGame().getTranslator().translate("gui.no"));
		if(this.getLoginFile(false).exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(this.getLoginFile(true)));
				String line = "";
				while((line = reader.readLine()) != null) {
					if(this.getComponent("username", TextBox.class).getText().equals("")) {
						this.getComponent("username", TextBox.class).setText(line);
					} else if(this.getComponent("password", TextBox.class).getText().equals("")) {
						this.getComponent("password", TextBox.class).setText(line);
						break;
					}
				}
			} catch(IOException e) {
				OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("gui.login.fail-check"));
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(reader);
			}
		} else {
			this.getComponent("login", Button.class).setActive(false);
		}
	}

	@Override
	public void onKeyPress(char c, int key) {
		super.onKeyPress(c, key);
		this.getComponent("login", Button.class).setActive(this.getComponent("username", TextBox.class).getText().length() > 0 && this.getComponent("password", TextBox.class).getText().length() > 0);
	}

	private File getLoginFile(boolean create) {
		File file = new File(OpenClassic.getClient().getDirectory(), ".login");
		if(!file.exists() && create) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("gui.login.fail-create"));
				e.printStackTrace();
			}
		}

		return file;
	}

	public static boolean auth(String username, String password) {
		CookieList cookies = new CookieList();
		CookieHandler.setDefault(cookies);
		String result = "";

		HTTPUtil.fetchUrl(InternalConstants.MINECRAFT_URL_HTTPS + "login", "", InternalConstants.MINECRAFT_URL_HTTPS);

		try {
			result = HTTPUtil.fetchUrl(InternalConstants.MINECRAFT_URL_HTTPS + "login", "username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"), InternalConstants.MINECRAFT_URL_HTTPS);
		} catch(UnsupportedEncodingException e) {
			OpenClassic.getLogger().severe("UTF-8 not supported!");
			return false;
		}

		Cookie cookie = cookies.getCookie(InternalConstants.MINECRAFT_URL_HTTPS, "PLAY_SESSION");
		if(cookie != null) result = HTTPUtil.fetchUrl(InternalConstants.MINECRAFT_URL_HTTPS, "", InternalConstants.MINECRAFT_URL_HTTPS + "login");

		if(result.contains("Logged in as")) {
			((ClientPlayer) OpenClassic.getClient().getPlayer()).setName(result.substring(result.indexOf("Logged in as ") + 13, result.indexOf(" | ")));
			parseServers(HTTPUtil.rawFetchUrl(InternalConstants.MINECRAFT_URL_HTTPS + "classic/list", "", InternalConstants.MINECRAFT_URL_HTTPS));
			return true;
		}
		
		return false;
	}

	private static void parseServers(String data) {
		int index = data.indexOf("<a href=\"");
		while(index != -1) {
			index = data.indexOf("classic/play/", index);
			if(index == -1) {
				break;
			}

			String id = data.substring(index + 13, data.indexOf("\"", index));
			index = data.indexOf(">", index) + 1;
			String name = data.substring(index, data.indexOf("</a>", index)).replaceAll("&amp;", "&").replaceAll("&hellip;", "...");
			index = data.indexOf("<td>", index) + 4;
			String users = data.substring(index, data.indexOf("</td>", index));
			index = data.indexOf("<td>", index) + 4;
			String max = data.substring(index, data.indexOf("</td>", index));

			Server s = new Server(name, Integer.valueOf(users).intValue(), Integer.valueOf(max).intValue(), id);
			ServerDataStore.addServer(s);
		}
	}

	public static String toHex(byte[] data) {
		StringBuffer buffer = new StringBuffer(data.length * 2);

		for(int index = 0; index < data.length; index++) {
			int hex = data[index] & 0xFF;
			if(hex < 16) {
				buffer.append("0");
			}

			buffer.append(Long.toString(hex, 16));
		}

		return buffer.toString();
	}

}
