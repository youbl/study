#*********************************************************************************************
#*** 文件说明，读取Chrome里储存的Cookie，并输出
#*********************************************************************************************
import os
import shutil
# pip install pywin32
import sqlite3, win32crypt
import json
import base64
import argparse
# pip install pycryptodomex
from Cryptodome.Cipher import AES

def main():
    # ========================================================================================
    # === 注意：
    # ===   必须在关闭Chrome的情况下运行本脚本，或者在关闭Chrome的情况下，把文件拷出来 ===
    # ===   不然文件会被占用
    # ========================================================================================

    # Cookie所在的文件 "%LocalAppData%\Google\Chrome\User Data\Default\Network\Cookies" 也可以在 chrome://version/ 里找Profile Path
    database_path = 'd:/Cookies' #os.path.expanduser('~') + encrypt_decrypt(Cookies, key)
    print(database_path)
    # 解密用的密码文件 "%LocalAppData%\Google\Chrome\User Data\Local State" json格式，密码在 os_crypt.encrypted_key
    local_state_path = 'd:/Local State'

    with open(local_state_path, 'r', encoding='utf-8') as fp:  # with会自动关闭连接
        local_state = json.load(fp)
        # 从"Local State"文件中，获取解密用的密钥
        master_key = base64.b64decode(local_state["os_crypt"]["encrypted_key"])
        # 移除密钥的前缀字符: DPAPI 
        master_key = master_key[5:]
        master_key = win32crypt.CryptUnprotectData(master_key, None, None, None, 0)[1]
        print('解密密钥: ' + str(master_key))   # 解密后的密钥

    # 连接密码数据库，读取数据
    with sqlite3.connect(database_path) as conn:
        cursor = conn.cursor()
        cursor.execute('SELECT * FROM cookies')
        #results = cursor.fetchall()

        # 收集字段名的数组
        all_field_names = []
        for field in cursor.description:
            all_field_names.append(field[0])
        i=0
        for row in cursor.fetchall():
            i=i+1
            print('============================ Cookie', i, '============================')
            for name in all_field_names:
                val = row[all_field_names.index(name)]
                if name == 'encrypted_value':
                    name = 'cookie_value'
                    val = decrypt_password(val, master_key) # 使用上面的密钥解密
                print(name, ': ', val) # 输出字段名+字段值
            #for column in row:
            #    print(column)

def encrypt_decrypt(data: str, key: str) -> str:
    return ''.join([chr(ord(data[i]) ^ ord(key[i % len(key)])) for i in range(len(data))])

def decrypt_payload(cipher, payload):
    return cipher.decrypt(payload)

def generate_cipher(aes_key, iv):
    return AES.new(aes_key, AES.MODE_GCM, iv)

def decrypt_password(buff, master_key):
    try:
        iv = buff[3:15]
        payload = buff[15:]
        cipher = generate_cipher(master_key, iv)
        decrypted_pass = decrypt_payload(cipher, payload)
        decrypted_pass = decrypted_pass[:-16]  # remove suffix bytes
        return decrypted_pass.decode('utf-8', errors='replace')
    except Exception as e:
        print("无法解码：", buff)

if __name__ == '__main__':
    main()
