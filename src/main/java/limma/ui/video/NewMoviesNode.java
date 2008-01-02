package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewMoviesNode extends SimpleBrowserNode {

    public NewMoviesNode(int numberOfDays, VideoPlayer videoPlayer, VideoRepository videoRepository, String title) {
        super(title);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        Date boundaryDate = calendar.getTime();

        for (Video video : videoRepository.getAllVideos()) {

            if (video.getCreated() != null && boundaryDate.before(video.getCreated())) {
                MovieBrowserNode movieNode = new MovieBrowserNode(video, videoPlayer, videoRepository);
                movieNode.setParent(this);
                add(movieNode);
            }
        }

        sort(new Comparator<BrowserModelNode>() {
            public int compare(BrowserModelNode o1, BrowserModelNode o2) {
                MovieBrowserNode node1 = (MovieBrowserNode) o1;
                MovieBrowserNode node2 = (MovieBrowserNode) o2;
                return node2.getVideo().getCreated().compareTo(node1.getVideo().getCreated());
            }
        });
    }
}
