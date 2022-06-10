## exmaple:

```kotlin
无UI
//初始化
QLive.init(context ,token);
QLive.updateUserInfo(
    "your avatar",
    "your nickname",
    HashMap<String, String>().apply {
        put("vip","1"); //自定义vip等级
        put("level","22");//扩展用户等级
    },
    object : QLiveCallBack<Void>{
        override fun onSuccess(data: Void) {}
        override fun onError(code: Int, msg: String) {}
    });
 
// 主播推流
//创建推流client
val client = QLive.createPusherClient();
//启动麦克风模块
client.enableAudio(MicrophoneParams().apply { mSampleRate = 48000 });
//启动摄像头模块
client.enableVideo(CameraParams().apply {fps=15  });
//本地预览
client.setLocalPreView(findViewById(R.id.QTextureView));
   
//注册客户端监听
client.setClientEventListener(object: QClientEventListener{})
//加入房间
client.joinRoom( roomId, object : QLiveCallBack<QLiveRoomInfo> {
    override fun onSuccess(data: QLiveRoomInfo){}
    override fun onError(code: Int, msg: String) {}
});
//关闭
client.closeRoom();
//销毁
client.destroy();
 
 
 
//用户拉流房间
val client = QLive.createPullerClient();
 
//设置本地预览
client.setPlayer(findViewById(R.id.QPLPlayer));
 
//注册客户端监听
client.setClientEventListener(object: QClientEventListener{})
 
//加入房间
client.joinRoom( roomId, object : QLiveCallBack<QLiveRoomInfo> {
    override fun onSuccess(data: QLiveRoomInfo) {}
    override fun onError(code: Int, msg: String) {}
});
 
//离开房间
client.leaveRoom(object : QLiveCallBack<Void> {
    override fun onSuccess(data: Void) {}
    override fun onError(code: Int, msg: String) {}
});
 
//关闭
client.destroy(); 
```



```kotlin
//UIKIT
//初始化
QLive.init(context ,token);
QLive.updateUserInfo(
         "your avatar",
         "your nickname",
         HashMap<String, String>().apply {
             put("vip","1"); //自定义vip等级
             put("level","22");//扩展用户等级
          },
         object : QLiveCallBack<Void>{
             override fun onSuccess(data: Void) {}
             override fun onError(code: Int, msg: String) {}
         });
//配置UI (可选);
roomUIKit = QLive.createLiveRoomUIKit();

val roomComponentsTable = roomUIKit.getRoomComponentsTable();
//每个内置UI组件都可以配置自己的替换实现
roomComponentsTable.mXXXComponent.setReplaceView(CustomView.Class);
           
//每个内置UI组件都可以禁用
roomComponentsTable.mXXXComponent.setIsEnable(false);
           
//如果使用使用某个槽位 每个UI组件可以定制样式
roomComponentsTable.mRoomNoticeComponent.showNoticeCall={ notice->
    //比如定制公告颜色和文字
    "<font color='#ffffff'> 今天的公告: ${notice}</font>"
}

//自定义主播头像点击事件
roomComponentsTable.mRoomHostComponent.clickCall=
    object: ViewClickWrap<QLiveUser> { kitContext, client, user:QLiveUser,view ->           
     //跳转到主播主页
     // 主播ID  -> user.uid
     // 主播头像 -> user.avatar              
   }       
     
//插入全局覆盖层
roomComponentsTable.mOuterCoverComponent.setReplaceView(CustomView::clas.java)
   
//跳转到直播列表页面
roomUIKit.launch(context);
 
//可选 配置直播列表样式
roomUIKit.mRoomListComponent.itemAdapterComponent = object : ItemAdapterComponent<QLiveRoomInfo>{
     override fun createAdapter(  context:KitContext,  client:QLiveRoomClient): RecyclerviewAdapter<QLiveRoomInfo, ViewHolder>{
          //创建自己的列表适配器
     }
}
//如果需要将直播列表
roomListView =  roomUIKit.mRoomListComponent.create(context);
//添加到自己想要的地方
addView(roomListView);

```

