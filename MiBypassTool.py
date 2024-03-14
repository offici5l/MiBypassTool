#!/usr/bin/python

print("\nBypass HyperOS Restriction(Couldn't add. Please go to Mi Community to apply for authorization and try again.)\n")

import os

for lib in ['Cryptodome', 'requests']:
    try:
        __import__(lib)
    except ImportError:
        print(f"\nInstalling {lib}...\n")
        os.system('yes | pkg update' if "com.termux" in os.getenv("PREFIX", "") else '')
        os.system(f'pip install pycryptodomex' if lib == 'Cryptodome' else f'pip install {lib}')

import re, base64, requests, time, json, hmac, hashlib, random, urllib, urllib.parse, platform, shutil, subprocess, zipfile
from Cryptodome.Util.Padding import unpad
from base64 import b64encode, b64decode
from Cryptodome.Cipher import AES
from urllib.parse import urlparse

def dw(s):
    print("\ndownload platform-tools...\n")
    url = f"https://dl.google.com/android/repository/platform-tools-latest-{s}.zip"
    cd = os.path.join(os.path.dirname(__file__))
    fp = os.path.join(cd, os.path.basename(url))    
    urllib.request.urlretrieve(url, fp)    
    with zipfile.ZipFile(fp, 'r') as zip_ref:
        zip_ref.extractall(cd)   
    os.remove(fp)

def dwt():
    os.system("yes | pkg uninstall termux-adb; curl -s https://raw.githubusercontent.com/nohajc/termux-adb/master/install.sh | bash; ln -s $PREFIX/bin/termux-fastboot $PREFIX/bin/fastboot; ln -s $PREFIX/bin/termux-adb $PREFIX/bin/adb")

s = platform.system()
if s == "Linux" and os.path.exists("/data/data/com.termux"):
    try:
        result_adb = os.popen("adb --version").read()
        if "Android Debug" not in result_adb:
            dwt()
    except (FileNotFoundError, Exception):
        dwt()
    up = os.path.join(os.getenv("PREFIX", ""), "bin", "mibypass")
    if not os.path.exists(up):
        shutil.copy(__file__, up)
        os.system(f"chmod +x {up}")
        print("\n(Now use command: \033[92mmibypass\033[0m)\n")
        exit()
    cmd = "adb"
    systemp = "t"
else:
    dir = os.path.dirname(__file__)
    fp = os.path.join(dir, "platform-tools")
    if not os.path.exists(fp):
        dw(s)
    cmd = os.path.join(fp, "adb")
    systemp = "o"

def CheckD(cmd):
    print("\nCheck if the device is connected via OTG in normal mode...\n")
    while True:
        try:
            result = subprocess.run([cmd, "get-state"], capture_output=True, text=True, timeout=1)
        except subprocess.TimeoutExpired:
            continue
        if "device" in result.stdout:
            return True
    return False

checkd = CheckD(cmd)

if checkd:
    print("Device is connected \033[92mDone\033[0m\n")
else:
    print("Device is not connected exit...")
    exit()

os.popen(f"{cmd} logcat -c")

os.popen(f"{cmd} shell svc wifi disable")

os.popen(f"{cmd} shell svc data enable")

os.popen(f"{cmd} shell am start --activity-clear-task -a android.settings.APPLICATION_DEVELOPMENT_SETTINGS")

print("\n\033[92mNow bind your account in Mi Unlock status ...\033[0m\n")

account_bind_found = False

while True:
    process = os.popen(f"{cmd} logcat *:S CloudDeviceStatus:V -d").read()
    args_found = False
    headers_found = False
    for output in process.split('\n'):
        if "CloudDeviceStatus: args:" in output:
            args = output.split('args:')[1].strip()
            args_found = True
        if "CloudDeviceStatus: headers:" in output:
            headers = output.split('headers:')[1].strip()
            headers_found = True
        if args_found and headers_found:
            account_bind_found = True
            os.system(f"{cmd} shell svc data disable")
            break
    if account_bind_found:
        break

headers = {"Cookie": re.search(r"Cookie=\[(.*)\]", AES.new(b'20nr1aobv2xi8ax4', AES.MODE_CBC, b'0102030405060708').decrypt(base64.b64decode(headers)).rstrip(b'\0').decode('utf-8')).group(1).strip(), "Content-Type": "application/x-www-form-urlencoded"}

aj = json.loads(unpad(AES.new("20nr1aobv2xi8ax4".encode("utf-8"), AES.MODE_CBC, "0102030405060708".encode("utf-8")).decrypt(base64.b64decode(args)), AES.block_size).decode("utf-8"))

print("\nversion:",aj["rom_version"])

if aj["rom_version"].startswith("V816"):
    aj["rom_version"] = aj["rom_version"].replace("V816", "V14")
    print("\nchange version to:",aj["rom_version"])

if aj["rom_version"].split(".")[-1][-4:-2] == "CN":
    rr = ""
else:
    rr = "intl."

data = json.dumps(aj)

signature = hmac.new("10f29ff413c89c8de02349cb3eb9a5f510f29ff413c89c8de02349cb3eb9a5f5".encode("utf-8"), f"POST\n/v1/unlock/applyBind\ndata={data}&sid=miui_sec_android".encode("utf-8"), hashlib.sha1).hexdigest()

payload = {
    "data": data,
    "sid": "miui_sec_android",
    "sign": signature
}

response = requests.post(f"https://unlock.update.{rr}miui.com/v1/unlock/applyBind", data=payload, headers=headers)

data = json.loads(response.text)

for key, value in data.items():
    print(f"\n{key}: {value}")

systemp == "o" and input("\nPress Enter to exit ...")