// 监听来自页面的消息
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
    if (request.action === "jsonProcess") {
      var contentType = request.contentType;
      alert(contentType);
  
      // 判断内容是否为JSON
      if (contentType === "application/json") {
        alert("收到JSON内容:\n" + JSON.stringify(request.data));
      }
    }
  });
  
  // 在页面中注入脚本
  var script = document.createElement('script');
  script.textContent = `
    // 监听DOM内容变化事件
    document.addEventListener('DOMContentLoaded', function() {
      // 获取页面内容
      var pageContent = document.documentElement.innerHTML;
  
      // 判断页面内容类型，如果是JSON则发送消息给content script
      if (pageContent.startsWith('{')) {
        chrome.runtime.sendMessage({ action: "jsonProcess", contentType: "application/json", data: JSON.parse(pageContent) });
      }
    });
  `;
  document.documentElement.appendChild(script);