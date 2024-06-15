# pip install playwright 
# if error: Looks like Playwright was just installed or updated.
# npm i -D playwright && npx playwright install
import sys
import asyncio
from playwright.async_api import async_playwright, Playwright     # async call 
import traceback

# help docs: https://playwright.dev/python/docs/input

async def main():
    async with async_playwright() as playwright:
        await run(playwright)

async def run(playwright: Playwright):
    try:
        await operationEnv('', playwright)

        # wait 10 second
        await asyncio.sleep(10)

    except:
        error_message = traceback.format_exc()
        print('run-error: ' + error_message)

# open page and operation
async def operationEnv(cdp_url, playwright):
    if cdp_url and cdp_url.startswith('http'):
        browser = await playwright.chromium.connect_over_cdp( cdp_url ) # connect to existed chrome, cdp_url format: http://127.0.0.1:12345
        default_context = browser.contexts[0]
    else:
        browser = await playwright.chromium.launch(headless=False) # run chrome        
        default_context = await browser.new_context()
    
    # try open page
    page1 = await default_context.new_page()
    await page1.goto('https://ipinfo.io')

    page2 = await default_context.new_page()
    await page2.goto('https://www.google.com/')
    print(page2.title)
    # warn: if the css-selector can't find element, this code will throw timeout after 30s
    await page2.fill('[name="q"]', 'youbl csdn')
    await page2.press('[name="q"]', 'Enter')
    #await page2.keyboard.press('Enter')  

    # try close and clear resource
    # await page1.close()
    # await page2.close()

if __name__ == '__main__':
    asyncio.run(main())