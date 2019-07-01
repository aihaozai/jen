package com.example.haozai.jen.Address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haozai.jen.R;
import com.example.haozai.jen.Utils.WebService;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 点击item展开隐藏部分,再次点击收起
 * 只可展开一条记录
 *
 */
public class OneExpandAdapter extends BaseAdapter {
	private Context context;
	private List<UserAddress> addresslist;
	private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
	private DeleteInterface deleteInterface;
	int tag;
	String a="";
	public OneExpandAdapter(Context context) {
		this.context = context;
	}
	public void setAddressList(List<UserAddress> addresslist) {
		this.addresslist = addresslist;
		notifyDataSetChanged();
	}

	public void setDeleteInterface(DeleteInterface deleteInterface) {
		this.deleteInterface = deleteInterface;
	}


	@Override
	public int getCount() {
		return addresslist == null ? 0 : addresslist.size();
	}

	@Override
	public Object getItem(int position) {
		return addresslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adddress_listview, parent, false);
			holder = new ViewHolder();
			holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
			holder.tv_Phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_detail = (TextView) convertView
					.findViewById(R.id.tv_detail);
			holder.default_tvs = (TextView) convertView
					.findViewById(R.id.red_default);
			holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);
			holder.lin_edit = (LinearLayout) convertView.findViewById(R.id.lin_edit);
			holder.lin_delete = (LinearLayout) convertView.findViewById(R.id.lin_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final UserAddress addlist = addresslist.get(position);

		// 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
		holder.showArea.setTag(position);
		holder.default_tvs.setTag(position);
		holder.tv_Phone.setText(addlist.getPnumber());
		holder.tv_detail.setText(addlist.getDetail());
		holder.tv_name.setText(addlist.getMing());
		holder.tv_address.setText(addlist.getAddress());

		// 通过 tag 来防止图片错位
		int pos=(int)holder.default_tvs.getTag();
		if(addlist.getKeynum()==1&&pos==position){
			holder.default_tvs.setText("默认");
		}else {
			holder.default_tvs.setText("");
			Log.i(TAG,"gggggggggggggg");
		}
        final ViewHolder finalHolder = holder;
		try{
			a = String.valueOf(addlist.getSid());
		}catch (NumberFormatException e){
			e.printStackTrace();
		}
		holder.lin_edit.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UserAddress addlists = addresslist.get(tag);
						Intent intent = new Intent(context,EditAddress.class);

						String phone = addlists.getPnumber();
						String detail = addlists.getDetail();
						String name = addlists.getMing();
						String address = addlists.getAddress();
						int sid = addlists.getSid();
						intent.putExtra("phone", phone);
						intent.putExtra("sid",sid);
						intent.putExtra("name", name);
						intent.putExtra("address", address);
						intent.putExtra("detail", detail);
						intent.putExtra("keynum", addlists.getKeynum());

						context.startActivity(intent);
					}
				}
		);

		holder.lin_delete.setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						AlertDialog alert = new AlertDialog.Builder(context).create();
						alert.setTitle("操作提示");
						alert.setMessage("您确定要将这些商品从购物车中移除吗？");
						alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
						alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										deleteInterface.childDelete(position);//删除
										new Thread() {
											public void run() {
												String end_result = WebService.DeleteAddress(a);
												currentItem = -1;
											};
										}.start();
									}
								});
						alert.show();

				notifyDataSetChanged();
					}
				}
		);
		//根据 currentItem 记录的点击位置来设置"对应Item"的可见性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
		if (currentItem == position) {

			holder.hideArea.setVisibility(View.VISIBLE);

		} else {
			holder.hideArea.setVisibility(View.GONE);
		}

		holder.showArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				//用 currentItem 记录点击位置
				 tag = (Integer) view.getTag();
				if (tag == currentItem) { //再次点击
					currentItem = -1; //给 currentItem 一个无效值

				} else {
					currentItem = tag;
				}
				//通知adapter数据改变需要重新加载
				notifyDataSetChanged(); //必须有的一步
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		private LinearLayout showArea;
		private TextView tv_Phone;
		private TextView tv_address;
		private TextView tv_detail;
		private TextView tv_name;
		private TextView default_tvs;
		private LinearLayout hideArea,lin_edit,lin_delete;
	}
	public interface DeleteInterface {
		/**
		 * 删除子item
		 *
		 * @param position
		 */
		void childDelete(int position);
	}
}
