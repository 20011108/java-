import { TabClass } from '@hm/basic'
import My from './My/My'

@Entry
@Component
struct Index {

  @State @Watch("updateName") currentIndex: number = 0 // 收到Tabs索引后刷新页面
  @Provide currentName: string = ""

  updateName () {
    this.currentName = this.tabsData[this.currentIndex].name
  }


  @State tabsData: TabClass[] = [{
    title: '任务',
    name: 'task',
    icon: $r("app.media.ic_tab_btn_task")
  },{
    title: '消息',
    name: 'message',
    icon: $r("app.media.ic_tab_btn_mess_nor")
  },{
    title: '我的',
    name: 'my',
    icon: $r("app.media.ic_tab_btn_mine_nor")
  }]
  @Builder
  getTabBar(item: TabClass) {
    Column() {
      Image(item.icon).width(22).height(22)                                            //橙色                             灰色
        .fillColor(item.name === this.tabsData[this.currentIndex].name ? $r('app.color.primary') : $r('app.color.text_secondary'))
      Text(item.title)
        .fontSize(12)
        .fontWeight(400)
        .margin({ top: 5 })
        .fontColor(item.name === this.tabsData[this.currentIndex].name ? $r('app.color.primary') : $r('app.color.text_secondary'))
    }.alignItems(HorizontalAlign.Center)
  }
  build() {
    Tabs({ barPosition: BarPosition.End, index: $$this.currentIndex }){  //index将Tabs的索引双向绑定给currentIndex
      ForEach(this.tabsData, (item: TabClass) => {  //数组里有三条数据，循环三次，所以有三个TabContent()
        TabContent(){
          if(item.name === 'task') {
            Text("任务组件")
          }
          else if(item.name === 'message') {
            Text("消息组件")
          }
          else {
            My()
          }
        }.tabBar(this.getTabBar(item))
      })
    }
  }
}