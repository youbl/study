let __codeRefreshing = false;
const STORAGE_OTP_KEY = 'key';          // otp's key used in storage
const STORAGE_CONFIG_KEY = 'configs';   // global config's key used in storage
let __currentLang = 'en-US';            // start language
var __languageMap = null;             // all multi-language map

startRun();

function startRun() {
    loadAndSwitchLanguage();

    // get all dialog's close-button
    const closeBtns = document.getElementsByClassName('dialog-close');
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            // stop esc to close window
            event.preventDefault();

            // enable esc to close all dialog
            for(let i=0,j=closeBtns.length; i<j; i++) {
                if(!isHidden(closeBtns[i]))
                    closeBtns[i].click();
            }
        }
    });

    /** extensions can't add onclick for dom, so I can only add listener in js */

    // add event for: all dialog's close-button 
    for(let i=0,j=closeBtns.length; i<j; i++) {
        closeBtns[i].addEventListener('click', function (){
            this.parentNode.style.display = 'none';
        });
    }

    // show add-key dialog
    document.getElementById('btnShowAddCode').addEventListener('click', function (){
        clearCanvas();
        document.getElementById('dialogAdd').style.display = 'block';
        document.getElementById('txtName').focus();
    });
    // confirm event in add-key dialog
    document.getElementById('btnAddCode').addEventListener('click', function (){
        let desc = document.getElementById('txtName').value.trim();
        let secret = document.getElementById('txtSecret').value.trim();
        if(!desc || !secret)
            return alert('请输入说明和密钥!');
        addNode(desc, secret);            
        addSecret(desc, secret);
        document.getElementById('dialogAdd').style.display = 'none';
    });
    // export all otp key from storage to clipboard
    document.getElementById('btnExport').addEventListener('click', function (){
        exportSecrets();
    });
    // import otp key from clipboard, and save to storage
    document.getElementById('btnImport').addEventListener('click', function (){
        importSecrets();
    });
    // close current window
    document.getElementById('btnClose').addEventListener('click', function (){
        window.close();
    });
    // read otp key from qrcode file, triggered after file select
    document.getElementById('fileSelect').addEventListener('change', (evt) => {
        const file = evt.target.files[0];
        parseKeyFromQRCode(file);
    });

    refreshCode();
    // 设置每秒重新生成
    setInterval(refreshCode, 1000);
}

// read last language, and switch to this lang on app start.
async function loadAndSwitchLanguage() {
    // hidden current language button, show the others
    const langBtns = document.getElementsByClassName('multi-lang-btn');
    for(let i=0, j=langBtns.length; i<j; i++) {
        const btn = langBtns[i];
        const lang = btn.attributes['lang'].value;
        // switch language event
        clickListen(btn, () => {
            switchLanguage(lang);
        });
    }

    // change language by last selected
    let configs = await getConfigs();
    await switchLanguage(configs.lang);
}

function hideLanguageBtns() {
    const langBtns = document.getElementsByClassName('multi-lang-btn');
    for(let i=0, j=langBtns.length; i<j; i++) {
        const btn = langBtns[i];
        const lang = btn.attributes['lang'].value;
        if(lang === __currentLang) {
            btn.style.display = 'none';
        }else{
            btn.style.display = '';
        }
    }
}

async function switchLanguage(targetLang) {
    targetLang = targetLang ? targetLang : 'en-US';
    console.log('current: ', __currentLang, ' target: ', targetLang);
    if(targetLang === __currentLang) {
        hideLanguageBtns();
        return;
    }
    
    const allMap = await getLanguageJson();
    if(!allMap) return;
    console.log(allMap);

    // do language change
    const langMapKey = __currentLang + '|' + targetLang;
    changeDomTextByLanguage(allMap[langMapKey]);

    // switch ok, save to global config
    let configs = await getConfigs();
    __currentLang = targetLang;
    configs.lang = __currentLang;
    setConfigs(configs);

    hideLanguageBtns();
}

// combine multi-language json
// example: key=en-US|zh-CN value={'a':'b'}
async function getLanguageJson() {
    if(__languageMap === null) {
        const ret = {};

        const jsPath = 'js/zh-CN.json';
        // map for: en-US => zh-CN
        const mapEnToZh = await getJsonFromUrl(jsPath);
        ret['en-US|zh-CN'] = mapEnToZh;

        // map for: zh-CN => en-US
        const mapZhToUs = {};
        ret['zh-CN|en-US'] = mapZhToUs;
        for(let key in mapEnToZh) {
            if (!mapEnToZh.hasOwnProperty(key)) 
                continue;
            let val = mapEnToZh[key];
            mapZhToUs[val] = key;
        }

        // todo: can add other language map here
        __languageMap = ret;
    }
    return __languageMap;
}

