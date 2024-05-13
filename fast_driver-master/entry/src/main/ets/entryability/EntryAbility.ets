import { AbilityConstant, UIAbility, Want } from '@kit.AbilityKit';
import { hilog } from '@kit.PerformanceAnalysisKit';
import { window } from '@kit.ArkUI';
import { AdvertClass, TOKEN_KEY, UserSettingClass } from '@hm/basic/Index';

export default class EntryAbility extends UIAbility {
  onCreate(want: Want, launchParam: AbilityConstant.LaunchParam): void {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onCreate');
  }

  onDestroy(): void {
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onDestroy')
  }

  async onWindowStageCreate(windowStage: window.WindowStage): Promise<void>  {
    // Main window is created, set main page for this ability
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageCreate');
    // 这里要尝试去读一下运营的配置-我们现在还没有实现接口，直接模拟一下
    const userSetting = new UserSettingClass(this.context)  //this.context 获取当前环境或上下文的信息传递给对象
    const result = await new Promise<AdvertClass>((resolve, reject) => {
      setTimeout(() => {
        resolve({
          showAd: true,
          adTime: 5,
          adImg: $r("app.media.start")
        } as AdvertClass)
      }, 500)
    })
    await userSetting.setUserAd(result) // 写入首选项
    if (result.showAd) {
      const win = await windowStage.createSubWindow('ad_window')  //创建子窗口
      await win.showWindow()   //加载子窗口
      win.setUIContent("pages/Start/Start")   //为当前的子窗口加载具体页面内容，使用Promise异步回调。
    }
    const token = await userSetting.getUserToken()
    if (token) {
      //将 token 存储到应用程序的存储中，如果已存在相同键（TOKEN_KEY）的数据，则更新该数据，否则创建新的键值对存储该令牌。
      AppStorage.setOrCreate(TOKEN_KEY, token)
      windowStage.loadContent('pages/Index/Index');  //加载一个指定路径下的页面内容或其他资源到窗口中显示。
    }else {
      windowStage.loadContent('pages/Login/Login');
    }
  }

  onWindowStageDestroy(): void {
    // Main window is destroyed, release UI related resources
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onWindowStageDestroy');
  }

  onForeground(): void {
    // Ability has brought to foreground
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onForeground');
  }

  onBackground(): void {
    // Ability has back to background
    hilog.info(0x0000, 'testTag', '%{public}s', 'Ability onBackground');
  }
}
