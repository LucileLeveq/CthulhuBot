package bot;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.hooks.EventListener;

/* Fonctionnalités :
 * Repérer "perd(*)", "jeu(x)" et "game(s)"
 * Repérer l'Allemand
 * Répondre "Pong !" à un ping
 * Répondre "C'est toujours meilleur qu'une pizza à l'ananas." quand on capte "dégueulasse"
 * Répondre aléatoirement "Toi-même, 'spèce de ..." 1 fois sur 50 où le "..." est le dernier mot de plus de 3
 * caractètres de la phrase (en excluant les mots en -er, -ez et -ir)
 * Répondre à "Humain"
 * Message d'accueil si !help
 * Liste des actions si !info
 * */

public class MainListener implements EventListener {
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            User author = e.getAuthor();
            boolean bot = author.isBot(); 
            String message = e.getMessage().getContent();
            
            if (!bot){ //On ne répond que si c'est pas un bot
            	
            	Random r = new Random();
            	int random = r.nextInt(50);
            	
            	
            	// Liste des mots à matcher
	            Pattern ping = Pattern.compile("^[Pp][Ii][Nn][Gg][ ,.].*|^[Pp][Ii][Nn][Gg]$| [Pp][Ii][Nn][Gg][ ,.].*| [Pp][Ii][Nn][Gg]$");
	            Pattern jeu = Pattern.compile(" [Jj][Ee][Uu][Xx]?[ .?!]| [Jj][Ee][Uu][Xx]?[.!?]?$|^[Jj][Ee][Uu][Xx]?[ .?!]|^[Jj][Ee][Uu][Xx]?[.?!]?$|[Gg][Aa][Mm][Ee][Ss]? |[Gg][Aa][Mm][Ee][Ss]?$|[Pp][Ee][Rr][Dd][UuRrSs]?");
	            Pattern allemand = Pattern.compile("^[Dd]ie | [Dd]ie |[Kk]artoffeln?|schön| [Dd]er |^[dD]er | [Dd]as |^[dD]as |danke|guten?|nacht| ich |^[iI]ch | bin | ein |^[Ee]in ");
	            Pattern degueu = Pattern.compile("[Dd][Eeé][Gg][uU][Ee][Uu]");
	            Pattern toimeme = Pattern.compile("^.* ([A-Za-zéêèôîïëüö]{4,}).*$");
	            Pattern info = Pattern.compile("!info");
	            Pattern help = Pattern.compile("!help");
	            Pattern humain = Pattern.compile("([Hh]umain)");

	            
	            // Réactions en fonction du match
	            Matcher m = ping.matcher(message);
	            if (m.find( )) {
	                e.getChannel().sendMessage("Pong !").queue();
	            }
	            
	            m = humain.matcher(message);
	            if (m.find()) {
	            	String hum = m.group(1);
	            	e.getChannel().sendMessage("\""+hum+"\" ? Ah oui, cette race de créatures qui fait partie de mes esclaves !").queue();
	            }
	            
	            if (random == 0){ // Une fois sur 50 on balance une connerie.
	            	m = toimeme.matcher(message);
	            	if (m.find()){
	            		String wtf = "Toi-même, 'spèce d";
	            		String suite = m.group(1);
			            Pattern verbe = Pattern.compile("[ei]r|ez$");
			            m = verbe.matcher(suite);
			            if (!m.find()){
			            	Pattern voyelles = Pattern.compile("^[éêèîêûïüëöaeiouyAEIOUY]");
		            		m = voyelles.matcher(suite);
		            		if (m.find()){
		            			wtf = wtf + "'" + suite + " !";
		            		} else {
		            			wtf = wtf + "e " + suite + " !";
		            		}
		            		e.getChannel().sendMessage(wtf).queue();
			            }
	            	}
	            }
	            
	            m = jeu.matcher(message);
	            if (m.find( )) {
	            	String randomPerdu;
	            	random = r.nextInt(3);
	            	switch (random) {
		            	case 0 :
		            		randomPerdu = "Vous venez de *perdre*. Cordialement.";
		            		break;
		            	case 1 : 
		            		randomPerdu = "C'est dommage de **perdre** !";
		            		break;
		            	case 2 :
		            		randomPerdu = "Vous avez tous perdu ! MUHAHAHAHA !";
		            		break;
		            	default :
		            		randomPerdu = "Perdu.";
	            	}	
	            	
	                e.getChannel().sendMessage(randomPerdu).queue();
	            }
	           
	            m = allemand.matcher(message);
	            if (m.find( )){
	            	String randomGerman;
	            	random = r.nextInt(5);
	            	switch (random) {
		            	case 0 :
		            		randomGerman = "Deutschland über alles !";
		            		break;
		            	case 1 :
		            		randomGerman = "Ein Volk, ein Reich, ein Kartoffel !";
		            		break;
		            	case 2 : 
		            		randomGerman = "Godwin would be proud.";
		            		break;
		            	case 3 : 
		            		randomGerman = "Heil Kartoffeln !";
		            		break;
		            	case 4 :
		            		randomGerman = "Achtung !";
		            		break;
		            	default :
		            		randomGerman = "ACHTUNG!";
	            	}
	            	
	            	e.getChannel().sendMessage(randomGerman).queue();
	            }
	            
	            m = degueu.matcher(message);
	            if (m.find( )) {
	                e.getChannel().sendMessage("C'est toujours mieux qu'une pizza à l'ananas.").queue();
	            }
	            
	            m = info.matcher(message);
	            if (m.find()) {
	            	e.getChannel().sendMessage("Fonctionnalités :\n"
	            			+ "Repérer \"perd(*)\", \"jeu(x)\" et \"game(s)\"\n"
	            			+ "Repérer l'Allemand\n"
	            			+ "Répondre à un ping\n"
	            			+ "Répondre à \"dégueulasse\"\n"
	            			+ "Répondre aléatoirement \"Toi-même, 'spèce de ...\" 1 fois sur 50 où le \"...\" est le dernier mot de plus de 3 caractères de la phrase (en excluant les mots en -er, -ez et -ir)\n"
	            			+ "Répondre à \"C'est pas faux\"\n"
	            			+ "Répondre à \"mécréant\"\n"
	            			+ "Répondre à \"humain\"\n"
	            			+ "Détecter les mots grossiers \"con\", \"fuck\" et \"merde\"\n"
	            			+ "Répondre quand on parle de lui, répond à un merci qui lui est adressé\n"
	            			+ "Répondre si on inverse le T et le H en début de Cthulhu\n"
	            			+ "Répondre à un \"bonne nuit\"\n"
	            			+ "Se tait 15 minutes lors d'un \"Silence Cthulhu\"\n"
	            			+ "Se vexe 15 minutes lors d'un \"Ta gueule Cthulhu\"\n"
	            			+ "Fonctionnalité livre d'or (tapez \"!helplivredor\" pour plus d'informations)").queue();
	            }
	            
	            m = help.matcher(message);
	            if (m.find()) {
	            	e.getChannel().sendMessage("Salut tout le monde ! Cthulhu est dans la place ! \n"
	        				+ "Pour expérimenter mon option livre d'or, tapez \"!helplivredor\" \n"
	        				+ "Pour me faire taire, tapez \"Silence Cthulhu\"\n"
	        				+ "Pour plus d'info tapez \"!info\"").queue();
	            }
            }   
        }
	}
}
