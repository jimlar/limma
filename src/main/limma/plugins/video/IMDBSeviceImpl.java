package limma.plugins.video;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMDBSeviceImpl implements IMDBSevice {
    private Pattern titleAndYearPattern;
    private Pattern directorPattern;
    private Pattern plotOutlinePattern;
    private Pattern plotSummaryPattern;
    private Pattern runtimePattern;
    private Pattern ratingPattern;
    private Pattern coverPattern;

    public IMDBSeviceImpl() {
        titleAndYearPattern = Pattern.compile(".*?<title>(.+) \\((\\d+).*\\)</title>.*");
        directorPattern = Pattern.compile(".*?\">Directed by</b><br> <a href=.*?/\">(.+?)</a>.*");
        plotOutlinePattern = Pattern.compile(".*?>Plot Outline:</b> (.*?)<a href=\".*");
        plotSummaryPattern = Pattern.compile(".*?>Plot Summary:</b> (.*?)<a href=\".*");
        runtimePattern = Pattern.compile(".*?>Runtime:</b>(.*? min).*");
        ratingPattern = Pattern.compile(".*?>User Rating:</b>.*?<b>(.*?)/10</b>.*");
        coverPattern = Pattern.compile(".*?<img border=\"0\" alt=\"cover\" src=\"(.*?)\" height=\"(.*?)\" width=\"(.*?)\"><.*");
    }

    public IMDBInfo getInfo(int imdbNumber) throws IOException {
        IMDBInfo imdbInfo = new IMDBInfo();
        imdbInfo.setImdbNumber(imdbNumber);

        String html = getHtml(imdbNumber);
        Matcher matcher = titleAndYearPattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setTitle(matcher.group(1));
            imdbInfo.setYear(Integer.parseInt(matcher.group(2)));
        }
        matcher = directorPattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setDirector(matcher.group(1));
        }

        matcher = plotOutlinePattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setPlot(StringUtils.trimToEmpty(matcher.group(1)));
        } else {
            matcher = plotSummaryPattern.matcher(html);
            if (matcher.matches()) {
                imdbInfo.setPlot(StringUtils.trimToEmpty(matcher.group(1)));
            }
        }

        matcher = runtimePattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setRuntime(StringUtils.trimToEmpty(matcher.group(1)));
        }

        matcher = ratingPattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setRating(StringUtils.trimToEmpty(matcher.group(1)));
        }

        matcher = coverPattern.matcher(html);
        if (matcher.matches()) {
            imdbInfo.setCover(StringUtils.trimToEmpty(matcher.group(1)));
        }

        return imdbInfo;
    }

    private String getHtml(int imdbNumber) throws IOException {
        URL url = new URL("http://www.imdb.com/title/tt" + imdbNumber + "/");
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDefaultUseCaches(false);
            String encoding = urlConnection.getContentEncoding();
            String html;
            if (encoding != null) {
                html = IOUtils.toString(urlConnection.getInputStream(), encoding);
            } else {
                html = IOUtils.toString(urlConnection.getInputStream());
            }
            return html.replace('\n', ' ').replace('\r', ' ');
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
