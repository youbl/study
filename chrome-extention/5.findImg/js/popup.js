// 要在content_scripts里接收请求，否则会报错
chrome.tabs.getSelected(null, function (tab) {
    // Send a request to the content script.
    chrome.tabs.sendRequest(tab.id, { action: 'img' }, function (response) {
        document.getElementById('divTxt').innerHTML = JSON.stringify(response);

        if (response.code !== 0 || response.data.length === 0) {
            return;
        }

        let root = document.getElementById('divImg');
        for (let i = 0, j = response.data.length; i < j; i++) {
            let src = response.data[i];
            appendImgNode(root, src);
        }
    });
});

/**
 * 在指定节点下添加图片子节点
 * @param {*} node  父节点
 * @param {*} src 图片的src
 */
function appendImgNode(node, src) {
    let element = document.createElement('IMG');
    element.src = src;
    node.appendChild(element);
}

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