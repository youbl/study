let __codeRefreshing = false;

startRun();

function startRun() {
    document.getElementById('btnAddCode').addEventListener('click', function (){
        addSecret();
    });
    document.getElementById('btnRefresh').addEventListener('click', function (){
        refreshCode();
    });

    refreshCode();
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
                Object.keys(arrSecrets).forEach((key) => {
                    let secret = arrSecrets[key];
                    let code = getCode(secret);
                    // console.log(key + ':' + secret + ' code:' + code);
                    let itemHtml = codeTml.replace(/\{\{desc\}\}/g, key).replace(/\{\{code\}\}/g, code).replace(/\{\{secret\}\}/g, secret);
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
            copyStr(code);
            showCustomAlert('复制成功:' + code);
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
    let desc = document.getElementById('txtName').value;
    let secret = document.getElementById('txtSecret').value;
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                arrSecrets = {};
            arrSecrets[desc] = secret;
            setStorage(arrSecrets)
                .then(refreshCode);
        });
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
    const el = document.createElement('textarea');
    el.value = str;
    el.setAttribute('readonly', '');
    el.style.position = 'absolute';
    el.style.left = '-9999px';
    document.body.appendChild(el);
    const selected = document.getSelection().rangeCount > 0 ? document.getSelection().getRangeAt(0) : false;
    el.select();
    document.execCommand('copy');
    document.body.removeChild(el);
    if (selected) {
        document.getSelection().removeAllRanges();
        document.getSelection().addRange(selected);
    }
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