```java
class QUserInfo {
    String avatar;
    String nickName;
    HashMap<String,String> extensions;
}
```


## 初始化
```java
class QLive {
    static init(Context context, String token, QLiveCallBack<Void> callBack);  // 初始化
    static updateUser(QUserInfo userInfo ,QLiveCallBack<Void> callBack);       //绑定用户信息
    static QPusherClient createPusherClient();                                 //创建主播端
    static QPlayerClient createPlayerClient();                                 //创建观众端
    static QLiveUIKit createLiveUIKit();                                       //创建uikit
}

class QLiveUIKit {
    RoomListComponent getRoomListComponent();                                  //房间列表组件
    RoomComponentsTable getRoomComponentsTable();                                //房间页面的组件表
    static void launch(Context context);                                       //启动 跳转直播列表页面
}
```


## 主播观众客户端

```java
class QPusherClient {
    void registerService(List<Class<? extends QNLiveService>> serviceClass);     //注册用户需要的服务
    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setClientEventListener(QClientEventListener clientListener);              //房间事件监听
    void joinRoom( String roomId, QLiveCallBack<QLiveRoomInfo> callBack);                //加入房间
    void closeRoom( QLiveCallBack<Void> callBack);                                //关闭房间
    void destroy();                                                               //销毁

    void setMediaEventListener(QMediaEventListener mediaEventListener);             //媒体事件监听
    void enableCamera(QCameraParams cameraParams);                                 //启动视频采集
    void enableMicrophone(QMicrophoneParams microphoneParams);                     //启动麦克参数
    void switchCamera();                                                           //切换摄像头
    void setLocalPreView(QRenderView view);                                        //设置本地预览
    void muteLocalCamera(boolean muted);                                           //禁/不禁用本地视频流
    void muteLocalMicrophone(boolean muted);                                       //禁/不禁用本地麦克风流
    void setVideoFrameListener(QVideoFrameListener frameListener);                 //设置本地摄像头数据监听
    void setAudioFrameListener(QAudioFrameListener frameListener);                 //设置本地音频数据监听
}

class QPlayerClient {
    void registerService(List<Class<? extends QNLiveService>> serviceClass);     //注册用户需要的服务
    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setClientEventListener(QClientEventListener clientListener);              //房间事件监听
    void joinRoom( String roomId, QLiveCallBack<QLiveRoomInfo> callBack);                //加入房间
    void leaveRoom( QLiveCallBack<Void> callBack);                                //关闭房间
    void destroy();                                                               //销毁 

    void setPlayer(IPullPlayer player);                                      //绑定播放器
    IPullPlayer getPlayer();
}

interface QRoomEventListener {
    void onRoomEntering(String roomId,QLiveUser user);                         //正在加入房间
    void onRoomJoined(QRoomInfo roomInfo);                                     //加入了某个房间  
    void onRoomLeft();                                                          //离开了某个房间 
    void onRoomClosed();                                                        //关闭了直播 
    void onDestroyed();                                                         //client销毁
    void onLiveStatusChanged(QLiveStatus liveStatus);                          //直播间状态变化 业务状态
}

//直播状态枚举  低代码服务端定义的业务状态
enum QLiveStatus {
    LiveStatusPrepare, //直播准中 （已经创建）
    LiveStatusOn,      // 直播间已发布  （已经发布，可以开播和拉流
    LiveStatusAnchorOnline, //主播在线
    LiveStatusAnchorOffline, //主播离线
    LiveStatusOff            //直播间销毁
}

interface QMediaEventListener{
    void onConnectionStateChanged(QRoomConnectionState state);//rtc推流链接状态
    void onCameraStatusChange(boolean isOpen); 
    void onMicrophoneStatusChange(boolean isOpen);
}

class QMicrophoneParams {
    int mSampleRate = 48000;
    int mBitsPerSample = 16;
    int mChannelCount = 1;
    int mBitrate = 64000;
}
 
class QCameraParams {
    int width = 720;
    int height = 1280;
    int fps = 25;
    int bitrate = 1000;
}

class QLiveRoomInfo {
    String liveId;
    String title;
    String notice;
    String coverUrl;
    Map<String, String> extension;
    QLiveUser anchorInfo;
    String roomToken;
    String pkId;
    long onlineCount;
    long startTime;
    long endTime;
    String chatId;
    String pushUrl;
    String hlsUrl;
    String rtmpUrl;
    String flvUrl;
    Double pv;
    Double uv;
    int totalCount;
    int totalMics;
    int liveStatus;
    int anchorStatus;
}

class QLiveUser {
    String userId;
    String avatar;
    String nick;
    Map<String,String> extensions; //扩展字段
    String imUid;
}
```

