package placeholder;

import Functionality.textFunctions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testTextFunctions {
    textFunctions temp = new textFunctions();

    @Test
    public void simpleLongRunning2(){
        assertEquals("The Ravens have been one of the more successful franchises ,since their inception compiling a record of.",temp.fixLongRunningSentence("The Ravens have been one of the more successful franchises since their inception compiling a record of."));
    }

    @Test
    public void simpleFixSpelling3(){
        assertEquals("Catt eat fish",temp.fixSpellingIssues("Catt eat fush"));
    }

    @Test
    public void simpleFixSpelling(){
        assertEquals("this should surreptitious",temp.fixSpellingIssues("thiss shsould surreptittious"));
    }

    @Test
    public void simpleFixSpelling2(){
        assertEquals("This should be very surreptitious to our users",temp.fixSpellingIssues("Thiiss shuld be verry surreptittious tp our users"));
    }

    @Test
    public void simpleFixFullStop(){

        assertEquals("thus notice",temp.fixMissingFullStop("thus Notice"));
    }



    @Test
    public void simple2FixFullStop(){
        assertEquals("thus. Notice",temp.fixMissingFullStop("thus. notice"));
    }


    @Test
    public void simpleFixCapitalInString() {assertEquals("notice this",temp.fixCapitalLettersInString("nOTICE tHIS"));}

    @Test
    public void ComplexCapitalInString() {
        assertEquals("In this story, we have a wolf, and a bear. So.",temp.fixCapitalLettersInString("IN tHIs Story, we Have a wOlf, aNd a bEAr. So."));}
    @Test
    public void ThreeFuncsInvolved(){
        assertEquals("this should surreptitious",temp.fixSpellingIssues("thiss shsould surreptittious"));
    }

}
