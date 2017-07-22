package bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/* Fonctionnalités :
 * Va chercher une citation dans le livre d'or au hasard
 * Explique comment ça marche
 * Va chercher une citation par auteur (TODO)
 * */

public class LivredorListener implements EventListener {
	
	private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private Document document = null;

	private String toString (Element citation){
		String result;
		String type = citation.getAttribute("type");
 	   	String replique = "\"" + citation.getElementsByTagName("replique").item(0).getTextContent() + "\" - "; 
 	   	String auteur = citation.getElementsByTagName("auteur").item(0).getTextContent();
 	   	String auteurbis = "";
 	   	String commentaire = "";
 	   	String date = citation.getElementsByTagName("date").item(0).getTextContent();
 	   	
 	   	if (null!=citation.getElementsByTagName("commentaire").item(0)){ //Check si commentaire
 	   		commentaire = citation.getElementsByTagName("commentaire").item(0).getTextContent() + ", ";
 	   	}

 	   	if (type.equals("dialogue")){ //Si c'est un dialoque, il y a un 2e auteur
 	   		auteurbis = citation.getElementsByTagName("auteur").item(1).getTextContent() + ", ";
 	   		result = replique + auteur + " et " + auteurbis + commentaire + date;
 	   	} else {
 	   		result = replique + auteur + ", " + commentaire + date;
 	   	}
 	   	
		return result;
	}
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            User author = e.getAuthor();
            boolean bot = author.isBot(); 
            String message = e.getMessage().getContent();
            
            if (!bot){ //On ne répond que si c'est pas un bot
            	
            	Pattern livredor = Pattern.compile("!livredor");
            	Pattern livredorauteur = Pattern.compile("auteur=([A-Za-zéêèôîïëüö]*)$");
            	Pattern livredorhelp = Pattern.compile("!helplivredor");
            	
            	Matcher m = livredor.matcher(message);
            	if (m.find( )) {
	        	   
	        	   try { //Initialisation du parser
						final DocumentBuilder builder = factory.newDocumentBuilder();
						document = builder.parse(new File("src\\livredor.xml")); //Adresse relative
					} catch (ParserConfigurationException | SAXException | IOException e1) {
						e1.printStackTrace();
						System.out.println("Une erreur est survenue lors de l'ouverture du document livredor.xml");
			            return;
					}
	        	   
	        	   NodeList citations = document.getElementsByTagName("citation"); //Récupération de toutes les citations
	        	   Random r = new Random();
	        	   int random;
	        	   Element citation;
	        	   
	        	   m = livredorauteur.matcher(message);
	        	   if (m.find()) { //Citations d'auteur particulier
	        		   String dequi = m.group(1); //Récup' de l'auteur
	        		   
	        		   List<Element> citationsde = new ArrayList<Element>();
	        		   
	        		   for (int i = 0; i < citations.getLength(); i++){ //Pour chaque citation
	        			   Element c = (Element)citations.item(i);
	        			   
	        			   //Si l'auteur correspond on l'ajoute à la liste de citations potentielles à renvoyer
	        			   if (c.getElementsByTagName("auteur").item(0).getTextContent().equals(dequi)){
	        				   citationsde.add(c);
	        			   }
	        			   
	        			   if (c.getAttribute("type").equals("dialogue")){ //On vérifie les autres auteurs
	        				   if (c.getElementsByTagName("auteur").item(1).getTextContent().equals(dequi)){
		        				   citationsde.add(c);
		        			   }
	        			   }
	        		   }
	        		   
		        	   random = r.nextInt(citationsde.size());
		        	   citation = citationsde.get(random); //On choisit une citation dans la liste au hasard	        		   
	        		   
	        	   } else {
	        		   //Si pas d'auteur, on prend une citation au pif
		        	   random = r.nextInt(citations.getLength()); 
		        	   citation = (Element) citations.item(random);
	        	   }
	        	   
	        	   e.getChannel().sendMessage(toString(citation)).queue();
            	}
            	
            	m = livredorhelp.matcher(message);
            	if (m.find( )) {
            		e.getChannel().sendMessage("Livre d'or :\n"
	            			+ "Tapez \"!livredor\" pour obtenir une citation au hasard\n"
	            			+ "Tapez \"!livredor auteur=Prénom\" (n'oubliez pas la majuscule) pour obtenir une citation d'une personne en particulier\n"
	            			+ "").queue();
            	}
            }
		}
	}

}
