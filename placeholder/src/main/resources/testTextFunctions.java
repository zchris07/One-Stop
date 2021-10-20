package placeholder_app;
import org.junit.jupiter.api.Test;
import services.textFunctions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testTextFunctions {
    textFunctions temp = new textFunctions();

    @Test
    public void simpleFixSpelling(){
        assertEquals("this should surreptitious",temp.fixSpellingIssues("thiss shsould surreptittious"));
    }

    @Test
    public void simpleFixSpelling2(){
        assertEquals("This should surreptitious",temp.fixSpellingIssues("Thiss shsould surreptittious"));
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
