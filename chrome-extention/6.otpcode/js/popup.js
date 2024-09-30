let __codeRefreshing = false;
const STORAGE_OTP_KEY = 'key';          // otp's key used in storage
const STORAGE_CONFIG_KEY = 'configs';   // global config's key used in storage
let __currentLang = 'en-US';            // start language
var __languageMap = null;               // all multi-language map

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
            return alert('title and key are required!');
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
    // regenerate otp-code per second
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
                return showCustomAlert('no data can export');
            }
            copyStr(ret).then(()=>{
                showCustomAlert('exported to clipboard, please save and import next time.');
            });            
        });    
}

function importSecrets() {
    readCopy().then(text=>{
        if(!text)
            return showCustomAlert('no data in clipboard');
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
            return showCustomAlert('the data in clipboard is not valid.');
        if(!confirm('confirm to import these ' + result.length + ' line data? Note: key with same title will be replaced'))
            return;
        getStorage()
            .then((arrSecrets)=>{
                if(!arrSecrets)
                    arrSecrets = {};
                for(let i=0,j=result.length;i<j;i++){
                    let item = result[i];
                    //console.log(item[0] + ' key: ' + item[1]);            
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
            // full load at first
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
    
        // only refresh code and time, don't refresh others
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
    
            endTimeNode.innerText = endTime + 's';
            if(code !== copyCodeNode.getAttribute('data')) {
                copyCodeNode.innerText = code;
                copyCodeNode.setAttribute('data', code);
            }
        }
    }catch(e){
        alert('err:' + e.message);
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
                    showCustomAlert('copyed: ' + code);
                });
            });
            btns[i].setAttribute('bindclick', 1); // avoid repeatedly bind.
        }
    }
}

function addDelClick(container){
    let btns = container.getElementsByClassName('del-btn');
    for(let i=0,j=btns.length;i<j;i++){
        if(btns[i].getAttribute('bindclick') === null) {
            btns[i].addEventListener('click', function () {
                let desc = this.getAttribute('data');
                if(!confirm('Confirm del?Note:can\'t restore!'))
                    return;
                removeNode(this);
                removeSecret(desc);
            });
            btns[i].setAttribute('bindclick', 1); // avoid repeatedly bind.
        }
    }
}

// add a line to page table
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
    // wait a moment, to avoid render not ok
    setTimeout(()=>{
        let liList = root.getElementsByTagName('LI');
        for(let i=0,j=liList.length;i<j;i++){
        let container = liList[i]; 
        addCopyClick(container);
        addDelClick(container);}
        __codeRefreshing = false;
    }, 50);
}

// add a record into LocalStorage
function addSecret(desc, secret){
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                arrSecrets = {};
            arrSecrets[desc] = secret;
            setStorage(arrSecrets);
        });
}

// remove btn's parentNode(delete the current row)
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

// delete a key according desc
function removeSecret(desc) {
    getStorage()
        .then((arrSecrets)=>{
            if(!arrSecrets)
                return;
            delete arrSecrets[desc];
            setStorage(arrSecrets);
        });
}

/**
 * generate a otp-code
 * 
 * @param {string} secret otp-key
 * @returns code
 */
function getCode(secret) {
    if (!secret) {
        return '';
    }
    let totp = new OTPAuth.TOTP({
        issuer: 'youbl',
        algorithm: "SHA1",
        digits: 6,                  // number of otp-code bits
        period: 30,                 // generate a code per 30 seconds
        secret: secret,
    });
    return totp.generate();
}

/**
 * generate left-time for current otp-code
 * 
 * @returns left-time
 */
function getCodeTimeLeft() {
    let ts = Math.floor(Date.now()/1000); // current timestamp
    let beginTime = Math.floor(ts / 30) * 30;
    let endTime = beginTime + 30;
    let ret = endTime - ts;
    if(ret > 9)
        return ret.toString();
    return '0' + ret.toString();
}

/**
 * read global config from LocalStorage
 * 
 * @returns configs
 */
async function getConfigs() {
    let configs = await getStorage(STORAGE_CONFIG_KEY);
    if(!configs) {
        configs = {
            'lang': 'en-US',  // default language set
        };
    }
    return configs;
}

/**
 * save global config to LocalStorage
 */
function setConfigs(configs) {
    setStorage(configs, STORAGE_CONFIG_KEY);
}

/**
 * save data to LocalStorage
 *
 * @param {Object} val data
 * @returns Promise
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
 * read data from LocalStorage
 *
 * @returns Promise
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

/**
 * set str to clipboard
 */
function copyStr(str) {
    return navigator.clipboard.writeText(str);
}

/**
 * read str data from clipboard
 */
function readCopy() {
    return navigator.clipboard.readText();
}

// these 2 var, used to avoid multi setTimeout executed, cause alert-win closed early
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

