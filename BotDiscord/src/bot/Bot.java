package bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot implements EventListener {

	private JDA jda;
	private boolean stop = false; 
    
    //Pour stopper le bot si besoin
	@Override
	public void onEvent(Event event) {
		if (event instanceof MessageReceivedEvent) {
			MessageReceivedEvent e = (MessageReceivedEvent) event;
			String message = e.getMessage().getContent();
			Pattern stopit = Pattern.compile("!stop");
			Matcher m = stopit.matcher(message);
			if (m.find()){ //Si le message contient "!stop"
				jda.shutdown(); //On arrête le bot
				stop = true;
			}
		}
	}
    
	//Paramètre et lance le bot
    private Bot (String token) {
        try {
        	
        	jda = (JDA) new JDABuilder(AccountType.BOT).setToken(token) //Token passé en paramètre dans le main
				.setBulkDeleteSplittingEnabled(false).buildBlocking();
        	jda.addEventListener(this); //Ajout du listener de stop
        	jda.addEventListener(new MainListener()); //Ajout du listener débile x)
        	jda.addEventListener(new PoliteListener()); //Ajout du listener de politesse
        	jda.addEventListener(new LivredorListener()); //Ajout du listener livre d'or
        	jda.addEventListener(new KaamelottListener()); //Ajout du listener Kaamelott
        } catch (IllegalArgumentException | LoginException | InterruptedException | RateLimitedException e) {
        	e.printStackTrace();
        	System.out.println("Une erreur est survenue ; veuillez vérifier le token ou votre connection internet.");
        	System.out.println(token);
        	return;
        }
        
    	int i; //Nombre de channels sur lequel le bot est actif
        System.out.println("Connecté comme : " + jda.getSelfUser().getName()); //Affichage du nom du bot
        System.out.println("Le bot est autorisé sur " + (i = jda.getGuilds().size()) + " serveur" + (i > 1 ? "s" : "")); //Serveur(s)
        
        List<TextChannel> chan = jda.getTextChannels();
        for (TextChannel c : chan){
        	//System.out.println("Channel - Name:"+c.getName()+" ID:"+c.getId()); //Affiche tous les salons où il est présent (nom + ID)
        	
        	if (c.getName().equals("general")) //Message de salutation sur general
        		c.sendMessage("Salut tout le monde ! Cthulhu est dans la place ! \n"
        				+ "Pour expérimenter mon option livre d'or, tapez \"!helplivredor\" \n"
        				+ "Pour me faire taire, tapez \"Silence Cthulhu\"\n"
        				+ "Pour plus d'info tapez \"!info\"").queue();
        }
        
        while (!stop) { //Tant qu'on ne le stoppe pas
            Scanner scanner = new Scanner(System.in); //Scanne la console
            String cmd = scanner.next(); //Retranscrit en string
            if (cmd.equalsIgnoreCase("stop")) { //Si = stop
                jda.shutdown(); //On arrête le bot
                stop = true;
            }
            scanner.close(); //On ferme le scan
        }
    }

    public static void main (String[] args) {
        if (args.length < 1) {
            System.out.println("Veuillez indiquer le token du bot.");
        } else {
            new Bot(args[0]);
        }
    }

	
}
