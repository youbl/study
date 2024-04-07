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
        addSecret();
    });
    // 重新生成页面所有otp-code
    document.getElementById('btnRefresh').addEventListener('click', function (){
        refreshCode();
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
    setInterval(refreshCode, 1001);
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
                    console.log(item[0] + ' 密钥: ' + item[1]);            
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

    getStorage()
        .then((arrSecrets)=>{
            if(arrSecrets) {
                let innerHtml = '';
                let codeTml = document.getElementById('codeItemTemp').innerHTML;
                
                let endTime = getCodeTimeLeft();
                Object.keys(arrSecrets).forEach((key) => {
                    let secret = arrSecrets[key];
                    let code = getCode(secret);
                    // console.log(key + ':' + secret + ' code:' + code);
                    let itemHtml = codeTml.replace(/\{\{desc\}\}/g, key)
                        .replace(/\{\{code\}\}/g, code)
                        .replace(/\{\{secret\}\}/g, secret)
                        .replace(/\{\{endTime\}\}/g, endTime);
                    innerHtml += itemHtml;
                });
                let container = document.getElementById('divCode');
                container.innerHTML = '<ul>' + innerHtml + '</ul>';
                // 等300ms, 防止未渲染
                setTimeout(()=>{
                    addCopyClick(container);
                    addDelClick(container);
                    __codeRefreshing = false;
                }, 300);
            }            
        });    
}

function addCopyClick(container){
    let btns = container.getElementsByClassName('copy-btn');
    for(let i=0,j=btns.length;i<j;i++){
        //let code = btns[i].parentNode.previousElementSibling.innerText;        
        btns[i].addEventListener('click', function () {
            let code = this.getAttribute('data');
            copyStr(code).then(()=>{
                showCustomAlert('复制成功:' + code);
            });
        });
    }
}

function addDelClick(container){
    let btns = container.getElementsByClassName('del-btn');
    for(let i=0,j=btns.length;i<j;i++){
        btns[i].addEventListener('click', function () {
            let desc = this.getAttribute('data');
            if(!confirm('确认要删除该密钥？注意：此操作无法恢复!'))
                return;
            removeSecret(desc);
        });
    }
}

// 添加一个说明和密钥
function addSecret(){
    let desc = document.getElementById('txtName').value.trim();
    let secret = document.getElementById('txtSecret').value.trim();
    if(!desc || !secret)
        return alert('请输入说明和密钥!');
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                arrSecrets = {};
            arrSecrets[desc] = secret;
            setStorage(arrSecrets)
                .then(refreshCode);
        });
    document.getElementById('dialogAdd').style.display = 'none';
}

// 根据说明，删除该密钥
function removeSecret(desc) {
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                return;
            delete arrSecrets[desc]; // 删除属性
            setStorage(arrSecrets)
                .then(refreshCode);
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
    return endTime - ts;
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
                console.log('Storage saved ok' + JSON.stringify(data));
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
            console.log('Storage get到的值为 ' + JSON.stringify(result));
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

// JavaScript
function showCustomAlert(message) {
    const alertElement = document.getElementById('customAlert');
    const alertContentElement = document.getElementById('alertContent');
    const closeButton = document.getElementById('closeButton');
  
    alertContentElement.textContent = message;
  
    // closeButton.addEventListener('click', () => {
    //     alertElement.style.display = 'none';
    // });
  
    alertElement.style.display = 'block';
    setTimeout(()=>{
        alertElement.style.display = 'none';
    }, 3000);
  }

