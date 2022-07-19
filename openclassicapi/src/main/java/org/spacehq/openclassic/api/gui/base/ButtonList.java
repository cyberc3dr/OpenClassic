package org.spacehq.openclassic.api.gui.base;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * Represents a list consisting of buttons.
 */
public class ButtonList extends GuiComponent {

	private List<String> contents = new ArrayList<String>();
	private List<String> visible = new ArrayList<String>();
	
	private boolean useSearch;
	private int pages = 0;
	private int index = 0;
	
	private ButtonListCallback callback;
	
	public ButtonList(String name, int x, int y, int width, int height) {
		this(name, x, y, width, height, false);
	}
	
	public ButtonList(String name, int x, int y, int width, int height, boolean search) {
		super(name, x, y, width, height);
		this.useSearch = search;
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		for (int button = 0; button < 5; button++) {
			int offset = ((button - 2) * 48) - 20;
			this.attachComponent(new Button("button" + button, this.getX() + (this.getWidth() / 2 - 200), this.getY() + (this.getHeight() / 2 + offset), "---").setCallback(new ButtonCallback() {
				@Override
				public void onButtonClick(Button button) {
					if(callback != null) {
						callback.onButtonListClick(ButtonList.this, button);
					}
				}
			}));
			
			Button b = this.getComponent("button" + button, Button.class);
			b.setVisible(false);
			b.setActive(false);
		}
		
		this.attachComponent(new Button("backbutton", this.getX() + (this.getWidth() / 2 - 400), this.getY() + (this.getHeight() / 2 - 20), 100, 40, OpenClassic.getGame().getTranslator().translate("gui.list.back")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				index--;
				button.setActive(index > 0);
				getNextButton().setActive(index < pages);
				updateContents();
			}
		}));
		
		this.attachComponent(new Button("nextbutton", this.getX() + (this.getWidth() / 2 + 300), this.getY() + (this.getHeight() / 2 - 20), 100, 40, OpenClassic.getGame().getTranslator().translate("gui.list.next")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				index++;
				button.setActive(index < pages);
				getBackButton().setActive(index > 0);
				updateContents();
			}
		}));
		
		if(this.useSearch) {
			this.attachComponent(new TextBox("search", this.getX() + (this.getWidth() / 2 - 200), this.getY() + (this.getHeight() / 2 + 124)).setCallback(new TextBoxCallback() {
				@Override
				public void onType(TextBox box, String oldText) {
					if(!useSearch) return;
					List<String> cont = new ArrayList<String>();
					for(String content : contents) {
						if(content.toLowerCase().contains(box.getText().toLowerCase())) {
							cont.add(content);
						}
					}
					
					visible = cont;
					index = 0;
					pages = (int) Math.ceil(visible.size() / 5);
					if(pages > 0 && visible.size() > (pages - 1) * 5) {
						getNextButton().setActive(true);
					}
					
					updateContents();
				}
			}));
		}
		
		this.getBackButton().setActive(false);
		this.getNextButton().setActive(false);
	}
	
	/**
	 * Gets the contents of this list.
	 * @return The contents of this list.
	 */
	public List<String> getContents() {
		return this.contents;
	}
	
	/**
	 * Sets the contents of this list.
	 * @param contents The list's new contents.
	 */
	public void setContents(List<String> contents) {
		this.contents = contents;
		this.visible = contents;
		if(this.useSearch) {
			this.getSearchBox().setText("");
		}
		
		this.index = 0;
		this.pages = (int) Math.ceil(this.visible.size() / 5);
		if(this.pages > 0 && this.visible.size() > (this.pages - 1) * 5) {
			this.getNextButton().setActive(true);
		}
		
		this.updateContents();
	}
	
	/**
	 * Gets the visible contents of the list.
	 * @return The list's visible contents.
	 */
	public List<String> getVisibleContents() {
		return new ArrayList<String>(this.visible);
	}
	
	/**
	 * Gets the current page the list is on.
	 * @return The current page.
	 */
	public int getCurrentPage() {
		return this.index;
	}
	
	/**
	 * Sets the current page the list is on.
	 * @param The new page.
	 */
	public void setCurrentPage(int page) {
		this.index = page;
		this.getBackButton().setActive(this.index > 0);
		this.getNextButton().setActive(this.index < this.pages);
		this.updateContents();
	}
	
	/**
	 * Gets the number of pages in this list.
	 * @return The list's page count.
	 */
	public int getPages() {
		return this.pages;
	}
	
	/**
	 * Gets the callback of this button list.
	 * @return This button list's callback.
	 */
	public ButtonListCallback getCallback() {
		return this.callback;
	}
	
	/**
	 * Sets the callback of this button list.
	 * @param callback Callback of this button list.
	 * @return This button list.
	 */
	public ButtonList setCallback(ButtonListCallback callback) {
		this.callback = callback;
		return this;
	}
	
	private void updateContents() {
		for (int curr = this.index * 5; curr < (this.index + 1) * 5; curr++) {
			boolean content = curr <= this.visible.size() - 1 && curr >= 0 && !this.visible.get(curr).equals("");
			int button = curr - this.index * 5;
			this.getButton(button).setActive(content);
			this.getButton(button).setText(content ? this.visible.get(curr) : "-");
			this.getButton(button).setVisible(true);
		}
	}
	
	/**
	 * Gets the button with the given ID. (0 = top of page, 4 = bottom, 5 = back, 6 = next)
	 * @param button Button to look for.
	 * @return The button with the given ID.
	 */
	public Button getButton(int button) {
		return this.getComponent("button" + button, Button.class);
	}
	
	/**
	 * Gets the back button of this list.
	 * @return The list's back button.
	 */
	public Button getBackButton() {
		return this.getComponent("backbutton", Button.class);
	}
	
	/**
	 * Gets the next button of this list.
	 * @return The list's next button.
	 */
	public Button getNextButton() {
		return this.getComponent("nextbutton", Button.class);
	}
	
	/**
	 * Gets the search box of this button list.
	 * @return This button list's search box.
	 */
	public TextBox getSearchBox() {
		return this.getComponent("search", TextBox.class);
	}
	
}
