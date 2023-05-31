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
    let tmp = location.search.substr(idx + name.length);
    idx = tmp.indexOf('&');
    if (idx === 0)
        return '';
    let ret;
    if (idx < 0) {
        ret = tmp;
    } else {
        ret = tmp.substr(0, idx);
    }
    return decodeURIComponent(ret.trim());
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

function exportDataToCsv(dataList) {
    if (!dataList || !dataList.length) {
        return alert('没有结果可导出');
    }
    let dataHeader = '';
    let dataContent = '';
    for (let i = 0, j = dataList.length; i < j; i++) {
        let row = dataList[i];
        if (i > 0) {
            dataContent += '\r\n';
        }
        for (let att in row) {
            if (i === 0) {
                dataHeader += '"' + att + '",';
            }
            let cell = (row[att] + '').replace(/"/g, '""'); // csv里的双引号转义为2个
            dataContent += '"\t' + cell + '",';  // \t是避免数字变成科学计数
        }
    }
    downloadDataToCsv(dataHeader + '\r\n' + dataContent);
}

function downloadDataToCsv(data) {
    // “\ufeff” BOM头
    let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(data);
    let downloadLink = document.createElement("a");
    downloadLink.href = uri;
    downloadLink.download = "exportData.csv";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
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