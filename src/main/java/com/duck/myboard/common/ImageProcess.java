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

    private List<ImageNameParser> imageList;
    private List<String> deletePath;
    private String content;

    public ImageProcess(String content) {
        this(content, new ArrayList<>());
    }

    public ImageProcess(String content, List<String> savedList) {
        this.imageList = new ArrayList<>();
        this.deletePath = new ArrayList<>();
        processing(content, savedList);
    }

    private void processing(String content, List<String> savedList) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        List<String> existList = new ArrayList<>();
        if (!images.isEmpty()) {
            for (Element image : images) {
                String srcStr = image.attr("src");
                if (srcStr.startsWith("/temp/image")) {
                    ImageNameParser imageNameParser = new ImageNameParser(srcStr);
                    imageList.add(imageNameParser);
                    image.attr("src", imageNameParser.getGcsPath());
                } else if (srcStr.startsWith("https://storage.googleapis.com/imgtest_bucket")) {
                    existList.add(srcStr);
                }
            }
        }

        if (!savedList.isEmpty()) {
            this.deletePath = savedList.stream()
                    .filter(item -> !existList.contains(item))
                    .toList();
        }
        this.content = doc.body().html();
    }

}
