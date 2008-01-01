package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;

import java.util.*;

public class NewMoviesNode extends BrowserModelNode {
    private int numberOfDays;
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public NewMoviesNode(int numberOfDays, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.numberOfDays = numberOfDays;
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return "New (" + numberOfDays + " days)";
    }

    public List<BrowserModelNode> getChildren() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        Date boundaryDate = calendar.getTime();

        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        for (Iterator i = videoRepository.getAllVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            if (video.getCreated() != null && boundaryDate.before(video.getCreated())) {

                MovieBrowserNode movieNode = new MovieBrowserNode(video, videoPlayer, videoRepository);
                movieNode.setParent(this);
                children.add(movieNode);
            }
        }

        Collections.sort(children, new Comparator<BrowserModelNode>() {
            public int compare(BrowserModelNode o1, BrowserModelNode o2) {
                MovieBrowserNode node1 = (MovieBrowserNode) o1;
                MovieBrowserNode node2 = (MovieBrowserNode) o2;
                return node2.getVideo().getCreated().compareTo(node1.getVideo().getCreated());
            }
        });

        return children;
    }
}
