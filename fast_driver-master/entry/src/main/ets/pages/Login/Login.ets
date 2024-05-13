import { TOKEN_KEY, UserSettingClass } from '@hm/basic/Index'
import {LoginFormModel} from '../../models'
import  router  from '@ohos.router'
import { login } from '../../api'

/*
 * 账号:yyyyyy
 * 密码:123456
 * */
@Entry
@Component
struct Login {
  //控制登录的加载动画
  @State showLoading: boolean = false

  @State accountForm:LoginFormModel = new LoginFormModel({
    account:'',
    password:''
  })

  //判断账号密码是否为空
  getFormValidate(){
    if (this.accountForm.account && this.accountForm.password) {
      return true
    }else {
      return false
    }
  }

  // 调用登录接口
  async login() {
    try {
      this.showLoading = true
      const token = await login(this.accountForm)
      AppStorage.setOrCreate(TOKEN_KEY, token) // 写入全局状态
      new UserSettingClass(getContext(this)).setUserToken(token) // 存入首选项
      this.showLoading = false
      router.replaceUrl({
        url: '/pages/Index/Index'.slice(1)
      })
    }catch (error) {

    } finally {
      this.showLoading = false
    }

  }



  build() {
    Column() {
      // 顶部标题
      Text("小时达").fontColor($r('app.color.text_primary')).fontSize(18).height(25)
      // 账号登录
      Row() {
        Text('账号登录').fontColor($r('app.color.text_primary')).fontSize(24).fontWeight(FontWeight.Bold)
        Row() {
          Text("账号登录").fontColor($r('app.color.primary')).fontSize(16).fontWeight(FontWeight.Bold)
          Image($r("app.media.ic_angle")).width(10).height(10).margin({ left: 5 })
        }
      }
      .width('100%')
      .justifyContent(FlexAlign.SpaceBetween)
      .margin({ top: 50, bottom: 50 })

      // 用户名输入框                        这里不能使用$$ 因为$$只支持单层数据的绑定，出现嵌套的情况还得使用监听方法
      TextInput({ placeholder: '请输入账号',text:this.accountForm.account })
        .loginStyle()
        .onChange((value)=>{
          this.accountForm.account = value
        })
        .onSubmit(()=>{
          if (this.getFormValidate()) {
            this.login()
          }
        })

      // 密码框
      TextInput({ placeholder: '请输入密码',text:this.accountForm.password })
        .loginStyle()
        .type(InputType.Password)// 密码框
        .showPasswordIcon(true) // 显示密码按钮
        .onChange((value)=>{
          this.accountForm.password = value
        })
        .onSubmit(()=>{
          if (this.getFormValidate()) {
            this.login()
          }
        })

      // 登录按钮  type:按钮类型 可设置按钮形状。  stateEffect 是否开启按压显示效果(这里根据条件判断点击反馈)
      Button({ type: ButtonType.Capsule,stateEffect:this.getFormValidate() }) {
        Row() {
          if (this.showLoading) {
            //加载动画
            LoadingProgress()
              .width(20)
              .height(20)
              .margin({ right: 12 })
              .color($r('app.color.white'))
          }
          Text('登录')
            .fontColor($r('app.color.white'))
            .onClick(()=>{
              this.login()
            })
        }
      }
      //条件判断使用何种颜色
      .backgroundColor(this.getFormValidate()?$r('app.color.primary'):$r('app.color.primary_disabled'))
      .width('100%')
      .height(50)
      .margin({ top: 50 })
    }
    .padding({ left: 32, right: 32 })
    .margin({ top: 40 })
  }

  @Styles
  loginStyle() {
    .backgroundColor('#fff')
    .border({ color: $r('app.color.background_divider'), width: { bottom: 1 } })
    .width('100%')
    .height(58)
    .borderRadius(0)
  }
}