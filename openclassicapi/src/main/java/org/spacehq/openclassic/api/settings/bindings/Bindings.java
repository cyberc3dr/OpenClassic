package org.spacehq.openclassic.api.settings.bindings;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class containing key bindings.
 */
public class Bindings {

	private Map<String, KeyBinding> bindings = new LinkedHashMap<String, KeyBinding>();
	
	/**
	 * Gets all registered bindings.
	 * @return All registered bindings.
	 */
	public Map<String, KeyBinding> getBindings() {
		return new HashMap<String, KeyBinding>(this.bindings);
	}
	
	/**
	 * Gets the binding at the given map index.
	 * @return The binding at the given map index.
	 */
	public KeyBinding getBinding(int index) {
		int i = 0;
		for(KeyBinding binding : this.bindings.values()) {
			if(i == index) {
				return binding;
			}
			
			i++;
		}
		
		return null;
	}
	
	/**
	 * Gets the binding with the given key.
	 * @param key Key of the binding.
	 * @return The binding to get.
	 */
	public KeyBinding getBinding(String key) {
		return this.bindings.get(key);
	}
	
	/**
	 * Registers the given binding.
	 * @param binding Binding to register.
	 */
	public void registerBinding(KeyBinding binding) {
		this.bindings.put(binding.getName(), binding);
	}
	
	/**
	 * Unregisters a binding.
	 * @param binding Binding to unregister.
	 */
	public void unregisterBinding(KeyBinding binding) {
		this.bindings.remove(binding.getName());
	}
	
}
