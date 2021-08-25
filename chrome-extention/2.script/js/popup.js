function refreshClock(element) {
    let today = new Date();
    let hour = append(today.getHours().toString());
    let minute = append(today.getMinutes().toString());
    let second = append(today.getSeconds().toString());

    let time = hour + ':' + minute + ':' + second;
    element.innerHTML = time;
}

function append(str) {
    if (str.length === 1)
        return '0' + str;
    return str;
}

setInterval(() => { refreshClock(document.getElementById('divTime')); }, 100);
