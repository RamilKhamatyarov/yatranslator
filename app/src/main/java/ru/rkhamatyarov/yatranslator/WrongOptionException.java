package ru.rkhamatyarov.yatranslator;



/**
 * Created by Asus on 18.04.2017.
 */

class WrongOptionException extends Exception {
    WrongOptionException(){
        super("The option is worng: it must be 'langList' or 'translateWords'");
    }
}
