# Alarm Scanner
#### Video Demo: https://youtu.be/KyXsQokNN44
#### Description:
Alarm Scanner is a Java 8, JavaFx project.
<br>
Task: get the latest messages in 15 minutes from the list of Telegram channels.
<br>
Check the messages for the presence of one of the key phrases.
<br>
If a match is detected, a sound and text warning will be issued.
<br>
Key phrases are checked from the beginning of the list, but from the end of the message queue.
<br>
In the text window of the message queue, the first keyword phrase found is highlighted.
<br>
Lists of URLs of Telegram channels and key phrases are stored in text files:
<ul>
<li><b>source.txt</b> each URL on a separate line</li>
<li><b>keyword.txt</b> semicolon-separated key phrases, UTF-8 encoding</li>
</ul>
Messages from one of the Telegram channels are downloaded every 30 seconds.
<br>
If you have made changes to the list of URLs or to the list of keywords, click the Refresh button to apply the updated data.
<br>
The contents of the URL and keyword text fields are saved to the hard disk in text files:
source.txt, keyword.txt when the program is closed.
<br>
The sound notification can be turned off.

#### Project details:
Project files:
<br>
<ol>
<li><b>Main.java</b> entry point to run the myMain() method of the AlarmScannerApplication class</li>
<li>
<b>AlarmScannerApplication.java</b> a JavaFx application class that extends the base class Application.
Implements the life cycle of the Application:
<br>
init()
<br>
start()
<br>
stop()
<br>
A <b>task</b> has been created for parallel execution of the Program's business process:
<ul>
<li>Check and if necessary update the lists of URLs and key phrases from the GUI text fields.</li>
<li>Get a list of the most recent 15-minute messages from another source.</li>
<li>Put this list in a queue of lists, remove the first list from the queue.</li>
<li>Sort all messages in the queue in ascending order of publication date.</li>
<li>Check for key phrases in messages and, if necessary, enable sound and text alerts.</li>
<li>Display the message queue.</li>
<li>Enable a 30 seconds delay before processing the next message source.</li>
<li>At closing the program, save the lists of URLs and key phrases in text files: <b>source.txt</b> and <b>keyword.txt</b></li>
</ul>
</li>
<li><b>AlarmScannerController.java</b> performs initialization and all manipulations with the graphical interface.</li>
<li>
<b>Source.java</b> the object of the class stores the URL and the list of the last messages of the Telegram channel.
<br>
Downloads messages, determines message text and publication date, removes links to external resources and emojis.
<br>
The Jsoup library is used to analyze the html page.
</li>
<li>
<b>AlarmSources.java</b> the object of the class stores the list of the Source objects.
<br>
Performs read and write operations on a file <b>source.txt</b>
</li>
<li><b>AlarmKeywords.java</b> the object of the class stores the list of the keywords and a text representation of the contents of the window for editing key phrases.
<br>
Performs read and write operations on a file <b>keyword.txt</b>
</li>
<li><b>Message.java</b> the class object stores the message string, the URL from which the message was received, and the publication date.
<br>
Generates a string that the user sees.
</li>
<li><b>alarm_scanner-view.fxml</b> represents the markup of a GUI window.</li>
<li><b>source.txt</b> a list of Telegram channel URLs to scan. Each URL on a separate line.</li>
<li><b>keyword.txt</b> a list of semicolon-separated key phrases, UTF-8 encoding.
<br>
Since key phrases are stored primarily in the String variable, line breaks are also stored.
This helps to organize phrases into groups.
</li>
<li><b>pom.xml</b> an XML file that contains information about the project and configuration details used by Maven to build the project.</li>
</ol>
