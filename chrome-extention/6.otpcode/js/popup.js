let __codeRefreshing = false;

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
    // 显示添加密钥的对话框
    document.getElementById('btnShowAddCode').addEventListener('click', function (){
        document.getElementById('dialogAdd').style.display = 'block';
    });
    // 所有对话框的关闭按钮添加事件
    let closeBtns = document.getElementsByClassName('dialog-close');
    for(let i=0,j=closeBtns.length;i<j;i++){
        closeBtns[i].addEventListener('click', function (){
            this.parentNode.style.display = 'none';
        });
    }
    // 添加密钥对话框里的确认添加按钮事件
    document.getElementById('btnAddCode').addEventListener('click', function (){
        let desc = document.getElementById('txtName').value.trim();
        let secret = document.getElementById('txtSecret').value.trim();
        if(!desc || !secret)
            return alert('请输入说明和密钥!');
        addNode(desc, secret);            
        addSecret(desc, secret);
        document.getElementById('dialogAdd').style.display = 'none';
    });
    // 导出页面所有otp密钥到粘贴板
    document.getElementById('btnExport').addEventListener('click', function (){
        exportSecrets();
    });
    // 从粘贴板导入所有otp密钥
    document.getElementById('btnImport').addEventListener('click', function (){
        importSecrets();
    });
    // 关闭页面按钮
    document.getElementById('btnClose').addEventListener('click', function (){
        window.close();
    });

    refreshCode();
    // 设置每秒重新生成
    setInterval(refreshCode, 1000);
}

function exportSecrets() {
    getStorage()
        .then((arrSecrets)=>{
            let ret = '';
            if(arrSecrets) {
                Object.keys(arrSecrets).forEach((key) => {
                    let secret = arrSecrets[key];
                    ret += key + ':' + secret + '\n';
                });
            }
            if(!ret){
                return showCustomAlert('无数据可以导出');
            }
            copyStr(ret).then(()=>{
                showCustomAlert('已成功导出到粘贴板，请维持格式，复制到目标电脑粘贴');
            });            
        });    
}

function importSecrets() {
    readCopy().then(text=>{
        if(!text)
            return showCustomAlert('粘贴板没有找到要导入的数据');
        let arr = text.split(/[\r\n]/g);
        let result = [];
        for(let i=0,j=arr.length;i<j;i++) {
            let item = arr[i].trim();
            if(item.length === 0) 
                continue;
            let idx = item.lastIndexOf(':');
            if(idx <= 0 || idx >= item.length - 1)
                continue;
            let desc = item.substring(0, idx);
            let secret = item.substring(idx+1);
            result.push([desc, secret]);
        }
        
        if(result.length <= 0)
            return showCustomAlert('粘贴板没有要导入的数据');
        if(!confirm('确认要导入这' + result.length + '行数据吗？注意：相同说明的密钥会被替换！'))
            return;
        getStorage()
            .then((arrSecrets)=>{
                if(!arrSecrets)
                    arrSecrets = {};
                for(let i=0,j=result.length;i<j;i++){
                    let item = result[i];
                    //console.log(item[0] + ' 密钥: ' + item[1]);            
                    arrSecrets[item[0]] = item[1];
                }
                setStorage(arrSecrets)
                    .then(refreshCode);
            });
    });
}

function refreshCode(){
    if(__codeRefreshing)
        return;
    __codeRefreshing = true;
    
    try{
        let root = document.getElementById('divCode');
        let ulList = root.getElementsByTagName('UL');
        if(ulList.length <= 0){
            // 首次要完整加载一下
            getStorage()
                .then((arrSecrets)=>{
                    if(arrSecrets) {
                        Object.keys(arrSecrets).forEach((key) => {
                            let secret = arrSecrets[key];
                            addNode(key, secret);
                        });
                    }
                });    
            return;
        }
    
        // 刷新时，只刷新code和时间，别的不动，避免影响
        let liList = ulList[0].getElementsByTagName('LI');
        let endTime = getCodeTimeLeft();
        for(let i=0,j=liList.length;i<j;i++) {
            let node = liList[i];
            let copyNodes = node.getElementsByClassName('copy-btn');
            let copyCodeNode = copyNodes[1];
            let copySecretNode = copyNodes[0];
            let endTimeNode = node.getElementsByClassName('endTime')[0];
    
            let secret = copySecretNode.getAttribute('data');
            let code = getCode(secret);
    
            endTimeNode.innerText = endTime + '秒';
            if(code !== copyCodeNode.getAttribute('data')) {
                // 不同时才渲染
                copyCodeNode.innerText = code;
                copyCodeNode.setAttribute('data', code);
            }
        }
    }catch(e){
        alert('出错了:' + e.message);
    }finally{
        __codeRefreshing = false;
    }
}

function addCopyClick(container){
    let btns = container.getElementsByClassName('copy-btn');
    for(let i=0,j=btns.length;i<j;i++){
        //let code = btns[i].parentNode.previousElementSibling.innerText;
        if(btns[i].getAttribute('bindclick') === null) {
            btns[i].addEventListener('click', function () {
                let code = this.getAttribute('data');
                copyStr(code).then(()=>{
                    showCustomAlert('复制成功:' + code);
                });
            });
            btns[i].setAttribute('bindclick', 1); // 避免重复绑定多次事件
        }
    }
}

