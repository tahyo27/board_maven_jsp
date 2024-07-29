package com.duck.myboard.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
public class ImageProcess {

    private List<ImageNameParser> imageList = new ArrayList<>();
    private List<String> deletePath = new ArrayList<>();
    private String content;

    public ImageProcess(String content) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        if(!images.isEmpty()) {
            for(Element image : images) {
                String srcStr = image.attr("src");
                ImageNameParser imageNameParser = new ImageNameParser(srcStr);
                imageList.add(imageNameParser);
                image.attr("src", imageNameParser.getGcsPath());
            }
        }
        this.content = doc.body().html();
    }

    public ImageProcess(String content, List<String> savedList) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        List<String> existList = new ArrayList<>();
        if(!images.isEmpty()) {
            for(Element image : images) { //todo write랑 중복되는 부분 나중에 묶을 생각
                String srcStr = image.attr("src");
                if(srcStr.startsWith("/temp/image")) {
                    log.info(">>>>>>>>>> contains >>>>>>>>> srcStr : {}", srcStr);
                    ImageNameParser imageNameParser = new ImageNameParser(srcStr);
                    imageList.add(imageNameParser);
                    image.attr("src", imageNameParser.getGcsPath());
                } else if(srcStr.startsWith("https://storage.googleapis.com/imgtest_bucket")) {
                    existList.add(srcStr);
                }
            }
        }

        this.deletePath = savedList.stream()
                .filter(item -> !existList.contains(item)).toList();
        this.content = doc.body().html();
    }

}
