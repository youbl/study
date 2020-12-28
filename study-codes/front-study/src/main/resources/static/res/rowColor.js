function onRowOver(o) {
    let r = convertColor(getBackgroundColor(o));
    r !== __colorClick && r !== __colorOver && r && (o.attributes["color"] = r),
        r !== __colorClick &&
            (arguments[1] ? (o.style["background-color"] = arguments[1]) : (o.style["background-color"] = __colorOver));
}
function onRowOut(o) {
    let r = o.attributes["color"];
    convertColor(getBackgroundColor(o)) !== __colorClick &&
    (arguments[1]
        ? (o.style["background-color"] = arguments[1])
        : r
        ? (o.style["background-color"] = r)
        : (o.style["background-color"] = __colorOut));
}
function onRowClick(o) {
    let r;
    if (r = window.event ? window.event.srcElement: onRowClick.caller.arguments[0].target, !r || !r.tagName || "TD" === r.tagName) {
        let c = convertColor(getBackgroundColor(o));
        c != __colorClick && (!arguments[1] || arguments[1] && c != arguments[1])
            ? arguments[1]
            ? (o.style["background-color"] = arguments[1])
            : (o.style["background-color"] = __colorClick)
            : arguments[2]
            ? (o).css("background-color", arguments[2])
            : (o.style["background-color"] = __colorOut);
    }
}
function convertColor(o) {
    let r = /^rgb\((\d+),\s+(\d+),\s+(\d+)\)$/gi;
    return r.test(o) ? "#" + parseInt(RegExp.$1).toString("16") + parseInt(RegExp.$2).toString("16") + parseInt(RegExp.$3).toString("16") : o
}
var __colorOver = "#e1eda9",
    __colorOut = "#ffffff",
    __colorClick = "#ffcc99";


function getBackgroundColor(element) {
    if (typeof element == "string")
        element = document.getElementById(element);
    if (!element)
        return null;

    let actualColor;
    if (element.currentStyle)
        actualColor = element.currentStyle['backgroundColor'];
    else {
        let cs = document.defaultView.getComputedStyle(element, null);
        actualColor = cs.getPropertyValue('background-color');
        let r = /^rgb\((\d+),\s+(\d+),\s+(\d+),\s+(\d+)\)$/gi;
        if (r.test(actualColor) && RegExp.$4 === '0' && element.parentNode) {
            return getBackgroundColor(element.parentNode);
        }
    }

    return actualColor;
} 