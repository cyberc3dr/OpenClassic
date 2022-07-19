package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.block.CustomBlockMessage;

public class CustomBlockMessageHandler extends MessageHandler<CustomBlockMessage> {

	@Override
	public void handle(ClassicSession session, Player player, CustomBlockMessage message) {
		/* List<Model> models = new ArrayList<Model>();
		models.add(message.getBlock().getModel());
		models.addAll(message.getBlock().getOutwardModels().keySet());
		for(Model model : models) {
			for(Quad quad : model.getQuads()) {
				if(!quad.getTexture().getParent().isInJar()) {
					File file = new File(OpenClassic.getGame().getDirectory(), "cache/" + GeneralUtils.getMinecraft().server + "/" + message.getBlock().getId() + ".png");
					if(!file.exists()) {
						if(!file.getParentFile().exists()) {
							try {
								file.getParentFile().mkdirs();
							} catch(SecurityException e) {
								e.printStackTrace();
								continue;
							}
						}
	
						try {
							file.createNewFile();
						} catch(Exception e) {
							e.printStackTrace();
							continue;
						}
	
						OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("http.downloading"), file.getName()));
	
						byte[] data = new byte[4096];
						DataInputStream in = null;
						DataOutputStream out = null;
	
						try {
							in = new DataInputStream((new URL(quad.getTexture().getParent().getTexture())).openStream());
							out = new DataOutputStream(new FileOutputStream(file));
	
							int length = 0;
							while(OpenClassic.getGame().isRunning()) {
								length = in.read(data);
								if(length < 0) break;
								out.write(data, 0, length);
							}
						} catch(IOException e) {
							e.printStackTrace();
						} finally {
							IOUtils.closeQuietly(in);
							IOUtils.closeQuietly(out);
						}
	
						OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("http.downloaded"), file.getName()));
					}
	
					quad.getTexture().getParent().setTexture(file.getPath());
				}
			}
		} */

		Blocks.register(message.getBlock());
	}

}
