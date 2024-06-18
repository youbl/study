# pip install selenium
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service

import traceback,time

r"""
chromedriver(webdriver)帮助: https://developer.chrome.com/docs/chromedriver/get-started
selenium帮助: https://www.selenium.dev/zh-cn/documentation/webdriver/getting_started/using_selenium/

注: 
webdriver.Chrome() 会根据当前系统安装的chrome版本，去下载chromedriver.exe, 
比如我的电脑是下载并存储在: C:\Users\youbl\.cache\selenium\chromedriver\win64\126.0.6478.61\chromedriver.exe
如果想用其它版本的chromedriver, 参考下面的 browserVersion 属性.
比如要使用124版本，对应代码: opts.set_capability('browserVersion', '124')
此时selenium会自动下载124的chromedriver
"""

def main():
    try:
        # 新建一个Chrome窗口
        driver = openNewChrome()

        # 在Chrome里新建tab页，并打开baidu搜索页
        driver.switch_to.new_window('tab')
        driver.get('https://www.baidu.com')
        # 等待页面加载完成
        wait = WebDriverWait(driver, 10)

        # 根据css查询语法条件，搜索输入框元素
        # 注：如果元素找不到，会抛出超时错误: selenium.common.exceptions.TimeoutException
        condition = (By.CSS_SELECTOR, 'input[name="wd"]')
        input_element = wait.until(EC.presence_of_element_located(condition))
        # 在输入框里输入搜索词
        input_element.send_keys("youbl csdn")
        print('word filled')

        # 根据css查询语法条件，搜索提交按钮，并点击进行搜索
        condition = (By.CSS_SELECTOR, 'input[type="submit"]')
        submit_button = wait.until(EC.element_to_be_clickable(condition))
        submit_button.click()

        # 如果是打开谷歌，可以用这3行代码进行搜索
        #element = driver.find_element('css selector', '[name="q"]')
        #element.send_keys("MoreLogin")
        #element.send_keys(Keys.RETURN)
        print('search executed')

        time.sleep(10)
        print('chrome will quit')
        # 关闭chrome。注：python脚本执行结束也会自动关闭前面打开的chrome.
        driver.quit()
        print('chrome quited')

    except:
        error_message = traceback.format_exc()
        print('run-error: ' + error_message)

#
def openNewChrome():
    opts = webdriver.ChromeOptions()
    # 禁用图片加载
    opts.add_experimental_option('prefs', {'profile.managed_default_content_settings.images': 2})

    # 设置浏览器兼容性，即下载和使用哪个版本的chromedriver.exe
    #opts.set_capability('browserVersion', str(version))
    # 添加扩展
    #opts.add_extension('/path/to/extension.crx')
    # 无界面模式运行(也叫无头模式)
    #opts.add_argument('--headless')
    # 禁用GPU加速
    opts.add_argument('--disable-gpu')
    # 连接到已打开的浏览器,而不是新建窗口 debug_url format: 127.0.0.1:12345
    # 要用命令行启动chrome，如 start chrome.exe --remote-debugging-port=12345
    #opts.add_experimental_option('debuggerAddress', debug_url)

    # 使用selenium自带的chromedriver.exe
    driver = webdriver.Chrome(options=opts)
    # 如果希望用指定目录下的chromedriver.exe, 可以用如下代码:
# from selenium.webdriver.chrome.service import Service
    #service = Service(executable_path='D:\chromedriver.exe')
    #driver = webdriver.Chrome(service=service, options=opts)
    
    print('debug url:', driver.command_executor._url)    
    print('opened chrome session id:', driver.session_id)
    
    return driver

if __name__ == '__main__':
    main()