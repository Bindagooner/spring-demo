package neospider.mngr.mvc.filter;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class XSSUtils {

    public static String stripXSS(String value) {
        if (value == null) {
            return null;
        }

        return Jsoup.clean(value, Whitelist.none());
    }

}
