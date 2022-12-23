package com.eulerity.hackathon.imagefinder;
import java.io.IOException;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler{
    
    private static final double REQUEST_DELAY = 300;
    private List<String> visitedUrls;
    public List<String> images;
    WebCrawler(){
        visitedUrls = new ArrayList<>();
        images = new ArrayList<>();
    }

    String[] getImages(){
        String[]imagesArray = new String[images.size()];
        for(int i = 0; i<images.size(); i++){
            imagesArray[i] = images.get(i);
        }
        return imagesArray;
    }
    public boolean isSubdomainMatch(String s, String t){
        return t.contains(s);
    }
    void crawlWebsite(int level, String url){
        if(level > 3) return;
        Document doc = requestVisit(url);
        if(doc != null){
            System.out.println("the url: " + url);
            Elements img = doc.getElementsByTag("img");
            for(Element element : img){
                String image = element.absUrl("src");
                String imageSource = element.attr("src");
                if(imageSource.length() == 0) {
                    continue;
                }
                if(!images.contains(image)){
                    System.out.println("the image link is: " + image);
                    images.add(image);
                }
            }

            for(Element link : doc.select("[href]")){
                String nextLink = link.absUrl("href");
                if(isSubdomainMatch(url, nextLink)) {
                    if (!visitedUrls.contains(nextLink)) {
                        crawlWebsite(level + 1, nextLink);
                    }
                }

            }
        }
    }

    Document requestVisit(String url){
        try{
            Connection connection = Jsoup.connect(url);
            Document doc = connection.get();
            if(connection.response().statusCode() == 200){
                System.out.println("The link: " + url);
                System.out.println("Title of the doc is: " + doc.title());
                visitedUrls.add(url);
                return doc;
            }

            // We can not visit the website.
            return null;
        }
        catch(IOException e){
            System.out.println("Error reading input!");
        }
        return null;
    }
}