## QLiveService

```java
interface QLinkMicService extends QLiveService {
   void removeServiceListener(QLinkMicServiceListener listener);
   void addServiceListener(QLinkMicServiceListener listener);
   List<QMicLinker> getAllLinker();
   void setUserPreview(String uid, QRenderView preview);
   void kickOutUser(String uid, String msg, QLiveCallBack<Void> callBack);
   void updateExtension(QMicLinker micLinker, Extension extension,QLiveCallBack<Void> callBack);
   QAudienceMicLinker getAudienceMicLinker();
   QAnchorHostMicLinker getAnchorHostMicLinker();
   QInvitationHandler getInvitationHandler();
}
 
interface QLinkMicServiceListener{
     onInitRoomLinkers(List<QMicLinker> linkers);
     onUserJoinLink(QMicLinker micLinker);
     onUserLeft(MicLinker micLinker);
     onUserMicrophoneStatusChange(MicLinker micLinker);
     onUserCameraStatusChange(MicLinker micLinker);
     onUserBeKick(MicLinker micLinker, String msg);
     onUserExtension(MicLinker micLinker, Extension extension);
}

class QAudienceMicLinker{
     removeListener(QLinkMicListener listener);
     addListener(QLinkMicListener listener);
     startLink(  HashMap<String,String> extensions,CameraParams cameraParams, MicrophoneParams microphoneParams, QLiveCallBack<Void> callBack  );
     stopLink();
     switchCamera();
     muteLocalVideo(boolean muted);
     muteLocalAudio(boolean muted);
     setVideoFrameListener(QVideoFrameListener frameListener);
     setAudioFrameListener(QAudioFrameListener frameListener);
    
    interface QLinkMicListener{
       onConnectionStateChanged(QRoomConnectionState state );
       onLocalRoleChange(boolean isLinker);
    }
}
```


```java
interface QPKService extends QLiveService{
    void removeServiceListener(QPKServiceListener pkServiceListener);
    void addServiceListener(QPKServiceListener pkServiceListener);
    void setMixStreamAdapter(QPKMixStreamAdapter mixAdapter);
    void updatePKExtension(Extension extension, QLiveCallBack<Void> callBack);
    void start(long timeoutTimestamp ,String receiverRoomId, String receiverUid, HashMap<String, String> extensions, QLiveCallBack<QPKSession> callBack);
    void stop(QLiveCallBack<Void> callBack);
    void setPeerAnchorPreView(QRenderView view);
    QInvitationHandler getInvitationHandler();
}

interface QPKServiceListener{
    void onStart(QPKSession pkSession);
    void onStop(QPKSession pkSession,int code,String msg);
    void onWaitPeerTimeOut(QPKSession pkSession);
    void onPKExtensionUpdate(QPKSession pkSession,Extension extension);
    void onInitGetRoomPKSession(QPKSession pkSession);
}
//pk混流适配
interface QPKMixStreamAdapter{
     MixStreamParams onPKMixStreamStart(QPKSession pkSession); //pk void>混流
     List<QMergeOption> onPKLinkerJoin(PKSession pkSession);
     List<QMergeOption> onPKLinkerLeft();
}

```