function changeDomTextByLanguage(json) {
    if(!json) return;

    const domArr = document.getElementsByClassName('multi-lang');
    for(let i=0, j=domArr.length; i<j; i++) {
        const dom = domArr[i];
        const key = dom.innerText.trim();
        const langTxt = json[key];
        if(langTxt !== undefined) {
            dom.innerText = langTxt;
        }
    }
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

async function getConfigs() {
    let configs = await getStorage(STORAGE_CONFIG_KEY);
    if(!configs) {
        configs = {
            'lang': 'en-US',  // default language set
        };
    }
    return configs;
}

function setConfigs(configs) {
    setStorage(configs, STORAGE_CONFIG_KEY);
}

/**
 * 写入LocalStorage
 * @param {Object} val 写入存储的对象
 * @returns Promise对象
 */
function setStorage(val, key) {
    val = validVal(val) ? val : '';
    let data = { };
    key = key ? key : STORAGE_OTP_KEY;
    data[key] = val;
    
    return new Promise((resolve, reject) => {
        chrome.storage.sync.set(data, ()=>{
            if (chrome.runtime.lastError) {
                reject(chrome.runtime.lastError);
              } else {
                console.log(key + ' saved to Storage: ' + JSON.stringify(data, null, 4));
                resolve(val);
              }
        });
      });
}

/**
 * 读取LocalStorage
 * @returns Promise对象
 */
function getStorage(key) {
    key = key ? key : STORAGE_OTP_KEY;
    return new Promise((resolve, reject) => {
        chrome.storage.sync.get(key, function(result) {
          if (chrome.runtime.lastError) {
            console.error(chrome.runtime.lastError);
            reject(chrome.runtime.lastError);
          } else {
            console.log(key + ' geted from Storage: ' + JSON.stringify(result, null, 4));
            let ret = validVal(result[key]) ? result[key] : '';
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

// 清理以前的canvas图像
function clearCanvas() {
    const canvas = document.getElementById("canvas"); // 获取canvas
    const ctx = (canvas).getContext('2d', {willReadFrequently: true});
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// 从二维码图片文件里解析出密钥
function parseKeyFromQRCode(file) {
    const canvas = document.getElementById("canvas"); // 获取canvas
    const URL = window.URL || window.webkitURL; // 兼容
    const url = URL.createObjectURL(file);   // 获取文件的临时路径(antd upload组件上传的对象为{originFileObj:File},对象中的originFileObj才是file对象)
    const img = new Image();  // 创建图片对象
    img.onload = function () {
        // 根据图片大小设置canvas大小
        canvas.width = img.width;
        canvas.height = img.height;
        // 释放对象URL所占用的内存
        URL.revokeObjectURL(img.src);
        // 获取canvas
        const context = (canvas).getContext('2d', {willReadFrequently: true});
        // canvas绘制图片
        context.drawImage(this, 0, 0, img.width, img.height);
        // 通过canvas获取imageData
        const imageData = context.getImageData(0, 0, img.width, img.height);
        // jsQR识别
        const code = jsQR(imageData.data, imageData.width, imageData.height);
        if (!code || !code.data) {
            return alert('The file isn\'t a otp image：' + code);
        }
        console.log("Found QR code", code.data);
        let qrKey = parseSecretFromCode(code.data);
        if(!qrKey) {
            qrKey = parseSecretFromGoogleAppExport(code.data);
        }
        if(!qrKey) {
            return alert('The file doesn\'t contain otp-key：' + code);
        }
        document.getElementById('txtSecret').value = qrKey;
    };
    img.src = url;  // 给img标签设置src属性
}

// 从标准的otp字符串中，解析secret密钥数据
function parseSecretFromCode(code) {
    if (!code)
        return '';
    // "otpauth://totp/AmazonWebServices:mfa-abc?secret=abc&issuer=AmazonWebServices"
    let start = 'secret=';
    let idx = code.indexOf(start);
    if (idx < 0) 
        return '';
    let ret = code.substring(idx + start.length);
    idx = ret.indexOf('&');
    if (idx > 0)
        ret = ret.substring(0, idx);
    return ret;
}

// 从Google Authenticator APP导出的otp字符串中，解析secret密钥数据
function parseSecretFromGoogleAppExport(codeData) {
    if (codeData.indexOf('otpauth-migration://offline?data=') !== 0) {
        return '';
    }
    let url = $$BASE_URL + 'otpcode/convertGoogleCode?code=' + encodeURIComponent(codeData);
    return axios.get(url).then(response => {
        parseSecretFromCode(response.data);
    }).catch(error => {
        alert(error);
    });
}

function clickListen(btnId, handler) {
    if(btnId === null || btnId === undefined || btnId === '') {
        return alert('btnId can not be empty.');
    }
    let btn;
    if(typeof(btnId) === 'string')
        btn = document.getElementById(btnId);
    else 
        btn = btnId;
    if(!btn) {
        return alert('btn="' + btnId + '" can not exists.');
    }
    btn.addEventListener('click', handler);
}

// check the element is unvisible or not
function isHidden(el) {
    return (el.offsetParent === null);
    //let style = window.getComputedStyle(el);
    //while(style) {
    //    if (style.display === 'none')
    //        return true;
    //    style = el.parentNode ? window.getComputedStyle(el.parentNode) : null;
    //}
    //return false;
}

// sync get json-data from the url
async function getJsonFromUrl(url) {
    const response = await fetch(url);
    if (!response.ok) {
        return null;
    }
    return await response.json();
}