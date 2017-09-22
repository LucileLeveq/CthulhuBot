package bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/* Fonctionnalit�s :
 * Envoyer la vid�o de Bohort au mot "m�cr�ant"
 * R�pondre "Qu'est-ce que t'as pas compris ?" si "C'est pas faux"
 * */

//TODO : https://github.com/sedmelluq/lavaplayer se servir de �a pour avoir juste l'audio

public class KaamelottListener implements EventListener {
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            User author = e.getAuthor();
            boolean bot = author.isBot(); 
            String message = e.getMessage().getContent();
            
            if (!bot){ //On ne r�pond que si c'est pas un bot
            	
            	// Liste des mots � matcher
	            Pattern mecreant = Pattern.compile("[Mm]�cr�ant"); 
	            Pattern pasfaux = Pattern.compile("[Cc]'est pas faux");
	            
	            // R�actions en fonction du match
	            Matcher m = mecreant.matcher(message);
	            if (m.find( )) {
	            	e.getChannel().sendMessage("https://www.youtube.com/watch?v=RNYOhWOPHS8").queue();
	            }
	            
	            m = pasfaux.matcher(message);
	            if (m.find()) {
	            	e.getChannel().sendMessage("Qu'est-ce que t'as pas compris ?").queue();
	            }
            }
		}
	}

}
