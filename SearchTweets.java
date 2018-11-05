import twitter4j.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by tangy on 6/4/2017.
 */

public class SearchTweets {

    //permentant variables
    private Twitter twitter;
    private String user;
    private int page = 1;
    private int limit;
    private boolean override;

    //variables that can change
    private String fileLocation;
    private String tweets = "";

    public int limitInt;
    public int pageInt = 1;

    public boolean  active = true;
    public String content;


    SearchTweets(Twitter twitter, String user, int limit, boolean override, String fileLocation){
        this.twitter = twitter;
        this.user = user;
        this.limit = limit;
        this.override = override;
        this.fileLocation = fileLocation;
        limitInt = limit;
    }


    //allows you to search for tweets since a specific time
    // limitation is that it only takes the first page only
    public List<Status> searchQuery(Twitter twitter, String search)
            throws TwitterException, IOException {

        Paging paging = new Paging(2, 100);
        List<Status> statuses = twitter.getHomeTimeline(paging);

        Query query = new Query(search);
        query.setCount(500);
        //this is a key part
        query.setSince("2015-01-02");
        QueryResult result;
        List<Status> tweets = null;
        do {
            System.out.println("Write to File ...");
            result = twitter.search(query);
            List<Status> newTweets = result.getTweets();
            if (tweets == null) {
                tweets = newTweets;
            } else {
                tweets.addAll(newTweets);
            }
            for (Status s:newTweets){
                System.out.println(s.getUser().getName() + ": " + s.getText());
            }

        } while ((query = result.nextQuery()) != null);
        return tweets;
    }

    public  List<Status> searchQueryMk2(Twitter twitter, String search, int page)
            throws TwitterException {

        // looks at all tweets since this page
        // the count is the limit
        int pageInt = page;
        int count = 0;
        Paging paging = new Paging(pageInt, 10);
        List<Status> statuses = twitter.getUserTimeline("meemezy",paging);


        for (Status s : statuses) {
            System.out.println(s.getUser().getName() + ": " + s.getText());
            System.out.println(s.getCreatedAt()+"\n");
            count++;
            if(count == 9 && s.getText() != null){
                pageInt++;
                searchQueryMk2(twitter, search, pageInt);
            }
        }

        return null;
    }

    // searches and creates a file to store links and pictures
    public void searchUserAccount( )
            throws TwitterException, URISyntaxException, IOException{

        // looks at all tweets since this page
        // the count is the limit

        int count = 0;
        String text = "";
        Paging paging = new Paging(pageInt, 10);
        List<Status> statuses = twitter.getUserTimeline(user, paging);

        for (Status s : statuses) {
            String url = "https://twitter.com/" + s.getUser().getScreenName() + "/status/" + s.getId();
            if (s.getRetweetedStatus() == null) {
                content = url;
                //send the links to text
                HandleFile(override, active);

                //regular expressions
                String regex = "https://t.co/.*";
                text = s.getText().replaceAll(regex,"");

                System.out.println(s.getUser().getName() + ": " +  text);
                System.out.println(url); //prints the url of the tweet
                System.out.println(s.getCreatedAt()+"\n");

                //sets the tweets to variables to be exported
                tweets = tweets + "         " +text + "\n"+ url + "\n" + s.getCreatedAt() + "\n\n";


                active = false;

                //search for nudes
                getTwitterImage(s);
            }
            count++;

            if(count == 5 && limitInt > 0){
                limitInt--;
                pageInt++;
                searchUserAccount();
            }
        }
    }

    public void HandleFile(boolean override, boolean active) throws URISyntaxException, IOException{
        BufferedWriter bw = null;
        FileWriter fw = null;
        if(override && active){
            fw = new FileWriter("C:\\Users\\Henry\\Desktop\\Coding\\Java\\TwitterPosts.txt", false);
        }
        try {
            //append means not to overwrite the file
            fw = new FileWriter("C:\\Users\\Henry\\Desktop\\Coding\\Java\\TwitterPosts.txt", true);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.newLine();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getTwitterImage(Status statuses) throws TwitterException{

        //create file
        File file;

        for (MediaEntity m : statuses.getMediaEntities()) {

            try {
                // sets the file location
                file = new File(fileLocation);

                URL url = new URL(m.getMediaURL());
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + "\\" + m.getId() + "." + getExtension(m.getType()));
                fos.write(response);
                fos.close();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //}


    private String getExtension(String type) {
        if (type.equals("photo")) {
            return "jpg";
        } else if (type.equals("video")) {
            return "mp4";
        } else if (type.equals("animated_gif")) {
            return "gif";
        } else {
            return "err";
        }
    }

    public String getTweet(){
        return tweets;
    }

}
