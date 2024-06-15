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
    # no arg means default chrome, or you can specified a path to chrome: webdriver.Chrome('/path/to/chromedriver')
    driver = webdriver.Chrome()
    
    print('debug url:', driver.command_executor._url)    
    print('opened chrome session id:', driver.session_id)
    
    return driver

if __name__ == '__main__':
    main()