```java
class QInvitationHandler{
    void apply(long expiration, String receiverRoomId,String receiverUid, HashMap<String,String>extensions, QLiveCallBack<QInvitation> callBack);
    void cancelApply(int invitationId,QLiveCallBack<Void> callBack);
    void accept(int invitationId, HashMap<String,String> extensions,QLiveCallBack<Void> callBack);
    void reject(int invitationId, HashMap<String,String> extensions,  QLiveCallBack<Void> callBack );
    void removeInvitationHandlerListener(QInvitationHandlerListener listener);
    void addInvitationHandlerListener(QInvitationHandlerListener listener);
}

interface QInvitationHandlerListener{
    void onReceivedApply(QInvitation invitation);
    void onApplyCanceled(QInvitation invitation);
    void onApplyTimeOut(QInvitation invitation);
    void onAccept(QInvitation invitation);
    void onReject(QInvitation invitation);
}

class QInvitation{
     QLiveUser initiator;
     QLiveUser receiver;
     String initiatorRoomId;
     String receiverRoomId;
     HashMap<String,String> extensions;
     int linkType;
}
```
```java
class QMergeOption {
    String uid;
    CameraMergeOption  cameraMergeOption;
    MicrophoneMergeOption microphoneMergeOption;
 
    class CameraMergeOption  {
       boolean isNeed = true;
       int mX = 0;
       int mY = 0;
       int mZ = 0;
       int mWidth = 0;
       int mHeight = 0;
       QRenderMode mStretchMode;
     }
 
    class MicrophoneMergeOption  {
       boolean isNeed ;
    }
}
```

```java
interface QChatRoomService extends QLiveService {
     void removeServiceListener(QChatRoomServiceListener chatServiceListener);
     void addServiceListener(QChatRoomServiceListener chatServiceListener);
     void sendCustomC2CMsg(String msg, String memberId, QLiveCallBack<Void> callBack);
     void sendCustomGroupMsg(String msg, QLiveCallBack<Void> callBack);
     void kickUser(String msg, String memberId, QLiveCallBack<Void> callBack);
     void muteUser(boolean isMute ,String msg, String memberId, long duration ,QLiveCallBack<Void> callBack);
     void addAdmin( String memberId, QLiveCallBack<Void> callBack);
     void removeAdmin(String msg, String memberId, QLiveCallBack<Void> callBack);
}

interface QChatRoomServiceListener{
     void onUserJoin(String memberId);
     void onUserLeft(String memberId);
     void onReceivedC2CMsg(String msg, String fromId, String toId);
     void onReceivedGroupMsg(String msg, String fromId, String toId);
     void onUserBeKicked(String memberId);
     void onUserBeMuted(boolean isMute, String memberId, long duration);
     void onAdminAdd(String memberId);
     void onAdminRemoved(String memberId, String reason);
}
```

```java
interface QRoomService {
    void removeServiceListener(QRoomServiceListener listener);
    void addServiceListener(QRoomServiceListener listener);
    QLiveRoomInfo getRoomInfo();
    void refreshRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);
    void updateRoomExtension(Extension extension, QLiveCallBack<void> callBack);
    void getOnlineUser(QLiveCallBack<List<LiveUser>> callBack);
    static searchUserByUserId(String uid, QLiveCallBack<LiveUser> callBack);
    static searchUserByIMUid(String imUid, QLiveCallBack<LiveUser> callBack);
    static createRoom(QCreateRoomParam param, QLiveCallBack<QLiveRoomInfo> callBack);
    static deleteRoom(QDeleteRoomParam param, QLiveCallBack<void> callBack);
    static listRoom(QLiveRoomStatus status, int pageNumber, int pageSize, QLiveCallBack<List<QLiveRoomInfo>> callBack);
    static getRoomInfo(String roomId, QLiveCallBack<List<QLiveRoomInfo>> callBack);
}

interface QRoomServiceListener{
     void onRoomExtensions(Extension extension);
}
```

