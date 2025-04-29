const $$BASE_URL = ['localhost', '10.100.73.40'].includes(location.hostname) ? 'http://10.100.72.46:9999/cb-ops/' : '/' + location.pathname.split('/')[1] + '/'; // '/ops/';

const DEFAULT_PRODUCT = 'ZiBird';

const $$langs = ["en-US", "zh-CN", "vi-VN"];
const $$envs = [{ value: 'client-home', label: 'home主站' }, { value: 'official', label: '官网' }, { value: 'blog', label: '博客' }];
/**
 * 把对象转换为字符串，并去空格返回
 *
 * @param obj 对象
 * @param noTrim 为true时，不进行trim，默认false
 * @returns {string} 字符串
 */
function toStr(obj, noTrim) {
    if (obj === null || obj === undefined)
        return '';
    if (noTrim === true)
        return obj.toString();
    else
        return obj.toString().trim();
}

/**
 * 从当前window上下文查找vue实例
 *
 * @param domId vue的根dom对象id，可空
 * @returns {*|Window.Vue|void}
 */
function getVueInstance(domId) {
    if (!window.Vue) {
        return null;// alert('当前页未引用Vue.js');
    }

    if (window.vueApp && window.vueApp instanceof window.Vue) {
        return window.vueApp;
    }
    if (domId === null || domId === undefined) {
        domId = 'divApp';
    }
    let dom = document.getElementById(domId);
    if (dom && dom.__vue__ && dom.__vue__ instanceof window.Vue) {
        return dom.__vue__;
    }
    return null;//alert('未找到Vue实例，请指定正确的dom元素id');
}

/**
 * 弹出消息
 * @param msg 要显示的消息
 * @param duration 多久后自动关闭，毫秒，默认0不关闭
 */
function vueAlert(msg, duration) {
    let vue = getVueInstance();
    if (!vue)
        return alert(msg);

    if (duration === undefined)
        duration = 0;
    return vue.$message({
        message: msg,
        type: 'success',
        duration: duration,
        showClose: true,
    });
}

/**
 * 获取url里的变量值
 * @param {string} name 变量名
 * @return {string} 变量值
 */
function getQueryString(name) {
    if (typeof (name) !== 'string') {
        return '';
    }
    name = name.trim();
    if (name.length === 0) {
        return '';
    }
    let localSearch = location.search.toLocaleLowerCase();
    name = name.toLowerCase() + '=';
    let tmpName = '?' + name;
    let idx = localSearch.indexOf(tmpName);
    if (idx < 0) {
        tmpName = '&' + name;
        idx = localSearch.indexOf(tmpName);
        if (idx < 0) {
            return '';
        }
    }
    name = tmpName;
    let tmp = location.search.substring(idx + name.length);
    idx = tmp.indexOf('&');
    if (idx === 0)
        return '';
    let ret;
    if (idx < 0) {
        ret = tmp;
    } else {
        ret = tmp.substring(0, idx);
    }
    return decodeURIComponent(ret.trim());
}

/**
 * 获取url里的变量数字，不存在时返回0
 *
 * @param {string} name 变量名
 * @return {number} 数字或0
 */
function getQueryInt(name) {
    let tmp = getQueryString(name);
    if ((/^-?\d+$/).test(tmp)) {
        return parseInt(tmp, 10);
    }
    return 0;
}

/**
 * window.open是GET方式，此方法是POST方式，在指定窗口打开页面。
 * 注：不支持Content-type: application/json
 *
 * @param url 页面地址
 * @param data POST数据
 * @param target 目标窗口
 * @param target 目标窗口
 */
function postToNewWindow(url, data, contentType, target) {
    // 创建一个隐藏的表单
    const form = document.createElement('form');
    form.action = url;
    form.method = 'POST';
    form.enctype = ''; // 默认为 application/x-www-form-urlencoded
    // 默认在新的窗口中打开
    form.target = target === undefined ? '_blank' : target;

    // 将数据添加到表单中作为隐藏字段
    if (data) {
        Object.keys(data).forEach(key => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = data[key];
            form.appendChild(input);
        });
    }
    // 将表单添加到文档中并提交
    document.body.appendChild(form);
    form.submit();
    // 提交后移除表单
    document.body.removeChild(form);
}

/**
 * 获取指定时间对应的时间戳。
 * 如果是字符串，进行转换；
 * 如果不是字符串，也不是Date，取当前时间
 * @param date Date或string类型
 * @returns {number} 时间戳
 */