// clear canvas's data before
function clearCanvas() {
    const canvas = document.getElementById("canvas");
    const ctx = (canvas).getContext('2d', {willReadFrequently: true});
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// parse otp-key from qrcode image
function parseKeyFromQRCode(file) {
    const canvas = document.getElementById("canvas");
    const URL = window.URL || window.webkitURL;
    const url = URL.createObjectURL(file);
    const img = new Image();
    img.onload = function () {
        canvas.width = img.width;
        canvas.height = img.height;
        // release URL memory
        URL.revokeObjectURL(img.src);
        
        const context = (canvas).getContext('2d', {willReadFrequently: true});
        context.drawImage(this, 0, 0, img.width, img.height);
        const imageData = context.getImageData(0, 0, img.width, img.height);
        // read data by jsQR
        const code = jsQR(imageData.data, imageData.width, imageData.height);
        if (!code || !code.data) {
            return alert('The file isn\'t a otp image：' + code);
        }
        console.log("Found QR code", code.data);
        let qrKey = parseSecretFromCode(code.data);
        if(!qrKey) {
            qrKey = parseSecretFromGoogleAppExport(code.data);
            // only get the first key
            if(qrKey && qrKey.length > 0) {
                qrKey = qrKey[0].secret;
            }
        }
        if(!qrKey) {
            return alert('The file doesn\'t contain otp-key：' + code);
        }
        document.getElementById('txtSecret').value = qrKey;
    };
    img.src = url;  // set img.src, to trigger img.onload
}

// parse otp-key from a standard OTP string
// standard like: "otpauth://totp/AmazonWebServices:mfa-abc?secret=abc&issuer=AmazonWebServices"
function parseSecretFromCode(code) {
    if (!code)
        return '';
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



/**
 * parse otp-key from a string exported by "Google Authenticator APP"
 * @param {string} otpauthString - a string start with "otpauth-migration://offline"
 * @returns {Array} array contains all otp-key info, demo: [{name: 'name-1', secret: 'secret-1'},{name: 'name-2', secret: 'secret-2'}]
 */
function parseSecretFromGoogleAppExport(otpauthString) {
  if (!otpauthString.startsWith('otpauth-migration://offline')) {
    throw new Error('invalid otpauth string');
  }

  try {
    // remove url scheme
    const decodedString = decodeURIComponent(otpauthString.replace('otpauth-migration://offline?data=', ''));    
    // Base64 decode
    const decodedData = atob(decodedString);
    
    // convert to Uint8Array
    const uint8Array = new Uint8Array(decodedData.length);
    for (let i = 0; i < decodedData.length; i++) {
      uint8Array[i] = decodedData.charCodeAt(i);
    }
    
    // parse data by pre-defined struct
    const mfaKeys = parseMigrationPayload(uint8Array);

    return mfaKeys;
  } catch (error) {
    console.error('parse otpauth string error:', error);
    throw new Error('parse otpauth string error');
  }
}

/**
 * parse MigrationPayload struct
 * @param {Uint8Array} data - binary data to parse
 * @returns {Array} array contains all otp-key info
 */
function parseMigrationPayload(data) {
  let offset = 0;
  const mfaKeys = [];

  while (offset < data.length) {
    const tag = data[offset] >> 3;
    offset++;

    if (tag === 1) { // otpParameters
      const length = readVarint(data, offset);
      offset += varintLength(length);
      
      const endOffset = offset + length;
      const otpParameter = parseOtpParameter(data.slice(offset, endOffset));
      if (otpParameter) {
        mfaKeys.push(otpParameter);
      }
      
      offset = endOffset;
    } else {
      // skip unknown field
      const wireType = data[offset - 1] & 0x7;
      if (wireType === 0) {
        offset += varintLength(readVarint(data, offset));
      } else if (wireType === 2) {
        const length = readVarint(data, offset);
        offset += varintLength(length) + length;
      } else {
        console.warn('unknown wire type:', wireType, 'at offset:', offset - 1);
        break; // break while loop
      }
    }
  }

  return mfaKeys;
}

/**
 * parse OtpParameter struct
 * @param {Uint8Array} data - binary data to parse
 * @returns {Object|null} object contains name and secret, or null
 */
function parseOtpParameter(data) {
  let offset = 0;
  let name, secret;

  while (offset < data.length) {
    const tag = data[offset] >> 3;
    offset++;

    if (tag === 1) { // secret
      const length = readVarint(data, offset);
      offset += varintLength(length);
      let secretArr = data.slice(offset, offset + length);
      secret = base32Encode(secretArr);
      offset += length;
    } else if (tag === 2) { // name
      const length = readVarint(data, offset);
      offset += varintLength(length);
      name = new TextDecoder().decode(data.slice(offset, offset + length));
      offset += length;
    } else {
      // skip unknown field
      const wireType = data[offset - 1] & 0x7;
      if (wireType === 0) {
        offset += varintLength(readVarint(data, offset));
      } else if (wireType === 1) {
        offset += 8;
      } else if (wireType === 2) {
        const length = readVarint(data, offset);
        offset += varintLength(length) + length;
      } else if (wireType === 5) {
        offset += 4;
      } else {
        console.warn('unknown wire type:', wireType, 'at offset:', offset - 1);
        // try to skip this field
        offset++;
      }
    }
  }

  return name && secret ? { name, secret } : null;
}

/**
 * read varint encoded integer
 * @param {Uint8Array} data - data contains varint
 * @param {number} offset - start position
 * @returns {number} decoded integer
 */
function readVarint(data, offset) {
  let result = 0;
  let shift = 0;
  let byte;

  do {
    byte = data[offset++];
    result |= (byte & 0x7f) << shift;
    shift += 7;
  } while (byte & 0x80);

  return result;
}

/**
 * calculate varint encoded length
 * @param {number} value - integer to calculate length
 * @returns {number} varint encoded length
 */
function varintLength(value) {
  let length = 0;
  while (value > 0) {
    value >>= 7;
    length++;
  }
  return length || 1;
}

function base32Encode(data) {
    return base32.encode(data);
}