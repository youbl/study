(function () {
    console.log('document_end后执行脚本1');

    chrome.extension.onRequest.addListener(function (request, sender, sendResponse) {
        sendResponse({ msg: "我收到你的请求了: " + JSON.stringify(request) });
    });
})();