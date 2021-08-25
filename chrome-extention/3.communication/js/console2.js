(function () {
    console.log('document_end后执行脚本2');

    // 创建一个按钮
    let ele = document.createElement("input");
    ele.type = "button";
    ele.value = "点我点我";
    ele.onclick = function () { alert(123) };

    // 添加到当前页面的最前面
    let firstBtn = document.documentElement.childNodes[0];
    document.documentElement.insertBefore(ele, firstBtn);
})();