```java
interface QPublicChatService extends QLiveService{
     void addServiceLister(QPublicChatServiceLister lister);;
     void removeServiceLister(QPublicChatServiceLister lister);;
     void sendPublicChat(String msg, QLiveCallBack<PubChatModel> callBack);;
     void sendWelCome(String msg, QLiveCallBack<PubChatModel> callBack);;
     void sendByeBye(String msg, QLiveCallBack<PubChatModel> callBack);;
     void sendLike(String msg, QLiveCallBack<PubChatModel> callBack);;
     void sendCustomPubChat(String action, String msg,  QLiveCallBack<PubChatModel> callBack);;
     void pubMsgToLocal(PubChatModel chatModel);;
}

interface QPublicChatServiceLister {
     void onReceivePublicChat(PubChatModel model);;
}

class PubChatModel {
     String action;
     QLiveUser sendUser;
     String content;
     String senderRoomId;
     String getMsgAction();
}

```



## UIKIT

### 以下UI组件还未完全dom化

```


interface KitContext {
     Context getAndroidContext();
     FragmentManager getAndroidFragmentManager();
     FragmentActivity getCurrentActivity();
}

//用户自定义的UI必须继承这个
class BaseUIComponentView extends FrameLayout implements LifecycleEventObserver,QClientEventListener{
    void attach( KitContext context,QLiveClient  client );
}

//UI型号组件
class BaseUIComponent{
    <T extends BaseUIComponentView> void setReplaceView(Class<T> serviceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);
}

//功能型号组件 处理事件
class BaseFuncComponentHandler implements LifecycleEventObserver,QClientEventListener{
    void attach( KitContext context,QLiveClient  client );
}

//功能型号组件
class BaseFucComponent{
    <T extends BaseFuncComponentHandler> void setReplaceHandler(Class<T> serviceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);
}

//左上角房主
class RoomHostComponent extends BaseUIComponent(); {
    var mClickCall: ViewClickWrap<QLiveUser>? = null  //房主头像点击事件回调 提供点击事件自定义回调
    var showHostTitleCall: ((room: QLiveRoomInfo); -> String);?=null
    var showSubTitleCall:  ((room: QLiveRoomInfo); -> String);?=null
}

 // 右上角在线用户槽位
class OnlineUserComponent extends BaseUIComponent(); {
    var mItemAdapterWrap: ItemAdapterWrap<QLiveUser>? = null
    var mClickCall: ViewClickWrap<QLiveUser>? = null
}

 // 右上角房间人数 位置
class RoomMemberCountComponent extends BaseUIComponent(); {
    var mClickCall: ViewClickWrap<Unit>? = null
}

 // 右上角房间id 位置
class RoomIdComponent extends BaseUIComponent(); {
    //文本回调 默认显示房间ID
    var showTextCall:( (roomInfo:QNLiveRoomInfo);->String );?=null
}

 // 房间计时器槽位
class RoomTimerComponent extends BaseUIComponent(); {
     // @param time 秒
     // 时间格式化回调 默认"mm:ss"
     // 返回 格式化后html样式
    var showTextCall: ((time: Int); -> String);? = null
}

 // 弹幕槽位
class DanmakuTrackManagerComponent extends BaseUIComponent(); {
       // 弹幕轨道个数
      int getIDanmakuViewCount();;
       // 距离上一个轨道的上间距
      int topMargin();;
}

 // 公屏聊天
class CommonChatComponent extends BaseUIComponent(); {
     // 如何消息每个消息文本 适配
    var mItemAdapterWrap: ItemAdapterWrap<QLiveUser>? = null
     // 点击事件回调
    var mClickCall: ViewClickWrap<PubChatMode>? = null
}

 // 公告槽位
class RoomNoticeComponent extends BaseUIComponent();{
     // 自定义显示样式
    var showTextCall: ((notice: String); -> String);?=null
    //背景
    var backgroundView: Int = -1
}

 // 输入框\
class InputComponent extends BaseUIComponent();{
    var backgroundView: Int = -1
}

//底部菜单栏目
class BottomFucBarComponent extends BaseUIComponent(); {

    //主播菜单默认 发弹幕 美颜 关房间
    val mAnchorFuncMenus = ArrayList<BaseUIComponent>();.apply {
        add(SendDanmakuFucMenu(););
        add(ShowBeautyFucMenu(););
        add(CloseRoomFucMenu(););
    }
    //用户菜单默认 发弹幕 连麦 关房间
    val mAudienceFuncMenus = ArrayList<BaseUIComponent>();.apply {
        add(SendDanmakuFucMenu(););
        add(ApplyLinkFucMenu(););
        add(CloseRoomFucMenu(););
    }
}

 // 开播预览槽位
class LivePreViewComponent extends BaseUIComponent(); {

}

// 主播开始pk槽位置
class StartPKComponent extends BaseUIComponent();

///
主播pk预览对方两个小窗口
class PKAnhorPrewComponent extends BaseUIComponent();


// 连麦中的用户 槽位
class LinkerComponent extends BaseUIComponent();{
   var mItemAdapterWrap: ItemAdapterWrap<QMicLinker>? = null
    // 内置槽位 设置 点击事件回调
   var mClickCall: ViewClickWrap<QMicLinker>? = null
}
 // 房间背景图
class RoomBackGroundComponent extends BaseUIComponent(); {
    //默认背景图片
    var defaultBackGroundImg = R.drawable.kit_dafault_room_bg
}

// 主播收到pk邀请弹窗
class ShowReceivedPKApply : BaseFuncComponentHandler();

// 主播收到连麦申请弹窗
class ShowLinkMicApply : BaseFuncComponentHandler();


//主播列表
class RoomListComponent  {
   //
   var mItemAdapterWrap: ItemAdapterWrap<QMicLinker>? = null
   fun create();:View
}


class ViewSlotTable {
 
    val mRoomBackGroundSlot = RoomBackGroundSlot();
 
    /**
     * 房间左上角房主，房主槽位置
     */
    val mRoomHostSlot = RoomHostSlot();
 
    //开播准备
    val mLivePreViewSlot =LivePreViewSlot();
 
    /**
     * 右上角在线用户槽位
     */
    val mOnlineUserSlot = OnlineUserSlot();
    
    val mRoomMemberCountSlot = RoomMemberCountSlot();
    /**
     * 右上角房间id 位置
     */
    val mRoomIdSlot = RoomIdSlot();
 
    /**
     * 右上角房间计时器
     */
    val mRoomTimerSlot = RoomTimerSlot();
 
    /**
     * 弹幕槽位
     */
    val mDanmakuTrackManagerSlot = DanmakuTrackManagerSlot();
 
    /**
     *  公屏聊天
     */
    val mPublicChatSlot = PublicChatSlot();
 
    /**
     * 公告槽位
     */
    val mRoomNoticeSlot = RoomNoticeSlot();
 
    /**
     * 主播开始pk槽位置
     */
    val mStartPKSlot = StartPKSlot();
 
   //PK覆盖层
    val mPKCoverSlot = QNEmptyViewSlot();
   //pk主播两个小窗口
    val mPKAnchorPreviewSlot = PKAnchorPreviewSlot();
 
    /**
     *连麦中的用户 槽位
     */
    val mLinkerSlot = LinkerSlot();
     
     /**
     * 房间底部 输入框
     */
    val mInputSlot = InputSlot();
 
    /**
     *  右下角功能栏目
     */
    val mBottomFucBarSlot = BottomFucBarSlot();
 
    /**
     * 全局上层覆盖自定义 槽位
     * 空槽位
     */
    val mOuterCoverSlot = QNEmptyViewSlot();
 
    /**
     * 全局底层覆盖自定义 槽位
     * 空槽位
     */
    val mInnerCoverSlot = QNEmptyViewSlot();
 
     //主播收到连麦申请弹窗
    val mShowReceivedPKApply = ShowReceivedPKApply();
 
    //主播收到pk邀请弹窗
    val mShowLinkMicApply = ShowLinkMicApply();

}

```



