package com.shengrui.huilian.xinge_receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shengrui.huilian.main_sideslip.indent_details.Specific_indent.Specific_indent_details;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageReceiver extends XGPushBaseReceiver {

	//注册结果
	@Override
	public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult xgPushRegisterResult) {
		// TODO Auto-generated method stub
		if (context == null || xgPushRegisterResult == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = xgPushRegisterResult + "注册成功";
			// 在这里拿token
			String token = xgPushRegisterResult.getToken();
		} else {
			text = xgPushRegisterResult + "注册失败，错误码：" + errorCode;
		}
	}

	//反注册结果
	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
	}

	//设置标签结果
	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
	}

	//删除标签结果
	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key2")) {
					String value = obj.getString("key2");
					Log.d("------", "get custom value:" + value);

				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	//通知被打开触发的结果
	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
	/*	if (context == null || xgPushClickedResult == null) {
			return;
		}
		String text = "";
		if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			text = "通知被打开 :" + xgPushClickedResult;
		} else if (xgPushClickedResult.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			text = "通知被清除 :" + xgPushClickedResult;
		}
		Toast.makeText(context, "广播接收到通知被点击:" + xgPushClickedResult.toString(),
				Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = xgPushClickedResult.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				String indentId = obj.getString("indentId");
				String progress = obj.getString("progress");
				Log.d("---------------------","get custom value:" + indentId);
			//	startActivity(new Intent(getActivity(), Specific_indent_details.class).putExtra("indentId", indentId).putExtra("progress", "1"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。*/
	}

	//通知被展示触发的结果，可以在此保存APP收到的通知
	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

	}

}
