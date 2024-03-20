startRun();

function startRun() {
    document.getElementById('btnAddCode').addEventListener('click', function (){
        addSecret();
    });

    refreshCode();
}

function refreshCode(){
    getAsync()
        .then((arrSecrets)=>{
            if(arrSecrets) {
                let innerHtml = '';
                Object.keys(arrSecrets).forEach((key) => {
                    let secret = arrSecrets[key];
                    let code = getCode(secret);
                    // console.log(key + ':' + secret + ' code:' + code);
                    innerHtml += '<div>' + key +':'+code +'</div>';
                });
                let container = document.getElementById('divCode');
                container.innerHTML = innerHtml;
            }            
        });    
}

function addSecret(){
    let desc = document.getElementById('txtName').value;
    let secret = document.getElementById('txtSecret').value;
    getAsync()
        .then((arrSecrets)=>{
            if(!arrSecrets)
            arrSecrets = {};
            arrSecrets[desc] = secret;
            setAsync(arrSecrets)
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

function setAsync(val) {
    val = validVal(val)?val:'';
    let data = {key:val};
    
    return new Promise((resolve, reject) => {
        chrome.storage.local.set(data, ()=>{
            if (chrome.runtime.lastError) {
                reject(chrome.runtime.lastError);
              } else {
                console.log('Storage saved ok' + JSON.stringify(data));
                resolve(val);
              }
        });
      });
}

function getAsync() {
    return new Promise((resolve, reject) => {
        chrome.storage.local.get('key', function(result) {
          if (chrome.runtime.lastError) {
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
