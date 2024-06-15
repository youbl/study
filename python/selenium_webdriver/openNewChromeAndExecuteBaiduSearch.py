# pip install selenium
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

import traceback,time

# help doc: https://developer.chrome.com/docs/chromedriver/get-started
# this demo worked well on chrome125, and using default chromedriver.exe
# the exe path on my computer: C:\Users\youbl\.cache\selenium\chromedriver\win64\125.0.6422.141

def main():
    try:
        driver = openNewChrome()

        # create new tab and navigate to baidu
        driver.switch_to.new_window('tab')
        driver.get('https://www.baidu.com')
        # wait for page loaded
        wait = WebDriverWait(driver, 10)

        # find input element and fill word
        cond = (By.CSS_SELECTOR, 'input[name="wd"]')
        input_element = wait.until(EC.presence_of_element_located(cond))
        input_element.send_keys("youbl csdn")
        print('word filled')

        # find button and click to submit
        cond = (By.CSS_SELECTOR, 'input[type="submit"]')
        submit_button = wait.until(EC.element_to_be_clickable(cond))
        submit_button.click()

        # google.com use these 3 line code
        #element = driver.find_element('css selector', '[name="q"]')
        #element.send_keys("MoreLogin")
        #element.send_keys(Keys.RETURN)
        print('search executed')

        time.sleep(15)
        print('chrome will quit')
        # close chrome. notice: after python code finished, chrome also will auto close.
        driver.quit()
        print('chrome quited')

    except:
        error_message = traceback.format_exc()
        print('run-error: ' + error_message)

#
def openNewChrome():
    # no arg means use default chromedriver
    driver = webdriver.Chrome()
    
    print('debug url:', driver.command_executor._url)    
    print('opened chrome session id:', driver.session_id)
    
    return driver

# create webdriver with exist browser, debug_url format: 127.0.0.1:12345
def createWebDriverWithUrl(debug_url):
    opts = webdriver.ChromeOptions()
    # opts.set_capability('browserVersion', str(version))
    opts.add_experimental_option('debuggerAddress', debug_url)
    driver = webdriver.Chrome(options=opts)
    print(driver.current_url)
    return driver


if __name__ == '__main__':
    main()