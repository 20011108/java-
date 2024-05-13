export interface LoginForm {
  /** 登录账号 */
  account: string;
  /** 登录密码 */
  password: string;
}

export interface UserInfoModel {
  /**
   * 头像
   */
  avatar: string;
  /**
   * 姓名
   */
  name: string;
  /**
   * 司机编号
   */
  number: string;
  /**
   * 手机号
   */
  phone: string;
}

export class LoginFormModel implements LoginForm {
  account: string = ''
  password: string = ''

  constructor(model: LoginForm) {
    this.account = model.account
    this.password = model.password
  }
}