function getTimestamp(date) {
    if (typeof date === "string") {
        date = new Date(date);
    }
    if (!(date instanceof Date) || isNaN(date)) {
        return Date.now();
    }
    return date.getTime();
}

/**
 * 从时间戳转换为字符串格式的时间
 * 不是时间戳时，取当前时间
 * @param ts 时间戳,小于100000000000表示秒，否则为毫秒
 * @returns {string} 字符串格式的时间
 */
function getStrTimeFromTimestamp(ts) {
    if (typeof ts === 'string') {
        ts = parseInt(ts, 10);
    }
    if (typeof ts !== 'number' || isNaN(ts)) {
        return getStrTime();
    }
    if (ts < 100000000000) {
        ts *= 1000;
    }
    return getStrTime(new Date(ts));
}

/**
 * 获取yyyy-MM-dd格式的日期字符串
 * @param date 时间参数，可为空，默认当前时间
 * @returns {string} yyyy-MM-dd
 */
function getYmd(date) {
    if (!date)
        date = new Date();
    let month = (date.getMonth() + 1);
    if (month < 10)
        month = '0' + month;
    let day = date.getDate();
    if (day < 10)
        day = '0' + day;
    return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 获取HH:mm:ss格式的时间字符串
 * @param date 时间参数，可为空，默认当前时间
 * @returns {string} HH:mm:ss
 */
function getTime(date) {
    if (!date)
        date = new Date();
    let hour = date.getHours();
    if (hour < 10)
        hour = '0' + hour;
    let minute = date.getMinutes();
    if (minute < 10)
        minute = '0' + minute;
    let second = date.getSeconds();
    if (second < 10)
        second = '0' + second;
    return hour + ':' + minute + ':' + second;
}

/**
 * 获取指定日期的0点时间，如 2022-02-11 00:00:00
 * @param date 时间参数，可为空，默认当前时间
 * @returns {Date} 0点的日期
 */
function getDateBegin(date) {
    if (!date)
        date = new Date();
    return new Date(getYmd(date) + ' 00:00:00');
}

/**
 * 获取指定日期的最后一秒的时间，如 2022-02-11 23:59:59
 * @param date 时间参数，可为空，默认当前时间
 * @returns {Date} 23:59:59的日期
 */
function getDateEnd(date) {
    if (!date)
        date = new Date();
    return new Date(getYmd(date) + ' 23:59:59');
}

/**
 * 获取yyyy-MM-dd HH:mm:ss格式的日期字符串
 * @param date 时间参数，可为空，默认当前时间
 * @returns {string} yyyy-MM-dd HH:mm:ss
 */
function getStrTime(date) {
    if (!date)
        date = new Date();
    return getYmd(date) + ' ' + getTime(date);
}

/**
 * 为指定时间添加秒数
 * @param date 时间参数，可为时间格式字符串，可为空，默认当前时间
 * @param second 要添加的秒数，可为负数
 * @returns {Date} 添加后的时间
 */
function addSeconds(date, second) {
    if (!second)
        return date;
    if (!date)
        date = new Date();
    else if (!(date instanceof Date))
        date = new Date(date);
    date.setSeconds(date.getSeconds() + second); // 会自动累进到分、时、日、月、年
    return date;
}

/**
 * 为指定时间添加分钟数
 * @param date 时间参数，可为时间格式字符串，可为空，默认当前时间
 * @param minute 要添加的分钟数，可为负数
 * @returns {Date} 添加后的时间
 */
function addMinutes(date, minute) {
    if (!minute)
        return date;
    return addSeconds(date, minute * 60);
}

/**
 * 为指定时间添加小时数
 * @param date 时间参数，可为时间格式字符串，可为空，默认当前时间
 * @param hour 要添加的小时数，可为负数
 * @returns {Date} 添加后的时间
 */
function addHours(date, hour) {
    if (!hour)
        return date;
    return addSeconds(date, hour * 60 * 60);
}

/**
 * 把数值秒 转换为字符串，如 x天x月x日x时x分x秒
 * @param seconds 数值秒数
 * @returns {string} 时间字符串
 */
function secondToStr(seconds) {
    if (seconds === null || seconds === undefined || seconds === '')
        return '';
    seconds = Math.ceil(seconds);
    if (seconds < 60)
        return seconds + '秒';
    let ret = '';

    let useSeconds = seconds % 60;
    if (useSeconds > 0)
        ret = useSeconds + '秒';

    let minutes = Math.floor(seconds / 60);
    let useMinutes = minutes % 60;
    if (useMinutes > 0)
        ret = useMinutes + '分' + ret;

    let hours = Math.floor(minutes / 60);
    let useHours = hours % 24;
    if (useHours > 0)
        ret = useHours + '时' + ret;

    let days = Math.floor(hours / 24);
    if (days > 0)
        ret = days + '天' + ret;
    return ret;
}

/**
 * 把数值字节 转换为字符串，如 2.1GB 54.3MB
 * @param size 字节数
 * @returns {string} 字符串
 */
function byteToStr(size) {
    if (size === null || size === undefined || size === '')
        return '';
    if (typeof (size) === 'string') {
        size = parseInt(size, 10);
    }
    if (size <= 0)
        return size;
    if (size < 1024)
        return size + 'Byte';

    size = Math.round(size * 10 / 1024) / 10;
    if (size < 1024)
        return size + 'KB';

    size = Math.round(size * 10 / 1024) / 10;
    if (size < 1024)
        return size + 'MB';

    size = Math.round(size * 10 / 1024) / 10;
    return size + 'GB';
}

/**
 * 返回2个时间的差值，秒数
 * @param dateStart 较小的时间
 * @param dateEnd 较大的时间
 * @returns {number} 秒
 */
function secondDiff(dateStart, dateEnd) {
    if (dateStart === null || dateStart === undefined || dateStart === '' ||
        dateEnd === null || dateEnd === undefined || dateEnd === '')
        return 0;
    if (!(dateStart instanceof Date))
        dateStart = new Date(dateStart);
    if (!(dateEnd instanceof Date))
        dateEnd = new Date(dateEnd);
    return (dateEnd - dateStart) / 1000;
}

/**
 * 是否只包含0~9的数字，不允许正负号，不允许小数点
 *
 * @param str
 * @returns {boolean}
 */
function isOnlyNum(str) {
    str = toStr(str);
    return /^\d+$/.test(str);
}

/**
 * 是否数字，允许正负号，允许小数点
 * 注：对于 12e4 也会返回true
 *
 * @param str
 * @returns {boolean}
 */
function isNum(str) {
    if (str === null || str === undefined || isNaN(str))
        return false;
    if (typeof (str) === 'number')
        return true;

    str = str.toString().trim();
    return !isNaN(parseFloat(str)) && isFinite(str);
}

/**
 * 是否由0-9的数字组成，不允许正负号和小数点
 *
 * @param str
 * @returns {boolean}
 */
function isDigit(str) {
    str = toStr(str);
    return /^\d+$/.test(str);
}

/**
 * 指定的字符串是否为空
 * @param str
 * @returns {boolean}
 */
function isEmpty(str) {
    str = toStr(str);
    return str.length === 0;
}

/**
 * 判断输入的字符串，是否邮箱地址
 * @param str 邮箱
 * @returns {boolean}
 */
function isEmail(str) {
    str = toStr(str);
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return regex.test(str);
}

/**
 * 从给定的字符串中，提取第一个邮箱，不存在时返回空串
 * @param str 含邮箱的字符串
 * @returns {string} 第一个邮箱
 */
function getEmailFromStr(str) {
    str = toStr(str);
    const regex = /\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}\b/g;
    const emails = str.match(regex);
    if (emails === null || emails.length === 0)
        return '';
    return emails[0];
}

