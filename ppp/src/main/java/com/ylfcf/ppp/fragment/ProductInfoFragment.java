package com.ylfcf.ppp.fragment;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.ui.BorrowDetailZXDActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ��Ʒ����--��Ŀ����
 * @author Administrator
 *
 */
public class ProductInfoFragment extends BaseFragment{
	private BorrowDetailZXDActivity borrowDetailActivity;
	private TextView zjytTV;//�ʽ���;
	private TextView rzjsTV;//���ʷ�����
	private TextView dbcsTV;//������ʩ
	private TextView tzldTV;//	Ͷ������
	private BorrowDetailZXDActivity detailsActivity;
	private ProjectInfo projectInfo;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		borrowDetailActivity = (BorrowDetailZXDActivity) getActivity();
		View view = inflater.inflate(R.layout.productinfo_fragment, container, false);
		findViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void findViews(View view){
		zjytTV = (TextView)view.findViewById(R.id.production_fragment_zjyt);
		rzjsTV = (TextView)view.findViewById(R.id.production_fragment_rzjs);
		dbcsTV = (TextView)view.findViewById(R.id.production_fragment_dbcs);
		tzldTV = (TextView)view.findViewById(R.id.production_fragment_tzld);
		detailsActivity = (BorrowDetailZXDActivity) getActivity();
		detailsActivity.setOnProductInfoListener(new OnProductInfoListener() {
			@Override
			public void back(ProjectInfo info) {
				projectInfo = info;
				zjytTV.setText(Html.fromHtml(info.getCapital()));
				rzjsTV.setText(Html.fromHtml(info.getIntroduced()));
				dbcsTV.setText(Html.fromHtml(info.getMeasures()));
				tzldTV.setText(Html.fromHtml(info.getInvest_point()));
			}
		});
	}
	
	public interface OnProductInfoListener{
		public void back(ProjectInfo info);
	}
}
