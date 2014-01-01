




function showSavedData(){
    var url = AJS.contextPath() + 
              "/rest/api/1.0/projects/PROJECT_1/repos/rep_1" +
              "/settings/hooks" + 
              "/de.aeffle.stash.plugin.stash-http-get-post-receive-hook:http-get-post-receive-hook" +
		      "/settings";
			   
    AJS.$.getJSON(url, function(data) {
        niceString = JSON.stringify(data, null, 4);
        alert(niceString);
    });
}

