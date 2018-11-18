package org.aai.text.statistics;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        ChatReader chtRd = new ChatReader();
        ChatStats chatStats = new ChatStats();
        String directory= ".\\Class";
        List<Path> pathList = chtRd.explorePath(directory);

        for(Path path: pathList) {
            ArrayList<Comment> commentList = chtRd.readFile(path);
            System.out.println(path.getFileName()+" Comments-->" + commentList.size());

            HtmlGenerator htmlGen = new HtmlGenerator(commentList);

            String fileName = path.getFileName().toString().replace(".txt","");
            htmlGen.writeHtml(new File( path.getParent().toString()+"/"+fileName+".html" ));
        }
    }
}
