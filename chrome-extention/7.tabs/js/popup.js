startRun();

function startRun() {

    // 阻止esc关闭窗口
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
          event.preventDefault(); // 阻止默认的 Esc 按钮行为
          // 这里可以添加你的自定义操作
        }
    });

    /** 因为插件开发，不让直接在html里添加onclick属性，只能在js里添加监听 */
    // 关闭页面按钮
    document.getElementById('btnClose').addEventListener('click', function (){
        window.close();
    });

    // 所有对话框的关闭按钮添加事件
    let closeBtns = document.getElementsByClassName('dialog-close');
    for(let i=0,j=closeBtns.length;i<j;i++){
        closeBtns[i].addEventListener('click', function (){
            this.parentNode.style.display = 'none';
        });
    }

    showTabInfos();
}

function showTabInfos() {
    // query第一个参数是查询条件，第二个参数是结果回调
    chrome.tabs.query({}, (tabs) => {
        let innerHtml = '';
        tabs.forEach((tabInfo) => {
          //chrome.tabs.get(tab.id, function(tabInfo) {
            const title = tabInfo.title;
            const url = tabInfo.url;

            let itemTml = document.getElementById('dataItemTemp').innerHTML;
            let itemHtml = itemTml
                .replace(/\{\{title\}\}/g, title)
                .replace(/\{\{titleShort\}\}/g, cutstr(title, 20, '..'))
                .replace(/\{\{url\}\}/g, url)
                .replace(/\{\{urlShort\}\}/g, cutstr(url, 40, '..'));
            innerHtml += itemHtml;

            console.log(tabInfo);
        });
        document.getElementById('objTabInfo').innerHTML = innerHtml;
    });
}

// 用浮层展示alert信息
function showCustomAlert(message) {
    const alertElement = document.getElementById('customAlert');
    const alertContentElement = document.getElementById('alertContent');
    alertContentElement.textContent = message;
    alertElement.style.display = 'block';
    setTimeout(()=>{
        alertElement.style.display = 'none';
    }, 3000);
}

// 计算带中文字符的字符串长度
function lenb(str) {
    if (str === null || str === undefined)
        return 0;

    if (typeof (str) !== 'string') {
        str = str.toString();
    }
    let ret = 0;
    for (let i = 0, j = str.length; i < j; i++) {
        let code = str.charCodeAt(i);
        ret += (code < 0 || code > 255) ? 2 : 1;
    }
    return ret;
}

// 如果str长度大于len，按len返回裁剪后的字符串
function cutstr(str, cutLen, sufix) {
    if (str === null || str === undefined)
        return '';
    if (cutLen <= 0)
        return str;
    let realLen = lenb(str);
    if (realLen <= cutLen) {
        return str;
    }

    let ret = '';
    let retLen = 0;
    for (let i = 0, j = str.length; i < j; i++) {
        let ch = str.charAt(i);
        let code = str.charCodeAt(i);
        retLen += (code < 0 || code > 255) ? 2 : 1;
        if (retLen > cutLen)
            break;
        ret += ch;
    }
    if (sufix !== undefined)
        ret += sufix;
    return ret;
}