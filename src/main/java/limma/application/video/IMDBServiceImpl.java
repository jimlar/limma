package limma.application.video;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import org.jaxen.JaxenException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBServiceImpl implements IMDBService {
    private Pattern titleAndYearPattern;
    private WebClient webClient;
    private HtmlUnitXPath directorXPath;
    private HtmlUnitXPath plotOutlineXPath;
    private HtmlUnitXPath plotSummaryXPath;
    private HtmlUnitXPath runtimeXPath;
    private HtmlUnitXPath ratingXPath;
    private HtmlUnitXPath coverXPath;
    private HtmlUnitXPath genreXPath;
    private HtmlUnitXPath actorsXPath;

    public IMDBServiceImpl() {
        this(new WebClient());
    }

    public IMDBServiceImpl(WebClient webClient) {
        this.webClient = webClient;
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setJavaScriptEnabled(false);
        titleAndYearPattern = Pattern.compile(".*?(.+) \\((\\d+).*\\).*");

        try {
            directorXPath = new HtmlUnitXPath("//h5[text()='Director:']/following-sibling::*");
            plotOutlineXPath = new HtmlUnitXPath("//h5[text()='Plot Outline:']");
            plotSummaryXPath = new HtmlUnitXPath("//h5[text()='Plot Summary:']");
            runtimeXPath = new HtmlUnitXPath("//h5[text()='Runtime:']");
            ratingXPath = new HtmlUnitXPath("//div[@class='general rating']/b[2]");
            coverXPath = new HtmlUnitXPath("//a[@name='poster']/img/@src");
            genreXPath = new HtmlUnitXPath("//h5[text()='Genre:']/following-sibling::a[starts-with(@href, '/Sections/Genres')]");
            actorsXPath = new HtmlUnitXPath("//table[@class='cast']/tbody/tr/td[@class='nm']/a");

        } catch (JaxenException e) {
            throw new IllegalStateException("Could not create xpath expression", e);
        }
    }

    public IMDBInfo getInfo(int imdbNumber) throws IOException {
        IMDBInfo imdbInfo = new IMDBInfo();
        imdbInfo.setImdbNumber(imdbNumber);

        HtmlPage page = getPage(imdbNumber);

        Matcher matcher = titleAndYearPattern.matcher(page.getTitleText());
        if (matcher.matches()) {
            imdbInfo.setTitle(matcher.group(1));
            imdbInfo.setYear(Integer.parseInt(matcher.group(2)));
        }

        imdbInfo.setDirector(getText(page, directorXPath));

        imdbInfo.setPlot(getSiblingText(page, plotOutlineXPath));
        if (imdbInfo.getPlot() == null) {
            imdbInfo.setPlot(getSiblingText(page, plotSummaryXPath));
        }

        imdbInfo.setRuntime(getSiblingText(page, runtimeXPath));
        imdbInfo.setRating(getText(page, ratingXPath));
        imdbInfo.setCover(getAttributeValue(page, coverXPath));

        imdbInfo.addGenres(getTexts(page, genreXPath));
        imdbInfo.addActors(getTexts(page, actorsXPath));

        return imdbInfo;
    }

    private List<String> getTexts(HtmlPage page, HtmlUnitXPath xPath) {
        List<String> result = new ArrayList<String>();
        try {
            List genres = xPath.selectNodes(page);
            for (Object genre : genres) {
                DomNode node = (DomNode) genre;
                result.add(asText(node));
            }
        } catch (JaxenException e) {
        }

        return result;
    }

    private String getAttributeValue(HtmlPage page, HtmlUnitXPath xPath) {
        try {
            HtmlAttr result = (HtmlAttr) xPath.selectSingleNode(page);
            if (result == null) {
                return null;
            }
            return result.getValue().toString();
        } catch (JaxenException e) {
            return null;
        }
    }

    private String getSiblingText(HtmlPage page, HtmlUnitXPath xpath) {
        try {
            HtmlElement result = (HtmlElement) xpath.selectSingleNode(page);
            if (result == null) {
                return null;
            }
            return asText(result.getNextSibling());

        } catch (JaxenException e) {
            return null;
        }
    }

    private String getText(HtmlPage page, HtmlUnitXPath xPath) {
        try {
            return asText((DomNode) xPath.selectSingleNode(page));
        } catch (JaxenException e) {
            return null;
        }
    }

    private String asText(DomNode node) {
        if (node == null) {
            return null;
        }
        return node.asText().trim();
    }

    private HtmlPage getPage(int imdbNumber) throws IOException {
        return (HtmlPage) webClient.getPage(new URL("http://www.imdb.com/title/tt" + imdbNumber + "/"));
    }
}
