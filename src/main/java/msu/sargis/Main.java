package msu.sargis;

import msu.sargis.player.Player;
import msu.sargis.selenium.SeleniumClient;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        SeleniumClient seleniumClient = new SeleniumClient();
        Player player = Player.getInstance();
        while (true){
            boolean thereIsVisa = seleniumClient.thereIsVisa();
            System.out.println("thereIsVisa = " + thereIsVisa);
            if (thereIsVisa){
                player.playSound();
            }
            int randomInt = random.nextInt(2);
            randomInt = randomInt + 14;
            Thread.sleep(randomInt * 1000);
        }
    }
}
