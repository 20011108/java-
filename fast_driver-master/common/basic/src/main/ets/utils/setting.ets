import preferences from '@ohos.data.preferences'
import { TOKEN_KEY, USER_SETTING, USER_SETTING_AD } from '../constants'
import { AdvertClass } from '../models'

// 默认广告选项
const defaultAd: AdvertClass = {
  showAd: true,
  adTime: 5,
  adImg: $r('app.media.start')
}

export class UserSettingClass {
  context: Context
  constructor(context: Context) {
    this.context = context
  }
  // 获取存储用户信息的首选项仓库
  getStore () {
    //this.context 参数是为了在获取首选项仓库时传递当前上下文的信息，以确保首选项的读写操作能够在正确的环境下进行。
    return preferences.getPreferences(this.context, USER_SETTING)
  }
  // 设置用户广告设置
  async setUserAd (ad: AdvertClass) {
    const adStore = await this.getStore()
    await adStore.put(USER_SETTING_AD, JSON.stringify(ad))  //将ad转成字符串存储到首选项中
    await adStore.flush() //flush() 方法用于强制将内存中的修改写入到物理存储介质中，以确保数据的持久性。
  }
  // 获取广告配置
  async getUserAd (): Promise<AdvertClass> {
    const  adStore = await this.getStore()
    //从用户首选项中获取键为 USER_SETTING_AD 的值，如果在首选项中找到了该键对应的值，则将其作为字符串返回；如果未找到，则返回默认的广告配置信息
    return  JSON.parse(await adStore.get(USER_SETTING_AD, JSON.stringify(defaultAd)) as string) as AdvertClass
  }

  //存储token
  async setUserToken (token: string) {
    const adStore = await this.getStore()
    await adStore.put(TOKEN_KEY, token)
    // 写入磁盘
    await adStore.flush()
  }
  //获取token
  async getUserToken (): Promise<string> {
    const adStore = await this.getStore()
    return await adStore.get(TOKEN_KEY, "") as string
  }


}

