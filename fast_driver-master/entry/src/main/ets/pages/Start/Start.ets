import { UserSettingClass, AdvertClass } from '@hm/basic'
import { window } from '@kit.ArkUI'

@Entry
@Component
struct Start {
  //getContext(this)用于获取当前上下文的信息,传递给UserSettingClass的构造函数,以便在创建UserSettingClass实例时使用
  userSetting: UserSettingClass = new UserSettingClass(getContext(this))
  @State
  adObj: AdvertClass  = {
    showAd: false,
    adTime: 0
  }
  timer: number = -1
  closeWin () {
    //查找ad_window这个窗口，然后 destroyWindow 销毁这个窗口
    window.findWindow("ad_window").destroyWindow()
  }
  async aboutToAppear() {
    this.adObj = await this.userSetting.getUserAd()
    this.timer = setInterval(() => {
      if(this.adObj.adTime === 0) {
        clearInterval(this.timer)
        this.closeWin()
        return
      }
      this.adObj.adTime--
    }, 1000)
  }
  aboutToDisappear(): void {
    clearInterval(this.timer)
  }
  build() {
    Stack({ alignContent: Alignment.TopEnd }) {
      Image(this.adObj.adImg).objectFit(ImageFit.Cover) //ImageFit.Cover 表示将图像等比例缩放，使其完全覆盖容器，并裁剪超出容器部分的内容，以保持图像的宽高比不变。
      Text(`${this.adObj.adTime}秒后跳过`)
        .padding({ left: 10, right: 10 })
        .margin({ right: 20, top: 20 })
        .height(30)
        .fontSize(14)
        .borderRadius(15)
        .backgroundColor($r("app.color.background_page"))
        .textAlign(TextAlign.Center)
        .onClick(() => {
          this.closeWin()
        })

    }.height('100%').width('100%')
  }
}
