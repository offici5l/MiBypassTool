**Regarding Xiaomi HyperOS Unlocking Restrictions (In brief):**

I downloaded all the versions of Xiaomi’s Settings app, decompiled them , and compared them. Naturally, there are differences between each version.

But what caught my attention was that the server (i.e., the data submission URL) was different in the past!

___

**Version (8.1.0-27) and above:**

If "_global" in "adb shell getprop ro.product.mod_device":
URL: "https://unlock.update.intl.miui.com/v1/unlock/applyBind"
Else:
URL: "https://unlock.update.miui.com/v1/unlock/applyBind"

**Older Version (7.1.2-25):**

If "_global" in "adb shell getprop ro.product.mod_device":
URL: "https://us.deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account"
Else:
URL: "https://deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account"

**Even Older Version (5.1.1-22):**

URL: "https://deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account" (China).

___
**steps I took:**
___

**1 Host Check Results:**

The Chinese URL "https://deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account" works.

The Global URL "https://us.deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account" is down.

**2 I made some modifications to the MibypassTool to send the request:**

We don't need : heartbeat_mode, imsi1, device, rom_version, error_code (it can also be left as it will not affect the order in either case.)

We don't need sid and sign either (deleting them is necessary)

**3 Send request:**

Send data ... using : "https://deviceapi.micloud.xiaomi.net/mic/binding/v1/identified/device/account"

Result: code 0 = Linked successfully

___

**But after using the Miunlocktool:**

code 20031 = Please add your account in Settings > Developer options > Mi Unlock status.

I also tried using the Chinese server with:

python MiUnlockTool.py China

but the same issue occurs because although the server changes, the data remains global when checking the value at: https://github.com/offici5l/MiUnlockTool/blob/main/MiUnlockTool.py#L170. This is because I used a global Xiaomi account + global phone.

___

**Summary and Proposed Solutions:**

These are just proposed solutions, and it’s possible that the method may never succeed, but there’s nothing preventing us from trying.

One possible solution is to use a Xiaomi account in the China region, and it’s preferable to also use a Chinese phone, followed by using "python MiUnlockTool.py china"

Another potential solution is to use the oldest version (meaning the first version released by Xiaomi for the official bootloader unlocking tool), as the link for obtaining the encrypted data may have been different in the past.

All source code and MiBypassTool(test): https://github.com/offici5l/MiBypassTool/tree/main/test

___

"I invite everyone, whether experienced or not, to share their thoughts and ideas. You might have a vision or a simple idea that could make a big difference, so don’t hesitate to participate and enlighten us with what you have!"

https://t.me/Offici5l_Group

___

# Installation:

### For MacOS, Windows, Linux:

1. Install Python3.
2. Download [MiBypassTool](https://github.com/offici5l/MiBypassTool/releases/download/tmibypass/MiBypassTool.py) and run it.

### For Android:

1. Install [Termux](https://github.com/termux/termux-app/releases/download/v0.118.0/termux-app_v0.118.0+github-debug_universal.apk)

2. Install [Termux API](https://github.com/termux/termux-api/releases/download/v0.50.1/termux-api_v0.50.1+github-debug.apk)

3. From Termux command line:
```bash
curl -s https://raw.githubusercontent.com/offici5l/MiBypassTool/main/test/.install | bash
```