import { Request } from '@hm/basic'
import { LoginFormModel, UserInfoModel } from '../models'
// 登录接口实现
export const login = (data: LoginFormModel) => {
  //Request.post<string>这里面的string相当于传入的泛型数据，指代返回的data是个string
  return Request.post<string>("/login/account", data)  //调用封装的Request类
}


// 获取用户信息
export const getUserInfo = () => {
  return Request.get<UserInfoModel>("/users")
}