function addDelClick(container){
    let btns = container.getElementsByClassName('del-btn');
    for(let i=0,j=btns.length;i<j;i++){
        if(btns[i].getAttribute('bindclick') === null) {
            btns[i].addEventListener('click', function () {
                let desc = this.getAttribute('data');
                if(!confirm('确认要删除该密钥？注意：此操作无法恢复!'))
                    return;
                removeNode(this);
                removeSecret(desc);
            });
            btns[i].setAttribute('bindclick', 1); // 避免重复绑定多次事件
        }
    }
}

// 添加一行
function addNode(desc, secret) {
    let codeTml = document.getElementById('codeItemTemp').innerHTML;
    
    let endTime = getCodeTimeLeft();
    let code = getCode(secret);
    // console.log(key + ':' + secret + ' code:' + code);
    let itemHtml = codeTml.replace(/\{\{desc\}\}/g, desc)
        .replace(/\{\{code\}\}/g, code)
        .replace(/\{\{secret\}\}/g, secret)
        .replace(/\{\{endTime\}\}/g, endTime);

    let root = document.getElementById('divCode');
    let ulList = root.getElementsByTagName('UL');
    let container = null;
    if(ulList.length > 0){
        ulList[0].innerHTML += itemHtml;
        
    }else{
        root.innerHTML = '<ul>' + itemHtml + '</ul>';
    }
    // 等一等, 防止未渲染
    setTimeout(()=>{
        let liList = root.getElementsByTagName('LI');
        for(let i=0,j=liList.length;i<j;i++){
        let container = liList[i]; 
        addCopyClick(container);
        addDelClick(container);}
        __codeRefreshing = false;
    }, 50);
}

// 往LocalStorage里，添加一个说明和密钥
function addSecret(desc, secret){
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                arrSecrets = {};
            arrSecrets[desc] = secret;
            setStorage(arrSecrets);
        });
}

// 删除当前btn节点的父li节点，即删除当前行
function removeNode(btn) {
    let parent = btn.parentNode;
    while(parent) {
        if(parent.tagName.toLowerCase() === 'li') {
            parent.remove();
            break;
        }
        parent = parent.parentNode;
    }
}

// 根据说明，从LocalStorage中删除该密钥
function removeSecret(desc) {
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                return;
            delete arrSecrets[desc]; // 删除属性
            setStorage(arrSecrets);
        });
}

/**
 * 根据密钥生成otp code
 * @param {string} secret 密钥
 * @returns code
 */
function getCode(secret) {
    if (!secret) {
        return '';
    }
    let totp = new OTPAuth.TOTP({
        issuer: 'youbl',            // 生成的url里的发行者
        algorithm: "SHA1",          // 使用的算法
        digits: 6,                  // 生成的otp位数
        period: 30,                 // 时间窗口，单位秒，每30秒生成一次
        secret: secret,        // 生成otp使用的密钥
    });
    return totp.generate();    // 生成密钥
}

/**
 * 计算otp code的剩余时间，每30秒生成一个
 */
function getCodeTimeLeft() {
    let ts = Math.floor(Date.now()/1000); // 当前时间戳
    let beginTime = Math.floor(ts / 30) * 30;
    let endTime = beginTime + 30;
    let ret = endTime - ts;
    if(ret > 9)
        return ret.toString();
    return '0' + ret.toString();
}

/**
 * 写入LocalStorage
 * @param {Object} val 写入存储的对象
 * @returns Promise对象
 */
function setStorage(val) {
    val = validVal(val)?val:'';
    let data = {key:val};
    
    return new Promise((resolve, reject) => {
        chrome.storage.sync.set(data, ()=>{
            if (chrome.runtime.lastError) {
                reject(chrome.runtime.lastError);
              } else {
                //console.log('Storage saved ok' + JSON.stringify(data));
                resolve(val);
              }
        });
      });
}


/**
 * 读取LocalStorage
 * @returns Promise对象
 */
function getStorage() {
    return new Promise((resolve, reject) => {
        chrome.storage.sync.get('key', function(result) {
          if (chrome.runtime.lastError) {
            console.error(chrome.runtime.lastError);
            reject(chrome.runtime.lastError);
          } else {
            //console.log('Storage get到的值为 ' + JSON.stringify(result));
            let ret = validVal(result.key) ? result.key : '';
            resolve(ret);
          }
        });
      });
}

function validVal(val){
    if(val === undefined || val === null)
        return false;
    return val !== '';
}

// 把指定的字符串复制到剪切板
function copyStr(str) {
    return navigator.clipboard.writeText(str);
}

// 从剪切板读取文本数据
function readCopy() {
    return navigator.clipboard.readText();
}

// 这2个变量，用于避免执行多个setTimeout，导致后面的alert被提前关闭
var __customAlertSecond = 3;
var __customAlertRuning = false;
function showCustomAlert(message) {
    __customAlertSecond = 3;
    const alertElement = document.getElementById('customAlert');
    const alertContentElement = document.getElementById('alertContent');
    alertContentElement.textContent = message;
    alertElement.style.display = 'block';

    if(__customAlertRuning)
        return;
    __customAlertRuning=true;
    hideCustomAlert();
}

function hideCustomAlert() {
    if(__customAlertSecond > 0){
        __customAlertSecond--;
        setTimeout(hideCustomAlert, 1000);
        return;
    }
    __customAlertRuning = false;
    const alertElement = document.getElementById('customAlert');
    alertElement.style.display = 'none';
}