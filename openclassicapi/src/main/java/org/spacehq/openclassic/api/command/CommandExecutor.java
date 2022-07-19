package org.spacehq.openclassic.api.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.command.annotation.Command;

/**
 * A class containing command methods to execute.
 */
public abstract class CommandExecutor {

	/**
	 * Gets the method for the given command.
	 * @param command The command to look for.
	 * @return The method for the command.
	 */
	public final Method getCommand(String command) {
		for(Method method : this.getClass().getMethods()) {
			Class<?> params[] = method.getParameterTypes();
			if(params.length == 3 && method.getAnnotation(Command.class) != null) {
				for(String alias : method.getAnnotation(Command.class).aliases()) {
					if(alias.equalsIgnoreCase(command)) return method;
				}
			}
		}
			
		return null;
	}
	
	/**
	 * Gets a list of command methods.
	 * @return A list of command methods.
	 */
	public final List<Method> getCommands() {
		List<Method> result = new ArrayList<Method>();
		
		for(Method method : this.getClass().getMethods()) {
			Class<?> params[] = method.getParameterTypes();
			if(params.length == 3 && method.getAnnotation(Command.class) != null) {
				result.add(method);
			}
		}
		
		return result;
	}
	
}
