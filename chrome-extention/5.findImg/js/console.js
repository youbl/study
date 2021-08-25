(function () {
    console.log('document_end后执行脚本1');

    chrome.extension.onRequest.addListener(function (request, sender, sendResponse) {
        if (!request || !request.action) {
            sendResponse({ code: 500, msg: '未指定请求action' });
            return;
        }
        let ret = {};
        if (request.action === 'img') {
            ret = findImgAction();
        }
        sendResponse(ret);
    });
})();

/**
 * 遍历当前DOM，收到所有图片的src返回
 * 
 * @returns 当前页面的所有img.src
 */
function findImgAction() {
    let arrImgs = [];
    let root = document.documentElement;
    findImg(root, arrImgs);

    return { code: 0, data: arrImgs };
}


function findImg(node, arrImgs) {
    for (let i = 0, j = node.childNodes.length; i < j; i++) {
        let subnode = node.childNodes[i];
        if (!subnode.tagName)
            continue;

        if (subnode.tagName === 'IMG') {
            if (subnode.src) {
                arrImgs.push(subnode.src);
            }
            continue;
        }
        findImg(subnode, arrImgs);
    }
}