function downloadDataToCsv(data) {
    // “\ufeff” BOM头
    let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(data);
    let downloadLink = document.createElement("a");
    downloadLink.href = uri;
    downloadLink.download = "export.csv";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}

function downloadDataToTxt(data, filename) {
    let uri = 'data:text/plain;charset=utf-8,' + encodeURIComponent(data);
    let downloadLink = document.createElement("a");
    downloadLink.href = uri;
    if (!filename) filename = 'down.txt';
    downloadLink.download = filename;
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}

// 计算带中文字符的字符串长度
function lenb(str) {
    str = toStr(str, true);
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


/**
 * 判断对象有属性
 * @param obj 对象
 * @returns {boolean} 有或没有
 */
function hasAnyProperty(obj) {
    if (!obj)
        return false;
    for (let prop in obj) {
        if (obj.hasOwnProperty(prop)) {
            return true;
        }
    }
    return false;
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


/**
 * 导出指定的数据
 * @param dataList 对象数组，必需，如 [{'aa':11, 'bb':22}, {'aa':33, 'bb':44}]
 * @param downFilename 下载使用的文件名, 可空
 * @param ignoreFieldArr 字符串数组, 可空，上面对象里哪些字段不需要导出，如 ['aa', 'cc']
 * @param attToTitle 函数,接收1个属性名参数, 可空，存在该方法时，调用它，获取实际展示使用的列标题
 * @param attValConvert 函数,接收2个参数:属性名/属性值, 可空，存在该方法时，调用它，把列值进行转换，如枚举转换为文字展示
 */
function exportDataToCsv(dataList, downFilename, ignoreFieldArr, attToTitle, attValConvert) {
    if (!dataList || !dataList.length) {
        return vueAlert('没有结果可导出', 3000);
    }
    if (!downFilename) downFilename = 'export.csv';
    if (attToTitle && typeof (attToTitle) !== 'function')
        attToTitle = null;

    let dataHeader = '';
    let dataContent = '';
    for (let i = 0, j = dataList.length; i < j; i++) {
        let row = dataList[i];
        if (i > 0) {
            dataContent += '\r\n';
        }
        for (let att in row) {
            // 过滤不导出的字段
            if (ignoreFieldArr && ignoreFieldArr.indexOf(att) >= 0) {
                continue;
            }
            if (i === 0) {
                let attName = attToTitle ? attToTitle(att) : att;
                dataHeader += '"' + attName + '",';
            }
            let cell = attValConvert ? attValConvert(att, row[att]) : row[att];
            cell = convertToCsvText(cell);
            dataContent += cell + ',';
        }
    }
    downloadCsv(dataHeader + '\r\n' + dataContent, downFilename);
}

/**
 * 把val转换为csv可展示的字符串
 *
 * @param val
 * @returns {string}
 */
function convertToCsvText(val) {
    val = toStr(val);
    if (isDigit(val))
        return '="' + val + '"'; // 加个等号，避免数字精度丢失；之前加\t，会导致复制出去不方便使用
    return '"' + val.replace(/"/g, '""') + '"'; // csv里的双引号转义为2个
}

/**
 * 导出指定的字符串为csv文件。
 * 即弹出下载
 *
 * @param csvContent csv的文件内容
 * @param downFilename 要下载的文件名
 */
function downloadCsv(csvContent, downFilename) {
    if (!downFilename) downFilename = 'down.csv';

    // “\ufeff” BOM头
    let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(csvContent);
    let downloadLink = document.createElement("a");
    downloadLink.href = uri;
    downloadLink.download = downFilename;
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}

/**
 * 导出指定的字符串为json文件。
 * 即弹出下载
 *
 * @param jsonStr json文件内容
 * @param downFilename 要下载的文件名
 */
function downloadJson(jsonStr, downFilename) {
    if (!downFilename) downFilename = 'down.json';
    let blob = new Blob([jsonStr]);
    let downloadLink = document.createElement("a");
    downloadLink.href = URL.createObjectURL(blob);
    downloadLink.download = downFilename;
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}

/**
 * 把给定的json字符串，进行格式化后返回
 * @param str 未格式化的json串
 * @returns {string} 格式化好的字符串
 */
function formatJsonStr(str) {
    if (str === null || str === undefined) {
        return '';
    }

    try {
        let json = str;
        if (typeof (str) === 'string') {
            if (str.length === 0) {
                return '';
            }
            json = JSON.parse(str);
        }
        return JSON.stringify(json, null, 4);
    } catch (e) {
        return '';
    }
}

/**
 * 获取文件名的扩展名，没有扩展名返回空
 *
 * @param filename 文件名
 * @param withPoint 返回值要不要带小数点，如 .txt
 * @returns {string} 扩展名
 */
function getFileExt(filename, withPoint) {
    if (!filename)
        return '';
    const idx = filename.lastIndexOf('.');
    if (idx <= 0 || idx >= filename.length - 1)
        return '';
    if (withPoint)
        return filename.substring(idx);
    return filename.substring(idx + 1);
}

/**
 * 判断字符串是否包含非法文件名或字符串
 * @param str 文件名
 * @returns {boolean} 包含与否
 */
function isValidFileName(str) {
    if (typeof (str) != 'string' || str.length === 0) {
        return false;
    }
    //  文件名中不允许出现的11个字符
    let reg = /[\<\>\/\\\|\:""\*\?\r\n]/g;
    if (reg.test(str)) {
        return false;
    }
    // 不允许以这些文件名开头
    let deservedFileNames = [
        "CON.", "PRN.", "AUX.", "NUL.", "COM1.", "COM2.", "COM3.", "COM4.",
        "COM5.", "COM6.", "COM7.", "COM8.", "COM9.", "LPT1"
    ];
    str = str.toUpperCase();
    for (let i = deservedFileNames.length - 1; i >= 0; i--) {
        if (str.indexOf(deservedFileNames[i]) === 0) {
            return false;
        }
    }
    return true;
}

/**
 * 返回由字母+数字组成的随机字符串
 * @param size 该随机串的长度
 * @param charFirst 首字符是否必须是字母
 * @returns {string} 随机字符串
 */
function rndStrAndNum(size, charFirst) {
    let ret = '';
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < size; i++) {
        if (i === 0 && charFirst) {
            ret += rndStr(1);
        } else {
            ret += chars.charAt(Math.floor(Math.random() * chars.length));
        }
    }
    return ret;
}

/**
 * 返回全字母组成的随机字符串
 * @param size 该随机串的长度
 * @returns {string} 随机字符串
 */
function rndStr(size) {
    let ret = '';
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    for (let i = 0; i < size; i++) {
        ret += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return ret;
}

/**
 * 通过jsonp的方法，调用指定url
 * @param url 返回jsonp格式的api-url
 * @returns {Promise<unknown>} 异步回调对象
 */
function jsonp(url) {
    return new Promise((resolve, reject) => {
        const callbackName = 'jsonp_callback_' + Date.now();
        const scriptDom = document.createElement('script');
        const linkChar = url.indexOf('?') >= 0 ? '&' : '?';
        scriptDom.src = url + linkChar + 'jsonp_callback=' + callbackName;
        scriptDom.onerror = reject;
        document.body.appendChild(scriptDom);

        window[callbackName] = (data) => {
            resolve(data);
            // 清理资源
            document.body.removeChild(scriptDom);
            delete window[callbackName];
        };
    });
}

/**
 * 把 x.x.x.x形式的IP地址，转换为整数返回
 * 注：转换为无符号整型，如 '255.255.255.255'转为4294967295
 * 即： (255*256*256*256) + (255*256*256) + (255*256) + 255
 * @param ip 字符串格式的ip
 * @returns {number} 无符号整数
 */
function ipaddrToNumber(ip) {
    //return (ip.split('.').reduce((acc, cur) => (acc << 8) + parseInt(cur), 0)) >>> 0;
    const ipArr = ip.split('.');
    if (ipArr.length !== 4)
        throw new Error('IP地址格式不对，应该有3个小数点');
    let ret = 0;
    // 验证每个项是否小于等于255
    for (let i = 0; i < 4; i++) {
        const number = parseInt(ipArr[i], 10);
        if (isNaN(number) || number > 255 || number < 0)
            throw new Error('IP地址中的每个项都应在0~255之间');
        ret = (ret << 8) + number;
    }
    // 转无符号数，避免负数返回
    return ret >>> 0;
}

/**
 * 把一个无符号整数，转换为ip地址返回
 * @param number ipaddrToNumber方法计算得出的无符号整数
 * @returns {string} ip地址
 */
function numberToIpAddr(number) {
    if (number < 0 || number > 4294967295) {
        throw new Error('参数应在0~4294967295之间');
    }
    const ipArr = [];
    for (let i = 3; i >= 0; i--) {
        ipArr[i] = (number >>> (8 * (3 - i))) & 255;
    }
    return ipArr.join('.');
}

/**
 * 给定的ip，是否在给定的ip起止范围内
 * @param ip 要判断的ip
 * @param startIP ip范围起始值
 * @param endIp ip范围结束值
 * @returns {boolean} 是否在范围内
 */
function inIpAddrRange(ip, startIP, endIp) {
    const ipNum = ipaddrToNumber(ip);
    const startIPNum = ipaddrToNumber(startIP);
    const endIPNum = ipaddrToNumber(endIp);
    return ipNum >= startIPNum && ipNum <= endIPNum;
}

/**
 * 给定的ip，是否在给定的CIDR ip地址范围内
 * CIDR是用ip网址+子网掩码的表示法，如 192.168.0.0/16
 * @param ip 要判断的ip
 * @param startIP ip网址起始值
 * @param ipMaskNum 子网掩码
 * @returns {boolean} 是否在范围内
 */
function inIpAddrCIDR(ip, startIP, ipMaskNum) {
    const ipMask = parseInt(ipMaskNum, 10);
    if (isNaN(ipMask) || ipMask < 1 || ipMask > 32)
        throw new Error('子网掩码应在1~32之间');
    const ipNum = ipaddrToNumber(ip);
    const startIPNum = ipaddrToNumber(startIP);
    const endIPNum = startIPNum + (Math.pow(2, 32 - ipMask) - 1);
    console.log(startIPNum + ':' + endIPNum);
    return ipNum >= startIPNum && ipNum <= endIPNum;
}

/**
 * 判断给定的ip，是否属于 IANA定义的保留地址（即私有地址）
 * @param ip 给定的ip
 * @returns {boolean} 是否私有地址
 */
function isPrivateIpAddr(ip) {
    const privateIp = [
        ['0.0.0.0', '0.255.255.255'],  // 0.0.0.0/8
        ['10.0.0.0', '10.255.255.255'],  // 10.0.0.0/8
        ['100.64.0.0', '100.127.255.255'],  // 100.64.0.0/10
        ['127.0.0.0', '127.255.255.255'],  // 127.0.0.0/8
        ['169.254.0.0', '169.254.255.255'],  // 169.254.0.0/16
        ['172.16.0.0', '172.31.255.255'],  // 172.16.0.0/12
        ['192.0.0.0', '192.0.0.255'],  // 192.0.0.0/24
        ['192.0.2.0', '192.0.2.255'],  // 192.0.2.0/24
        ['192.88.99.0', '192.88.99.255'],  // 192.88.99.0/24
        ['192.168.0.0', '192.168.255.255'],  // 192.168.0.0/16
        ['198.18.0.0', '198.19.255.255'],  // 198.18.0.0/15
        ['198.51.100.0', '198.51.100.255'],  // 198.51.100.0/24
        ['203.0.113.0', '203.0.113.255'],  // 203.0.113.0/24
        ['224.0.0.0', '239.255.255.255'],  // 224.0.0.0/4
        ['233.252.0.0', '233.252.0.255'],  // 233.252.0.0/24
        ['240.0.0.0', '255.255.255.254'],  // 240.0.0.0/4
        ['255.255.255.255', '255.255.255.254'],  // 255.255.255.255/32
    ];
    for (const [startIP, endIP] of privateIp) {
        if (inIpAddrRange(ip, startIP, endIP)) {
            return true;
        }
    }
    return false;
}

/**
 * 用于axios的成功回调方法
 * @param response 响应对象
 */
function ajaxSuccessCheck(response) {
    let vueApp = getVueInstance();
    // 外部业务，使用了loading作为ajax加载防重复逻辑
    if (vueApp && vueApp.loading)
        vueApp.loading = false;

    // 响应的http状态不等于200
    if (!response || !response.status || response.status !== 200) {
        throw '响应http状态码不为200';
    }
    if (response.data === null || response.data === undefined) {
        throw '未找到响应数据';
    }
    if (response.data.code && response.data.code !== 200) {
        let msg = response.data['msg'];
        if (msg === '请重新登录') {
            return goLoginPage();
        }
        throw '后台返回code:' + response.data.code + ' ' + msg;
    }
}

/**
 * 用于axios的error
 * @param error 错误对象
 */
function ajaxError(error) {
    let vueApp = getVueInstance();
    // 外部业务，使用了loading作为ajax加载防重复逻辑
    if (vueApp && vueApp.loading)
        vueApp.loading = false;

    if (error.response && error.response.data) {
        console.log(JSON.stringify(error.response.data));
        let msg = getErrorMsg(error.response);
        if (msg && msg === 'Unauthorized') {
            goLoginPage();
        } else {
            vueAlert(msg ? msg : '出错了', 3000);
        }
    } else if (error.message) {
        vueAlert('ajax错误:' + error.message, 3000);
    } else {
        console.log(JSON.stringify(error));
        vueAlert('未知错误', 3000);
    }
}

/**
 * 从响应对象里，获取错误信息
 * @param response 响应对象
 * @returns {string} 错误信息
 */
function getErrorMsg(response) {
    if (!response) return '';
    if (response.msg) return response.msg;
    if (!response.data) return '';
    console.log(JSON.stringify(response.data));
    let msg = response.data['error'];
    if (!msg) msg = response.data['error'];
    if (!msg) msg = response.data['errMsg'];
    if (!msg) msg = response.data['msg'];
    if (!msg) msg = response.data['Msg'];
    return msg ? msg : '';
}

function goLoginPage() {
    top.location.href = $$BASE_URL + 'login.html?url=' + encodeURIComponent(location.href);
}

var globalPickOptions = {
    shortcuts: [
        {
            text: '最近3天',
            onClick(picker) {
                const end = new Date();
                end.setTime(end.getTime());
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 2);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近7天',
            onClick(picker) {
                const end = new Date();
                end.setTime(end.getTime());
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近14天',
            onClick(picker) {
                const end = new Date();
                end.setTime(end.getTime());
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 13);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近30天',
            onClick(picker) {
                const end = new Date();
                end.setTime(end.getTime());
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                picker.$emit('pick', [start, end]);
            }
        }, {
            text: '最近90天',
            onClick(picker) {
                const end = new Date();
                end.setTime(end.getTime());
                const start = new Date();
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                picker.$emit('pick', [start, end]);
            }
        }
    ]
};

function getUploadImg() {
    return {
        props: {
            value: {
                type: String,
                default: '',
            }
        },
        data() {
            return {
                uploadUrl: $$BASE_URL + 'upload',
                uploadHeaders: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            }
        },
        methods: {
            beforeUploadFile: function (file) {
                const isAllowedType = ['image/jpeg', 'image/png', 'image/svg+xml', 'image/webp'].includes(file.type);
                const isLt2M = file.size / 1024 / 1024 < 20;
                if (!isAllowedType) {
                    this.$message.error('只能上传 JPG/PNG 格式!');
                    return false;
                }
                if (!isLt2M) {
                    this.$message.error('文件大小不能超过 20MB!');
                    return false;
                }
                return true; // 通过校验
            },
            handleUploadFile: async function (params) {
                try {
                    // 1. 获取s3签名上传用的URL
                    const signObj = await this.getS3SignUrl(params.file.name, params.file.type);
                    const s3url = signObj.presignedUrl;
                    // 2. 执行 PUT 上传
                    await axios.put(s3url, params.file, { headers: { 'Content-Type': params.file.type } });
                    // 3. 上传成功
                    this.$emit('input', signObj.downloadUrl);
                } catch (error) {
                    this.$message.error('上传失败：' + error.message);
                }
            },
            getS3SignUrl: async function (fileName, contentType) {
                let s3Filename = rndStrAndNum(10, false) + getFileExt(fileName, true);
                let url = $$BASE_URL + 'uploadFile/signedUrl?uploadType=SEO&fileName=' + encodeURIComponent(s3Filename)
                    + '&contentType=' + encodeURIComponent(contentType);
                const response = await axios.get(url);
                return response.data.data;
            },
        },
        template: `<div style="display: flex; align-items: start; gap: 16px">
    <el-upload
        action=""
        accept="image/jpeg,image/png,image/svg+xml,image/webp"
        :auto-upload="true"
        :http-request="handleUploadFile"
        :before-upload="beforeUploadFile"
        :file-list="[]"
        :limit="1"
        :show-file-list="false"
        class="upload-demo">
        <el-button size="small" type="primary">点击上传</el-button>
    </el-upload>
    <el-image
      style="width: 100px; height: 100px; border-radius: 8px;"
      :src="value"
      fit="fit">
    </el-image>
    
<div>`
    }
}

! function (e, n) {
    "object" == typeof exports && "undefined" != typeof module ? module.exports = n() : "function" == typeof define && define.amd ? define(n) : (e = e || self).mitt = n()
}(this, function () {
    return function (e) {
        return {
            all: e = e || new Map,
            on: function (n, t) {
                var f = e.get(n);
                f ? f.push(t) : e.set(n, [t])
            },
            off: function (n, t) {
                var f = e.get(n);
                f && (t ? f.splice(f.indexOf(t) >>> 0, 1) : e.set(n, []))
            },
            emit: function (n, t) {
                var f = e.get(n);
                f && f.slice().map(function (e) {
                    e(t)
                }), (f = e.get("*")) && f.slice().map(function (e) {
                    e(n, t)
                })
            }
        }
    }
});

const gMitt = mitt();//全局事件中心   
