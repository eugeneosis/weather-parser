import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "http://www.pogoda.msk.ru/";
        Document page = Jsoup.parse(new URL(url), 500);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFormatFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Fuck up!");
    }

    private static int printPartValues(Elements values, int index) {
        int count = 4;
        if (index == 0) {

            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            if (isMorning) {
                count = 3;
            }
            for (int i = 0; i < count; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        } else {
            for (int i = 0; i < count; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element weatherPage = page.select("table[class=wt]").first();
        Elements names = weatherPage.select("tr[class=wth]");
        Elements values = weatherPage.select("tr[valign=top]");

        int index = 0;

        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFormatFromString(dateString);
            System.out.println(date + "    Явления     Температура     Давление        Влажность       Ветер");
           int count = printPartValues(values, index);
           index = index + count;
        }
    }
}
