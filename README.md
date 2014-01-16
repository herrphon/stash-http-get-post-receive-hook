## The story of this plugin is as follows: ##

I stumbled over a problem were I wanted to have a post-receive hook to trigger some Jenkins without having a clone of this repository laying around. The Stash Jenkins plugin (Stash Post-Receive Webhook to Jenkins) that is available is nice but unfortunately has the requirement that I have to have a clone of that repository laying around. 

With this simple plugin you can trigger Jenkins by doing the following and not having to clone the repo:

1. Get your API-Token in the Jenkins user settings
2. Configure the URL in the Stash Plugin (http://jenkins.url/job/my_job_title/build
3. Set the username to your username
4. Set the password to the API-Token key
5. Enjoy!


## What I plan to do: ##

* add the possiblity to trigger multiple URLs


## Enable debug logging: ##
To enable more debug output add the following to the file `./stash/webapp/WEB-INF/classes/logback.xml`

`<!-- Turn on maximum logging for HttpGetPostReceiveHook plugin -->`
`<logger name="de.aeffle.stash.plugin.hook.HttpGetPostReceiveHook" level="DEBUG"/>`
