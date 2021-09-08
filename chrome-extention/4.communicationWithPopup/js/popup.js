// 要在content_scripts里接收请求，否则会报错
chrome.tabs.getSelected(null, function (tab) {
    // Send a request to the content script.
    chrome.tabs.sendRequest(tab.id, { action: "getMessage" }, function (response) {
        document.getElementById('divTxt').innerHTML = JSON.stringify(tab) + ', ' + response.msg;

        console.log(response.msg); // 这个是输出在popup页面的控制台，而不是tab页的控制台，所以看不到
    });
});

/*
tab的属性值清单
{
    "active":true
    "audible":false
    "autoDiscardable":true
    "discarded":false
    "favIconUrl":"https://beinet.cn/favicon.ico"
    "groupId":-1
    "height":936
    "highlighted":true
    "id":409
    "incognito":false
    "index":12
    "mutedInfo":{"muted":false}
    "pinned":false
    "selected":true
    "status":"complete"
    "title":"403 Forbidden"
    "url":"https://beinet.cn/"
    "width":1920
    "windowId":72
}
*/