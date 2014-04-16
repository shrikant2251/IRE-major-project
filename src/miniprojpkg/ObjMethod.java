package miniprojpkg;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

import java.util.Stack;
import java.util.StringTokenizer;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ObjMethod extends DefaultHandler
{
    public static enum PageType
    {
    TITLE('t'), INFOBOX('i'), CATEGORY('c'), ELINK('l'), REFERENCE('r'), BODY('b');
    private char value;
        private PageType(char v)
        {
           value = v;
        }
        public char getValue()
        {
           return value;
        }
    };
//    int tokenCtr = 0;
//    int fullCount= 0;
    public FileWriter tWriter;
    public StringBuilder tBuilder = new StringBuilder();
    public boolean tFirst = true;
    public static final int SUCCESS_EXECUTION = 0;
    public static final int MISSING_XMLFILE_ARGUMENT = 1;
    public static final int INPUT_XMLFILE_NOTFOUND = 2;
    public String indexDir = new String();
    File inpXmlFile;
    private int page_count = 0;    
    private int local_page_count = 0;
    public int page_id;
    public int revision_id;
    public int contributor_id;
    public boolean revisionStarted;
    public boolean contributorStarted;
    public boolean idStarted;
    public boolean titleStarted;
    public boolean textStarted;
    public StringBuilder titleStr = new StringBuilder("");
    public StringBuilder textStr = new StringBuilder("");
    public StringBuilder infoStr = new StringBuilder("");
    public StringBuilder bodyStr = new StringBuilder("");
    public StringBuilder refStr = new StringBuilder("");
    public StringBuilder catStr  = new StringBuilder("");
    public StringBuilder elinkStr = new StringBuilder("");
    public boolean textParsing;
    public int chunkSize = 100000;
    PageType pageType;
    HashMap btsHash = new HashMap();
    HashMap revBtsHash = new HashMap();
    TreeMap wordHash = new TreeMap();
    char dlimitc[] = {';', ',', '\b', '\t', '-', ':', ' ', '!', '(', 
                      ')', '[', ']', '{', '}', '\\', '/', '<', '>', 
                      '\n', '.', '=', ':', ',', '*', '|', '"', '\'', 
                      '#', '$', '^', '*', '&', '+', '_', '?'};
    String delimiters = new String(dlimitc);

    TreeMap stopWords = new TreeMap();
    Stemmer stemmer = new Stemmer();
    
    public void clean() 
    {
       wordHash.clear();
    }

    public void initialize() 
    {
        btsHash.put(new Character('{'), new Character('}'));
        btsHash.put(new Character('('), new Character(')'));
        btsHash.put(new Character('['), new Character(']'));
        revBtsHash.put(new Character('}'), new Character('{'));
        revBtsHash.put(new Character(')'), new Character('('));
        revBtsHash.put(new Character(']'), new Character('['));
        stopWords.put(new String("a"), 1);
        stopWords.put(new String("a's"), 1);
        stopWords.put(new String("able"), 1);
        stopWords.put(new String("about"), 1);
        stopWords.put(new String("above"), 1);
        stopWords.put(new String("abroad"), 1);
        stopWords.put(new String("according"), 1);
        stopWords.put(new String("accordingly"), 1);
        stopWords.put(new String("across"), 1);
        stopWords.put(new String("actually"), 1);
        stopWords.put(new String("adj"), 1);
        stopWords.put(new String("after"), 1);
        stopWords.put(new String("afterwards"), 1);
        stopWords.put(new String("again"), 1);
        stopWords.put(new String("against"), 1);
        stopWords.put(new String("ago"), 1);
        stopWords.put(new String("ahead"), 1);
        stopWords.put(new String("ain't"), 1);
        stopWords.put(new String("all"), 1);
        stopWords.put(new String("allow"), 1);
        stopWords.put(new String("allows"), 1);
        stopWords.put(new String("almost"), 1);
        stopWords.put(new String("alone"), 1);
        stopWords.put(new String("along"), 1);
        stopWords.put(new String("alongside"), 1);
        stopWords.put(new String("already"), 1);
        stopWords.put(new String("also"), 1);
        stopWords.put(new String("although"), 1);
        stopWords.put(new String("always"), 1);
        stopWords.put(new String("am"), 1);
        stopWords.put(new String("amid"), 1);
        stopWords.put(new String("amidst"), 1);
        stopWords.put(new String("among"), 1);
        stopWords.put(new String("amongst"), 1);
        stopWords.put(new String("an"), 1);
        stopWords.put(new String("and"), 1);
        stopWords.put(new String("another"), 1);
        stopWords.put(new String("any"), 1);
        stopWords.put(new String("anybody"), 1);
        stopWords.put(new String("anyhow"), 1);
        stopWords.put(new String("anyone"), 1);
        stopWords.put(new String("anything"), 1);
        stopWords.put(new String("anyway"), 1);
        stopWords.put(new String("anyways"), 1);
        stopWords.put(new String("anywhere"), 1);
        stopWords.put(new String("apart"), 1);
        stopWords.put(new String("appear"), 1);
        stopWords.put(new String("appreciate"), 1);
        stopWords.put(new String("appropriate"), 1);
        stopWords.put(new String("are"), 1);
        stopWords.put(new String("aren't"), 1);
        stopWords.put(new String("around"), 1);
        stopWords.put(new String("as"), 1);
        stopWords.put(new String("aside"), 1);
        stopWords.put(new String("ask"), 1);
        stopWords.put(new String("asking"), 1);
        stopWords.put(new String("associated"), 1);
        stopWords.put(new String("at"), 1);
        stopWords.put(new String("available"), 1);
        stopWords.put(new String("away"), 1);
        stopWords.put(new String("awfully"), 1);
        stopWords.put(new String("b"), 1);
        stopWords.put(new String("back"), 1);
        stopWords.put(new String("backward"), 1);
        stopWords.put(new String("backwards"), 1);
        stopWords.put(new String("be"), 1);
        stopWords.put(new String("became"), 1);
        stopWords.put(new String("because"), 1);
        stopWords.put(new String("become"), 1);
        stopWords.put(new String("becomes"), 1);
        stopWords.put(new String("becoming"), 1);
        stopWords.put(new String("been"), 1);
        stopWords.put(new String("before"), 1);
        stopWords.put(new String("beforehand"), 1);
        stopWords.put(new String("begin"), 1);
        stopWords.put(new String("behind"), 1);
        stopWords.put(new String("being"), 1);
        stopWords.put(new String("believe"), 1);
        stopWords.put(new String("below"), 1);
        stopWords.put(new String("beside"), 1);
        stopWords.put(new String("besides"), 1);
        stopWords.put(new String("best"), 1);
        stopWords.put(new String("better"), 1);
        stopWords.put(new String("between"), 1);
        stopWords.put(new String("beyond"), 1);
        stopWords.put(new String("both"), 1);
        stopWords.put(new String("brief"), 1);
        stopWords.put(new String("but"), 1);
        stopWords.put(new String("by"), 1);
        stopWords.put(new String("c"), 1);
        stopWords.put(new String("c'mon"), 1);
        stopWords.put(new String("c's"), 1);
        stopWords.put(new String("came"), 1);
        stopWords.put(new String("can"), 1);
        stopWords.put(new String("can't"), 1);
        stopWords.put(new String("cannot"), 1);
        stopWords.put(new String("cant"), 1);
        stopWords.put(new String("caption"), 1);
        stopWords.put(new String("cause"), 1);
        stopWords.put(new String("causes"), 1);
        stopWords.put(new String("certain"), 1);
        stopWords.put(new String("certainly"), 1);
        stopWords.put(new String("changes"), 1);
        stopWords.put(new String("clearly"), 1);
        stopWords.put(new String("co"), 1);
        stopWords.put(new String("co."), 1);
        stopWords.put(new String("com"), 1);
        stopWords.put(new String("come"), 1);
        stopWords.put(new String("comes"), 1);
        stopWords.put(new String("concerning"), 1);
        stopWords.put(new String("consequently"), 1);
        stopWords.put(new String("consider"), 1);
        stopWords.put(new String("considering"), 1);
        stopWords.put(new String("contain"), 1);
        stopWords.put(new String("containing"), 1);
        stopWords.put(new String("contains"), 1);
        stopWords.put(new String("corresponding"), 1);
        stopWords.put(new String("could"), 1);
        stopWords.put(new String("couldn't"), 1);
        stopWords.put(new String("course"), 1);
        stopWords.put(new String("currently"), 1);
        stopWords.put(new String("d"), 1);
        stopWords.put(new String("dare"), 1);
        stopWords.put(new String("daren't"), 1);
        stopWords.put(new String("dear"), 1);
        stopWords.put(new String("definitely"), 1);
        stopWords.put(new String("described"), 1);
        stopWords.put(new String("despite"), 1);
        stopWords.put(new String("did"), 1);
        stopWords.put(new String("didn't"), 1);
        stopWords.put(new String("different"), 1);
        stopWords.put(new String("directly"), 1);
        stopWords.put(new String("do"), 1);
        stopWords.put(new String("does"), 1);
        stopWords.put(new String("doesn't"), 1);
        stopWords.put(new String("doing"), 1);
        stopWords.put(new String("don't"), 1);
        stopWords.put(new String("done"), 1);
        stopWords.put(new String("down"), 1);
        stopWords.put(new String("downwards"), 1);
        stopWords.put(new String("during"), 1);
        stopWords.put(new String("e"), 1);
        stopWords.put(new String("each"), 1);
        stopWords.put(new String("edu"), 1);
        stopWords.put(new String("eg"), 1);
        stopWords.put(new String("eight"), 1);
        stopWords.put(new String("eighty"), 1);
        stopWords.put(new String("either"), 1);
        stopWords.put(new String("else"), 1);
        stopWords.put(new String("elsewhere"), 1);
        stopWords.put(new String("end"), 1);
        stopWords.put(new String("ending"), 1);
        stopWords.put(new String("enough"), 1);
        stopWords.put(new String("entirely"), 1);
        stopWords.put(new String("especially"), 1);
        stopWords.put(new String("et"), 1);
        stopWords.put(new String("etc"), 1);
        stopWords.put(new String("even"), 1);
        stopWords.put(new String("ever"), 1);
        stopWords.put(new String("evermore"), 1);
        stopWords.put(new String("every"), 1);
        stopWords.put(new String("everybody"), 1);
        stopWords.put(new String("everyone"), 1);
        stopWords.put(new String("everything"), 1);
        stopWords.put(new String("everywhere"), 1);
        stopWords.put(new String("ex"), 1);
        stopWords.put(new String("exactly"), 1);
        stopWords.put(new String("example"), 1);
        stopWords.put(new String("except"), 1);
        stopWords.put(new String("f"), 1);
        stopWords.put(new String("fairly"), 1);
        stopWords.put(new String("far"), 1);
        stopWords.put(new String("farther"), 1);
        stopWords.put(new String("few"), 1);
        stopWords.put(new String("fewer"), 1);
        stopWords.put(new String("fifth"), 1);
        stopWords.put(new String("first"), 1);
        stopWords.put(new String("five"), 1);
        stopWords.put(new String("followed"), 1);
        stopWords.put(new String("following"), 1);
        stopWords.put(new String("follows"), 1);
        stopWords.put(new String("for"), 1);
        stopWords.put(new String("forever"), 1);
        stopWords.put(new String("former"), 1);
        stopWords.put(new String("formerly"), 1);
        stopWords.put(new String("forth"), 1);
        stopWords.put(new String("forward"), 1);
        stopWords.put(new String("found"), 1);
        stopWords.put(new String("four"), 1);
        stopWords.put(new String("from"), 1);
        stopWords.put(new String("further"), 1);
        stopWords.put(new String("furthermore"), 1);
        stopWords.put(new String("g"), 1);
        stopWords.put(new String("get"), 1);
        stopWords.put(new String("gets"), 1);
        stopWords.put(new String("getting"), 1);
        stopWords.put(new String("given"), 1);
        stopWords.put(new String("gives"), 1);
        stopWords.put(new String("go"), 1);
        stopWords.put(new String("goes"), 1);
        stopWords.put(new String("going"), 1);
        stopWords.put(new String("gone"), 1);
        stopWords.put(new String("got"), 1);
        stopWords.put(new String("gotten"), 1);
        stopWords.put(new String("greetings"), 1);
        stopWords.put(new String("h"), 1);
        stopWords.put(new String("had"), 1);
        stopWords.put(new String("hadn't"), 1);
        stopWords.put(new String("half"), 1);
        stopWords.put(new String("happens"), 1);
        stopWords.put(new String("hardly"), 1);
        stopWords.put(new String("has"), 1);
        stopWords.put(new String("hasn't"), 1);
        stopWords.put(new String("have"), 1);
        stopWords.put(new String("haven't"), 1);
        stopWords.put(new String("having"), 1);
        stopWords.put(new String("he"), 1);
        stopWords.put(new String("he'd"), 1);
        stopWords.put(new String("he'll"), 1);
        stopWords.put(new String("he's"), 1);
        stopWords.put(new String("hello"), 1);
        stopWords.put(new String("help"), 1);
        stopWords.put(new String("hence"), 1);
        stopWords.put(new String("her"), 1);
        stopWords.put(new String("here"), 1);
        stopWords.put(new String("here's"), 1);
        stopWords.put(new String("hereafter"), 1);
        stopWords.put(new String("hereby"), 1);
        stopWords.put(new String("herein"), 1);
        stopWords.put(new String("hereupon"), 1);
        stopWords.put(new String("hers"), 1);
        stopWords.put(new String("herself"), 1);
        stopWords.put(new String("hi"), 1);
        stopWords.put(new String("him"), 1);
        stopWords.put(new String("himself"), 1);
        stopWords.put(new String("his"), 1);
        stopWords.put(new String("hither"), 1);
        stopWords.put(new String("hopefully"), 1);
        stopWords.put(new String("how"), 1);
        stopWords.put(new String("how's"), 1);
        stopWords.put(new String("howbeit"), 1);
        stopWords.put(new String("however"), 1);
        stopWords.put(new String("http"), 1);
        stopWords.put(new String("hundred"), 1);
        stopWords.put(new String("i"), 1);
        stopWords.put(new String("i'd"), 1);
        stopWords.put(new String("i'll"), 1);
        stopWords.put(new String("i'm"), 1);
        stopWords.put(new String("i've"), 1);
        stopWords.put(new String("ie"), 1);
        stopWords.put(new String("if"), 1);
        stopWords.put(new String("ignored"), 1);
        stopWords.put(new String("immediate"), 1);
        stopWords.put(new String("in"), 1);
        stopWords.put(new String("inasmuch"), 1);
        stopWords.put(new String("inc"), 1);
        stopWords.put(new String("inc."), 1);
        stopWords.put(new String("indeed"), 1);
        stopWords.put(new String("indicate"), 1);
        stopWords.put(new String("indicated"), 1);
        stopWords.put(new String("indicates"), 1);
        stopWords.put(new String("inner"), 1);
        stopWords.put(new String("inside"), 1);
        stopWords.put(new String("insofar"), 1);
        stopWords.put(new String("instead"), 1);
        stopWords.put(new String("into"), 1);
        stopWords.put(new String("inward"), 1);
        stopWords.put(new String("is"), 1);
        stopWords.put(new String("isn't"), 1);
        stopWords.put(new String("it"), 1);
        stopWords.put(new String("it'd"), 1);
        stopWords.put(new String("it'll"), 1);
        stopWords.put(new String("it's"), 1);
        stopWords.put(new String("its"), 1);
        stopWords.put(new String("itself"), 1);
        stopWords.put(new String("j"), 1);
        stopWords.put(new String("just"), 1);
        stopWords.put(new String("k"), 1);
        stopWords.put(new String("keep"), 1);
        stopWords.put(new String("keeps"), 1);
        stopWords.put(new String("kept"), 1);
        stopWords.put(new String("know"), 1);
        stopWords.put(new String("known"), 1);
        stopWords.put(new String("knows"), 1);
        stopWords.put(new String("l"), 1);
        stopWords.put(new String("last"), 1);
        stopWords.put(new String("lately"), 1);
        stopWords.put(new String("later"), 1);
        stopWords.put(new String("latter"), 1);
        stopWords.put(new String("latterly"), 1);
        stopWords.put(new String("least"), 1);
        stopWords.put(new String("less"), 1);
        stopWords.put(new String("lest"), 1);
        stopWords.put(new String("let"), 1);
        stopWords.put(new String("let's"), 1);
        stopWords.put(new String("like"), 1);
        stopWords.put(new String("liked"), 1);
        stopWords.put(new String("likely"), 1);
        stopWords.put(new String("likewise"), 1);
        stopWords.put(new String("little"), 1);
        stopWords.put(new String("look"), 1);
        stopWords.put(new String("looking"), 1);
        stopWords.put(new String("looks"), 1);
        stopWords.put(new String("low"), 1);
        stopWords.put(new String("lower"), 1);
        stopWords.put(new String("ltd"), 1);
        stopWords.put(new String("m"), 1);
        stopWords.put(new String("made"), 1);
        stopWords.put(new String("mainly"), 1);
        stopWords.put(new String("make"), 1);
        stopWords.put(new String("makes"), 1);
        stopWords.put(new String("many"), 1);
        stopWords.put(new String("may"), 1);
        stopWords.put(new String("maybe"), 1);
        stopWords.put(new String("mayn't"), 1);
        stopWords.put(new String("me"), 1);
        stopWords.put(new String("mean"), 1);
        stopWords.put(new String("meantime"), 1);
        stopWords.put(new String("meanwhile"), 1);
        stopWords.put(new String("merely"), 1);
        stopWords.put(new String("might"), 1);
        stopWords.put(new String("mightn't"), 1);
        stopWords.put(new String("mine"), 1);
        stopWords.put(new String("minus"), 1);
        stopWords.put(new String("miss"), 1);
        stopWords.put(new String("more"), 1);
        stopWords.put(new String("moreover"), 1);
        stopWords.put(new String("most"), 1);
        stopWords.put(new String("mostly"), 1);
        stopWords.put(new String("mr"), 1);
        stopWords.put(new String("mrs"), 1);
        stopWords.put(new String("much"), 1);
        stopWords.put(new String("must"), 1);
        stopWords.put(new String("mustn't"), 1);
        stopWords.put(new String("my"), 1);
        stopWords.put(new String("myself"), 1);
        stopWords.put(new String("n"), 1);
        stopWords.put(new String("name"), 1);
        stopWords.put(new String("namely"), 1);
        stopWords.put(new String("nd"), 1);
        stopWords.put(new String("near"), 1);
        stopWords.put(new String("nearly"), 1);
        stopWords.put(new String("necessary"), 1);
        stopWords.put(new String("need"), 1);
        stopWords.put(new String("needn't"), 1);
        stopWords.put(new String("needs"), 1);
        stopWords.put(new String("neither"), 1);
        stopWords.put(new String("never"), 1);
        stopWords.put(new String("neverf"), 1);
        stopWords.put(new String("neverless"), 1);
        stopWords.put(new String("nevertheless"), 1);
        stopWords.put(new String("new"), 1);
        stopWords.put(new String("next"), 1);
        stopWords.put(new String("nine"), 1);
        stopWords.put(new String("ninety"), 1);
        stopWords.put(new String("no"), 1);
        stopWords.put(new String("no-one"), 1);
        stopWords.put(new String("nobody"), 1);
        stopWords.put(new String("non"), 1);
        stopWords.put(new String("none"), 1);
        stopWords.put(new String("nonetheless"), 1);
        stopWords.put(new String("noone"), 1);
        stopWords.put(new String("nor"), 1);
        stopWords.put(new String("normally"), 1);
        stopWords.put(new String("not"), 1);
        stopWords.put(new String("nothing"), 1);
        stopWords.put(new String("notwithstanding"), 1);
        stopWords.put(new String("novel"), 1);
        stopWords.put(new String("now"), 1);
        stopWords.put(new String("nowhere"), 1);
        stopWords.put(new String("o"), 1);
        stopWords.put(new String("obviously"), 1);
        stopWords.put(new String("of"), 1);
        stopWords.put(new String("off"), 1);
        stopWords.put(new String("often"), 1);
        stopWords.put(new String("oh"), 1);
        stopWords.put(new String("ok"), 1);
        stopWords.put(new String("okay"), 1);
        stopWords.put(new String("old"), 1);
        stopWords.put(new String("on"), 1);
        stopWords.put(new String("once"), 1);
        stopWords.put(new String("one"), 1);
        stopWords.put(new String("one's"), 1);
        stopWords.put(new String("ones"), 1);
        stopWords.put(new String("only"), 1);
        stopWords.put(new String("onto"), 1);
        stopWords.put(new String("opposite"), 1);
        stopWords.put(new String("or"), 1);
        stopWords.put(new String("other"), 1);
        stopWords.put(new String("others"), 1);
        stopWords.put(new String("otherwise"), 1);
        stopWords.put(new String("ought"), 1);
        stopWords.put(new String("oughtn't"), 1);
        stopWords.put(new String("our"), 1);
        stopWords.put(new String("ours"), 1);
        stopWords.put(new String("ourselves"), 1);
        stopWords.put(new String("out"), 1);
        stopWords.put(new String("outside"), 1);
        stopWords.put(new String("over"), 1);
        stopWords.put(new String("overall"), 1);
        stopWords.put(new String("own"), 1);
        stopWords.put(new String("p"), 1);
        stopWords.put(new String("particular"), 1);
        stopWords.put(new String("particularly"), 1);
        stopWords.put(new String("past"), 1);
        stopWords.put(new String("per"), 1);
        stopWords.put(new String("perhaps"), 1);
        stopWords.put(new String("placed"), 1);
        stopWords.put(new String("please"), 1);
        stopWords.put(new String("plus"), 1);
        stopWords.put(new String("possible"), 1);
        stopWords.put(new String("presumably"), 1);
        stopWords.put(new String("probably"), 1);
        stopWords.put(new String("provided"), 1);
        stopWords.put(new String("provides"), 1);
        stopWords.put(new String("q"), 1);
        stopWords.put(new String("que"), 1);
        stopWords.put(new String("quite"), 1);
        stopWords.put(new String("qv"), 1);
        stopWords.put(new String("r"), 1);
        stopWords.put(new String("rather"), 1);
        stopWords.put(new String("rd"), 1);
        stopWords.put(new String("re"), 1);
        stopWords.put(new String("really"), 1);
        stopWords.put(new String("reasonably"), 1);
        stopWords.put(new String("recent"), 1);
        stopWords.put(new String("recently"), 1);
        stopWords.put(new String("regarding"), 1);
        stopWords.put(new String("regardless"), 1);
        stopWords.put(new String("regards"), 1);
        stopWords.put(new String("relatively"), 1);
        stopWords.put(new String("respectively"), 1);
        stopWords.put(new String("right"), 1);
        stopWords.put(new String("round"), 1);
        stopWords.put(new String("s"), 1);
        stopWords.put(new String("said"), 1);
        stopWords.put(new String("same"), 1);
        stopWords.put(new String("saw"), 1);
        stopWords.put(new String("say"), 1);
        stopWords.put(new String("saying"), 1);
        stopWords.put(new String("says"), 1);
        stopWords.put(new String("second"), 1);
        stopWords.put(new String("secondly"), 1);
        stopWords.put(new String("see"), 1);
        stopWords.put(new String("seeing"), 1);
        stopWords.put(new String("seem"), 1);
        stopWords.put(new String("seemed"), 1);
        stopWords.put(new String("seeming"), 1);
        stopWords.put(new String("seems"), 1);
        stopWords.put(new String("seen"), 1);
        stopWords.put(new String("self"), 1);
        stopWords.put(new String("selves"), 1);
        stopWords.put(new String("sensible"), 1);
        stopWords.put(new String("sent"), 1);
        stopWords.put(new String("serious"), 1);
        stopWords.put(new String("seriously"), 1);
        stopWords.put(new String("seven"), 1);
        stopWords.put(new String("several"), 1);
        stopWords.put(new String("shall"), 1);
        stopWords.put(new String("shan't"), 1);
        stopWords.put(new String("she"), 1);
        stopWords.put(new String("she'd"), 1);
        stopWords.put(new String("she'll"), 1);
        stopWords.put(new String("she's"), 1);
        stopWords.put(new String("should"), 1);
        stopWords.put(new String("shouldn't"), 1);
        stopWords.put(new String("since"), 1);
        stopWords.put(new String("six"), 1);
        stopWords.put(new String("so"), 1);
        stopWords.put(new String("some"), 1);
        stopWords.put(new String("somebody"), 1);
        stopWords.put(new String("someday"), 1);
        stopWords.put(new String("somehow"), 1);
        stopWords.put(new String("someone"), 1);
        stopWords.put(new String("something"), 1);
        stopWords.put(new String("sometime"), 1);
        stopWords.put(new String("sometimes"), 1);
        stopWords.put(new String("somewhat"), 1);
        stopWords.put(new String("somewhere"), 1);
        stopWords.put(new String("soon"), 1);
        stopWords.put(new String("sorry"), 1);
        stopWords.put(new String("specified"), 1);
        stopWords.put(new String("specify"), 1);
        stopWords.put(new String("specifying"), 1);
        stopWords.put(new String("still"), 1);
        stopWords.put(new String("sub"), 1);
        stopWords.put(new String("such"), 1);
        stopWords.put(new String("sup"), 1);
        stopWords.put(new String("sure"), 1);
        stopWords.put(new String("t"), 1);
        stopWords.put(new String("t's"), 1);
        stopWords.put(new String("take"), 1);
        stopWords.put(new String("taken"), 1);
        stopWords.put(new String("taking"), 1);
        stopWords.put(new String("tell"), 1);
        stopWords.put(new String("tends"), 1);
        stopWords.put(new String("th"), 1);
        stopWords.put(new String("than"), 1);
        stopWords.put(new String("thank"), 1);
        stopWords.put(new String("thanks"), 1);
        stopWords.put(new String("thanx"), 1);
        stopWords.put(new String("that"), 1);
        stopWords.put(new String("that'll"), 1);
        stopWords.put(new String("that's"), 1);
        stopWords.put(new String("that've"), 1);
        stopWords.put(new String("thats"), 1);
        stopWords.put(new String("the"), 1);
        stopWords.put(new String("their"), 1);
        stopWords.put(new String("theirs"), 1);
        stopWords.put(new String("them"), 1);
        stopWords.put(new String("themselves"), 1);
        stopWords.put(new String("then"), 1);
        stopWords.put(new String("thence"), 1);
        stopWords.put(new String("there"), 1);
        stopWords.put(new String("there'd"), 1);
        stopWords.put(new String("there'll"), 1);
        stopWords.put(new String("there're"), 1);
        stopWords.put(new String("there's"), 1);
        stopWords.put(new String("there've"), 1);
        stopWords.put(new String("thereafter"), 1);
        stopWords.put(new String("thereby"), 1);
        stopWords.put(new String("therefore"), 1);
        stopWords.put(new String("therein"), 1);
        stopWords.put(new String("theres"), 1);
        stopWords.put(new String("thereupon"), 1);
        stopWords.put(new String("these"), 1);
        stopWords.put(new String("they"), 1);
        stopWords.put(new String("they'd"), 1);
        stopWords.put(new String("they'll"), 1);
        stopWords.put(new String("they're"), 1);
        stopWords.put(new String("they've"), 1);
        stopWords.put(new String("thing"), 1);
        stopWords.put(new String("things"), 1);
        stopWords.put(new String("think"), 1);
        stopWords.put(new String("third"), 1);
        stopWords.put(new String("thirty"), 1);
        stopWords.put(new String("this"), 1);
        stopWords.put(new String("thorough"), 1);
        stopWords.put(new String("thoroughly"), 1);
        stopWords.put(new String("those"), 1);
        stopWords.put(new String("though"), 1);
        stopWords.put(new String("three"), 1);
        stopWords.put(new String("through"), 1);
        stopWords.put(new String("throughout"), 1);
        stopWords.put(new String("thru"), 1);
        stopWords.put(new String("thus"), 1);
        stopWords.put(new String("till"), 1);
        stopWords.put(new String("tis"), 1);
        stopWords.put(new String("to"), 1);
        stopWords.put(new String("together"), 1);
        stopWords.put(new String("too"), 1);
        stopWords.put(new String("took"), 1);
        stopWords.put(new String("toward"), 1);
        stopWords.put(new String("towards"), 1);
        stopWords.put(new String("tried"), 1);
        stopWords.put(new String("tries"), 1);
        stopWords.put(new String("truly"), 1);
        stopWords.put(new String("try"), 1);
        stopWords.put(new String("trying"), 1);
        stopWords.put(new String("twas"), 1);
        stopWords.put(new String("twice"), 1);
        stopWords.put(new String("two"), 1);
        stopWords.put(new String("u"), 1);
        stopWords.put(new String("un"), 1);
        stopWords.put(new String("under"), 1);
        stopWords.put(new String("underneath"), 1);
        stopWords.put(new String("undoing"), 1);
        stopWords.put(new String("unfortunately"), 1);
        stopWords.put(new String("unless"), 1);
        stopWords.put(new String("unlike"), 1);
        stopWords.put(new String("unlikely"), 1);
        stopWords.put(new String("until"), 1);
        stopWords.put(new String("unto"), 1);
        stopWords.put(new String("up"), 1);
        stopWords.put(new String("upon"), 1);
        stopWords.put(new String("upwards"), 1);
        stopWords.put(new String("us"), 1);
        stopWords.put(new String("use"), 1);
        stopWords.put(new String("used"), 1);
        stopWords.put(new String("useful"), 1);
        stopWords.put(new String("uses"), 1);
        stopWords.put(new String("using"), 1);
        stopWords.put(new String("usually"), 1);
        stopWords.put(new String("uucp"), 1);
        stopWords.put(new String("v"), 1);
        stopWords.put(new String("value"), 1);
        stopWords.put(new String("various"), 1);
        stopWords.put(new String("versus"), 1);
        stopWords.put(new String("very"), 1);
        stopWords.put(new String("via"), 1);
        stopWords.put(new String("viz"), 1);
        stopWords.put(new String("vs"), 1);
        stopWords.put(new String("w"), 1);
        stopWords.put(new String("want"), 1);
        stopWords.put(new String("wants"), 1);
        stopWords.put(new String("was"), 1);
        stopWords.put(new String("wasn't"), 1);
        stopWords.put(new String("way"), 1);
        stopWords.put(new String("we"), 1);
        stopWords.put(new String("we'd"), 1);
        stopWords.put(new String("we'll"), 1);
        stopWords.put(new String("we're"), 1);
        stopWords.put(new String("we've"), 1);
        stopWords.put(new String("welcome"), 1);
        stopWords.put(new String("well"), 1);
        stopWords.put(new String("went"), 1);
        stopWords.put(new String("were"), 1);
        stopWords.put(new String("weren't"), 1);
        stopWords.put(new String("what"), 1);
        stopWords.put(new String("what'll"), 1);
        stopWords.put(new String("what's"), 1);
        stopWords.put(new String("what've"), 1);
        stopWords.put(new String("whatever"), 1);
        stopWords.put(new String("when"), 1);
        stopWords.put(new String("when's"), 1);
        stopWords.put(new String("whence"), 1);
        stopWords.put(new String("whenever"), 1);
        stopWords.put(new String("where"), 1);
        stopWords.put(new String("where's"), 1);
        stopWords.put(new String("whereafter"), 1);
        stopWords.put(new String("whereas"), 1);
        stopWords.put(new String("whereby"), 1);
        stopWords.put(new String("wherein"), 1);
        stopWords.put(new String("whereupon"), 1);
        stopWords.put(new String("wherever"), 1);
        stopWords.put(new String("whether"), 1);
        stopWords.put(new String("which"), 1);
        stopWords.put(new String("whichever"), 1);
        stopWords.put(new String("while"), 1);
        stopWords.put(new String("whilst"), 1);
        stopWords.put(new String("whither"), 1);
        stopWords.put(new String("who"), 1);
        stopWords.put(new String("who'd"), 1);
        stopWords.put(new String("who'll"), 1);
        stopWords.put(new String("who's"), 1);
        stopWords.put(new String("whoever"), 1);
        stopWords.put(new String("whole"), 1);
        stopWords.put(new String("whom"), 1);
        stopWords.put(new String("whomever"), 1);
        stopWords.put(new String("whose"), 1);
        stopWords.put(new String("why"), 1);
        stopWords.put(new String("why's"), 1);
        stopWords.put(new String("will"), 1);
        stopWords.put(new String("willing"), 1);
        stopWords.put(new String("wish"), 1);
        stopWords.put(new String("wish"), 1);
        stopWords.put(new String("with"), 1);
        stopWords.put(new String("within"), 1);
        stopWords.put(new String("without"), 1);
        stopWords.put(new String("won't"), 1);
        stopWords.put(new String("wonder"), 1);
        stopWords.put(new String("would"), 1);
        stopWords.put(new String("wouldn't"), 1);
        stopWords.put(new String("www"), 1);
        stopWords.put(new String("x"), 1);
        stopWords.put(new String("y"), 1);
        stopWords.put(new String("yes"), 1);
        stopWords.put(new String("yet"), 1);
        stopWords.put(new String("you"), 1);
        stopWords.put(new String("you'd"), 1);
        stopWords.put(new String("you'll"), 1);
        stopWords.put(new String("you're"), 1);
        stopWords.put(new String("you've"), 1);
        stopWords.put(new String("your"), 1);
        stopWords.put(new String("yours"), 1);
        stopWords.put(new String("yourself"), 1);
        stopWords.put(new String("yourselves"), 1);
        stopWords.put(new String("z"), 1);
        stopWords.put(new String("zero"), 1);
        stopWords.put(new String("cent"), 1);
        stopWords.put(new String("lt"), 1);
        stopWords.put(new String("gt"), 1);
        stopWords.put(new String("amp"), 1);
        stopWords.put(new String("quot"), 1);
        stopWords.put(new String("nbsp"), 1);
        stopWords.put(new String("ndash"), 1);
        stopWords.put(new String("href"), 1);                
    }

    public ObjMethod(File inpXmlFile, String indexDir) 
    {
        super();
        initialize();
        this.inpXmlFile = inpXmlFile;
        this.indexDir = indexDir;
        try
        {
           this.tWriter = new FileWriter(this.indexDir + "//primaryindex.tnd", true);
        }
        catch(Exception e) 
        {
           e.printStackTrace();    
        }

    }
    public ObjMethod() 
    {
        super();
        initialize();
    }

    public void parseDocument(File inpXmlFile) 
    {
       // parse
       SAXParserFactory factory = SAXParserFactory.newInstance();
       try 
       {
          SAXParser parser = factory.newSAXParser();
          parser.parse(inpXmlFile, this);
       } 
       catch (Exception e) 
       {
           e.printStackTrace();
       }
    }
    
    public static void main(String[] args) 
    {
        File inpXmlFile;
        ObjMethod objMethod = new ObjMethod();
        int exit_code = SUCCESS_EXECUTION;
        if(args.length!=2)
        {
           System.out.println("Error: Please provide the corpus file in xml form and index directory in the command line"); 
           exit_code = MISSING_XMLFILE_ARGUMENT;
        }
        else
        {
           objMethod.indexDir = args[1];
           inpXmlFile = new File(args[0]);
           if(inpXmlFile.exists()==false) 
           {
              System.out.println("Error: "+args[0]+" not found");    
              exit_code = INPUT_XMLFILE_NOTFOUND;
           }
           else 
           {
               Runnable r;
               Thread mergerThread = null;
               try
               {
                   objMethod.tWriter = new FileWriter(objMethod.indexDir + "//titleMap.tnd", true);

//                   r = new Merger(objMethod.indexDir, 1, 1);
   //                mergerThread = new Thread(r);
   //                mergerThread.start();
               }
               catch(Exception e) 
               {
                  e.printStackTrace();    
               }
//               System.out.println("Start " + objMethod.getDate());
               objMethod.parseDocument(inpXmlFile);
               File completedFile = new File(objMethod.indexDir + "//index_completed.txt");
               try
               {
                  completedFile.createNewFile();
                   objMethod.tWriter.close();
                   if (mergerThread != null)
                   {
   //                  mergerThread.join();
                   }
               }
               catch(Exception e) 
               {
                   e.printStackTrace();
               }
//               System.out.println("End " + objMethod.getDate());
           }
        }
        System.exit(exit_code);
    }
    
    public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException 
    {    
            if (qName.equalsIgnoreCase("page")) 
            {
                page_count++;
                local_page_count++;
                if (refStr.length() > 0)   
                {
                   refStr.delete(0, refStr.length());
                }
            }
            else if (qName.equalsIgnoreCase("revision")) 
            {
               revisionStarted = true;
            }
            else if (qName.equalsIgnoreCase("contributor")) 
            {
                contributorStarted = true;
            }
            else if (qName.equalsIgnoreCase("id")) 
            {
                idStarted = true;
            }
            else if (qName.equalsIgnoreCase("title")) 
            {
               titleStarted = true;
               if (titleStr.length()>0)
               {
                  titleStr.delete(0, titleStr.length());
               }
            }
            else if (qName.equalsIgnoreCase("text")) 
            {
               textStarted = true;    
               if (textStr.length() > 0)
               {
                  textStr.delete(0, textStr.length());
               }
            }
    }
    
    public void endElement(String uri, String localName,
            String qName) throws SAXException 
    {
            if(qName.equalsIgnoreCase("file")) 
            {
               if (wordHash.size() > 0)
               {
                  createIndex(page_count);
//                  System.out.println(this.getDate() + " Completed "+page_count + " page chunk " + local_page_count + " Word Count "+wordHash.size());
                  local_page_count = 0;
                 wordHash.clear();
               }
            }
            if(qName.equalsIgnoreCase("page"))
            {           
               Pattern eLinkPattern =  Pattern.compile("==\\s*External\\s*links\\s*==");
               String tmpStr = textStr.toString();
               Matcher matcher = eLinkPattern.matcher(tmpStr);
               if(matcher.find()) 
               {
                  if (elinkStr.length() > 0)
                  {
                     elinkStr.delete(0, elinkStr.length());
                  }
                  elinkStr.append(matcher.group());
                  String tmpStr2 = "";
                  int startSt = matcher.start();
                  if (startSt > 0)
                  {
                     tmpStr2 = tmpStr.substring(0, startSt-1);
                  }
                  String tmpStr1 = tmpStr.substring(startSt+matcher.group().length(), tmpStr.length());
                  Pattern eLinkEndPattern = Pattern.compile("\\n");
                  Matcher matcherEnd = eLinkEndPattern.matcher(tmpStr1);
                  while(matcherEnd.find())
                  {
                     int start = matcherEnd.start();
                     if (start<tmpStr1.length()-1 && tmpStr1.charAt(start+1)=='*') 
                     {
                        continue;    
                     }
                      else 
                     {
                        if (elinkStr.length() > 0)
                        {
                           elinkStr.delete(0, elinkStr.length());
                        }
                        if (textStr.length() > 0)
                        {
                           textStr.delete(0, textStr.length()); 
                        }
                        elinkStr.append(tmpStr1.substring(0, start).toString());
                        textStr.append(tmpStr2 + " " + tmpStr1.substring(start));
                        break;
                    }
                  }
                  
               }
               else 
               {
                  if (elinkStr.length() > 0)
                  {
                     elinkStr.delete(0, elinkStr.length());
                  }
               }
//             System.out.println("DEBUG: page_id = " + page_id);
               breakIntoParts(textStr.toString().toLowerCase());
               processString(titleStr, page_id, pageType.TITLE);
               if (tFirst == true)
               {
                   tBuilder.append(page_id+"~"+titleStr.toString());   
                   tFirst = false;
               }
               else 
               {
                   tBuilder.append("\n"+page_id+"~"+titleStr.toString());   
               }
//               System.out.println(titleStr);
               processString(infoStr, page_id, pageType.INFOBOX);
               processString(bodyStr, page_id, pageType.BODY);
               processString(refStr, page_id, pageType.BODY);
               processString(catStr, page_id, pageType.CATEGORY);
               processString(elinkStr, page_id, pageType.ELINK);
//               System.out.println(page_count + " = " + tokenCtr + " fullCount = " + fullCount);
                page_id = 0;
                revision_id = 0;
                contributor_id = 0;
//                System.out.println("Ending " + page_count);               
//                tokenCtr = 0; 
                if(wordHash.size() >= chunkSize)
                {
                   createIndex(page_count);
/*                   if (page_count%100000 == 0)
                   {
                       System.out.println(this.getDate() + " Completed "+page_count + " page chunk " + local_page_count + " Word Count "+wordHash.size());
                   }
*/
                    wordHash.clear();
                   local_page_count = 0;
                }
            }
            else if (qName.equalsIgnoreCase("revision")) 
            {
               revisionStarted = false;
            }
            else if (qName.equalsIgnoreCase("contributor")) 
            {
                contributorStarted = false;
            }
            else if (qName.equalsIgnoreCase("id")) 
            {
               idStarted = false;    
            }
            else if (qName.equalsIgnoreCase("title")) 
            {
               titleStarted = false;    
            }
            else if (qName.equalsIgnoreCase("text")) 
            {
               textStarted = false;    
            }
    }
    
    public void characters(char ch[], int start, int length) throws SAXException 
    {
          String chStr = new String(ch, start, length);
          if (idStarted == true)
          {
             int iId = Integer.parseInt(chStr.toString());
             if (revisionStarted == false && contributorStarted == false) 
             {
                page_id = iId;
             }
             else if (revisionStarted == true && contributorStarted == false) 
             {
                revision_id = iId;
             }
             else
             {
                contributor_id = iId;
             }
          }
          else if (titleStarted == true) 
          {
              titleStr.append(chStr);
          }
          else if (textStarted == true) 
          {
              textStr.append(chStr);
          }
    }

    public void processString(StringBuilder inpStr, int pageId, PageType block_type)
    {
        if (block_type.equals(pageType.INFOBOX) ||
            block_type.equals(pageType.REFERENCE)) 
        {
            infoProcessString(inpStr.toString(), pageId, block_type);
        }
        else
        {
           StringTokenizer stringTokenizer = new StringTokenizer(inpStr.toString(), delimiters);
           while (stringTokenizer.hasMoreElements()) 
           {
              String strToken = stringTokenizer.nextElement().toString();
              this.tokenPush(strToken.toLowerCase(), pageId, block_type);
           }
        }
    }

    public void infoProcessString(String inpStr, int pageId, PageType block_type) 
    {
        String format;
        StringTokenizer stringTokenizer = new StringTokenizer(inpStr, "|");
        while (stringTokenizer.hasMoreElements()) 
        {
           String strToken = stringTokenizer.nextElement().toString();
           format = ".+=(.*)";
           String modStrToken = strToken.replaceAll(format, "$1");
           StringTokenizer iStrToken = new StringTokenizer(modStrToken, delimiters);
            while(iStrToken.hasMoreElements()) 
            {
                this.tokenPush(((String)iStrToken.nextElement()).toLowerCase(), pageId, block_type);     
            }
        }
    }
    
    public boolean isNumber(String strToken) 
    {
        boolean is_number = false;
        Pattern numberPattern =  Pattern.compile("^(-)?\\d+(\\.\\d+)?$");
        Matcher numberMatcher = numberPattern.matcher(strToken);
        if (numberMatcher.find())
        {
           is_number = true;
        }
        return is_number;
    }
    public void tokenPush(String strToken1, int pageId, PageType block_type)
    {
//        if (stopWords.containsKey(strToken) == false &&  isNumber(strToken)==false)
        if (stopWords.containsKey(strToken1) == false &&  !(strToken1.charAt(0) == '0' ||
        strToken1.charAt(0) == '1' ||       strToken1.charAt(0) == '2' ||
        strToken1.charAt(0) == '3' ||        strToken1.charAt(0) == '4' ||
        strToken1.charAt(0) == '5' ||        strToken1.charAt(0) == '6' ||
        strToken1.charAt(0) == '7' ||        strToken1.charAt(0) == '8' ||
        strToken1.charAt(0) == '9') && strToken1.length()>1)
        {
//           tokenCtr++;
//           fullCount++;
           String strToken = stemmer.stemString(strToken1);
           if(wordHash.isEmpty())
           {
              HashMap pageHash = new HashMap();
              HashMap typeHash = new HashMap();
              typeHash.put(block_type, new Integer(1));
              pageHash.put(new Integer(pageId), typeHash);
              wordHash.put(strToken, pageHash);
           }
           else 
           {
               HashMap pageHash = (HashMap)wordHash.get(strToken);
               if(pageHash != null)
               {
                   if(pageHash.isEmpty()) 
                   {
                       HashMap pageObj = new HashMap();
                       pageObj.put(block_type, new Integer(1));
                       pageHash.put(new Integer(pageId), pageObj);
                   }
                   else 
                   {
                       HashMap pageObj = (HashMap)pageHash.get(new Integer(pageId));
                       if(pageObj != null) 
                       {
                           Integer v = (Integer)pageObj.get(block_type);
                           if(v != null)
                           {
                              int val = v.intValue();
                              pageObj.put(block_type, new Integer(++val));
                           }
                           else
                           {
                              pageObj.put(block_type, new Integer(1));
                           }
                       }
                       else
                       {
                           pageObj = new HashMap();
                           pageObj.put(block_type, new Integer(1));
                           pageHash.put(new Integer(pageId), pageObj);
                       }
                   }
               }
               else
               {
                   pageHash = new HashMap();
                   HashMap pageObj = new HashMap();
                   pageObj.put(block_type, new Integer(1));
                   pageHash.put(new Integer(pageId), pageObj);
                   wordHash.put(strToken, pageHash);
               }
           }
        }
    }
    
    public void breakIntoParts(String inpStr) 
    {
        String outStr = "";
        if (catStr.length() > 0)
        {
           catStr.delete(1, catStr.length());
        }
        boolean infoStrParsed = false;
        Stack stk = new Stack();
        StringBuilder bodyBuilder = new StringBuilder();
        StringBuilder refBuilder = new StringBuilder();   
        StringBuilder catBuilder = new StringBuilder();
        boolean appendStr = false;
        boolean startPattern = false;
        boolean checkInfoStr = true;
        boolean comment_tag = false;
        boolean linkStart = false;
        boolean categoryStart = false;
        boolean linkStart1 = false;
        String startToken = "{{";
        int ind = inpStr.toLowerCase().indexOf(startToken);
        if (ind != -1) 
        {
            int startInd = ind+startToken.length();
            StringBuilder linkName = new StringBuilder();
            StringBuilder linkName1 = new StringBuilder();
            stk.push(new Character('{'));
            stk.push(new Character('{'));
            int i;
            int lgt = inpStr.length();
            for (i=startInd; i<lgt; i++) 
            {
                char c = inpStr.charAt(i);
                char nextC = ' ';
                if (i<lgt-1)
                {
                   nextC = inpStr.charAt(i+1);
                }
                if(i<lgt-10 && c=='[' && inpStr.charAt(i+1)=='[' && inpStr.charAt(i+2)=='c' &&
                    inpStr.charAt(i+3)=='a' && inpStr.charAt(i+4)=='t' && inpStr.charAt(i+5)=='e' && 
                   inpStr.charAt(i+6)=='g' && inpStr.charAt(i+7)=='o' && inpStr.charAt(i+8)=='r' && 
                   inpStr.charAt(i+9)=='y' && inpStr.charAt(i+10)==':') 
                {
                    categoryStart = true;
                    i = i+10;
                    continue;
                }
                else if (i<lgt-1 && categoryStart == true && c == ']' && inpStr.charAt(i+1)==']') 
                {
                   categoryStart = false;
                   i = i+1;
                   catStr.append(" " + catBuilder.toString());
                   if (catBuilder.length() > 0)
                   {
                      catBuilder.delete(0, catBuilder.length());
                   }
                   continue;
                }
                else if (categoryStart) 
                {
                    catBuilder.append(c);
                    continue;
                }
                                                                                                
                if(i<lgt-3 && c == '<' && nextC == 'r' && inpStr.charAt(i+2) == 'e' &&
                    inpStr.charAt(i+3) == 'f') 
                {
                   int refEndIndex = inpStr.indexOf("</ref>", i+3);
                   int refStartTagEndIndex = inpStr.indexOf("|", i+3);
                   if(refEndIndex !=-1 && (refStartTagEndIndex >= refEndIndex || refStartTagEndIndex == -1))
                   {
                       i = refEndIndex+5;
                   }
                   else if(refStartTagEndIndex !=-1 && refEndIndex != -1) 
                   {
                       refStr.append(" " + inpStr.substring(refStartTagEndIndex+1, refEndIndex));
                       i = refEndIndex+5;
                   }
                   else 
                   {
                      i = i+3;    
                   }
                   continue;
                }
                
                if (i<lgt-3 && c == '<' && nextC == '!' &&
                    inpStr.charAt(i+2) == '-' &&
                    inpStr.charAt(i+3) == '-') 
                {
                   comment_tag = true;    
                   i = i+3;
                }
                else if (i<lgt-2 && c == '-' && nextC == '-' &&
                    inpStr.charAt(i+2) == '>') 
                {
                   comment_tag = false;    
                   i = i+2;
                   continue;
                }                
                if(comment_tag == true)
                {
                   continue;
                }
                
                if (i<lgt-4 && c == '{' && nextC == '{' && inpStr.charAt(i+2) == 'G' 
                    && inpStr.charAt(i+3) == 'R' && inpStr.charAt(i+4)=='|') 
                {
                    i = inpStr.indexOf("}}", i+4);
                    i++;
                    continue;
                }
                
                if (i<lgt-1 && c == '[' && nextC == '[') 
                {
                   i++;
                   if (linkStart == false)
                   {
                      linkStart = true;
                      if (linkName.length() > 0)
                      {
                         linkName.delete(0, linkName.length());
                      }
                   }
                   else 
                   {
                      linkStart1 = true;    
                      if (linkName1.length() > 0)
                      {
                         linkName1.delete(0, linkName1.length());
                      }
                   }
                   continue;
                }
                else if (i<lgt-1 && linkStart == true && c == ']' && nextC == ']') 
                {
                    i++;
                    if (linkStart1 == true) 
                    {
                       linkStart1 = false;    
                       String linkStr = linkName1.toString();
                       bodyBuilder.append(linkStr);
                       continue;
                    }
                    linkStart = false;
                    String linkStr = linkName.toString();
                    bodyBuilder.append(linkStr);
                    continue;
                }
                else if (linkStart==true && c == '|')
                {
                    if(linkStart1 == true) 
                    {
                        if (linkName1.length() > 0)
                        {
                           linkName1.delete(0, linkName1.length());
                        }
                    }
                    else
                    {
                       if (linkName.length() > 0)
                       {
                          linkName.delete(0, linkName.length());
                       }
                    }
                    continue;
                }
                else if (linkStart == true) 
                {
                    if (linkStart1 == true) 
                    {
                        linkName1.append(c);
                    }
                    else 
                    {
                        linkName.append(c);   
                    }
                   continue;
                }
                if (c == '\n') 
                {
                   c = ' ';
                   continue;
                }
                bodyBuilder.append(c);
                Character chObj = new Character(c);
                if (btsHash.containsKey(chObj))
                {
                   stk.push(chObj);    
                }
                else if (revBtsHash.containsKey(chObj))
                {
                    if (stk.isEmpty()) 
                    {
                      // Could be intentionally they kept some syntax error here.
                      continue;
                    }
                    Character cObj = (Character)stk.peek();
                    Character matchObj = (Character)btsHash.get(cObj);
//                    if (btsHash.containsKey(cObj))
                    if (matchObj != null)
                    {
                       if(matchObj.equals(chObj))
                       {
                          stk.pop();    
                          if(infoStrParsed == false && stk.empty()) 
                          {
                             outStr = bodyBuilder.toString().substring(0, bodyBuilder.length()-2);
                             if (infoStr.length() > 0)
                             {
                                infoStr.delete(0, infoStr.length());
                             }
                             infoStr.append(outStr);
                             if (bodyBuilder.length() > 0)
                             {
                                bodyBuilder.delete(0, bodyBuilder.length());
                             }
                              infoStrParsed = true;
                             continue;
                          }
                       }
                    }
                }
            }
            if (bodyStr.length() > 0)
            {
               bodyStr.delete(0, bodyStr.length());
            }
            bodyStr.append(bodyBuilder.toString());
        }
        else 
        {
          if (bodyStr.length() > 0)
          {
             bodyStr.delete(0, bodyStr.length());
          }
//          System.out.println("Infobox not found page_id = "+page_id);
          bodyStr.append(inpStr);
        }
    }
    
    public String getDate() 
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
   
    public String hashString(HashMap h)
    {
       String str = "";
       Iterator itr = h.keySet().iterator();
       while(itr.hasNext())
       {
          PageType pType = (PageType)itr.next();
          Integer i = (Integer)h.get(pType);
          str = str + pType.getValue()+i.intValue();
       }
       return str;
    }
 
    public void createIndex(int paid)
    {
        if (wordHash.isEmpty())
        {
//           System.out.println("No Words present");    
        }
        else 
        {
            StringBuilder sBuilder;
            BufferedWriter out = null;
            try 
            {
                String fileName = new String(indexDir + "/index" + paid + ".indt");
                sBuilder = new StringBuilder("");
                Iterator itr = wordHash.keySet().iterator();
                if (tBuilder.length() > 0)
                {
                   tWriter.write(tBuilder.toString());
                   tBuilder.delete(0, tBuilder.length());
                   tFirst = true;
                }
                while(itr.hasNext()) 
                {
                    boolean firstTime = true;
                    String strToken = itr.next().toString();
                    HashMap pageIdHash = (HashMap)wordHash.get(strToken);
                    Iterator itr1 = pageIdHash.keySet().iterator();
//                    out.write("\n" + strToken + "~");
                    sBuilder.append(strToken + "~");
                    while(itr1.hasNext()) 
                    {
                        Integer pageIdObj = (Integer)itr1.next();
                        HashMap pageObj = (HashMap)pageIdHash.get(pageIdObj);
//                        out.write(pageIdObj.intValue() + ":" + pageObj.getIndexString() + ",");
                        if (firstTime == true) 
                        {
                            sBuilder.append(pageIdObj.intValue() + ":" + this.hashString(pageObj));
                            firstTime = false;
                        }
                        else
                        {
                           sBuilder.append("," + pageIdObj.intValue() + ":" + this.hashString(pageObj));
                        }
                    }
                    sBuilder.append("\n");
                }
                FileWriter fstream = new FileWriter(fileName, true); //true tells to append data.
//                out = new BufferedWriter(fstream);
                fstream.write(sBuilder.toString());
//                out.write(sBuilder.toString());
//                out.close();
                fstream.close();
                File oldFile = new File(fileName);
                File newFile = new File(indexDir + "/index" + paid + ".ind");
                oldFile.renameTo(newFile);
            }
            catch(Exception e) 
            {
                e.printStackTrace();
            }
            finally 
            {
                if (out != null) 
                {
                    try
                    {
                      out.close();    
                    }
                    catch(Exception e) 
                    {
                        
                    }
                }
            }
        }
    }
}

