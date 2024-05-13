import { BASE_URL, TOKEN_KEY } from "../constants"
import http from '@ohos.net.http'; // 原生的请求地址
import { promptAction, router } from '@kit.ArkUI';
import { UserSettingClass } from '.';
import { ResponseData } from '../models'

// 请求工具 get/put/delete/post/patch
// url 类型 参数
// 任何对象都可以as object
async function requestHttp<T>(url: string = "", method: http.RequestMethod = http.RequestMethod.GET, data?: object): Promise<T> {
  // 创建一个请求对象- 如果你想用单例 那么请不要在请求时销毁
  const httpRequest = http.createHttp()
  let urlStr = BASE_URL + url
  // 官方文档上写的是ok的，但是运行有问题，虽然说了 如果get类型会进行拼接地址，但是实际并没有
  if (method === http.RequestMethod.GET) {
    //Object.keys(data) 获取 data 对象的所有键并组成一个新数组
    if (data && Object.keys(data).length) {  //检查 data 是否存在且不为空。
      urlStr += "?" + Object.keys(data).map(key => {
        return `${key}=${data[key]}`  //每个键值对转换为 key=value 的格式  a=1&b=2&c=3  注意只有对象才能用data[key]的形式获取value。如果是数组则用data[index]
      }).join("&")  //使用 join("&") 方法将所有的键值对用 "&" 符号连接起来，
    }
  }
  // 组装参数
  const config: http.HttpRequestOptions = {
    extraData: method !== http.RequestMethod.GET ? data : "", // 当请求类型不等于get时 将data赋给extraData
    header: {
      "Content-Type": "application/json",
      // 如果 AppStorage.get(TOKEN_KEY) 返回的值为假（如 null、undefined 或空字符串），则使用空字符串 "" 作为默认值。
      Authorization: AppStorage.get(TOKEN_KEY) || "", // 设置请求头的token 每个接口都有。
    },
    method,
    // expectDataType:http.HttpDataType.OBJECT,
    readTimeout: 10000 // 如果多少秒没响应就断开
  }
  try {
    // 发请求
    const result = await httpRequest.request(urlStr, config) // 发请求
    // 这里要处理一些异常
    // 判断状态码 http状态码 业务状态码
    if (result.responseCode === 401) {
      // 401 就表示token失效、超时或者没传。移动端产品应该去做无感刷新
      // token refresh_token => 换取新的token => 重新发请求
      // 提示错误 -删除目前的token 跳到登录页
      promptAction.showToast({ message: '登录失效' })
      // 删除token （要删两个地址，首选项和全局状态）
      new UserSettingClass(getContext()).setUserToken("") // 清空首选项token
      AppStorage.set(TOKEN_KEY, "") // 只能设置 不能删除
      router.replaceUrl({
        url: 'pages/Login/Login' // 跳转到登录页
      })
      // Hap har
      return Promise.reject(new Error("登录失效"))
    }
    else if (result.responseCode === 404) {
      promptAction.showToast({ message: '请求地址错误' })
      return Promise.reject(new Error("请求地址错误"))
    } else {
      //接收返回值，判断业务状态码为200，成功
      const res = JSON.parse(result.result as string) as ResponseData<T> // { code, data, msg }
      // const res = result.result as ResponseData<T> 如果加上 expectDataType:http.HttpDataType.OBJECT 就可以这么写
      if (res.code === 200) {
        // 才认为这次请求真的成功了
        return res.data as T
      } else {
        // 业务错了
        promptAction.showToast({ message: "服务器异常" }) //  res.msg没处理好 有可能出来一堆服务器语言
        return Promise.reject(new Error(res.msg)) // 业务错误 请求终止
      }
    }
  } catch (error) {
    promptAction.showToast({ message: error.message }) //  res.msg没处理好 有可能出来一堆服务器语言
    return Promise.reject(error) // 业务错误 请求终止
  }

}

export class Request {
  static get<T>(url: string, data?: object): Promise<T> {
    return requestHttp<T>(url, http.RequestMethod.GET, data)
  }

  static post<T>(url: string, data?: object): Promise<T> {
    return requestHttp<T>(url, http.RequestMethod.POST, data)
  }

  static delete<T>(url: string, data?: object): Promise<T> {
    return requestHttp<T>(url, http.RequestMethod.DELETE, data)
  }

  static put<T>(url: string, data?: object): Promise<T> {
    return requestHttp<T>(url, http.RequestMethod.PUT, data)
  }
}
