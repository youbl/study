/**
 * @file 通用方法
 */

/**
 *
 * 判断字符串是否包含非法文件名或字符串
 * @param {type} str 字符串
 */
function isValidFileName(str) {
    if (typeof (str) != 'string' || str.length === 0) {
        return false;
    }
    //  文件名中不允许出现的11个字符
    var reg = /[\<\>\/\\\|\:""\*\?\r\n]/g;
    if (reg.test(str)) {
        return false;
    }
    // 不允许以这些文件名开头
    var deservedFileNames = [
        "CON.", "PRN.", "AUX.", "NUL.", "COM1.", "COM2.", "COM3.", "COM4.",
        "COM5.", "COM6.", "COM7.", "COM8.", "COM9.", "LPT1"
    ];
    str = str.toUpperCase();
    for (var i = deservedFileNames.length - 1; i >= 0; i--) {
        if (str.indexOf(deservedFileNames[i]) === 0) {
            return false;
        }
    }
    return true;
}

/**
 * html编码，用浏览器内部转换器实现
 * @param {any} html
 */
function htmlEncode(html) {
    //1.首先动态创建一个容器标签元素，如DIV
    var temp = document.createElement("div");
    //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
    (temp.textContent != undefined) ? (temp.textContent = html) : (temp.innerText = html);
    //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
    var output = temp.innerHTML;
    temp = null;
    return output;
}

/**
 * html解码，用浏览器内部转换器实现
 * @param {any} text
 */
function htmlDecode(text) {
    //1.首先动态创建一个容器标签元素，如DIV
    var temp = document.createElement("div");
    //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
    temp.innerHTML = text;
    //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
    var output = temp.innerText || temp.textContent;
    temp = null;
    return output;
}

/**
 *
 * 限制对象只能输入数字或逗号
 * @param {type} obj 要限制的对象
 * @param {type} enableComma 是否允许输入半角逗号
 */
function digiLimit(obj, enableComma) {
    var reg;
    if (enableComma) {
        reg = /[^\d,]/g;
    } else {
        reg = /[^\d]/g;
    }
    $(obj).val($(obj).val().replace(reg, ''));
}

/**
 * 初始化对话框
 * @param {string|jQuery} id 对象id
 * @param {method} confirmMethod 点确认时的调用方法
 * @return {undefined}
 */
function initDialog(id, confirmMethod) {
    // https://jqueryui.com/dialog/#modal-message
    $(id).dialog({
        autoOpen: false,
        height: 450,
        width: 650,
        modal: true, // 模态开启
        draggable: true, // 是否可拖拽
        minWidth: 600, // 最小宽度
        hide: {effect: 'explode', duration: 500}, // 隐藏效果
        buttons: {
            '确认': confirmMethod,
            '取消': function () {
                hideDialog(id);
            }
        }
    });
}

/**
 * 显示对话框
 * @param {string|jQuery} id 对象id
 * @return {undefined}
 */
function showDialog(id) {
    $(id).dialog('open');
}

/**
 * 隐藏对话框
 * @param {string|jQuery} id 对象id
 * @return {undefined}
 */
function hideDialog(id) {
    $(id).dialog('close');
}

/**
 * 根据指定模板，进行数据展开
 * @param {string} template 模板名
 * @param {Array|Object} data 数据
 * @return {string|render.temp|jQuery}
 */
function render(template, data) {
    var temp;
    if (data instanceof Array) {
        var result = [];
        for (var index in data) {
            if (data.hasOwnProperty(index)) {
                temp = render(template, data[index]);
                temp = temp.replace(/{\$index}/g, index);
                result.push(temp);
            }
        }
        return result.join('');
    }
    temp = $('#' + template).html();
    for (var i in data) {
        if (data.hasOwnProperty(i)) {
            var regex = new RegExp('{' + i + '}', 'g');
            temp = temp.replace(regex, toStr(data[i]));
        }
    }
    return temp;
}

/**
 * 把数组或对象的属性，通过分号分隔拼接
 * @param {type} obj 传入的数组或对象
 * @return {String}
 */
function toStr(obj) {
    if (obj === null || obj === undefined) {
        return '';
    }
    if (obj instanceof Object) {
        var ret = '';
        var idx = 0;
        for (var i in obj) {
            if (obj.hasOwnProperty(i)) {
                if (idx !== 0) {
                    ret += '; ';
                }
                idx++;
                ret += obj[i];
            }
        }
        return ret;
    }
    return obj.toString();
}

/**
 * 获取url里的变量值
 * @param {string} name 变量名
 * @return {string} 得到的值
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
        let idx = localSearch.indexOf(tmpName);
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
 * 获取url里的变量数字
 * @param {string} name 变量名
 * @return {number}
 */
function getQueryInt(name) {
    var tmp = getQueryString(name);
    if ((/^-?\d+$/).test(tmp)) {
        return parseInt(tmp, 10);
    }
    return 0;
}

/**
 * 对指定的table进行隔行变色
 * @param {string} id table的id
 * @return {undefined}
 */
function trColorChg(id) {
    // 延时处理，避免dom没加载完成
    setTimeout(function () {
        // table鼠标移动变色
        $(id).find('tr:gt(0)').each(function () {
            $(this).bind('mouseover', function () {
                window.onRowOver(this);
            }).bind('mouseout', function () {
                window.onRowOut(this);
            }).bind('click', function () {
                window.onRowClick(this);
            });
        });
    }, 500);
}

function ajaxJsonp(url, para, callback) {
    var signpara = getSignPara();
    $.ajax({
        type: 'GET',
        data: para,
        url: url,
        headers: {
            'Ts': signpara["Ts"]
        },
        cache: false,
        dataType: 'jsonp',
        success: callback
    });
}

function ajaxPost(url, para, callback, method) {
    if (!method)
        method = 'POST';
    var signpara = getSignPara();
    $.ajax({
        type: method,
        data: para,
        url: url,
        headers: {
            'Ts': signpara["Ts"]
        },
        cache: false,
        dataType: 'json',
        success: callback
    });
}

/**
 * 通过ajax加载数据
 * @param {type} url
 * @param {type} callback
 * @returns {undefined}
 */
function ajaxLoadResponse(url, callback) {
    var para = getSignPara();
    $.ajax({
        type: 'GET',
        url: url,
        headers: {
            'Ts': para["Ts"]
        },
        cache: false,
        dataType: 'json',
        success: function (response) {
            callback(response);
        }
    });
}


/**
 * 通过ajax加载数据
 * @param {type} url
 * @param {type} callback
 * @returns {undefined}
 */
function ajaxLoadData(url, callback) {
    ajaxLoadResponse(url, function (response) {
        if (response.code !== 200) {
            alert(response.code + (response.message ? '-' + response.message : ''));
            return;
        }
        if (!response.result || response.result.length === 0) {
            return;
        }
        callback(response.result);
    });
}

function getSignPara() {
    var params = {};
    params['Ts'] = Math.floor(new Date().getTime() / 1000);// 时间戳
    return params;
}
