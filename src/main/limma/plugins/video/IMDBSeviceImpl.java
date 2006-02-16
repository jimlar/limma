package limma.plugins.video;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBSeviceImpl implements IMDBSevice {
    private Pattern titleAndYearPattern;
    private WebClient webClient;

    public IMDBSeviceImpl() {
        this(new WebClient());
    }

    public IMDBSeviceImpl(WebClient webClient) {
        this.webClient = webClient;
        titleAndYearPattern = Pattern.compile(".*?(.+) \\((\\d+).*\\).*");
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

        HtmlElement titleElement = page.getDocumentElement().getOneHtmlElementByAttribute("strong", "class", "title");
        HtmlElement contentCell = titleElement.getEnclosingElement("td");

        HtmlElement directedByLabel = getElementContainingText("Directed by", contentCell);
        imdbInfo.setDirector(getSiblingText(directedByLabel, HtmlAnchor.class));

        HtmlElement plotOutlineLabel = getElementContainingText("Plot ", contentCell);
        if (plotOutlineLabel != null && plotOutlineLabel.getNextSibling() != null) {
            imdbInfo.setPlot(plotOutlineLabel.getNextSibling().asText().trim());
        }

        HtmlElement runtimeLabel = getElementContainingText("Runtime:", contentCell);
        if (runtimeLabel != null && runtimeLabel.getNextSibling() != null) {
            imdbInfo.setRuntime(runtimeLabel.getNextSibling().asText().trim());
        }


        HtmlElement ratingLabel = getElementContainingText("User Rating:", contentCell); 
        String ratingText = getSiblingText(ratingLabel, UnknownHtmlElement.class);
        imdbInfo.setRating(ratingText);

        List anchors = contentCell.getHtmlElementsByAttribute("a", "name", "poster");
        if (!anchors.isEmpty()) {
            HtmlAnchor posterLink = (HtmlAnchor) anchors.get(0);
            HtmlImage poster = (HtmlImage) posterLink.getHtmlElementsByTagName("img").get(0);
            imdbInfo.setCover(poster.getSrcAttribute());
        }
        return imdbInfo;
    }

    private HtmlElement getElementContainingText(String text, HtmlElement element) {
        if (element == null) {
            return null;
        }
        for (Iterator i = element.getChildElementsIterator(); i.hasNext();) {
            HtmlElement child = (HtmlElement) i.next();
            if (child.asText().indexOf(text) != -1) {
                return getElementContainingText(text, child);
            }
        }
        if (element.asText().indexOf(text) != -1) {
            return element;
        }
        return null;
    }

    private String getSiblingText(DomNode start, Class elementClass) {
        if (start == null) {
            return null;
        }
        DomNode sibling = start.getNextSibling();
        if (sibling == null) {
            return null;
        }
        if (elementClass.isInstance(sibling)) {
            return sibling.asText().trim();
        }

        return getSiblingText(sibling, elementClass);
    }

    private HtmlPage getPage(int imdbNumber) throws IOException {
        return (HtmlPage) webClient.getPage(new URL("http://www.imdb.com/title/tt" + imdbNumber + "/"));
    }
}
